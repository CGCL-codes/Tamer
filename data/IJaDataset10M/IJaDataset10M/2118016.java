package herschel.phs.prophandler.tools.missionevolver.data;

import herschel.phs.prophandler.tools.missionevolver.gui.datatree.DataTree;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.tree.*;

public abstract class OidNode extends DefaultMutableTreeNode {

    protected static final Logger LOGGER = Logger.getLogger(OidNode.class.getName());

    /**
	 * 
	 */
    private static final long serialVersionUID = -7878912724737812509L;

    public static final String ROOT = "root";

    public static final String PROGRAM = "program";

    public static final String PROPOSAL = "proposal";

    public static final String OBSERVATION = "observation";

    public static final String P_SET = "pset";

    public static final String CONSTRAINT = "constraint";

    public static final String FILE = "file";

    public static final String PDF_FILE = "PDFfile";

    public static final String MISSION_CONFIGURATION = "missionConfig";

    public static final String EXCEPTION = "exception";

    public static final String CONTAINER = "container";

    protected int m_id;

    protected String m_name;

    protected InfoList m_dataList;

    protected String m_type;

    protected String m_program;

    protected boolean m_expanded = false;

    public OidNode(int id, String name, String type) {
        m_id = id;
        m_name = name;
        m_type = type;
    }

    public OidNode(int id, String type, String program, String name, InfoList dataList) {
        m_id = id;
        m_type = type;
        m_name = name;
        m_program = program;
        m_dataList = dataList;
    }

    public int getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public String getType() {
        return m_type;
    }

    public String getProgram() {
        return m_program;
    }

    public boolean getExpanded() {
        return m_expanded;
    }

    public InfoList getDataList() {
        return m_dataList;
    }

    public void setExpanded(boolean expanded) {
        m_expanded = expanded;
    }

    public String toString() {
        return m_name;
    }

    public abstract void expand(DataTree container);

    public abstract ImageIcon getIcon();
}
