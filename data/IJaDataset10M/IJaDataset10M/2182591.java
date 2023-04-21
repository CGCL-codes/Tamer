package vn.edu.hcmup.fit.k34.c104.arpostcard;

import java.io.IOException;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import vn.edu.hcmup.fit.k34.c104.arpostcard.scenes.IScene;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Light;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.World;
import android.app.Activity;
import android.database.Cursor;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;

public class AugmentedRealityRenderer implements Renderer {

    FrameBuffer mBuffer;

    World mWorld;

    Activity mActivity;

    private final RGBColor mBackgroundColor = new RGBColor(0, 0, 0, 0);

    private MapHelper mMap;

    private IScene[] mScenes;

    private String[] mNames;

    private Light mLight;

    private int mActiveId;

    public AugmentedRealityRenderer(Activity activity) {
        mActivity = activity;
        mWorld = new World();
        vn.edu.hcmup.fit.k34.c104.arpostcard.Config.mWorld = mWorld;
        vn.edu.hcmup.fit.k34.c104.arpostcard.Config.mResources = mActivity.getResources();
        mLight = new Light(mWorld);
        mLight.setIntensity(255, 255, 0);
        mLight.setDiscardDistance(Config.farPlane);
        mWorld.getCamera().setFOV(mWorld.getCamera().convertDEGAngleIntoFOV(45.66f));
        initEngine();
        loadScenes();
    }

    private void loadScenes() {
        mMap = new MapHelper(mActivity);
        try {
            mMap.createDataBase();
            mMap.openDataBase();
        } catch (IOException e) {
            throw new Error("Unable to interact with database");
        }
        Cursor cursor = mMap.query();
        cursor.moveToFirst();
        mScenes = new IScene[cursor.getCount()];
        mNames = new String[cursor.getCount()];
        int nameIdx = cursor.getColumnIndex("name");
        int classIdx = cursor.getColumnIndex("class");
        int idx = 0;
        while (true) {
            try {
                mNames[idx] = cursor.getString(nameIdx);
                Class<?> tmp = Class.forName(cursor.getString(classIdx));
                mScenes[idx] = (IScene) tmp.newInstance();
                idx++;
                if (cursor.isLast()) {
                    break;
                } else {
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                cursor.close();
                e.printStackTrace();
                throw new RuntimeException("Damaged database");
            }
        }
        cursor.close();
    }

    private Object[] collisionResult;

    public boolean onTouchEvent(int actionType, int pointer, float x, float y) {
        if (mActiveId < 0 || mBuffer == null || mWorld == null) {
            return false;
        }
        switch(actionType) {
            case 2:
                collisionResult = mWorld.calcMinDistanceAndObject3D(mWorld.getCamera().getPosition(), Interact2D.reproject2D3D(mWorld.getCamera(), mBuffer, (int) x, (int) y).normalize(), Config.farPlane);
                Logger.log("onTouch x: " + x + ", y: " + y);
                if ((Float) collisionResult[0] != Object3D.COLLISION_NONE) {
                    Object3D o = (Object3D) collisionResult[1];
                    mScenes[mActiveId].onPick(o);
                    Logger.log("Object: " + o);
                    while (o.getParents().length > 0) {
                        o = o.getParents()[0];
                        Logger.log("Parent - Object: " + o);
                    }
                }
                break;
        }
        return false;
    }

    private void initEngine() {
        Config.farPlane = Float.MAX_VALUE;
        Config.glTransparencyMul = 0.1f;
        Config.glTransparencyOffset = 0.1f;
        Config.useVBO = true;
        Texture.defaultToMipmapping(true);
        Texture.defaultTo4bpp(true);
    }

    long mLastUpdateStamp = 0, mCurrentStamp;

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mBuffer == null) {
            return;
        }
        for (IScene scene : mScenes) {
            scene.setVisibility(false);
        }
        mActiveId = -1;
        if (!vn.edu.hcmup.fit.k34.c104.arpostcard.Config.CUT_OFF_MODE && RealityRenderer.mNumActiveTrackables > 0) {
            for (int i = 0; i < mNames.length; i++) {
                if (mNames[i].equals(RealityRenderer.mActiveTrackableName)) {
                    mActiveId = i;
                    break;
                }
            }
        }
        if (mActiveId >= 0) {
            mScenes[mActiveId].loadScene();
            mScenes[mActiveId].setVisibility(true);
            if (!vn.edu.hcmup.fit.k34.c104.arpostcard.Config.CUT_OFF_MODE) {
                mWorld.getCamera().setBack(RealityRenderer.mActiveModelViewMatrix);
            }
            mLight.setPosition(mWorld.getCamera().getPosition());
            mScenes[mActiveId].onDrawFrame(gl);
            mCurrentStamp = SystemClock.uptimeMillis();
            if (mCurrentStamp - mLastUpdateStamp > vn.edu.hcmup.fit.k34.c104.arpostcard.Config.UPDATE_INTERVAL) {
                mScenes[mActiveId].onUpdate();
            }
        }
        mBuffer.clear(mBackgroundColor);
        mWorld.renderScene(mBuffer);
        try {
            mWorld.draw(mBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBuffer.display();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mBuffer != null) {
            mBuffer.dispose();
        }
        if (!vn.edu.hcmup.fit.k34.c104.arpostcard.Config.CUT_OFF_MODE) {
            mBuffer = new FrameBuffer(width, height);
        } else {
            mBuffer = new FrameBuffer(gl, width, height);
            mWorld.getCamera().setBack(vn.edu.hcmup.fit.k34.c104.arpostcard.Config.TEST_MATRIX);
            mActiveId = vn.edu.hcmup.fit.k34.c104.arpostcard.Config.TEST_SCENE_ID;
        }
        vn.edu.hcmup.fit.k34.c104.arpostcard.Config.mBuffer = mBuffer;
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
    }
}
