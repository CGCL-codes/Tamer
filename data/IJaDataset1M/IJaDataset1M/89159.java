package com.siema.games.freeland.gameobjects;

import static org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.siema.games.freeland.main.ResourceManager;
import com.siema.games.freeland.main.ResourceManager.Snd;
import com.siema.games.freeland.main.ResourceManager.TiledTextReg;
import com.siema.games.freeland.main.Stock;
import com.siema.games.freeland.main.ResourceManager.Textr;

public class Avatar {

    private static final boolean RENDER_PHYSICS_BASE = false;

    public AnimatedSprite sprite;

    private float initialX = 100;

    private float initialY = 0;

    private Character character;

    public static boolean touchingGround = true;

    public static boolean jumping = false;

    public AvatarAnimationUpdateHandler animation;

    private static final FixtureDef mainFixtureDef = PhysicsFactory.createFixtureDef(3, 0, .1f);

    private static final FixtureDef sensorFixtureDef = PhysicsFactory.createFixtureDef(3, 0, .1f);

    private Rectangle sensorBottomCenterGeometry;

    public Body sensorBottomCenter;

    private Rectangle physicsCenterGeometry;

    private Body physicsCenter;

    private Vector2 physicsCenterWorldCenter;

    private boolean indestructible = false;

    private TimerHandler indestructibleTimed;

    public enum Character {

        TUX
    }

    ;

    public void loadResources() {
        ResourceManager.getInstance().loadSound(Snd.PLAYER_HIT);
        switch(this.character) {
            case TUX:
                ResourceManager.getInstance().loadTexture(Textr.PLAYER);
                break;
        }
    }

    public Avatar(Character character) {
        this.character = character;
        Avatar.sensorFixtureDef.isSensor = true;
    }

    public void init() {
        Stock stock = Stock.getInstance();
        this.sprite = new AnimatedSprite(-12.5f, -8, ResourceManager.getInstance().getTiledTextureRegion(TiledTextReg.TUX));
        this.animation = new AvatarAnimationUpdateHandler();
        this.sprite.registerUpdateHandler(this.animation);
        this.physicsCenterGeometry = new Rectangle(this.initialX, this.initialY, 20, 37);
        this.physicsCenterGeometry.setAlpha(0);
        stock.game.getScene().attachChild(this.physicsCenterGeometry);
        this.physicsCenterGeometry.attachChild(this.sprite);
        this.physicsCenter = PhysicsFactory.createBoxBody(stock.physicsWorld, this.physicsCenterGeometry, BodyType.DynamicBody, mainFixtureDef);
        stock.physicsWorld.registerPhysicsConnector(new PhysicsConnector(this.physicsCenterGeometry, this.physicsCenter, true, true));
        this.physicsCenter.setFixedRotation(true);
        this.physicsCenterWorldCenter = this.physicsCenter.getWorldCenter();
        this.sensorBottomCenterGeometry = new Rectangle(this.initialX + 9, this.initialY + 36, 3, 10);
        this.sensorBottomCenterGeometry.setVisible(false);
        stock.game.getScene().attachChild(this.sensorBottomCenterGeometry);
        this.sensorBottomCenter = PhysicsFactory.createBoxBody(stock.physicsWorld, this.sensorBottomCenterGeometry, BodyType.DynamicBody, sensorFixtureDef);
        stock.physicsWorld.registerPhysicsConnector(new PhysicsConnector(this.sensorBottomCenterGeometry, this.sensorBottomCenter, true, true));
        final WeldJointDef joint = new WeldJointDef();
        joint.initialize(this.physicsCenter, this.sensorBottomCenter, this.physicsCenterWorldCenter);
        stock.physicsWorld.createJoint(joint);
        if (RENDER_PHYSICS_BASE) {
            this.physicsCenterGeometry.setColor(0, 0, 0, .5f);
            this.sprite.setAlpha(.5f);
            this.sensorBottomCenterGeometry.setVisible(true);
            this.sensorBottomCenterGeometry.setColor(0, 1, 1, .5f);
        }
        this.indestructibleTimed = new TimerHandler(5, true, new ITimerCallback() {

            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                Avatar.this.indestructible = false;
                Stock.getInstance().game.getScene().unregisterUpdateHandler(Avatar.this.indestructibleTimed);
            }
        });
    }

    public void startJump() {
        this.animation.startJump();
        Avatar.jumping = true;
    }

    public void endJump() {
        this.animation.endJump();
        Avatar.jumping = false;
    }

    public float getHorizontalSpeed() {
        return this.physicsCenter.getLinearVelocity().x;
    }

    public float getVerticalSpeed() {
        return this.physicsCenter.getLinearVelocity().y;
    }

    public void applyForce(Vector2 force) {
        this.physicsCenter.applyForce(force, this.physicsCenter.getWorldCenter());
    }

    public void applyHorizontalForce(Vector2 force) {
        this.physicsCenter.applyForce(force, this.physicsCenter.getWorldCenter());
    }

    public void setHorizontalSpeed(float x) {
        this.setSpeed(x, this.getVerticalSpeed());
    }

    public void setVerticalSpeed(float y) {
        this.setSpeed(this.getHorizontalSpeed(), y);
    }

    public void setSpeed(float x, float y) {
        this.physicsCenter.setLinearVelocity(x, y);
    }

    public void setTouchingGround(boolean flag) {
        Avatar.touchingGround = flag;
        if (Avatar.touchingGround) {
            this.sensorBottomCenterGeometry.setColor(1, 0, 0);
            this.endJump();
            Stock.getInstance().score.breakCombo();
        } else {
            this.sensorBottomCenterGeometry.setColor(0, 0, 1);
        }
    }

    public void resetPosition() {
        this.physicsCenter.setTransform(this.initialX / PIXEL_TO_METER_RATIO_DEFAULT, this.initialY / PIXEL_TO_METER_RATIO_DEFAULT, 0);
    }

    public float getVisualY() {
        return this.physicsCenterGeometry.getY();
    }

    public boolean damage() {
        if (!this.indestructible) {
            this.indestructible = true;
            this.sprite.registerEntityModifier(ObjectsAnimation.getPlayerIndestructible(this.sprite));
            Stock.getInstance().game.getScene().registerUpdateHandler(this.indestructibleTimed);
            Stock.getInstance().game.wikiItems = 0;
            ResourceManager.getInstance().getSound(Snd.PLAYER_HIT).play();
            this.animation.damage();
            Stock.getInstance().inputUpdateHandler.disableInputTimed(1.5f);
            float factor;
            if (this.getHorizontalSpeed() > 0) {
                factor = -1;
            } else {
                factor = 1;
            }
            this.setHorizontalSpeed(5 * factor);
            this.setVerticalSpeed(-10);
            return true;
        } else {
            return false;
        }
    }

    public void bounce(Body sensor) {
        if (this.physicsCenter.getWorldCenter().y < sensor.getWorldCenter().y && this.getVerticalSpeed() > 3) {
            float bounceSpeed = 0;
            if (this.getVerticalSpeed() > 30) {
                bounceSpeed = -30;
            } else if (this.getVerticalSpeed() < 10) {
                bounceSpeed = -10;
            } else {
                bounceSpeed = -this.getVerticalSpeed();
            }
            this.setVerticalSpeed(bounceSpeed);
        }
    }
}
