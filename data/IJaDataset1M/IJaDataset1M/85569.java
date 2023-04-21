package com.siegre.action;

import com.siegre.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

/**
 * Clase amistadList, session bean que permite listar registros de una tabla 
 *
 * @author Rafael Ortega
 * @author Mayer Monsalve
 * @version 1.0 Build 10 Diciembre 15, 2011
 */
@Name("amistadList")
public class AmistadList extends EntityQuery<Amistad> {

    private static final long serialVersionUID = -8254138232005250714L;

    private static final String EJBQL = "select amistad from Amistad amistad";

    private static final String[] RESTRICTIONS = { "lower(amistad.amisEstado) like lower(#{amistadList.amistad.amisEstado})", "amistad.egresadoEnvia.egreId = #{amistadList.amistad.egresadoEnvia.egreId}", "amistad.egresadoRecibe.egreId = #{amistadList.amistad.egresadoRecibe.egreId}" };

    private Amistad amistad = new Amistad();

    public AmistadList() {
        setEjbql(EJBQL);
        setMaxResults(20);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Amistad getAmistad() {
        return amistad;
    }
}
