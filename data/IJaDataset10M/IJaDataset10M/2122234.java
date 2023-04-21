package net.confex.tree;

import java.io.File;
import java.lang.reflect.Method;
import net.confex.directedit.IPropertyDialog;
import net.confex.directedit.JavaPropertyDialog;
import net.confex.translations.Translator;
import net.confex.utils.FileClassLoader;
import net.confex.utils.MyURLClassLoader;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Node;

public class JavaNode extends TreeNode implements ICompositeProvider {

    public JavaNode(ConfigTree configTree, IStateObserver stateObserver) {
        super(configTree, stateObserver);
    }

    public String getAboutString() {
        return Translator.getString("ABOUT_JavaNode");
    }

    protected static String default_image_name = "java_file.gif";

    public String getDefaultImage() {
        return default_image_name;
    }

    public static String getDefaultImageName() {
        return default_image_name;
    }

    protected String getPropertiesXml(boolean read_src_text) {
        String str_xml = super.getPropertiesXml(read_src_text);
        if (src_text != null && !src_text.equals("")) {
            str_xml += "<java>\n" + Utils.toHtmlSpecialEntities(src_text) + "\n";
            str_xml += "</java>\n";
        }
        return str_xml;
    }

    protected void parsePropertyXml(Node property, boolean new_node) {
        super.parsePropertyXml(property, new_node);
        if (property.getNodeName().equals("java")) {
            Node nd = property.getFirstChild();
            String text = "";
            if (nd != null) text = nd.getNodeValue();
            this.setSrcText(Utils.fromHtmlSpecialEntities(text.trim()));
        } else if (property.getNodeName().equals("file_name")) {
            Node nd = property.getFirstChild();
            String text = "";
            if (nd != null) text = nd.getNodeValue();
            this.setSrcFileNameXml(Utils.fromHtmlSpecialEntities(text.trim()));
        }
    }

    public ITreeNode createNewITreeNode() {
        return new JavaNode(getConfigTree(), null);
    }

    public void setPropertyLike(ITreeNode prototype) {
        super.setPropertyLike(prototype);
        if (!(prototype instanceof JavaNode)) {
            System.err.println("[JavaNode.setPropertyLike] prototype NOT instanceof JavaNode!");
            return;
        }
    }

    protected IPropertyDialog newPropertyDialog(Shell shell) {
        readFromSrcFile();
        return new JavaPropertyDialog(shell, this);
    }

    public String getFullText() {
        String java_text = getChildrensText(this.getClass());
        return TreeUtils.doAllSubstitutions(this, java_text);
    }

    Composite m_parent = null;

    public void refreshComposite() {
        if (m_parent != null) {
            m_parent.layout();
            m_parent.redraw();
            System.out.println("refreshComposite()");
        }
    }

    public void disposeComposite() {
        m_parent = null;
    }

    /**
	 * ���� ����� ����� run()
	 * 
	 * @param parent
	 * @param viewPart
	 * @param monitor
	 */
    public void makeComposite(Composite parent, ViewPart viewPart, IProgressMonitor monitor) {
        m_parent = parent;
        try {
        } catch (Exception e) {
            System.err.println("[JavaNode.makeComposite()] " + e.getMessage());
            setErrorState();
        }
    }

    /**
	 * ������ ����������� ���� ���� ����� ����� main()
	 * 
	 * @param view
	 * @param monitor
	 * @return
	 */
    public IStatus run(IViewPart view, IProgressMonitor monitor) {
        if (monitor != null) {
            monitor.worked(1);
            monitor.subTask("reading src... " + 1);
            if (monitor.isCanceled()) return Status.CANCEL_STATUS;
        }
        readFromSrcFile();
        try {
            if (monitor != null) {
                monitor.worked(5);
                monitor.subTask("Bind java vars... " + 5);
                if (monitor.isCanceled()) return Status.CANCEL_STATUS;
            }
            if (monitor == null) setRunState();
            if (monitor != null) {
                monitor.worked(15);
                monitor.subTask("Evaluate java... " + 15);
                if (monitor.isCanceled()) return Status.CANCEL_STATUS;
            }
            doMain();
            if (monitor == null) setSuccessState();
            if (monitor != null) {
                monitor.done();
            }
        } catch (Exception e) {
            System.err.println("[JavaNode.run()] " + e.getMessage());
            setErrorState();
            return Status.OK_STATUS;
        }
        return Status.OK_STATUS;
    }

    public String getChildrensText(Class _interface) {
        String ret_str = "";
        if (_interface == this.getClass()) {
            readFromSrcFile();
            ret_str += src_text;
        }
        return ret_str += super.getChildrensText(_interface);
    }

    public String getSrcFileNameGui() {
        String nameInGui = super.getSrcFileNameGui();
        if (nameInGui.startsWith(File.pathSeparator + "src" + File.separator)) {
            nameInGui = nameInGui.substring(4);
        }
        return nameInGui;
    }

