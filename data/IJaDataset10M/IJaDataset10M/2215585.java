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
import ao.com.bna.beans.AuthUtilizador;
import ao.com.bna.dao.AuthUtilizadorDao;
import ao.com.bna.util.BeanFactory;
import ao.com.bna.util.EncriptacaoUtil;
import ao.com.bna.util.JsfUtil;

@ManagedBean
@SessionScoped
public class AuthUtilizadorControle {

    private AuthUtilizador infoBean = new AuthUtilizador();

    private ListDataModel<AuthUtilizador> listaInfos = new ListDataModel<AuthUtilizador>();

    private AuthUtilizadorDao dao = (AuthUtilizadorDao) BeanFactory.getBean("authUtilizadorDao", AuthUtilizadorDao.class);

    private String estadoCorrente;

    private String paginaDestino;

    private String tituloPagina;

    private String confirmaPassword;

    public AuthUtilizadorControle() {
        actualizaTela();
    }

    /**
	 * @return the infoBean
	 */
    public AuthUtilizador getInfoBean() {
        return infoBean;
    }

    /**
	 * @param authUtilizador
	 *            the infoBean to set
	 */
    public void setInfoBean(AuthUtilizador infoBean) {
        this.infoBean = infoBean;
    }

    /**
	 * @return the listaInfos
	 */
    public ListDataModel<AuthUtilizador> getListaInfos() {
        return listaInfos;
    }

    /**
	 * @param listaInfos
	 *            the listaInfos to set
	 */
    public void setListaInfos(ListDataModel<AuthUtilizador> listaInfos) {
        this.listaInfos = listaInfos;
    }

    /**
	 * @return the dao
	 */
    public AuthUtilizadorDao getAuthUtilizadorDao() {
        return dao;
    }

    /**
	 * @param dao
	 *            the dao to set
	 */
    public void setAuthUtilizadorDao(AuthUtilizadorDao dao) {
        this.dao = dao;
    }

    private void actualizaTela() {
        infoBean = new AuthUtilizador();
        listaInfos = new ListDataModel<AuthUtilizador>(dao.listar());
        tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorTituloListar");
    }

    /**
	 * @see Metodo que ivoca o metodo gravar apartir da classe EstadoDao
	 */
    public String gravar() {
        String senha = this.infoBean.getPassword();
        if (!senha.equals(this.confirmaPassword)) {
            JsfUtil.addErrorMessage(null, ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorConfirmarPasswordRequiredMessage"));
            return null;
        }
        try {
            byte[] b = EncriptacaoUtil.digest(infoBean.getPassword().getBytes(), "md5");
            infoBean.setPassword(EncriptacaoUtil.byteArrayToHexString(b));
            Date dt = new Date();
            infoBean.setDataAlteracao((Date) dt.clone());
            if (this.isEstadoAdicionar()) {
                infoBean.setDataRegisto((Date) dt.clone());
                this.dao.gravar(this.getInfoBean());
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources.mensagens").getString("geralRegistoCriado"));
                actualizaTela();
                return this.preparaAdicionar();
            } else if (this.isEstadoActualizar()) {
                this.dao.gravar(this.getInfoBean());
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
	 * @see Metodo que obtem um registo
	 */
    public AuthUtilizador obterPorID() {
        tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorTituloListar");
        return dao.obterPorID(this.getInfoBean().getId());
    }

    /**
	 * @see Metodo que ivoca o metodo remover apartir da classe EstadoDao
	 */
    public String excluir() {
        try {
            this.dao.remover(this.getInfoBean());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources.mensagens").getString("geralRegistoEliminado"));
            this.setEstadoCorrente(JsfUtil.ESTADO_FORMVAZIO);
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources.mensagens").getString("geralErroDePersistencia"));
            return null;
        }
    }

    public SelectItem[] getItemsAvailableSelectOneEmpety() {
        List<AuthUtilizador> lista = new ArrayList<AuthUtilizador>();
        return JsfUtil.getSelectItems(lista, true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(this.dao.listar(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.dao.listar(), true);
    }

    @FacesConverter(forClass = AuthUtilizador.class)
    public static class EntityConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AuthUtilizadorControle controller = (AuthUtilizadorControle) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "authUtilizadorControle");
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
            if (object instanceof AuthUtilizador) {
                AuthUtilizador o = (AuthUtilizador) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + AuthUtilizador.class.getName());
            }
        }
    }

    /**
	 * Prepara view detalhe
	 */
    public String preparaDetalhes() {
        this.setEstadoCorrente(JsfUtil.ESTADO_DETALHES);
        this.tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorTituloDetalhes");
        return null;
    }

    public String novo() {
        this.infoBean = new AuthUtilizador();
        return ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorPaginaCRUD");
    }

    /**
	 * Prepara view adicionar
	 */
    public String preparaAdicionar() {
        this.setEstadoCorrente(JsfUtil.ESTADO_ADICIONAR);
        this.tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorTituloRegistar");
        return novo();
    }

    /**
	 * Prepara view editar
	 */
    public String preparaEditar() {
        this.setEstadoCorrente(JsfUtil.ESTADO_ACTUALIZAR);
        this.tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorTituloAlterar");
        return ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorPaginaCRUD");
    }

    /**
	 * Prepara view eliminar
	 */
    public String preparaEliminar() {
        this.setEstadoCorrente(JsfUtil.ESTADO_ELIMINAR);
        this.tituloPagina = ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorTituloEliminar");
        return ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorPaginaCRUD");
    }

    /**
	 * Referente ao botao voltar
	 */
    public String voltar() {
        this.actualizaTela();
        return ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorPaginaListar");
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
	 * @return the confirmaPassword
	 */
    public String getConfirmaPassword() {
        return confirmaPassword;
    }

    /**
	 * @param confirmaPassword the confirmaPassword to set
	 */
    public void setConfirmaPassword(String confirmaPassword) {
        this.confirmaPassword = confirmaPassword;
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
