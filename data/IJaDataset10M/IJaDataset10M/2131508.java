package net.sf.magicmap.client.plugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.sf.magicmap.artifact.Artifact;
import net.sf.magicmap.artifact.ArtifactTools;
import net.sf.magicmap.artifact.IArtifact;
import net.sf.magicmap.client.plugin.util.PluginDescriptorReader;
import net.sf.magicmap.client.utils.HtmlSaxParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 *
 *
 */
public class PluginRepository {

    private final URL baseUrl;

    private final ArtifactTools tools = new ArtifactTools();

    public PluginRepository(URL url) {
        this.baseUrl = url;
    }

    /**
	 *
	 * @param manager
	 */
    public Collection<net.sf.magicmap.client.plugin.IPluginDescriptor> loadRepository(net.sf.magicmap.client.plugin.PluginManager manager) {
        LinkedList<net.sf.magicmap.client.plugin.IPluginDescriptor> pluginDescriptors = new LinkedList<net.sf.magicmap.client.plugin.IPluginDescriptor>();
        return pluginDescriptors;
    }

    public Collection<String> getVersions(IArtifact artifact) {
        return new ArrayList<String>();
    }

    public URL getPluginUrl(IArtifact plugin) throws MalformedURLException {
        return new URL(baseUrl, tools.getPath(plugin) + "/" + tools.getFileName(plugin));
    }

    /**
	 *
	 * @param path
	 * @return
	 */
    public net.sf.magicmap.client.plugin.IPluginDescriptor addPlugin(URL path) {
        return null;
    }

    public Collection<net.sf.magicmap.client.plugin.IPluginDescriptor> loadIndex() throws IOException {
        ArrayList<net.sf.magicmap.client.plugin.IPluginDescriptor> l = new ArrayList<net.sf.magicmap.client.plugin.IPluginDescriptor>();
        for (String groups : loadIndex(baseUrl, true)) {
            for (String artifacts : loadIndex(new URL(baseUrl + "/" + groups), true)) {
                for (String versions : loadIndex(new URL(baseUrl + "/" + groups + artifacts), true)) {
                    l.add(getPluginDetails(new Artifact(groups.substring(0, groups.length() - 1), artifacts.substring(0, artifacts.length() - 1), versions.substring(0, versions.length() - 1))));
                }
            }
        }
        return l;
    }

    public net.sf.magicmap.client.plugin.IPluginDescriptor getPluginDetails(IArtifact artifact) throws IOException {
        URL descriptorSource = new URL(baseUrl + tools.getPath(artifact) + "/plugin.xml");
        PluginDescriptorReader reader = new PluginDescriptorReader(descriptorSource.openStream());
        return reader.getDescriptor();
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    private Collection<String> loadIndex(URL url, boolean dir) throws IOException {
        System.out.println("Loading Index of " + url.toString());
        SAXReader reader = new SAXReader(new HtmlSaxParser());
        Document document = null;
        try {
            document = reader.read(url.openStream());
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
        List<Node> linkList = dir ? document.selectNodes("//a[((not (starts-with(@href, '/'))) and (preceding-sibling::img/@alt='[DIR]'))]") : document.selectNodes("//a[not (starts-with(@href, '/'))]");
        List<String> pluginLinkList = new LinkedList<String>();
        for (Node node : linkList) {
            String href = node.valueOf("@href");
            if (href != null && href.length() > 1) pluginLinkList.add(href);
        }
        return pluginLinkList;
    }

    public static void main(String[] args) {
        try {
            PluginRepository repository = new PluginRepository(new URL("http://butler/magicmap-plugins/"));
            Collection<String> strings = repository.loadIndex(repository.baseUrl, true);
            System.out.println(strings.size());
            for (String str : strings) {
                System.out.println("DIR: " + str);
            }
            Collection<net.sf.magicmap.client.plugin.IPluginDescriptor> artiacts = repository.loadIndex();
            for (net.sf.magicmap.client.plugin.IPluginDescriptor a : artiacts) {
                System.out.println("Artifact: " + a.toString() + "\n\tURL:" + repository.getPluginUrl(a));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
