package br.sysclinic.session.movimentacao;

import br.sysclinic.entity.*;
import br.sysclinic.session.cadastro.DistribuidorHome;
import br.sysclinic.session.cadastro.ProdutoHome;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("produtoCotadoHome")
public class ProdutoCotadoHome extends EntityHome<ProdutoCotado> {

    @In(create = true)
    CotacaoHome cotacaoHome;

    @In(create = true)
    DistribuidorHome distribuidorHome;

    @In(create = true)
    ProdutoHome produtoHome;

    public void setProdutoCotadoId(Integer id) {
        setId(id);
    }

    public Integer getProdutoCotadoId() {
        return (Integer) getId();
    }

    @Override
    protected ProdutoCotado createInstance() {
        ProdutoCotado produtoCotado = new ProdutoCotado();
        return produtoCotado;
    }

    public void wire() {
        getInstance();
        Cotacao cotacao = cotacaoHome.getDefinedInstance();
        if (cotacao != null) {
            getInstance().setCotacao(cotacao);
        }
        Distribuidor distribuidor = distribuidorHome.getDefinedInstance();
        if (distribuidor != null) {
            getInstance().setDistribuidor(distribuidor);
        }
        Produto produto = produtoHome.getDefinedInstance();
        if (produto != null) {
            getInstance().setProduto(produto);
        }
    }

    public boolean isWired() {
        return true;
    }

    public ProdutoCotado getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<Venda> getVendas() {
        return getInstance() == null ? null : new ArrayList<Venda>(getInstance().getVendas());
    }
}
