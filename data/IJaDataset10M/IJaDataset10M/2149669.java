package net.sf.buildbox.releasator.legacy;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.codehaus.plexus.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.buildbox.changes.bean.ChangesDocumentBean;

public class JReleasatorIntegrationTest {

    private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

    private File resources;

    private File wc;

    private File settingsFile;

    private String tmpbase;

    private String repoUrl;

    @Before
    public void cleanup() throws IOException, TransformerException, InterruptedException {
        final File f = new File(getClass().getResource("/data/minimal-settings.xml").getFile()).getParentFile();
        resources = f.getParentFile();
        File testDataDir = resources.getName().equals("test-classes") ? resources.getParentFile() : resources;
        testDataDir = new File(testDataDir, "test-data");
        tmpbase = testDataDir.getAbsolutePath();
        System.err.println("removing directory: " + testDataDir);
        FileUtils.deleteDirectory(testDataDir);
        final File svnRepo = new File(testDataDir, "svnrepo");
        wc = new File(testDataDir, "wc");
        settingsFile = new File(testDataDir, "settings.xml");
        final File currentSettings = new File(System.getProperty("user.home"), ".m2/settings.xml");
        prepareSettings(settingsFile, new File(resources, "data/minimal-settings.xml"), currentSettings);
        repoUrl = Helper.svnRepoPrepareAndCheckout(svnRepo, wc);
    }

    private void prepareSettings(File settingsFile, File templateSettings, File currentSettings) throws TransformerException, IOException {
        final URL url = getClass().getResource("/settings-AddMirrors.xsl");
        final InputStream stream = url.openStream();
        try {
            final Transformer t = TRANSFORMER_FACTORY.newTransformer(new StreamSource(stream));
            t.setParameter("current.settings.xml", currentSettings.getAbsolutePath());
            t.setParameter("tmp.repo.base", tmpbase);
            settingsFile.getParentFile().mkdirs();
            t.transform(new StreamSource(templateSettings), new StreamResult(settingsFile));
        } finally {
            stream.close();
        }
    }

    @Test
    public void testMultiModuleWithOneChild() throws Exception {
        Helper.doCmd(wc, "svn", "update", "trunk");
        final File simple = new File(wc, "trunk/multi1/simple");
        simple.mkdirs();
        final File multi = simple.getParentFile();
        FileUtils.copyFile(new File(resources, "data/multi1.pom.xml"), new File(multi, "pom.xml"));
        FileUtils.copyFile(new File(resources, "data/multi.changes.xml"), new File(multi, "changes.xml"));
        FileUtils.copyFile(new File(resources, "data/simple.pom.xml"), new File(simple, "pom.xml"));
        Helper.doCmd(wc, "svn", "add", multi.getAbsolutePath());
        Helper.doCmd(wc, "svn", "ci", "-m", "adding modules", multi.getAbsolutePath());
        JReleasator.main("--tmpbase", tmpbase, "--settings", settingsFile.getAbsolutePath(), "--author", "releasator@buildbox.sf.net", "full", "scm:svn:" + repoUrl + "/trunk/multi1", "1.0.0-alpha-2", "container with one child");
    }

    @Test
    public void testMultiModuleWithTwoChildren() throws Exception {
        Helper.doCmd(wc, "svn", "update", "trunk");
        final File first = new File(wc, "trunk/multi2/first");
        final File second = new File(wc, "trunk/multi2/second");
        first.mkdirs();
        final File multi = first.getParentFile();
        FileUtils.copyFile(new File(resources, "data/multi2.pom.xml"), new File(multi, "pom.xml"));
        FileUtils.copyFile(new File(resources, "data/multi.changes.xml"), new File(multi, "changes.xml"));
        FileUtils.copyFile(new File(resources, "data/simple.pom.xml"), new File(first, "pom.xml"));
        FileUtils.copyFile(new File(resources, "data/second.pom.xml"), new File(second, "pom.xml"));
        Helper.doCmd(wc, "svn", "add", multi.getAbsolutePath());
        Helper.doCmd(wc, "svn", "ci", "-m", "adding modules", multi.getAbsolutePath());
        JReleasator.main("--tmpbase", tmpbase, "--settings", settingsFile.getAbsolutePath(), "--author", "releasator@buildbox.sf.net", "full", "scm:svn:" + repoUrl + "/trunk/multi2", "1.0.0-alpha-3", "container with two children");
    }

    @Test
    public void testSingleModule() throws Exception {
        Helper.doCmd(wc, "svn", "update", "underworld/trunk");
        final File myModule = new File(wc, "underworld/trunk/single");
        myModule.mkdirs();
        FileUtils.copyFile(new File(resources, "data/simple.pom.xml"), new File(myModule, "pom.xml"));
        final File changesXml = new File(myModule, "changes.xml");
        FileUtils.copyFile(new File(resources, "data/simple.changes.xml"), changesXml);
        Helper.doCmd(wc, "svn", "add", myModule.getAbsolutePath());
        Helper.doCmd(wc, "svn", "ci", "-m", "adding module", myModule.getAbsolutePath());
        JReleasator.main("--tmpbase", tmpbase, "--settings", settingsFile.getAbsolutePath(), "--author", "releasator@buildbox.sf.net", "full", repoUrl + "/underworld/trunk/single", "1.0.0-alpha-1");
        Helper.doCmd(wc, "svn", "update");
        final ChangesDocumentBean changes = ChangesDocumentBean.Factory.parse(changesXml);
        final String tag = changes.getChanges().getReleaseArray(0).getVcs().getTag();
        Assert.assertEquals("/underworld/tags/net.sf.buildbox.releasator.integration-test-test-simple-1.0.0-alpha-1", tag);
    }
}
