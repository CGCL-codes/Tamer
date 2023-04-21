package net.sourceforge.esw.ant;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import net.sourceforge.esw.collection.*;
import net.sourceforge.esw.graph.*;

/**
 * Based on the specified file, will read in the contents of the file and remove
 * every line that starts with "/* {{<< BEGIN REPLACEMENT >>}}" and will also
 * change the "${EXP_DATE}" with a date one month ahead of today.
 * <p>
 *
 */
public class ChangeCodeForEval extends Task {

    protected String filename;

    private static final String DELIMITER_BEGIN = "{{<< BEGIN REPLACEMENT >>}}";

    private static final String DELIMITER_END = "{{<< END REPLACEMENT >>}}";

    private static final String EXP_DATE = "${EXP_DATE}";

    /****************************************************************************
   * Sets the filename attribute.
   */
    public void setFilename(String aFilename) {
        filename = aFilename;
    }

    /****************************************************************************
   * Executes the task for this ChangeCodeForEval instance.
   */
    public void execute() throws BuildException {
        try {
            String newLine = System.getProperty("line.separator");
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuffer sb = new StringBuffer(10000);
            String line = reader.readLine();
            while (null != line) {
                if (line.indexOf(DELIMITER_BEGIN) == -1 && line.indexOf(DELIMITER_END) == -1) {
                    int index = line.indexOf(EXP_DATE);
                    if (index != -1) {
                        sb.append(line.substring(0, index));
                        sb.append(getOneMonthInAdvanceAtMidnight());
                        sb.append("L");
                        sb.append(line.substring(index + EXP_DATE.length(), line.length()));
                    } else {
                        sb.append(line);
                    }
                    sb.append(newLine);
                }
                line = reader.readLine();
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            System.out.println("Expiration Date: " + new Date(getOneMonthInAdvanceAtMidnight()).toString());
            project.setProperty("EXPIRATION_DATE", new Date(getOneMonthInAdvanceAtMidnight()).toString());
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private long getOneMonthInAdvanceAtMidnight() {
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MONTH, 1);
        rightNow.add(Calendar.DATE, 1);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.SECOND, 0);
        rightNow.set(Calendar.MILLISECOND, 0);
        return rightNow.getTime().getTime();
    }
}
