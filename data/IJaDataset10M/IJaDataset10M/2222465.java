package it.ansf.liferay.notizie;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import com.liferay.portal.NoSuchPortletPreferencesException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.asset.NoSuchTagException;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;

/**
 * Portlet implementation class AnsfAnnouncementManage
 */
public class AnsfListaNotizie extends GenericPortlet {

    private List<JournalArticle> articles;

    private HashMap<String, String> webArticles;

    private String tag;

    private String previewParam;

    public void init() {
        viewJSP = getInitParameter("view-jsp");
        previewJSP = getInitParameter("preview-jsp");
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws IOException, PortletException {
        previewParam = request.getParameter("articleTitle");
        request.removeAttribute("articleTitle");
    }

    public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
        String locale = renderRequest.getLocale().toString();
        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
        if (previewParam == null || webArticles == null || articles == null) {
            webArticles = new LinkedHashMap<String, String>();
            try {
                PortletSession pSes = renderRequest.getPortletSession();
                String var = (String) pSes.getAttribute("ASCOPE", PortletSession.APPLICATION_SCOPE);
                PortletPreferences preferences = PortletPreferencesLocalServiceUtil.getPortletPreferences(010101);
                tag = preferences.getPreferences();
            } catch (NoSuchPortletPreferencesException e) {
                tag = "Notizie";
            } catch (PortalException e) {
                tag = "Notizie";
            } catch (SystemException e) {
                tag = "Notizie";
            }
            try {
                articles = findArticleByTag(themeDisplay, tag);
                ArrayList<Notizia> notizie = new ArrayList<Notizia>();
                for (JournalArticle article : articles) {
                    notizie.add(new Notizia(article, locale));
                }
                Collections.sort(notizie);
                Notizia notizia;
                for (int i = notizie.size() - 1; i >= 0; i--) {
                    notizia = notizie.get(i);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                    String date = formatter.format(notizia.getDisplayDate());
                    String title = date + "__" + notizia.getTitle() + "__" + notizia.getId();
                    String description = notizia.getDescription();
                    webArticles.put(title, description);
                }
            } catch (NoSuchTagException e) {
                e.printStackTrace();
            } catch (PortalException e) {
                e.printStackTrace();
            } catch (SystemException e) {
                e.printStackTrace();
            }
            renderRequest.setAttribute("articles", webArticles);
            include(viewJSP, renderRequest, renderResponse);
        } else {
            int i = 0;
            int indexParameter = Integer.parseInt(previewParam);
            for (String key : webArticles.keySet()) {
                if (i == indexParameter) {
                    renderRequest.setAttribute("title", key.split("__")[0] + " - " + key.split("__")[1]);
                    renderRequest.setAttribute("content", getContentArticleByid(articles, key.split("__")[2].trim(), themeDisplay, renderRequest));
                }
                i++;
            }
            previewParam = null;
            include(previewJSP, renderRequest, renderResponse);
        }
    }

    public String getContentArticleByid(List<JournalArticle> articles, String idString, ThemeDisplay themeDisplay, RenderRequest renderRequest) {
        String result = "";
        String locale = renderRequest.getLocale().toString();
        Long id = Long.parseLong(idString);
        for (JournalArticle article : articles) {
            if (article.getId() == id) {
                String languageId = LanguageUtil.getLanguageId(renderRequest);
                result = JournalContentUtil.getContent(themeDisplay.getPortletGroupId(), article.getArticleId(), article.getContent(), languageId, themeDisplay);
            }
        }
        return result;
    }

    public List<JournalArticle> findArticleByTag(ThemeDisplay themeDisplay, String tagName) throws SystemException, PortalException {
        AssetTag assetTagObj = AssetTagLocalServiceUtil.getTag(themeDisplay.getScopeGroupId(), tagName);
        long tagid = assetTagObj.getTagId();
        AssetEntryQuery assetEntryQuery = new AssetEntryQuery();
        long[] anyTagIds = { tagid };
        assetEntryQuery.setAnyTagIds(anyTagIds);
        List<AssetEntry> assetEntryList = AssetEntryLocalServiceUtil.getEntries(assetEntryQuery);
        List<JournalArticle> journalArticleList = new ArrayList<JournalArticle>();
        for (AssetEntry ae : assetEntryList) {
            JournalArticleResource journalArticleResourceObj = JournalArticleResourceLocalServiceUtil.getJournalArticleResource(ae.getClassPK());
            JournalArticle journalArticleObj = JournalArticleLocalServiceUtil.getArticle(themeDisplay.getScopeGroupId(), journalArticleResourceObj.getArticleId());
            journalArticleList.add(journalArticleObj);
        }
        return journalArticleList;
    }

    protected void include(String path, RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
        PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(path);
        if (portletRequestDispatcher == null) {
            _log.error(path + " is not a valid include");
        } else {
            portletRequestDispatcher.include(renderRequest, renderResponse);
        }
    }

    protected String viewJSP, previewJSP;

    private static Log _log = LogFactoryUtil.getLog(AnsfListaNotizie.class);
}
