package jshm.wts;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import jshm.Difficulty;
import jshm.Platform;
import jshm.util.PhpUtil;

public class WTScore implements Serializable {

    static final Logger LOG = Logger.getLogger(WTScore.class.getName());

    WTGame game = null;

    Platform platform = null;

    Instrument instrument = null;

    Difficulty difficulty = null;

    WTSong song = null;

    int score = 0;

    int rating = 0;

    int percent = 0;

    int streak = 0;

    String comment = "";

    String imageUrl = "";

    String videoUrl = "";

    public WTScore(WTGame game, Platform plat, Difficulty diff, WTSong song, Instrument instrument, int score) {
        this(game, plat, diff, song, instrument, score, 0, 0, 0);
    }

    public WTScore(WTGame game, Platform plat, Difficulty diff, WTSong song, Instrument instrument, int score, int rating, int percent, int streak) {
        this(game, plat, diff, song, instrument, score, rating, percent, streak, null);
    }

    public WTScore(WTGame game, Platform plat, Difficulty diff, WTSong song, Instrument instrument, int score, int rating, int percent, int streak, String comment) {
        this(game, plat, diff, song, instrument, score, rating, percent, streak, comment, null, null);
    }

    public WTScore(WTGame game, Platform plat, Difficulty diff, WTSong song, Instrument instrument, int score, int rating, int percent, int streak, String comment, String imageUrl, String videoUrl) {
        setGame(game);
        setPlatform(plat);
        setDifficulty(diff);
        setSong(song);
        setInstrument(instrument);
        setScore(score);
        setRating(rating);
        setPercent(percent);
        setStreak(streak);
        setComment(comment);
        setImageUrl(imageUrl);
        setVideoUrl(videoUrl);
    }

    public WTGame getGame() {
        return game;
    }

    public void setGame(WTGame game) {
        if (null == game) throw new IllegalArgumentException("game was null");
        this.game = game;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        if (null == platform) throw new IllegalArgumentException("platform was null");
        this.platform = platform;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        if (null == difficulty) throw new IllegalArgumentException("difficulty was null");
        this.difficulty = difficulty;
    }

    public WTSong getSong() {
        return song;
    }

    public void setSong(WTSong song) {
        if (null == song) throw new IllegalArgumentException("song was null");
        this.song = song;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        if (null == instrument) throw new IllegalArgumentException("instrument was null");
        this.instrument = instrument;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 0) throw new IllegalArgumentException("score must be >= 0, was " + score);
        this.score = score;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (!((0 == rating) || (3 <= rating && rating <= 5))) throw new IllegalArgumentException("rating must be 0, 3, 4, or 5, was " + rating);
        this.rating = rating;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        if (!(0 <= percent && percent <= 100)) throw new IllegalArgumentException("percent must be between 0 and 100, was " + percent);
        this.percent = percent;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        if (streak < 0) throw new IllegalArgumentException("streak must not be negative, was " + streak);
        this.streak = streak;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (null == comment) comment = "";
        this.comment = comment.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        if (null == imageUrl) imageUrl = "";
        this.imageUrl = imageUrl.trim();
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        if (null == videoUrl) videoUrl = "";
        this.videoUrl = videoUrl.trim();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        return PhpUtil.implode(platform, instrument, difficulty, "{" + song + "}", score, rating, percent, streak);
    }

    static final File CACHE_FILE = new File("data/wts/scores.cache");

    public static void save(List<WTScore> scores) {
        LOG.fine("Saving scores to " + CACHE_FILE.getName());
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(CACHE_FILE));
            out.writeObject(scores);
        } catch (Throwable t) {
            LOG.log(Level.WARNING, "Unable to save scores", t);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (Throwable t) {
                    LOG.log(Level.WARNING, "Unable to close output stream", t);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static List<WTScore> load() throws IOException {
        LOG.fine("Loading scores from " + CACHE_FILE.getName());
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(CACHE_FILE));
            Object o = null;
            try {
                o = in.readObject();
            } catch (ClassNotFoundException e) {
            }
            if (null == o) return null;
            return (List<WTScore>) o;
        } catch (IOException t) {
            LOG.throwing("WTScore", "load", t);
            throw t;
        } finally {
            if (null != in) in.close();
        }
    }
}
