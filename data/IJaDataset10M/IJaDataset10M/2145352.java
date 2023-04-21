package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.web3d.x3d.types.X3DGeometryNode;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * BOX.java
 * Created on July 11, 2007, 3:32 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public class BOX extends X3DGeometryNode {

    private SFFloat sizeX, sizeXDefault;

    private SFFloat sizeY, sizeYDefault;

    private SFFloat sizeZ, sizeZDefault;

    private boolean solid;

    public BOX() {
    }

    @Override
    public String getElementName() {
        return BOX_ELNAME;
    }

    @Override
    public void initialize() {
        String[] sa = parse3(BOX_ATTR_SIZE_DFLT);
        sizeX = sizeXDefault = makeSize(sa[0]);
        sizeY = sizeYDefault = makeSize(sa[1]);
        sizeZ = sizeZDefault = makeSize(sa[2]);
        solid = Boolean.parseBoolean(BOX_ATTR_SOLID_DFLT);
    }

    @Override
    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        super.initializeFromJdom(root, comp);
        org.jdom.Attribute attr = root.getAttribute(BOX_ATTR_SIZE_NAME);
        if (attr != null) {
            String[] sa = parse3(attr.getValue());
            sizeX = makeSize(sa[0]);
            sizeY = makeSize(sa[1]);
            sizeZ = makeSize(sa[2]);
        }
        attr = root.getAttribute(BOX_ATTR_SOLID_NAME);
        if (attr != null) solid = Boolean.parseBoolean(attr.getValue());
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return BOXCustomizer.class;
    }

    @Override
    protected String createAttributes() {
        StringBuffer sb = new StringBuffer();
        if (BOX_ATTR_SIZE_REQD || (!sizeX.equals(sizeXDefault)) || (!sizeY.equals(sizeYDefault)) || (!sizeZ.equals(sizeZDefault))) {
            sb.append(" ");
            sb.append(BOX_ATTR_SIZE_NAME);
            sb.append("='");
            sb.append(sizeX);
            sb.append(" ");
            sb.append(sizeY);
            sb.append(" ");
            sb.append(sizeZ);
            sb.append("'");
        }
        if (BOX_ATTR_SOLID_REQD || solid != Boolean.parseBoolean(BOX_ATTR_SOLID_DFLT)) {
            sb.append(" ");
            sb.append(BOX_ATTR_SOLID_NAME);
            sb.append("='");
            sb.append(solid);
            sb.append("'");
        }
        return sb.toString();
    }

    public String getSizeX() {
        return sizeX.toString();
    }

    public void setSizeX(String sizeX) {
        this.sizeX = makeSize(sizeX);
    }

    public String getSizeY() {
        return sizeY.toString();
    }

    public void setSizeY(String sizeY) {
        this.sizeY = makeSize(sizeY);
    }

    public String getSizeZ() {
        return sizeZ.toString();
    }

    public void setSizeZ(String sizeZ) {
        this.sizeZ = makeSize(sizeZ);
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    private SFFloat makeSize(String s) {
        return new SFFloat(s, 0.0f, null);
    }
}
