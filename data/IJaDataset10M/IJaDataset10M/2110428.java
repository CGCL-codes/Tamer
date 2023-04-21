package br.com.caelum.notasfiscais.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import br.com.caelum.notasfiscais.dao.DAO;
import br.com.caelum.notasfiscais.modelo.Item;
import br.com.caelum.notasfiscais.modelo.NotaFiscal;
import br.com.caelum.notasfiscais.modelo.Produto;

@ManagedBean
@ViewScoped
public class NotaFiscalBean {

    private Item item = new Item();

    private NotaFiscal notaFiscal = new NotaFiscal();

    private Long idProduto;

    public void adicionaItem() {
        item.setValorUnitario(item.getProduto().getPreco());
        notaFiscal.adicionaItem(item);
        item = new Item();
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Item getItem() {
        return item;
    }

    public NotaFiscal getNotaFiscal() {
        return notaFiscal;
    }

    public void adiciona() {
        DAO<NotaFiscal> dao = new DAO<NotaFiscal>(NotaFiscal.class);
        dao.adiciona(notaFiscal);
        notaFiscal = new NotaFiscal();
    }
}
