package net.sf.moviekebab.demo.quicktime.export;

import java.io.File;
import java.io.IOException;
import net.sf.moviekebab.toolset.QuicktimeTools;
import quicktime.Errors;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieProgress;
import quicktime.std.qtcomponents.MovieExporter;

/**
 * Export an existing movie to custom format without dialog, and now with
 * a progress listener that may cancel.
 *
 * @author Laurent Caillette
 * @author http://lists.apple.com/archives/quicktime-java/2002/Sep/msg00004.html
 */
public class ExportDemo4 {

    private static final String ORIGIN_MOVIE_FILE_NAME = "C:\\usr\\MovieKebab\\head\\src\\test\\net\\sf\\moviekebab\\service\\implementation\\movie\\quicktime\\export\\slaveprocess\\Foo.dv";

    private static final String DESTINATION_MOVIE_FILE_NAME = "exported.mov";

    private static final String EXPORT_DESCRIPTOR_NAME = "myformat.bin";

    public static void main(String[] args) throws QTException, IOException, InterruptedException {
        final String originFileName = new File(ORIGIN_MOVIE_FILE_NAME).getCanonicalPath();
        final File destinationFile = new File(DESTINATION_MOVIE_FILE_NAME).getCanonicalFile();
        destinationFile.delete();
        destinationFile.createNewFile();
        final String destinationFileName = destinationFile.getCanonicalPath();
        QTSession.open();
        try {
            final AtomContainer exportSettings = QuicktimeTools.readExportDescriptor(ExportDemo4.class, ExportDemo4.EXPORT_DESCRIPTOR_NAME);
            final OpenMovieFile movieOpener = OpenMovieFile.asRead(new QTFile(originFileName));
            final Movie originMovie = Movie.fromFile(movieOpener);
            final MovieExporter exporter = new MovieExporter(StdQTConstants.kQTFileTypeMovie);
            exporter.setExportSettingsFromAtomContainer(exportSettings);
            exporter.setProgressProc(new MovieProgress() {

                public int execute(Movie movie, int message, int whatOperation, float percentDone) {
                    return ExportDemo4.doExecute(message, whatOperation, percentDone);
                }
            });
            class ExportEndLock {
            }
            final ExportEndLock exportEndLock = new ExportEndLock();
            final Runnable exportRunner = new Runnable() {

                public void run() {
                    System.out.println("Starting export from\n  " + originFileName + " \nto\n  " + destinationFileName);
                    try {
                        try {
                            final QTFile qtFile = new QTFile(destinationFileName);
                            final int duration = originMovie.getDuration();
                            exporter.toFile(qtFile, originMovie, null, 0, duration);
                        } catch (QTException e) {
                            if (Errors.userCanceledErr == e.errorCode()) {
                                System.out.println("User cancelled.");
                            } else {
                                throw e;
                            }
                        }
                    } catch (QTException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Export finished.");
                    synchronized (exportEndLock) {
                        exportEndLock.notifyAll();
                    }
                }
            };
            new Thread(exportRunner).start();
            synchronized (exportEndLock) {
                exportEndLock.wait();
            }
        } finally {
            QTSession.close();
        }
    }

    private static int doExecute(int message, int whatOperation, float percentDone) {
        System.out.println("percentDone = " + percentDone + ", " + "whatOperation = " + QuicktimeTools.getProgressOperationName(whatOperation) + ", " + "message = " + QuicktimeTools.getProgressMessageName(message));
        return Errors.noErr;
    }
}
