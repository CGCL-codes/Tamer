package net.sf.moviekebab.demo.quicktime.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;

/**
 * Plays a movie in an AWT component.
 * 
 * @author http://www.onjava.com/lpt/a/4318#examples
 */
public class PlayerDemo1 extends Frame {

    Movie movie;

    public PlayerDemo1(String title) throws QTException {
        super(title);
        QTSession.open();
        FileDialog fd = new FileDialog(this, "Select source movie", FileDialog.LOAD);
        fd.show();
        if (fd.getFile() == null) return;
        final File f = new File(fd.getDirectory(), fd.getFile());
        OpenMovieFile omFile = OpenMovieFile.asRead(new QTFile(f));
        movie = Movie.fromFile(omFile);
        final MovieController controller = new MovieController(movie);
        final int timeScale = controller.getTimeScale();
        final int duration = movie.getDuration();
        final long startTime = duration / 3;
        final long endTime = (long) (duration / 1.5);
        final TimeRecord timeRecordStart = new TimeRecord(timeScale, startTime);
        final TimeRecord timeRecordDuration = new TimeRecord(timeScale, endTime - startTime);
        controller.setSelectionBegin(timeRecordStart);
        controller.setSelectionDuration(timeRecordDuration);
        controller.setLooping(true);
        controller.setPlaySelection(true);
        controller.setVisible(false);
        Component canvas = (Component) QTFactory.makeQTComponent(controller);
        setBackground(Color.GRAY);
        add(canvas);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                QTSession.close();
                System.exit(0);
            }
        });
        System.out.println("timeScale = " + timeScale);
        System.out.println("Canvas is a " + canvas.getClass().getName());
    }

    public static void main(String[] args) throws QTException {
        System.out.println("java version is " + System.getProperty("java.version"));
        PlayerDemo1 frame = new PlayerDemo1("Simple QTJ Player");
        frame.pack();
        frame.setVisible(true);
        frame.movie.start();
    }
}
