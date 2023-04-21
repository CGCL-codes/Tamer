package uk.org.ogsadai.parser.sql92query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.antlr.runtime.tree.CommonTree;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.test.TestHelper;

/**
 * Tests the SQLQueryParser.
 * 
 * @author The OGSA-DAI Project Team.
 * 
 */
public class SQLQueryParserTest extends TestCase {

    private Set<Integer> mKnownFails = new HashSet<Integer>(Arrays.asList(new Integer[] { 55, 56, 57, 58, 59, 60, 61, 77, 78, 79, 80, 81, 82, 89, 90, 110, 111, 112 }));

    /**
     * Constructor
     * 
     * @param name
     */
    public SQLQueryParserTest(String name) {
        super(name);
    }

    public void testAndNoExceptions() throws Exception {
        SQLQueryParser p = SQLQueryParser.getInstance();
        String condSql = "AIRCRAFT.aid = @AIRCRAFT_COPY.aid AND AIRCRAFT.aid = AIRCRAFT_COPY.aid CERTIFIED_COPY.gid = @CERTIFIED.gid";
        try {
            p.parseSQLForCondition(condSql);
            fail("SQLParserException expected");
        } catch (SQLParserException e) {
        }
        CommonTree ct = p.parseSQL("SELECT name, authorname, dat FROM series WHERE name='Dresden'");
        System.out.println(p.generateSQL(ct));
    }

    /**
     * Tests a number of queries.
     * 
     * @throws Exception
     */
    public void testParser() throws Exception {
        List<String> errorList = new ArrayList<String>();
        SQLQueryParser p = SQLQueryParser.getInstance();
        File f = TestHelper.getFileFromClassName(getClass(), "queries.txt");
        BufferedReader buffReader = new BufferedReader(new FileReader(f));
        int knownQueryCnt = 0;
        String nextLine;
        boolean known;
        while ((nextLine = buffReader.readLine()) != null) {
            known = false;
            String[] lineSplit = nextLine.split("\\|");
            String origQuery = lineSplit[1];
            int origQueryIdx = Integer.parseInt(lineSplit[0]);
            CommonTree origQueryTree = null;
            try {
                origQueryTree = p.parseSQL(origQuery);
            } catch (Exception e) {
                if (mKnownFails.contains(origQueryIdx)) {
                    known = true;
                    knownQueryCnt++;
                }
                errorList.add(generateErrorReport(origQuery, origQueryIdx, e.getCause().getMessage(), known));
                continue;
            }
            String walkedQuery = null;
            try {
                walkedQuery = p.generateSQL(origQueryTree);
            } catch (Exception e) {
                if (mKnownFails.contains(origQueryIdx)) {
                    known = true;
                    knownQueryCnt++;
                }
                errorList.add(generateErrorReport(origQuery, origQueryIdx, e.getCause().getMessage(), known));
                continue;
            }
            CommonTree walkedQueryTree = null;
            try {
                walkedQueryTree = p.parseSQL(walkedQuery);
            } catch (Exception e) {
                if (mKnownFails.contains(origQueryIdx)) {
                    known = true;
                    knownQueryCnt++;
                }
                errorList.add(generateErrorReport(origQuery, origQueryIdx, e.getCause().getMessage(), known));
                continue;
            }
            try {
                p.generateSQL(walkedQueryTree);
            } catch (Exception e) {
                if (mKnownFails.contains(origQueryIdx)) {
                    known = true;
                    knownQueryCnt++;
                }
                errorList.add(generateErrorReport(origQuery, origQueryIdx, e.getCause().getMessage(), known));
                continue;
            }
            if (!origQueryTree.toStringTree().equals(walkedQueryTree.toStringTree())) {
                if (mKnownFails.contains(origQueryIdx)) {
                    known = true;
                    knownQueryCnt++;
                }
                errorList.add(generateErrorReport(origQuery, origQueryIdx, "ORIGINAL AND WALKED QUERIES NOT EQUAL", known));
            }
        }
        buffReader.close();
        if (errorList.size() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("FAILED " + errorList.size() + " QUERIES WHERE " + knownQueryCnt + " KNOWN\n");
            for (String s : errorList) {
                sb.append(s);
            }
            if (errorList.size() == knownQueryCnt) {
                fail("KNOWN: " + sb.toString());
            } else {
                fail(sb.toString());
            }
        }
    }

    /**
     * Generate error report string for a query.
     * 
     * @param origQ
     *            original query
     * @param qIdx
     *            query index
     * @param message
     *            failure message
     * @param known
     *            whether the failure is known
     * @return error report string
     */
    private String generateErrorReport(String origQ, int qIdx, String message, boolean known) {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + (known ? "KNOWN " : "") + qIdx + "]" + " ORIG: ").append(origQ).append('\n');
        sb.append("+ MESG: ").append((message == null) ? "NONE" : message).append('\n');
        return sb.toString();
    }
}
