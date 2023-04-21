package com.endgamefraction.androidinfection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import objectManager.CObjectManager;
import objectManager.CNPC;
import collisionSystem.CCollision;

public class AndroidInfection extends Activity {

    private GLSurface mSurface = null;

    private CNPC obj1 = null;

    private CNPC obj2 = null;

    private CObjectManager objManagerInstance = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSurface = new GLSurface(this);
        obj1 = new CNPC();
        obj2 = new CNPC();
        objManagerInstance = CObjectManager.getObjectManager();
        obj1.setPosX(50.0f);
        obj1.setPosY(50.0f);
        obj1.circle.center.setX(-1.0f);
        obj1.circle.center.setY(1.0f);
        obj1.circle.radius = 1.0f;
        obj2.setPosX(150.0f);
        obj2.setPosY(150.0f);
        obj2.circle.center.setX(0.5f);
        obj2.circle.center.setY(0.5f);
        obj2.circle.radius = 1.0f;
        objManagerInstance.AddObject(obj1);
        objManagerInstance.AddObject(obj2);
        if (CCollision.CircleToCircle(obj1.circle, obj2.circle)) Log.d("COLLISION TEST", "SUCCESS: The circles are colliding"); else Log.d("COLLISION TEST", "FAIL: The circles are NOT colliding");
        super.onCreate(savedInstanceState);
        setContentView(mSurface);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
