package papertoolkit.pattern;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import papertoolkit.PaperToolkit;
import papertoolkit.units.Units;
import papertoolkit.util.DebugUtils;

/**
 * <p>
 * Creates arbitrary-sized pattern blocks, assuming you have enough pattern files to supply it with.
 * </p>
 * <p>
 * This object keeps track of what pattern blocks you have used. It stores it in a map of each page (basically
 * a Rectangle2D bounds). Once a request comes in that is not servicable by this SINGLE page, it will
 * increment the patternFile Number, and allow you to get pattern from the next page.
 * </p>
 * <p>
 * <span class="BSDLicense"> This software is distributed under the <a
 * href="http://hci.stanford.edu/research/copyright.txt">BSD License</a>. </span>
 * </p>
 * 
 * @author <a href="http://graphics.stanford.edu/~ronyeh">Ron B Yeh</a> (ronyeh(AT)cs.stanford.edu)
 */
public class TiledPatternGenerator {

    /**
	 * Number of dots we pad between pattern requests, so that no two pattern requests are touching each
	 * other.
	 */
    private static final int BUFFER = 30;

    /**
	 * The name of the default pattern package (stored in pattern/default/).
	 */
    public static final String DEFAULT_PATTERN_PACKAGE_NAME = "default";

    /**
	 * Packages indexed by name.
	 */
    private Map<String, PatternPackage> availablePackages;

    /**
	 * Allows us to track usage within a page.
	 */
    private int lastDotUsedX = 0;

    /**
	 * Track pattern usage within a page. Reset every time we go to a new page.
	 */
    private int lastDotUsedY = 0;

    private int maxOfRecentHeightsInDots = 0;

    /**
	 * For debugging purposes.
	 */
    private int numTimesGetPatternCalled = 0;

    /**
	 * Where we should start getting our pattern from. This is incremented by some amount every time we call
	 * getPattern(...), so that the pattern returned will be unique.
	 */
    private int patternFileNumber = 0;

    /**
	 * Currently selected pattern package.
	 */
    private PatternPackage patternPackage;

    /**
	 * Customize this to reflect where you store your pattern definition files.
	 */
    private File patternPath;

    /**
	 * Default Pattern Path Location (PaperToolkit\data\pattern), automatically copied by eclipse to the
	 * export folder: PaperToolkit\bin\pattern. This will have to change at some point, when we move to the
	 * .jar deployment.
	 */
    public TiledPatternGenerator() {
        this(PaperToolkit.getPatternPath());
    }

    /**
	 * Customize the location of pattern definition files.
	 */
    public TiledPatternGenerator(File patternPathLocation) {
        patternPath = patternPathLocation;
        availablePackages = PatternPackage.getAvailablePatternPackages(patternPath);
        setPackage(DEFAULT_PATTERN_PACKAGE_NAME);
    }

    /**
	 * Prints out some information on the tiling...
	 */
    public void displayTilingInformation(Units horizontal, Units vertical) {
        long numDotsX = Math.round(horizontal.getValueInPatternDots());
        long numDotsY = Math.round(vertical.getValueInPatternDots());
        int numTilesNeededX = 0;
        int numTilesNeededY = 0;
        int numDotsRemainingX = (int) numDotsX;
        int numDotsRemainingY = (int) numDotsY;
        final int numPatternColsPerFile = patternPackage.getNumPatternColsPerFile();
        final int numPatternRowsPerFile = patternPackage.getNumPatternRowsPerFile();
        while (numDotsRemainingX > 0) {
            numDotsRemainingX -= numPatternColsPerFile;
            numTilesNeededX++;
        }
        final int numDotsXFromRightMostTiles = numDotsRemainingX + numPatternColsPerFile;
        while (numDotsRemainingY > 0) {
            numDotsRemainingY -= numPatternRowsPerFile;
            numTilesNeededY++;
        }
        final int numDotsYFromBottomMostTiles = numDotsRemainingY + numPatternRowsPerFile;
        System.out.println("Tiling Information (" + horizontal + ", " + vertical + ") {");
        System.out.println("\t" + numTilesNeededX + " Tile(s) in X, with " + numDotsXFromRightMostTiles + " horizontal dots from the rightmost tiles.");
        System.out.println("\t" + numTilesNeededY + " Tile(s) in Y, with " + numDotsYFromBottomMostTiles + " vertical dots from the bottommost tiles.");
        System.out.println("}");
    }

