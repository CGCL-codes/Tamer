package org.rascalli.framework.wsbcs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.rascalli.framework.core.Agent;
import org.rascalli.framework.core.AgentConfiguration;
import org.rascalli.framework.properties.UrlList;
import org.rascalli.framework.properties.UrlList.RssUrl;
import org.rascalli.webui.ws.Rascallo;
import org.rascalli.webui.ws.WebContent;

public class AgentConfigurationTransformationTest {

    private static final Rascallo RASCALLO_0 = createRascalloWithId(0).ofUser(0).withAgentDefinitionVersion(1).withProperty("p1", "v1").withProperty("p2", "v2").done();

    private static RascalloBuilder createRascalloWithId(int id) {
        return new RascalloBuilder(id);
    }

    @Test
    public void testTransformation() throws MalformedURLException {
        List<RssUrl> rssUrls = Arrays.asList(new RssUrl(1, new URL("http://my.feed.org")), new RssUrl(2, new URL("http://news.com/feed")));
        List<WebContent> webContents = Arrays.asList(createWebContent(true, "http://my.feed.org", 1), createWebContent(true, "http://news.com/feed", 2), createWebContent(false, "http://some.other.url", 3), createWebContent(true, "invalid url", 4));
        AgentConfiguration agentConfiguration = AgentConfigurationTransformation.transform(RASCALLO_0, webContents);
        Assert.assertNotNull(agentConfiguration);
        Assert.assertEquals(0, agentConfiguration.getAgentId());
        Assert.assertEquals("agent-definition-1", agentConfiguration.getAgentFactoryId());
        Assert.assertEquals(0, agentConfiguration.getUserId());
        Assert.assertEquals(4, agentConfiguration.getAgentProperties().size());
        Assert.assertEquals("rascallo 0", agentConfiguration.getProperty(Agent.P_AGENT_NAME));
        Assert.assertEquals(new UrlList(rssUrls), agentConfiguration.getProperty(Agent.P_AGENT_RSS_URLS));
        Assert.assertEquals("v1", agentConfiguration.getProperty("p1"));
        Assert.assertEquals("v2", agentConfiguration.getProperty("p2"));
    }

    private WebContent createWebContent(boolean isRssFeed, String url, Integer webContentId) {
        WebContent webContent = new WebContent();
        webContent.setRssFeed(isRssFeed);
        webContent.setUrl(url);
        webContent.setId(webContentId);
        return webContent;
    }
}
