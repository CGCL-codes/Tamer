package org.web3d.x3d.palette.items;

import org.web3d.x3d.types.SceneGraphStructureNodeType;

/**
 * PROTOTYPE_ArbitraryAxisCylinderSensor.java
 * Created on 6 May 2009
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Don Brutzman, Mike Bailey
 * @version $Id$
 */
public class PROTOTYPE_ArbitraryAxisCylinderSensor extends SceneGraphStructureNodeType {

    public PROTOTYPE_ArbitraryAxisCylinderSensor() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public String createBody() {
        return "\n" + "    <!-- ==================== -->\n" + "    <ExternProtoDeclare name='ArbitraryAxisCylinderSensor' appinfo='Modified CylinderSensor with children nodes oriented about an arbitrary axis. Warning: ArbitraryAxisCylinderSensor affects children, not peers.' url='\"../../../Savage/Tools/Animation/ArbitraryAxisCylinderSensorPrototype.x3d#ArbitraryAxisCylinderSensor\" \"https://savage.nps.edu/Savage/Tools/Animation/ArbitraryAxisCylinderSensorPrototype.x3d#ArbitraryAxisCylinderSensor\" \"../../../Savage/Tools/Animation/ArbitraryAxisCylinderSensorPrototype.wrl#ArbitraryAxisCylinderSensor\" \"https://savage.nps.edu/Savage/Tools/Animation/ArbitraryAxisCylinderSensorPrototype.wrl#ArbitraryAxisCylinderSensor\"'>\n" + "      <field accessType='initializeOnly' appinfo='shifted axis of rotation from local vertical, default 1 0 0 0' name='shiftRotationAxis' type='SFRotation'/>\n" + "      <field accessType='initializeOnly' appinfo='local center for axis of rotation, default 0 0 0' name='center' type='SFVec3f'/>\n" + "      <field accessType='initializeOnly' appinfo='whether to show visualization shape to show orientation and cylindrical mapping of mouse movements by sensor, default true' name='showCylinderSensorShape' type='SFBool'/>\n" + "      <field accessType='inputOutput' appinfo='scale for visualization shape, default 1 1 1' name='scaleCylinderSensorShape' type='SFVec3f'/>\n" + "      <field accessType='inputOutput' appinfo='color for visualization shape, default 0.9 0.9 0.4' name='colorCylinderSensorShape' type='SFColor'/>\n" + "      <field accessType='inputOutput' appinfo='transparency for visualization shape0.8,' name='transparencyCylinderSensorShape' type='SFFloat'/>\n" + "      <field accessType='inputOutput' appinfo='children nodes affected by ArbitraryAxisCylinderSensor' name='children' type='MFNode'/>\n" + "      <field accessType='inputOutput' appinfo='determines whether previous offset values are remembered/accumulated, default true' name='autoOffset' type='SFBool'/>\n" + "      <field accessType='inputOutput' appinfo='Text description to be displayed for action of this node' name='description' type='SFString'/>\n" + "      <field accessType='inputOutput' appinfo='diskAngle 0 forces disk-like behavior, diskAngle 1.57 (90 degrees) forces cylinder-like behavior, default 0.262, range [0,pi/2]' name='diskAngle' type='SFFloat'/>\n" + "      <field accessType='inputOutput' appinfo='enables/disables node operation, default true' name='enabled' type='SFBool'/>\n" + "      <field accessType='inputOutput' appinfo='clamps rotation_changed events, default 0, range [-2pi,2pi]' name='minAngle' type='SFFloat'/>\n" + "      <field accessType='inputOutput' appinfo='clamps rotation_changed events, default -1, range [-2pi,2pi]' name='maxAngle' type='SFFloat'/>\n" + "      <field accessType='initializeOnly' appinfo='sends event and remembers last value sensed, default 0, range (-infinity,infinity)' name='offset' type='SFFloat'/>\n" + "      <field accessType='outputOnly' appinfo='output event isActive=true when primary mouse button is pressed, output event isActive=false when released.' name='isActive' type='SFBool'/>\n" + "      <field accessType='outputOnly' appinfo='rotation_changed events equal sum of relative bearing changes plus offset value about Y-axis in local coordinate system' name='rotation_changed' type='SFRotation'/>\n" + "      <field accessType='outputOnly' appinfo='trackPoint_changed events give intersection point of bearing with sensor&apos;s virtual geometry' name='trackPoint_changed' type='SFVec3f'/>\n" + "    </ExternProtoDeclare>\n" + "    <ProtoInstance DEF='ArbitraryAxisCylinderSensorTest' name='ArbitraryAxisCylinderSensor'>\n" + "      <!-- rotate yAxis to xAxis -->\n" + "      <fieldValue name='shiftRotationAxis' value='0 0 1 -1.5707963'/>\n" + "      <fieldValue name='children'>\n" + "        <Shape>\n" + "           <Box/>\n" + "          <Appearance>\n" + "             <Material diffuseColor='1 0 0'/>\n" + "          </Appearance>\n" + "         </Shape>\n" + "      </fieldValue>\n" + "    </ProtoInstance>\n" + "    <!-- Example use: https://savage.nps.edu/Savage/Tools/Animation/ArbitraryAxisCylinderSensorExample.x3d -->\n" + "    <!-- ==================== -->\n";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return null;
    }

    @Override
    public String getElementName() {
        return "ArbitraryAxisCylinderSensor";
    }
}
