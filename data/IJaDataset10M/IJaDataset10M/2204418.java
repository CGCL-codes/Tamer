package clubmixer.server.control;

import clubmixer.commons.plugins.queue.IQueueControl;
import clubmixer.server.IClubmixerServerProperties;
import clubmixer.server.plugins.PluginController;
import clubmixer.server.prefs.ClubmixerServerPreferences;

/**
 *
 * @author Alexander Schindler
 */
public class QueueControlController {

    private static QueueControlController instance;

    private IQueueControl activeControl;

    private final ClubmixerServerPreferences prefs;

    /**
     * Constructs ...
     *
     */
    public QueueControlController() {
        instance = this;
        prefs = ClubmixerServerPreferences.getInstance();
        String activeQueueID = prefs.getActiveQueueControllerID();
        if (activeQueueID == null) {
            activeQueueID = IClubmixerServerProperties.STANDARD_QUEUECONTROL_NAME;
            prefs.setActiveQueueControllerID(activeQueueID);
        }
        loadQueueControl(activeQueueID);
    }

    /**
     * Method description
     *
     *
     * @param queueID
     */
    public void loadQueueControl(String queueID) {
        activeControl = PluginController.getInstance().loadQueueControl(queueID);
        EventhandlerController.getQueueChangeHandler().fireQueueChanged();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public static QueueControlController getInstance() {
        return instance;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public IQueueControl getActiveControl() {
        return activeControl;
    }
}
