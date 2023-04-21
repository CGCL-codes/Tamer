package net.sourceforge.jetdog.input;

public interface InputReleasedAction extends InputMapListener {

    public boolean inputReleasedAction(int player, int actionNum);
}
