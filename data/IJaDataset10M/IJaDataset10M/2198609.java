package util;

/**
 * Description of pluginassociation
 *
 * @author acozzolino
 */
public class Pluginassociation {

    public int id_plugin;

    public int id_resultset;

    public int id_group;

    public Pluginassociation(int id_plugin, int id_resultset, int id_group) {
        this.id_plugin = id_plugin;
        this.id_resultset = id_resultset;
        this.id_group = id_group;
    }

    public int get_idresultset() {
        return this.id_resultset;
    }

    public int get_idplugin() {
        return this.id_plugin;
    }

    public int get_idgroup() {
        return this.id_group;
    }
}
