package ao.com.bna.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import ao.com.bna.beans.ItemPedido;
import ao.com.bna.beans.Pedido;
import ao.com.bna.dao.PedidoDao;
import ao.com.bna.util.BeanFactory;
import ao.com.bna.util.JsfUtil;

@ManagedBean
@SessionScoped
public class PedidoControle {

    private Pedido infoBean = new Pedido();

    private ItemPedido infoBeanItem = new ItemPedido();

    private ItemPedido infoBeanItemTmp = new ItemPedido();

    private List<Pedido> listaPedidos = new ArrayList<Pedido>();

    private List<ItemPedido> listaPedidosItem = new ArrayList<ItemPedido>();

    private List<ItemPedido> listaPedidosItemTmp = new ArrayList<ItemPedido>();

    private PedidoDao dao = (PedidoDao) BeanFactory.getBean("pedidoDao", PedidoDao.class);

    private String estadoCorrente;

    private String paginaDestino;

    private String tituloPagina;

    public PedidoControle() {
        actualizaTela();
    }

    /**
	 * @return the infoBean
	 */
    public Pedido getInfoBean() {
        return infoBean;
    }

    /**
	 * @param infoBean
	 *            the infoBean to set
	 */
    public void setInfoBean(Pedido infoBean) {
        this.infoBean = infoBean;
    }

    /**
	 * @return the infoBeanItem
	 */
    public ItemPedido getInfoBeanItem() {
        return infoBeanItem;
    }

    /**
	 * @param infoBeanItem
	 *            the infoBeanItem to set
	 */
    public void setInfoBeanItem(ItemPedido infoBeanItem) {
        this.infoBeanItem = infoBeanItem;
    }

    /**
	 * @return the listaInfos
	 */
    public ListDataModel<Pedido> getListaInfos() {
        return new ListDataModel<Pedido>(listaPedidos);
    }

    /**
	 * @return the listaInfosItem
	 */
    public ListDataModel<ItemPedido> getListaInfosItem() {
        return new ListDataModel<ItemPedido>(listaPedidosItem);
    }

    /**
	 * @return the listaInfosItemTmp
	 */
    public ListDataModel<ItemPedido> getListaInfosItemTmp() {
        return new ListDataModel<ItemPedido>(listaPedidosItemTmp);
    }

    /**
	 * @return the infoBeanItemTmp
	 */
    public ItemPedido getInfoBeanItemTmp() {
        return infoBeanItemTmp;
    }

    /**
	 * @param infoBeanItemTmp the infoBeanItemTmp to set
	 */
    public void setInfoBeanItemTmp(ItemPedido infoBeanItemTmp) {
        this.infoBeanItemTmp = infoBeanItemTmp;
    }

