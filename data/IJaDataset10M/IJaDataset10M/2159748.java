package cn.vlabs.duckling.vwb.services.diff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import org.apache.commons.jrcs.diff.*;
import org.apache.commons.jrcs.diff.myers.MyersDiff;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import cn.vlabs.duckling.util.BaseDAO;
import cn.vlabs.duckling.util.TextUtil;
import cn.vlabs.duckling.vwb.VWBContext;
import cn.vlabs.duckling.vwb.services.config.Configable;
import cn.vlabs.duckling.vwb.services.config.NoPropertyFoundException;
import cn.vlabs.duckling.vwb.services.i18n.DucklingMessage;

/**
 * 
 * @author Yong Ke
 */
public class TraditionalDiffProvider extends BaseDAO implements DiffProvider {

    private static final Logger log = Logger.getLogger(TraditionalDiffProvider.class);

    private static final String CSS_DIFF_ADDED = "<tr><td class=\"diffadd\">";

    private static final String CSS_DIFF_REMOVED = "<tr><td class=\"diffrem\">";

    private static final String CSS_DIFF_UNCHANGED = "<tr><td class=\"diff\">";

    private static final String CSS_DIFF_CLOSE = "</td></tr>" + Diff.NL;

    protected String queryContentSQL = "SELECT content FROM vwb_dpage_content_info WHERE resourceid=? and version=?";

    public TraditionalDiffProvider() {
    }

    /**
     * @see com.ecyrd.jspwiki.WikiProvider#getProviderInfo()
     */
    public String getProviderInfo() {
        return "TraditionalDiffProvider";
    }

    /**
     * @see com.ecyrd.jspwiki.WikiProvider#initialize(com.ecyrd.jspwiki.WikiEngine, java.util.Properties)
     */
    public void initialize(Configable config) throws NoPropertyFoundException {
    }

    /**
     * Makes a diff using the BMSI utility package. We use our own diff printer,
     * which makes things easier.
     */
    public String makeDiffHtml(VWBContext ctx, String p1, String p2) {
        String diffResult = "";
        try {
            String[] first = Diff.stringToArray(TextUtil.replaceEntities(p1));
            String[] second = Diff.stringToArray(TextUtil.replaceEntities(p2));
            Revision rev = Diff.diff(first, second, new MyersDiff());
            if (rev == null || rev.size() == 0) {
                return "";
            }
            StringBuffer ret = new StringBuffer(rev.size() * 20);
            ret.append("<table class=\"diff\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
            rev.accept(new RevisionPrint(ctx, ret));
            ret.append("</table>\n");
            return ret.toString();
        } catch (DifferentiationFailedException e) {
            diffResult = "makeDiff failed with DifferentiationFailedException";
            log.error(diffResult, e);
        }
        return diffResult;
    }

    public static final class RevisionPrint implements RevisionVisitor {

        private StringBuffer m_result = null;

        private ResourceBundle m_rb;

        private RevisionPrint(VWBContext ctx, StringBuffer sb) {
            m_result = sb;
            m_rb = ctx.getBundle(DucklingMessage.CORE_RESOURCES);
        }

        public void visit(Revision rev) {
        }

        public void visit(AddDelta delta) {
            Chunk changed = delta.getRevised();
            print(changed, m_rb.getString("diff.traditional.added"));
            changed.toString(m_result, CSS_DIFF_ADDED, CSS_DIFF_CLOSE);
        }

        public void visit(ChangeDelta delta) {
            Chunk changed = delta.getOriginal();
            print(changed, m_rb.getString("diff.traditional.changed"));
            changed.toString(m_result, CSS_DIFF_REMOVED, CSS_DIFF_CLOSE);
            delta.getRevised().toString(m_result, CSS_DIFF_ADDED, CSS_DIFF_CLOSE);
        }

        public void visit(DeleteDelta delta) {
            Chunk changed = delta.getOriginal();
            print(changed, m_rb.getString("diff.traditional.removed"));
            changed.toString(m_result, CSS_DIFF_REMOVED, CSS_DIFF_CLOSE);
        }

        private void print(Chunk changed, String type) {
            m_result.append(CSS_DIFF_UNCHANGED);
            String[] choiceString = { m_rb.getString("diff.traditional.oneline"), m_rb.getString("diff.traditional.lines") };
            double[] choiceLimits = { 1, 2 };
            MessageFormat fmt = new MessageFormat("");
            fmt.setLocale(m_rb.getLocale());
            ChoiceFormat cfmt = new ChoiceFormat(choiceLimits, choiceString);
            fmt.applyPattern(type);
            Format[] formats = { NumberFormat.getInstance(), cfmt, NumberFormat.getInstance() };
            fmt.setFormats(formats);
            Object[] params = { new Integer(changed.first() + 1), new Integer(changed.size()), new Integer(changed.size()) };
            m_result.append(fmt.format(params));
            m_result.append(CSS_DIFF_CLOSE);
        }
    }

    /**
     * Get the specific version content of page which pageid is given. 
     * Brief Intro Here
     * @param int pageid - The page's ID
     * @param int version - The version specified.
     * @return - Return the content of the page at the version.
     */
    public String getPageContentByVersion(int pageid, int version) throws NoPageContentFoundException {
        ;
        Object objContent = getJdbcTemplate().query(queryContentSQL, new Integer[] { pageid, version }, new ResultSetExtractor() {

            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getString("content");
                }
                return null;
            }
        });
        if (objContent == null) {
            throw new NoPageContentFoundException(pageid, version);
        } else {
            return (String) objContent;
        }
    }

    /**
     * Makes a diff using the BMSI utility package. We use our own diff printer,
     * which makes things easier.
     */
    public String makeDiffHtml(VWBContext ctx, int pageid, int verFirst, int verSecond) throws NoPageContentFoundException {
        String contentFirst = "";
        String contentSecond = "";
        try {
            contentFirst = this.getPageContentByVersion(pageid, verFirst);
            contentSecond = this.getPageContentByVersion(pageid, verSecond);
        } catch (NoPageContentFoundException ncfex) {
            this.log.error("The specific version content not found, the difference action broken. ");
            throw ncfex;
        }
        return makeDiffHtml(ctx, contentFirst, contentSecond);
    }
}
