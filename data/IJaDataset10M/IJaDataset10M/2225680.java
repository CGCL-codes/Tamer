package jbomberman.client.gui;

import jbomberman.client.LogicInterface;
import jbomberman.client.config.Configuration;
import jbomberman.client.lang.Language;

/**
 * GUIInterface.java
 *
 *
 *
 * @author <a href="mailto:swulf@sbox.tugraz.at">Wolfgang Schriebl</a>
 * @version 1.0
 */
public interface GUIInterface {

    public boolean initialize(LogicInterface logic, Language language, Configuration config);

    public void show();

    public void quit();
}
