package br.com.cps.distribuidora.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.faces.event.ValueChangeEvent;
import br.com.cps.distribuidora.apoio.EnderecoCompleto;
import br.com.cps.distribuidora.apoio.RetornoPedidoEnum;
import br.com.cps.distribuidora.apoio.SessionBean;
import br.com.cps.distribuidora.business.PedidoBO;
import br.com.cps.distribuidora.model.Base;
import br.com.cps.distribuidora.model.Cliente;
import br.com.cps.distribuidora.model.Endereco;
import br.com.cps.distribuidora.model.EnderecoCliente;
import br.com.cps.distribuidora.model.EnderecoClientePK;
import br.com.cps.distribuidora.model.ParametroQtdeDesejada;
import br.com.cps.distribuidora.model.Produto;
import br.com.cps.distribuidora.model.ProdutoPK;
import br.com.cps.distribuidora.model.TipoProduto;
import br.com.cps.distribuidora.persistence.EnderecoClienteDAO;
import br.com.cps.distribuidora.persistence.EnderecoDAO;
import br.com.cps.distribuidora.persistence.ParametroQtdeDesejadaDAO;
import br.com.cps.distribuidora.persistence.ProdutoDAO;
import br.com.cps.distribuidora.utils.DAOFactory;

public class PedidoController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Base base;

    private Produto produto;

    private Cliente cliente;

    private PedidoBO pedidoBO;

    private Endereco endereco;

    private SessionBean sessionBean;

    private RetornoPedidoEnum retornoPedidoEnum;

    private ParametroQtdeDesejada parametroQtdeDesejada;

    private Collection<Produto> listaProduto;

    private Collection<Endereco> listaEndereco;

    private Collection<TipoProduto> listaTipoProduto;

    private Collection<EnderecoCompleto> listaEnderecoCompleto;

    private Collection<ParametroQtdeDesejada> listaParametroQtdeDesejada;

    private String idTipoProduto;

    private Date dsData;

    public String formPedido() {
        try {
            cliente = sessionBean.getCliente();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "paginas/pedido/grid_pedido.xhtml";
    }

    public Collection<EnderecoCompleto> carregaEndereco() {
        try {
            cliente = sessionBean.getCliente();
            listaEndereco = new ArrayList<Endereco>();
            for (EnderecoCliente enderecoCliente : cliente.getEnderecoClienteCollection()) listaEndereco.add(enderecoCliente.getEndereco());
            listaEnderecoCompleto = new ArrayList<EnderecoCompleto>();
            for (Endereco endereco : listaEndereco) {
                if (endereco.getFlHabilitado()) {
                    EnderecoCompleto enderecoCompleto = new EnderecoCompleto(endereco.getIdEndereco(), endereco.getDsLogradouro() + " - " + endereco.getDsComplemento() + " - " + endereco.getDsMunicipio() + "/" + endereco.getDsEstado());
                    listaEnderecoCompleto.add(enderecoCompleto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return listaEnderecoCompleto;
    }

    public void atualizaListaProduto(ValueChangeEvent vce) {
        try {
            final DAOFactory daoFactory = DAOFactory.getDAOFactory();
            final EnderecoDAO enderecoDAO = daoFactory.createEnderecoDAO();
            final EnderecoClienteDAO enderecoClienteDAO = daoFactory.createEnderecoClienteDAO();
            cliente = sessionBean.getCliente();
            if (!vce.getNewValue().equals("-1")) {
                endereco = enderecoDAO.consultar(Integer.parseInt(vce.getNewValue().toString()));
                EnderecoClientePK enderecoClientePK = new EnderecoClientePK(cliente.getIdCliente(), endereco.getIdEndereco());
                EnderecoCliente enderecoCliente = enderecoClienteDAO.consultarPorPK(enderecoClientePK);
                base = enderecoCliente.getBase();
                listaProduto = base.getProdutoCollection();
                listaTipoProduto = new ArrayList<TipoProduto>();
                for (Produto produto : listaProduto) listaTipoProduto.add(produto.getTipoProduto());
            }
            setEndereco(endereco);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizaListaQuantidade(ValueChangeEvent vce) {
        try {
            final DAOFactory daoFactory = DAOFactory.getDAOFactory();
            final ProdutoDAO produtoDAO = daoFactory.createProdutoDAO();
            final ParametroQtdeDesejadaDAO parametroQtdeDesejadaDAO = daoFactory.createParametroQtdeDesejadaDAO();
            if (!vce.getNewValue().equals("-1")) {
                listaParametroQtdeDesejada = parametroQtdeDesejadaDAO.listarTodos();
                ProdutoPK produtoPK = new ProdutoPK(Integer.parseInt(vce.getNewValue().toString()), base.getIdBaseOrigem());
                produto = produtoDAO.consultarPorPK(produtoPK);
            }
            setIdTipoProduto(vce.getNewValue().toString());
        } catch (Exception e) {
            setIdTipoProduto(vce.getNewValue().toString());
            e.printStackTrace();
        }
    }

    public void selecionaQuantidade(ValueChangeEvent vce) {
        try {
            final DAOFactory daoFactory = DAOFactory.getDAOFactory();
            final ParametroQtdeDesejadaDAO parametroQtdeDesejadaDAO = daoFactory.createParametroQtdeDesejadaDAO();
            if (!vce.getNewValue().equals("-1")) {
                parametroQtdeDesejada = parametroQtdeDesejadaDAO.consultar(Integer.parseInt(vce.getNewValue().toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String processarPedido() {
        try {
            cliente = sessionBean.getCliente();
            retornoPedidoEnum = pedidoBO.efetuarProcessoPedido(cliente, endereco, produto, parametroQtdeDesejada, getDsData(), base);
            retornoPedidoEnum.getMensagem();
        } catch (Exception e) {
            e.printStackTrace();
            return "paginas/pedido/grid_pedido.xhtml";
        }
        return "paginas/pedido/grid_confirmacao_pedido.xhtml";
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Collection<Endereco> getListaEndereco() {
        return listaEndereco;
    }

    public void setListaEndereco(Collection<Endereco> listaEndereco) {
        this.listaEndereco = listaEndereco;
    }

    public PedidoBO getPedidoBO() {
        return pedidoBO;
    }

    public void setPedidoBO(PedidoBO pedidoBO) {
        this.pedidoBO = pedidoBO;
    }

    public Collection<EnderecoCompleto> getListaEnderecoCompleto() {
        return carregaEndereco();
    }

    public void setListaEnderecoCompleto(Collection<EnderecoCompleto> listaEnderecoCompleto) {
        this.listaEnderecoCompleto = listaEnderecoCompleto;
    }

    public Collection<TipoProduto> getListaTipoProduto() {
        return listaTipoProduto;
    }

    public void setListaTipoProduto(Collection<TipoProduto> listaTipoProduto) {
        this.listaTipoProduto = listaTipoProduto;
    }

    public Collection<Produto> getListaProduto() {
        return listaProduto;
    }

    public void setListaProduto(Collection<Produto> listaProduto) {
        this.listaProduto = listaProduto;
    }

    public Collection<ParametroQtdeDesejada> getListaParametroQtdeDesejada() {
        return listaParametroQtdeDesejada;
    }

    public void setListaParametroQtdeDesejada(Collection<ParametroQtdeDesejada> listaParametroQtdeDesejada) {
        this.listaParametroQtdeDesejada = listaParametroQtdeDesejada;
    }

    public Date getDsData() {
        return dsData;
    }

    public void setDsData(Date dsData) {
        this.dsData = dsData;
    }

    public String getIdTipoProduto() {
        return idTipoProduto;
    }

    public void setIdTipoProduto(String idTipoProduto) {
        this.idTipoProduto = idTipoProduto;
    }

    public RetornoPedidoEnum getRetornoPedidoEnum() {
        return retornoPedidoEnum;
    }

    public void setRetornoPedidoEnum(RetornoPedidoEnum retornoPedidoEnum) {
        this.retornoPedidoEnum = retornoPedidoEnum;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public ParametroQtdeDesejada getParametroQtdeDesejada() {
        return parametroQtdeDesejada;
    }

    public void setParametroQtdeDesejada(ParametroQtdeDesejada parametroQtdeDesejada) {
        this.parametroQtdeDesejada = parametroQtdeDesejada;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
