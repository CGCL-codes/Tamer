package com.io_software.catools.search.ranking;

import java.util.Vector;
import java.util.Enumeration;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.HashSet;

/** Decides whether or not a query is applicable. Only RankingQuery objects are considered. Furthermore, the RankingProduction contains another Production telling what other "more basic" query types a source supports. The decorated (inner) query of the ranking query must be matched by this inner production of the RankingProduction.

    @version $Id: RankingProduction.java,v 1.5 2001/01/15 09:45:08 aul Exp $
  */
public class RankingProduction implements com.io_software.catools.search.capability.Production {

    /** 
      */
    public RankingProduction(com.io_software.catools.search.capability.Production innerProduction) throws java.rmi.RemoteException {
        setDecoratedProduction(innerProduction);
    }

    /** The decorated (inner) query of the ranking query must be matched by this inner production of the RankingProduction.
Subclasses may use this method and add special constraints for the comparator that the passed ranking query contains, e.g. not allowing certain types of comparators.
      */
    public boolean matches(com.io_software.catools.search.Query query) {
        boolean result = false;
        if (query instanceof RankingQuery) {
            RankingQuery rq = (RankingQuery) query;
            result = getDecoratedProduction().matches(rq.getDecoratedQuery());
        }
        return result;
    }

    /** a production of this kind assembles RankingQuery objects and whatever query types the contained production for the decorated query uses.
      */
    public java.util.Set getQueryTypes() {
        Set result = new HashSet();
        result.add(RankingQuery.class);
        result.addAll(getDecoratedProduction().getQueryTypes());
        return result;
    }

    /** Getter for role DecoratedProduction */
    public com.io_software.catools.search.capability.Production getDecoratedProduction() {
        return m_decoratedProduction;
    }

    /** Setter for role DecoratedProduction */
    public void setDecoratedProduction(com.io_software.catools.search.capability.Production rel) throws RemoteException {
        m_decoratedProduction = rel;
    }

    /** 
      */
    private transient com.io_software.catools.search.capability.Production m_decoratedProduction = null;
}
