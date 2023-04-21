package edu.umn.cs5115.scheduler;

import edu.umn.cs5115.scheduler.framework.Document;
import edu.umn.cs5115.scheduler.framework.DocumentController;

/**
 * This is the main class for the application.  It registers the 
 * SchedulerDocument with the DocumentController and the application's main 
 * menu, then runs the main event loop.
 * @author grant
 */
public class SchedulerMain {

    /**
     * Main method; program entry point.  Kicks off the application.
     * @param args Command line arguments - presently ignored.
     */
    public static void main(String[] args) {
        DocumentController controller = DocumentController.getSharedDocumentController();
        controller.setDocumentFactory(new DocumentController.DocumentFactory() {

            public Document createDocument() {
                return new SchedulerDocument();
            }
        });
        controller.setMainMenu(new SchedulerMainMenu());
        controller.run();
    }
}
