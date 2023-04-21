package org.elf.businesslayer.kernel;

import org.elf.businesslayer.dictionary.*;
import org.elf.businesslayer.*;

/**
 * Clase que contiene el estado de una sesi�n de la BusinessLayer
 * 
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class ThreadSessionState {

    private BusinessDictionary _businessDictionary;

    private Lock _lock;

    private Sequence _sequence;

    public BusinessDictionary getBusinessDictionary() {
        return _businessDictionary;
    }

    public void setBusinessDictionary(BusinessDictionary businessDictionary) {
        _businessDictionary = businessDictionary;
    }

    public Lock getLock() {
        return _lock;
    }

    public void setLock(Lock lock) {
        _lock = lock;
    }

    public Sequence getSequence() {
        return _sequence;
    }

    public void setSequence(Sequence sequence) {
        _sequence = sequence;
    }
}
