package com.allesblinkt.leicasdream;

import java.io.FileInputStream;
import mathematik.Vector3f;
import gestalt.candidates.JoglGLUTBitmapFont;
import gestalt.context.DisplayCapabilities;
import gestalt.impl.jogl.context.JoglDisplay;
import gestalt.render.AnimatorRenderer;
import gestalt.render.bin.BilateralBin;
import gestalt.shape.Color;
import gestalt.shape.Plane;
import gestalt.shape.material.TexturePlugin;
import gestalt.texture.Bitmaps;
import com.sun.opengl.util.GLUT;

public class LeicasDream extends AnimatorRenderer implements Constants {

    private static String _ownOid = "0";

    private static String _leicaVersion = "";

    private static String displaycapabilitesFile = "";

    private String _searchId = "0000";

    private Plane _floorBplane;

    private Plane _floorAplane;

    private Plane _floorCplane;

    private TexturePlugin _floorATexture;

    private TexturePlugin _floorBTexture;

    private TexturePlugin _floorCTexture;

    private Plane _logoPlane;

    private TexturePlugin _logoTexture;

    private Plane _keyPlane;

    private TexturePlugin _keyTexture;

    private JoglGLUTBitmapFont _myActiveTags;

    private JoglGLUTBitmapFont _instructions;

    private JoglGLUTBitmapFont _searchLabel;

    private SputnikReceiver _sputnikReceiver;

    private SputnikTagManager _tagManager;

    private ObserverManager _observerManager;

    public void setup() {
        framerate(UNDEFINED);
        _tagManager = new SputnikTagManager(this, _ownOid);
        _observerManager = new ObserverManager(this);
        _sputnikReceiver = new SputnikReceiver(_tagManager);
        _sputnikReceiver.setDaemon(true);
        _sputnikReceiver.start();
        _floorBplane = drawablefactory().plane();
        _floorBTexture = drawablefactory().texture();
        _floorBTexture.load(Bitmaps.getBitmap(data.Resource.getPath("levelB.png")));
        _floorBTexture.setFilterType(TEXTURE_FILTERTYPE_MIPMAP);
        _floorBplane.material().addPlugin(_floorBTexture);
        _floorBplane.material().depthtest = true;
        _floorBplane.material().wireframe = false;
        _floorBplane.setPlaneSizeToTextureSize();
        _floorBplane.rotation().x = -PI_HALF;
        _floorBplane.position().y = 0f;
        bin(BIN_3D).add(_floorBplane);
        _floorAplane = drawablefactory().plane();
        _floorATexture = drawablefactory().texture();
        _floorATexture.load(Bitmaps.getBitmap(data.Resource.getPath("levelA.png")));
        _floorATexture.setFilterType(TEXTURE_FILTERTYPE_MIPMAP);
        _floorAplane.material().addPlugin(_floorATexture);
        _floorAplane.material().depthtest = true;
        _floorAplane.material().wireframe = false;
        _floorAplane.setPlaneSizeToTextureSize();
        _floorAplane.rotation().x = -PI_HALF;
        _floorAplane.position().y = -FLOOR_HEIGHT;
        bin(BIN_3D).add(_floorAplane);
        _floorCplane = drawablefactory().plane();
        _floorCTexture = drawablefactory().texture();
        _floorCTexture.load(Bitmaps.getBitmap(data.Resource.getPath("levelC.png")));
        _floorCTexture.setFilterType(TEXTURE_FILTERTYPE_MIPMAP);
        _floorCplane.material().addPlugin(_floorCTexture);
        _floorCplane.material().depthtest = true;
        _floorCplane.material().wireframe = false;
        _floorCplane.setPlaneSizeToTextureSize();
        _floorCplane.material().color.a = 0.5f;
        _floorCplane.rotation().x = -PI_HALF;
        _floorCplane.position().y = FLOOR_HEIGHT;
        bin(BIN_3D).add(_floorCplane);
        _floorCplane.material().depthmask = false;
        _floorBplane.material().depthmask = false;
        _logoPlane = drawablefactory().plane();
        _logoTexture = drawablefactory().texture();
        _logoTexture.load(Bitmaps.getBitmap(data.Resource.getPath("logo.png")));
        _logoPlane.material().addPlugin(_logoTexture);
        _logoPlane.origin(SHAPE_ORIGIN_TOP_LEFT);
        _logoPlane.setPlaneSizeToTextureSize();
        _logoPlane.position().x = displaycapabilities().width / -2 + 20;
        _logoPlane.position().y = displaycapabilities().height / 2 - 20;
        bin(BIN_2D_FOREGROUND).add(_logoPlane);
        _keyPlane = drawablefactory().plane();
        _keyTexture = drawablefactory().texture();
        _keyTexture.load(Bitmaps.getBitmap(data.Resource.getPath("key.png")));
        _keyPlane.material().addPlugin(_keyTexture);
        _keyPlane.origin(SHAPE_ORIGIN_BOTTOM_LEFT);
        _keyPlane.setPlaneSizeToTextureSize();
        _keyPlane.position().x = displaycapabilities().width / -2 + 20;
        _keyPlane.position().y = displaycapabilities().height / -2 + 20;
        bin(BIN_2D_FOREGROUND).add(_keyPlane);
        _myActiveTags = new JoglGLUTBitmapFont();
        _myActiveTags.color.set(1f, 1f);
        _myActiveTags.align = JoglGLUTBitmapFont.RIGHT;
        _myActiveTags.position.set(displaycapabilities().width / 2 - 40, displaycapabilities().height / 2 - 40);
        _myActiveTags.font = GLUT.BITMAP_HELVETICA_12;
        bin(BIN_2D_FOREGROUND).add(_myActiveTags);
        _instructions = new JoglGLUTBitmapFont();
        _instructions.color.set(1f, 1f);
        _instructions.align = JoglGLUTBitmapFont.RIGHT;
        _instructions.position.set(displaycapabilities().width / 2 - 40, -displaycapabilities().height / 2 + 40);
        _instructions.font = GLUT.BITMAP_HELVETICA_12;
        _instructions.text = "Enter 4 digits to search for tag. [Space] resets.";
        bin(BIN_2D_FOREGROUND).add(_instructions);
        _searchLabel = new JoglGLUTBitmapFont();
        _searchLabel.color.set(1f, 1f);
        _searchLabel.align = JoglGLUTBitmapFont.CENTERED;
        _searchLabel.position.set(0, 0);
        _searchLabel.font = GLUT.BITMAP_HELVETICA_18;
        bin(BIN_2D_FOREGROUND).add(_searchLabel);
        camera().setMode(CAMERA_MODE_LOOK_AT);
        camera().setLookAtRef(new Vector3f(0, 0, 0));
        camera().up(200f);
        camera().forward(-600f);
        BilateralBin binbin = (BilateralBin) bin(BIN_3D);
        binbin.setSortStyle(SHAPEBIN_SORT_BY_DISTANCE_TO_CAMERA);
    }

