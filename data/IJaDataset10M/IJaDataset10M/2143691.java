package com.ballroomregistrar.compinabox.online.web.action.ajax;

import java.util.List;
import com.ballroomregistrar.compinabox.online.data.Competition;
import com.ballroomregistrar.compinabox.online.data.CompetitionRepository;
import com.ballroomregistrar.compinabox.online.web.action.Unsecured;
import com.opensymphony.xwork2.Action;

@Unsecured
public class ResultList implements Action {

    private CompetitionRepository repo;

    public void setCompetitionRepository(final CompetitionRepository repo) {
        this.repo = repo;
    }

    public String execute() {
        return "success";
    }

    public List<Competition> getCompetitions() {
        return repo.getCompetitionsWithResults();
    }
}
