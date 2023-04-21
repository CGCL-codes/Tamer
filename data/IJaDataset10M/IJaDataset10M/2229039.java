package net.sourceforge.geeboss.controller.thread;

import net.sourceforge.geeboss.view.MainView;
import net.sourceforge.geeboss.view.i18n.I18nUtil;

/**
 * This Thread opens midi ports.
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public class MidiPortOpenner extends MidiJobThread {

    private String mInputPortName;

    private String mOutputPortName;

    private String mStatusText;

    /**
     * Create a new PortOpenner thread
     * @param geebossGui the main view
     * @param inputPortName the input port name or null
     * @param outputPortName the output port name or null
     */
    public MidiPortOpenner(MainView geebossGui, String inputPortName, String outputPortName) {
        super(geebossGui);
        mStatusText = I18nUtil.getI18nString("status.initializing", mContainer.getSettings().getLocale());
        mInputPortName = inputPortName;
        mOutputPortName = outputPortName;
        setName("Midi port openner thread");
        setDaemon(true);
    }

    /**
     * Load the newsfeed located at the URL
     * 
     * @see java.lang.Thread#run()
     */
    public void run() {
        if (mInputPortName != null) {
            mStatusText = I18nUtil.getI18nString("status.input.port", mContainer.getSettings().getLocale());
            mContainer.getSettings().getMidiSettings().setInputPort(mInputPortName);
        }
        if (mOutputPortName != null) {
            mStatusText = I18nUtil.getI18nString("status.output.port", mContainer.getSettings().getLocale());
            mContainer.getSettings().getMidiSettings().setOutputPort(mOutputPortName);
        }
        mContainer.getEventManager().initEditorsFromDevice(false);
        stopThread();
    }

    public String getStatusText() {
        return mStatusText;
    }
}
