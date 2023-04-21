package com.jsu.struts2.action;

import java.util.List;
import com.jsu.hibernate.domains.Contest;
import com.jsu.hibernate.domains.Statistics;
import com.jsu.util.DAOFactory;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Administrator
 *
 */
public class ShowStatistics extends ActionSupport {

    private static final long serialVersionUID = -5711987674L;

    private Integer contestId;

    private List<Statistics> statisticses;

    private Contest contest;

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public List<Statistics> getStatisticses() {
        return statisticses;
    }

    public void setStatisticses(List<Statistics> statisticses) {
        this.statisticses = statisticses;
    }

    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    public String execute() {
        try {
            contest = DAOFactory.getInstance().getContestDAO().findById(contestId, false);
            statisticses = DAOFactory.getInstance().getStatisticsDAO().findAllInContest(contest);
            return SUCCESS;
        } catch (Exception e) {
            return "error";
        }
    }
}
