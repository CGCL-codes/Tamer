package ca.ucalgary.cpsc.agilePlanner.persister;

public interface Keystroke extends MouseMove {

    public void setKeystroke(char c);

    public char getKeystroke();

    public boolean isSendKeyOut();

    public void setSendKeyOut(boolean sendKeyOut);
}
