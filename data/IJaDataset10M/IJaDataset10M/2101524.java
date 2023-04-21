package net.jfipa.xml.fipa;

import java.util.ArrayList;

public interface ToI {

    public AgentIdentifier getTo();

    public AgentIdentifier getTo(int number);

    public ArrayList getToList();

    public int getSizeOfToList();
}
