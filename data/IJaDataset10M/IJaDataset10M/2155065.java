package vodoo.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import vodoo.catalog.Catalog;

public class XMLConfiguration {

    protected static List<ConfigurationElem> myCatalogs;

    protected static List<ConfigurationElem> myServers;

    protected static String pluginPath;

    public static List<ConfigurationElem> GetCatalogs() {
        if (myCatalogs != null) {
            return myCatalogs;
        } else {
            return new ArrayList<ConfigurationElem>();
        }
    }

    public static List<ConfigurationElem> GetServers() {
        if (myServers != null) {
            return myServers;
        } else {
            return new ArrayList<ConfigurationElem>();
        }
    }

    public static String GetPluginPath() {
        return pluginPath;
    }

    static final String fileName = "config.xml";

    static final String stringRacine = "Configuration";

    static final String stringCatalogs = "Catalogs";

    static final String stringCatalog = "Catalog";

    static final String stringServersMCAST = "ServersMCAST";

    static final String stringServerMCAST = "ServerMCAST";

    static final String stringIP = "IP";

    static final String stringPORT = "PORT";

    static final String stringPluginPath = "PluginPath";

    public static void SaveConfiguration(List<Catalog> myCatalogList, List<ConfigurationElem> serveurMCASTList, String myPluginPath) {
        Element racine = new Element(stringRacine);
        Document document = new Document(racine);
        Element ip;
        Element port;
        Element Catalogs = new Element(stringCatalogs);
        for (Catalog myCatalog : myCatalogList) {
            Element Catalog = new Element(stringCatalog);
            ip = new Element(stringIP);
            ip.setText(myCatalog.GetIP());
            Catalog.addContent(ip);
            port = new Element(stringPORT);
            port.setText(Integer.toString(myCatalog.GetPort()));
            Catalog.addContent(port);
            Catalogs.addContent(Catalog);
        }
        Element Servers = new Element(stringServersMCAST);
        for (ConfigurationElem myServeur : serveurMCASTList) {
            Element Server = new Element(stringServerMCAST);
            ip = new Element(stringIP);
            ip.setText(myServeur.Ip);
            Server.addContent(ip);
            port = new Element(stringPORT);
            port.setText(Integer.toString(myServeur.Port));
            Server.addContent(port);
            Servers.addContent(Server);
        }
        racine.addContent(Catalogs);
        racine.addContent(Servers);
        Element pluginPathElem = new Element(stringPluginPath);
        pluginPathElem.setText(myPluginPath);
        racine.addContent(pluginPathElem);
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        try {
            sortie.output(document, new FileOutputStream(fileName));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static void LoadConfiguration() {
        SAXBuilder sxb = new SAXBuilder();
        Document document = null;
        try {
            document = sxb.build(new File(fileName));
        } catch (JDOMException e) {
            return;
        } catch (IOException e) {
            return;
        }
        Element racine = document.getRootElement();
        Element Catalogs = racine.getChild(stringCatalogs);
        Element pluginPathElem = racine.getChild(stringPluginPath);
        List<Element> catalogs = Catalogs.getChildren(stringCatalog);
        myCatalogs = new ArrayList<ConfigurationElem>();
        for (Element catalog : catalogs) {
            ConfigurationElem tmp = new ConfigurationElem();
            tmp.Ip = catalog.getChildText(stringIP);
            tmp.Port = Integer.parseInt(catalog.getChildText(stringPORT));
            myCatalogs.add(tmp);
        }
        Element Servers = racine.getChild(stringServersMCAST);
        List<Element> servers = Servers.getChildren(stringServerMCAST);
        myServers = new ArrayList<ConfigurationElem>();
        for (Element server : servers) {
            ConfigurationElem tmp = new ConfigurationElem();
            tmp.Ip = server.getChildText(stringIP);
            tmp.Port = Integer.parseInt(server.getChildText(stringPORT));
            myServers.add(tmp);
        }
        pluginPath = pluginPathElem.getText();
    }

    public static class ConfigurationElem {

        public String Ip;

        public int Port;
    }
}
