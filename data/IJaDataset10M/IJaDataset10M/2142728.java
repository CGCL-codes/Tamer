package downloadmanager;

import java.io.*;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class App extends SingleFrameApplication implements Serializable {

    /**
     * Filename of saved file.
     */
    private static String filename = "D:/Teste.ser";

    /**
     * Application Models
     */
    private DownloadsModel _downloadsModel;

    private PreferencesModel _preferencesModel;

    private transient View _view;

    public App() {
        initApp(loadApplication());
    }

    private void initApp(App app) {
        initModels(app);
    }

    private void initModels(App app) {
        if (app == null) {
            _downloadsModel = new DownloadsModel();
            _preferencesModel = new PreferencesModel();
        } else {
            _downloadsModel = app._downloadsModel;
            _preferencesModel = app._preferencesModel;
        }
        _downloadsModel.init();
    }

    public PreferencesModel getPreferencesModel() {
        return _preferencesModel;
    }

    public DownloadsModel getDownloadsModel() {
        return _downloadsModel;
    }

    @Override
    public View getMainView() {
        return _view;
    }

    void saveApplication() {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        } catch (IOException e) {
        }
    }

    App loadApplication() {
        App temp = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            temp = (App) in.readObject();
            in.close();
        } catch (Exception e) {
        }
        return temp;
    }

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        _view = new View(this);
        _view.init();
        show(_view);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of App
     */
    public static App getApplication() {
        return Application.getInstance(App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(App.class, args);
    }
}
