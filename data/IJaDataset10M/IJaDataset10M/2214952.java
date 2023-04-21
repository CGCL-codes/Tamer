package org.virbo.jythonsupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.das2.util.filesystem.FileSystem;
import org.das2.util.monitor.NullProgressMonitor;
import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.core.PyTuple;
import org.python.util.InteractiveInterpreter;
import org.python.util.PythonInterpreter;
import org.virbo.datasource.DataSetURI;

/**
 *
 * @author jbf
 */
public class JythonUtil {

    /**
     * create an interpretter object configured for Autoplot contexts:
     *   * QDataSets are wrapped so that operators are overloaded.
     *   * a standard set of names are imported.
     *   
     * @param sandbox limit symbols to safe symbols for server.
     * @return PythonInterpreter ready for commands.
     * @throws java.io.IOException
     */
    public static InteractiveInterpreter createInterpreter(boolean sandbox) throws IOException {
        if (PySystemState.cachedir == null) {
            System.setProperty("python.cachedir", System.getProperty("user.home") + "/autoplot_data/pycache");
        }
        org.python.core.PySystemState pySys = new org.python.core.PySystemState();
        URL jarUrl = InteractiveInterpreter.class.getResource("/glob.py");
        if (jarUrl != null) {
            String jarFile = jarUrl.toString();
            if (jarFile.startsWith("jar:file:") && jarFile.contains("!")) {
                int i = jarFile.indexOf("!");
                String jar = jarFile.substring(9, i);
                File ff = new File(jar);
                if (ff.exists()) {
                    pySys.path.insert(0, new PyString(jar));
                } else {
                    String f = getLocalJythonLib();
                    pySys.path.insert(0, new PyString(f));
                }
            } else {
                String f = getLocalJythonLib();
                pySys.path.insert(0, new PyString(f));
            }
        } else {
            System.err.println("Not adding Lib stuff!!!  See https://sourceforge.net/tracker/index.php?func=detail&aid=3134982&group_id=199733&atid=970682");
        }
        InteractiveInterpreter interp = new InteractiveInterpreter(null, pySys);
        Py.getAdapter().addPostClass(new PyQDataSetAdapter());
        URL imports = JythonOps.class.getResource("imports.py");
        if (imports == null) {
            throw new RuntimeException("unable to locate imports.py on classpath");
        }
        interp.execfile(imports.openStream(), "imports.py");
        return interp;
    }

    private static String getLocalJythonLib() throws IOException {
        File ff2 = FileSystem.settings().getLocalCacheDir();
        File ff = new File(ff2.toString() + "/http/autoplot.org/jnlp-lib/jython-lib-2.2.1.jar");
        if (!ff.exists()) {
            System.err.println("looking for " + ff + ", but didn't find it.");
            System.err.println("doesn't seem like we have the right file, downloading...");
            File f = DataSetURI.getFile(new URL("http://autoplot.org/jnlp-lib/jython-lib-2.2.1.jar"), new NullProgressMonitor());
            ff = f;
        }
        System.err.println("   ...done");
        return ff.toString();
    }

