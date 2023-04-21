package com.f4.hotelf4;

import com.f4.hotelf4.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tipoServicoHome")
public class TipoServicoHome extends EntityHome<TipoServico> {

    public void setTipoServicoTsCodPk(Integer id) {
        setId(id);
    }

    public Integer getTipoServicoTsCodPk() {
        return (Integer) getId();
    }

    @Override
    protected TipoServico createInstance() {
        TipoServico tipoServico = new TipoServico();
        return tipoServico;
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public TipoServico getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<DespesaServico> getDespesaServicos() {
        return getInstance() == null ? null : new ArrayList<DespesaServico>(getInstance().getDespesaServicos());
    }

    public List<DespesaServico> getDespesaServicos_1() {
        return getInstance() == null ? null : new ArrayList<DespesaServico>(getInstance().getDespesaServicos_1());
    }
}
