package uy.com.pnocetti.spaceshooter.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.TextureOptions;
import android.content.Context;

public class ShotCreator implements IUpdateHandler {

    private Context mContext;

    private ArrayList<Shot> mCurrent;

    private Engine mEngine;

    private Date mLastDate = new Date();

    private PhysicsWorld mPhysicsWorld;

    private Scene mScene;

    private Ship mShip;

    private Queue<Shot> mShotPool;

    private boolean mToCreate;

    private ArrayList<Shot> mToRemove;

    public ShotCreator(Scene mScene, PhysicsWorld mPhysicsWorld, Ship mShip, Engine mEngine, Context mContext) {
        super();
        this.mScene = mScene;
        this.mPhysicsWorld = mPhysicsWorld;
        this.mShip = mShip;
        this.mEngine = mEngine;
        this.mContext = mContext;
        this.mCurrent = new ArrayList<Shot>();
        this.mToRemove = new ArrayList<Shot>();
        this.mShotPool = new ArrayBlockingQueue<Shot>(20);
        this.mToCreate = false;
        for (int i = 1; i <= 20; i++) {
            Shot en1 = new Shot(this.mShip, this);
            this.mShotPool.add(en1);
        }
    }

    public void create() {
        Date current = new Date();
        if (current.getTime() - mLastDate.getTime() >= 300) {
            this.mToCreate = true;
        }
    }

    public Integer getShotsLength() {
        return mCurrent.size();
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (this.mToCreate) {
            Shot newE = mShotPool.poll();
            if (newE != null) {
                newE.setParent(mShip);
                newE.setRemoved(false);
                newE.setTexture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
                newE.setTextureRegion(mContext, "ship/shot.png", 0, 0);
                newE.onLoadResources(mEngine);
                newE.onLoadScene(mPhysicsWorld, mScene);
                mCurrent.add(newE);
                mScene.registerUpdateHandler(newE);
                mLastDate = new Date();
            }
            this.mToCreate = false;
        }
        for (Shot mRem : mToRemove) {
            final PhysicsConnector remPhysicsConnector = this.mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(mRem.getSprite());
            if (remPhysicsConnector != null) {
                this.mPhysicsWorld.destroyBody(remPhysicsConnector.getBody());
                this.mPhysicsWorld.unregisterPhysicsConnector(remPhysicsConnector);
            }
            this.mScene.getLastChild().detachChild(mRem.getSprite());
            this.mCurrent.remove(mRem);
            if (mShotPool.size() < 20) this.mShotPool.add(mRem);
        }
        mToRemove.clear();
    }

    public void remove(Shot mRem) {
        mRem.setRemoved(true);
        mToRemove.add(mRem);
    }

    @Override
    public void reset() {
    }
}
