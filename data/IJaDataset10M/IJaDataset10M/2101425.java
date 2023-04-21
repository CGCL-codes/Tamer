package com.mttaboros.feed;

import com.mttaboros.health.feed.FeedClient;
import com.mttaboros.health.authsub.AuthenticationException;
import com.mttaboros.util.IOUtils;
import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Person;
import com.sun.syndication.io.FeedException;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.security.GeneralSecurityException;

/**
 * <p/>
 * Test case for ROME based atom feed parsing.
 * </p>
 *
 * @author az@mttaboros.com
 */
public class FeedClientTestCase extends TestCase {

    public void testParseBuildAtomFeed() throws FeedException, IOException, JDOMException, ParserConfigurationException, SAXException, GeneralSecurityException, AuthenticationException {
        InputStream stream = this.getClass().getResourceAsStream("/exampleResponseFromSendEntry.xml");
        Assert.assertEquals(1, FeedClient.buildAtomFeedFromEntryXml(IOUtils.toString(stream)).getEntries().size());
    }

    public void testParseExampleNoticesFeed() throws FeedException, IOException, JDOMException {
        assertParseNoticesFeed(FeedClient.getAtomFeed(this.getClass().getResourceAsStream("/exampleNoticesFeed.xml"), true));
    }

    public void testParseExampleProfileFeed() throws FeedException, IOException, JDOMException {
        assertParseProfileFeed(FeedClient.getAtomFeed(this.getClass().getResourceAsStream("/examplehealth_ProfileFeed.xml"), true));
    }

    public void testParseExampleProfileFeedWithXmlnsOnEntries() throws FeedException, IOException, JDOMException {
        assertParseProfileFeed(FeedClient.getAtomFeed(this.getClass().getResourceAsStream("/exampleProfileWithXmlnsEntry.xml"), true));
    }

    private void assertParseNoticesFeed(Feed atomFeed) throws FeedException, IOException, JDOMException {
        int ccrCounter = 0;
        Assert.assertEquals(5, atomFeed.getEntries().size());
        String output = "";
        Iterator entryIterator = atomFeed.getEntries().iterator();
        while (entryIterator.hasNext()) {
            Entry entry = (Entry) entryIterator.next();
            output += "<div>\n";
            output += "From: " + ((Person) entry.getAuthors().toArray()[0]).getName() + "<br/>\n";
            output += "Date: " + entry.getPublished() + "<br/>\n";
            output += "Subject: " + entry.getTitle() + "<br/>\n";
            output += "Message: " + ((entry.getContents().isEmpty()) ? "empty" : ((Content) entry.getContents().toArray()[0]).getValue()) + "<br/>\n";
            Iterator foreignMarkupIterator = ((List) entry.getForeignMarkup()).iterator();
            while (foreignMarkupIterator.hasNext()) {
                Element element = (Element) foreignMarkupIterator.next();
                if ("ContinuityOfCareRecord".equals(element.getName())) {
                    ccrCounter++;
                    output += "Contains CCR data:\n";
                    output += "<pre>\n";
                    XMLOutputter xmlOutputter = new XMLOutputter();
                    xmlOutputter.setFormat(Format.getPrettyFormat());
                    StringWriter writer = new StringWriter();
                    xmlOutputter.output(element, writer);
                    output += writer.getBuffer().toString();
                    output += "</pre>\n";
                }
            }
            output += "</div><br/>\n";
        }
        Assert.assertEquals(3, ccrCounter);
    }

    private void assertParseProfileFeed(Feed atomFeed) throws FeedException, IOException, JDOMException {
        int ccrCounter = 0;
        Assert.assertEquals(1, atomFeed.getEntries().size());
        String output = "";
        Iterator iterator = atomFeed.getEntries().iterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            Iterator foreignMarkupIterator = ((List) entry.getForeignMarkup()).iterator();
            while (foreignMarkupIterator.hasNext()) {
                Element element = (Element) foreignMarkupIterator.next();
                if ("ContinuityOfCareRecord".equals(element.getName())) {
                    ccrCounter++;
                    output += "Contains CCR data:\n";
                    output += "<pre>\n";
                    XMLOutputter xmlOutputter = new XMLOutputter();
                    xmlOutputter.setFormat(Format.getPrettyFormat());
                    StringWriter writer = new StringWriter();
                    xmlOutputter.output(element, writer);
                    output += writer.getBuffer().toString();
                    output += "</pre>\n";
                }
            }
            output += "</div><br/>\n";
        }
        Assert.assertEquals(1, ccrCounter);
    }
}
