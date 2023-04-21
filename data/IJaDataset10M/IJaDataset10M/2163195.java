package org.jives.implementors.engine.jme;

import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;
import org.jives.core.Jives;
import org.jives.core.JivesActiveNode;
import org.jives.core.JivesScene;
import org.jives.network.NetworkAddress;
import org.jives.network.RemoteActiveNode;
import org.jives.sim.ActiveNodeModelIntf;
import org.jives.sim.JivesRenderableIntf;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;

/**
 * 
 * @author adriano
 *
 */
public class PlayingCharacterRenderer implements ActiveNodeModelIntf, ActionListener, AnimEventListener {

    float airTime = 0;

    boolean left = false, right = false, up = false, down = false;

    @Delegate
    JivesActiveNode activeNode;

    PhysicsSpace physics;

    CharacterControl character;

    AnimChannel animationChannel;

    AnimControl animationControl;

    private float updateInterval = 0;

    private boolean undefinedPosition;

    private boolean initialized;

    @Getter
    protected static ChaseCamera chaseCam;

    @Setter
    protected static String modelPath = "Models/Oto/Oto.mesh.xml";

    private Vector3f oldPosition;

    private Vector3f newPosition;

    private static float SEND_RATE = 1;

    public PlayingCharacterRenderer(PhysicsSpace physics, JivesActiveNode activeNode) {
        this.physics = physics;
        this.activeNode = activeNode;
        this.initialized = false;
    }

    private void initialize() {
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(3f, 4f);
        character = new CharacterControl(capsule, 0.01f);
        JMEImplementor implementor = (JMEImplementor) Jives.getEngine();
        AssetManager assetManager = implementor.getAssetManager();
        Node model = (Node) assetManager.loadModel(modelPath);
        model.setShadowMode(ShadowMode.CastAndReceive);
        animationControl = model.getControl(AnimControl.class);
        animationControl.addListener(this);
        animationChannel = animationControl.createChannel();
        getProperties().put("model", model);
        model.addControl(character);
        physics.add(character);
        implementor.getSceneModelReference().getSceneRootNode().attachChild(model);
        BitmapText playerText = new BitmapText(implementor.getDefaultFont(), false);
        playerText.scale(0.05f);
        playerText.setQueueBucket(Bucket.Translucent);
        NetworkAddress address = (NetworkAddress) getProperties().get(RemoteActiveNode.PROPERTY_NETWORK_ADDRESS);
        String playerName = address.getPeerName();
        playerText.setText(playerName);
        float textWidth = playerText.getLineWidth() + 20f;
        float textOffset = textWidth / 2f;
        playerText.setBox(new Rectangle(-textOffset, 0f, textWidth, playerText.getHeight()));
        playerText.setAlignment(BitmapFont.Align.Center);
        playerText.setShadowMode(ShadowMode.Off);
        model.attachChild(playerText);
        BoundingBox bounding = (BoundingBox) model.getWorldBound();
        playerText.addControl(new BillboardControl());
        playerText.setLocalTranslation(0, bounding.getYExtent() + 2f, 0);
        if (JivesScene.getMyself().equals(activeNode)) {
            Vector3f position;
            if (getProperties().get("position") == null) {
                position = new Vector3f(-140, 11.8f, -10);
                getProperties().put("position", position);
            } else {
                position = (Vector3f) getProperties().get("position");
            }
            position.y = 11.8f;
            character.setPhysicsLocation(position);
            undefinedPosition = false;
            chaseCam = new ChaseCamera(implementor.getCamera(), model, implementor.getInputManager());
            chaseCam.setMinVerticalRotation(0.02f);
            setupKeys();
        } else {
            undefinedPosition = true;
        }
        initialized = true;
    }

