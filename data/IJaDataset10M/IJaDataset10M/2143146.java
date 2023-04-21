package com.msli.jme.model;

import com.msli.core.exception.AlreadyInitializedException;
import com.msli.core.exception.NotYetInitializedException;
import com.msli.core.util.CoreUtils;
import com.msli.graphic.math.MathUtils;
import com.msli.jme.JmeBundle;
import com.msli.jme.view.JmeViewContext;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Pyramid;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.TextureState;
import com.jme.util.GameTask;
import com.jme.util.TextureManager;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;

/**
 * A debug scene model, which contains a spinning texture mapped cube, a
 * stationary blinking sphere, a sliding blinking pyramid, and a blinking
 * background color.
 * <p>
 * Demonstrates all major forms of model state update, including view context
 * switching, high level (via nested model)and low level (via scenegraph)
 * content updates, and indirect updates via queued tasks executed on the OpenGL
 * thread. Also demonstrates model serialization, via Savable.
 * <P>
 * Derived from jmetest.util.JMESWTTest.MyImplementor.
 * @author jonb
 */
public class JmeDebugModel {

    private JmeDebugModel() {
    }

    /**
	 * Creates a new instance of the standard JmeDebugModel. The model consists
	 * of a parent "cube" model with a single child "pyramid" model.
	 */
    public static JmeTreeNode newInstance() {
        CubeModel parentModel = new CubeModel("JmeDebugModel.CubeModel");
        JmeTreeNode parentNode = new JmeTreeNode.Impl(parentModel);
        PyramidModel childModel = new PyramidModel("JmeDebugModel.PyramidModel");
        JmeTreeNode childNode = new JmeTreeNode.Impl(childModel);
        parentNode.attachChild(childNode);
        parentModel.initModel(childNode);
        childModel.initModel();
        parentNode.activateSubtree(true);
        return parentNode;
    }

    /**
	 * A texture mapped Cube model centered on a ball model.  The cube rotates
	 * while the ball blinks, via scenegraph update.  If set, a target model node
	 * also blinks, via node activation.
	 * @author jonb
	 */
    public static class CubeModel extends JmeScene {

        /**
		 * Used by the system for serialization.
		 */
        public CubeModel() {
            super();
        }

        /**
		 * Creates an instance. Must call initModel() before use.
		 * @param name Model node name.  Never null.
		 */
        public CubeModel(String name) {
            super(name);
        }

        /**
		 * Called by the client to initialize this model.
		 * @param target Ceded exposed target model node. None if null.
		 * @throws AlreadyInitializedException if model was already initialized.
		 */
        public void initModel(JmeTreeNode targetNode) {
            AlreadyInitializedException.assertOk(!_isModelInit);
            _isModelInit = true;
            getRootNode().setName("CubeModel.Root");
            _targetNode = targetNode;
            _boxRpm = 15f;
            _boxAxis = new Vector3f(1, 1, 0.5f).normalizeLocal();
            _boxAngle = 0.0f;
            _blinkRpm = 30f;
            _blinkTime = 0f;
            _isBlinkShow = false;
            Vector3f max = new Vector3f(5, 5, 5);
            Vector3f min = new Vector3f(-5, -5, -5);
            _box = new Box("CubeModel.Root.Box", min, max);
            _box.setModelBound(new BoundingBox());
            _box.updateModelBound();
            _box.setLocalTranslation(new Vector3f(0, 0, -10));
            _box.setDefaultColor(ColorRGBA.white);
            getRootNode().attachChild(_box);
            URL textureUrl = JmeBundle.getInstance().newBundleUrl("data/Monkey.jpg");
            _boxTexture = TextureManager.loadTexture(textureUrl, MinificationFilter.Trilinear, MagnificationFilter.Bilinear);
            _ball = new Sphere("CubeModel.Root.Ball", Vector3f.ZERO, 30, 30, 6.5f);
            _ball.setModelBound(new BoundingBox());
            _ball.updateModelBound();
            _ball.setLocalTranslation(new Vector3f(0, 0, -10));
            _ball.setDefaultColor(ColorRGBA.randomColor());
            _blinkShowBg = ColorRGBA.randomColor();
            _blinkHideBg = ColorRGBA.randomColor();
        }

