package org.web3d.x3d.palette.items;

import java.util.Arrays;
import javax.swing.text.JTextComponent;
import org.web3d.x3d.types.X3DTextureCoordinateNode;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * TEXTURECOORDINATE3D.java
 * Created on 20 November 2011
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public class TEXTURECOORDINATE3D extends X3DTextureCoordinateNode {

    private SFFloat[][] point, pointDefault;

    public TEXTURECOORDINATE3D() {
    }

    @Override
    public String getElementName() {
        return TEXTURECOORDINATE3D_ELNAME;
    }

    @Override
    public void initialize() {
        String[] sa = parseX(TEXTURECOORDINATE3D_ATTR_POINT_DFLT);
        point = pointDefault = new SFFloat[sa.length / 3][3];
        int r = point.length;
        if (r <= 0) return;
        int c = point[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) point[ir][ic] = pointDefault[ir][ic] = buildSFFloat(sa[i++]);
    }

    @Override
    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        super.initializeFromJdom(root, comp);
        org.jdom.Attribute attr;
        attr = root.getAttribute(TEXTURECOORDINATE3D_ATTR_POINT_NAME);
        if (attr != null) {
            String[] sa = parseX(attr.getValue());
            point = makeSFFloatArray(sa, 3);
        } else point = new SFFloat[][] {};
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return TEXTURECOORDINATE3DCustomizer.class;
    }

    @Override
    public String createAttributes() {
        StringBuilder sb = new StringBuilder();
        if (TEXTURECOORDINATE3D_ATTR_POINT_REQD || !Arrays.equals(point, pointDefault)) {
            sb.append(" ");
            sb.append(TEXTURECOORDINATE3D_ATTR_POINT_NAME);
            sb.append("='");
            sb.append(formatFloatArray(point));
            sb.append("'");
        }
        return sb.toString();
    }

    public String[][] getPoint() {
        if (point != null && point.length > 0) return makeStringArray(point);
        return new String[][] { {} };
    }

    public void setPoint(String[][] saa) {
        if (saa != null && saa.length > 0) point = makeSFFloatArray(saa); else point = new SFFloat[][] {};
    }

    public boolean isNil() {
        return !(point != null && point.length > 0);
    }
}
