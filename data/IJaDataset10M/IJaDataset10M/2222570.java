package com.panopset.flywheel;

import static com.panopset.Util.saveStringToFile;
import java.io.File;
import java.io.StringWriter;

/**
 * <h5>f - File</h5>
 * 
 * <p>Example 1</p>
 * <pre>
 * ${&#064;f someFile.txt}
 * </pre>
 *
 * <p>Example 2</p>
 * <pre>
 * ${&#064;p fileName}someFile.txt${&#064;q}
 * ${&#064;f &#064;fileame}
 * </pre>
 *
 * Output to the specified file, until the end of the template or another
 * file command is found.
 * 
 * @author Karl Dinwiddie
 *
 */
public class CommandFile extends MatchableCommand {

    public static String getShortHTMLText() {
        return "${&#064;f somefile.txt}";
    }

    public CommandFile(String source, String innerPiece, Template template) {
        super(source, innerPiece, template);
    }

    @Override
    public void resolve(StringWriter sw) {
        if (getParams() == null) return;
        Flywheel.getFlywheel().setWriter(sw = new StringWriter());
        super.resolve(sw);
        saveStringToFile(sw.toString(), new File(Flywheel.getFlywheel().targetDirectory + FORWARD_SLASH + getParams()));
    }
}
