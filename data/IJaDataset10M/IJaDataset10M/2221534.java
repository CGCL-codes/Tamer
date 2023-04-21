package saadadb.query.constbuilders;

import static saadadb.query.parser.SaadaQLRegex.FacWS;
import static saadadb.query.parser.SaadaQLRegex.MATCH_PATTERN;
import static saadadb.query.parser.SaadaQLRegex.WHERE_CLAUSE_LD;
import static saadadb.query.parser.SaadaQLRegex.WHERE_CLAUSE_RD;
import static saadadb.query.parser.SaadaQLRegex.WHERE_RELATION;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import saadadb.exceptions.QueryException;
import saadadb.exceptions.SaadaException;
import saadadb.meta.VOResource;

/**
 * @author F.X. Pineau
 */
public final class WhereRelation {

    private static final String syntax = "WhereRelation { matchPattern [...] }";

    private static final String inSecStat = "[^\\{\\}]+(?:[^\\{\\}]+\\{[^\\{\\}]+\\})*";

    private static final String inStatSimple = MATCH_PATTERN + FacWS + WHERE_CLAUSE_LD + FacWS + "(" + inSecStat + ")" + FacWS + WHERE_CLAUSE_RD;

    private static final String inStat = "(?:" + inStatSimple + FacWS + ")*(?:" + inStatSimple + ")";

    private static final String regex = WHERE_RELATION + FacWS + WHERE_CLAUSE_LD + FacWS + inStat + FacWS + WHERE_CLAUSE_RD;

    private static final Pattern pattern = Pattern.compile(regex);

    private String strMatch;

    private MatchPattern[] patterns;

    private VOResource vor;

    public WhereRelation(String strQuery, VOResource vor) throws SaadaException {
        this.vor = vor;
        this.parse(strQuery);
    }

    public final String getStrMatch() {
        return this.strMatch;
    }

    public final MatchPattern[] getPattern() {
        return this.patterns;
    }

    public static final boolean isIn(String strQ) {
        return pattern.matcher(strQ).find();
    }

    /**
	 * @param strQ
	 * @throws SaadaException
	 */
    private final void parse(String strQ) throws SaadaException {
        Matcher m = pattern.matcher(strQ);
        if (m.find()) {
            List<MatchPattern> al = new ArrayList<MatchPattern>();
            this.strMatch = m.group(0);
            Matcher m2 = Pattern.compile(inStatSimple).matcher(m.group(0));
            while (m2.find()) {
                al.add(new MatchPattern(m2.group(1), this.vor));
            }
            if (al.size() > 0) {
                this.patterns = al.toArray(new MatchPattern[0]);
            } else {
                QueryException.throwNewException(SaadaException.SYNTAX_ERROR, "In WhereRelation... Humm... I don't understand! This is a regex error not supposed to happen!");
            }
        } else {
            QueryException.throwNewException(SaadaException.SYNTAX_ERROR, "Error parsing the WhereRelation clause!\nCheck the syntax \"" + syntax + "\"");
        }
    }
}
