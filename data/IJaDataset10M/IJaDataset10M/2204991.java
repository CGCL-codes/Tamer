package net.sf.worldsaver.names.filters;

import java.io.*;
import java.util.*;
import net.sf.worldsaver.names.*;

/**
 * This filter prooves the end of the Strings. If a String ends with a filterstring,
 * isValid() returns false. The fileformat of this Filter is a simple textfile with the
 * filters separated by newlines.
 *@author Andreas Schmitz
 *@version August 14th 2001
 */
public class EndFilter extends SubstringFilter implements Filter {

    public EndFilter(File file) throws IOException {
        super(file);
    }

    public EndFilter() {
        super();
    }

    /**
     * Creates new filters. This method reads a textfile from `filename' and creates some
     * random names of the given length. For each created name it prooves, if there is any
     * word in the textfile that ends with the generated name. If there is none, a new
     * filter is found. It is of course best, if only very small names are generated (eg,
     * two or three character names), because the probability that a long, randomly
     * generated name is in the textfile, is very small. This filter is useful to filter
     * out names that end with `yy' or something like that.
     *@param names the NameGenerator object that is used to generate random names.
     *@param file the textfile.
     *@param length the length of randomly generated names.
     *@param number the number of random names that should be generated.
     *@throws IOException guess, when.
     */
    public void createFilters(final NameGenerator names, File file, final int length, final int number) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
        while (in.ready()) sb.append(in.readLine());
        in.close();
        final StringTokenizer t = new StringTokenizer(sb.toString());
        final String[] words = new String[t.countTokens()];
        progress = 0;
        maxlength = number;
        new Thread() {

            public void run() {
                int cnt = 0;
                while (t.hasMoreTokens()) words[cnt++] = t.nextToken();
                for (int i = 0; i < number; i++) {
                    progress++;
                    String n = names.getName(length);
                    boolean foundone = false;
                    for (int j = 0; j < words.length; j++) {
                        if (words[j].endsWith(n)) {
                            foundone = true;
                            break;
                        }
                    }
                    if (!foundone) filters.add(n);
                }
            }
        }.start();
    }

    public boolean isValid(String name) {
        Iterator i = filters.iterator();
        while (i.hasNext()) if (name.endsWith(i.next().toString())) return false;
        return true;
    }

    public String toString() {
        return lang.get("endfilter");
    }
}
