package com.extentech.toolkit;

public interface ProgressNotifier {

    void register(ProgressListener j);

    void fireProgressChanged();

    int getProgress();

    void setProgress(int progress);

    String getProgressText();

    void setProgressText(String ptext);

    boolean iscompleted();
}
