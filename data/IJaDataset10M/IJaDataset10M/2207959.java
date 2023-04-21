package de.haw.mussDasSein.rule;

import java.util.List;
import java.util.ArrayList;

/**
 * UNDER CONSTRUCTION
 * 
 * @author Bjoern BEttzueche
 *
 * @param <T> generic Type of the CauseListeners
 */
public class CausalitySupport<T> {

    protected List<CauseListener<T>> listeners = new ArrayList<CauseListener<T>>();

    protected void fireCause(T source) {
        for (CauseListener<T> listener : listeners) {
            listener.performEffect(source);
        }
    }

    protected void addCauseListener(CauseListener<T> listener) {
        listeners.add(listener);
    }

    protected void removeCauseListener(CauseListener<T> listener) {
        listeners.remove(listener);
    }
}
