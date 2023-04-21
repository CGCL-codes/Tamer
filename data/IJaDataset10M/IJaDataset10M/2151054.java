package org.tolven.plugin.repository.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.tolven.logging.TolvenLogger;
import org.tolven.plugin.repository.RepositoryMetadata;
import org.tolven.plugin.repository.RepositoryUpgrade;
import org.tolven.plugin.repository.bean.Plugins;
import junit.framework.TestCase;

public class RepositoryTests extends TestCase {

    public void testMetadataMerge() throws Exception {
        TolvenLogger.defaultInitialize();
        List<URL> urls = new ArrayList<URL>();
        URL url1 = getClass().getResource("plugins1.xml");
        urls.add(url1);
        URL url2 = getClass().getResource("plugins2.xml");
        urls.add(url2);
        URL url3 = getClass().getResource("plugins3.xml");
        urls.add(url3);
        RepositoryUpgrade repositoryUpgrade = new RepositoryUpgrade();
        Plugins plugins = repositoryUpgrade.getMergedPlugins(urls);
        String generatedXML = RepositoryMetadata.getPluginsXML(plugins);
        System.out.println(generatedXML);
        String storedXML = IOUtils.toString(getClass().getResource("plugins-result.xml").openStream());
        assertTrue(generatedXML.equals(storedXML));
    }
}
