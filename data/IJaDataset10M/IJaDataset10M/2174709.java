package org.vtlabs.ec2backup.config;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
class ConfigException extends Exception {

    public ConfigException(String string, Exception e) {
        super(string, e);
    }
}
