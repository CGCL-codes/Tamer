package com.kirolak.jsf.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import com.kirolak.Group;
import com.kirolak.KirolakObject;
import com.kirolak.Match;
import com.kirolak.Round;
import com.kirolak.Standing;
import com.kirolak.Team;
import com.kirolak.dao.GroupDAO;
import com.kirolak.dao.MatchDAO;
import com.kirolak.dao.RoundDAO;
import com.kirolak.dao.StandingDAO;
import com.kirolak.dao.TeamDAO;
import com.kirolak.extended.GroupExt;
import com.kirolak.extended.StandingExt;
import com.kirolak.jsf.util.FacesUtil;
import com.kirolak.jsf.util.Messages;
import com.kirolak.util.Updater;

public class RoundBean extends KirolakSession {

    private List<Match> listMatch;

    public String saveRound() {
        Round round = (Round) this.item;
        round.setMatches(this.listMatch);
        RoundDAO.saveRound(round);
        StandingDAO.calculate(round);
        Updater.notify(round);
        this.items = null;
        return "list";
    }

    public String getTitle() {
        if (this.item.getIntId() > -1) {
            return this.item.getName();
        } else {
            return Messages.getString("messages", "new_round");
        }
    }

    public List<KirolakObject> getItems() {
        if (this.items == null) {
            this.items = RoundDAO.listByGroup((Group) this.parent);
        }
        return this.items;
    }

    public String newItem() {
        this.item = new Round();
        ((Round) this.item).setGroup((Group) this.parent);
        return "edit";
    }

    @Override
    public void setParent(KirolakObject parent) {
        this.parent = parent;
        if (this.item != null) {
            ((Round) this.item).setGroup((Group) parent);
        }
    }

    public String load() {
        this.setParent(GroupDAO.get(Integer.parseInt("" + FacesUtil.getRequestParameter("parent"))));
        this.items = null;
        return "rounds";
    }

    public String auto() {
        if (this.getItems().size() == 0) {
            List<Round> rounds = GroupExt.calculateSchedule((Group) this.parent);
            Iterator<Round> iterator = rounds.iterator();
            while (iterator.hasNext()) {
                Round round = iterator.next();
                RoundDAO.saveRound(round);
                StandingExt.create(round);
                Updater.notify(round);
            }
        }
        this.items = null;
        return "rounds";
    }

    public List<SelectItem> getSelectableTeams() {
        List<SelectItem> selectableTeams = new ArrayList<SelectItem>();
        List<KirolakObject> teams = TeamDAO.listByGroup((Group) this.parent);
        Iterator<KirolakObject> iterator = teams.iterator();
        selectableTeams.add(new SelectItem(null, ""));
        while (iterator.hasNext()) {
            Team team = (Team) iterator.next();
            selectableTeams.add(new SelectItem((team), team.getName()));
        }
        return selectableTeams;
    }

    public List<Match> getListMatch() {
        Round round = (Round) this.item;
        this.listMatch = MatchDAO.listByRound(round);
        if (this.listMatch.isEmpty()) {
            for (int n = 0; n < this.getSelectableTeams().size() / 2; n++) {
                Match match = new Match();
                match.setRound(round);
                this.listMatch.add(match);
            }
        }
        return this.listMatch;
    }

    public List<Standing> getStandings() {
        return StandingDAO.listByRound((Round) this.item);
    }

    public void setListMatch(List<Match> listMatch) {
        this.listMatch = listMatch;
    }
}
