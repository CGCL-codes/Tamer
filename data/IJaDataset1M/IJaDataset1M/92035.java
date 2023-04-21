package org.web3d.x3d.palette.items;

import org.web3d.x3d.types.SceneGraphStructureNodeType;

/**
 * PROTOTYPE_RelativeProximitySensor.java
 * Created on 18 October 2009
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Don Brutzman, Mike Bailey
 * @version $Id$
 */
public class PROTOTYPE_RelativeProximitySensor extends SceneGraphStructureNodeType {

    public PROTOTYPE_RelativeProximitySensor() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public String createBody() {
        return "\n" + "    <!-- ==================== -->\n" + "    <ExternProtoDeclare name='RelativeProximitySensor'\n" + "        appinfo='TimeSensor functionality commences after delayInterval pause'\n" + "        url='\"../../Savage/Tools/Animation/RelativeProximitySensorPrototype.x3d#RelativeProximitySensor\"\n" + "             \"../../../Savage/Tools/Animation/RelativeProximitySensorPrototype.x3d#RelativeProximitySensor\"\n" + "             \"https://savage.nps.edu/Savage/Tools/Animation/RelativeProximitySensorPrototype.x3d#RelativeProximitySensor\"\n" + "             \"../../../Savage/Tools/Animation/RelativeProximitySensorPrototype.wrl#RelativeProximitySensor\"\n" + "             \"../../Savage/Tools/Animation/RelativeProximitySensorPrototype.wrl#RelativeProximitySensor\"\n" + "             \"https://savage.nps.edu/Savage/Tools/Animation/RelativeProximitySensorPrototype.wrl#RelativeProximitySensor\" '>\n" + "      <field accessType='inputOutput' appinfo='describe the purpose of this sensor' name='description' type='SFString'/>\n" + "      <field accessType='initializeOnly' appinfo='where is the primary object' name='locationPrimary' type='SFVec3f'/>\n" + "      <field accessType='inputOnly' appinfo='update location of the primary object' name='set_locationPrimary' type='SFVec3f'/>\n" + "      <field accessType='initializeOnly' appinfo='where is the secondary object' name='locationSecondary' type='SFVec3f'/>\n" + "      <field accessType='inputOnly' appinfo='update location of the secondary object' name='set_locationSecondary' type='SFVec3f'/>\n" + "      <field accessType='initializeOnly' appinfo='distance for detecting object-to-object collision' name='proximityRangeThreshold' type='SFFloat'/>\n" + "      <field accessType='inputOnly' appinfo='change threshold distance for detecting collision' name='set_proximityRangeThreshold' type='SFFloat'/>\n" + "      <field accessType='outputOnly' appinfo='is object-to-object distance less than proximityRangeThreshold?' name='isInRange' type='SFBool'/>\n" + "      <field accessType='outputOnly' appinfo='when did object-to-object range meet detection criteria?' name='isInRangeTime' type='SFTime'/>\n" + "      <field accessType='initializeOnly' appinfo='whether sensor is active' name='enabled' type='SFBool'/>\n" + "      <field accessType='inputOnly' name='set_enabled' type='SFBool'/>\n" + "    </ExternProtoDeclare>\n" + "    <ProtoInstance name='RelativeProximitySensor' DEF='ExampleRelativeProximitySensor'>\n" + "      <fieldValue name='description' value='test case'/>\n" + "      <fieldValue name='enabled' value='true'/>\n" + "      <fieldValue name='locationPrimary' value='-10 0 0'/>\n" + "      <fieldValue name='locationSecondary' value='10 0 0'/>\n" + "    </ProtoInstance>\n" + "    <!-- Example use: https://savage.nps.edu/Savage/Tools/Animation/RelativeProximitySensorExample.x3d -->\n" + "    <!-- ==================== -->\n";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return null;
    }

    @Override
    public String getElementName() {
        return "RelativeProximitySensor";
    }
}
