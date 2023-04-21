package net.confex.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Program that lets you run a short segment of Java code.
 *
 * @author Danila Master original author - Shawn Silverman
 */
public class JavaEval {

    /** The compiler object. */
    private com.sun.tools.javac.Main m_javac = new com.sun.tools.javac.Main();

    protected String m_main_text = "";

    protected String m_class_text = "";

    public void setMainText(String s) {
        m_main_text = s;
    }

    public void setClassText(String s) {
        m_class_text = s;
    }

    public String getMainText() {
        return m_main_text;
    }

    protected String m_import_text = "";

    public void setImportText(String s) {
        m_import_text = s;
    }

    public String getImportText() {
        return m_import_text;
    }

    public JavaEval() {
    }

    /**
     * Shows a message to the user.
     *
     * @param msg the message
     */
    private void showMsg(String msg) {
        System.err.println(msg);
    }

    public void compileAndExec() {
        try {
            doRun();
        } catch (Exception ex) {
            showMsg(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Compiles and runs the short code segment.
     *
     * @throws IOException if there was an error creating the source file.
     */
    private void doRun() throws IOException {
        String path_at_class = System.getProperty("user.dir") + File.separator;
        System.out.println("Debug: Path at classes: " + path_at_class);
        File f_user_dir = new File(path_at_class);
        File file = File.createTempFile("jav", ".java", f_user_dir);
        System.out.println(file.getAbsolutePath());
        file.deleteOnExit();
        String filename = file.getName();
        String classname = filename.substring(0, filename.length() - 5);
        PrintWriter out = new PrintWriter(new FileOutputStream(file));
        out.println("/**");
        out.println(" * Source created on " + new Date());
        out.println(" */");
        out.println(m_import_text);
        out.println("public class " + classname + "  implements Runnable  { ");
        if (!m_class_text.equals("")) {
            out.println(m_class_text);
        } else {
            out.println("    public  void run()  {");
            out.println("try {");
            out.println(m_main_text);
            out.println(" } catch(Exception ex)  {");
            out.println("    System.err.println(ex.getMessage());");
            out.println("     ex.printStackTrace();");
            out.println("    }");
            out.println(" } //-- end run");
        }
        out.println("} //-- End class");
        out.flush();
        out.close();
        String[] args = new String[] { "-d", path_at_class, file.getAbsolutePath() };
        System.out.println("before compile");
        int status = m_javac.compile(args);
        System.out.println("after compile");
        switch(status) {
            case 0:
                File fclass = new File(path_at_class + classname + ".class");
                fclass.deleteOnExit();
                try {
                    System.out.println("before main method invoke");
                    FileClassLoader loader = new FileClassLoader(path_at_class);
                    Class testClass = null;
                    try {
                        testClass = loader.loadClass(classname);
                    } catch (Exception ex) {
                        System.out.println("Load failed");
                        ex.printStackTrace();
                        return;
                    }
                    System.out.println("Loaded class " + testClass.getName());
                    try {
                        Runnable instance = (Runnable) testClass.newInstance();
                        instance.run();
                    } catch (Exception ex) {
                        showMsg("Failed to instantiate");
                        ex.printStackTrace();
                    }
                    System.out.println("after main method invoke");
                } catch (Exception ex) {
                    showMsg(ex.toString());
                }
                break;
            case 1:
                showMsg("Compile status: ERROR");
                break;
            case 2:
                showMsg("Compile status: CMDERR");
                break;
            case 3:
                showMsg("Compile status: SYSERR");
                break;
            case 4:
                showMsg("Compile status: ABNORMAL");
                break;
            default:
                showMsg("Compile status: Unknown exit status");
        }
    }
}
