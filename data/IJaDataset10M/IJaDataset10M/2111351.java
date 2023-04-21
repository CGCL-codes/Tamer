package net.java.dev.javarecord.i18n;

/**
 * Used for gets the Plural Name of an table.
 * @author <p><a href="mailto:felix.rafael@gmail.com">Rafael Felix da Silva</p>
 * @version 0.1
 * @since 05/11/2008
 */
public interface Pluralize {

    /**
	 * Receive a name and return the name in plural
	 * @param name
	 * @return name in plural mode
	 */
    String getPlural(String name);
}
