package com.amidasoft.lincat.session;

import com.amidasoft.lincat.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tipusFormacionsList")
public class TipusFormacionsList extends EntityQuery<TipusFormacions> {

    private static final String EJBQL = "select tipusFormacions from TipusFormacions tipusFormacions";

    private static final String[] RESTRICTIONS = { "lower(tipusFormacions.descripcio) like concat(lower(#{tipusFormacionsList.tipusFormacions.descripcio}),'%')" };

    private TipusFormacions tipusFormacions = new TipusFormacions();

    public TipusFormacionsList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
    }

    public TipusFormacions getTipusFormacions() {
        return tipusFormacions;
    }
}
