package org.orekit.frames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.orekit.data.DataDirectoryCrawler;
import org.orekit.data.DataFileLoader;
import org.orekit.errors.OrekitException;
import org.orekit.time.TimeStamped;

/** Loader for bulletin B files.
 * <p>Bulletin B files contain {@link EarthOrientationParameters
 * Earth Orientation Parameters} for a few months periods.</p>
 * <p>The bulletin B files are recognized thanks to their base names,
 * which must match the pattern <code>bulletinb_IAU2000-###.txt</code>
 * (or <code>bulletinb_IAU2000-###.txt.gz</code> for gzip-compressed files)
 * where # stands for a digit character.</p>
 * @author Luc Maisonobe
 * @version $Revision:1665 $ $Date:2008-06-11 12:12:59 +0200 (mer., 11 juin 2008) $
 */
class BulletinBFilesLoader implements DataFileLoader {

    /** Conversion factor. */
    private static final double ARC_SECONDS_TO_RADIANS = 2 * Math.PI / 1296000;

    /** Supported files name pattern. */
    private Pattern namePattern;

    /** Section header pattern. */
    private final Pattern sectionHeaderPattern;

    /** Data line pattern in section 1. */
    private final Pattern section1DataPattern;

    /** Pattern for line introducing the final bulletin B values. */
    private final Pattern finalValuesStartPattern;

    /** Pattern for line introducing the bulletin B preliminary extension. */
    private final Pattern finalValuesEndPattern;

    /** Data line pattern in section 2. */
    private final Pattern section2DataPattern;

    /** Earth Orientation Parameters entries. */
    private SortedSet<TimeStamped> eop;

    /** Create a loader for IERS bulletin B files.
     * @param eop set where to <em>add</em> EOP data
     * (pre-existing data is preserved)
     */
    public BulletinBFilesLoader(final SortedSet<TimeStamped> eop) {
        namePattern = Pattern.compile("^bulletinb_IAU2000-(\\d\\d\\d)\\.txt$");
        this.eop = eop;
        sectionHeaderPattern = Pattern.compile("^ +([123456]) - \\p{Upper}+ \\p{Upper}+ \\p{Upper}+.*");
        finalValuesStartPattern = Pattern.compile("^\\p{Blank}+Final Bulletin B values.*");
        finalValuesEndPattern = Pattern.compile("^\\p{Blank}+Preliminary extension,.*");
        final String monthField = "^\\p{Blank}*\\p{Upper}\\p{Upper}\\p{Upper}";
        final String dayField = "\\p{Blank}+[ 0-9]\\p{Digit}";
        final String mjdField = "\\p{Blank}+(\\p{Digit}\\p{Digit}\\p{Digit}\\p{Digit}\\p{Digit})";
        final String storedRealField = "\\p{Blank}+(-?\\p{Digit}+\\.(?:\\p{Digit})+)";
        final String ignoredRealField = "\\p{Blank}+-?\\p{Digit}+\\.(?:\\p{Digit})+";
        final String finalBlanks = "\\p{Blank}*$";
        section1DataPattern = Pattern.compile(monthField + dayField + mjdField + ignoredRealField + ignoredRealField + ignoredRealField + ignoredRealField + ignoredRealField + ignoredRealField + finalBlanks);
        section2DataPattern = Pattern.compile(monthField + dayField + mjdField + storedRealField + storedRealField + storedRealField + ignoredRealField + ignoredRealField + ignoredRealField + ignoredRealField + finalBlanks);
    }

    /** Load Earth Orientation Parameters.
     * <p>The data is concatenated from all bulletin B data files
     * which can be found in the configured IERS directory.</p>
     * @exception OrekitException if some data can't be read or some
     * file content is corrupted
     */
    public void loadEOP() throws OrekitException {
        new DataDirectoryCrawler().crawl(this);
    }

    /** {@inheritDoc} */
    public void loadData(final InputStream input, final String name) throws OrekitException, IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        int mjdMin = -1;
        int mjdMax = -1;
        boolean inFinalValuesPart = false;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Matcher matcher = finalValuesStartPattern.matcher(line);
            if (matcher.matches()) {
                inFinalValuesPart = true;
            } else if (inFinalValuesPart) {
                matcher = section1DataPattern.matcher(line);
                if (matcher.matches()) {
                    final int mjd = Integer.parseInt(matcher.group(1));
                    if (mjdMin < 0) {
                        mjdMin = mjd;
                    } else {
                        mjdMax = mjd;
                    }
                } else {
                    matcher = finalValuesEndPattern.matcher(line);
                    if (matcher.matches()) {
                        break;
                    }
                }
            }
        }
        boolean inSection2 = false;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Matcher matcher = sectionHeaderPattern.matcher(line);
            if (matcher.matches() && "2".equals(matcher.group(1))) {
                inSection2 = true;
            } else if (inSection2) {
                matcher = section2DataPattern.matcher(line);
                if (matcher.matches()) {
                    final int date = Integer.parseInt(matcher.group(1));
                    final double x = Double.parseDouble(matcher.group(2)) * ARC_SECONDS_TO_RADIANS;
                    final double y = Double.parseDouble(matcher.group(3)) * ARC_SECONDS_TO_RADIANS;
                    final double dtu1 = Double.parseDouble(matcher.group(4));
                    if (date >= mjdMin) {
                        eop.add(new EarthOrientationParameters(date, dtu1, new PoleCorrection(x, y)));
                        if (date >= mjdMax) {
                            return;
                        }
                    }
                }
            }
        }
    }

    /** {@inheritDoc} */
    public boolean fileIsSupported(final String fileName) {
        return namePattern.matcher(fileName).matches();
    }
}
