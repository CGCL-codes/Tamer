package ch.articlefox.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import ch.articlefox.db.TTasks;
import com.orelias.infoaccess.InfoBean;

/**
 * A comparator which compares articles by last edit time for a given user.
 * 
 * @author Lukas Blunschi
 */
public class ArticlesByLastEditedComparator implements Comparator<InfoBean> {

    private int userId;

    public ArticlesByLastEditedComparator(InfoBean user) {
        this.userId = (Integer) user.getId();
    }

    public int compare(InfoBean article1, InfoBean article2) {
        Date lastEditTime1 = getLastEditTime(article1);
        Date lastEditTime2 = getLastEditTime(article2);
        if (lastEditTime1.equals(lastEditTime2)) {
            return ((Integer) article1.getId()).compareTo((Integer) article2.getId());
        } else {
            return -1 * (lastEditTime1.compareTo(lastEditTime2));
        }
    }

    private Date getLastEditTime(InfoBean article) {
        Date lastEditTime = new Date(0L);
        Collection<InfoBean> tasks = article.getManyRelation(TTasks.MR_ARTICLE);
        for (InfoBean task : tasks) {
            boolean done = (Boolean) task.getProperty(TTasks.F_DONE);
            if (done) {
                int curUserId = (Integer) task.getOneRelation(TTasks.F_USER).getId();
                if (userId == curUserId) {
                    Date curEditDate = (Date) task.getProperty(TTasks.F_EDITDATETIME);
                    if (curEditDate.after(lastEditTime)) {
                        lastEditTime = curEditDate;
                    }
                }
            }
        }
        return lastEditTime;
    }
}
