package examples;

import java.io.*;
import org.apache.oro.text.*;
import org.apache.oro.text.regex.*;

/**
 * This is a simple example program showing how to use the MatchActionProcessor
 * class.  It reads the provided semi-colon delimited file semicolon.txt and
 * outputs only the second column to standard output.
 *
 * @version @version@
 */
public final class semicolon {

    public static final void main(String[] args) {
        MatchActionProcessor processor = new MatchActionProcessor();
        try {
            processor.setFieldSeparator(";");
            processor.addAction(null, new MatchAction() {

                public void processMatch(MatchActionInfo info) {
                    info.output.println(info.fields.get(1));
                }
            });
        } catch (MalformedPatternException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            processor.processMatches(new FileInputStream("semicolon.txt"), System.out);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
