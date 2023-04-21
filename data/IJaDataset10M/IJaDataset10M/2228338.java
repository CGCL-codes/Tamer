package com.ballroomregistrar.compinabox.online.web.action;

import java.util.Collections;
import java.util.List;
import com.ballroomregistrar.compinabox.online.data.Competition;
import com.ballroomregistrar.compinabox.online.data.Couple;
import com.ballroomregistrar.compinabox.online.data.Entry;
import com.ballroomregistrar.compinabox.util.CompetitionDateComparator;
import com.google.common.collect.Lists;
import com.opensymphony.xwork2.Action;

@Unsecured
public class CoupleView implements Action {

    private Couple couple;

    public String execute() {
        return "success";
    }

    public void setCouple(final Couple couple) {
        this.couple = couple;
    }

    public Couple getCouple() {
        return couple;
    }

    public List<Competition> getRecentCompetitions() {
        List<Competition> competitions = Lists.newArrayList();
        for (Entry e : couple.getEntries()) {
            if (!competitions.contains(e.getEvent().getCompetition())) {
                competitions.add(e.getEvent().getCompetition());
            }
        }
        Collections.sort(competitions, new CompetitionDateComparator(-1));
        return competitions;
    }
}
