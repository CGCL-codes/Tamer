package ubc.lersse.sqlia.preventer;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Token;
import com.p6spy.engine.logging.appender.P6Logger;
import ubc.lersse.sqlia.SQLPreventLexer;
import ubc.lersse.sqlia.TaintUtils;

/**
 * OVERVIEW: A SQLPreventerWithTaint is a SQLPreventer that uses String taint tracking to signal a SQL injection
 * 			 attack if any tainted data appears in a non-literal inside SQL statement.
 * 
 * @author levistoddard
 */
public final class SQLPreventerWithTaint extends SQLPreventerBase {

    public SQLPreventerWithTaint(P6Logger logger, boolean signalMalicious, boolean displayHTML) {
        super(logger, signalMalicious, displayHTML);
    }

    protected DetectionResult doCheckQuery(HttpServletRequest req, String sql) throws CheckQueryFailedException {
        try {
            AlgorithmSpecificData toLog = new AlgorithmSpecificData();
            boolean ttConformity = passesTokenTypeConformity(sql);
            return new DetectionResult(ttConformity, toLog);
        } catch (LogDataException e) {
            throw new CheckQueryFailedException(getClass().getName() + ".doCheckQuery(HttpServletRequest, String): unable to log data: " + e.toString());
        }
    }

    /**
	 * EFFECTS: Returns true if all tainted data in {@code sql} is contained within numeric/string
	 * 			literal tokens.
	 */
    private boolean passesTokenTypeConformity(String sql) throws CheckQueryFailedException {
        try {
            char[] taint = TaintUtils.getTaintArray(sql);
            if (!TaintUtils.isTainted(taint, 0, taint.length)) {
                return true;
            }
            ANTLRStringStream input = new ANTLRStringStream(sql);
            SQLPreventLexer lexer = new SQLPreventLexer(input);
            CommonTokenStream stream = new CommonTokenStream(lexer);
            List tokens = stream.getTokens();
            Iterator itr = tokens.iterator();
            while (itr.hasNext()) {
                Token token = (Token) itr.next();
                String text = token.getText();
                int index = token.getCharPositionInLine();
                if (TaintUtils.isTainted(taint, index, index + text.length())) {
                    int tokenType = token.getType();
                    if ((tokenType != SQLPreventLexer.CHAR_STRING) && (tokenType != SQLPreventLexer.EXACT_NUM_LIT)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (InvocationTargetException e) {
            throw new CheckQueryFailedException(getClass().getName() + ".passesTokenTypeConformity(String): unable to get taint.");
        }
    }
}
