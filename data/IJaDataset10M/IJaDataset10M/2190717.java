package br.sysclinic.session.cadastro;

import br.sysclinic.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.List;
import java.util.Arrays;

@Name("planoList")
public class PlanoList extends EntityQuery<Plano> {

    private static final String[] RESTRICTIONS = { "lower(plano.nome) like concat('%',lower(#{planoList.plano.nome}),'%')", "lower(plano.observacao) like concat('%',lower(#{planoList.plano.observacao}),'%')" };

    private Plano plano = new Plano();

    public PlanoList() {
        setEjbql(getEjbql());
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(15);
    }

    @Override
    public String getEjbql() {
        return "select plano from Plano plano";
    }

    @Override
    public Integer getMaxResults() {
        return 25;
    }

    public Plano getPlano() {
        return plano;
    }
}
