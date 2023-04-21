package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.web3d.x3d.types.X3DTransformNode;
import static org.web3d.x3d.types.X3DSchemaData.*;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;

/**
 * TRANSFORM.java
 * Created on July 11, 2007, 5:13 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public class TRANSFORM extends X3DTransformNode {

    public TRANSFORM() {
    }

    @Override
    public String getElementName() {
        return TRANSFORM_ELNAME;
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return TRANSFORMCustomizer.class;
    }

    @Override
    public void initialize() {
        String[] fa;
        fa = parse3(TRANSFORM_ATTR_TRANSLATION_DFLT);
        translationX = translationXDefault = new SFFloat(fa[0], null, null);
        translationY = translationYDefault = new SFFloat(fa[1], null, null);
        translationZ = translationZDefault = new SFFloat(fa[2], null, null);
        fa = parse4(TRANSFORM_ATTR_ROTATION_DFLT);
        rotation0 = rotation0Default = new SFFloat(fa[0], null, null);
        rotation1 = rotation1Default = new SFFloat(fa[1], null, null);
        rotation2 = rotation2Default = new SFFloat(fa[2], null, null);
        rotation3 = rotation3Default = new SFFloat(fa[3], null, null);
        fa = parse3(TRANSFORM_ATTR_CENTER_DFLT);
        centerX = centerXDefault = new SFFloat(fa[0], null, null);
        centerY = centerYDefault = new SFFloat(fa[1], null, null);
        centerZ = centerZDefault = new SFFloat(fa[2], null, null);
        fa = parse3(TRANSFORM_ATTR_SCALE_DFLT);
        scaleX = scaleXDefault = new SFFloat(fa[0], null, null);
        scaleY = scaleYDefault = new SFFloat(fa[1], null, null);
        scaleZ = scaleZDefault = new SFFloat(fa[2], null, null);
        fa = parse4(TRANSFORM_ATTR_SCALEORIENTATION_DFLT);
        scaleOrientationX = scaleOrientation0Default = new SFFloat(fa[0], null, null);
        scaleOrientationY = scaleOrientation1Default = new SFFloat(fa[1], null, null);
        scaleOrientationZ = scaleOrientation2Default = new SFFloat(fa[2], null, null);
        scaleOrientationAngle = scaleOrientation3Default = new SFFloat(fa[3], null, null);
        fa = parse3(TRANSFORM_ATTR_BBOXCENTER_DFLT);
        bboxCenterX = bboxCenterXDefault = new SFFloat(fa[0], null, null);
        bboxCenterY = bboxCenterYDefault = new SFFloat(fa[1], null, null);
        bboxCenterZ = bboxCenterZDefault = new SFFloat(fa[2], null, null);
        fa = parse3(TRANSFORM_ATTR_BBOXSIZE_DFLT);
        bboxSizeX = bboxSizeXDefault = new SFFloat(fa[0], null, null);
        bboxSizeY = bboxSizeYDefault = new SFFloat(fa[1], null, null);
        bboxSizeZ = bboxSizeZDefault = new SFFloat(fa[2], null, null);
        setContent("\n\t\t<!--TODO add children nodes here-->\n\t");
    }

    @Override
    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        super.initializeFromJdom(root, comp);
        org.jdom.Attribute attr;
        String[] fa;
        attr = root.getAttribute(TRANSFORM_ATTR_TRANSLATION_NAME);
        if (attr != null) {
            fa = parse3(attr.getValue());
            translationX = new SFFloat(fa[0], null, null);
            translationY = new SFFloat(fa[1], null, null);
            translationZ = new SFFloat(fa[2], null, null);
        }
        attr = root.getAttribute(TRANSFORM_ATTR_ROTATION_NAME);
        if (attr != null) {
            fa = parse4(attr.getValue());
            rotation0 = new SFFloat(fa[0], null, null);
            rotation1 = new SFFloat(fa[1], null, null);
            rotation2 = new SFFloat(fa[2], null, null);
            rotation3 = new SFFloat(fa[3], null, null);
        }
        attr = root.getAttribute(TRANSFORM_ATTR_CENTER_NAME);
        if (attr != null) {
            fa = parse3(attr.getValue());
            centerX = new SFFloat(fa[0], null, null);
            centerY = new SFFloat(fa[1], null, null);
            centerZ = new SFFloat(fa[2], null, null);
        }
        attr = root.getAttribute(TRANSFORM_ATTR_SCALE_NAME);
        if (attr != null) {
            fa = parse3(attr.getValue());
            scaleX = new SFFloat(fa[0], null, null);
            scaleY = new SFFloat(fa[1], null, null);
            scaleZ = new SFFloat(fa[2], null, null);
        }
        attr = root.getAttribute(TRANSFORM_ATTR_SCALEORIENTATION_NAME);
        if (attr != null) {
            fa = parse4(attr.getValue());
            scaleOrientationX = new SFFloat(fa[0], null, null);
            scaleOrientationY = new SFFloat(fa[1], null, null);
            scaleOrientationZ = new SFFloat(fa[2], null, null);
            scaleOrientationAngle = new SFFloat(fa[3], null, null);
        }
        attr = root.getAttribute(TRANSFORM_ATTR_BBOXCENTER_NAME);
        if (attr != null) {
            fa = parse3(attr.getValue());
            bboxCenterX = new SFFloat(fa[0], null, null);
            bboxCenterY = new SFFloat(fa[1], null, null);
            bboxCenterZ = new SFFloat(fa[2], null, null);
        }
        attr = root.getAttribute(TRANSFORM_ATTR_BBOXSIZE_NAME);
        if (attr != null) {
            fa = parse3(attr.getValue());
            bboxSizeX = new SFFloat(fa[0], null, null);
            bboxSizeY = new SFFloat(fa[1], null, null);
            bboxSizeZ = new SFFloat(fa[2], null, null);
        }
    }

    @Override
    public String createAttributes() {
        StringBuffer sb = new StringBuffer();
        if (TRANSFORM_ATTR_BBOXCENTER_REQD || (!bboxCenterX.equals(bboxCenterXDefault) || !bboxCenterY.equals(bboxCenterYDefault) || !bboxCenterZ.equals(bboxCenterZDefault))) {
            sb.append(" ");
            sb.append(TRANSFORM_ATTR_BBOXCENTER_NAME);
            sb.append("='");
            sb.append(bboxCenterX);
            sb.append(" ");
            sb.append(bboxCenterY);
            sb.append(" ");
            sb.append(bboxCenterZ);
            sb.append("'");
        }
        if (TRANSFORM_ATTR_BBOXSIZE_REQD || (!bboxSizeX.equals(bboxSizeXDefault) || !bboxSizeY.equals(bboxSizeYDefault) || !bboxSizeZ.equals(bboxSizeZDefault))) {
            sb.append(" ");
            sb.append(TRANSFORM_ATTR_BBOXSIZE_NAME);
            sb.append("='");
            sb.append(bboxSizeX);
            sb.append(" ");
            sb.append(bboxSizeY);
            sb.append(" ");
            sb.append(bboxSizeZ);
            sb.append("'");
        }
        if (TRANSFORM_ATTR_CENTER_REQD || (!centerX.equals(centerXDefault) || !centerY.equals(centerYDefault) || !centerZ.equals(centerZDefault))) {
            sb.append(" ");
            sb.append(TRANSFORM_ATTR_CENTER_NAME);
            sb.append("='");
            sb.append(centerX);
            sb.append(" ");
            sb.append(centerY);
            sb.append(" ");
            sb.append(centerZ);
            sb.append("'");
        }
        if (TRANSFORM_ATTR_ROTATION_REQD || (!rotation0.equals(rotation0Default) || !rotation1.equals(rotation1Default) || !rotation2.equals(rotation2Default) || !rotation3.equals(rotation3Default))) {
            sb.append(" ");
            sb.append(TRANSFORM_ATTR_ROTATION_NAME);
            sb.append("='");
            sb.append(rotation0);
            sb.append(" ");
            sb.append(rotation1);
            sb.append(" ");
            sb.append(rotation2);
            sb.append(" ");
            sb.append(rotation3);
            sb.append("'");
        }
        if (TRANSFORM_ATTR_SCALE_REQD || (!scaleX.equals(scaleXDefault) || !scaleY.equals(scaleYDefault) || !scaleZ.equals(scaleZDefault))) {
            sb.append(" ");
            sb.append(TRANSFORM_ATTR_SCALE_NAME);
            sb.append("='");
            sb.append(scaleX);
            sb.append(" ");
            sb.append(scaleY);
            sb.append(" ");
            sb.append(scaleZ);
            sb.append("'");
        }
        if (TRANSFORM_ATTR_SCALEORIENTATION_REQD || (!scaleOrientationX.equals(scaleOrientation0Default) || !scaleOrientationY.equals(scaleOrientation1Default) || !scaleOrientationZ.equals(scaleOrientation2Default) || !scaleOrientationAngle.equals(scaleOrientation3Default))) {
            sb.append(" ");
            sb.append(TRANSFORM_ATTR_SCALEORIENTATION_NAME);
            sb.append("='");
            sb.append(scaleOrientationX);
            sb.append(" ");
            sb.append(scaleOrientationY);
            sb.append(" ");
            sb.append(scaleOrientationZ);
            sb.append(" ");
            sb.append(scaleOrientationAngle);
            sb.append("'");
        }
        if (TRANSFORM_ATTR_TRANSLATION_REQD || (!translationX.equals(translationXDefault) || !translationY.equals(translationYDefault) || !translationZ.equals(translationZDefault))) {
            sb.append(" ");
            sb.append(TRANSFORM_ATTR_TRANSLATION_NAME);
            sb.append("='");
            sb.append(translationX);
            sb.append(" ");
            sb.append(translationY);
            sb.append(" ");
            sb.append(translationZ);
            sb.append("'");
        }
        return sb.toString();
    }
}
