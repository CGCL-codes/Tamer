package org.mooym;

/**
 * Mocking the running application.
 */
public class MooymMock extends Mooym {

    private MainFrame mainFrame;

    private MooymData mooymData;

    private Configuration configuration;

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public MooymData getMooymData() {
        return mooymData;
    }

    public void setMooymData(MooymData mooymData) {
        this.mooymData = mooymData;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
