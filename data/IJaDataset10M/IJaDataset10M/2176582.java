package com.sysdelphia.bindings.w3.xmldsig;

import java.util.ArrayList;

public class SeedType {

    public void addChoice(Object choice) {
        choiceList.add(choice);
    }

    public Object getChoice(int index) {
        return (Object) choiceList.get(index);
    }

    public int sizeChoiceList() {
        return choiceList.size();
    }

    protected ArrayList choiceList = new ArrayList();
}
