package fr.amille.animebrowser.model;

import java.util.HashMap;

/**
 * Serie --> chemin vers config serie ; Site --> chemin vers config
 * fr.amille.animebrowser.model.site ; Minitaure --> chemin vers images
 * miniatures
 * 
 * @author amille
 * 
 */
public final class Configuration {

    private static Configuration instance;

    public static Configuration getInstance() {
        if (Configuration.instance == null) {
            return new Configuration();
        } else {
            return Configuration.instance;
        }
    }

    private HashMap<String, String> variables = new HashMap<String, String>();

    private Configuration() {
        Configuration.instance = this;
    }

    public HashMap<String, String> getVariables() {
        return this.variables;
    }

    public void setVariables(final HashMap<String, String> variables) {
        this.variables = variables;
    }
}
