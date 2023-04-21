package sourceforge.pebblesframewor.api.data;

import java.io.Serializable;

/**
 * Dimension
 * @author JunSun Whang
 * @version $Id: Dimension.java 135 2009-11-12 21:22:13Z junsunwhang $
 */
public interface Dimension extends Serializable {

    public String getDimensionName();

    public Hierarchy getRootHierarchy();

    public long getAxis();

    public void setAxis(long axis);

    public long getPosition();

    public void setPosition(long position);

    public void setVisible(boolean visible);

    public boolean isVisible();
}
