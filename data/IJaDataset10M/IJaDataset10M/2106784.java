package org.easyrec.utils.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for parsing an sql file. The statements from the sql file are
 * returned as a <code>List</code> of <code>String</code>. The utility is
 * meant for use with batch sql functions.
 * <p>
 * The class that can be used to execute an sql comfortably is
 * {@link at.researchstudio.sat.utio.store.service.sqlscript.impl.SqlScriptServiceImpl}. It relies
 * on this class for sql file parsing.
 * </p>
 * <p/>
 * Note: <br />
 * This parser will not be able to handle cases where
 * <code>;</code>, <code>//</code>, <code>#</code>, <code>--</code> occur in
 * string literals, i.e inside <code>' ... '</code>
 * <p/>
 * <p><b>Company:&nbsp;</b>
 * SAT, Research Studios Austria</p>
 * <p/>
 * <p><b>Copyright:&nbsp;</b>
 * (c) 2006</p>
 * <p/>
 * <p><b>last modified:</b><br/>
 * $Author: sat-rsa $<br/>
 * $Date: 2011-08-12 12:40:54 -0400 (Fri, 12 Aug 2011) $<br/>
 * $Revision: 119 $</p>
 *
 * @author Florian Kleedorfer
 */
public class SqlFileParser {

    private BufferedReader reader;

    private final Log logger = LogFactory.getLog(getClass());

    public SqlFileParser(BufferedReader reader) {
        this.reader = reader;
    }

    public SqlFileParser(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    /**
     * creates a new parser object for the given file
     *
     * @param filename
     * @throws IllegalArgumentException if the file does not exist
     */
    public SqlFileParser(File sqlScript) {
        try {
            this.reader = new BufferedReader(new FileReader(sqlScript));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("file not found " + sqlScript);
        }
    }

    /**
     * creates a new parser object for the given filename
     *
     * @param filename
     * @throws IllegalArgumentException if the file does not exist
     */
    public SqlFileParser(String filename) {
        try {
            this.reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("file not found " + filename);
        }
    }

    /**
     * creates a new parser object for the given input steam
     *
     * @param stream
     */
    public SqlFileParser(InputStream stream) {
        this.reader = new BufferedReader(new InputStreamReader(stream));
    }

    public List<String> parse() {
        String line = null;
        int lineCounter = 0;
        StringBuilder statement = new StringBuilder();
        List<String> statements = new LinkedList<String>();
        Pattern commentPattern = Pattern.compile("(//|#|--)+.*$");
        try {
            while ((line = reader.readLine()) != null) {
                lineCounter++;
                Matcher m = commentPattern.matcher(line);
                if (m.find()) {
                    line = line.substring(0, m.start());
                }
                statement.append(" ");
                line = statement.append(line).toString();
                line = line.replaceAll("\\s+", " ");
                line = line.trim();
                String[] tokens = line.split(";");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim();
                }
                boolean containsSemicolon = line.contains(";");
                boolean endsWithSemicolon = line.endsWith(";");
                if (!containsSemicolon) {
                    continue;
                }
                if (tokens.length == 1 && endsWithSemicolon) {
                    statements.add(tokens[0]);
                    statement = new StringBuilder();
                    continue;
                }
                for (int i = 0; i < tokens.length - 1; i++) {
                    statements.add(tokens[0]);
                    statement = new StringBuilder();
                }
                if (endsWithSemicolon) {
                    statements.add(tokens[0]);
                    statement = new StringBuilder();
                } else {
                    statement = new StringBuilder();
                    statement.append(tokens[tokens.length - 1]);
                }
            }
            if (statement != null && statement.toString().trim().length() > 0) throw new UnclosedStatementException("Statement is not closed until the end of the file.");
        } catch (IOException e) {
            logger.warn(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return statements;
    }
}
