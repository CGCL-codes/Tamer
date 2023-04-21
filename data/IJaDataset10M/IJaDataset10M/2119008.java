package com.siegre.action;

import com.siegre.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

/**
 * Clase fuespersonaList, session bean que permite listar registros de una tabla 
 *
 * @author Rafael Ortega
 * @author Mayer Monsalve
 * @version 1.0 Build 10 Diciembre 15, 2011
 */
@Name("fuespersonaList")
public class FuespersonaList extends EntityQuery<Fuespersona> {

    private static final long serialVersionUID = 7690852953438626054L;

    private static final String EJBQL = "select fuespersona from Fuespersona fuespersona";

    private static final String[] RESTRICTIONS = { "lower(fuespersona.fupeRegistradopor) like lower(concat(#{fuespersonaList.fuespersona.fupeRegistradopor},'%'))" };

    private Fuespersona fuespersona = new Fuespersona();

    public FuespersonaList() {
        setEjbql(EJBQL);
        setMaxResults(20);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Fuespersona getFuespersona() {
        return fuespersona;
    }
}
