package testingapplication.progressbar;

import com.softaspects.jsf.component.progressBar.ProgressBar;
import java.util.Random;

public class DataBean {

    private int startValue = 1;

    private int finishValue = 100;

    private int progress1 = 0;

    private int progress2 = 0;

    private int progress3 = 0;

    private int progress4 = 0;

    private int progress5 = 0;

    private int progress6 = 0;

    private int progress7 = 0;

    private int progress8 = 0;

    public int getProgress(Integer progress) {
        Random ram = new Random();
        progress += ram.nextInt(5);
        return (progress >= getFinishValue()) ? getFinishValue() : progress;
    }

    public int getProgressStatus(Integer progress) {
        if (progress >= getFinishValue()) {
            return ProgressBar.COMPLITE;
        } else {
            return ProgressBar.PROCESS;
        }
    }

    public int getProgress1() {
        progress1 = getProgress(progress1);
        return progress1;
    }

    public int getProgress2() {
        progress2 = getProgress(progress2);
        return progress2;
    }

    public int getProgress3() {
        progress3 = getProgress(progress3);
        return progress3;
    }

    public int getProgress4() {
        progress4 = getProgress(progress4);
        return progress4;
    }

    public int getProgress5() {
        progress5 = getProgress(progress5);
        return progress5;
    }

    public int getProgress6() {
        progress6 = getProgress(progress6);
        return progress6;
    }

    public int getProgress7() {
        progress7 = getProgress(progress7);
        return progress7;
    }

    public int getProgress8() {
        progress8 = getProgress(progress8);
        return progress8;
    }

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public int getFinishValue() {
        return finishValue;
    }

    public void setFinishValue(int finishValue) {
        this.finishValue = finishValue;
    }

    public int getProgress1Status() {
        return getProgressStatus(progress1);
    }

    public int getProgress2Status() {
        return getProgressStatus(progress2);
    }

    public int getProgress3Status() {
        return getProgressStatus(progress3);
    }

    public int getProgress4Status() {
        return getProgressStatus(progress4);
    }

    public int getProgress5Status() {
        return getProgressStatus(progress5);
    }

    public int getProgress6Status() {
        return getProgressStatus(progress6);
    }

    public int getProgress7Status() {
        return getProgressStatus(progress7);
    }

    public int getProgress8Status() {
        return getProgressStatus(progress8);
    }
}
