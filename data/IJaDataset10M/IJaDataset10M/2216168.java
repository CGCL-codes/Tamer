package ch.articlefox.servlet.pages;

import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import ch.articlefox.SessionConstants;
import ch.articlefox.db.TArticles;
import ch.articlefox.db.TTemplates;
import ch.articlefox.db.TUsers;
import ch.articlefox.localization.Dictionaries;
import ch.articlefox.localization.Dictionary;
import ch.articlefox.servlet.elements.ArticleAddButton;
import ch.articlefox.servlet.elements.ArticlesPlanningTable;
import ch.articlefox.servlet.elements.DateSelector;
import ch.articlefox.servlet.elements.InitScript;
import ch.articlefox.servlet.elements.PageTitle;
import ch.articlefox.servlet.elements.Subtitle;
import ch.articlefox.servlet.elements.TemplateListTable;
import ch.articlefox.utils.DateUtils;
import com.orelias.infoaccess.InfoAccess;
import com.orelias.infoaccess.InfoBean;

/**
 * A page to plan articles grouped by date.
 * 
 * @author Lukas Blunschi
 */
public class PlanningByDatePage extends AbstractPage {

    public static final String PAGENAME = "planningByDate";

    public StringBuffer getContent(HttpServletRequest req, InfoAccess infoaccess) {
        StringBuffer html = new StringBuffer();
        Dictionary dict = Dictionaries.getDictionaryFromSession(req);
        Date dateSelected = DateSelector.getSelectedDate(req);
        new InitScript(null).appendHtml(html, dict);
        new PageTitle(dict.articles()).appendHtml(html, dict);
        Collection<InfoBean> coll = infoaccess.query(TArticles.TBL_NAME, null, null, true);
        new DateSelector(dateSelected, coll, PAGENAME).appendHtml(html, dict);
        String filter = TArticles.F_DATE + "='" + DateUtils.dateFormatterISO8601.format(dateSelected) + "'";
        coll = infoaccess.query(TArticles.TBL_NAME, filter, null, true);
        new ArticlesPlanningTable(coll).appendHtml(html, dict);
        Integer userId = (Integer) req.getSession().getAttribute(SessionConstants.A_USERID);
        InfoBean user = infoaccess.get(TUsers.TBL_NAME, userId, true);
        boolean isAdmin = (Boolean) user.getProperty(TUsers.F_ISADMIN);
        boolean isEditor = (Boolean) user.getProperty(TUsers.F_ISEDITOR);
        if (isAdmin || isEditor) {
            new ArticleAddButton().appendHtml(html, dict);
        }
        new Subtitle(dict.templates()).appendHtml(html, dict);
        Collection<InfoBean> templates = infoaccess.query(TTemplates.TBL_NAME, null, TTemplates.F_NAME, true);
        new TemplateListTable(templates).appendHtml(html, dict);
        return html;
    }
}
