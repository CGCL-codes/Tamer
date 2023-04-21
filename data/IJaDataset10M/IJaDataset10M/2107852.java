package net.sf.nebulacards.util.ui;

import net.sf.nebulacards.main.*;
import java.awt.*;

/**
 * Display the information for all players in one box.
 * @author James Ranson
 * @version 0.8
 */
public class AllPlayersPanel extends Panel {

    protected boolean app_showScore, app_showBid, app_showBags;

    protected String app_bagsHeader = "Bags";

    protected int app_numPlayers;

    Label[] app_names, app_scores, app_bids, app_bags;

    /**
	 * Constructor.
	 * @param showScore Should the scores should be displayed.
	 * @param showBid Should the bids be displayed.
	 * @param showBags Should the bags be displayed.
	 */
    public AllPlayersPanel(final boolean showScore, final boolean showBid, final boolean showBags) {
        app_showScore = showScore;
        app_showBid = showBid;
        app_showBags = showBags;
        app_numPlayers = 0;
    }

    /**
	 * Set the text to appear as a heading on the bags column.  The default
	 * is "Bags".
	 */
    public void setBagsHeader(String header) {
        app_bagsHeader = (header == null) ? "<NULL>" : header;
    }

    /**
	 * Construct a panel with all displays on.
	 */
    public AllPlayersPanel() {
        this(true, true, true);
    }

    /**
	 * Set the information on all players.  This is the only way to change
	 * the number of players.
	 * @param p The new array of players.
	 */
    protected void setPlayersResize(final Player[] p) {
        int columns = 1;
        if (app_showScore) columns++;
        if (app_showBid) columns++;
        if (app_showBags) columns++;
        removeAll();
        setLayout(new GridLayout(p.length + 1, columns));
        add(new Label("Name", Label.CENTER));
        if (app_showScore) add(new Label("Score", Label.CENTER));
        if (app_showBid) add(new Label("Bids", Label.CENTER));
        if (app_showBags) add(new Label(app_bagsHeader, Label.CENTER));
        app_names = new Label[p.length];
        app_scores = new Label[p.length];
        app_bids = new Label[p.length];
        app_bags = new Label[p.length];
        for (int i = 0; i < p.length; i++) {
            add(app_names[i] = new Label(p[i].getName(), Label.LEFT));
            if (app_showScore) add(app_scores[i] = new Label("" + p[i].getScore(), Label.CENTER));
            if (app_showBid) add(app_bids[i] = new Label("" + p[i].getBid(), Label.CENTER));
            if (app_showBags) add(app_bags[i] = new Label("" + p[i].getBags(), Label.CENTER));
        }
        validate();
        app_numPlayers = p.length;
    }

    /**
	 * Set the information on all players.  Only components whose information
	 * has changed are redrawn.
	 * @param p The new array of players.
	 */
    protected void setPlayersSameSize(final Player[] p) {
        for (int i = 0; i < p.length; i++) {
            if (!app_names[i].getText().equals(p[i].getName())) app_names[i].setText(p[i].getName());
            if (app_showScore && !app_scores[i].getText().equals("" + p[i].getScore())) app_scores[i].setText("" + p[i].getScore());
            if (app_showBid && !app_bids[i].getText().equals("" + p[i].getBid())) app_bids[i].setText("" + p[i].getBid());
            if (app_showBags && !app_bags[i].getText().equals("" + p[i].getBags())) app_bags[i].setText("" + p[i].getBags());
        }
    }

    /**
	 * Set the information on all players.
	 * @param p The new array of players.
	 */
    public synchronized void setPlayers(final Player[] p) {
        if (app_numPlayers != p.length) setPlayersResize(p); else setPlayersSameSize(p);
    }
}
