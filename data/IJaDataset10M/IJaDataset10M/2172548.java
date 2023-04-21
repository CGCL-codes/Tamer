package de.beas.explicanto.distribution.rss.impl;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import de.beas.explicanto.distribution.ApplicationException;
import de.beas.explicanto.distribution.config.Constants;
import de.beas.explicanto.distribution.dao.CourseDAO;
import de.beas.explicanto.distribution.model.RssItem;
import de.beas.explicanto.distribution.model.Survey;
import de.beas.explicanto.distribution.rss.RSSManager;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.exporters.RSS_0_91_Exporter;
import de.nava.informa.impl.basic.Channel;
import de.nava.informa.impl.basic.ChannelBuilder;

/**
 * @author Herta Paul
 */
public class RSSManagerImpl implements RSSManager {

    private CourseDAO courseDAO = null;

    public void addContent(String id, int entity, Set set, String chanTitle, Writer writer) {
        try {
            ChannelBuilder builder = new ChannelBuilder();
            ChannelIF channel = new Channel(chanTitle);
            for (Iterator itr = set.iterator(); itr.hasNext(); ) {
                RssItem rssItem = (RssItem) itr.next();
                StringBuffer itemDescription = new StringBuffer(rssItem.getDescription());
                if (Constants.USER_ENTITY == entity || Constants.GROUP_ENTITY == entity || Constants.PROFILE_ENTITY == entity) {
                    itemDescription.append("<BR/><a href=\"" + Constants.URL + "/admin/Login.action?url=play&id=" + rssItem.getCourseId() + "\">Start course</a>");
                    itemDescription.append(", <a href=\"" + Constants.URL + "/admin/Login.action?url=blog&id=" + rssItem.getBlogId() + "\">Blog comment</a>");
                    Survey survey = courseDAO.getCourse(rssItem.getCourseId()).getPublishedSurvey();
                    if (survey != null) {
                        itemDescription.append(", <a href=\"" + Constants.URL + "/admin/Login.action?url=survey&id=" + survey.getId() + "\">Attend survey</a>");
                    }
                    itemDescription.append("<BR/>");
                }
                ItemIF item1 = builder.createItem(channel, rssItem.getName(), itemDescription.toString(), new URL(rssItem.getLink()));
            }
            RSS_0_91_Exporter exp = new RSS_0_91_Exporter(writer, Constants.UTF8);
            exp.write(channel);
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
	 * @param courseDAO The courseDAO to set.
	 */
    public void setCourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }
}
