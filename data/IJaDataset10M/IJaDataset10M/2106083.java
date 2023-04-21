package de.mpiwg.vspace.images.ui.controller;

import java.util.Observable;
import de.mpiwg.vspace.images.core.ImageImpl;

public class DbObservable extends Observable {

    public static final DbObservable INSTANCE = new DbObservable();

    public void dbChanged(ImageImpl image) {
        setChanged();
        notifyObservers(image);
    }
}
