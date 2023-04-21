package ie.dkit.java3Demulation.objects;

import java.util.Vector;

public class GraphicsObjectGroup extends Vector<GraphicsObject> implements GraphicsObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5821410087233159286L;

    private String m_Name;

    public String getName() {
        return m_Name;
    }

    public void setName(String name) {
        m_Name = name;
    }
}
