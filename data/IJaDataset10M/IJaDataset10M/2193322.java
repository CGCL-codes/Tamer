package net.sf.jour.instrumentor;

import net.sf.jour.InterceptorException;
import net.sf.jour.filter.PointcutListFilter;

/**
 *
 * Contributing Author(s):
 *
 *   Misha Lifschitz <mishalifschitz at users.sourceforge.net> (Inital implementation)
 *   Vlad Skarzhevskyy <vlads at users.sourceforge.net> (Inital implementation)
 *
 * @author michaellif
 * @version $Revision: 43 $ ($Author: vlads $) $Date: 2007-08-28 13:06:19 -0400 (Tue, 28 Aug 2007) $
 */
public class InstrumentorFactory {

    public static Instrumentor createInstrumentor(String instrumentor, PointcutListFilter pointcuts) throws InterceptorException {
        Instrumentor instr = null;
        if (instrumentor == null) {
            throw new InterceptorException("Instrumentor is NULL");
        }
        try {
            instr = (Instrumentor) Class.forName(instrumentor).newInstance();
        } catch (Exception e) {
            throw new InterceptorException("Failed to instantiate " + instrumentor + "instrumentor.", e);
        }
        instr.setPointcuts(pointcuts);
        return instr;
    }
}
