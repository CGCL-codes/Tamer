package nyc3d;

import nyc3d.ai.*;
import nyc3d.vehicle.*;
import nyc3d.street.*;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.collision.CollisionResults;
import com.jme3.input.ChaseCamera;
import com.jme3.input.JoyInput;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import nyc3d.settings.SettingsUtility;

public class NYC3D extends NYC3DApplication {

    public static void main(String[] args) {
        NYC3D app = new NYC3D();
        app.setShowSettings(false);
        app.loadSettings();
        app.start();
    }

    public void loadSettings() {
        try {
            settings = SettingsUtility.parseSettingsFromFile("settings.nyc");
        } catch (IOException e) {
        }
    }

    private BulletAppState bulletAppState;

    public static Vehicle currentCar;

    private ArrayList<Vehicle> vehicleList;

    public enum Status {

        FLYCAM, HUMAN, CAR
    }

    ;

    public static Status status;

    ArrayList<Node> nodes;

    ArrayList<TrafficLight> signalList;

    Node roads, cars, buildings, sidewalks;

    Human human;

    Vector3f flyCamLocation, humanCamLocation;

    Quaternion flyCamRotation, humanCamRotation;

    Streetlight streetLight1;

    PedestrianWalkSignal signal1, signal2, signal3;

    HUD hud;

    NiftyJmeDisplay niftyDisplay;

    Nifty nifty;

    public enum State {

        INTRO, MENU, GAME
    }

    ;

    public static State state = State.MENU;

    public static State desiredState = State.MENU;

    public static boolean exit, restartDisplay, showMouse, hideMouse, menuActive, showMenuTrigger, hideMenuTrigger, flyCamEnabledBefore, hudActive, day;

    public static AppSettings desiredSettings;

    private Sphere sphereMesh = new Sphere(10, 10, 100, false, true);

    private Geometry skySphere = new Geometry("Sky", sphereMesh);

    ChaseCamera carCam;

    private float steerDeadzone = .4f, triggerDeadzone = 1f;

    private final String DATE_FORMAT = "HH";

    private String currentHour = "";

    private DirectionalLight sun;

