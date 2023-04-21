package ch.articlefox.db.wrappers;

import java.util.Collection;
import ch.articlefox.Phases;
import ch.articlefox.db.TTasks;
import com.orelias.infoaccess.InfoBean;

/**
 * A wrapper around an article info bean.
 * 
 * @author Lukas Blunschi
 */
public class Article {

    private final InfoBean article;

    public Article(InfoBean article) {
        this.article = article;
    }

    public int getActivePhase() {
        int activePhase = Phases.DONE;
        Collection<InfoBean> tasks = article.getManyRelation(TTasks.MR_ARTICLE);
        for (InfoBean task : tasks) {
            boolean done = (Boolean) task.getProperty(TTasks.F_DONE);
            if (!done) {
                int phase = (Integer) task.getProperty(TTasks.F_PHASE);
                if (phase < activePhase) {
                    activePhase = phase;
                }
            }
        }
        return activePhase;
    }
}