    public void finish() {
    }

    public static void main(String[] arg) {
        JoglDisplay.SWITCH_RESOLUTION = false;
        if (arg.length > 0) {
            _ownOid = arg[0];
            System.out.println(_ownOid);
        } else {
            _ownOid = "0";
        }
        if (arg.length > 1) {
            displaycapabilitesFile = arg[1];
        }
        new LeicasDream().init();
    }

    @Override
    public void keyPressed(char theKey, int theKeyCode) {
        switch(theKey) {
            case '0':
                appendSearchString("0");
                break;
            case '1':
                appendSearchString("1");
                break;
            case '2':
                appendSearchString("2");
                break;
            case '3':
                appendSearchString("3");
                break;
            case '4':
                appendSearchString("4");
                break;
            case '5':
                appendSearchString("5");
                break;
            case '6':
                appendSearchString("6");
                break;
            case '7':
                appendSearchString("7");
                break;
            case '8':
                appendSearchString("8");
                break;
            case '9':
                appendSearchString("9");
                break;
            default:
                break;
        }
        if (theKeyCode == KEYCODE_SPACE) {
            resetSearchString();
        }
        super.keyPressed(theKey, theKeyCode);
    }

    private void resetSearchString() {
        _searchId = "0000";
        _tagManager.searchId = _searchId;
    }

    private void appendSearchString(String string) {
        _searchId += string;
        _searchId = _searchId.substring(_searchId.length() - 4, _searchId.length());
        _tagManager.searchId = _searchId;
        System.out.println(_searchId);
    }

    public DisplayCapabilities createDisplayCapabilities() {
        DisplayCapabilities myDisplayCapabilities = new DisplayCapabilities();
        if (displaycapabilitesFile == "") {
            myDisplayCapabilities.name = "Leica Sputnik 25C3";
            myDisplayCapabilities.width = 1024;
            myDisplayCapabilities.height = 768;
            myDisplayCapabilities.undecorated = true;
            myDisplayCapabilities.fullscreen = false;
            myDisplayCapabilities.centered = true;
            myDisplayCapabilities.backgroundcolor.set(0f);
            myDisplayCapabilities.antialiasinglevel = 4;
            myDisplayCapabilities.cursor = true;
            myDisplayCapabilities.renderer = ENGINE_JOGL;
            myDisplayCapabilities.headless = false;
            myDisplayCapabilities.synctovblank = true;
            DisplayCapabilities.listDisplayDevices();
        } else {
            System.out.println("** Using external displaycapabilities file: " + displaycapabilitesFile);
            try {
                myDisplayCapabilities = DisplayCapabilities.getFromFile(new FileInputStream(displaycapabilitesFile));
            } catch (Exception e) {
                System.err.print("Could not find displaycapabilites XML");
            }
        }
        return myDisplayCapabilities;
    }

    public void loop(float theDeltaTime) {
        if (_searchId.equals("0000")) {
            _searchLabel.active = false;
        } else {
            _searchLabel.active = true;
            _searchLabel.text = _searchId;
        }
        _tagManager.animate();
        _myActiveTags.text = String.valueOf(_tagManager.getSeenTagCount()) + " Tags Seen, " + String.valueOf(_tagManager.getActiveTagCount()) + " Currently Active" + _leicaVersion + " - allesblinkt.com / 08";
        double time = System.currentTimeMillis() * 0.001d;
        float cameraX = (float) (FLOOR_TEXTURE_SIZE * -Math.sin(time / (60.0 + (1.0 / 60.0)) * TWO_PI));
        float cameraY = (float) (100 + 250 * (Math.sin(time / (50.0 + (1.0 / 30.0)) * TWO_PI) + 1));
        float cameraZ = (float) (FLOOR_TEXTURE_SIZE * -Math.sin(time / (60.0 - (1.0 / 60.0)) * TWO_PI));
        camera().position().set(cameraX, cameraY, cameraZ);
        Vector3f middle = new Vector3f(0, 0, 0);
        float middleDistance = middle.distance(camera().position());
        fog().enable = true;
        fog().color().set(new Color(0, 0, 0, 1));
        fog().start(middleDistance - 400);
        fog().end(middleDistance + 400);
        fog().filter = FOG_FILTER_LINEAR;
        camera().farclipping = middleDistance + FLOOR_TEXTURE_SIZE;
        camera().nearclipping = middleDistance - FLOOR_TEXTURE_SIZE;
    }
}
