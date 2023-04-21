package fr.lip6.sma.simulacion.simbar3;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Interface panel to show the game time.
 * 
 * The GUI should look like:
 * 
 * <pre>
 *                          _________________________ 
 *                         |                         |
 *                         | Today: Monday           |
 *                         | Today's progress:       |
 *                         | [----- Morning        ] |
 *                         |_________________________|
 * </pre>
 * 
 * @author Eric Platon <platon@nii.ac.jp>
 * @version $Revision: 1.20 $
 * 
 * @see "Nothing special"
 */
public class SimBarTimer extends JPanel implements ActionListener, PropertyChangeListener {

    /**
     * Resolution of the update period.
     * 
     * @see #mUpdatePeriod
     */
    private static final int RESOLUTION = 300;

    /**
     * An array to access the name of days by integers.
     */
    private static final String[] NAME_OF_DAYS = { "Sunday_Key", "Monday_Key", "Tuesday_Key", "Wednesday_Key", "Thursday_Key", "Friday_Key", "Saturday_Key" };

    /**
     * Bar model (responsible for the timer).
     */
    private final BarModel mModel;

    /**
     * Bar controller.
     */
    private final SimBarController mController;

    /**
     * The timer to measure time.
     */
    private final Timer mTimer;

    /**
     * GUI update frequency.
     * 
     * @see #RESOLUTION
     */
    private double mUpdatePeriod;

    /**
     * Progress bar to show the evolution of the current day.
     */
    private final JProgressBar mDayProgress;

    /**
     * The label that shows the current day of the week.
     */
    private final JLabel mToday;

    /**
     * Constructor. Extracts from the configuration file the number of minutes
     * to represent a day in the simulation.
     * 
     * @param inModel		The game model.
     * @param inController	Game controller.
     */
    public SimBarTimer(SimBarModel inModel, SimBarController inController) {
        mModel = inModel.getBarModel();
        mController = inController;
        mUpdatePeriod = mModel.getDayTimerDelay() / RESOLUTION;
        mTimer = new Timer((int) mUpdatePeriod, this);
        mDayProgress = new JProgressBar(0, (int) (mUpdatePeriod * RESOLUTION));
        mToday = new JLabel("SettingUp_Key");
        setSize(142, 55);
        setMinimumSize(getSize());
        setMaximumSize(getSize());
        setPreferredSize(getSize());
        initComponents();
        mModel.addPropertyChangeListener(this);
    }

    /**
     * Initialize GUI components.
     */
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(Box.createRigidArea(new Dimension(0, 3)));
        final JPanel theDayInfo = new JPanel();
        theDayInfo.setLayout(new BoxLayout(theDayInfo, BoxLayout.LINE_AXIS));
        theDayInfo.setAlignmentX(0);
        final JLabel theDayLabel = new JLabel("Today:_Key");
        theDayLabel.setHorizontalAlignment(SwingConstants.LEFT);
        theDayInfo.add(theDayLabel);
        theDayInfo.add(mToday);
        add(theDayInfo);
        add(Box.createVerticalGlue());
        mDayProgress.setValue(0);
        mDayProgress.setString("");
        mDayProgress.setStringPainted(true);
        add(mDayProgress);
        add(Box.createRigidArea(new Dimension(0, 3)));
    }

    /**
     * M�thode appel�e lorsque le widget est affich�.
     */
    public void addNotify() {
        super.addNotify();
        mDayProgress.setValue(0);
        showCurrentDay();
        startTimer();
    }

    /**
     * M�thode appel�e lorsque le widget est cach�.
     */
    public void removeNotify() {
        super.removeNotify();
        stopTimer();
    }

    /**
     * Start this application timer.
     */
    public void startTimer() {
        mTimer.start();
    }

    /**
     * Stop this application timer.
     */
    public void stopTimer() {
        mTimer.stop();
    }

    /**
     * Action performed after receiving events from the timer.
     * 
     * @param inEvent
     *            The event to be processed.
     */
    public void actionPerformed(ActionEvent inEvent) {
        int current = mDayProgress.getValue();
        if (current < mUpdatePeriod * RESOLUTION) {
            current = (int) Math.min(current + mUpdatePeriod, mUpdatePeriod * RESOLUTION);
            mDayProgress.setValue(current);
            if (current <= mUpdatePeriod * RESOLUTION / 5) {
                mDayProgress.setString("Night_Key");
            } else if (current <= mUpdatePeriod * RESOLUTION / 3) {
                mDayProgress.setString("Morning_Key");
            } else if (current <= mUpdatePeriod * RESOLUTION * 2 / 3) {
                mDayProgress.setString("Mid-day_Key");
            } else if (current <= mUpdatePeriod * RESOLUTION * 4 / 5) {
                mDayProgress.setString("Evening_Key");
            } else {
                mDayProgress.setString("Night_Key");
            }
        } else {
            mDayProgress.setValue((int) (mUpdatePeriod * RESOLUTION));
            mDayProgress.setString("Night_Key");
            stopTimer();
        }
        mController.localizeContainer(this);
    }

    /**
     * Inherited from the PropertyChangeListener class.
     * 
     * @param inEvent
     *            The event to process.
     */
    public void propertyChange(PropertyChangeEvent inEvent) {
        final String theEvent = inEvent.getPropertyName();
        if (SwingUtilities.isEventDispatchThread()) {
            doUpdate(theEvent);
        } else {
            final Runnable scheduledCode = new Runnable() {

                public void run() {
                    doUpdate(theEvent);
                }
            };
            SwingUtilities.invokeLater(scheduledCode);
        }
    }

    /**
     * Internal method to update the day information. Called by the swing
     * dispatcher.
     * 
     * @param inEvent
     *            The name of the event to processed.
     */
    private synchronized void doUpdate(String inEvent) {
        if (inEvent.equals(BarModel.DAY_PROPERTY_NAME)) {
            stopTimer();
            mDayProgress.setValue(0);
            mDayProgress.setString("Night_Key");
            showCurrentDay();
            mController.localizeContainer(this);
            startTimer();
        } else if (inEvent.equals(BarModel.DAYSPEED_PROPERTY_NAME)) {
            mUpdatePeriod = mModel.getDayTimerDelay() / RESOLUTION;
            mTimer.setDelay((int) mUpdatePeriod);
            mDayProgress.setMaximum((int) (mUpdatePeriod * RESOLUTION));
        }
    }

    /**
     * Show the current day.
     */
    private void showCurrentDay() {
        final int theDay = mModel.getCurrentDay();
        assert theDay >= 0 : "Error in day numbering. Negative number.";
        assert theDay < 7 : "Error in day numbering. Number too large.";
        mToday.setText(NAME_OF_DAYS[mModel.getCurrentDay()]);
    }
}