        @Override
        public void doViewSetup(final JmeViewContext context) {
            NotYetInitializedException.assertOk(_isModelInit);
            _isFirstViewUpdate = true;
            TextureState ts = context.getRenderer().createTextureState();
            ts.setEnabled(true);
            ts.setTexture(_boxTexture);
            _box.setRenderState(ts);
            GameTask<Object> task;
            task = new GameTask<Object>(new Callable<Object>() {

                public Object call() {
                    context.getRenderer().setBackgroundColor(_blinkShowBg);
                    return null;
                }
            });
            _blinkShowTasks.put(context, task);
            task = new GameTask<Object>(new Callable<Object>() {

                public Object call() {
                    context.getRenderer().setBackgroundColor(_blinkHideBg);
                    return null;
                }
            });
            _blinkHideTasks.put(context, task);
        }

        @Override
        public void doViewUpdate(JmeViewContext context, double tickTime, double tickDelta) {
            if (_viewUpdateTime == tickTime) return;
            _viewUpdateTime = tickTime;
            float boxFreq = _boxRpm / 60f;
            _boxAngle += (tickDelta * boxFreq) * 360f;
            _boxAngle = MathUtils.F.modFloor(_boxAngle, 0, 360);
            _dummyQuat.fromAngleNormalAxis(_boxAngle * FastMath.DEG_TO_RAD, _boxAxis);
            _box.setLocalRotation(_dummyQuat);
            float blinkPeriod = 60f / _blinkRpm;
            float blinkDuty = blinkPeriod / 2.0f;
            float blinkTimeOld = _blinkTime;
            _blinkTime += tickDelta;
            boolean isBlinkUpdate = false;
            if (blinkTimeOld < blinkDuty && _blinkTime >= blinkDuty) {
                isBlinkUpdate = true;
                _isBlinkShow = true;
            } else if (blinkTimeOld < blinkPeriod && _blinkTime >= blinkPeriod) {
                isBlinkUpdate = true;
                _isBlinkShow = false;
            }
            if (_isFirstViewUpdate) {
                isBlinkUpdate = true;
            }
            _isFirstViewUpdate = false;
            _blinkTime = MathUtils.F.modFloor(_blinkTime, 0, blinkPeriod);
            if (isBlinkUpdate && _isBlinkShow) {
                if (!getRootNode().hasChild(_ball)) {
                    getRootNode().attachChild(_ball);
                }
                if (_targetNode != null) {
                    _targetNode.setActive(true);
                }
                for (Map.Entry<JmeViewContext, GameTask<Object>> entry : _blinkShowTasks.entrySet()) {
                    JmeViewContext ctx = entry.getKey();
                    GameTask<Object> task = entry.getValue();
                    ctx.getRenderQueue().enqueue(task.getCallable());
                }
            }
            if (isBlinkUpdate && !_isBlinkShow) {
                if (_targetNode != null) {
                    _targetNode.setActive(false);
                }
                getRootNode().detachChild(_ball);
                for (Map.Entry<JmeViewContext, GameTask<Object>> entry : _blinkHideTasks.entrySet()) {
                    JmeViewContext ctx = entry.getKey();
                    GameTask<Object> task = entry.getValue();
                    ctx.getRenderQueue().enqueue(task.getCallable());
                }
            }
            getRootNode().updateRenderState();
            getRootNode().updateGeometricState((float) tickDelta, true);
            notifyUpdateObservers();
        }

        @Override
        public void doViewCleanup(JmeViewContext context) {
            _blinkShowTasks.remove(context);
            _blinkHideTasks.remove(context);
        }