    public void setSrcFileNameGui(String file_name) {
        String nameInXml = file_name;
        if (!nameInXml.contains(File.pathSeparator)) {
            nameInXml = File.pathSeparator + "src" + File.separator + nameInXml;
        }
        super.setSrcFileNameGui(nameInXml);
    }

    String filename;

    String classname;

    String path_at_src;

    String path_at_class;

    File java_file;

    protected boolean setup_internal_vars() {
        java_file = getSrcFile();
        if (java_file == null || !java_file.exists()) {
            System.err.println("Src file not exist");
            return false;
        }
        filename = java_file.getName();
        classname = filename.substring(0, filename.length() - 5);
        String s = java_file.getAbsolutePath();
        int l = (int) (java_file.getAbsolutePath().length() - filename.length());
        path_at_src = java_file.getAbsolutePath().substring(0, l);
        path_at_class = path_at_src + ".." + File.separator + "classes" + File.separator;
        return true;
    }

    /**
	 * �������� ����� �� ������������� ���� �� �� �����������
	 * 
	 * ���� ���� ����������������� � ��� ����� ��������� ������ ��� �����
	 * ����������� ��������� �� ���������������
	 * 
	 * @return true - if Ok
	 */
    protected boolean compaleIfNeed() {
        if (!setup_internal_vars()) return false;
        File fclass = new File(path_at_class + classname + ".class");
        if (fclass.exists()) {
            if (java_file.lastModified() > fclass.lastModified()) return compile();
            return true;
        }
        compile();
        return true;
    }

    /**
	 * W! ����� ���������� ������ ����� setup_internal_vars()!
	 * 
	 * @return
	 */
    protected boolean compile() {
        String lp = "";
        File lib_dir = new File(getConfigTree().getConfexDir() + "lib" + File.separator);
        if (lib_dir.exists()) {
            final File[] files = lib_dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                final File file = files[i];
                if (file.getName().endsWith(".jar")) {
                    String path = file.getAbsolutePath();
                    System.out.println("Local CLASSPATH add jar " + path);
                    lp += path + File.pathSeparatorChar;
                }
            }
        }
        String[] args = new String[] { "-d", path_at_class, "-classpath", lp, java_file.getAbsolutePath() };
        System.out.println("before compile " + args[1] + "  " + args[2]);
        com.sun.tools.javac.Main m_javac = new com.sun.tools.javac.Main();
        int status = m_javac.compile(args);
        System.out.println("after compile");
        switch(status) {
            case 0:
                return true;
            case 1:
                System.err.println("Compile status: ERROR");
                return false;
            case 2:
                System.err.println("Compile status: CMDERR");
                return false;
            case 3:
                System.err.println("Compile status: SYSERR");
                return false;
            case 4:
                System.err.println("Compile status: ABNORMAL");
                return false;
            default:
                System.err.println("Compile status: Unknown exit status");
                return false;
        }
    }

    /**
	 * ��������� main ����� ������
	 * 
	 * W! ����� ���������� ������ ����� setup_internal_vars()!
	 */
    protected void doMain() {
        if (!compaleIfNeed()) {
            return;
        }
        Class testClass = null;
        try {
            MyURLClassLoader loader = new MyURLClassLoader(path_at_class);
            String s = getConfigTree().getConfexDir() + "classes" + File.separator;
            loader.addClasspath(s);
            File lib_dir = new File(getConfigTree().getConfexDir() + "lib" + File.separator);
            if (lib_dir.exists()) {
                final File[] files = lib_dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    final File file = files[i];
                    if (file.getName().endsWith(".jar")) {
                        String path = file.getAbsolutePath();
                        System.out.println("Local CLASSPATH add jar " + path);
                        loader.addClasspath(path);
                    }
                }
            }
            testClass = loader.loadClass(classname);
        } catch (Exception ex) {
            System.out.println("Load failed");
            ex.printStackTrace();
            return;
        }
        System.out.println("Loaded class " + testClass.getName());
        try {
            Method main = testClass.getMethod("main", new Class[] { String[].class });
            main.invoke(null, new Object[] { new String[0] });
        } catch (NoSuchMethodException e) {
            System.err.println("Method main not found in class " + classname + " !");
            return;
        } catch (Exception ex) {
            System.err.println("[JavaNode.doMain()] ");
            ex.printStackTrace();
        }
    }

    /**
	 * runs the �����.
	 * 
	 * W! ����� ���������� ������ ����� setup_internal_vars()!
	 */
    protected void doRun() {
        if (!compaleIfNeed()) {
            return;
        }
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
            System.err.println("Failed to instantiate");
            ex.printStackTrace();
        }
    }

    /**
	 * @param args
	 */
    public static void main2222(String[] args) {
    }
}
