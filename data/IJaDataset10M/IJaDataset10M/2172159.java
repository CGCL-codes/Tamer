package org.epoline.bsi.bps.shared;

/**
 * Insert the type's description here.
 * Creation date: (19-04-02 13:05:26)
 * @author: Erik Broekhuisen
 */
public interface BPSHeaderPageIF {

    public void addHeader(int level, String aText);

    public void addLine(String[] aText);

    public void addLine(String aText);

    public String getOriginalText();

    public void setOriginalText(String newOriginalText);

    public String toString();
}
