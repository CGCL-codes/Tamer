package com.golemgame.physical.ode;

import java.util.Collection;
import java.util.Map;
import com.golemgame.model.Model;
import com.golemgame.model.spatial.SpatialModel;
import com.golemgame.model.spatial.shape.CylinderFacade;
import com.golemgame.model.spatial.shape.CylinderModel;
import com.golemgame.model.texture.TextureTypeKey.TextureShape;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.golems.CylinderInterpreter;
import com.golemgame.physical.PhysicsComponent;
import com.golemgame.physical.ode.compile.OdePhysicsEnvironment;
import com.golemgame.structural.MaterialWrapper;
import com.golemgame.structural.collision.CollisionMember;
import com.golemgame.structural.structures.decorators.PhysicalDecorator;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jmex.physics.PhysicsCollisionGeometry;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.geometry.PhysicsMesh;

public class OdeCylinderStructure extends OdePhysicalStructure {

    private CylinderInterpreter interpreter;

    public OdeCylinderStructure(PropertyStore store) {
        super(store);
        interpreter = new CylinderInterpreter(store);
        super.getStructuralAppearanceEffect().setPreferedShape(TextureShape.Cylinder);
    }

    @Override
    protected TextureShape getPrefferedTextureShape() {
        return TextureShape.Cylinder;
    }

    protected SpatialModel physicsModel = null;

    public CylinderInterpreter getInterpreter() {
        return interpreter;
    }

    public void buildCollidable(CollisionMember collidable) {
        physicsModel = new CylinderModel(true);
        physicsModel.getLocalTranslation().set(interpreter.getLocalTranslation());
        physicsModel.getLocalRotation().set(interpreter.getLocalRotation());
        physicsModel.getLocalRotation().multLocal(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));
        physicsModel.getLocalScale().set(interpreter.getRadius() * 2f, interpreter.getRadius() * 2f, interpreter.getHeight());
        collidable.registerCollidingModel(physicsModel);
        collidable.getModel().addChild(physicsModel);
        physicsModel.getListeners().clear();
        physicsModel.updateModelData();
        physicsModel.updateWorldData();
        Model physicsFacade = new CylinderFacade();
        physicsFacade.loadModelData(physicsModel);
        collidable.getModel().addChild(physicsFacade);
        super.getStructuralAppearanceEffect().attachModel(physicsFacade);
    }

    protected transient PhysicsMesh meshCollision = null;

    protected transient PhysicsCollisionGeometry cylCollision = null;

    public float buildCollisionGeometries(PhysicsNode physicsNode, Collection<PhysicsComponent> components, Vector3f store) {
        cylCollision = physicsNode.createCylinder("cylinder");
        cylCollision.getLocalTranslation().set(physicsModel.getLocalTranslation());
        cylCollision.getLocalRotation().set(physicsModel.getLocalRotation());
        cylCollision.getLocalScale().set(physicsModel.getLocalScale());
        cylCollision.getLocalScale().x /= 2f;
        cylCollision.getLocalScale().y /= 2f;
        cylCollision.setIsCollidable(false);
        cylCollision.setMaterial(this.getMaterial());
        cylCollision.updateWorldVectors();
        float radius = cylCollision.getLocalScale().x;
        float length = cylCollision.getLocalScale().z;
        {
            float numberOfBoxes = 8;
            float boxRadius = radius * 0.96f;
            float boxLength = length * 0.96f;
            float theta = FastMath.PI / numberOfBoxes;
            float halfWidth = FastMath.sin(theta / 2f) * boxRadius;
            float boxHeight = FastMath.sqrt(boxRadius * boxRadius - halfWidth * halfWidth) * 2f;
            float boxWidth = halfWidth * 2f;
            for (int i = 0; i < numberOfBoxes; i++) {
                PhysicsBox box = physicsNode.createBox("CylinderAsterisk" + i);
                box.getLocalScale().set(boxHeight, boxWidth, boxLength);
                box.getLocalTranslation().set(physicsModel.getLocalTranslation());
                Quaternion boxRotation = new Quaternion().fromAngleNormalAxis(theta * i, Vector3f.UNIT_Z);
                box.getLocalRotation().set(physicsModel.getLocalRotation());
                box.getLocalRotation().multLocal(boxRotation);
                box.setIsCollidable(false);
                physicsNode.attachChild(box);
                physicsNode.updateWorldVectors();
                box.setMaterial(MaterialWrapper.WEIGHTLESS);
                OdePhysicsComponent comp = new OdePhysicsComponent(box, physicsModel);
                comp.setMass(0f);
                components.add(comp);
            }
        }
        physicsNode.attachChild(cylCollision);
        cylCollision.updateWorldVectors();
        OdePhysicsComponent comp = new OdePhysicsComponent(cylCollision, physicsModel);
        comp.setMass(cylCollision.getVolume() * this.getMaterial().getDensity());
        components.add(comp);
        store.set(cylCollision.getLocalTranslation());
        return cylCollision.getVolume() * this.getMaterial().getDensity();
    }

    public void buildRelationships(Map<OdePhysicalStructure, PhysicsNode> physicsMap, OdePhysicsEnvironment compiledEnvironment) {
        PhysicsCollisionGeometry[] involved = new PhysicsCollisionGeometry[] { meshCollision, cylCollision };
        for (PhysicalDecorator decorator : super.getPhysicalDecorators()) decorator.attach(physicsMap.get(this), involved);
    }

    @Override
    public float getLinearResonanceScalar() {
        return interpreter.getHeight() + 4f * interpreter.getRadius();
    }
}
