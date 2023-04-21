package com.appspot.spelstegen.client.widgets.league;

import java.util.List;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.MatchDrawException;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.services.MatchListUpdateListener;
import com.appspot.spelstegen.client.services.ScoreCalculator;
import com.appspot.spelstegen.client.services.ServiceManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Displays all match results in a league
 * 
 * @author Per Mattsson
 */
public class MatchesTablePanel extends VerticalPanel implements MatchListUpdateListener {

    private static final DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");

    private Grid matchTable;

    private List<Match> matches;

    public MatchesTablePanel() {
        this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        this.setSpacing(10);
        ServiceManager.getInstance().addMatchListUpdateListener(this);
        matchTable = new Grid(3, 4);
        matchTable.setCellSpacing(0);
        matchTable.setWidth("100%");
        add(matchTable);
    }

    private void populateMatches() {
        matchTable.resize(matches.size(), 4);
        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            matchTable.setText(i, 0, df.format(match.getDate()));
            matchTable.setHTML(i, 1, getPlayerNameHtml(match, match.getPlayer1()));
            matchTable.setHTML(i, 2, getPlayerNameHtml(match, match.getPlayer2()));
            matchTable.setText(i, 3, ScoreCalculator.getResultsString(match));
        }
    }

    /**
	 * Returns player name. If player is winner of match, name will be
	 * returned in bold; otherwise plain text.
	 */
    private String getPlayerNameHtml(Match match, Player player) {
        try {
            boolean isWinner = ScoreCalculator.getWinner(match).equals(player);
            if (isWinner) {
                return "<b>" + player.getPlayerName() + "</b>";
            }
        } catch (MatchDrawException e) {
            GWT.log(e.getMessage(), e);
        }
        return player.getPlayerName();
    }

    @Override
    public void matchListUpdated(List<Match> matches) {
        this.matches = matches;
        populateMatches();
    }
}
