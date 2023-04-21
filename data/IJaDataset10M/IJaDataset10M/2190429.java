package org.objectstyle.wolips.core.resources.pattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.objectstyle.wolips.core.CorePlugin;

/**
 * @author ulrich
*/
public class PatternsetReader {

    private String[] pattern = new String[0];

    public PatternsetReader(IFile patternset) {
        ArrayList patternList = new ArrayList();
        BufferedReader patternReader = null;
        try {
            patternReader = new BufferedReader(new FileReader(new File(patternset.getLocation().toOSString())));
            String line = patternReader.readLine();
            while (line != null) {
                if (line.length() > 0) {
                    patternList.add(line);
                }
                line = patternReader.readLine();
            }
        } catch (IOException ioe) {
            CorePlugin.getDefault().log(ioe);
        } finally {
            if (null != patternReader) {
                try {
                    patternReader.close();
                } catch (IOException ioe) {
                }
            }
        }
        pattern = (String[]) patternList.toArray(new String[patternList.size()]);
    }

    /**
	 * @param pattern
	 */
    public PatternsetReader(String[] pattern) {
        super();
        this.pattern = pattern;
    }

    /**
	 * @return Returns the pattern.
	 */
    public String[] getPattern() {
        return pattern;
    }
}
