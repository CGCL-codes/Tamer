package freedbimporter.extraction;

/**
 * Indicates the extractor encountered a compact disc (CD) without a year of release set.
 * <p>
 *
 * @version      1.0 by 10.8.2004
 * @author       Copyright 2004 <a href="MAILTO:freedb2mysql@freedb2mysql.de">Christian Kruggel</a> - freedbimporter and all it&acute;s parts are free software and destributed under <a href="http://www.gnu.org/licenses/gpl-2.0.txt" target="_blank">GNU General Public License</a>
 */
public class ExtractedCDWithUnsetReleaseYear extends ExtractorException {

    public ExtractedCDWithUnsetReleaseYear(String exceptionMessage) {
        super(exceptionMessage);
    }
}
