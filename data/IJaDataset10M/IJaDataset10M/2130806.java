package org.mbari.qt;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quicktime.QTException;
import quicktime.std.movies.Movie;

/**
 *
 * @author brian
 */
public class QTTest {

    private static final Logger log = LoggerFactory.getLogger(QTTest.class);

    /** Creates a new instance of TimeUtilTest */
    public QTTest() {
    }

    /**
     * Method description
     *
     */
    @Test()
    public void testMpeg() {
        String url = SupportUtil.getNetworkMovieFile();
        try {
            QT.manageSession();
            Movie movie = QT.openMovieFromUrl(new URL(url));
            boolean isMpeg = QT.isMpegMovie(movie);
            Assert.assertTrue(isMpeg);
        } catch (MalformedURLException ex) {
            Assert.fail();
        } catch (QTException ex) {
            Assert.fail();
        }
    }

    /**
     * Method description
     *
     */
    @Test()
    public void testNotMpeg() {
        String url = SupportUtil.getLocalMovieFile(SupportUtil.TIMECODE_MOVIE);
        try {
            QT.manageSession();
            Movie movie = QT.openMovieFromUrl(new URL(url));
            boolean isMpeg = QT.isMpegMovie(movie);
            Assert.assertFalse(isMpeg);
        } catch (MalformedURLException ex) {
            Assert.fail();
        } catch (QTException ex) {
            Assert.fail();
        }
    }
}
