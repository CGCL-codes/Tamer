package net.sf.robobo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.digester.Digester;
import net.sf.robobo.data.Plugin;

/**
 * @author partenon
 *
 */
public class Plugins {

    private static List plugins;

    private static void parsePlugins() throws Exception {
        Digester digester = new Digester();
        digester.push(Plugins.class.newInstance());
        digester.addCallMethod("robobo/executores/plugin", "addPlugin", 2);
        digester.addCallParam("robobo/executores/plugin/chave", 0);
        digester.addCallParam("robobo/executores/plugin/classe", 1);
        File arquivo = new File("plugins.xml");
        digester.parse("plugins.xml");
    }

    public static List getPlugins() throws Exception {
        if (plugins == null) {
            parsePlugins();
        }
        return plugins;
    }

    public static void addPlugin(String chave, String classe) {
        if (plugins == null) {
            plugins = new ArrayList();
        }
        Plugin plugin = new Plugin();
        plugin.setChave(chave);
        plugin.setClasse(classe);
        plugins.add(plugin);
    }
}
