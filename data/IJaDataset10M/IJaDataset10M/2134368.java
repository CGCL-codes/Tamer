package org.cyberaide.gridshell2.commandlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.cyberaide.gridshell2.CommandletBase;
import org.cyberaide.gridshell2.Context;
import org.cyberaide.gridshell2.util.CliValues;

/** Cat returns the contents of a file.
 * 
 * If the file is not present locally it is copied to a local temp-file first.
 *
 * @author boris
 */
public class Cat extends CommandletBase {

    @SuppressWarnings("static-access")
    public Cat() {
        cliOptions.addOptionOrdered(OptionBuilder.withArgName("file").withLongOpt("file").hasArgs(Option.UNLIMITED_VALUES).isRequired().withDescription("File(s) to cat").create("f"));
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    protected String work(CliValues args, Context context) {
        List<String> files = args.getMulti("file");
        StringBuffer output = new StringBuffer();
        for (String cur : files) {
            Reader r = null;
            File tempFile = null;
            try {
                tempFile = File.createTempFile("gridshell", ".tmp");
                if (cur.startsWith("gsiftp")) {
                    CommandletBase copy = new GridFTPCopy();
                    String[] a = { "--from", cur, "--to", "file://" + tempFile.getAbsolutePath() };
                    String result = copy.run(a);
                    if (!"Succeeded".equals(result)) {
                        logger.warning("Could not copy file " + cur);
                        continue;
                    }
                    cur = tempFile.getAbsolutePath();
                }
                r = new BufferedReader(new InputStreamReader(new FileInputStream(cur)));
                char[] buf = new char[1024];
                while (r.read(buf) != -1) {
                    output.append(buf);
                }
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, "File \"" + cur + "\" was not found.", ex);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (r != null) {
                        r.close();
                    }
                    if (tempFile != null) {
                        tempFile.delete();
                    }
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "IOException while closing Reader", ex);
                }
            }
        }
        return output.toString();
    }
}