    /**
     * check the script that it doesn't redefine symbol names like "str"
     * @param code
     * @return true if an err is suspected.
     */
    public static boolean pythonLint(URI uri, List<String> errs) throws IOException {
        LineNumberReader reader = null;
        File src = DataSetURI.getFile(uri, new NullProgressMonitor());
        try {
            reader = new LineNumberReader(new BufferedReader(new FileReader(src)));
            String vnarg = "\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*";
            Pattern assign = Pattern.compile(vnarg + "=.*");
            InteractiveInterpreter interp = createInterpreter(true);
            String line = reader.readLine();
            while (line != null) {
                Matcher m = assign.matcher(line);
                if (m.matches()) {
                    String vname = m.group(1);
                    try {
                        PyObject po = interp.eval(vname);
                        errs.add("" + reader.getLineNumber() + ": " + vname + "=" + po.__repr__());
                    } catch (PyException ex) {
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(JythonUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return errs.size() > 0;
    }

    public static class Param {

        public String name;

        public String label;

        public Object deft;

        public String doc;

        public List<Object> enums;

        /**
         * A (String) or F (Double) or R (URI)
         */
        public char type;
    }

    public static List<Param> getGetParams(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        StringBuilder build = new StringBuilder();
        Pattern getParamPattern = Pattern.compile("\\s*([_a-zA-Z][_a-zA-Z0-9]*)\\s*=\\s*getParam\\(\\.*");
        while (s != null) {
            Matcher m = getParamPattern.matcher(s);
            if (m.matches() || s.contains("getParam")) {
                build.append(s).append("\n");
            }
            s = reader.readLine();
        }
        reader.close();
        String params = build.toString();
        String myCheat = "def getParam( name, deflt, doc='', enums=[] ):\n  return [ name, deflt, doc, enums ]\n";
        String prog = myCheat + params;
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec(prog);
        PyObject locals = interp.getLocals();
        PyStringMap mlocals = (PyStringMap) locals;
        mlocals.__delitem__("getParam");
        List<Param> result = new ArrayList();
        PyList list = mlocals.items();
        for (int i = 0; i < list.__len__(); i++) {
            PyTuple o = (PyTuple) list.get(i);
            Param p = new Param();
            p.label = o.__getitem__(0).toString();
            if (p.label.startsWith("__")) continue;
            PyList oo = (PyList) o.__getitem__(1);
            p.name = oo.__getitem__(0).toString();
            p.deft = oo.__getitem__(1);
            p.doc = oo.__getitem__(2).toString();
            if (oo.__getitem__(3) instanceof PyList) {
                PyList pyList = ((PyList) oo.__getitem__(3));
                List<Object> enums = new ArrayList(pyList.size());
                for (int j = 0; j < pyList.size(); j++) {
                    enums.add(j, pyList.get(j));
                }
                p.enums = enums;
            }
            if (p.name.equals("resourceUri")) {
                p.name = "resourceURI";
            }
            if (p.name.equals("resourceURI")) {
                p.type = 'R';
                p.deft = p.deft.toString();
            } else {
                if (p.deft instanceof String) {
                    p.type = 'A';
                    p.deft = p.deft.toString();
                } else if (p.deft instanceof PyInteger) {
                    p.type = 'F';
                    p.deft = ((PyInteger) p.deft).__tojava__(int.class);
                } else if (p.deft instanceof PyFloat) {
                    p.type = 'F';
                    p.deft = ((PyFloat) p.deft).__tojava__(double.class);
                }
            }
            result.add(p);
        }
        return result;
    }

    /**
     * scrape script for local variables, looking for assignments.
     * @param script
     * @return
     */
    public static Map getLocals(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        Pattern assignPattern = Pattern.compile("\\s*([_a-zA-Z][_a-zA-Z0-9]*)\\s*=.*(#(.*))?");
        Pattern defPattern = Pattern.compile("def .*");
        boolean inDef = false;
        Map<String, String> result = new LinkedHashMap<String, String>();
        boolean haveResult = false;
        while (s != null) {
            if (inDef == false) {
                Matcher defm = defPattern.matcher(s);
                if (defm.matches()) {
                    inDef = true;
                }
            } else {
                if (s.length() > 0 && !Character.isWhitespace(s.charAt(0))) {
                    Matcher defm = defPattern.matcher(s);
                    inDef = defm.matches();
                }
            }
            if (!inDef) {
                Matcher m = assignPattern.matcher(s);
                if (m.matches()) {
                    if (m.group(3) != null) {
                        result.put(m.group(1), m.group(3));
                    } else {
                        result.put(m.group(1), s);
                    }
                }
            }
            s = reader.readLine();
        }
        reader.close();
        return result;
    }

    /**
     * return python code that is equivalent, except it has not side-effects like plotting.
     * This code is not exact, for example (a,b)= (1,2) is not supported.
     * @param reader input to read.
     * @return the script as a string, with side-effects removed.
     */
    public static String removeSideEffects(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        Pattern assignPattern = Pattern.compile("\\s*([_a-zA-Z][_a-zA-Z0-9]*)\\s*=.*(#(.*))?");
        Pattern defPattern = Pattern.compile("def .*");
        Pattern importPattern1 = Pattern.compile("from .*");
        Pattern importPattern2 = Pattern.compile("import .*");
        boolean inDef = false;
        StringBuilder result = new StringBuilder();
        boolean haveResult = false;
        while (s != null) {
            boolean sideEffect = true;
            if (inDef == false) {
                Matcher defm = defPattern.matcher(s);
                if (defm.matches()) {
                    inDef = true;
                    sideEffect = false;
                }
            } else {
                if (s.length() > 0 && !Character.isWhitespace(s.charAt(0))) {
                    Matcher defm = defPattern.matcher(s);
                    inDef = defm.matches();
                    if (inDef) sideEffect = false;
                }
            }
            if (!inDef) {
                Matcher m = assignPattern.matcher(s);
                if (m.matches()) {
                    sideEffect = false;
                } else if (importPattern1.matcher(s).matches()) {
                    sideEffect = false;
                } else if (importPattern2.matcher(s).matches()) {
                    sideEffect = false;
                }
            }
            if (!sideEffect) {
                result.append(s).append("\n");
            }
            s = reader.readLine();
        }
        reader.close();
        return result.toString();
    }
}
