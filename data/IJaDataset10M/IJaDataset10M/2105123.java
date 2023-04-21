package com.acv.webapp.taglib;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.acv.dao.common.Constants;
import com.acv.dao.profiles.model.UserProfile;
import com.acv.dao.security.model.Audience;
import com.acv.service.common.ContentManager;
import com.acv.service.profile.UserProfileManager;

/**
 * Tag that retrieves list of static contents of the same type (Opportunity, FAQ or Policy)from the cms
 * Once retrieved, the list of contents is stored in the page context for beeing accessible in the jsp.
 */
public class ContentListRetrieverTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(ContentListRetrieverTag.class);

    private String type;

    private String id;

    private static final String OPPORTUNITY_TYPE = "Opportunity";

    private static final String FAQ_TYPE = "FAQ";

    private static final String POLICY_TYPE = "Policy";

    private transient ContentManager contentManager;

    /**
	 * Process the start tag for this instance. This method is invoked by the JSP page implementation object.
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
    public int doStartTag() {
        return EVAL_PAGE;
    }

    /**
	 * Process the end tag for this instance. This method is invoked by the JSP page implementation object on all Tag handlers.
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 * retrieve content from <code>contentManager</code>
	 */
    public int doEndTag() throws JspException {
        ServletContext servletContext = this.pageContext.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        UserProfileManager userProfileManager = (UserProfileManager) wac.getBean("userProfileManager");
        String audience = Constants.CMS_AUDIENCE_B2C;
        UserProfile userProfile = userProfileManager.getCurrentUser();
        Audience userAudience = userProfile.getAudience();
        if (userAudience != null) {
            audience = userAudience.getCmsAudience();
        }
        HttpSession session = this.pageContext.getSession();
        Locale currentLocale = (Locale) session.getAttribute(Constants.PREFERRED_LOCALE_KEY);
        String langCode = currentLocale.getLanguage().toUpperCase();
        this.contentManager = (ContentManager) wac.getBean("contentManager");
        List result = null;
        Timestamp now = (Timestamp) this.pageContext.getSession().getAttribute(Constants.CONTENT_TIMESTAMP);
        if (log.isDebugEnabled()) log.debug("Timestamp gotten from the request: " + now.toString());
        if (Constants.PRESS_RELEASE_TYPE.equals(type)) {
            result = this.getPressReleases(langCode, audience, now);
        }
        if (Constants.TRAVEL_NEWS_TYPE.equals(type)) {
            result = this.getTravelNews(langCode, audience, now);
        }
        if (Constants.NEWSLETTER_TYPE.equals(type)) {
            result = this.getNewsletters(langCode, audience, now);
        }
        if (OPPORTUNITY_TYPE.equals(type)) {
            result = this.getOpportunities(langCode, audience, now);
        }
        if (FAQ_TYPE.equals(type)) {
            result = this.getFAQs(langCode, audience, now);
        }
        if (POLICY_TYPE.equals(type)) {
            result = this.getPolicies(langCode, audience, now);
        }
        this.pageContext.setAttribute(id, result);
        return EVAL_PAGE;
    }

    /**
	 * get content of type PressReleases
	 * @param langCode the lang
	 * @param audience the audience
	 * @param timestamp the date
	 * @return a list of PressReleases
	 */
    private List getPressReleases(String langCode, String audience, Timestamp timestamp) {
        return this.contentManager.getPressReleases(langCode, audience, timestamp);
    }

    /**
	 * get content of type TravelNews
	 * @param langCode the lang
	 * @param audience the audience
	 * @param timestamp the date
	 * @return a list of TravelNews
	 */
    private List getTravelNews(String langCode, String audience, Timestamp timestamp) {
        return this.contentManager.getTravelNews(langCode, audience, timestamp);
    }

    /**
	 * get content of type Newsletters
	 * @param langCode the lang
	 * @param audience the audience
	 * @param timestamp the date
	 * @return a list of Newsletters
	 */
    private List getNewsletters(String langCode, String audience, Timestamp timestamp) {
        return this.contentManager.getNewsletters(langCode, audience, timestamp);
    }

    /**
	 * get content of type Opportunities
	 * @param langCode the lang
	 * @param audience the audience
	 * @param timestamp the date
	 * @return a list of Opportunities
	 */
    private List getOpportunities(String langCode, String audience, Timestamp timestamp) {
        return this.contentManager.getOpportunities(langCode, audience, timestamp);
    }

    /**
	 * get content of type FAQ
	 * @param langCode the lang
	 * @param audience the audience
	 * @param timestamp the date
	 * @return a list of FAQ
	 */
    private List getFAQs(String langCode, String audience, Timestamp timestamp) {
        return this.contentManager.getFAQs(langCode, audience, timestamp);
    }

    /**
	 * get content of type policies
	 * @param langCode the lang
	 * @param audience the audience
	 * @param timestamp the date
	 * @return a list of policies
	 */
    private List getPolicies(String langCode, String audience, Timestamp timestamp) {
        return this.contentManager.getPolicies(langCode, audience, timestamp);
    }

    /**
	 * get he id that will be used to get the List
	 * @return id
	 */
    public String getId() {
        return id;
    }

    /**
	 * set the id that will be used to get the List
	 * @param id The id that will be used to get the List
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * get the type of the content list to retrieve.
	 * @return The type of the content list to retrieve.
	 */
    public String getType() {
        return type;
    }

    /**
	 * set the type of the content list to retrieve.
	 * @param type The type of the content list to retrieve.
	 */
    public void setType(String type) {
        this.type = type;
    }
}
