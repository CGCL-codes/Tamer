package net.sourceforge.myvd.protocol.ldap.mina.ldap.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MandatoryAndOptionalComponentsMonitor implements ComponentsMonitor {

    private ComponentsMonitor mandatoryComponentsMonitor;

    private ComponentsMonitor optionalComponentsMonitor;

    public MandatoryAndOptionalComponentsMonitor(String[] mandatoryComponents, String[] optionalComponents) throws IllegalArgumentException {
        for (int i = 0; i < mandatoryComponents.length; i++) {
            for (int j = 0; j < optionalComponents.length; j++) {
                if (mandatoryComponents[i].equals(optionalComponents[j])) {
                    throw new IllegalArgumentException("Common element, \"" + mandatoryComponents[i] + "\" detected for Mandatory and Optional components.");
                }
            }
        }
        mandatoryComponentsMonitor = new MandatoryComponentsMonitor(mandatoryComponents);
        optionalComponentsMonitor = new OptionalComponentsMonitor(optionalComponents);
    }

    public ComponentsMonitor useComponent(String component) {
        try {
            mandatoryComponentsMonitor.useComponent(component);
        } catch (IllegalArgumentException e1) {
            try {
                optionalComponentsMonitor.useComponent(component);
            } catch (IllegalArgumentException e2) {
                throw new IllegalArgumentException("Unregistered or previously used component: " + component);
            }
        }
        return this;
    }

    public boolean allComponentsUsed() {
        return (mandatoryComponentsMonitor.allComponentsUsed() && optionalComponentsMonitor.allComponentsUsed());
    }

    public boolean finalStateValid() {
        return (mandatoryComponentsMonitor.finalStateValid() && optionalComponentsMonitor.finalStateValid());
    }

    public List getRemainingComponents() {
        List remainingComponents = new LinkedList();
        remainingComponents.addAll(mandatoryComponentsMonitor.getRemainingComponents());
        remainingComponents.addAll(optionalComponentsMonitor.getRemainingComponents());
        return Collections.unmodifiableList(remainingComponents);
    }
}
