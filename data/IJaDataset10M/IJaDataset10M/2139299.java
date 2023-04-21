package com.nccsjz.back.vote.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.nccsjz.back.vote.service.VoteService;
import com.nccsjz.base.BaseAction;
import com.nccsjz.pojo.Vote;
import com.nccsjz.pojo.VoteAnswer;

/**
 * VoteAction用于处理和选票相关的页面操作
 * 
 * @author jason
 * 
 */
@SuppressWarnings("serial")
public class VoteAction extends BaseAction {

    /** PATH_LIST_VOTES String 跳转至votes.jsp的逻辑路径 */
    private static final String PATH_LIST_VOTES = "listVotes";

    /** PATH_ADD_VOTE_INPUT 跳转至voteadd.jsp的逻辑路径 */
    private static final String PATH_ADD_VOTE_INPUT = "addVoteInput";

    /** PATH_EDIT_VOTE_INPUT 跳转至voteadd.jsp的逻辑路径 */
    private static final String PATH_EDIT_VOTE_INPUT = "editVoteInput";

    /** votes List<Vote> 所有选票信息 */
    private List<Vote> votes;

    /** vote Vote 封装用户提交的投票信息 */
    private Vote vote;

    /** answer String[] 封装用户提交的投票答案 */
    private String[] answers;

    /** answerIds String[] 封装答案编号 */
    private String[] answerIds;

    /** isVoted String 是否设定为投票问题 */
    private String isVoted;

    /*****答案ID*******/
    private Long answerid;

    /**
	 * listVotes方法用于处理显示选票列表的操作
	 * 
	 * @return
	 * @throws Exception
	 */
    public String listVotes() throws Exception {
        VoteService voteService = new VoteService();
        votes = voteService.getAllVotes();
        return PATH_LIST_VOTES;
    }

    /**
	 * addVoteInput方法处理跳转至voteadd.jsp的操作
	 * 
	 * @return
	 * @throws Exception
	 */
    public String addVoteInput() throws Exception {
        return PATH_ADD_VOTE_INPUT;
    }

    /**
	 * addVote方法用于处理添加投票问题的操作
	 * 
	 * @return
	 * @throws Exception
	 */
    public String addVote() throws Exception {
        if (null == answers || null == vote) {
            return PATH_ADD_VOTE_INPUT;
        }
        VoteService voteService = new VoteService();
        if (null != answers && null != vote) {
            List<VoteAnswer> voteAnswers = new ArrayList<VoteAnswer>();
            for (String answer : answers) {
                VoteAnswer voteAnswer = new VoteAnswer(vote);
                voteAnswer.setContent(answer);
                voteAnswers.add(voteAnswer);
            }
            vote.setAnswers(voteAnswers);
            if (StringUtils.equals("on", isVoted)) {
                vote.setState(1);
            }
            voteService.saveVote(vote);
            addActionMessage("选票问题及答案保存成功！");
        }
        votes = voteService.getAllVotes();
        return PATH_LIST_VOTES;
    }

    /**
	 * deleteVote方法删除指定的选票信息
	 * 
	 * @return
	 * @throws SQLException
	 */
    public String deleteVote() throws SQLException {
        if (null == vote) {
            return PATH_ADD_VOTE_INPUT;
        }
        VoteService voteService = new VoteService();
        voteService.removeVoteById(vote.getVoteId());
        addActionMessage("选票删除成功！");
        votes = voteService.getAllVotes();
        return PATH_LIST_VOTES;
    }

    /**
	 * setVoted方法用于将指定选票设置为投票
	 * 
	 * @return
	 * @throws SQLException
	 */
    public String setVoted() throws SQLException {
        VoteService voteService = new VoteService();
        if (null == vote) {
            votes = voteService.getAllVotes();
            return PATH_LIST_VOTES;
        }
        voteService.setVotedState(vote.getVoteId());
        addActionMessage("设置投票成功！");
        votes = voteService.getAllVotes();
        return PATH_LIST_VOTES;
    }

    /**
	 * editVoteInput方法用于跳转到voteedit.jsp页面
	 * 
	 * @return
	 * @throws SQLException
	 */
    public String editVoteInput() throws SQLException {
        VoteService voteService = new VoteService();
        if (null == vote) {
            return PATH_LIST_VOTES;
        }
        vote = voteService.findVoteByVoteId(vote.getVoteId());
        if (vote.getState() == 1) {
            isVoted = "on";
        }
        votes = voteService.getAllVotes();
        return PATH_EDIT_VOTE_INPUT;
    }

    /**
	 * editVote用于处理选票编辑操作
	 * 
	 * @return
	 * @throws SQLException
	 */
    public String editVote() throws SQLException {
        VoteService voteService = new VoteService();
        if (null == answers || null == vote) {
            return PATH_LIST_VOTES;
        }
        Vote v = new Vote();
        v.setVoteId(vote.getVoteId());
        v.setTitle(vote.getTitle());
        if (StringUtils.equals("on", isVoted)) {
            v.setState(1);
        }
        List<VoteAnswer> answersList = new ArrayList<VoteAnswer>();
        for (int i = 0; i < answers.length; i++) {
            VoteAnswer voteAnswer = new VoteAnswer(v);
            voteAnswer.setContent(answers[i]);
            voteAnswer.setVoteAnswerId(Long.valueOf(answerIds[i]));
            answersList.add(voteAnswer);
        }
        v.setAnswers(answersList);
        voteService.editVote(v);
        addActionMessage("选票内容更新成功！");
        votes = voteService.getAllVotes();
        return PATH_LIST_VOTES;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public String getIsVoted() {
        return isVoted;
    }

    public void setIsVoted(String isVoted) {
        this.isVoted = isVoted;
    }

    public String[] getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(String[] answerIds) {
        this.answerIds = answerIds;
    }

    public Long getAnswerid() {
        return answerid;
    }

    public void setAnswerid(Long answerid) {
        this.answerid = answerid;
    }
}
