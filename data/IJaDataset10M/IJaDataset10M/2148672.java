package jadaedor;

import jadacommon.operation.ReadXml;
import java.io.File;
import javax.swing.JInternalFrame;

/**
 *
 * @author Cris
 */
public class JadaProjectObj {

    private File path;

    private String name;

    private File projectPath;

    private String[] types = new String[] { "class", "attributes", "leguage" };

    private JadaInternalFrame frame;

    private static File workSpace = new File("./workspace");

    public JadaProjectObj(File source, JadaInternalFrame frame) {
        path = source;
        this.frame = frame;
        if (!source.isDirectory()) name = path.getName().substring(0, path.getName().length() - 4); else name = path.getName();
        File temp = source;
        while (!temp.getParentFile().equals(workSpace)) {
            temp = temp.getParentFile();
        }
        projectPath = temp;
    }

    public File getFile() {
        return this.path;
    }

    public File getProject() {
        return this.projectPath;
    }

    @Override
    public String toString() {
        return name;
    }

    public JInternalFrame open() {
        if (frame != null) {
            System.out.println(this.projectPath);
            frame.setFile(this.path);
            frame.setVisible(true);
            return frame;
        }
        return null;
    }

    public static JadaProjectObj createProjectObj(File source) {
        String ext = getExstension(source.getName());
        if (ext.equals("xml")) {
            String type = getXmlType(source);
            if (type.equals("class")) return new JadaProjectObj(source, new ClassEditorFrame()); else return new JadaProjectObj(source, new TextEditorFrame());
        }
        if (ext.equals("txt")) {
            return new JadaProjectObj(source, new TextEditorFrame());
        }
        if (ext.equals("py")) return new JadaProjectObj(source, new TextEditorFrame());
        return new JadaProjectObj(source, null);
    }

    private static String getExstension(String name) {
        int index = name.lastIndexOf(".");
        return name.substring(index + 1, name.length());
    }

    private static String getXmlType(File f) {
        ReadXml read = new ReadXml(f.getPath());
        return read.GetTagList().item(0).getNodeName();
    }
}
