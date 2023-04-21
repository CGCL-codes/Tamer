package org.domain.siplacad5.session;

import org.domain.siplacad5.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.List;
import java.util.Arrays;

@Name("aulaList")
public class AulaList extends EntityQuery {

    private static final String[] RESTRICTIONS = { "lower(aula.codigo) like concat(lower(#{aulaList.aula.codigo}),'%')", "lower(aula.edificio) like concat(lower(#{aulaList.aula.edificio}),'%')" };

    private Aula aula = new Aula();

    @Override
    public String getEjbql() {
        return "select aula from Aula aula";
    }

    @Override
    public Integer getMaxResults() {
        return 25;
    }

    public Aula getAula() {
        return aula;
    }

    @Override
    public List<String> getRestrictions() {
        return Arrays.asList(RESTRICTIONS);
    }
}