    /**
	 * @return the current pattern package.
	 */
    public PatternPackage getCurrentPatternPackage() {
        return patternPackage;
    }

    /**
	 * Returned pattern that is tiled appropriately, and automatically selected from the pattern package. By
	 * default, this pattern generator class will keep track of which pattern it has given you, and will give
	 * you unique pattern (if possible) every time you call this method.
	 * 
	 * @param width
	 *            the amount of pattern we need
	 * @param height
	 * 
	 * @return
	 */
    public TiledPattern getPattern(Units width, Units height) {
        final long numDotsX = Math.round(width.getValueInPatternDots());
        final long numDotsY = Math.round(height.getValueInPatternDots());
        int numTilesNeededX = 0;
        int numTilesNeededY = 0;
        int numDotsRemainingX = (int) numDotsX;
        int numDotsRemainingY = (int) numDotsY;
        final int numPatternColsPerFile = patternPackage.getNumPatternColsPerFile();
        final int numPatternRowsPerFile = patternPackage.getNumPatternRowsPerFile();
        while (numDotsRemainingX > 0) {
            numDotsRemainingX -= numPatternColsPerFile;
            numTilesNeededX++;
        }
        final int numDotsXFromRightMostTiles = numDotsRemainingX + numPatternColsPerFile;
        while (numDotsRemainingY > 0) {
            numDotsRemainingY -= numPatternRowsPerFile;
            numTilesNeededY++;
        }
        final int numDotsYFromBottomMostTiles = numDotsRemainingY + numPatternRowsPerFile;
        if (numTilesNeededX == 1 && numTilesNeededY == 1) {
            if (lastDotUsedX + numDotsXFromRightMostTiles > numPatternColsPerFile) {
                lastDotUsedX = 0;
                lastDotUsedY += maxOfRecentHeightsInDots + BUFFER;
            }
            if (lastDotUsedY + numDotsYFromBottomMostTiles > numPatternRowsPerFile) {
                lastDotUsedY = 0;
                patternFileNumber++;
            }
        } else {
        }
        final TiledPattern pattern = new TiledPattern(patternPackage, patternFileNumber, lastDotUsedX, lastDotUsedY, numTilesNeededX, numTilesNeededY, numDotsXFromRightMostTiles, numDotsYFromBottomMostTiles);
        patternFileNumber = pattern.getLastPatternFileUsed();
        lastDotUsedX += numDotsXFromRightMostTiles + BUFFER;
        maxOfRecentHeightsInDots = Math.max(maxOfRecentHeightsInDots, numDotsYFromBottomMostTiles + BUFFER);
        return pattern;
    }

    /**
	 * @param name
	 * @return a PatternPackage, indexed by name.
	 */
    public PatternPackage getPatternPackageByName(String name) {
        return availablePackages.get(name);
    }

    /**
	 * @return
	 * @return the set of name of available pattern packages.
	 */
    public List<String> listAvailablePatternPackageNames() {
        return new ArrayList<String>(availablePackages.keySet());
    }

    /**
	 * Resets the tracked history in this object. The next call to getPattern(...) will start over at the
	 * default state after calling this function.
	 */
    public void resetUniquePatternTracker() {
        patternFileNumber = 0;
        lastDotUsedY = 0;
        lastDotUsedX = 0;
    }

    /**
	 * Choose the package of pattern data.
	 * 
	 * @param packageName
	 */
    private void setPackage(String packageName) {
        PatternPackage pkg = availablePackages.get(packageName);
        if (pkg == null) {
            pkg = new ArrayList<PatternPackage>(availablePackages.values()).get(0);
            System.err.println("Warning: " + packageName + " does not exist. Setting Pattern Package to the first one available (" + pkg.getName() + ").");
        }
        patternPackage = pkg;
    }

    /**
	 * Explicitly manipulate the page number which will be the source of pattern for the next call to
	 * getPattern(...)
	 * 
	 * @param num
	 */
    public void setPatternFileNumber(int num) {
        resetUniquePatternTracker();
        patternFileNumber = num;
    }
}
