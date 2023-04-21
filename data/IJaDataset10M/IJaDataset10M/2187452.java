package battlezone;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

/**
 * The view - what the user actually sees
 * Coordinates are passed into view with x being left/right, y being in/out of the screen, and z being up/down
 * Inside the view, coordinates are transformed to match Java 3D's coordinate system of x being left/right, y being up/down, and z being in/out
 */
public class View extends JFrame {

    private static Configuration _configuration = Configuration.getInstance();

    private Controller _controller;

    private Canvas3D _canvas3D;

    private Canvas2D _canvas2D;

    private JPanel mainPane;

    private SimpleUniverse _universe;

    private BranchGroup _scene;

    private BranchGroup _dynamicScene;

    private boolean _exitDialogIsOpen;

    TransformGroup _transformGroupSight;

    public View() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Battlezone");
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        _canvas3D = new Canvas3D(config);
        _canvas2D = new Canvas2D();
        _canvas2D.setMinimumSize(new Dimension(Short.MAX_VALUE, 150));
        _canvas2D.setPreferredSize(new Dimension(Short.MAX_VALUE, 150));
        _canvas2D.setMaximumSize(new Dimension(Short.MAX_VALUE, 150));
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(_canvas2D);
        contentPane.add(_canvas3D);
        if (Configuration.getInstance().getUseFullScreen()) {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        } else {
            this.setSize(800, 600);
        }
        this.setVisible(true);
        _universe = new SimpleUniverse(_canvas3D);
        javax.media.j3d.View view = _universe.getViewer().getView();
        view.setBackClipDistance(50);
        _scene = createSceneGraph(_universe);
        _universe.addBranchGraph(_scene);
    }

    public void setController(Controller controller) {
        if (_controller != controller) {
            _controller = controller;
            this.removeKeyListener(_controller);
            this.addKeyListener(_controller);
            _controller.setView(this);
        }
    }

    public void drawGraphics(Tank myTank, Collection<BattlezoneObject> othersTanksAndAllProjectiles, short remainingLives, short remainingAmmo) {
        moveCamera(myTank);
        _canvas2D.setLives(remainingLives);
        _canvas2D.setAmmo(remainingAmmo);
        _canvas2D.setObjects(othersTanksAndAllProjectiles);
        _canvas2D.setMyTank(myTank);
        _canvas2D.repaint();
    }

    private BranchGroup createSceneGraph(SimpleUniverse su) {
        BranchGroup objRoot = new BranchGroup();
        _dynamicScene = new BranchGroup();
        _dynamicScene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        _dynamicScene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        objRoot.addChild(_dynamicScene);
        objRoot.compile();
        return objRoot;
    }

    /**
     * Build background
     * EXAMPLE WITH A SIMPLE IMAGE
     * @return
     */
    private BranchGroup createBackground() {
        Background backg = new Background(0.0f, 0.0f, 1.0f);
        BranchGroup backgroundGroup = new BranchGroup();
        backg.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), 1000));
        try {
            BufferedImage image = ImageIO.read(new File("resources/space.jpg"));
            ImageComponent2D imageComponent2D = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, image);
            backg.setImage(imageComponent2D);
        } catch (IOException e) {
            System.out.println("Unable to load image background.jpg");
        }
        backgroundGroup.addChild(backg);
        return backgroundGroup;
    }

    private BranchGroup buildField() {
        BranchGroup parent = new BranchGroup();
        float shift = 10.0f;
        LineArray landGeom = new LineArray((int) (_configuration.getFieldWidth() * 2 / shift + 2 + _configuration.getFieldHeight() * 2 / shift + 2), GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        int pointCount = 0;
        for (float y = -_configuration.getFieldHeight() / 2; y <= _configuration.getFieldHeight() / 2; y += shift) {
            landGeom.setCoordinate(pointCount++, new Point3f(-_configuration.getFieldWidth() / 2, y, 0));
            landGeom.setCoordinate(pointCount++, new Point3f(_configuration.getFieldWidth() / 2, y, 0));
        }
        for (float x = -_configuration.getFieldWidth() / 2; x <= _configuration.getFieldWidth() / 2; x += shift) {
            landGeom.setCoordinate(pointCount++, new Point3f(x, -_configuration.getFieldHeight() / 2, 0));
            landGeom.setCoordinate(pointCount++, new Point3f(x, _configuration.getFieldHeight() / 2, 0));
        }
        for (int i = 0; i < pointCount; i++) {
            landGeom.setColor(i, _configuration.getForegroundColor());
        }
        parent.addChild(new Shape3D(landGeom));
        return parent;
    }

    /**
     * TEST to build gun sight
     * @return
     */
    private BranchGroup buildSight() {
        BranchGroup parent = new BranchGroup();
        LineArray landGeom = new LineArray(2, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        landGeom.setCoordinate(0, new Point3f(0.0f, 0, 0));
        landGeom.setCoordinate(0, new Point3f(0.0f, 20, 0));
        Color3f c = new Color3f(0.1f, 0.8f, 0.1f);
        for (int i = 0; i < 2; i++) {
            landGeom.setColor(i, c);
        }
        _transformGroupSight = new TransformGroup();
        _transformGroupSight.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        _transformGroupSight.addChild(new Shape3D(landGeom));
        parent.addChild(_transformGroupSight);
        return parent;
    }

    /**
     * TEST to move gun sight
     * @param myTank
     */
    private void adjustTransform(Tank myTank) {
        Transform3D newTransform = new Transform3D();
        newTransform.rotZ(2 * Math.PI - myTank.getAzimuthInRadians());
        newTransform.setTranslation(new Vector3f(myTank.getPositionX(), myTank.getPositionY(), 0));
        _transformGroupSight.setTransform(newTransform);
    }

    private void moveCamera(Tank myTank) {
        TransformGroup viewingPlatformTransformGroup = _universe.getViewingPlatform().getViewPlatformTransform();
        Transform3D xTransform = new Transform3D();
        xTransform.rotX(Math.PI / 2);
        xTransform.setTranslation(new Vector3f(myTank.getPositionX(), myTank.getPositionY(), 2));
        Transform3D yTransform = new Transform3D();
        yTransform.rotY(2 * Math.PI - myTank.getAzimuthInRadians());
        xTransform.mul(yTransform);
        viewingPlatformTransformGroup.setTransform(xTransform);
    }

    public void addObject(BattlezoneObject object) {
        _dynamicScene.addChild(object.getBranchGroup());
    }

    public void playMoveSound() {
    }

    public void playFireSound() {
    }

    public void playKillSound() {
    }

    public void showExitDialog() {
        _exitDialogIsOpen = true;
    }

    public void hideExitDialog() {
        _exitDialogIsOpen = false;
    }

    public boolean getExitDialogIsOpen() {
        return _exitDialogIsOpen;
    }
}
