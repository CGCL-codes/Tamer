package gestalt.demo.advanced;

import gestalt.candidates.NativeMovieTextureProducer;
import gestalt.candidates.materialplugin.JoglMaterialPluginNonPowerOfTwoTextureReference;
import gestalt.context.DisplayCapabilities;
import gestalt.render.AnimatorRenderer;
import gestalt.shape.Cube;
import gestalt.shape.Plane;
import data.Resource;

public class UsingNativeMovies extends AnimatorRenderer {

    private static final String SAMPLE_MOVIE_PATH = "demo/common/tree.mov";

    private float _myPassedTime = 0;

    private NativeMovieTextureProducer _myMovieProducer;

    private JoglMaterialPluginNonPowerOfTwoTextureReference _myMovieTexture;

    private Plane _myPlane;

    private Cube _myCube;

    public void setup() {
        framerate(UNDEFINED);
        fpscounter(true);
        System.out.println("The Library path is: " + System.getProperty("java.library.path"));
        _myCube = drawablefactory().cube();
        _myCube.scale().set(100, 100, 100);
        bin(BIN_3D).add(_myCube);
        _myPlane = drawablefactory().plane();
        bin(BIN_3D).add(_myPlane);
        _myMovieTexture = new JoglMaterialPluginNonPowerOfTwoTextureReference(true);
        _myMovieProducer = new NativeMovieTextureProducer("NativeMovieTextureProducer", _myMovieTexture, Resource.getPath(SAMPLE_MOVIE_PATH), 25.0f);
        bin(BIN_3D_FINISH).add(_myMovieProducer);
        _myPlane.material().addPlugin(_myMovieTexture);
        _myCube.material().addPlugin(_myMovieTexture);
    }

    public void loop(float theDeltaTime) {
        _myPassedTime += theDeltaTime;
        _myCube.rotation().set(Math.sin(_myPassedTime), Math.sin(_myPassedTime * 2), Math.sin(_myPassedTime * -1.4f));
        _myCube.position().set(-(float) event().mouseX, -(float) event().mouseY, 100);
        if (_myMovieProducer.isInitialized()) {
            _myPlane.setPlaneSizeToTextureSize();
        }
    }

    public DisplayCapabilities createDisplayCapabilities() {
        DisplayCapabilities myDisplayCapabilities = new DisplayCapabilities();
        myDisplayCapabilities.name = "Native Movie";
        myDisplayCapabilities.width = 1024;
        myDisplayCapabilities.height = 768;
        myDisplayCapabilities.centered = true;
        myDisplayCapabilities.backgroundcolor.set(0.12f, 0.06f, 0.03f, 1f);
        myDisplayCapabilities.cursor = false;
        myDisplayCapabilities.renderer = ENGINE_JOGL;
        myDisplayCapabilities.synctovblank = false;
        DisplayCapabilities.listDisplayDevices();
        return myDisplayCapabilities;
    }

    public static void main(String[] args) {
        new UsingNativeMovies().init();
    }
}
