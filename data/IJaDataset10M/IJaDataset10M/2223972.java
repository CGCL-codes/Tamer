package org.rascalli.mbe.adaptiveMusicCompanion;

import java.util.List;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.rascalli.webui.ws.RascalliWSImpl;
import org.rascalli.webui.ws.RascalliWSImplService;
import org.rascalli.webui.ws.Rascallo;
import org.rascalli.webui.ws.WebContent;

/**
 * <p>
 * A test application for RSS feed integration in SimpleMusicCompanion.
 * </p>
 * 
 * <p>
 * User: rss-feed-test, password: 123, user-id: 28, agent-id: 37
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: $<br/> $Date: $<br/> $Revision: $
 * </p>
 * 
 * @author Christian Schollum
 */
public class RssFeedIntegrationTestApp {

    Mockery context = new JUnit4Mockery();

    @Test
    public void integrationTest() throws InterruptedException {
    }

    public static void main(String[] args) {
        printRascalloFeeds(40);
    }

    /**
     * @param rascalliWS
     */
    @SuppressWarnings("unused")
    private static void printUsersAgentsFeeds() {
        RascalliWSImpl rascalliWS = new RascalliWSImplService().getRascalliWSImplPort();
        final List<org.rascalli.webui.ws.User> users = rascalliWS.loadAllUsers();
        for (org.rascalli.webui.ws.User user : users) {
            System.out.println(user.getName() + " [" + user.getId() + "]");
            final List<Rascallo> agents = rascalliWS.loadRascallosFromUser(user.getId());
            for (Rascallo agent : agents) {
                System.out.println("  " + agent.getName() + " [" + agent.getId() + "]");
                List<WebContent> webContentList = rascalliWS.loadWebContentFromRascallo(agent.getId());
                for (WebContent webContent : webContentList) {
                    if (webContent.isRssFeed()) {
                        System.out.println("    " + webContent.getUrl());
                    }
                }
            }
        }
    }

    /**
     * @param rascalliWS
     * @param agent
     */
    private static void printRascalloFeeds(int agentId) {
        RascalliWSImpl rascalliWS = new RascalliWSImplService().getRascalliWSImplPort();
        List<WebContent> webContentList = rascalliWS.loadWebContentFromRascallo(agentId);
        for (WebContent webContent : webContentList) {
            if (webContent.isRssFeed()) {
                System.out.println("    " + webContent.getUrl());
            }
        }
    }
}
