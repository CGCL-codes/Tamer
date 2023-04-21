package jme3test.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

public class TestSoftwareMouse extends SimpleApplication {

    private Picture cursor;

    private RawInputListener inputListener = new RawInputListener() {

        private float x = 0, y = 0;

        public void beginInput() {
        }

        public void endInput() {
        }

        public void onJoyAxisEvent(JoyAxisEvent evt) {
        }

        public void onJoyButtonEvent(JoyButtonEvent evt) {
        }

        public void onMouseMotionEvent(MouseMotionEvent evt) {
            x += evt.getDX();
            y += evt.getDY();
            AppSettings settings = TestSoftwareMouse.this.settings;
            x = FastMath.clamp(x, 0, settings.getWidth());
            y = FastMath.clamp(y, 0, settings.getHeight());
            cursor.setPosition(x, y - 64);
        }

        public void onMouseButtonEvent(MouseButtonEvent evt) {
        }

        public void onKeyEvent(KeyInputEvent evt) {
        }

        public void onTouchEvent(TouchEvent evt) {
        }
    };

    public static void main(String[] args) {
        TestSoftwareMouse app = new TestSoftwareMouse();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        Texture tex = assetManager.loadTexture("Interface/Logo/Cursor.png");
        cursor = new Picture("cursor");
        cursor.setTexture(assetManager, (Texture2D) tex, true);
        cursor.setWidth(64);
        cursor.setHeight(64);
        guiNode.attachChild(cursor);
        inputManager.addRawInputListener(inputListener);
    }
}
