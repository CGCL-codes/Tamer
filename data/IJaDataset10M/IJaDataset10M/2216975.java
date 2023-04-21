package org.poset.model;

import java.util.EventListener;

public interface UpdateListener extends EventListener {

    void onUpdate(DomainObject object);
}
