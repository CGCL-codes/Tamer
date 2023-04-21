package org.encuestame.mvc.test.syndication;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import junit.framework.Assert;
import org.encuestame.mvc.test.config.AbstractJsonMvcUnitBeans;
import org.encuestame.mvc.view.TweetPollAtomFeedView;
import org.encuestame.mvc.view.TweetPollRssFeedView;
import org.encuestame.persistence.domain.tweetpoll.TweetPoll;
import org.encuestame.utils.enums.MethodJson;
import org.encuestame.utils.json.TweetPollBean;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Test Cases for Feed Controller, RSS and ATOM.
 * @author Picado, Juan juanATencuestame.org
 * @since Oct 24, 2010 8:57:54 PM
 * @version $Id:$
 */
public class TweetPollFeedControllerTestCase extends AbstractJsonMvcUnitBeans {

    /**
     * TweetPoll..
     */
    TweetPoll tweetPoll;

    /** TweetPollAtomFeedView. **/
    private TweetPollAtomFeedView tweetPollAtomFeedView;

    /** TweetPollRssFeedView. **/
    private TweetPollRssFeedView tweetPollRssFeedView;

    /**
     * Before.
     */
    @Before
    public void initView() {
        Assert.assertNotNull(this.tweetPollAtomFeedView);
        Assert.assertNotNull(this.tweetPollRssFeedView);
        createFakesTweetPoll(getSpringSecurityLoggedUserAccount());
    }

    /**
     * Test for TweetPollRssFeedView.
     * @throws Exception exception
     */
    @Test
    public void testTweetPollRssFeedView() throws Exception {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("tweetPolls", new ArrayList<TweetPollBean>());
        this.tweetPollRssFeedView.render(model, request, response);
        Assert.assertEquals("application/rss+xml", response.getContentType());
        assertXpathExists("//rss", response.getContentAsString());
        assertXpathExists("//description", response.getContentAsString());
        assertXpathExists("//copyright", response.getContentAsString());
        assertXpathExists("//pubDate", response.getContentAsString());
    }

    /**
     * Test TweetPollAtomFeedView.
     * @throws Exception exception.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTweetPollAtomFeedView() throws Exception {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("tweetPolls", new ArrayList<TweetPollBean>());
        this.tweetPollAtomFeedView.render(model, request, response);
        log.debug(response.getContentType());
        log.debug(response.getContentAsString());
        Assert.assertEquals("application/atom+xml", response.getContentType());
        final SAXBuilder builder = new SAXBuilder();
        org.jdom.Document document = builder.build(new StringReader(response.getContentAsString()));
        final Element root = document.getRootElement();
        final Namespace name = root.getNamespace();
        Assert.assertEquals(name.getURI(), "http://www.w3.org/2005/Atom");
        final Element title = (Element) root.getChildren("title", name).get(0);
        Assert.assertEquals(title.getName(), "title");
        Assert.assertEquals(title.getValue(), "TweetPoll Published");
        final Element id = (Element) root.getChildren("id", name).get(0);
        Assert.assertEquals(id.getName(), "id");
        Assert.assertEquals(id.getValue(), "TweetPoll Published");
    }

    /**
     * @throws IOException
     * @throws ServletException
     * @throws JDOMException
     *
     */
    @Test
    public void testTweetpollDOTatom() throws ServletException, IOException, JDOMException {
        initService("/feed/" + getSpringSecurityLoggedUserAccount().getUsername() + "/tweetpoll.atom", MethodJson.GET);
        final Document response = callFeedService();
        Assert.assertEquals(response.getRootElement().getName(), "feed");
    }

    /**
     * @throws IOException
     * @throws ServletException
     * @throws JDOMException
     *
     */
    @Test
    public void testTweetpollDOTrss() throws ServletException, IOException, JDOMException {
        initService("/feed/" + getSpringSecurityLoggedUserAccount().getUsername() + "/tweetpoll.rss", MethodJson.GET);
        final Document response = callFeedService();
        Assert.assertEquals(response.getRootElement().getName(), "rss");
    }

    /**
     * @return the tweetPollAtomFeedView
     */
    public TweetPollAtomFeedView getTweetPollAtomFeedView() {
        return tweetPollAtomFeedView;
    }

    /**
     * @param tweetPollAtomFeedView the tweetPollAtomFeedView to set
     */
    @Autowired
    public void setTweetPollAtomFeedView(final TweetPollAtomFeedView tweetPollAtomFeedView) {
        this.tweetPollAtomFeedView = tweetPollAtomFeedView;
    }

    /**
     * @return the tweetPollRssFeedView
     */
    public TweetPollRssFeedView getTweetPollRssFeedView() {
        return tweetPollRssFeedView;
    }

    /**
     * @param tweetPollRssFeedView the tweetPollRssFeedView to set
     */
    @Autowired
    public void setTweetPollRssFeedView(final TweetPollRssFeedView tweetPollRssFeedView) {
        this.tweetPollRssFeedView = tweetPollRssFeedView;
    }
}
