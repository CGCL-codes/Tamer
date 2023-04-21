package ps.client.gui.timerwindow;

import java.util.TimerTask;
import ps.client.gui.MainFrame;
import ps.client.sound.SoundPlayer;
import ps.server.trigger.TriggerEntry;

public class TriggerTask extends TimerTask {

    MainFrame mainFrame;

    TriggerEntry trigger;

    TimerPanel[] timerPanels;

    int timeLeft;

    public TriggerTask(MainFrame mainFrame, TriggerEntry trigger, TimerPanel[] timerPanels) {
        this.mainFrame = mainFrame;
        this.trigger = trigger;
        this.timerPanels = timerPanels;
        this.timeLeft = trigger.getTimerPeriod();
        for (int i = 0; i < timerPanels.length; i++) {
            timerPanels[i].getTitleLabel().setText(timerPanels[i].getTriggerEntry().getTitle());
            timerPanels[i].getTimerBar().setInitialTime(timerPanels[i].getTriggerEntry().getTimerPeriod());
            timerPanels[i].getTimerBar().setTimeLeft(timeLeft);
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < timerPanels.length; i++) {
            timerPanels[i].getTimerBar().setTimeLeft(timeLeft);
            timerPanels[i].getTimeLabel().setText(getFormatedTime(timeLeft));
            timerPanels[i].getTimeLabel().setForeground(timerPanels[i].getTimerBar().getCurrentBarColor());
        }
        if (timeLeft == trigger.getTimerWarning()) {
            if (trigger.getTimerWarningMsg().length() > 0) {
                mainFrame.appendTextMsg(trigger.getSolvedTimerWarningMessage(), trigger.getTimerWarningMsgSize(), trigger.getTimerWarningMsgColor());
            }
            if (!trigger.getTimerWarningSound().equals(TriggerEntry.NO_SOUND)) {
                try {
                    SoundPlayer.play(MainFrame.SOUND_DIR + "/" + trigger.getTimerWarningSound());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (timeLeft < (-1 * trigger.getTimerRemove())) {
            cancel();
            for (int i = 0; i < timerPanels.length; i++) {
                timerPanels[i].removeTimer();
            }
        }
        timeLeft--;
    }

    private String getFormatedTime(int time) {
        String ret = time < 0 ? "-" : "";
        ret += Math.abs(time) / 60;
        ret += ":";
        int sec = Math.abs(time) % 60;
        ret += sec < 10 ? "0" + sec : sec;
        return ret;
    }

    public void cancelIfRunning() {
        try {
            cancel();
        } catch (Exception ex) {
        }
    }
}
