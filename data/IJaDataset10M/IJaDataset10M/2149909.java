package com.liferay.portlet.polls.service.http;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.polls.model.PollsVote;
import java.util.Date;
import java.util.List;

/**
 * <a href="PollsVoteJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.liferay.portlet.polls.service.http.PollsVoteServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.polls.service.http.PollsVoteServiceJSON
 *
 */
public class PollsVoteJSONSerializer {

    public static JSONObject toJSONObject(PollsVote model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();
        jsonObj.put("voteId", model.getVoteId());
        jsonObj.put("userId", model.getUserId());
        jsonObj.put("questionId", model.getQuestionId());
        jsonObj.put("choiceId", model.getChoiceId());
        Date voteDate = model.getVoteDate();
        String voteDateJSON = StringPool.BLANK;
        if (voteDate != null) {
            voteDateJSON = String.valueOf(voteDate.getTime());
        }
        jsonObj.put("voteDate", voteDateJSON);
        return jsonObj;
    }

    public static JSONArray toJSONArray(List<com.liferay.portlet.polls.model.PollsVote> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
        for (PollsVote model : models) {
            jsonArray.put(toJSONObject(model));
        }
        return jsonArray;
    }
}
