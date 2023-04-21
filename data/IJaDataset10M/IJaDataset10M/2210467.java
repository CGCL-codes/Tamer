package org.galaxy.gpf.application;

/**
 * {@code Application} is the interface of Galaxy Picture Factory (as GPF).<br />
 * It supplies a simple life cycle of GPF: initialize, startup, shutdown, etc. Its
 * launch method used for startup application. When there is main method entry, this
 * method should be invoked to start up.<br />
 * It should be a singleton instance. {@code Application} uses {@code ApplicationModel}
 * singleton to find resources, actions and others. 
 * 
 * @see ApplicationModel
 * @author Cheng Liang
 * @version 1.0.0
 */
public interface Application {

    /**
	 * Sets the application model to this application.
	 * 
	 * @param model new application model
	 */
    public void setApplicationModel(ApplicationModel model);

    /**
	 * Gets the application model.
	 * 
	 * @return application model related to this application
	 */
    public ApplicationModel getApplicationModel();

    /**
	 * Startup application. This method should be invoked in main method and only once.
	 * 
	 * @param args command line startup arguments
	 */
    public void launch(String[] args);
}
