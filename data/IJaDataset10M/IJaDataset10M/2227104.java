package fitlibrary.graphic;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import fitlibrary.FitLibraryFixture;
import fitlibrary.differences.LocalFile;
import fitlibrary.log.Logging;

/**
 * Used to check that the Dot file associated with the GIF matches the expected
 * Dot file contents.
 * It assumes that Dot is installed, as it runs it to produce a GIF for an actual
 * value that doesn't match.
 * This general approach can be used with any image-making scheme.
 */
public class DotGraphic implements GraphicInterface {

    private static final Random random = new Random(System.currentTimeMillis());

    protected String dot;

    public DotGraphic(String dot) {
        this.dot = dot;
    }

    public static DotGraphic parseGraphic(LocalFile file) {
        return new DotGraphic(getDot(file));
    }

    public boolean equals(Object other) {
        if (!(other instanceof DotGraphic)) return false;
        boolean equals = dot.equals(((DotGraphic) other).dot);
        Logging.log(this, "equals(): '" + dot + "' and '" + other + "' equals=" + equals);
        return equals;
    }

    public LocalFile toGraphic() {
        try {
            LocalFile actualImageFile = actualImageFile(dot);
            Logging.log(this, "toGraphic(): '" + actualImageFile + "'");
            return actualImageFile;
        } catch (IOException ex) {
            throw new RuntimeException("Problem with Dot: " + ex);
        }
    }

    public String toString() {
        String htmlImageLink = toGraphic().htmlImageLink();
        Logging.log(this, "toString(): '" + htmlImageLink + "'");
        return htmlImageLink;
    }

    private LocalFile actualImageFile(String actualDot) throws IOException {
        final String actuals = "tempActuals";
        String actualName = actuals + "/actual" + random.nextInt(999999);
        FitLibraryFixture.getLocalFile(actuals).mkdir();
        File dotFile = FitLibraryFixture.getLocalFile(actualName + ".dot").getFile();
        FileWriter writer = new FileWriter(dotFile);
        writer.write(actualDot);
        writer.close();
        LocalFile imageFileName = FitLibraryFixture.getLocalFile(actualName + ".gif");
        File imageFile = imageFileName.getFile();
        Process process = Runtime.getRuntime().exec("dot -Tgif \"" + dotFile.getAbsolutePath() + "\" -o \"" + imageFile.getAbsolutePath() + "\"");
        try {
            process.waitFor();
        } catch (InterruptedException e1) {
            throw new RuntimeException("Dot process timed out.");
        }
        if (process.exitValue() != 0) throw new RuntimeException("Problems with actual Dot:\n" + actualDot);
        return imageFileName;
    }

    private static String getDot(LocalFile file) {
        return getFileContents(file.withSuffix("dot").getFile());
    }

    private static String getFileContents(File file) {
        FileReader reader;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            return new String(chars);
        } catch (IOException ex) {
            throw new RuntimeException("Problem reading " + file.getAbsolutePath() + ": " + ex);
        }
    }
}