    /**
	 * @return the listaPedidos
	 */
    public List<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    /**
	 * @param listaPedidos the listaPedidos to set
	 */
    public void setListaPedidos(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    /**
	 * @return the listaPedidosItem
	 */
    public List<ItemPedido> getListaPedidosItem() {
        return listaPedidosItem;
    }

    /**
	 * @param listaPedidosItem the listaPedidosItem to set
	 */
    public void setListaPedidosItem1(List<ItemPedido> listaPedidosItem) {
        this.listaPedidosItem = listaPedidosItem;
    }

    /**
	 * @return the listaPedidosItemTmp
	 */
    public List<ItemPedido> getListaPedidosItemTmp() {
        return listaPedidosItemTmp;
    }

    /**
	 * @param listaPedidosItemTmp the listaPedidosItemTmp to set
	 */
    public void setListaPedidosItemTmp(List<ItemPedido> listaPedidosItemTmp) {
        this.listaPedidosItemTmp = listaPedidosItemTmp;
    }

    /**
	 * @return the dao
	 */
    public PedidoDao getDao() {
        return dao;
    }

    /**
	 * @param pedidoDao
	 *            the pedidoDao to set
	 */
    public void setDao(PedidoDao dao) {
        this.dao = dao;
    }

    private void actualizaTela() {
        infoBean = new Pedido();
        infoBeanItem = new ItemPedido();
        infoBeanItemTmp = new ItemPedido();
        listaPedidos = dao.listar();
        tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("pedidoTituloListar");
    }

    /**
	 * @see Metodo que ivoca o metodo gravar apartir da classe EstadoDao
	 */
    public String gravar() {
        try {
            Date dt = new Date();
            infoBean.setDataAlteracao((Date) dt.clone());
            if (this.isEstadoAdicionar()) {
                infoBean.setDataRegisto((Date) dt.clone());
                this.dao.gravar(getInfoBean(), getListaPedidosItem());
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources.mensagens").getString("geralRegistoCriado"));
                actualizaTela();
                return this.preparaAdicionar();
            } else if (this.isEstadoActualizar()) {
                this.dao.gravar(getInfoBean(), getListaPedidosItem());
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources.mensagens").getString("geralRegistoAlterado"));
                this.setEstadoCorrente(JsfUtil.ESTADO_FORMVAZIO);
                return null;
            }
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources.mensagens").getString("geralErroDePersistencia"));
            return null;
        }
    }

    /**
	 * @see Metodo que ivoca o metodo gravar
	 */
    public String adicionarItem() {
        try {
            this.infoBeanItem.setStatus(false);
            this.listaPedidosItem.add(infoBeanItem);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources.mensagens").getString("itemPedidoMsgAdicionar"));
            actualizaTela();
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources.mensagens").getString("Falha ao adicionar item"));
            return null;
        }
    }

    /**
	 * @see Metodo que obtem um registo
	 */
    public Pedido obterPorID() {
        tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("pedidoTituloListar");
        return dao.obterPorID(getInfoBean().getId());
    }

    /**
	 * @see Metodo que ivoca o metodo remover apartir da classe EstadoDao
	 */
    public String excluir() {
        try {
            dao.remover(this.getInfoBean());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources.mensagens").getString("geralRegistoEliminado"));
            setEstadoCorrente(JsfUtil.ESTADO_FORMVAZIO);
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources.mensagens").getString("geralErroDePersistencia"));
            return null;
        }
    }

    public SelectItem[] getItemsAvailableSelectOneEmpety() {
        List<Pedido> lista = new ArrayList<Pedido>();
        return JsfUtil.getSelectItems(lista, true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(this.dao.listar(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.dao.listar(), true);
    }

    @FacesConverter(value = "pedido", forClass = Pedido.class)
    public static class EntityConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PedidoControle controller = (PedidoControle) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "pedidoControle");
            return controller.dao.obterPorID(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Pedido) {
                Pedido o = (Pedido) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Pedido.class.getName());
            }
        }
    }

    /**
	 * Prepara view detalhe
	 */
    public String preparaDetalhes() {
        setEstadoCorrente(JsfUtil.ESTADO_DETALHES);
        tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("pedidoTituloDetalhes");
        return null;
    }

    public String novo() {
        infoBean = new Pedido();
        infoBeanItem = new ItemPedido();
        infoBeanItemTmp = new ItemPedido();
        return ResourceBundle.getBundle("resources.mensagens").getString("pedidoPaginaCRUD");
    }

    /**
	 * Prepara view adicionar
	 */
    public String preparaAdicionar() {
        setEstadoCorrente(JsfUtil.ESTADO_ADICIONAR);
        tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("pedidoTituloRegistar");
        return novo();
    }

    /**
	 * Prepara view editar
	 */
    public String preparaEditar() {
        setEstadoCorrente(JsfUtil.ESTADO_ACTUALIZAR);
        tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("pedidoTituloAlterar");
        listaPedidosItem = infoBean.getItemPedidos();
        return ResourceBundle.getBundle("resources.mensagens").getString("pedidoPaginaCRUD");
    }

    /**
	 * Prepara view eliminar
	 */
    public String preparaEliminar() {
        setEstadoCorrente(JsfUtil.ESTADO_ELIMINAR);
        tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("pedidoTituloEliminar");
        listaPedidosItem = infoBean.getItemPedidos();
        return ResourceBundle.getBundle("resources.mensagens").getString("pedidoPaginaCRUD");
    }

    /**
	 * Referente ao botao voltar
	 */
    public String voltar() {
        actualizaTela();
        return ResourceBundle.getBundle("resources.mensagens").getString("pedidoPaginaListar");
    }

    public boolean isEstadoPesquisar() {
        String state = this.getEstadoCorrente();
        return (state == null || JsfUtil.ESTADO_PESQUISAR.equals(state));
    }

    public boolean isEstadoAdicionar() {
        return JsfUtil.ESTADO_ADICIONAR.equals(this.getEstadoCorrente());
    }

    public boolean isEstadoActualizar() {
        return JsfUtil.ESTADO_ACTUALIZAR.equals(this.getEstadoCorrente());
    }

    public boolean isEstadoEliminar() {
        return JsfUtil.ESTADO_ELIMINAR.equals(this.getEstadoCorrente());
    }

    public boolean isEstadoDetalhe() {
        return JsfUtil.ESTADO_DETALHES.equals(this.getEstadoCorrente());
    }

    public boolean isEstadoFormVazio() {
        return JsfUtil.ESTADO_FORMVAZIO.equals(this.getEstadoCorrente());
    }

    public boolean isEstadoCrud() {
        return (this.isEstadoAdicionar() || this.isEstadoActualizar() || this.isEstadoEliminar() || this.isEstadoDetalhe() || this.isEstadoFormVazio());
    }

    /**
	 * @return the estadoCorrente
	 */
    public String getEstadoCorrente() {
        return estadoCorrente;
    }

    /**
	 * @param estadoCorrente
	 *            the estadoCorrente to set
	 */
    public void setEstadoCorrente(String estadoCorrente) {
        this.estadoCorrente = estadoCorrente;
    }

    /**
	 * @return the paginaDestino
	 */
    public String getPaginaDestino() {
        return paginaDestino;
    }

    /**
	 * @param paginaDestino
	 *            the paginaDestino to set
	 */
    public void setPaginaDestino(String paginaDestino) {
        this.paginaDestino = paginaDestino;
    }

    /**
	 * @return the tituloPagina
	 */
    public String getTituloPagina() {
        return tituloPagina;
    }

    /**
	 * @param tituloPagina
	 *            the tituloPagina to set
	 */
    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }
}
