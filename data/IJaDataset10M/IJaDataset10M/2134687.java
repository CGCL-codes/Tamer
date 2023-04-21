package com.f4.hotelf4;

import com.f4.hotelf4.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("hospedeList")
public class HospedeList extends EntityQuery<Hospede> {

    private static final String EJBQL = "select hospede from Hospede hospede";

    private static final String[] RESTRICTIONS = { "lower(hospede.hoCpf) like concat(lower(#{hospedeList.hospede.hoCpf}),'%')", "lower(hospede.hoNome) like concat(lower(#{hospedeList.hospede.hoNome}),'%')" };

    private Hospede hospede = new Hospede();

    public HospedeList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
    }

    public Hospede getHospede() {
        return hospede;
    }
}
