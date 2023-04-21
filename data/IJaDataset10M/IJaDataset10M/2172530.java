package org.maestroframework.maestro.interfaces;

import java.security.Principal;
import java.util.List;

public interface MaestroUser extends Principal {

    public List<String> getGroups();
}
