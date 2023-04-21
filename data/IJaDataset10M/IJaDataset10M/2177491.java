package com.golemgame.physical.ode;

import java.util.Collection;
import java.util.Map;
import com.golemgame.model.spatial.SpatialModel;
import com.golemgame.model.spatial.shape.BoxModel;
import com.golemgame.model.spatial.shape.ConeFacade;
import com.golemgame.model.spatial.shape.ConeModel;
import com.golemgame.model.texture.TextureTypeKey.TextureShape;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.GhostConeInterpreter;
import com.golemgame.physical.PhysicsComponent;
import com.golemgame.physical.ode.compile.OdePhysicsEnvironment;
import com.golemgame.states.physics.ode.PhysicsSpatialMonitor;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jmex.physics.PhysicsCollisionGeometry;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.geometry.PhysicsMesh;
import com.jmex.physics.material.Material;

public class OdeGhostCone extends OdeGhostStructure {

    private GhostConeInterpreter interpreter;

    public OdeGhostCone(PropertyStore store) {
        super(store);
        interpreter = new GhostConeInterpreter(store);
    }

    protected PhysicsMesh meshCollision = null;

    @Override
    protected TextureShape getPrefferedShape() {
        return TextureShape.Cone;
    }

    @Override
    public float buildCollisionGeometries(PhysicsNode physicsNode, Collection<PhysicsComponent> components, Vector3f store) {
        meshCollision = physicsNode.createMesh("cylMesh");
        ConeModel physicsModel = new ConeModel();
        physicsModel.getLocalScale().set(interpreter.getRadius() * 2f, interpreter.getRadius() * 2f, interpreter.getHeight());
        meshCollision.copyFrom((TriMesh) ((SpatialModel) physicsModel).getSpatial());
        meshCollision.getLocalTranslation().set(interpreter.getLocalTranslation());
        meshCollision.getLocalRotation().set(interpreter.getLocalRotation());
        meshCollision.getLocalRotation().set(getParentRotation().mult(meshCollision.getLocalRotation()));
        meshCollision.getLocalRotation().multLocal(getParentRotation());
        meshCollision.getLocalTranslation().addLocal(getParentTranslation());
        meshCollision.setMaterial(Material.GHOST);
        PhysicsSpatialMonitor.getInstance().registerGhost(meshCollision);
        physicsNode.attachChild(meshCollision);
        OdePhysicsComponent comp = new OdePhysicsComponent(meshCollision, null);
        comp.setMass(0f);
        components.add(comp);
        return 0;
    }

    public PhysicsCollisionGeometry getGhost() {
        return meshCollision;
    }

    public void buildRelationships(Map<OdePhysicalStructure, PhysicsNode> physicalMap, OdePhysicsEnvironment environment) {
        SpatialModel sensorField = new ConeFacade();
        meshCollision.getParent().attachChild(sensorField.getSpatial());
        sensorField.getLocalTranslation().set(this.meshCollision.getLocalTranslation());
        sensorField.getLocalScale().set(this.meshCollision.getLocalScale());
        sensorField.getLocalRotation().set(this.meshCollision.getLocalRotation());
        super.getStructuralAppearanceEffect().attachModel(sensorField);
        sensorField.updateModelData();
        sensorField.updateModelData();
        super.getStructuralAppearanceEffect().attachModel(sensorField);
        sensorField.setVisible(false);
        super.setSensorField(sensorField);
        environment.registerSensorSpatial(meshCollision);
    }
}
