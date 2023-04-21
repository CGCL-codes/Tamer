package com.cinosynachosama.freeland;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.anddev.andengine.extension.physics.box2d.util.triangulation.ITriangulationAlgoritm;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class game extends BaseGameActivity implements IUpdateHandler {

    private static final int CAMERA_WIDTH = 426;

    private static final int CAMERA_HEIGHT = 240;

    private BoundCamera mBoundChaseCamera;

    private BitmapTextureAtlas mTexture;

    private TiledTextureRegion mPlayerTextureRegion;

    private TMXTiledMap mTMXTiledMap;

    private TMXCollidablePolygonMaker polygonMaker = new TMXCollidablePolygonMaker();

    private PhysicsWorld mPhysicsWorld;

    private Body playeBody;

    private AnimatedSprite player;

    public static boolean leftPressed = false;

    public static boolean rightPressed = false;

    public static boolean jumpPressed = false;

    private TextureRegion leftButtonTR;

    private TextureRegion jumpButtonTR;

    private TextureRegion actionButtonTR;

    private TextureRegion rightButtonTR;

    private BitmapTextureAtlas fondoTexture;

    private TextureRegion fondoTR;

    private BitmapTextureAtlas fontTexture;

    private Font font;

    private Sprite leftButton;

    private Sprite rightButton;

    private Sprite jumpButton;

    private Sprite actionButton;

    private ChangeableText textHud;

    private float jumpPressedTime = 0;

    private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0f, 0.1f);

    enum Control {

        LEFT, RIGHT, JUMP, ACTION
    }

    ;

    enum State {

        STOPPED, WALKING, RUNNING
    }

    ;

    State lastState = State.STOPPED;

    public static Control[] thumbPressed = new Control[2];

    private boolean notMoving;

    private boolean firstRunningSpeed;

    private boolean secondRunningSpeed;

    private long timeFrame = 0;

    private FPSLogger fps;

    @Override
    public Engine onLoadEngine() {
        this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera));
    }

    @Override
    public void onLoadResources() {
        this.mTexture = new BitmapTextureAtlas(256, 512, TextureOptions.NEAREST);
        this.fondoTexture = new BitmapTextureAtlas(512, 256, TextureOptions.NEAREST);
        this.fontTexture = new BitmapTextureAtlas(128, 128, TextureOptions.NEAREST);
        this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mTexture, this, "gfx/sonic.png", 0, 0, 5, 3);
        this.mEngine.getTextureManager().loadTextures(this.mTexture);
        this.leftButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/hud/leftButton.png", 0, 135);
        this.rightButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/hud/rightButton.png", 85, 135);
        this.actionButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/hud/actionButton.png", 170, 135);
        this.jumpButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/hud/jumpButton.png", 158, 85);
        this.fondoTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.fondoTexture, this, "gfx/fondo.png", 0, 0);
        this.font = FontFactory.createStroke(this.fontTexture, Typeface.DEFAULT_BOLD, 15, true, Color.YELLOW, 1, Color.BLACK);
        this.mEngine.getFontManager().loadFont(this.font);
        this.mEngine.getTextureManager().loadTextures(this.mTexture, this.fondoTexture, this.fontTexture);
    }

    @Override
    public Scene onLoadScene() {
        final Scene scene = new Scene();
        this.fps = new FPSLogger();
        this.mEngine.registerUpdateHandler(this.fps);
        try {
            this.mEngine.setTouchController(new MultiTouchController());
        } catch (MultiTouchException e) {
            e.printStackTrace();
        }
        try {
            final TMXLoader tmxLoader = new TMXLoader(this, this.mEngine.getTextureManager(), TextureOptions.NEAREST, new ITMXTilePropertiesListener() {

                @Override
                public void onTMXTileWithPropertiesCreated(TMXTiledMap pTMXTiledMap, TMXLayer pTMXLayer, TMXTile pTMXTile, TMXProperties<TMXTileProperty> pTMXTileProperties) {
                }
            });
            this.mTMXTiledMap = tmxLoader.loadFromAsset(this, "tmx/testcol.tmx");
        } catch (final TMXLoadException tmxle) {
            Debug.e(tmxle);
        }
        final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
        polygonMaker.generatePolygonsCreated(this.mTMXTiledMap, tmxLayer);
        scene.attachChild(tmxLayer);
        scene.attachChild(this.mTMXTiledMap.getTMXLayers().get(1));
        this.mBoundChaseCamera.setBounds(0, tmxLayer.getWidth(), 0, tmxLayer.getHeight());
        this.mBoundChaseCamera.setBoundsEnabled(true);
        this.player = new AnimatedSprite(CAMERA_WIDTH / 2 - 20, 10, this.mPlayerTextureRegion);
        this.mBoundChaseCamera.setChaseEntity(this.player);
        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0f, 1f);
        Iterator<Polygon> i = this.polygonMaker.poligonList.iterator();
        while (i.hasNext()) {
            List<Vector2> vertexList = i.next().vertexList;
            Iterator<Vector2> it = vertexList.iterator();
            while (it.hasNext()) {
                Vector2 v = it.next();
                v.x = v.x / PIXEL_TO_METER_RATIO_DEFAULT;
                v.y = v.y / PIXEL_TO_METER_RATIO_DEFAULT;
            }
            if (vertexList.size() > 2) {
                Shape shape = new Rectangle(0, 0, 0, 0);
                final ITriangulationAlgoritm triangulationAlgoritm = new EarClippingTriangulator();
                final List<Vector2> triangles = triangulationAlgoritm.computeTriangles(vertexList);
                PhysicsFactory.createTrianglulatedBody(this.mPhysicsWorld, shape, triangles, BodyType.StaticBody, wallFixtureDef);
                scene.attachChild(shape);
            }
        }
        Vector2[] playerShape = new Vector2[8];
        playerShape[0] = new Vector2(20, 6);
        playerShape[1] = new Vector2(25, 6);
        playerShape[2] = new Vector2(32, 13);
        playerShape[3] = new Vector2(32, 37);
        playerShape[4] = new Vector2(25, 45);
        playerShape[5] = new Vector2(20, 45);
        playerShape[6] = new Vector2(13, 37);
        playerShape[7] = new Vector2(13, 13);
        int c = 0;
        while (c < playerShape.length) {
            playerShape[0].x = (playerShape[0].x - 22) / PIXEL_TO_METER_RATIO_DEFAULT;
            playerShape[0].y = (playerShape[0].y - 22) / PIXEL_TO_METER_RATIO_DEFAULT;
            c++;
        }
        this.playeBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, this.player, BodyType.DynamicBody, FIXTURE_DEF);
        this.playeBody.setFixedRotation(true);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(player, this.playeBody, true, true));
        scene.attachChild(player);
        scene.setBackground(new SpriteBackground(0, 0, 0, new Sprite(0, 0, this.fondoTR)));
        scene.registerUpdateHandler(this.mPhysicsWorld);
        scene.registerUpdateHandler(this);
        HUD hud = new HUD();
        this.mBoundChaseCamera.setHUD(hud);
        this.textHud = new ChangeableText(5, 5, this.font, "", 200);
        hud.attachChild(this.textHud);
        this.leftButton = new Sprite(0, CAMERA_HEIGHT - 85, this.leftButtonTR);
        this.rightButton = new Sprite(85, CAMERA_HEIGHT - 85, this.rightButtonTR);
        this.jumpButton = new Sprite(CAMERA_WIDTH - 85, CAMERA_HEIGHT - 85, this.jumpButtonTR);
        this.actionButton = new Sprite(CAMERA_WIDTH - 85 * 2, CAMERA_HEIGHT - 85, this.actionButtonTR);
        hud.setOnAreaTouchTraversalFrontToBack();
        hud.setTouchAreaBindingEnabled(true);
        hud.getLastChild().attachChild(this.leftButton);
        hud.getLastChild().attachChild(this.rightButton);
        hud.getLastChild().attachChild(this.jumpButton);
        hud.getLastChild().attachChild(this.actionButton);
        Rectangle ctrlLeft = new MultitouchControl(0, 0, 85, CAMERA_HEIGHT, Control.LEFT);
        Rectangle ctrlRight = new MultitouchControl(85, 0, 85, CAMERA_HEIGHT, Control.RIGHT);
        Rectangle ctrlJump = new MultitouchControl(CAMERA_WIDTH - 85, 0, 85, CAMERA_HEIGHT, Control.JUMP);
        ctrlLeft.setColor(1, 0, 0, 0);
        ctrlRight.setColor(0, 1, 0, 0);
        ctrlJump.setColor(0, 0, 1, 0);
        hud.attachChild(ctrlLeft);
        hud.attachChild(ctrlRight);
        hud.attachChild(ctrlJump);
        hud.registerTouchArea(ctrlLeft);
        hud.registerTouchArea(ctrlRight);
        hud.registerTouchArea(ctrlJump);
        return scene;
    }

    @Override
    public void onLoadComplete() {
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        NumberFormat formatter = new DecimalFormat("0.00");
        Vector2 newForce = new Vector2(0, 0);
        short impulseX = 0;
        short impulseY = 0;
        this.leftButton.setColor(.5f, .5f, .5f, .5f);
        this.rightButton.setColor(.5f, .5f, .5f, .5f);
        this.actionButton.setColor(.5f, .5f, .5f, 0);
        this.jumpButton.setColor(.5f, .5f, .5f, .5f);
        if (game.leftPressed) {
            this.leftButton.setColor(1, 1, 1, .5f);
            Log.d("FREELAND", "Left Pressed");
            impulseX = -1;
        }
        if (game.rightPressed) {
            this.rightButton.setColor(1, 1, 1, .5f);
            Log.d("FREELAND", "Right Pressed");
            impulseX = 1;
        }
        if (game.jumpPressed) {
            this.jumpButton.setColor(1, 1, 1, .5f);
            Log.d("FREELAND", "Jump Pressed");
            impulseY = -1;
            this.jumpPressedTime += pSecondsElapsed;
        } else {
            this.jumpPressedTime = 0;
        }
        if (impulseX != 0) {
            newForce.x = impulseX * 20;
        }
        if (impulseY != 0 && this.jumpPressedTime <= .2) {
            newForce.y = impulseY * 80;
        }
        game.this.playeBody.applyForce(newForce, new Vector2(game.this.playeBody.getWorldCenter()));
        if (game.this.playeBody.getLinearVelocity().x >= 20) {
            game.this.playeBody.setLinearVelocity(new Vector2(20, game.this.playeBody.getLinearVelocity().y));
        }
        if (game.this.playeBody.getLinearVelocity().y >= 10) {
            game.this.playeBody.setLinearVelocity(new Vector2(game.this.playeBody.getLinearVelocity().x, 10));
        }
        if (game.this.playeBody.getLinearVelocity().x <= -20) {
            game.this.playeBody.setLinearVelocity(new Vector2(-20, game.this.playeBody.getLinearVelocity().y));
        }
        if (game.this.playeBody.getLinearVelocity().y <= -10) {
            game.this.playeBody.setLinearVelocity(new Vector2(game.this.playeBody.getLinearVelocity().x, -10));
        }
        if (game.this.playeBody.getLinearVelocity().x < 0) {
            game.this.player.getTextureRegion().setFlippedHorizontal(true);
        } else if (game.this.playeBody.getLinearVelocity().x > 0) {
            game.this.player.getTextureRegion().setFlippedHorizontal(false);
        }
        State currentState = State.STOPPED;
        if (game.this.playeBody.getLinearVelocity().x == 0 && !this.notMoving) {
            currentState = State.STOPPED;
        } else if (!this.firstRunningSpeed && Math.abs(game.this.playeBody.getLinearVelocity().x) >= 0 && Math.abs(game.this.playeBody.getLinearVelocity().x) < 10) {
            currentState = State.WALKING;
        } else if (!this.secondRunningSpeed && Math.abs(game.this.playeBody.getLinearVelocity().x) >= 10) {
            currentState = State.RUNNING;
        }
        if (this.lastState != currentState) {
            switch(currentState) {
                case STOPPED:
                    this.timeFrame = 0;
                    Log.d("FREELAND", "Player stopped");
                    if (game.this.player.isAnimationRunning()) {
                        game.this.player.stopAnimation(0);
                    }
                    break;
                case WALKING:
                    Log.d("FREELAND", "Player start to walk");
                    this.timeFrame = 50;
                    game.this.player.animate(new long[] { timeFrame, timeFrame, timeFrame, timeFrame }, 1, 4, true);
                    break;
                case RUNNING:
                    this.timeFrame = 100;
                    Log.d("FREELAND", "Player start to run");
                    game.this.player.animate(new long[] { timeFrame, timeFrame, timeFrame, timeFrame }, 5, 8, true);
                    break;
            }
        }
        this.lastState = currentState;
        game.this.textHud.setText("SPEED: X:" + formatter.format(game.this.playeBody.getLinearVelocity().x) + " Y:" + formatter.format(game.this.playeBody.getLinearVelocity().y) + "\nJUMPTIMEPRESSED: " + formatter.format(this.jumpPressedTime) + "\nFORCE: X:" + formatter.format(newForce.x) + " Y:" + formatter.format(newForce.y) + "\nFRAMETIME: " + this.timeFrame + "\n" + "FPS: " + this.fps.getFPS());
    }

    @Override
    public void reset() {
    }
}
