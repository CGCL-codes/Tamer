package org.extwind.osgi.console.service;

/**
 * @author Donf Yang
 *
 */
public interface LaunchServiceProvider {

    public static final String DEFAULT = "DEFAULT";

    public LaunchService getService();
}
