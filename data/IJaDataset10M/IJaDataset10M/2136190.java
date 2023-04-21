package com.innovative.main.event;

import java.util.EventObject;
import com.innovative.main.MainComponent;

/**
 *
 * @author Dylon Edwards
 */
public class MainEvent extends EventObject {

    private static final long serialVersionUID = -7842847554601294116L;

    private final MainComponent<?> prev;

    public MainEvent(final Object source, final MainComponent<?> prev) {
        super(source);
        this.prev = prev;
    }

    public MainComponent<?> getPrev() {
        return prev;
    }
}