        /**
		 * Should not be called by the client.
		 */
        @Override
        public void write(JMEExporter porter) throws IOException {
            CoreUtils.assertNotDisposed(this);
            super.write(porter);
            OutputCapsule capsule = porter.getCapsule(this);
            capsule.write(_isModelInit, "_isModelInit", false);
            capsule.write(_boxRpm, "_boxRpm", 0f);
            capsule.write(_boxAxis, "_boxAxis", null);
            capsule.write(_boxAngle, "_boxAngle", 0f);
            capsule.write(_box, "_box", null);
            capsule.write(_boxTexture, "_boxTexture", null);
            capsule.write(_ball, "_ball", null);
            capsule.write(_blinkRpm, "_blinkRpm", 0f);
            capsule.write(_blinkTime, "_blinkTime", 0f);
            capsule.write(_blinkShowBg, "_blinkShowBg", null);
            capsule.write(_blinkHideBg, "_blinkHideBg", null);
            capsule.write(_isBlinkShow, "_isBlinkShow", false);
            capsule.write(_targetNode, "_targetNode", null);
        }

        /**
		 * Should not be called by the client.
		 */
        @Override
        public void read(JMEImporter porter) throws IOException {
            CoreUtils.assertNotDisposed(this);
            super.read(porter);
            InputCapsule capsule = porter.getCapsule(this);
            _isModelInit = capsule.readBoolean("_isModelInit", false);
            _boxRpm = capsule.readFloat("_boxRpm", 0f);
            _boxAxis = (Vector3f) capsule.readSavable("_boxAxis", null);
            _boxAngle = capsule.readFloat("_boxAngle", 0f);
            _box = (Box) capsule.readSavable("_box", null);
            _boxTexture = (Texture) capsule.readSavable("_boxTexture", null);
            _ball = (Sphere) capsule.readSavable("_ball", null);
            _blinkRpm = capsule.readFloat("_blinkRpm", 0f);
            _blinkTime = capsule.readFloat("_blinkTime", 0f);
            _blinkShowBg = (ColorRGBA) capsule.readSavable("_blinkShowBg", null);
            _blinkHideBg = (ColorRGBA) capsule.readSavable("_blinkHideBg", null);
            _isBlinkShow = capsule.readBoolean("_isBlinkShow", false);
            _targetNode = (JmeTreeNode) capsule.readSavable("_targetNode", null);
        }

        /**
		 * Should not be called by the client.
		 */
        @Override
        public Class<?> getClassTag() {
            CoreUtils.assertNotDisposed(this);
            return getClass();
        }

        @Override
        protected void implDispose() {
            super.implDispose();
            _boxAxis = CoreUtils.dispose(_boxAxis);
            _box = CoreUtils.dispose(_box);
            _boxTexture = CoreUtils.dispose(_boxTexture);
            _ball = CoreUtils.dispose(_ball);
            _blinkShowBg = CoreUtils.dispose(_blinkShowBg);
            _blinkHideBg = CoreUtils.dispose(_blinkHideBg);
            _targetNode = CoreUtils.dispose(_targetNode);
            _blinkShowTasks = CoreUtils.dispose(_blinkShowTasks);
            _blinkHideTasks = CoreUtils.dispose(_blinkHideTasks);
            _dummyQuat = CoreUtils.dispose(_dummyQuat);
        }

        private boolean _isModelInit = false;

        private JmeTreeNode _targetNode = null;

        private float _boxRpm;

        private Vector3f _boxAxis;

        private float _boxAngle;

        private Box _box;

        private Texture _boxTexture;

        private Sphere _ball;

        private float _blinkRpm;

        private float _blinkTime;

        private ColorRGBA _blinkShowBg;

        private ColorRGBA _blinkHideBg;

        private boolean _isBlinkShow;

        private transient Quaternion _dummyQuat = new Quaternion();

        private transient boolean _isFirstViewUpdate;

        private transient double _viewUpdateTime = Double.NaN;

        private transient Map<JmeViewContext, GameTask<Object>> _blinkShowTasks = new HashMap<JmeViewContext, GameTask<Object>>();

        private transient Map<JmeViewContext, GameTask<Object>> _blinkHideTasks = new HashMap<JmeViewContext, GameTask<Object>>();
    }

