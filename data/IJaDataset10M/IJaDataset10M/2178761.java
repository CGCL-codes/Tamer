package com.liferay.portlet.journal.service.impl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.servlet.ImageServletTokenUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lucene.LuceneFields;
import com.liferay.portal.lucene.LuceneUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.User;
import com.liferay.portal.model.impl.ResourceImpl;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.impl.ImageLocalUtil;
import com.liferay.portal.service.persistence.CompanyUtil;
import com.liferay.portal.service.persistence.UserUtil;
import com.liferay.portal.servlet.filters.layoutcache.LayoutCacheUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portlet.journal.ArticleContentException;
import com.liferay.portlet.journal.ArticleDisplayDateException;
import com.liferay.portlet.journal.ArticleExpirationDateException;
import com.liferay.portlet.journal.ArticleIdException;
import com.liferay.portlet.journal.ArticleReviewDateException;
import com.liferay.portlet.journal.ArticleTitleException;
import com.liferay.portlet.journal.ArticleTypeException;
import com.liferay.portlet.journal.DuplicateArticleIdException;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.NoSuchArticleResourceException;
import com.liferay.portlet.journal.NoSuchTemplateException;
import com.liferay.portlet.journal.job.CheckArticleJob;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleDisplay;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.model.impl.JournalArticleDisplayImpl;
import com.liferay.portlet.journal.model.impl.JournalArticleImpl;
import com.liferay.portlet.journal.service.JournalArticleImageLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalContentSearchLocalServiceUtil;
import com.liferay.portlet.journal.service.base.JournalArticleLocalServiceBaseImpl;
import com.liferay.portlet.journal.service.persistence.JournalArticleFinder;
import com.liferay.portlet.journal.service.persistence.JournalArticleUtil;
import com.liferay.portlet.journal.service.persistence.JournalStructureUtil;
import com.liferay.portlet.journal.service.persistence.JournalTemplateUtil;
import com.liferay.portlet.journal.util.Indexer;
import com.liferay.portlet.journal.util.JournalUtil;
import com.liferay.portlet.journal.util.comparator.ArticleDisplayDateComparator;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.ratings.service.RatingsStatsLocalServiceUtil;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LocalizationUtil;
import com.liferay.util.MathUtil;
import com.liferay.util.lucene.HitsImpl;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.internet.InternetAddress;
import javax.portlet.PortletPreferences;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserUtils;

