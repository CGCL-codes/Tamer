package org.gjt.universe;

/** This class is used to access Galaxy objects in the database.
	@see GalaxyBase
*/
public final class GalaxyID extends Index {

    public GalaxyID() {
        super(0);
    }

    public GalaxyID(int in) {
        super(in);
    }

    /** 
	 * @deprecated Use GalaxyList.get() instead 
	 **/
    public DBItem getItem() {
        return GalaxyList.get(this);
    }

    public DisplayNode getDisplayNodeChildren(CivID AID) {
        GalaxyBase galaxy = GalaxyList.get(this);
        VectorSystemID systems = galaxy.getGalaxySystems();
        DisplayNode returnDN;
        int size = systems.size();
        if (size == 0) {
            return null;
        }
        returnDN = new DisplayNode(AID, systems.get(0));
        DisplayNode tmpDN = returnDN;
        for (int cnt = 1; cnt < size; cnt++) {
            tmpDN.setNext(new DisplayNode(AID, systems.get(cnt)));
            tmpDN = tmpDN.getNext();
        }
        return returnDN;
    }

    public String getDisplayNodeMessage() {
        return "Galaxy " + this.get();
    }

    public String getDisplayNodeImage() {
        return new String("galaxy.icon.gif");
    }
}
