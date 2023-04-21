package at.ac.univie.zsu.aguataplan.video.util;

/**
 * @author gerry
 * 
 */
public abstract class ThreadWorking extends Thread {

    protected int retValue;

    protected String dirInput;

    protected String fileInput;

    protected String dirOutput;

    protected String fileOutput;

    public ThreadWorking(String dirInput, String fileInput, String dirOutput, String fileOutput) {
        this.dirInput = dirInput;
        this.fileInput = fileInput;
        this.dirOutput = dirOutput;
        this.fileOutput = fileOutput;
    }

    public int getRetValue() {
        return retValue;
    }
}