/**
 * <a href="JournalArticleLocalServiceImpl.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JournalArticleLocalServiceImpl extends JournalArticleLocalServiceBaseImpl {

    public JournalArticle addArticle(long userId, String articleId, boolean autoArticleId, long plid, String title, String description, String content, String type, String structureId, String templateId, int displayDateMonth, int displayDateDay, int displayDateYear, int displayDateHour, int displayDateMinute, int expirationDateMonth, int expirationDateDay, int expirationDateYear, int expirationDateHour, int expirationDateMinute, boolean neverExpire, int reviewDateMonth, int reviewDateDay, int reviewDateYear, int reviewDateHour, int reviewDateMinute, boolean neverReview, boolean indexable, Map images, String articleURL, PortletPreferences prefs, String[] tagsEntries, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {
        return addArticle(userId, articleId, autoArticleId, plid, title, description, content, type, structureId, templateId, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, images, articleURL, prefs, tagsEntries, Boolean.valueOf(addCommunityPermissions), Boolean.valueOf(addGuestPermissions), null, null);
    }

    public JournalArticle addArticle(long userId, String articleId, boolean autoArticleId, long plid, String title, String description, String content, String type, String structureId, String templateId, int displayDateMonth, int displayDateDay, int displayDateYear, int displayDateHour, int displayDateMinute, int expirationDateMonth, int expirationDateDay, int expirationDateYear, int expirationDateHour, int expirationDateMinute, boolean neverExpire, int reviewDateMonth, int reviewDateDay, int reviewDateYear, int reviewDateHour, int reviewDateMinute, boolean neverReview, boolean indexable, Map images, String articleURL, PortletPreferences prefs, String[] tagsEntries, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        return addArticle(userId, articleId, autoArticleId, plid, title, description, content, type, structureId, templateId, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, images, articleURL, prefs, tagsEntries, null, null, communityPermissions, guestPermissions);
    }

    public JournalArticle addArticle(long userId, String articleId, boolean autoArticleId, long plid, String title, String description, String content, String type, String structureId, String templateId, int displayDateMonth, int displayDateDay, int displayDateYear, int displayDateHour, int displayDateMinute, int expirationDateMonth, int expirationDateDay, int expirationDateYear, int expirationDateHour, int expirationDateMinute, boolean neverExpire, int reviewDateMonth, int reviewDateDay, int reviewDateYear, int reviewDateHour, int reviewDateMinute, boolean neverReview, boolean indexable, Map images, String articleURL, PortletPreferences prefs, String[] tagsEntries, Boolean addCommunityPermissions, Boolean addGuestPermissions, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        long groupId = PortalUtil.getPortletGroupId(plid);
        return addArticleToGroup(userId, articleId, autoArticleId, groupId, title, description, content, type, structureId, templateId, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, images, articleURL, prefs, tagsEntries, addCommunityPermissions, addGuestPermissions, communityPermissions, guestPermissions);
    }

    public JournalArticle addArticleToGroup(long userId, String articleId, boolean autoArticleId, long groupId, String title, String description, String content, String type, String structureId, String templateId, int displayDateMonth, int displayDateDay, int displayDateYear, int displayDateHour, int displayDateMinute, int expirationDateMonth, int expirationDateDay, int expirationDateYear, int expirationDateHour, int expirationDateMinute, boolean neverExpire, int reviewDateMonth, int reviewDateDay, int reviewDateYear, int reviewDateHour, int reviewDateMinute, boolean neverReview, boolean indexable, Map images, String articleURL, PortletPreferences prefs, String[] tagsEntries, Boolean addCommunityPermissions, Boolean addGuestPermissions, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        User user = UserUtil.findByPrimaryKey(userId);
        articleId = articleId.trim().toUpperCase();
        Date now = new Date();
        Date displayDate = PortalUtil.getDate(displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, user.getTimeZone(), new ArticleDisplayDateException());
        Date expirationDate = null;
        if (!neverExpire) {
            expirationDate = PortalUtil.getDate(expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, user.getTimeZone(), new ArticleExpirationDateException());
        }
        Date reviewDate = null;
        if (!neverReview) {
            reviewDate = PortalUtil.getDate(reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, user.getTimeZone(), new ArticleReviewDateException());
        }
        validate(groupId, articleId, autoArticleId, title, content, type, structureId, templateId);
        if (autoArticleId) {
            articleId = String.valueOf(CounterLocalServiceUtil.increment());
        }
        long id = CounterLocalServiceUtil.increment();
        long resourcePrimKey = JournalArticleResourceLocalServiceUtil.getArticleResourcePrimKey(groupId, articleId);
        JournalArticle article = JournalArticleUtil.create(id);
        content = format(groupId, articleId, article.getVersion(), false, content, structureId, images);
        article.setResourcePrimKey(resourcePrimKey);
        article.setGroupId(groupId);
        article.setCompanyId(user.getCompanyId());
        article.setUserId(user.getUserId());
        article.setUserName(user.getFullName());
        article.setCreateDate(now);
        article.setModifiedDate(now);
        article.setArticleId(articleId);
        article.setVersion(JournalArticleImpl.DEFAULT_VERSION);
        article.setTitle(title);
        article.setDescription(description);
        article.setContent(content);
        article.setType(type);
        article.setStructureId(structureId);
        article.setTemplateId(templateId);
        article.setDisplayDate(displayDate);
        article.setApproved(false);
        if ((expirationDate == null) || expirationDate.after(now)) {
            article.setExpired(false);
        } else {
            article.setExpired(true);
        }
        article.setExpirationDate(expirationDate);
        article.setReviewDate(reviewDate);
        article.setIndexable(indexable);
        JournalArticleUtil.update(article);
        if ((addCommunityPermissions != null) && (addGuestPermissions != null)) {
            addArticleResources(article, addCommunityPermissions.booleanValue(), addGuestPermissions.booleanValue());
        } else {
            addArticleResources(article, communityPermissions, guestPermissions);
        }
        updateTagsAsset(userId, article, tagsEntries);
        sendEmail(article, articleURL, prefs, "requested");
        return article;
    }

    public void addArticleResources(long groupId, String articleId, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {
        JournalArticle article = getLatestArticle(groupId, articleId);
        addArticleResources(article, addCommunityPermissions, addGuestPermissions);
    }

    public void addArticleResources(JournalArticle article, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {
        ResourceLocalServiceUtil.addResources(article.getCompanyId(), article.getGroupId(), article.getUserId(), JournalArticle.class.getName(), article.getResourcePrimKey(), false, addCommunityPermissions, addGuestPermissions);
    }

    public void addArticleResources(long groupId, String articleId, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        JournalArticle article = getLatestArticle(groupId, articleId);
        addArticleResources(article, communityPermissions, guestPermissions);
    }

    public void addArticleResources(JournalArticle article, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        ResourceLocalServiceUtil.addModelResources(article.getCompanyId(), article.getGroupId(), article.getUserId(), JournalArticle.class.getName(), article.getResourcePrimKey(), communityPermissions, guestPermissions);
    }

    public JournalArticle approveArticle(long userId, long groupId, String articleId, double version, String articleURL, PortletPreferences prefs) throws PortalException, SystemException {
        User user = UserUtil.findByPrimaryKey(userId);
        Date now = new Date();
        JournalArticle article = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        article.setModifiedDate(now);
        article.setApproved(true);
        article.setApprovedByUserId(user.getUserId());
        article.setApprovedByUserName(user.getFullName());
        article.setApprovedDate(now);
        JournalArticleUtil.update(article);
        sendEmail(article, articleURL, prefs, "granted");
        try {
            if (article.isIndexable()) {
                Indexer.updateArticle(article.getCompanyId(), article.getGroupId(), article.getArticleId(), article.getVersion(), article.getTitle(), article.getDescription(), article.getContent(), article.getType(), article.getDisplayDate());
            }
        } catch (IOException ioe) {
            _log.error("Indexing " + article.getId(), ioe);
        }
        return article;
    }

    public JournalArticle checkArticleResourcePrimKey(long groupId, String articleId, double version) throws PortalException, SystemException {
        JournalArticle article = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        if (article.getResourcePrimKey() > 0) {
            return article;
        }
        long resourcePrimKey = JournalArticleResourceLocalServiceUtil.getArticleResourcePrimKey(groupId, articleId);
        article.setResourcePrimKey(resourcePrimKey);
        JournalArticleUtil.update(article);
        return article;
    }

    public void checkArticles() throws PortalException, SystemException {
        Date now = new Date();
        List articles = JournalArticleFinder.findByExpirationDate(Boolean.FALSE, now, new Date(now.getTime() - CheckArticleJob.INTERVAL));
        if (_log.isDebugEnabled()) {
            _log.debug("Expiring " + articles.size() + " articles");
        }
        Set companies = new HashSet();
        for (int i = 0; i < articles.size(); i++) {
            JournalArticle article = (JournalArticle) articles.get(i);
            article.setApproved(false);
            article.setExpired(true);
            JournalArticleUtil.update(article);
            try {
                if (article.isIndexable()) {
                    Indexer.deleteArticle(article.getCompanyId(), article.getArticleId());
                }
            } catch (IOException ioe) {
                _log.error("Removing index " + article.getId(), ioe);
            }
            JournalContentUtil.clearCache(article.getGroupId(), article.getArticleId(), article.getTemplateId());
            companies.add(new Long(article.getCompanyId()));
        }
        Iterator itr = companies.iterator();
        while (itr.hasNext()) {
            long companyId = ((Long) itr.next()).longValue();
            LayoutCacheUtil.clearCache(companyId);
        }
        articles = JournalArticleFinder.findByReviewDate(now, new Date(now.getTime() - CheckArticleJob.INTERVAL));
        if (_log.isDebugEnabled()) {
            _log.debug("Sending review notifications for " + articles.size() + " articles");
        }
        for (int i = 0; i < articles.size(); i++) {
            JournalArticle article = (JournalArticle) articles.get(i);
            Date reviewDate = article.getReviewDate();
            if (reviewDate != null) {
                long diff = reviewDate.getTime() - now.getTime();
                if ((diff > 0) && (diff < CheckArticleJob.INTERVAL)) {
                    String articleURL = StringPool.BLANK;
                    long ownerId = article.getGroupId();
                    int ownerType = PortletKeys.PREFS_OWNER_TYPE_GROUP;
                    long plid = PortletKeys.PREFS_PLID_SHARED;
                    String portletId = PortletKeys.JOURNAL;
                    PortletPreferences prefs = PortletPreferencesLocalServiceUtil.getPreferences(article.getCompanyId(), ownerId, ownerType, plid, portletId);
                    sendEmail(article, articleURL, prefs, "review");
                }
            }
        }
    }

    public void checkNewLine(long groupId, String articleId, double version) throws PortalException, SystemException {
        JournalArticle article = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        String content = article.getContent();
        if ((content != null) && (content.indexOf("\\n") != -1)) {
            content = StringUtil.replace(content, new String[] { "\\n", "\\r" }, new String[] { "\n", "\r" });
            article.setContent(content);
            JournalArticleUtil.update(article);
        }
    }

    public void deleteArticle(long groupId, String articleId, double version, String articleURL, PortletPreferences prefs) throws PortalException, SystemException {
        JournalArticle article = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        deleteArticle(article, articleURL, prefs);
    }

    public void deleteArticle(JournalArticle article, String articleURL, PortletPreferences prefs) throws PortalException, SystemException {
        try {
            if (article.isApproved() && article.isIndexable()) {
                Indexer.deleteArticle(article.getCompanyId(), article.getArticleId());
            }
        } catch (IOException ioe) {
            _log.error("Deleting index " + article.getPrimaryKey(), ioe);
        }
        if ((prefs != null) && !article.isApproved() && isLatestVersion(article.getGroupId(), article.getArticleId(), article.getVersion())) {
            sendEmail(article, articleURL, prefs, "denied");
        }
        TagsAssetLocalServiceUtil.deleteAsset(JournalArticle.class.getName(), article.getResourcePrimKey());
        RatingsStatsLocalServiceUtil.deleteStats(JournalArticle.class.getName(), article.getResourcePrimKey());
        MBMessageLocalServiceUtil.deleteDiscussionMessages(JournalArticle.class.getName(), article.getResourcePrimKey());
        JournalContentSearchLocalServiceUtil.deleteArticleContentSearches(article.getGroupId(), article.getArticleId());
        JournalArticleImageLocalServiceUtil.deleteImages(article.getGroupId(), article.getArticleId(), article.getVersion());
        if (JournalArticleUtil.countByG_A(article.getGroupId(), article.getArticleId()) == 1) {
            ResourceLocalServiceUtil.deleteResource(article.getCompanyId(), JournalArticle.class.getName(), ResourceImpl.SCOPE_INDIVIDUAL, article.getResourcePrimKey());
        }
        if (JournalArticleUtil.countByG_A(article.getGroupId(), article.getArticleId()) == 1) {
            try {
                JournalArticleResourceLocalServiceUtil.deleteArticleResource(article.getGroupId(), article.getArticleId());
            } catch (NoSuchArticleResourceException nsare) {
            }
        }
        JournalArticleUtil.remove(article.getPrimaryKey());
    }

    public void deleteArticles(long groupId) throws PortalException, SystemException {
        Iterator itr = JournalArticleUtil.findByGroupId(groupId).iterator();
        while (itr.hasNext()) {
            JournalArticle article = (JournalArticle) itr.next();
            deleteArticle(article, null, null);
        }
    }

    public void expireArticle(long groupId, String articleId, double version, String articleURL, PortletPreferences prefs) throws PortalException, SystemException {
        JournalArticle article = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        expireArticle(article, articleURL, prefs);
    }

    public void expireArticle(JournalArticle article, String articleURL, PortletPreferences prefs) throws PortalException, SystemException {
        if ((prefs != null) && !article.isApproved() && isLatestVersion(article.getGroupId(), article.getArticleId(), article.getVersion())) {
            sendEmail(article, articleURL, prefs, "denied");
        }
        article.setExpirationDate(new Date());
        article.setApproved(false);
        article.setExpired(true);
        JournalArticleUtil.update(article);
        try {
            if (article.isIndexable()) {
                Indexer.deleteArticle(article.getCompanyId(), article.getArticleId());
            }
        } catch (IOException ioe) {
            _log.error("Removing index " + article.getId(), ioe);
        }
    }

    public JournalArticle getArticle(long id) throws PortalException, SystemException {
        return JournalArticleUtil.findByPrimaryKey(id);
    }

    public JournalArticle getArticle(long groupId, String articleId) throws PortalException, SystemException {
        try {
            return getLatestArticle(groupId, articleId, Boolean.TRUE);
        } catch (NoSuchArticleException nsae) {
            return getLatestArticle(groupId, articleId, Boolean.FALSE);
        }
    }

    public JournalArticle getArticle(long groupId, String articleId, double version) throws PortalException, SystemException {
        return JournalArticleUtil.findByG_A_V(groupId, articleId, version);
    }

    public String getArticleContent(long groupId, String articleId, String languageId, ThemeDisplay themeDisplay) throws PortalException, SystemException {
        return getArticleContent(groupId, articleId, null, languageId, themeDisplay);
    }

    public String getArticleContent(long groupId, String articleId, String templateId, String languageId, ThemeDisplay themeDisplay) throws PortalException, SystemException {
        JournalArticleDisplay articleDisplay = getArticleDisplay(groupId, articleId, templateId, languageId, themeDisplay);
        return articleDisplay.getContent();
    }

    public String getArticleContent(long groupId, String articleId, double version, String languageId, ThemeDisplay themeDisplay) throws PortalException, SystemException {
        return getArticleContent(groupId, articleId, version, null, languageId, themeDisplay);
    }

    public String getArticleContent(long groupId, String articleId, double version, String templateId, String languageId, ThemeDisplay themeDisplay) throws PortalException, SystemException {
        JournalArticleDisplay articleDisplay = getArticleDisplay(groupId, articleId, version, templateId, languageId, themeDisplay);
        if (articleDisplay == null) {
            return StringPool.BLANK;
        } else {
            return articleDisplay.getContent();
        }
    }

    public JournalArticleDisplay getArticleDisplay(long groupId, String articleId, String languageId, ThemeDisplay themeDisplay) throws PortalException, SystemException {
        return getArticleDisplay(groupId, articleId, null, languageId, themeDisplay);
    }

    public JournalArticleDisplay getArticleDisplay(long groupId, String articleId, String languageId, int page, ThemeDisplay themeDisplay, String xmlRequest) throws PortalException, SystemException {
        return getArticleDisplay(groupId, articleId, null, languageId, page, themeDisplay, xmlRequest);
    }

    public JournalArticleDisplay getArticleDisplay(long groupId, String articleId, String templateId, String languageId, ThemeDisplay themeDisplay) throws PortalException, SystemException {
        JournalArticle article = getDisplayArticle(groupId, articleId);
        return getArticleDisplay(groupId, articleId, article.getVersion(), templateId, languageId, themeDisplay);
    }

    public JournalArticleDisplay getArticleDisplay(long groupId, String articleId, String templateId, String languageId, int page, ThemeDisplay themeDisplay, String xmlRequest) throws PortalException, SystemException {
        JournalArticle article = getDisplayArticle(groupId, articleId);
        return getArticleDisplay(groupId, articleId, article.getVersion(), templateId, languageId, page, themeDisplay, xmlRequest);
    }

    public JournalArticleDisplay getArticleDisplay(long groupId, String articleId, double version, String templateId, String languageId, ThemeDisplay themeDisplay) throws PortalException, SystemException {
        return getArticleDisplay(groupId, articleId, version, templateId, languageId, 1, themeDisplay, null);
    }

    public JournalArticleDisplay getArticleDisplay(long groupId, String articleId, double version, String templateId, String languageId, int page, ThemeDisplay themeDisplay, String xmlRequest) throws PortalException, SystemException {
        String content = null;
        Date now = new Date();
        JournalArticle article = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        if (article.isExpired()) {
            Date expirationDate = article.getExpirationDate();
            if ((expirationDate != null) && expirationDate.before(now)) {
                return null;
            }
        }
        if (article.getDisplayDate().after(now)) {
            return null;
        }
        if (page < 1) {
            page = 1;
        }
        int numberOfPages = 1;
        boolean paginate = false;
        boolean pageFlow = false;
        Map tokens = JournalUtil.getTokens(groupId, themeDisplay);
        String xml = article.getContent();
        try {
            Document doc = null;
            Element root = null;
            if (article.isTemplateDriven()) {
                SAXReader reader = new SAXReader();
                doc = reader.read(new StringReader(xml));
                root = doc.getRootElement();
                Document request = null;
                if (Validator.isNotNull(xmlRequest)) {
                    request = reader.read(new StringReader(xmlRequest));
                }
                List pages = root.elements("page");
                if (pages.size() > 0) {
                    pageFlow = true;
                    String targetPage = request.valueOf("/request/parameters/parameter[name='targetPage']/" + "value");
                    Element pageEl = null;
                    if (Validator.isNotNull(targetPage)) {
                        XPath xpathSelector = DocumentHelper.createXPath("/root/page[@id = '" + targetPage + "']");
                        pageEl = (Element) xpathSelector.selectSingleNode(doc);
                    }
                    DocumentFactory docFactory = DocumentFactory.getInstance();
                    if (pageEl != null) {
                        doc = docFactory.createDocument(pageEl);
                        root = doc.getRootElement();
                        numberOfPages = pages.size();
                    } else {
                        if (page > pages.size()) {
                            page = 1;
                        }
                        pageEl = (Element) pages.get(page - 1);
                        doc = docFactory.createDocument(pageEl);
                        root = doc.getRootElement();
                        numberOfPages = pages.size();
                        paginate = true;
                    }
                }
                if (request != null) {
                    root.add(request.getRootElement().createCopy());
                }
                JournalUtil.addAllReservedEls(root, tokens, article);
                xml = JournalUtil.formatXML(doc);
            }
        } catch (DocumentException de) {
            throw new SystemException(de);
        } catch (IOException ioe) {
            throw new SystemException(ioe);
        }
        try {
            if (_log.isDebugEnabled()) {
                _log.debug("Transforming " + articleId + " " + version + " " + languageId);
            }
            String script = null;
            String langType = null;
            if (article.isTemplateDriven()) {
                String defaultTemplateId = article.getTemplateId();
                if (Validator.isNull(templateId)) {
                    templateId = defaultTemplateId;
                }
                JournalTemplate template = null;
                try {
                    template = JournalTemplateUtil.findByG_T(groupId, templateId);
                } catch (NoSuchTemplateException nste) {
                    if (!defaultTemplateId.equals(templateId)) {
                        template = JournalTemplateUtil.findByG_T(groupId, defaultTemplateId);
                    } else {
                        throw nste;
                    }
                }
                script = template.getXsl();
                langType = template.getLangType();
            }
            content = JournalUtil.transform(tokens, languageId, xml, script, langType);
            if (!pageFlow) {
                String[] pieces = StringUtil.split(content, _TOKEN_PAGE_BREAK);
                if (pieces.length > 1) {
                    if (page > pieces.length) {
                        page = 1;
                    }
                    content = pieces[page - 1];
                    numberOfPages = pieces.length;
                    paginate = true;
                }
            }
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return new JournalArticleDisplayImpl(article.getId(), article.getResourcePrimKey(), article.getGroupId(), article.getUserId(), article.getArticleId(), article.getVersion(), article.getTitle(), article.getDescription(), article.getAvailableLocales(), content, article.getType(), article.getStructureId(), templateId, numberOfPages, page, paginate);
    }

    public List getArticles() throws SystemException {
        return JournalArticleUtil.findAll();
    }

    public List getArticles(long groupId) throws SystemException {
        return JournalArticleUtil.findByGroupId(groupId);
    }

    public List getArticles(long groupId, int begin, int end) throws SystemException {
        return JournalArticleUtil.findByGroupId(groupId, begin, end);
    }

    public List getArticles(long groupId, int begin, int end, OrderByComparator obc) throws SystemException {
        return JournalArticleUtil.findByGroupId(groupId, begin, end, obc);
    }

    public int getArticlesCount(long groupId) throws SystemException {
        return JournalArticleUtil.countByGroupId(groupId);
    }

    public JournalArticle getDisplayArticle(long groupId, String articleId) throws PortalException, SystemException {
        List articles = JournalArticleUtil.findByG_A_A(groupId, articleId, true);
        if (articles.size() == 0) {
            throw new NoSuchArticleException();
        }
        Collections.sort(articles, new ArticleDisplayDateComparator());
        Date now = new Date();
        for (int i = 0; i < articles.size(); i++) {
            JournalArticle article = (JournalArticle) articles.get(i);
            if (article.getDisplayDate().before(now)) {
                return article;
            }
        }
        return (JournalArticle) articles.get(0);
    }

    public JournalArticle getLatestArticle(long groupId, String articleId) throws PortalException, SystemException {
        return getLatestArticle(groupId, articleId, null);
    }

    public JournalArticle getLatestArticle(long groupId, String articleId, Boolean approved) throws PortalException, SystemException {
        List articles = null;
        if (approved == null) {
            articles = JournalArticleUtil.findByG_A(groupId, articleId, 0, 1);
        } else {
            articles = JournalArticleUtil.findByG_A_A(groupId, articleId, approved.booleanValue(), 0, 1);
        }
        if (articles.size() == 0) {
            throw new NoSuchArticleException();
        }
        return (JournalArticle) articles.get(0);
    }

    public double getLatestVersion(long groupId, String articleId) throws PortalException, SystemException {
        JournalArticle article = getLatestArticle(groupId, articleId);
        return article.getVersion();
    }

    public double getLatestVersion(long groupId, String articleId, Boolean approved) throws PortalException, SystemException {
        JournalArticle article = getLatestArticle(groupId, articleId, approved);
        return article.getVersion();
    }

    public List getStructureArticles(long groupId, String structureId) throws SystemException {
        return JournalArticleUtil.findByG_S(groupId, structureId);
    }

    public List getStructureArticles(long groupId, String structureId, int begin, int end, OrderByComparator obc) throws SystemException {
        return JournalArticleUtil.findByG_S(groupId, structureId, begin, end, obc);
    }

    public int getStructureArticlesCount(long groupId, String structureId) throws SystemException {
        return JournalArticleUtil.countByG_S(groupId, structureId);
    }

    public List getTemplateArticles(long groupId, String templateId) throws SystemException {
        return JournalArticleUtil.findByG_T(groupId, templateId);
    }

    public List getTemplateArticles(long groupId, String templateId, int begin, int end, OrderByComparator obc) throws SystemException {
        return JournalArticleUtil.findByG_T(groupId, templateId, begin, end, obc);
    }

    public int getTemplateArticlesCount(long groupId, String templateId) throws SystemException {
        return JournalArticleUtil.countByG_T(groupId, templateId);
    }

    public boolean hasArticle(long groupId, String articleId) throws SystemException {
        try {
            getArticle(groupId, articleId);
            return true;
        } catch (PortalException pe) {
            return false;
        }
    }

    public boolean isLatestVersion(long groupId, String articleId, double version) throws PortalException, SystemException {
        if (getLatestVersion(groupId, articleId) == version) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLatestVersion(long groupId, String articleId, double version, Boolean active) throws PortalException, SystemException {
        if (getLatestVersion(groupId, articleId, active) == version) {
            return true;
        } else {
            return false;
        }
    }

    public void reIndex(String[] ids) throws SystemException {
        long companyId = GetterUtil.getLong(ids[0]);
        IndexWriter writer = null;
        try {
            writer = LuceneUtil.getWriter(companyId);
            Iterator itr = JournalArticleUtil.findByCompanyId(companyId).iterator();
            while (itr.hasNext()) {
                JournalArticle article = (JournalArticle) itr.next();
                long groupId = article.getGroupId();
                String articleId = article.getArticleId();
                double version = article.getVersion();
                String title = article.getTitle();
                String description = article.getDescription();
                String content = article.getContent();
                String type = article.getType();
                Date displayDate = article.getDisplayDate();
                if (article.isApproved() && article.isIndexable()) {
                    try {
                        org.apache.lucene.document.Document doc = Indexer.getAddArticleDocument(companyId, groupId, articleId, version, title, description, content, type, displayDate);
                        writer.addDocument(doc);
                    } catch (Exception e1) {
                        _log.error("Reindexing " + article.getId(), e1);
                    }
                }
            }
        } catch (SystemException se) {
            throw se;
        } catch (Exception e2) {
            throw new SystemException(e2);
        } finally {
            try {
                if (writer != null) {
                    LuceneUtil.write(companyId);
                }
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }

    public JournalArticle removeArticleLocale(long groupId, String articleId, double version, String languageId) throws PortalException, SystemException {
        JournalArticle article = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        String content = article.getContent();
        if (article.isTemplateDriven()) {
            content = JournalUtil.removeArticleLocale(content, languageId);
        } else {
            content = LocalizationUtil.removeLocalization(content, "static-content", languageId);
        }
        article.setContent(content);
        JournalArticleUtil.update(article);
        return article;
    }

    public Hits search(long companyId, long groupId, String title, String description, String content, String type) throws SystemException {
        return search(companyId, groupId, title, description, content, type, "displayDate");
    }

    public Hits search(long companyId, long groupId, String title, String description, String content, String type, String sortField) throws SystemException {
        Searcher searcher = null;
        try {
            HitsImpl hits = new HitsImpl();
            if ((Validator.isNull(title)) && (Validator.isNull(content)) && (Validator.isNull(type))) {
                return hits;
            }
            BooleanQuery contextQuery = new BooleanQuery();
            LuceneUtil.addRequiredTerm(contextQuery, LuceneFields.PORTLET_ID, Indexer.PORTLET_ID);
            if (groupId > 0) {
                LuceneUtil.addRequiredTerm(contextQuery, LuceneFields.GROUP_ID, groupId);
            }
            BooleanQuery searchQuery = new BooleanQuery();
            LuceneUtil.addTerm(searchQuery, LuceneFields.TITLE, title);
            LuceneUtil.addTerm(searchQuery, LuceneFields.CONTENT, content);
            LuceneUtil.addTerm(searchQuery, LuceneFields.DESCRIPTION, description);
            if (Validator.isNotNull(type)) {
                LuceneUtil.addRequiredTerm(searchQuery, "type", type);
            }
            BooleanQuery fullQuery = new BooleanQuery();
            fullQuery.add(contextQuery, BooleanClause.Occur.MUST);
            fullQuery.add(searchQuery, BooleanClause.Occur.MUST);
            searcher = LuceneUtil.getSearcher(companyId);
            Sort sort = new Sort(new SortField(sortField, true));
            try {
                hits.recordHits(searcher.search(fullQuery, sort), searcher);
            } catch (RuntimeException re) {
                String msg = GetterUtil.getString(re.getMessage());
                if (!msg.endsWith("does not appear to be indexed")) {
                    throw re;
                }
            }
            return hits;
        } catch (Exception e) {
            StringMaker sm = new StringMaker();
            sm.append(title);
            sm.append(StringPool.SPACE);
            sm.append(content);
            sm.append(StringPool.SPACE);
            sm.append(description);
            String keywords = sm.toString();
            return LuceneUtil.closeSearcher(searcher, keywords, e);
        }
    }

    public List search(long companyId, long groupId, String keywords, Double version, String type, String structureId, String templateId, Date displayDateGT, Date displayDateLT, Boolean approved, Boolean expired, Date reviewDate, int begin, int end, OrderByComparator obc) throws SystemException {
        return JournalArticleFinder.findByKeywords(companyId, groupId, keywords, version, type, structureId, templateId, displayDateGT, displayDateLT, approved, expired, reviewDate, begin, end, obc);
    }

    public List search(long companyId, long groupId, String articleId, Double version, String title, String description, String content, String type, String structureId, String templateId, Date displayDateGT, Date displayDateLT, Boolean approved, Boolean expired, Date reviewDate, boolean andOperator, int begin, int end, OrderByComparator obc) throws SystemException {
        return JournalArticleFinder.findByC_G_A_V_T_D_C_T_S_T_D_A_E_R(companyId, groupId, articleId, version, title, description, content, type, structureId, templateId, displayDateGT, displayDateLT, approved, expired, reviewDate, andOperator, begin, end, obc);
    }

    public List search(long companyId, long groupId, String articleId, Double version, String title, String description, String content, String type, String[] structureIds, String[] templateIds, Date displayDateGT, Date displayDateLT, Boolean approved, Boolean expired, Date reviewDate, boolean andOperator, int begin, int end, OrderByComparator obc) throws SystemException {
        return JournalArticleFinder.findByC_G_A_V_T_D_C_T_S_T_D_A_E_R(companyId, groupId, articleId, version, title, description, content, type, structureIds, templateIds, displayDateGT, displayDateLT, approved, expired, reviewDate, andOperator, begin, end, obc);
    }

    public int searchCount(long companyId, long groupId, String keywords, Double version, String type, String structureId, String templateId, Date displayDateGT, Date displayDateLT, Boolean approved, Boolean expired, Date reviewDate) throws SystemException {
        return JournalArticleFinder.countByKeywords(companyId, groupId, keywords, version, type, structureId, templateId, displayDateGT, displayDateLT, approved, expired, reviewDate);
    }

    public int searchCount(long companyId, long groupId, String articleId, Double version, String title, String description, String content, String type, String structureId, String templateId, Date displayDateGT, Date displayDateLT, Boolean approved, Boolean expired, Date reviewDate, boolean andOperator) throws SystemException {
        return JournalArticleFinder.countByC_G_A_V_T_D_C_T_S_T_D_A_E_R(companyId, groupId, articleId, version, title, description, content, type, structureId, templateId, displayDateGT, displayDateLT, approved, expired, reviewDate, andOperator);
    }

    public int searchCount(long companyId, long groupId, String articleId, Double version, String title, String description, String content, String type, String[] structureIds, String[] templateIds, Date displayDateGT, Date displayDateLT, Boolean approved, Boolean expired, Date reviewDate, boolean andOperator) throws SystemException {
        return JournalArticleFinder.countByC_G_A_V_T_D_C_T_S_T_D_A_E_R(companyId, groupId, articleId, version, title, description, content, type, structureIds, templateIds, displayDateGT, displayDateLT, approved, expired, reviewDate, andOperator);
    }

    public JournalArticle updateArticle(long userId, long groupId, String articleId, double version, boolean incrementVersion, String title, String description, String content, String type, String structureId, String templateId, int displayDateMonth, int displayDateDay, int displayDateYear, int displayDateHour, int displayDateMinute, int expirationDateMonth, int expirationDateDay, int expirationDateYear, int expirationDateHour, int expirationDateMinute, boolean neverExpire, int reviewDateMonth, int reviewDateDay, int reviewDateYear, int reviewDateHour, int reviewDateMinute, boolean neverReview, boolean indexable, Map images, String articleURL, PortletPreferences prefs, String[] tagsEntries) throws PortalException, SystemException {
        User user = UserUtil.findByPrimaryKey(userId);
        articleId = articleId.trim().toUpperCase();
        Date now = new Date();
        Date displayDate = PortalUtil.getDate(displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, user.getTimeZone(), new ArticleDisplayDateException());
        Date expirationDate = null;
        if (!neverExpire) {
            expirationDate = PortalUtil.getDate(expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, user.getTimeZone(), new ArticleExpirationDateException());
        }
        Date reviewDate = null;
        if (!neverReview) {
            reviewDate = PortalUtil.getDate(reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, user.getTimeZone(), new ArticleReviewDateException());
        }
        validate(groupId, title, content, type, structureId, templateId);
        JournalArticle oldArticle = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        JournalArticle article = null;
        if (incrementVersion) {
            double latestVersion = getLatestVersion(groupId, articleId);
            long id = CounterLocalServiceUtil.increment();
            article = JournalArticleUtil.create(id);
            article.setResourcePrimKey(oldArticle.getResourcePrimKey());
            article.setGroupId(oldArticle.getGroupId());
            article.setCompanyId(user.getCompanyId());
            article.setUserId(user.getUserId());
            article.setUserName(user.getFullName());
            article.setCreateDate(now);
            article.setArticleId(articleId);
            article.setVersion(MathUtil.format(latestVersion + 0.1, 1, 1));
        } else {
            article = oldArticle;
        }
        content = format(groupId, articleId, article.getVersion(), incrementVersion, content, structureId, images);
        boolean approved = oldArticle.isApproved();
        if (incrementVersion) {
            approved = false;
        }
        article.setModifiedDate(now);
        article.setTitle(title);
        article.setDescription(description);
        article.setContent(content);
        article.setType(type);
        article.setStructureId(structureId);
        article.setTemplateId(templateId);
        article.setDisplayDate(displayDate);
        article.setApproved(approved);
        if ((expirationDate == null) || expirationDate.after(now)) {
            article.setExpired(false);
        } else {
            article.setExpired(true);
        }
        article.setExpirationDate(expirationDate);
        article.setReviewDate(reviewDate);
        article.setIndexable(indexable);
        JournalArticleUtil.update(article);
        updateTagsAsset(userId, article, tagsEntries);
        if (incrementVersion) {
            sendEmail(article, articleURL, prefs, "requested");
        }
        try {
            if (article.isIndexable()) {
                if (article.isApproved()) {
                    Indexer.updateArticle(article.getCompanyId(), article.getGroupId(), article.getArticleId(), article.getVersion(), article.getTitle(), article.getDescription(), article.getContent(), article.getType(), article.getDisplayDate());
                } else {
                    Indexer.deleteArticle(article.getCompanyId(), article.getArticleId());
                }
            }
        } catch (IOException ioe) {
            _log.error("Indexing " + article.getPrimaryKey(), ioe);
        }
        return article;
    }

    public JournalArticle updateContent(long groupId, String articleId, double version, String content) throws PortalException, SystemException {
        JournalArticle article = JournalArticleUtil.findByG_A_V(groupId, articleId, version);
        article.setContent(content);
        JournalArticleUtil.update(article);
        return article;
    }

    public void updateTagsAsset(long userId, JournalArticle article, String[] tagsEntries) throws PortalException, SystemException {
        boolean updateAsset = true;
        if (article.getVersion() == 1) {
        } else if (!article.isApproved()) {
            updateAsset = false;
        } else if (!isLatestVersion(article.getGroupId(), article.getArticleId(), article.getVersion(), Boolean.TRUE)) {
            updateAsset = false;
        }
        if (updateAsset) {
            TagsAssetLocalServiceUtil.updateAsset(userId, JournalArticle.class.getName(), article.getResourcePrimKey(), tagsEntries, null, null, article.getDisplayDate(), article.getExpirationDate(), ContentTypes.TEXT_HTML, article.getTitle(), article.getDescription(), article.getDescription(), null, 0, 0);
        }
    }

    protected String format(long groupId, String articleId, double version, boolean incrementVersion, String content, String structureId, Map images) throws SystemException {
        if (Validator.isNotNull(structureId)) {
            SAXReader reader = new SAXReader();
            Document doc = null;
            try {
                doc = reader.read(new StringReader(content));
                Element root = doc.getRootElement();
                format(groupId, articleId, version, incrementVersion, root, images);
                content = JournalUtil.formatXML(doc);
            } catch (DocumentException de) {
                _log.error(de);
            } catch (IOException ioe) {
                _log.error(ioe);
            }
        }
        return content;
    }

    protected void format(long groupId, String articleId, double version, boolean incrementVersion, Element root, Map images) throws SystemException {
        Iterator itr = root.elements().iterator();
        while (itr.hasNext()) {
            Element el = (Element) itr.next();
            String elName = el.attributeValue("name", StringPool.BLANK);
            String elType = el.attributeValue("type", StringPool.BLANK);
            if (elType.equals("image")) {
                formatImage(groupId, articleId, version, incrementVersion, el, elName, images);
            } else if (elType.equals("text_area")) {
                Element dynamicContent = el.element("dynamic-content");
                String text = dynamicContent.getText();
                try {
                    text = ParserUtils.trimTags(text, new String[] { "script" }, false, true);
                } catch (ParserException pe) {
                    text = pe.getLocalizedMessage();
                } catch (UnsupportedEncodingException uee) {
                    text = uee.getLocalizedMessage();
                }
                dynamicContent.setText(text);
            }
            format(groupId, articleId, version, incrementVersion, el, images);
        }
    }

    protected void formatImage(long groupId, String articleId, double version, boolean incrementVersion, Element el, String elName, Map images) throws SystemException {
        List imageContents = el.elements("dynamic-content");
        Iterator itr = imageContents.listIterator();
        while (itr.hasNext()) {
            Element dynamicContent = (Element) itr.next();
            String elLanguage = dynamicContent.attributeValue("language-id", StringPool.BLANK);
            if (!elLanguage.equals(StringPool.BLANK)) {
                elLanguage = "_" + elLanguage;
            }
            long imageId = JournalArticleImageLocalServiceUtil.getArticleImageId(groupId, articleId, version, elName, elLanguage);
            double oldVersion = MathUtil.format(version - 0.1, 1, 1);
            long oldImageId = 0;
            if (oldVersion >= 1) {
                oldImageId = JournalArticleImageLocalServiceUtil.getArticleImageId(groupId, articleId, oldVersion, elName, elLanguage);
            }
            String elContent = "/image/journal/article?img_id=" + imageId + "&t=" + ImageServletTokenUtil.getToken(imageId);
            if (dynamicContent.getText().equals("delete")) {
                dynamicContent.setText(StringPool.BLANK);
                ImageLocalUtil.deleteImage(imageId);
                String defaultElLanguage = "";
                if (!Validator.isNotNull(elLanguage)) {
                    defaultElLanguage = "_" + LocaleUtil.toLanguageId(LocaleUtil.getDefault());
                }
                long defaultImageId = JournalArticleImageLocalServiceUtil.getArticleImageId(groupId, articleId, version, elName, defaultElLanguage);
                ImageLocalUtil.deleteImage(defaultImageId);
                continue;
            }
            byte[] bytes = (byte[]) images.get(elName + elLanguage);
            if (bytes != null && (bytes.length > 0)) {
                dynamicContent.setText(elContent);
                dynamicContent.addAttribute("id", String.valueOf(imageId));
                ImageLocalUtil.updateImage(imageId, bytes);
                continue;
            }
            if ((version > JournalArticleImpl.DEFAULT_VERSION) && (incrementVersion)) {
                Image oldImage = null;
                if (oldImageId > 0) {
                    oldImage = ImageLocalUtil.getImage(oldImageId);
                }
                if (oldImage != null) {
                    dynamicContent.setText(elContent);
                    dynamicContent.addAttribute("id", String.valueOf(imageId));
                    bytes = oldImage.getTextObj();
                    ImageLocalUtil.updateImage(imageId, bytes);
                }
                continue;
            }
            Image image = ImageLocalUtil.getImage(imageId);
            if (image != null) {
                dynamicContent.setText(elContent);
                dynamicContent.addAttribute("id", String.valueOf(imageId));
                continue;
            }
            String defaultElLanguage = "";
            if (!Validator.isNotNull(elLanguage)) {
                defaultElLanguage = "_" + LocaleUtil.toLanguageId(LocaleUtil.getDefault());
            }
            long defaultImageId = JournalArticleImageLocalServiceUtil.getArticleImageId(groupId, articleId, version, elName, defaultElLanguage);
            Image defaultImage = ImageLocalUtil.getImage(defaultImageId);
            if (defaultImage != null) {
                dynamicContent.setText(elContent);
                dynamicContent.addAttribute("id", String.valueOf(imageId));
                bytes = defaultImage.getTextObj();
                ImageLocalUtil.updateImage(imageId, bytes);
                continue;
            }
            dynamicContent.setText(StringPool.BLANK);
        }
    }

    protected void sendEmail(JournalArticle article, String articleURL, PortletPreferences prefs, String emailType) throws PortalException, SystemException {
        try {
            if (emailType.equals("denied") && JournalUtil.getEmailArticleApprovalDeniedEnabled(prefs)) {
            } else if (emailType.equals("granted") && JournalUtil.getEmailArticleApprovalGrantedEnabled(prefs)) {
            } else if (emailType.equals("requested") && JournalUtil.getEmailArticleApprovalRequestedEnabled(prefs)) {
            } else if (emailType.equals("review") && JournalUtil.getEmailArticleReviewEnabled(prefs)) {
            } else {
                return;
            }
            Company company = CompanyUtil.findByPrimaryKey(article.getCompanyId());
            User user = UserUtil.findByPrimaryKey(article.getUserId());
            articleURL += "&groupId=" + article.getGroupId() + "&articleId=" + article.getArticleId() + "&version=" + article.getVersion();
            String portletName = PortalUtil.getPortletTitle(PortletKeys.JOURNAL, user);
            String fromName = JournalUtil.getEmailFromName(prefs);
            String fromAddress = JournalUtil.getEmailFromAddress(prefs);
            String toName = user.getFullName();
            String toAddress = user.getEmailAddress();
            if (emailType.equals("requested") || emailType.equals("review")) {
                String tempToName = fromName;
                String tempToAddress = fromAddress;
                fromName = toName;
                fromAddress = toAddress;
                toName = tempToName;
                toAddress = tempToAddress;
            }
            String subject = null;
            String body = null;
            if (emailType.equals("denied")) {
                subject = JournalUtil.getEmailArticleApprovalDeniedSubject(prefs);
                body = JournalUtil.getEmailArticleApprovalDeniedBody(prefs);
            } else if (emailType.equals("granted")) {
                subject = JournalUtil.getEmailArticleApprovalGrantedSubject(prefs);
                body = JournalUtil.getEmailArticleApprovalGrantedBody(prefs);
            } else if (emailType.equals("requested")) {
                subject = JournalUtil.getEmailArticleApprovalRequestedSubject(prefs);
                body = JournalUtil.getEmailArticleApprovalRequestedBody(prefs);
            } else if (emailType.equals("review")) {
                subject = JournalUtil.getEmailArticleReviewSubject(prefs);
                body = JournalUtil.getEmailArticleReviewBody(prefs);
            }
            subject = StringUtil.replace(subject, new String[] { "[$ARTICLE_ID$]", "[$ARTICLE_TITLE$]", "[$ARTICLE_URL$]", "[$ARTICLE_VERSION$]", "[$FROM_ADDRESS$]", "[$FROM_NAME$]", "[$PORTAL_URL$]", "[$PORTLET_NAME$]", "[$TO_ADDRESS$]", "[$TO_NAME$]" }, new String[] { article.getArticleId(), article.getTitle(), articleURL, String.valueOf(article.getVersion()), fromAddress, fromName, company.getVirtualHost(), portletName, toAddress, toName });
            body = StringUtil.replace(body, new String[] { "[$ARTICLE_ID$]", "[$ARTICLE_TITLE$]", "[$ARTICLE_URL$]", "[$ARTICLE_VERSION$]", "[$FROM_ADDRESS$]", "[$FROM_NAME$]", "[$PORTAL_URL$]", "[$PORTLET_NAME$]", "[$TO_ADDRESS$]", "[$TO_NAME$]" }, new String[] { article.getArticleId(), article.getTitle(), articleURL, String.valueOf(article.getVersion()), fromAddress, fromName, company.getVirtualHost(), portletName, toAddress, toName });
            InternetAddress from = new InternetAddress(fromAddress, fromName);
            InternetAddress to = new InternetAddress(toAddress, toName);
            MailMessage message = new MailMessage(from, to, subject, body, true);
            MailServiceUtil.sendEmail(message);
        } catch (IOException ioe) {
            throw new SystemException(ioe);
        } catch (PortalException pe) {
            throw pe;
        }
    }

    protected void validate(long groupId, String articleId, boolean autoArticleId, String title, String content, String type, String structureId, String templateId) throws PortalException, SystemException {
        if (!autoArticleId) {
            if ((Validator.isNull(articleId)) || (Validator.isNumber(articleId)) || (articleId.indexOf(StringPool.SPACE) != -1)) {
                throw new ArticleIdException();
            }
            try {
                JournalArticleUtil.findByG_A_V(groupId, articleId, JournalArticleImpl.DEFAULT_VERSION);
                throw new DuplicateArticleIdException();
            } catch (NoSuchArticleException nste) {
            }
        }
        validate(groupId, title, content, type, structureId, templateId);
    }

    protected void validate(long groupId, String title, String content, String type, String structureId, String templateId) throws PortalException, SystemException {
        if (Validator.isNull(title)) {
            throw new ArticleTitleException();
        } else if (Validator.isNull(content)) {
            throw new ArticleContentException();
        } else if (Validator.isNull(type)) {
            throw new ArticleTypeException();
        }
        if (Validator.isNotNull(structureId)) {
            JournalStructureUtil.findByG_S(groupId, structureId);
            JournalTemplate template = JournalTemplateUtil.findByG_T(groupId, templateId);
            if (!template.getStructureId().equals(structureId)) {
                throw new NoSuchTemplateException();
            }
        }
    }

    private static final String _TOKEN_PAGE_BREAK = PropsUtil.get(PropsUtil.JOURNAL_ARTICLE_TOKEN_PAGE_BREAK);

    private static Log _log = LogFactory.getLog(JournalArticleLocalServiceImpl.class);
}
