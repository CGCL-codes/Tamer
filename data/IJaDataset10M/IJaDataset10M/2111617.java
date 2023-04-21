package com.golemgame.physical.ode;

import java.util.Collection;
import java.util.Map;
import com.golemgame.model.spatial.SpatialModel;
import com.golemgame.model.spatial.shape.PyramidModel;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.GhostPyramidInterpreter;
import com.golemgame.physical.PhysicsComponent;
import com.golemgame.physical.ode.compile.OdePhysicsEnvironment;
import com.golemgame.states.physics.ode.PhysicsSpatialMonitor;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Pyramid;
import com.jmex.physics.PhysicsCollisionGeometry;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.geometry.PhysicsMesh;
import com.jmex.physics.material.Material;

public class OdeGhostPyramid extends OdeGhostStructure {

    private GhostPyramidInterpreter interpreter;

    public OdeGhostPyramid(PropertyStore store) {
        super(store);
        interpreter = new GhostPyramidInterpreter(store);
    }

    protected PhysicsCollisionGeometry collision = null;

    public PhysicsCollisionGeometry getGhost() {
        return collision;
    }

    @Override
    public float buildCollisionGeometries(PhysicsNode physicsNode, Collection<PhysicsComponent> components, Vector3f store) {
        collision = physicsNode.createMesh("pyramid");
        TriMesh pyramid = new Pyramid("", 1f, 1f);
        pyramid.getLocalScale().set(interpreter.getPyramidScale());
        ((PhysicsMesh) collision).copyFrom(pyramid);
        collision.getLocalTranslation().set(interpreter.getLocalTranslation());
        collision.getLocalRotation().multLocal(new Quaternion().fromAngleNormalAxis(FastMath.PI, Vector3f.UNIT_X));
        collision.getLocalRotation().multLocal(interpreter.getLocalRotation());
        collision.getLocalScale().set(interpreter.getPyramidScale());
        collision.getLocalRotation().set(getParentRotation().mult(collision.getLocalRotation()));
        getParentRotation().multLocal(collision.getLocalTranslation());
        collision.getLocalTranslation().addLocal(getParentTranslation());
        collision.setIsCollidable(false);
        collision.setMaterial(Material.GHOST);
        PhysicsSpatialMonitor.getInstance().registerGhost(collision);
        physicsNode.attachChild(collision);
        OdePhysicsComponent comp = new OdePhysicsComponent(collision, null);
        comp.setMass(0f);
        components.add(comp);
        return 0;
    }

    public void buildRelationships(Map<OdePhysicalStructure, PhysicsNode> physicalMap, OdePhysicsEnvironment environment) {
        SpatialModel sensorField = new PyramidModel(false);
        collision.getParent().attachChild(sensorField.getSpatial());
        sensorField.getLocalTranslation().set(this.collision.getLocalTranslation());
        sensorField.getLocalScale().set(this.collision.getLocalScale());
        sensorField.getLocalRotation().set(this.collision.getLocalRotation());
        super.getStructuralAppearanceEffect().attachModel(sensorField);
        sensorField.updateModelData();
        sensorField.updateModelData();
        super.getStructuralAppearanceEffect().attachModel(sensorField);
        sensorField.setVisible(false);
        super.setSensorField(sensorField);
        environment.registerSensorSpatial(collision);
    }
}
