package com.liferay.portlet.polls.util;

import java.util.Iterator;
import com.liferay.portal.SystemException;
import com.liferay.portlet.polls.ejb.PollsChoiceManagerUtil;
import com.liferay.portlet.polls.ejb.PollsVoteManagerUtil;
import com.liferay.portlet.polls.model.PollsChoice;
import com.liferay.util.StringPool;

/**
 * <a href="PollsUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @author  Shepherd Ching
 * @version $Revision: 1.15 $
 *
 */
public class PollsUtil {

    public static org.jfree.data.category.CategoryDataset getVotesDataset(String questionId) throws SystemException {
        org.jfree.data.category.DefaultCategoryDataset dataset = new org.jfree.data.category.DefaultCategoryDataset();
        String seriesName = StringPool.BLANK;
        Iterator itr = PollsChoiceManagerUtil.getChoices(questionId).iterator();
        while (itr.hasNext()) {
            PollsChoice choice = (PollsChoice) itr.next();
            String category = choice.getChoiceId();
            Integer number = new Integer(PollsVoteManagerUtil.getVotesSize(questionId, choice.getChoiceId()));
            dataset.addValue(number, seriesName, choice.getChoiceId());
        }
        return dataset;
    }
}
