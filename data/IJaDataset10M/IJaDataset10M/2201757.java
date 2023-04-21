package com.divrep;

import java.io.Serializable;

public abstract class DivRepEventListener implements Serializable {

    public abstract void handleEvent(DivRepEvent e);
}
