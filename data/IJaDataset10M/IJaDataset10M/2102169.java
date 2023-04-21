package net.sourceforge.sfeclipse.model.command;

public interface ICommand {

    public void push(String cmd);

    public String pop();

    public String shift();

    public void unshift(String cmd);

    public String[] get();

    public String toString();
}
