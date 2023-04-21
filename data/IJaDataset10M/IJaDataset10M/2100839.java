package muvis;

import java.io.FileNotFoundException;
import javax.swing.JFrame;
import muvis.view.MuVisAppView;
import muvis.view.controllers.LoadLibraryController;
import muvis.view.loader.LoadLibraryView;

/**
 * This is the entry point of the application.
 * @author Ricardo
 */
public class MuVisApp extends JFrame implements Runnable {

    private void processLibrary() {
        Environment environment = Environment.getEnvironmentInstance();
        if (!environment.configFileExists()) {
            environment.initConfigFile();
            JFrame frame = new JFrame();
            environment.setRootFrame(frame);
            LoadLibraryController controller = new LoadLibraryController();
            new LoadLibraryView(controller);
        } else {
            try {
                environment.loadWorkspace();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Continuing without the loaded configuration");
            }
            MuVisAppView frameTest = new MuVisAppView();
            frameTest.setResizable(true);
            frameTest.setSize(1280, 770);
            environment.setRootFrame(frameTest);
            frameTest.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            frameTest.setVisible(true);
            frameTest.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    public MuVisApp() {
    }

    @Override
    public void run() {
        processLibrary();
    }
}
