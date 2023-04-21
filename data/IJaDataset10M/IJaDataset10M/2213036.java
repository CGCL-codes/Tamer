package org.xtoto.utils;

import org.xtoto.model.Team;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class TeamScoreRenderer implements IChoiceRenderer<Team> {

    public Object getDisplayValue(Team team) {
        return FormatUtils.formatTeamWithScore(team);
    }

    public String getIdValue(Team team, int i) {
        return Long.toString(team.getId());
    }
}
