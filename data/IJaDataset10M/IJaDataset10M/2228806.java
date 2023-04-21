package com.consciouscode.alex.reader;

/**
 * 
 */
public class AIdentifier implements AItem {

    private final String myId;

    public AIdentifier(String id) {
        myId = id;
    }

    @Override
    public String toString() {
        return myId;
    }
}
