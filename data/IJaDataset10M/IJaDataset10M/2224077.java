package il.ac.biu.cs.grossmm.tests.fooAdapters;

import il.ac.biu.cs.grossmm.api.presence.Entity;

public interface AuthenticationHandler {

    Entity authenticate(String user, String password);
}
