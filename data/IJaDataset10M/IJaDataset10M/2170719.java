package net.sf.moviekebab.demo.quicktime.export;

import net.sf.moviekebab.toolset.QuicktimeTools;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.io.IOConstants;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.qtcomponents.MovieExporter;

/**
 * Export an existing movie to default format without dialog.
 *
 * @author Laurent Caillette
 * @author http://lists.apple.com/archives/quicktime-java/2002/Sep/msg00004.html
 */
public class ExportDemo2 {

    private static final String ORIGIN_MOVIE_FILE_NAME = System.getProperty("user.home") + "/Desktop/Sample.dv";

    private static final String DESTINATION_MOVIE_FILE_NAME = System.getProperty("user.home") + "/Desktop/exported.mov";

    public static void main(String[] args) throws QTException {
        QTSession.open();
        final OpenMovieFile movieOpener = OpenMovieFile.asRead(new QTFile(ExportDemo2.ORIGIN_MOVIE_FILE_NAME));
        final Movie originMovie = Movie.fromFile(movieOpener);
        final MovieExporter exporter = new MovieExporter(StdQTConstants.kQTFileTypeMovie);
        QuicktimeTools.dumpAtomContainer(System.out, exporter.getExportSettingsFromAtomContainer());
        originMovie.convertToFile(null, new QTFile(ExportDemo2.DESTINATION_MOVIE_FILE_NAME), StdQTConstants.kQTFileTypeMovie, StdQTConstants.kMoviePlayer, IOConstants.smSystemScript, StdQTConstants.movieToFileOnlyExport, exporter);
        QTSession.close();
    }
}
