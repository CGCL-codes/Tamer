package fr.yahoo.smanciot.controller;

/**
 * @author smanciot
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ConfigContainer extends Clone {

    String FILE_SEPARATOR = System.getProperty("file.separator");

    void setConfig(Config config);

    void loadParams(Config config);

    void unload();

    void close();
}
