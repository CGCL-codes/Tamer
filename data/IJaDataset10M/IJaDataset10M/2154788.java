package it.infodea.tapestrydea.filter.demodea;

import it.infodea.tapestrydea.support.enumerations.SecurityGroup;

public class SecurityGroupForTesting {

    private static SecurityGroup group;

    public static SecurityGroup getGroup() {
        return group;
    }

    public static void setGroup(SecurityGroup group) {
        SecurityGroupForTesting.group = group;
    }
}
