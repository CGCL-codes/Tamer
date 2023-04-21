package com.yxl.test.lesson9;

import com.jme.image.Texture;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.math.spring.SpringPoint;
import com.jme.math.spring.SpringPointForce;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.cloth.ClothPatch;
import com.jmex.effects.cloth.ClothUtils;
import com.jmex.terrain.TerrainBlock;

/**
 * Flag保存了游戏中的“goal”。驾驶员将为了分数尝试获取Flag。 这个类的主要工作是创建flag geometry，
 * 还有在一段时间后在平面内随机移动自己的位置
 * 
 * @author yxl
 */
public class Flag extends Node {

    private static final int LIFT_TIME = 10;

    private float countdown = LIFT_TIME;

    private TerrainBlock tb;

    private ClothPatch cloth;

    private float windStrength = 15f;

    private Vector3f windDirection = new Vector3f(0.8f, 0, 0.2f);

    private SpringPointForce gravity, wind;

    public double score = 0;

    public Flag(TerrainBlock tb) {
        super("flag");
        this.tb = tb;
        Cylinder cylinder = new Cylinder("pole", 10, 10, 0.5f, 50);
        this.attachChild(cylinder);
        Quaternion q = new Quaternion();
        q.fromAngleAxis(FastMath.PI / 2, new Vector3f(1, 0, 0));
        cylinder.setLocalRotation(q);
        cylinder.setLocalTranslation(new Vector3f(-12.5f, -12.5f, 0));
        this.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
        this.setLocalScale(0.25f);
        cloth = new ClothPatch("cloth", 25, 25, 1f, 100000);
        wind = new RandomFlagWindForce(windStrength, windDirection);
        cloth.addForce(wind);
        gravity = ClothUtils.createBasicGravity();
        cloth.addForce(gravity);
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setTexture(TextureManager.loadTexture(Flag.class.getClassLoader().getResource("jmetest/data/images/Monkey.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        cloth.setRenderState(ts);
        PointLight dr = new PointLight();
        dr.setEnabled(true);
        dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        dr.setLocation(new Vector3f(0.5f, -0.5f, 0));
        LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.setTwoSidedLighting(true);
        lightState.attach(dr);
        setRenderState(lightState);
        LightNode lightNode = new LightNode("light");
        lightNode.setLight(dr);
        lightNode.setLocalTranslation(new Vector3f(15, 0, 0));
        attachChild(lightNode);
        cloth.setRenderState(lightState);
        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(CullState.Face.None);
        cloth.setRenderState(cs);
        this.attachChild(cloth);
        for (int i = 0; i < 25; i++) {
            cloth.getSystem().getNode(i * 25).position.y *= .8f;
            cloth.getSystem().getNode(i * 25).setMass(Float.POSITIVE_INFINITY);
        }
    }

    /**
     * 在update期间，我们减少time。 当它到了0，我们重设flag
     * 
     * @param time 间隔2帧的时间
     */
    public void update(float time, float distance) {
        calculateScore(distance);
        countdown -= time;
        if (countdown <= 0) {
            score -= .5f;
            showScore();
            reset();
        }
    }

    /**
     * 设置生命时间为10， 然后随机在terrain上放置flag
     */
    private void reset() {
        countdown = LIFT_TIME;
        placeFlag();
    }

    /**
     * 在terrain上选择一个随机点并在那放置flag。 设置值在（45和175）之间，这在force平面里面
     */
    public void placeFlag() {
        float x = 45 + FastMath.nextRandomFloat() * 130;
        float z = 45 + FastMath.nextRandomFloat() * 130;
        float y = tb.getHeight(x, z) + 7.5f;
        localTranslation.x = x;
        localTranslation.y = y;
        localTranslation.z = z;
    }

    /**
     * 随机改变的风
     * @author YXL
     */
    private class RandomFlagWindForce extends SpringPointForce {

        /**风力*/
        private final float strength;

        /**风的方向*/
        private final Vector3f windDirection;

        /**
         * 创建一个随机改变的风
         * @param strength 风力
         * @param direction 风向
         */
        public RandomFlagWindForce(float strength, Vector3f direction) {
            this.strength = strength;
            this.windDirection = direction;
        }

        /**
         * 调整风力风向
         */
        @Override
        public void apply(float dt, SpringPoint node) {
            windDirection.x += dt * (FastMath.nextRandomFloat() - 0.5f);
            windDirection.z += dt * (FastMath.nextRandomFloat() - 0.5f);
            windDirection.normalize();
            float tStr = FastMath.nextRandomFloat() * strength;
            node.acceleration.addLocal(windDirection.x * tStr, windDirection.y * tStr, windDirection.z * tStr);
        }
    }

    /**
     * 计算得分
     * @param distance 距离
     */
    public void calculateScore(float distance) {
        if (distance < 200) {
            reset();
            score += countdown / 10;
            showScore();
        }
    }

    ;

    /**
     * 显示得分
     */
    public void showScore() {
        System.out.println("现在得分: " + score);
    }
}
