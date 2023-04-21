package oxygen.forum.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import oxygen.forum.ForumConstants;
import oxygen.forum.ForumIndexingManager;
import oxygen.forum.ForumLocal;
import oxygen.forum.data.ForumDAO;
import oxygen.forum.data.Topic;
import oxygen.markup.indexing.HitsHandler;
import oxygen.util.StringUtils;
import oxygen.web.GenericWebAction;
import oxygen.web.ViewContext;
import oxygen.web.WebInteractionContext;
import oxygen.web.WebLocal;

public class SearchResultsAction extends GenericWebAction {

    public static final SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");

    protected void preRender() throws Exception {
        WebInteractionContext wctx = WebLocal.getWebInteractionContext();
        String s = null;
        Map q = new HashMap();
        if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_POSTER))) {
            q.put(ForumConstants.SEARCH_INDEX_POSTER, s);
        }
        if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_TITLE))) {
            q.put(ForumConstants.SEARCH_INDEX_TITLE, s);
        }
        if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_TOPIC_CONTENTS))) {
            q.put(ForumConstants.SEARCH_INDEX_TOPIC_CONTENTS, s);
        }
        if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_FORUM_ID))) {
            q.put(ForumConstants.SEARCH_INDEX_FORUM_ID, s);
        }
        Date[] d = new Date[2];
        if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_TOPIC_LAST_POST_DATE + ".0"))) {
            d[0] = dateformat.parse(s);
        }
        if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_TOPIC_LAST_POST_DATE + ".1"))) {
            d[1] = dateformat.parse(s);
        }
        if (d[0] != null || d[1] != null) {
            q.put(ForumConstants.SEARCH_INDEX_TOPIC_LAST_POST_DATE, d);
        }
        ForumIndexingManager fim = ForumLocal.getForumEngine().getForumIndexingManager();
        final ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
        final LinkedHashMap map = new LinkedHashMap();
        HitsHandler hhdlr = new HitsHandler() {

            public void handleHits(Hits hits) throws Exception {
                int numhits = hits.length();
                int maxHits = Integer.parseInt(ForumLocal.getForumEngine().getProperty(ForumConstants.SEARCH_MAX_NUM_HITS_KEY));
                numhits = Math.min(maxHits, numhits);
                for (int i = 0; i < numhits; i++) {
                    Document doc = hits.doc(i);
                    Long id = new Long(doc.get(ForumConstants.SEARCH_INDEX_TOPIC_ID));
                    Topic t = (Topic) dao.get(Topic.class, id, true);
                    map.put(t, new Double((double) hits.score(i)));
                }
            }
        };
        fim.search(q, hhdlr);
        ViewContext tctx = WebLocal.getViewContext();
        SearchResultsModel model = new SearchResultsModel();
        model.map = map;
        tctx.setModel("searchresults", model);
    }

    public int render() throws Exception {
        preRender();
        return super.render();
    }

    public static class SearchResultsModel {

        private LinkedHashMap map;

        public Topic[] getTopics() {
            return (Topic[]) map.keySet().toArray(new Topic[0]);
        }

        public double getScore(Topic t) {
            return ((Double) map.get(t)).doubleValue();
        }
    }
}
