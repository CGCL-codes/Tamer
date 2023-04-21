package jshm.concepts;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jshm.*;
import jshm.gh.*;
import jshm.gui.LoginDialog;
import jshm.rb.*;
import jshm.sh.scraper.*;
import jshm.wt.WtGame;
import jshm.wt.WtSong;

@SuppressWarnings("unused")
public class ShGhScraperTest {

    static final RbGame game = RbGame.RB1_XBOX360;

    static final WtGame wtgame = WtGame.GHWT_XBOX360;

    static final Instrument.Group group = Instrument.Group.GUITAR;

    static final Difficulty difficulty = Difficulty.EXPERT;

    public static void main(String[] args) throws Exception {
        jshm.logging.Log.configTestLogging();
        jshm.util.TestTimer.start(true);
        doSongs();
        jshm.util.TestTimer.stop(true);
    }

    static void doScores() throws Exception {
        LoginDialog.showDialog();
        List<? extends Score> scores = WtScoreScraper.scrapeLatest(wtgame, group, difficulty);
        for (Score s : scores) System.out.println(s);
    }

    static void doSongs() throws Exception {
        List<WtSong> songs = WtSongScraper.scrape(wtgame);
        for (Song s : songs) {
            System.out.println(s);
        }
    }
}
