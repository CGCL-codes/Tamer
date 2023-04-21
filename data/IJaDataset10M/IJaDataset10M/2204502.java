package de.fmf.ui;

public interface IComm {

    void showInfoMessage(String msg);

    void showErrorMessage(String msg);

    void showErrorMessage(String msg, Exception e);

    void addLogMessage(String msg);
}