    private Spatial sky;

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        inputManager.deleteMapping("SIMPLEAPP_Exit");
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("menu.xml", "menu");
        guiViewPort.addProcessor(niftyDisplay);
        inputManager.setCursorVisible(true);
        guiNode.detachAllChildren();
        initJoystick();
        exit = false;
        restartDisplay = false;
        showMouse = hideMouse = menuActive = showMenuTrigger = hideMenuTrigger = false;
        flyCamEnabledBefore = true;
        desiredSettings = settings;
    }

    private void loadGame() {
        human = new Human(new Vector3f(0, 50, 0), cam, assetManager, inputManager, getPhysicsSpace(), rootNode);
        vehicleList = new ArrayList<Vehicle>();
        Driver ai = new Driver();
        status = Status.FLYCAM;
        nodes = new ArrayList<Node>();
        nodes.add(rootNode);
        roads = new Node("Roads");
        rootNode.attachChild(roads);
        nodes.add(roads);
        sidewalks = new Node("Sidewalks");
        rootNode.attachChild(sidewalks);
        nodes.add(sidewalks);
        cars = new Node("Cars");
        rootNode.attachChild(cars);
        nodes.add(cars);
        buildings = new Node("Buildings");
        rootNode.attachChild(buildings);
        nodes.add(buildings);
        cam.setLocation(new Vector3f(0, 50, 0));
        cam.lookAt(new Vector3f(1000, 0, 1000 * FastMath.tan(.1437440552f)), Vector3f.UNIT_Y);
        flyCamLocation = new Vector3f(cam.getLocation());
        humanCamLocation = new Vector3f(cam.getLocation());
        flyCamRotation = new Quaternion(cam.getRotation());
        humanCamRotation = new Quaternion(cam.getRotation());
        flyCam.setMoveSpeed(100f);
        cam.setFrustumFar(5000);
        try {
            Builder.buildRoadsFromFile("brooklynRoads.nyc", assetManager, getPhysicsSpace(), nodes);
            Builder.buildSidewalksFromFile("brooklynSidewalks.nyc", assetManager, getPhysicsSpace(), nodes);
            Builder.buildBuildingsFromFile("brooklynBuildings.nyc", assetManager, getPhysicsSpace(), nodes);
            Builder.buildCarsFromFile(vehicleList, "brooklynCars.nyc", settings, inputManager, viewPort, assetManager, getPhysicsSpace(), nodes);
            signalList = Builder.buildTrafficSignalsFromFile("brooklynTrafficSignals.nyc", assetManager, getPhysicsSpace(), nodes);
        } catch (IOException e) {
        }
        initTrafficLights();
        initHUD();
        sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false);
        sun = new DirectionalLight();
        Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        rootNode.setShadowMode(ShadowMode.Off);
        BasicShadowRenderer bsr = new BasicShadowRenderer(assetManager, 256);
        bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(bsr);
        hudActive = true;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        currentHour = sdf.format(cal.getTime());
        int hour = Integer.parseInt(currentHour);
        if (day = (hour >= 6 && hour < 20)) {
            attachSky();
            rootNode.addLight(sun);
        }
        initKeys();
        inputManager.setCursorVisible(false);
    }

    private void attachSky() {
        rootNode.attachChild(sky);
    }

    private void removeSky() {
        rootNode.detachChild(sky);
    }

    private void initKeys() {
        inputManager.addMapping("Switch Status", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Toggle HUD", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Enter/Exit Vehicle", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addListener(actionListener, new String[] { "Switch Status", "Pause", "Toggle HUD", "Enter/Exit Vehicle" });
    }

    private void initJoystick() {
        if (settings.useJoysticks()) {
            Joystick[] joysticks;
            if (inputManager.getJoysticks().length > 0) {
                joysticks = inputManager.getJoysticks();
                System.out.println("Joystick Detected (Not Necessarily Supported): " + joysticks[0].getName());
            }
        }
        inputManager.addMapping("Steer Left", new JoyAxisTrigger(0, 1, true));
        inputManager.addMapping("Steer Right", new JoyAxisTrigger(0, 1, false));
        inputManager.addMapping("Accelerate Vehicle", new JoyAxisTrigger(0, 4, true));
        inputManager.addMapping("Brake Vehicle", new JoyAxisTrigger(0, 4, false));
        inputManager.addMapping("Menu Up", new JoyAxisTrigger(0, JoyInput.AXIS_POV_Y, false));
        inputManager.addMapping("Menu Down", new JoyAxisTrigger(0, JoyInput.AXIS_POV_Y, true));
        inputManager.addMapping("Menu Left", new JoyAxisTrigger(0, JoyInput.AXIS_POV_X, true));
        inputManager.addMapping("Menu Right", new JoyAxisTrigger(0, JoyInput.AXIS_POV_X, true));
        inputManager.addMapping("Select Menu Option", new JoyButtonTrigger(0, 0));
        inputManager.addMapping("Menu Back", new JoyButtonTrigger(0, 1));
        inputManager.addListener(analogListener, new String[] { "Steer Left", "Steer Right", "Accelerate Vehicle", "Brake Vehicle" });
        inputManager.addListener(actionListener, new String[] { "Menu Up", "Menu Down", "Menu Left", "Menu Right", "Select Menu Option", "Menu Back" });
    }

    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Menu Down") && keyPressed && nifty != null && !nifty.getCurrentScreen().isNull()) {
                if (nifty.getCurrentScreen().getScreenId().equals("menu") || nifty.getCurrentScreen().getScreenId().equals("about")) nifty.getCurrentScreen().keyEvent(new KeyboardInputEvent(KeyboardInputEvent.KEY_DOWN, 'D', true, false, false));
                if (nifty.getCurrentScreen().getScreenId().equals("options")) nifty.getCurrentScreen().keyEvent(new KeyboardInputEvent(KeyboardInputEvent.KEY_TAB, 'T', true, false, false));
            } else if (name.equals("Menu Up") && keyPressed && nifty != null && !nifty.getCurrentScreen().isNull()) {
                if (nifty.getCurrentScreen().getScreenId().equals("menu") || nifty.getCurrentScreen().getScreenId().equals("about")) nifty.getCurrentScreen().keyEvent(new KeyboardInputEvent(KeyboardInputEvent.KEY_UP, 'U', true, false, false));
                if (nifty.getCurrentScreen().getScreenId().equals("options")) nifty.getCurrentScreen().keyEvent(new KeyboardInputEvent(KeyboardInputEvent.KEY_TAB, 'T', true, true, false));
            } else if (name.equals("Select Menu Option") && keyPressed && nifty != null && !nifty.getCurrentScreen().isNull()) {
                nifty.getCurrentScreen().keyEvent(new KeyboardInputEvent(KeyboardInputEvent.KEY_RETURN, 'R', true, false, false));
            }
            if (name.equals("Menu Back") && keyPressed && nifty != null && !nifty.getCurrentScreen().isNull()) {
                if (nifty.getCurrentScreen().getScreenId().equals("menu")) nifty.getCurrentScreen().findElementByName("exit").onClick();
                if (nifty.getCurrentScreen().getScreenId().equals("options")) nifty.getCurrentScreen().findElementByName("backButton2").onClick();
                if (nifty.getCurrentScreen().getScreenId().equals("about")) nifty.getCurrentScreen().findElementByName("returnToMainMenu").onClick();
            }
            if (!menuActive) {
                if (name.equals("Enter/Exit Vehicle") && status.equals(Status.HUMAN)) {
                    CollisionResults results = new CollisionResults();
                    Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                    nodes.get(3).collideWith(ray, results);
                    if (results.size() > 0) {
                        currentCar = vehicleList.get(nodes.get(3).getChildIndex(nodes.get(3).getChild(results.getClosestCollision().getGeometry().getName())));
                        status = Status.CAR;
                        cam.setRotation(new Quaternion());
                        cam.setLocation(new Vector3f(cam.getLocation().getX() - 25f, cam.getLocation().getY(), cam.getLocation().getZ()));
                        flyCam.setEnabled(false);
                        carCam = new ChaseCamera(cam, currentCar.getCarNode(), inputManager);
                        carCam.setEnabled(true);
                        carCam.setSmoothMotion(true);
                        carCam.setDefaultHorizontalRotation(FastMath.PI);
                        carCam.setDefaultVerticalRotation((1f / 5));
                        carCam.setDefaultDistance(10f);
                        carCam.setMaxDistance(15f);
                    }
                } else if (name.equals("Enter/Exit Vehicle") && status.equals(Status.CAR) && keyPressed) {
                    carCam.setEnabled(false);
                    flyCam.setEnabled(true);
                    currentCar = null;
                    status = Status.HUMAN;
                    inputManager.setCursorVisible(false);
                    cam.setLocation(cam.getLocation().add(new Vector3f(-1, 0, 0)));
                    cam.setRotation(humanCamRotation);
                }
                if (name.equals("Toggle HUD") && keyPressed) {
                    if (!hudActive) {
                        hudActive = true;
                        nifty.fromXml("hud.xml", "hud");
                        System.out.println(nifty.getCurrentScreen());
                    } else if (keyPressed) {
                        hudActive = false;
                        nifty.exit();
                    }
                }
                if (name.equals("Pause")) {
                    exit = true;
                    inputManager.setCursorVisible(true);
                }
                if (name.equals("Switch Status") && !keyPressed) {
                    if (status.equals(Status.FLYCAM)) {
                        status = Status.HUMAN;
                        flyCamLocation.set(cam.getLocation());
                        flyCamRotation.set(cam.getRotation());
                        cam.setLocation(humanCamLocation);
                        cam.setRotation(humanCamRotation);
                    } else {
                        if (status.equals(Status.HUMAN)) {
                            status = Status.FLYCAM;
                            inputManager.setCursorVisible(false);
                            humanCamLocation.set(cam.getLocation());
                            humanCamRotation.set(cam.getRotation());
                            cam.setLocation(flyCamLocation);
                            cam.setRotation(flyCamRotation);
                            flyCam.setEnabled(true);
                        }
                    }
                }
            }
        }
    };

    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float isPressed, float tpf) {
            float value = isPressed * 1000f;
            float steerValue = 0;
            if (FastMath.abs(value) >= steerDeadzone) steerValue = value / 4f;
            if (currentCar != null) {
                if (name.equals("Steer Left")) {
                    if (FastMath.abs(value) >= steerDeadzone) currentCar.getPlayer().steer(value / 4f); else currentCar.getPlayer().steer(0);
                } else {
                    if (name.equals("Steer Right")) {
                        if (FastMath.abs(value) >= steerDeadzone) currentCar.getPlayer().steer(-value / 4f); else currentCar.getPlayer().steer(0);
                    }
                }
                if (name.equals("Accelerate Vehicle")) {
                    if (FastMath.abs(value) >= triggerDeadzone) currentCar.getPlayer().accelerate(value * -800f); else currentCar.getPlayer().accelerate(0);
                } else if (name.equals("Brake Vehicle")) if (FastMath.abs(value) >= triggerDeadzone) currentCar.getPlayer().accelerate(value * 1000f); else currentCar.getPlayer().accelerate(0);
                System.out.println(value);
            }
        }
    };

    private void initHUD() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        hud = new HUD(settings);
    }

    private void initTrafficLights() {
        signal1 = new PedestrianWalkSignal("AvenueX&E1_1", PedestrianWalkSignal.Type.AWAY_AWAY, PedestrianWalkSignal.Model.SEPARATE, assetManager, new Vector3f(102.5f, 5f, 40), new Vector3f(0, -.1437440552f + FastMath.PI, 0), new Vector3f(1, 1, 1), rootNode);
        signal2 = new PedestrianWalkSignal("AvenueX&E1_2", PedestrianWalkSignal.Type.AWAY_AWAY, PedestrianWalkSignal.Model.ATTACHED, assetManager, new Vector3f(145, 5f, 47), new Vector3f(0, -.1437440552f, 0), new Vector3f(1, 1, 1), rootNode);
        signal3 = new PedestrianWalkSignal("AvenueX&E1_3", PedestrianWalkSignal.Type.AWAY_AWAY, PedestrianWalkSignal.Model.ATTACHED, assetManager, new Vector3f(110, 5f, -10), new Vector3f(0, -.1437440552f + FastMath.PI, 0), new Vector3f(1, 1, 1), rootNode);
        for (TrafficLight signal : signalList) {
            if (signal.getName().equals("Avenue_X&E2_North")) {
                signal.linkPedWalkSignal(signal1);
                signal.linkPedWalkSignal(signal2);
                signal.linkPedWalkSignal(signal3);
            }
        }
        Spatial oneWay1 = assetManager.loadModel("Models/OneWaySignLeft/OneWaySignLeft.j3o");
        oneWay1.setLocalTranslation(144.3f, 21, 47f);
        oneWay1.scale(1.5f, 0, 1.5f);
        oneWay1.rotate(FastMath.PI / 2, -.1437440552f + FastMath.PI / -2, 0);
        Material oneWay1mat = new Material(assetManager, "Common/MatDefs/Misc/ColoredTextured.j3md");
        oneWay1mat.setTexture("ColorMap", assetManager.loadTexture("Textures/oneWayRight.png"));
        oneWay1.setMaterial(oneWay1mat);
        rootNode.attachChild(oneWay1);
        Spatial aveXStreetSign = assetManager.loadModel("Models/StreetSign/Street Sign.j3o");
        aveXStreetSign.setLocalTranslation(-105.3f, 20f, -39.6f);
        aveXStreetSign.rotate(0, FastMath.PI - .1437440552f, 0);
        rootNode.attachChild(aveXStreetSign);
        Spatial e2SignBack = assetManager.loadModel("Models/e2/e2.j3o");
        e2SignBack.setLocalTranslation(-108f, 20f, -41.95f);
        e2SignBack.rotate(0, FastMath.PI / -2f - .1437440552f, 0);
        rootNode.attachChild(e2SignBack);
        streetLight1 = new Streetlight("Avenue X", Streetlight.Type.COBRA_HEAD, assetManager, new Vector3f(-108f, 2.5f, -40f), new Vector3f(0, (FastMath.PI * 3) / -4f - .1437440552f, 0), new Vector3f(0, -.1437440552f, 0), new Vector3f(1, 1, 1), rootNode, rootNode);
    }

    private void showMenu() {
        showMenuTrigger = false;
        inputManager.setCursorVisible(true);
        NYC3D.menuActive = true;
        if (!NYC3D.state.equals(NYC3D.State.MENU) && NYC3D.status.equals(NYC3D.Status.FLYCAM)) flyCam.setEnabled(false);
    }

    private void hideMenu() {
        hideMenuTrigger = false;
        NYC3D.menuActive = false;
        inputManager.setCursorVisible(false);
        if (!NYC3D.state.equals(NYC3D.State.MENU) && NYC3D.status.equals(NYC3D.Status.FLYCAM)) flyCam.setEnabled(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (showMenuTrigger) {
            showMenu();
        }
        if (hideMenuTrigger) hideMenu();
        if (restartDisplay && !settings.equals(desiredSettings)) {
            settings.copyFrom(desiredSettings);
            restartDisplay = false;
        }
        if (!state.equals(desiredState)) {
            switch(desiredState) {
                case GAME:
                    switch(state) {
                        case MENU:
                            loadGame();
                            hideMenu();
                            nifty.fromXml("hud.xml", "hud");
                            state = desiredState;
                            break;
                    }
                    break;
            }
        }
        switch(state) {
            case MENU:
                break;
            case GAME:
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                currentHour = sdf.format(cal.getTime());
                int hour = Integer.parseInt(currentHour);
                if ((hour >= 6 && hour < 20) && !day) {
                    day = true;
                    attachSky();
                    rootNode.addLight(sun);
                }
                if ((hour < 6 || hour >= 20) && day) {
                    day = false;
                    removeSky();
                    rootNode.removeLight(sun);
                }
                streetLight1.update();
                if (status.equals(Status.HUMAN)) human.update(cam);
                if (status.equals(Status.CAR)) {
                    rootNode.updateGeometricState();
                }
                for (TrafficLight signal : signalList) signal.update(tpf);
                if (!nifty.getCurrentScreen().isNull() && nifty.getCurrentScreen().getScreenId().equals("hud")) {
                    DecimalFormat carSpeedFormat = new DecimalFormat("0");
                    nifty.getCurrentScreen().findNiftyControl("currentStreet", Label.class).setText(hud.getCurrentStreet());
                    nifty.getCurrentScreen().findNiftyControl("currentTime", Label.class).setText(hud.getCurrentTime());
                    if (status.equals(Status.CAR)) {
                        nifty.getCurrentScreen().findNiftyControl("carSpeed", Label.class).setText(carSpeedFormat.format(FastMath.abs(Math.signum(NYC3D.currentCar.getPlayer().getCurrentVehicleSpeedKmHour()) * -1f * (3600f / 5280f) * FastMath.sqrt(FastMath.sqr(NYC3D.currentCar.getPlayer().getLinearVelocity().x) + FastMath.sqr(NYC3D.currentCar.getPlayer().getLinearVelocity().z)))));
                    } else nifty.getCurrentScreen().findNiftyControl("carSpeed", Label.class).setText("");
                }
                hud.update(nodes, cam, currentCar, nifty);
                skySphere.setLocalTranslation(cam.getLocation());
                if (exit) {
                    exit();
                    exit = false;
                }
                break;
        }
    }

    private void exit() {
        nifty.fromXml("game.xml", "exit");
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }
}
