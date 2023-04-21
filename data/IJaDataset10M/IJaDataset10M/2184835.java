package timeregistrering_v01;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class TimeRegistrering_v01App extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new TimeRegistrering_v01View(this));
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
     * @return the instance of TimeRegistrering_v01App
     */
    public static TimeRegistrering_v01App getApplication() {
        return Application.getInstance(TimeRegistrering_v01App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(TimeRegistrering_v01App.class, args);
        TestClass testClass = new TestClass();
    }
}
