package de.fzi.wikipipes.impl.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;
import javax.xml.transform.TransformerException;
import junit.framework.Assert;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.junit.Ignore;
import org.junit.Test;
import de.fzi.wikipipes.IWikiPage;
import de.fzi.wikipipes.IWikiRepository;
import de.fzi.wikipipes.impl.AbstractWebWikiRepository;
import de.fzi.wikipipes.impl.Util;
import de.fzi.wikipipes.impl.XMLUtil;
import de.fzi.wikipipes.impl.filesystem.WikiPageFS;
import de.fzi.wikipipes.impl.filesystem.WikiRepositoryFS;

/**
 * @author voelkel
 */
public abstract class AbstractWikiRepositoryTest {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(AbstractWikiRepositoryTest.class);

    /** must be initialized by tests */
    protected IWikiRepository rep = null;

    protected File testResultRootDir = null;

    protected String testPageName = null;

    @Test
    public void testSend() {
        String pageName = "WifTransformation";
        if (this.testPageName != null) pageName = this.testPageName;
        IWikiPage temp = this.rep.getPageByName(pageName);
        long l = System.currentTimeMillis();
        Assert.assertNotNull(temp);
        String raw = temp.rawWikiPage().getNativeWikitext();
        temp.rawWikiPage().setNativeWikitext(raw + "\nHello" + l);
        String result = temp.rawWikiPage().getNativeWikitext();
        Assert.assertTrue("result is " + result, result.contains("Hello" + l));
    }

    /** @return a Reader which contains a sample of native wiki syntax */
    public abstract Reader getWikiSyntaxExample();

    /** @return a Reader which contains the corresponding WIF content */
    public abstract Reader getWikiSyntaxExample_asWIF();

    public void testNativeToWIF() throws IOException {
        Reader nativeReader = this.getWikiSyntaxExample();
        Assert.assertNotNull(nativeReader);
        Document originalWifDoc = Util.getReaderAsDocument(this.getWikiSyntaxExample_asWIF());
        XMLUtil.filterRelevant(originalWifDoc);
        String originalWif = XMLUtil.toCanonicalXML(originalWifDoc, 70);
        String wikisyntax = Util.getAsString(nativeReader);
        IWikiPage temp = this.rep.getPageByName("WifTransformation");
        temp.rawWikiPage().setNativeWikitext(wikisyntax);
        Reader secondWifReader = temp.getContentAsWif();
        Assert.assertNotNull(secondWifReader);
        Document secondWifDoc = Util.getReaderAsDocument(secondWifReader);
        secondWifDoc.setDocType(originalWifDoc.getDocType());
        XMLUtil.filterRelevant(secondWifDoc);
        String secondWif = XMLUtil.toCanonicalXML(secondWifDoc, 70);
        Assert.assertEquals(originalWif, secondWif);
    }

    /**
	 * load example as native, load example as wif, transform wif to native,
	 * compare
	 */
    public void testWifToNative() {
        String originalSyntax = Util.getAsString(this.getWikiSyntaxExample());
        String transformedSyntax = this.rep.syntaxConverter().toWikiSyntax(this.getWikiSyntaxExample_asWIF());
        transformedSyntax = XMLUtil.removeTransfWhiteSpaces(transformedSyntax);
        Assert.assertEquals(originalSyntax, transformedSyntax);
    }

    /**
	 * get and set wiki syntax
	 */
    @Test
    public void testGetRaw() {
        IWikiPage page = this.rep.getPageByName("WifTransformation");
        String testString = "Hello";
        String backup = page.rawWikiPage().getNativeWikitext();
        page.rawWikiPage().setNativeWikitext("");
        String web = page.rawWikiPage().getNativeWikitext();
        Assert.assertFalse(web.contains(testString));
        page.rawWikiPage().setNativeWikitext(testString);
        web = page.rawWikiPage().getNativeWikitext();
        page.rawWikiPage().setNativeWikitext(backup);
        Assert.assertTrue(web.contains(testString));
    }

