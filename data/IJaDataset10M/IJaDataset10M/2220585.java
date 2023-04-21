package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.web3d.x3d.types.X3DNetworkSensorNode;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * LOADSENSOR.java
 * Created on Sep 12, 2007, 2:46 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public class LOADSENSOR extends X3DNetworkSensorNode {

    private SFFloat timeOut, timeOutDefault;

    private String watchList;

    public LOADSENSOR() {
    }

    @Override
    public String getDefaultContainerField() {
        return "children";
    }

    @Override
    public String getElementName() {
        return LOADSENSOR_ELNAME;
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return LOADSENSORCustomizer.class;
    }

    @Override
    public void initialize() {
        enabled = enabledDefault = Boolean.parseBoolean(LOADSENSOR_ATTR_ENABLED_DFLT);
        timeOut = timeOutDefault = new SFFloat(LOADSENSOR_ATTR_TIMEOUT_DFLT, 0.0f, null);
        setContent("\n\t\t<!--TODO add AudioClip|ImageTexture|Inline|MovieTexture|" + "(X3D version 3.1 or greater)ImageCubeMapTexture|ImageTexture3D|PackagedShader|ShaderPart|ShaderProgram nodes here-->\n\t");
    }

    @Override
    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        super.initializeFromJdom(root, comp);
        org.jdom.Attribute attr;
        attr = root.getAttribute(LOADSENSOR_ATTR_ENABLED_NAME);
        if (attr != null) enabled = Boolean.parseBoolean(attr.getValue());
        attr = root.getAttribute(LOADSENSOR_ATTR_TIMEOUT_NAME);
        if (attr != null) timeOut = new SFFloat(attr.getValue(), 0.0f, null);
    }

    @Override
    public String createAttributes() {
        StringBuffer sb = new StringBuffer();
        if (LOADSENSOR_ATTR_ENABLED_REQD || enabled != enabledDefault) {
            sb.append(" ");
            sb.append(LOADSENSOR_ATTR_ENABLED_NAME);
            sb.append("='");
            sb.append(enabled);
            sb.append("'");
        }
        if (LOADSENSOR_ATTR_TIMEOUT_REQD || !timeOut.equals(timeOutDefault)) {
            sb.append(" ");
            sb.append(LOADSENSOR_ATTR_TIMEOUT_NAME);
            sb.append("='");
            sb.append(timeOut);
            sb.append("'");
        }
        return sb.toString();
    }

    public String getTimeOut() {
        return timeOut.toString();
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = new SFFloat(timeOut, 0.0f, null);
    }
}
