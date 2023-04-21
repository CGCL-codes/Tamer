package fr.esrf.tangoatk.core;

import java.beans.*;

public interface IStringImageListener extends IAttributeStateListener {

    public void stringImageChange(StringImageEvent e);
}
