package hu.csq.dyneta.configuration.plugininfo;

import java.util.LinkedList;

/**
 *
 * @author Tamás Cséri
 */
public class PluginDescription {

    public String name;

    public String version;

    public String author;

    public String shortDescription;

    public String longHTMLDescription;

    public LinkedList<PluginParameterDescription> parameters;
}
