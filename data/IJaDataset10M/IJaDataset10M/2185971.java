package ch.articlefox.servlet.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import ch.articlefox.ArticleFoxConstants;
import ch.articlefox.SessionConstants;
import ch.articlefox.db.TArticles;
import ch.articlefox.db.TUsers;
import ch.articlefox.init.ArticleFoxConfig;
import ch.articlefox.localization.Dictionaries;
import ch.articlefox.localization.Dictionary;
import ch.articlefox.servlet.elements.ArticleRow;
import ch.articlefox.servlet.elements.ArticleRowHeader;
import ch.articlefox.servlet.elements.DateSelector;
import ch.articlefox.servlet.elements.InitScript;
import ch.articlefox.servlet.elements.PageTitle;
import ch.articlefox.utils.ArticlesByCategoryComparator;
import ch.articlefox.utils.DateUtils;
import ch.articlefox.utils.MySQL;
import com.orelias.infoaccess.InfoAccess;
import com.orelias.infoaccess.InfoBean;

/**
 * A page which shows the state of all articles grouped by date.
 * 
 * @author Lukas Blunschi
 */
public class StateByDatePage extends AbstractPage {

    public static final String PAGENAME = "stateByDate";

    public StringBuffer getContent(HttpServletRequest req, InfoAccess infoaccess) {
        StringBuffer html = new StringBuffer();
        HttpSession session = req.getSession();
        ServletContext ctx = session.getServletContext();
        Dictionary dict = Dictionaries.getDictionaryFromSession(session);
        ArticleFoxConfig config = (ArticleFoxConfig) ctx.getAttribute(ArticleFoxConstants.A_CONFIG);
        Date dateSelected = DateSelector.getSelectedDate(req);
        String js = config.isEnableAutorefresh() ? "window.setInterval(\"reload()\",60000);" : "";
        new InitScript(js).appendHtml(html, dict);
        new PageTitle(dict.todaysArticles()).appendHtml(html, dict);
        Collection<InfoBean> coll2 = infoaccess.query(TArticles.TBL_NAME, null, null, true);
        new DateSelector(dateSelected, coll2, PAGENAME).appendHtml(html, dict);
        Integer userId = (Integer) req.getSession().getAttribute(SessionConstants.A_USERID);
        InfoBean userLoggedIn = infoaccess.get(TUsers.TBL_NAME, userId, true);
        html.append("<!-- article list table -->\n");
        html.append("<table id='article_list_table'>\n\n");
        String filter = TArticles.F_DATE + "='" + MySQL.dateFormatter.format(dateSelected) + "'";
        Collection<InfoBean> coll = infoaccess.query(TArticles.TBL_NAME, filter, null, true);
        ArrayList<InfoBean> articles = new ArrayList<InfoBean>(coll);
        Collections.sort(articles, new ArticlesByCategoryComparator());
        new ArticleRowHeader().appendHtml(html, dict);
        for (InfoBean article : articles) {
            new ArticleRow(article, userLoggedIn).appendHtml(html, dict);
        }
        html.append("<!-- closing article list table -->\n");
        html.append("</table>\n\n");
        html.append("<!-- status info -->\n");
        html.append("<div id='status_info'>\n");
        html.append(DateUtils.viewExactTimeFormatter.format(new Date()));
        html.append("</div>\n\n");
        return html;
    }
}
