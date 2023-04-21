package org.nakedobjects.nos.client.dnd;

import org.nakedobjects.noa.adapter.NakedValue;
import org.nakedobjects.noa.reflect.Consent;

public interface ValueContent extends Content {

    void clear();

    boolean canWrap();

    void entryComplete();

    int getMaximumLength();

    int getNoLines();

    NakedValue getObject();

    int getTypicalLineLength();

    Consent isEditable();

    boolean isEmpty();
}
