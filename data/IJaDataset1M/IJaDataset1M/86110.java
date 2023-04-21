package test.com.gestioni.adoc.aps.system.services.repository;

import com.gestioni.adoc.aps.system.services.profilo.Profilo;

public class JcrCredentials {

    public static String getUserIdentifier(Profilo profilo) {
        String identifier = null;
        if (profilo != null) {
            identifier = String.valueOf(profilo.getId());
        } else {
            identifier = "test";
        }
        return identifier;
    }
}
