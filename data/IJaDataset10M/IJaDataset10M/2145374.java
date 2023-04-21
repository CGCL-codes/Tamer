package org.spockframework.util;

import org.codehaus.groovy.runtime.InvokerHelper;
import groovy.lang.GroovyObject;

public class GroovyReleaseInfo {

    public static VersionNumber getVersion() {
        return VersionNumber.parse(InvokerHelper.getVersion());
    }

    public static String getArtifactPath() {
        return GroovyObject.class.getProtectionDomain().getCodeSource().getLocation().toString();
    }
}