    @Override
    public void render(JivesRenderableIntf renderable, Object context) {
        if (!initialized) {
            initialize();
        }
        float tpf = (Float) renderable.getProperties().get("tpf");
        if (JivesScene.getMyself().equals(activeNode)) {
            JMEImplementor implementor = (JMEImplementor) Jives.getEngine();
            Camera cam = implementor.getCamera();
            Vector3f camDir = cam.getDirection().clone().normalizeLocal();
            Vector3f camLeft = cam.getLeft().clone().normalizeLocal();
            camDir.y = 0;
            camLeft.y = 0;
            Vector3f walkDirection = new Vector3f();
            walkDirection.set(0, 0, 0);
            if (left) {
                walkDirection.addLocal(camLeft);
            }
            if (right) {
                walkDirection.addLocal(camLeft.negate());
            }
            if (up) {
                walkDirection.addLocal(camDir);
            }
            if (down) {
                walkDirection.addLocal(camDir.negate());
            }
            walkDirection.normalizeLocal().multLocal(0.1f);
            if (!character.onGround()) {
                airTime = airTime + tpf;
            } else {
                airTime = 0;
            }
            if (walkDirection.length() == 0) {
                if (!"stand".equals(animationChannel.getAnimationName())) {
                    animationChannel.setAnim("stand", 1f);
                }
            } else {
                character.setViewDirection(walkDirection);
                if (airTime > 1f) {
                    if (!"stand".equals(animationChannel.getAnimationName())) {
                        animationChannel.setAnim("stand");
                    }
                } else if (!"Walk".equals(animationChannel.getAnimationName())) {
                    animationChannel.setAnim("Walk", 0.7f);
                }
            }
            character.setWalkDirection(walkDirection);
            getProperties().put("direction", walkDirection);
            getProperties().put("position", character.getPhysicsLocation());
            if (updateInterval > SEND_RATE) {
                updateInterval = 0;
                activeNode.onPropertyUpdate();
            } else {
                updateInterval += tpf;
            }
        } else {
            if (newPosition == null) {
                newPosition = (Vector3f) getProperties().get("position");
            }
            Vector3f direction = (Vector3f) getProperties().get("direction");
            if (newPosition != null && direction != null) {
                if (undefinedPosition) {
                    undefinedPosition = false;
                    oldPosition = new Vector3f(newPosition);
                    character.setPhysicsLocation(newPosition);
                    character.setWalkDirection(direction);
                } else {
                    updateInterval += tpf;
                    if (updateInterval >= SEND_RATE) {
                        updateInterval -= SEND_RATE;
                        oldPosition = new Vector3f(newPosition);
                        newPosition = (Vector3f) getProperties().get("position");
                    }
                    float t = updateInterval / SEND_RATE;
                    Vector3f position = oldPosition.mult(1 - t).add(newPosition.mult(t));
                    character.setPhysicsLocation(position);
                    Vector3f newDirection = newPosition.subtract(oldPosition).normalize();
                    if (newDirection.length() == 0 || Math.abs(newDirection.y) > 0.9) {
                        newDirection = new Vector3f();
                    } else {
                        newDirection.y = 0;
                        newDirection.normalize();
                        getProperties().put("direction", newDirection);
                        character.setViewDirection(newDirection);
                        character.setWalkDirection(newDirection);
                    }
                    if (newDirection.length() <= 0.1) {
                        animationChannel.setAnim("stand", 1f);
                    } else if (animationChannel.getAnimationName() != null && !animationChannel.getAnimationName().equals("Walk")) {
                        animationChannel.setAnim("Walk", 0.7f);
                    }
                }
            }
        }
    }

    private void setupKeys() {
        JMEImplementor implementor = (JMEImplementor) Jives.getEngine();
        InputManager inputManager = implementor.getInputManager();
        inputManager.addListener(this, "CharLeft");
        inputManager.addListener(this, "CharRight");
        inputManager.addListener(this, "CharUp");
        inputManager.addListener(this, "CharDown");
        inputManager.addListener(this, "CharJump");
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("CharUp", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("CharDown", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("CharJump", new KeyTrigger(KeyInput.KEY_RCONTROL));
    }

    @Override
    public void onPropertyUpdate() {
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("CharLeft")) {
            if (value) {
                left = true;
                right = false;
            } else {
                left = false;
            }
        } else if (binding.equals("CharRight")) {
            if (value) {
                right = true;
                left = false;
            } else {
                right = false;
            }
        } else if (binding.equals("CharUp")) {
            if (value) {
                up = true;
                down = false;
            } else {
                up = false;
            }
        } else if (binding.equals("CharDown")) {
            if (value) {
                down = true;
                up = false;
            } else {
                down = false;
            }
        } else if (binding.equals("CharJump")) {
            character.jump();
        }
    }
}
