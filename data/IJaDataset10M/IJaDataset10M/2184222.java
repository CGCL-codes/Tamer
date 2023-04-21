package net.sf.jour.filter;

import net.sf.jour.log.Logger;

/**
 * TODO Add docs
 * 
 * Created on 05.12.2004 Contributing Author(s):
 * 
 * Misha Lifschitz <mishalifschitz at users.sourceforge.net> (Inital
 * implementation) Vlad Skarzhevskyy <vlads at users.sourceforge.net> (Inital
 * implementation)
 * 
 * @author vlads
 * @version $Revision: 46 $ ($Author: vlads $) $Date: 2006-11-19 16:52:09 -0500
 *          (Sun, 19 Nov 2006) $
 */
public class PointcutParamsFilter extends MatchStringListFilter {

    protected static final Logger log = Logger.getLogger();

    boolean matchAny = false;

    PointcutParamsFilter nextParam = null;

    public PointcutParamsFilter() {
    }

    public PointcutParamsFilter(String params) {
        setParams(params);
    }

    public void debug() {
        log.debug("matchAny " + matchAny);
        log.debug("has nextParam " + (nextParam != null));
        super.debug();
    }

    public void setParams(String params) {
        params = params.trim();
        if (params.equals("..")) {
            this.matchAny = true;
            return;
        }
        int idx = params.indexOf(",");
        if (idx == -1) {
            super.addPatterns(params);
            return;
        }
        String firstParam = params.substring(0, idx);
        super.addPatterns(firstParam);
        this.nextParam = new PointcutParamsFilter(params.substring(idx + 1));
    }

    public int matchState(Object obj) {
        if (debug) {
            debug();
        }
        if (obj instanceof String) {
            return super.matchState(obj);
        }
        if (!(obj instanceof String[])) {
            return MATCH_NO;
        }
        if (matchAny) {
            return MATCH_YES;
        }
        String[] params = (String[]) obj;
        if (params.length == 0) {
            if (isEmpty()) {
                return MATCH_YES;
            } else {
                return MATCH_NO;
            }
        }
        if (!super.match(params[0])) {
            return MATCH_NO;
        }
        if (this.nextParam == null) {
            return MATCH_YES;
        }
        String[] nextParams = new String[params.length - 1];
        for (int i = 1; i < params.length; i++) {
            nextParams[i - 1] = params[i];
        }
        return this.nextParam.matchState(nextParams);
    }
}
