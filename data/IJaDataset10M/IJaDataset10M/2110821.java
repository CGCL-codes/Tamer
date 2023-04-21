package org.solol.mmseg.core;

/**
 * @author solo L
 * 
 */
public interface IWord {

    static int UNRECOGNIZED = 0;

    static int BASICLATIN_WORD = 1;

    static int CJK_WORD = 2;

    String getValue();

    int getLength();

    int getFrequency();

    int getType();

    void setType(int type);
}
