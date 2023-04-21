package com.golemgame.mvc.golems.transform;

import java.util.ArrayList;
import com.golemgame.mvc.DataType;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.ReferenceMap;
import com.golemgame.mvc.golems.BallAndSocketInterpreter;
import com.golemgame.mvc.golems.MachineInterpreter;
import com.golemgame.mvc.golems.MachineSpaceInterpreter;
import com.golemgame.mvc.golems.SpatialInterpreter;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class OldToNewBallAndSocketTransformation extends Transformation {

    public OldToNewBallAndSocketTransformation() {
        super("0.54.0");
    }

    @Override
    public void apply(PropertyStore store) {
        MachineSpaceInterpreter machineSpace = new MachineSpaceInterpreter(store);
        for (DataType val : machineSpace.getMachines().getValues()) {
            if (val instanceof PropertyStore) {
                apply(new MachineInterpreter((PropertyStore) val));
            }
        }
    }

    private static final String BALL_SOCKET_CLASS_OLD = "Structure.BallandSocket";

    private void apply(MachineInterpreter interpreter) {
        ArrayList<PropertyStore> oldBallAndSockets = new ArrayList<PropertyStore>();
        for (DataType val : interpreter.getStructures().getValues()) {
            if (val instanceof PropertyStore) {
                PropertyStore store = (PropertyStore) val;
                if (BALL_SOCKET_CLASS_OLD.equals(((PropertyStore) val).getClassName())) {
                    oldBallAndSockets.add(store);
                }
            }
        }
        for (PropertyStore store : oldBallAndSockets) apply(interpreter, store);
    }

    private static final float OLD_RADIUS = 0.25f;

    private static final float OLD_HEIGHT = 0.5f;

    private static final float OLD_DISTANCE = 0.625f;

    private void apply(MachineInterpreter interpreter, PropertyStore store) {
        ReferenceMap refMap = new ReferenceMap();
        PropertyStore copy1 = store.uniqueDeepCopy(refMap);
        PropertyStore copy2 = store.uniqueDeepCopy(refMap);
        BallAndSocketInterpreter replacement1 = new BallAndSocketInterpreter(copy1);
        BallAndSocketInterpreter replacement2 = new BallAndSocketInterpreter(copy2);
        replacement1.setLeftLength(OLD_HEIGHT);
        replacement2.setLeftLength(OLD_HEIGHT);
        replacement1.setLeftRadius(OLD_RADIUS);
        replacement2.setLeftRadius(OLD_RADIUS);
        replacement1.setRightRadius(OLD_DISTANCE - OLD_HEIGHT / 2f - OLD_RADIUS);
        replacement2.setRightRadius(OLD_DISTANCE - OLD_HEIGHT / 2f - OLD_RADIUS);
        Quaternion rotation = store.getQuaternion(SpatialInterpreter.LOCALROTATION, new Quaternion());
        replacement1.getLocalRotation().set(rotation);
        Quaternion mirror = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Z);
        replacement2.getLocalRotation().set(rotation).multLocal(mirror);
        interpreter.removeStructure(store);
        interpreter.addStructure(replacement1.getStore());
        interpreter.addStructure(replacement2.getStore());
    }
}
