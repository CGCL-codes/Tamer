package idlcompiler.gui;

import idlcompiler.files.IDLFile;
import java.awt.Component;

/**
 *
 * @author xhewe
 */
public class ProjectListComponent extends Component {

    IDLFile file;

    /** Creates a new instance of ProjectListComponent */
    public ProjectListComponent(IDLFile f) {
        file = f;
    }

    public String toString() {
        return file.getName();
    }

    public String getName() {
        return file.getName();
    }
}
