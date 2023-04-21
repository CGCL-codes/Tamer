package org.nakedobjects;

import org.nakedobjects.object.security.Session;

public interface SessionLookup {

    Session getCurrentSession();
}