    /**
	 * A pyramid shape that slides back and forth along the X axis.
	 * @author jonb
	 */
    public static class PyramidModel extends JmeScene {

        /**
		 * Used by the system for serialization.
		 */
        public PyramidModel() {
            super();
        }

        /**
		 * Creates an instance. Must call initModel() before use.
		 * @param name Model node name.  Never null.
		 */
        public PyramidModel(String name) {
            super(name);
        }

        /**
		 * Called by the client to initialize this model.
		 * @throws AlreadyInitializedException if model was already initialized.
		 */
        public void initModel() {
            AlreadyInitializedException.assertOk(!_isModelInit);
            _isModelInit = true;
            getRootNode().setName("PyramidModel.Root");
            _slideRpm = 15f;
            _extentX = 7f;
            _slideX = 0;
            _posY = 1f;
            _posZ = -5f;
            Pyramid pyramid = new Pyramid("PyramidModel.Root.Pyramid", 12f, 12f);
            pyramid.setModelBound(new BoundingBox());
            pyramid.updateModelBound();
            pyramid.setLocalTranslation(new Vector3f(0, _posY, _posZ));
            pyramid.setDefaultColor(ColorRGBA.randomColor());
            getRootNode().attachChild(pyramid);
        }

        @Override
        public void doViewSetup(JmeViewContext context) {
            NotYetInitializedException.assertOk(_isModelInit);
        }

        @Override
        public void doViewUpdate(JmeViewContext context, double tickTime, double tickDelta) {
            if (_viewUpdateTime == tickTime) return;
            _viewUpdateTime = tickTime;
            float slideFreq = _slideRpm / 60f;
            float slideExtent = 4 * _extentX;
            _slideX += (tickDelta * slideFreq) * slideExtent;
            _slideX = MathUtils.F.modFloor(_slideX, 0, slideExtent);
            float posX = _slideX;
            if (posX > _extentX) {
                posX = _extentX - (posX - _extentX);
            }
            if (posX < -_extentX) {
                posX = -_extentX - (posX + _extentX);
            }
            getRootNode().setLocalTranslation(new Vector3f(posX, _posY, _posZ));
            getRootNode().updateRenderState();
            getRootNode().updateGeometricState((float) tickDelta, true);
        }

        /**
		 * Should not be called by the client.
		 */
        @Override
        public void write(JMEExporter porter) throws IOException {
            CoreUtils.assertNotDisposed(this);
            super.write(porter);
            OutputCapsule capsule = porter.getCapsule(this);
            capsule.write(_isModelInit, "_isModelInit", false);
            capsule.write(_slideRpm, "_slideRpm", 0f);
            capsule.write(_extentX, "_extentX", 0f);
            capsule.write(_slideX, "_slideX", 0f);
            capsule.write(_posY, "_posY", 0f);
            capsule.write(_posZ, "_posZ", 0f);
        }

        /**
		 * Should not be called by the client.
		 */
        @Override
        public void read(JMEImporter porter) throws IOException {
            CoreUtils.assertNotDisposed(this);
            super.read(porter);
            InputCapsule capsule = porter.getCapsule(this);
            _isModelInit = capsule.readBoolean("_isModelInit", false);
            _slideRpm = capsule.readFloat("_slideRpm", 0f);
            _extentX = capsule.readFloat("_extentX", 0f);
            _slideX = capsule.readFloat("_slideX", 0f);
            _posY = capsule.readFloat("_posY", 0f);
            _posZ = capsule.readFloat("_posZ", 0f);
        }

        /**
		 * Should not be called by the client.
		 */
        @Override
        public Class<?> getClassTag() {
            CoreUtils.assertNotDisposed(this);
            return getClass();
        }

        @Override
        protected void implDispose() {
            super.implDispose();
        }

        private boolean _isModelInit = false;

        private float _slideRpm;

        private float _extentX;

        private float _slideX;

        private float _posY;

        private float _posZ;

        private transient double _viewUpdateTime = Double.NaN;
    }
}
