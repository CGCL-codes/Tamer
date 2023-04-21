package com.googlecode.tcime.unofficial;

/**
 * Extends Editor to compose by cangjie rules. 
 */
public class CangjieEditor extends Editor {

    public boolean simplified;

    public void setSimplified(boolean simplified) {
        this.simplified = simplified;
    }

    /**
   * Composes the key-code into the composing-text by cangjie composing rules.
   */
    @Override
    public boolean doCompose(int keyCode) {
        char c = (char) keyCode;
        if (!CangjieTable.isLetter(c)) {
            return false;
        }
        int maxLength = simplified ? CangjieTable.MAX_SIMPLIFIED_CODE_LENGTH : CangjieTable.MAX_CODE_LENGTH;
        if (composingText.length() >= maxLength) {
            return true;
        }
        composingText.append(c);
        return true;
    }
}