    @Test
    public void testGetAllPages() {
        Collection<? extends IWikiPage> result = this.rep.getAllWifPages();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    @Ignore("many adapters dont support this yet")
    public void testFindPages() {
        Collection<? extends IWikiPage> result = this.rep.findPages("wiki+rdf");
        if (((AbstractWebWikiRepository) this.rep).getServerURL().indexOf("localhost:8080") != -1) log.warn("wiki at localhost:8080 dont support search, testing shows success"); else {
            Assert.assertNotNull(result);
            Assert.assertTrue(result.size() > 0);
        }
        log.debug("query returned " + result.size() + " results");
    }

    @Test
    public void testWritePageToFileSystem() throws IOException {
        String pageName = "WifTransformation";
        if (this.testPageName != null) pageName = this.testPageName;
        IWikiPage sandbox = this.rep.getPageByName(pageName);
        if (!this.testResultRootDir.exists()) this.testResultRootDir.mkdirs();
        File directory = new File(this.testResultRootDir, "testWritePageToFileSystem");
        directory.mkdirs();
        WikiRepositoryFS filesystem = new WikiRepositoryFS(directory);
        assertTrue(directory.exists());
        assertNotNull(sandbox);
        IWikiPage sandbox_fs = new WikiPageFS(filesystem, pageName);
        Reader in = sandbox.getContentAsWif();
        sandbox_fs.setContentFromWif(in);
    }

    public Reader getExampleWif() {
        return TestData.getExampleWif();
    }

    public void testSendContent() throws HttpException, IOException {
        String pageWikiName = new String("WifTransformation");
        IWikiPage page = this.rep.getPageByName(pageWikiName);
        Document wiflocalDoc = Util.getReaderAsDocument(this.getExampleWif());
        page.setContentFromWif(this.getExampleWif());
        Reader wifReader = page.getContentAsWif();
        Document wifwebDoc = Util.getReaderAsDocument(wifReader);
        wifwebDoc.setDocType(wiflocalDoc.getDocType());
        XMLUtil.filterRelevant(wiflocalDoc);
        XMLUtil.filterRelevant(wifwebDoc);
        String wif_local = XMLUtil.toCanonicalXML(wiflocalDoc, 70);
        String wif_web = XMLUtil.toCanonicalXML(wifwebDoc, 70);
        Assert.assertEquals(wif_local, wif_web);
    }

    @Test
    public void testGetChangeDate() {
        String pageWikiName = new String("WifTransformation");
        IWikiPage page = this.rep.getPageByName(pageWikiName);
        long date = page.getChangeDate();
        log.debug("\nlast change date of given test page: " + date + ": " + new Date(date).toString());
        Assert.assertTrue(date != 0);
    }

    @Test
    public void testGetAuthor() {
        String pageWikiName = new String("WifTransformation");
        IWikiPage page = this.rep.getPageByName(pageWikiName);
        log.debug(page.getAuthor());
        assertNotNull(page.getAuthor());
    }

    /**
	 * read wif example file, transform in wiki syntax, upload in wiki, get
	 * first wif from wiki, transform it in wiki syntax, upload in wiki, get
	 * second wif, assert first and second are equal
	 * 
	 * @throws IOException
	 */
    @Test
    public void testRoundTripping() throws IOException {
        Reader wifReader = this.getWikiSyntaxExample_asWIF();
        Assert.assertNotNull(wifReader);
        String wikisyntax = this.rep.syntaxConverter().toWikiSyntax(wifReader);
        IWikiPage temp = this.rep.getPageByName("WifTransformation");
        temp.rawWikiPage().setNativeWikitext(wikisyntax);
        Reader firstWifReader = temp.getContentAsWif();
        Assert.assertNotNull(firstWifReader);
        Document firstDoc = Util.getReaderAsDocument(firstWifReader);
        wikisyntax = this.rep.syntaxConverter().toWikiSyntax(Util.getDocumentAsReader(firstDoc));
        temp.rawWikiPage().setNativeWikitext(wikisyntax);
        Reader secondWifReader = temp.getContentAsWif();
        Assert.assertNotNull(secondWifReader);
        Document secondDoc = Util.getReaderAsDocument(secondWifReader);
        Assert.assertEquals(firstDoc.asXML(), secondDoc.asXML());
    }

    /**
	 * test whether IWikiPage.exists does work correct
	 * 
	 */
    @Test
    public void testPageExistence() {
        IWikiPage realPage = this.rep.getPageByName("WifTransformation");
        Assert.assertTrue(realPage.exists());
        IWikiPage noPage = this.rep.getPageByName("ThereIsNoWaySomeoneCreatedAPageInThisWikiWithSuchADumbName");
        Assert.assertFalse(noPage.exists());
    }

    /**
	 * test get page by Uri funtionality
	 * 
	 */
    @Test
    public void testGetPageByURI() {
        String uri = this.rep.getPageByName("WifTransformation").getSource();
        IWikiPage page = this.rep.getPageByURI(uri);
        Assert.assertEquals("WifTransformation", page.getName());
    }
}
