package phworld;

import dsc.netgame.*;
import java.util.*;

public class MaterialTransferWCM extends WorldComponentMessage {

    private MaterialBundle toMaterials, fromMaterials;

    private Vector toComponents, fromComponents;

    private int transferTo;

    private HostShip initiator = null;

    public MaterialTransferWCM(int toComponent, int transferTo, MaterialBundle toMaterials, MaterialBundle fromMaterials, Vector toComponents, Vector fromComponents) {
        super(toComponent);
        this.transferTo = transferTo;
        this.toMaterials = toMaterials;
        this.fromMaterials = fromMaterials;
        this.toComponents = toComponents;
        this.fromComponents = fromComponents;
    }

    public MaterialBundle getToMaterials() {
        if (toMaterials == null) return new MaterialBundle(MaterialBundle.UNLIMITED); else return toMaterials;
    }

    public MaterialBundle getFromMaterials() {
        if (fromMaterials == null) return new MaterialBundle(MaterialBundle.UNLIMITED); else return fromMaterials;
    }

    public int getTransferTarget() {
        return transferTo;
    }

    public Enumeration getToComponents() {
        return createIntegerEnumeration(toComponents);
    }

    public Enumeration getFromComponents() {
        return createIntegerEnumeration(fromComponents);
    }

    public void forceOneWayTransfer() {
        fromMaterials = new MaterialBundle(MaterialBundle.UNLIMITED);
        fromComponents = new Vector();
    }

    private Enumeration createIntegerEnumeration(Vector v) {
        if (v == null) return (new Vector().elements());
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (!(o instanceof Integer)) {
                return (new Vector()).elements();
            }
        }
        return v.elements();
    }

    protected void setInitiator(HostShip h) {
        initiator = h;
    }

    protected HostShip getInitiator() {
        return initiator;
    }
}
