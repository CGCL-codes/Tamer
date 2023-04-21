package ch.ethz.mxquery.model.ft;

import ch.ethz.mxquery.util.Hashtable;
import java.util.Vector;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.adm.AllMatch;
import ch.ethz.mxquery.datamodel.adm.Match;
import ch.ethz.mxquery.datamodel.adm.StringMatch;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.exceptions.StaticException;

public class FTScope extends FTPositional {

    int scopeType = 0;

    QueryLocation loc;

    public FTScope(int scope, String unit, QueryLocation loc) throws StaticException {
        super(unit);
        scopeType = scope;
        this.loc = loc;
    }

    public AllMatch checkPosConstraint(AllMatch am) throws MXQueryException {
        Vector resMatches = new Vector();
        Match[] matches = am.getMatches();
        for (int i = 0; i < matches.length; i++) {
            Match mat = matches[i];
            if (scopeType == POS_SCOPE_SAME) {
                int myScope = 0;
                boolean isSameSentence = true;
                StringMatch[] smi = mat.getIncludes();
                for (int j = 0; j < smi.length; j++) {
                    StringMatch sm = smi[j];
                    int startScope;
                    int endScope;
                    if (ftUnit.equals("sentence")) {
                        startScope = sm.getStartSentence();
                        endScope = sm.getEndSentence();
                    } else {
                        startScope = sm.getStartParagraph();
                        endScope = sm.getEndParagraph();
                    }
                    if (j == 0) myScope = startScope;
                    if (startScope != endScope || startScope != myScope) {
                        isSameSentence = false;
                        break;
                    }
                }
                if (isSameSentence) {
                    Vector retainedExcl = new Vector();
                    StringMatch[] sme = mat.getExcludes();
                    if (sme != null) for (int j = 0; j < sme.length; j++) {
                        StringMatch sm = smi[j];
                        int startScope;
                        int endScope;
                        if (ftUnit.equals("sentence")) {
                            startScope = sm.getStartSentence();
                            endScope = sm.getEndSentence();
                        } else {
                            startScope = sm.getStartParagraph();
                            endScope = sm.getEndParagraph();
                        }
                        if (startScope == endScope && startScope == myScope) {
                            retainedExcl.addElement(sme[j]);
                            break;
                        }
                    }
                    StringMatch[] newExcl = null;
                    if (retainedExcl.size() > 0) {
                        newExcl = new StringMatch[retainedExcl.size()];
                        for (int j = 0; j < newExcl.length; j++) newExcl[j] = (StringMatch) retainedExcl.elementAt(j);
                    }
                    resMatches.addElement(new Match(smi, newExcl));
                }
            } else {
                boolean isDifferentSentence = true;
                Hashtable seenScopes = new Hashtable();
                StringMatch[] smi = mat.getIncludes();
                for (int j = 0; j < smi.length; j++) {
                    StringMatch sm = smi[j];
                    int startScope;
                    int endScope;
                    if (ftUnit.equals("sentence")) {
                        startScope = sm.getStartSentence();
                        endScope = sm.getEndSentence();
                    } else {
                        startScope = sm.getStartParagraph();
                        endScope = sm.getEndParagraph();
                    }
                    for (int k = j + 1; k < smi.length; k++) {
                        int startScope2;
                        int endScope2;
                        StringMatch sm2 = smi[k];
                        if (ftUnit.equals("sentence")) {
                            startScope2 = sm2.getStartSentence();
                            endScope2 = sm2.getEndSentence();
                        } else {
                            startScope2 = sm2.getStartParagraph();
                            endScope2 = sm2.getEndParagraph();
                        }
                        if (!(startScope != startScope2 || startScope != endScope || startScope2 != endScope2)) {
                            isDifferentSentence = false;
                        }
                    }
                }
                if (smi.length == 1) isDifferentSentence = false;
                if (isDifferentSentence) {
                    Vector retainedExcl = new Vector();
                    StringMatch[] sme = mat.getExcludes();
                    if (sme != null) for (int j = 0; j < sme.length; j++) {
                        StringMatch sm = smi[j];
                        int startScope;
                        int endScope;
                        if (ftUnit.equals("sentence")) {
                            startScope = sm.getStartSentence();
                            endScope = sm.getEndSentence();
                        } else {
                            startScope = sm.getStartParagraph();
                            endScope = sm.getEndParagraph();
                        }
                        if (!seenScopes.contains(new Integer(startScope)) || !seenScopes.contains(new Integer(endScope))) {
                            retainedExcl.addElement(sme[j]);
                            break;
                        }
                    }
                    StringMatch[] newExcl = null;
                    if (retainedExcl.size() > 0) {
                        newExcl = new StringMatch[retainedExcl.size()];
                        for (int j = 0; j < newExcl.length; j++) newExcl[j] = (StringMatch) retainedExcl.elementAt(j);
                    }
                    resMatches.addElement(new Match(smi, newExcl));
                }
            }
        }
        if (resMatches.size() > 0) {
            AllMatch resAm = new AllMatch(resMatches);
            return resAm;
        }
        return null;
    }

    public FTPositional copy(Context ctx, Vector nestedPredCtxStack) throws MXQueryException {
        return new FTScope(scopeType, ftUnit, loc);
    }

    public void reset() throws MXQueryException {
    }

    public void setContext(Context ctx) throws MXQueryException {
    }

    public void setResettable(boolean r) throws MXQueryException {
    }
}
