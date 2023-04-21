package com.kirolak.jsp.beans;

import java.util.ArrayList;
import java.util.List;
import com.kirolak.KirolakObject;
import com.kirolak.Sport;
import com.kirolak.Team;
import com.kirolak.dao.CompetitionDAO;

public class competitionsBean {

    private KirolakObject item;

    public competitionsBean() {
    }

    public List<KirolakObject> getCompetitions() {
        if (this.item.getClass().equals(Sport.class)) {
            return CompetitionDAO.listBySport((Sport) this.item);
        } else if (this.item.getClass().equals(Team.class)) {
            return CompetitionDAO.listByTeam((Team) this.item);
        } else {
            return new ArrayList<KirolakObject>();
        }
    }

    public KirolakObject getItem() {
        return item;
    }

    public void setItem(KirolakObject item) {
        this.item = item;
    }
}
