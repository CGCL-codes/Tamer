package org.hip.kernel.bom.directory.test;

import org.hip.kernel.bom.directory.LDAPObject;

@SuppressWarnings("serial")
public class TestLDAPObject extends LDAPObject {

    public static final String HOME_CLASS_NAME = "org.hip.kernel.bom.directory.test.TestLDAPObjectHome";

    public String getHomeClassName() {
        return HOME_CLASS_NAME;
    }
}
