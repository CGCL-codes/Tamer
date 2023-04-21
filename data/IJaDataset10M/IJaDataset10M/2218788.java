package net.sourceforge.rules.compiler.drools.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Various utility methods for processing command line arguments.
 * 
 * @version $Revision: 680 $ $Date: 2010-06-02 18:59:48 -0400 (Wed, 02 Jun 2010) $
 * @author <a href="mailto:rlangbehn@users.sourceforge.net">Rainer Langbehn</a>
 */
public class CommandLine {

    /**
	 * Process Win32-style command files for the specified command line
	 * arguments and return the resulting arguments. A command file argument
	 * is of the form '@file' where 'file' is the name of the file whose
	 * contents are to be parsed for additional arguments. The contents of
	 * the command file are parsed using StreamTokenizer and the original
	 * '@file' argument replaced with the resulting tokens. Recursive command
	 * files are not supported. The '@' character itself can be quoted with
	 * the sequence '@@'.
	 *
	 * @param args The command line parameters.
	 * @return
	 * @throws IOException
	 */
    public static String[] parse(String[] args) throws IOException {
        List<String> newArgs = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.length() > 1 && arg.charAt(0) == '@') {
                arg = arg.substring(1);
                if (arg.charAt(0) == '@') {
                    newArgs.add(arg);
                } else {
                    loadCmdFile(arg, newArgs);
                }
            } else {
                newArgs.add(arg);
            }
        }
        return newArgs.toArray(new String[newArgs.size()]);
    }

    /**
	 * TODO
	 *
	 * @param name
	 * @param args
	 * @throws IOException
	 */
    private static void loadCmdFile(String name, List<String> args) throws IOException {
        Reader r = new BufferedReader(new FileReader(name));
        StreamTokenizer st = new StreamTokenizer(r);
        st.resetSyntax();
        st.wordChars(' ', 255);
        st.whitespaceChars(0, ' ');
        st.commentChar('#');
        st.quoteChar('"');
        st.quoteChar('\'');
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            args.add(st.sval);
        }
        r.close();
    }
}
