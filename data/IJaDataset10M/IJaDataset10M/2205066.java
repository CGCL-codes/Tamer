package eu.cherrytree.paj.test;

import eu.cherrytree.paj.base.AppState;
import eu.cherrytree.paj.base.JoypadInput.DPad;
import eu.cherrytree.paj.base.KeyboardInput.Key;
import eu.cherrytree.paj.base.MouseInput.MouseButton;
import eu.cherrytree.paj.graphics.Camera;
import eu.cherrytree.paj.graphics.CameraManager;
import eu.cherrytree.paj.graphics.EnvironmentManager;
import eu.cherrytree.paj.graphics.Graphics;
import eu.cherrytree.paj.graphics.RenderManager;
import eu.cherrytree.paj.graphics.Transformation;
import eu.cherrytree.paj.graphics.constantgeometry.BufferedGeometry;
import eu.cherrytree.paj.graphics.constantgeometry.ConstantMesh;
import eu.cherrytree.paj.graphics.constantgeometry.ConstantMeshManager;
import eu.cherrytree.paj.graphics.light.Light;
import eu.cherrytree.paj.graphics.light.LightManager;
import eu.cherrytree.paj.graphics.light.Light.LightType;
import eu.cherrytree.paj.gui.UITextList.UITextListElement;
import eu.cherrytree.paj.math.Vector3d;
import eu.cherrytree.paj.physics.CollisionVolume.CollisionShapeType;
import eu.cherrytree.paj.physics.PhysicalEntity;
import eu.cherrytree.paj.physics.PhysicsManager;

public class PhysicsTestState extends AppState {

    RenderManager renderManager;

    PhysicsManager physicsManager;

    PhysicalEntity ENTITY_1, ENTITY_2, ENTITY_3;

    ConstantMesh mesh_1, mesh_2, mesh_3;

    Camera cam;

    public PhysicsTestState() throws Throwable {
        super();
        renderManager = new RenderManager();
        Light.LightType[] lights = { LightType.OMNILIGHT };
        EnvironmentManager.requestShader(lights, false);
        Graphics.setClearColor(0.75f, 0.75f, 0.75f);
        LightManager.setGlobalIlumination(1.0f, 1.0f, 1.0f, 0.15f);
        LightManager.createOmniLight(new Vector3d(0.0f, 0.0f, 0.0f));
        cam = CameraManager.getActiveCamera();
        cam.setLocation(new Vector3d(0.0f, 0.0f, 10.0f), true);
        cam.setTarget(new Vector3d(0.0f, 0.0f, 0.0f));
        BufferedGeometry.setBoundingBoxGeneration(CollisionShapeType.BOX_SHAPE);
        physicsManager = new PhysicsManager();
        physicsManager.setGravity(new Vector3d(0.0f, -10.0f, 0.0f));
        mesh_1 = ConstantMeshManager.loadMesh("ship.pam");
        mesh_2 = ConstantMeshManager.loadMesh("ship.pam");
        mesh_3 = ConstantMeshManager.loadMesh("ship.pam");
        ENTITY_1 = new PhysicalEntity(10.0f, mesh_1.getCollisionVolume());
        ENTITY_2 = new PhysicalEntity(15.0f, mesh_2.getCollisionVolume());
        ENTITY_3 = new PhysicalEntity(0.0f, mesh_3.getCollisionVolume());
        Transformation transformation = new Transformation();
        transformation.setLocation(0.0f, -3.0f, 0.0f);
        ENTITY_3.setTransformation(transformation);
        transformation.setLocation(0.0f, 3.0f, 0.0f);
        ENTITY_1.setTransformation(transformation);
        transformation.setLocation(0.0f, 0.0f, 0.0f);
        ENTITY_2.setTransformation(transformation);
        ENTITY_2.setLinearVelocity(new Vector3d(0.0f, 1.5f, 0.0f));
        ENTITY_1.setRotationalVelocity(new Vector3d(10.0f, 1.5f, 0.0f));
        physicsManager.addPhysicalEntity(ENTITY_1);
        physicsManager.addPhysicalEntity(ENTITY_2);
        physicsManager.addPhysicalEntity(ENTITY_3);
    }

    @Override
    protected void update(float frame) {
        physicsManager.update(frame);
        ENTITY_1.sync();
        ENTITY_2.sync();
        ENTITY_3.sync();
        mesh_1.setTransformation(ENTITY_1.getTransformation());
        mesh_2.setTransformation(ENTITY_2.getTransformation());
        mesh_3.setTransformation(ENTITY_3.getTransformation());
        mesh_1.render();
        mesh_2.render();
        System.out.println("VEL: " + ENTITY_1.getLinearVelocity());
        System.out.println("ROT: " + ENTITY_1.getRotationalVelocity());
        mesh_3.render();
        renderManager.renderDirect(true);
    }

    @Override
    public void onJoypadButtonPress(int joypad_id, int button_id) {
    }

    @Override
    public void onJoypadButtonRelease(int joypad_id, int button_id) {
    }

    @Override
    public void onJoypadDPad(int joypad_id, DPad dpad) {
    }

    @Override
    public void onJoypadAxisChange(int joypad_id, int axis_id, float value) {
    }

    @Override
    public void onButtonPress(int button_id) {
    }

    @Override
    public void onButtonRelease(int button_id) {
    }

    @Override
    public void onTextListPress(int list_id, UITextListElement element) {
    }

    @Override
    public void onTextListRelease(int list_id, UITextListElement element) {
    }

    @Override
    public void onTextInputAccept(int input_id, String text) {
    }

    @Override
    protected void onKeyDown(Key key) {
    }

    @Override
    protected void onKeyUp(Key key) {
    }

    @Override
    protected void onMouseMove(int x, int y) {
    }

    @Override
    protected void onMouseDown(MouseButton button, int x, int y) {
    }

    @Override
    protected void onMouseUp(MouseButton button, int x, int y) {
    }
}
