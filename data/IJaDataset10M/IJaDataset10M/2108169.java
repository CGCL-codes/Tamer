package net.sourceforge.myvd.protocol.ldap.mina.ldap.util;

import java.util.List;

public interface ComponentsMonitor {

    public ComponentsMonitor useComponent(String component) throws IllegalArgumentException;

    public boolean allComponentsUsed();

    public boolean finalStateValid();

    public List getRemainingComponents();
}
