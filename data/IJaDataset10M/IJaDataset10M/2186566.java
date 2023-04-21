package org.web3d.x3d.palette.items;

import org.web3d.x3d.types.X3DParametricGeometryNode;
import javax.swing.text.JTextComponent;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * NURBSSWEPTSURFACE.java
 * Created on 26 December 2011
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey and Don Brutzman
 * @version $Id$
 */
public class NURBSSWEPTSURFACE extends X3DParametricGeometryNode {

    private boolean ccw;

    private boolean solid;

    public NURBSSWEPTSURFACE() {
    }

    @Override
    public String getElementName() {
        return NURBSSWEPTSURFACE_ELNAME;
    }

    @Override
    public void initialize() {
        ccw = Boolean.parseBoolean(NURBSSWEPTSURFACE_ATTR_CCW_DFLT);
        solid = Boolean.parseBoolean(NURBSSWEPTSURFACE_ATTR_SOLID_DFLT);
        setContent("\n\t\t\t<!--TODO add crossSectionCurve ContourPolyline2D|NurbsCurve2D and trajectoryCurve NurbsCurve nodes here-->\n\t\t");
    }

    @Override
    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        super.initializeFromJdom(root, comp);
        org.jdom.Attribute attr;
        attr = root.getAttribute(NURBSSWEPTSURFACE_ATTR_CCW_NAME);
        if (attr != null) ccw = Boolean.parseBoolean(attr.getValue());
        attr = root.getAttribute(NURBSSWEPTSURFACE_ATTR_SOLID_NAME);
        if (attr != null) solid = Boolean.parseBoolean(attr.getValue());
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return NURBSSWEPTSURFACECustomizer.class;
    }

    @Override
    public String createAttributes() {
        StringBuilder sb = new StringBuilder();
        if (NURBSSWEPTSURFACE_ATTR_CCW_REQD || ccw != Boolean.parseBoolean(NURBSSWEPTSURFACE_ATTR_CCW_DFLT)) {
            sb.append(" ");
            sb.append(NURBSSWEPTSURFACE_ATTR_CCW_NAME);
            sb.append("='");
            sb.append(ccw);
            sb.append("'");
        }
        if (NURBSSWEPTSURFACE_ATTR_SOLID_REQD || solid != Boolean.parseBoolean(NURBSSWEPTSURFACE_ATTR_SOLID_DFLT)) {
            sb.append(" ");
            sb.append(NURBSSWEPTSURFACE_ATTR_SOLID_NAME);
            sb.append("='");
            sb.append(solid);
            sb.append("'");
        }
        return sb.toString();
    }

    public boolean isCcw() {
        return ccw;
    }

    public void setCcw(boolean ccw) {
        this.ccw = ccw;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }
}
