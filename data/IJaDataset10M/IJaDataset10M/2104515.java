package br.sysclinic.session.contas;

import java.util.Arrays;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import br.sysclinic.entity.Natureza;

@SuppressWarnings("serial")
@Name("naturezaList")
public class NaturezaList extends EntityQuery<Natureza> {

    private static final String[] RESTRICTIONS = { "lower(natureza.descricao) like concat('%',lower(#{naturezaList.natureza.descricao}),'%')" };

    private Natureza natureza = new Natureza();

    public NaturezaList() {
        setEjbql(getEjbql());
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @Override
    public String getEjbql() {
        return "select natureza from Natureza natureza";
    }

    @Override
    public Integer getMaxResults() {
        return 25;
    }

    public Natureza getNatureza() {
        return natureza;
    }

    public void setNatureza(Natureza natureza) {
        this.natureza = natureza;
    }
}
