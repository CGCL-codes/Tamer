package org.opu.db_vdumper.util.monitors;

/**
 *
 * @author yura
 */
public interface DoubleProgresMonitor extends SimpleProgresMonitor {

    public void setLowBarIndeterminate(boolean newValue);

    public void setLowBarNoteAndProgress(String note, int prog);

    public void setLowBarNote(String note);

    public void setLowBarProgress(int prog);

    public void showLowBar(boolean value);
}
