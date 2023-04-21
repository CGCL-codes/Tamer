package com.ibm.videoNoteTaker;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.MoviePlayer;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTJComponent;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.image.Matrix;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import java.awt.*;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @author eben
 * 
 * This class encapsulates a quicktime movie player.  Quicktime
 * provides a very basic interface for controlling playback 
 * (a play/pause button and a slider bar showing progress).
 * More advanced controls are found in the PlayerPanel wrapper.
 */
public class Player {

    protected QTComponent movComp;

    private MoviePlayer movPlayer;

    protected int timeScale;

    protected float frameRate = 30.0f;

    protected QTFile qtFile;

    protected Movie movie;

    protected OpenMovieFile openMovieFile;

    protected MovieController movieController;

    /**
	 * Constructor takes the QTFile indicating the media to play.
	 * 
	 * @param qtFile - media to play
	 * @throws QTException
	 */
    public Player(QTFile qtFile) throws QTException {
        this.qtFile = qtFile;
        OpenMovieFile openMovieFile = OpenMovieFile.asRead(qtFile);
        movie = MovieFromJNI.getPitchPreserveMovie(qtFile.getAbsolutePath());
        if (movie == null) movie = Movie.fromFile(openMovieFile);
        movPlayer = new MoviePlayer(movie);
        movieController = new MovieController(movie);
        movComp = QTFactory.makeQTComponent(movieController);
        timeScale = movPlayer.getScale();
    }

    /**
	 * Constructor that takes the name of the media file to play
	 * 
	 * @param movieFile - name of a media file playable by quicktime
	 * @throws QTException
	 */
    public Player(String movieFile) throws QTException {
        this(new QTFile(movieFile));
    }

    /**
	 * Get a java component for the player
	 * 
	 * @return
	 */
    public Component getVisualComponent() {
        return (movComp.asComponent());
    }

    /**
	 * Set the location for the video playback head.
	 * 
	 * @param seconds - the number of seconds into the video
	 * @throws QTException
	 */
    public void setTime(float seconds) throws QTException {
        movPlayer.setTime(Math.round(timeScale * seconds));
    }

    /**
	 * Get the current location of the video playback head.
	 * 
	 * @return a float value indicating the number of seconds in
	 * @throws QTException
	 */
    public float getTime() throws QTException {
        return ((float) movPlayer.getTime()) / timeScale;
    }

    /**
	 * getTimeRecord
	 * 
	 * @return a TimeRecord corresponding to the current location of
	 * the playback head.
	 * 
	 * @throws QTException
	 */
    public TimeRecord getTimeRecord() throws QTException {
        return new TimeRecord(getTime(), frameRate);
    }

    /**
	 * Get the current frame rate that we're playing the video.
	 * 1.0 is normal forward, -1.0 is normal reverse, 2.0 is double
	 * speed, etc.
	 * 
	 * @return the current frame rate, where 1.0 is normal forward
	 */
    public float getFrameRate() {
        return frameRate;
    }

    /**
	 * Set the playback rate, where 1.0 is normal forward.
	 * 
	 * @param rate - the rate at which to play the video
	 * @throws QTException
	 */
    public void setRate(float rate) throws QTException {
        movPlayer.setRate(rate);
    }

    /**
	 * How long is the movie?
	 * 
	 * @return - the number of seconds long
	 * @throws QTException
	 */
    public float getDuration() throws QTException {
        return ((float) movPlayer.getDuration()) / timeScale;
    }

    float currentScale = 1.0f;

    /**
	 * setScaling - used to zoom in on the movie
	 * @param scale - how much to zoom
	 * @throws QTException
	 */
    void setScaling(float scale) throws QTException {
        currentScale = scale;
        Matrix mat = movPlayer.getMatrix();
        mat.scale(scale, scale, 0, 0);
        movPlayer.setMatrix(mat);
    }

    /**
	 * getScaling - how much is the video currently zoomed?
	 * 
	 * @return - a floating point indicating the zoom leevl
	 */
    float getScaling() {
        return (currentScale);
    }
}
