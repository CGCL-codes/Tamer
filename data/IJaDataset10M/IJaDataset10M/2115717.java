package br.com.gerpro.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.model.SelectItem;
import br.com.gerpro.dao.FacadeEquipe;
import br.com.gerpro.dao.FacadeItem;
import br.com.gerpro.dao.FacadeProposta;
import br.com.gerpro.dao.FacadePropostaItem;
import br.com.gerpro.dao.FacadeStatus;
import br.com.gerpro.dao.FacadeUsuario;
import br.com.gerpro.dao.impl.EquipeDao;
import br.com.gerpro.dao.impl.ItemDao;
import br.com.gerpro.dao.impl.PropostaDao;
import br.com.gerpro.dao.impl.PropostaItemDao;
import br.com.gerpro.dao.impl.StatusDao;
import br.com.gerpro.dao.impl.UsuarioDao;
import br.com.gerpro.mensagens.MessageManagerImpl;
import br.com.gerpro.model.Equipe;
import br.com.gerpro.model.Item;
import br.com.gerpro.model.Proposta;
import br.com.gerpro.model.PropostaItem;
import br.com.gerpro.model.Status;
import br.com.gerpro.model.Usuario;
import br.com.gerpro.util.ApplicationSecurityManager;

public class SubmeterPropostaBean {

    private UIData objDatatablePropostaItem;

    private List<PropostaItem> listaPropostaItem;

    private List<Usuario> listaUsuarios;

    private ApplicationSecurityManager appSecurityManager = new ApplicationSecurityManager();

    private boolean desabilita;

    private Proposta proposta = new Proposta();

    private Item item = new Item();

    private PropostaItem propitem = new PropostaItem();

    private Equipe equipe = new Equipe();

    private Status status = new Status();

    private FacadeProposta propostaDao = new PropostaDao();

    private FacadeItem itemDao = new ItemDao();

    private FacadePropostaItem propitemDao = new PropostaItemDao();

    private FacadeEquipe equipeDao = new EquipeDao();

    private FacadeStatus statusDao = new StatusDao();

    private FacadeUsuario usuarioDao = new UsuarioDao();

    public SelectItem[] getEquipesCombo() {
        List<Equipe> le = getEquipeDao().listar();
        List<SelectItem> itens = new ArrayList<SelectItem>(le.size());
        for (Equipe e : le) {
            itens.add(new SelectItem(e.getId(), e.getNome()));
        }
        return itens.toArray(new SelectItem[itens.size()]);
    }

    public SelectItem[] getStatusCombo() {
        List<Status> le = getStatusDao().listar();
        List<SelectItem> itens = new ArrayList<SelectItem>(le.size());
        for (Status e : le) {
            itens.add(new SelectItem(e.getId(), e.getNome()));
        }
        return itens.toArray(new SelectItem[itens.size()]);
    }

    public String prepararBean() {
        propitem = new PropostaItem();
        listaPropostaItem = getPropitemDao().listarPoridPropostaSemAvaliacaoGeral(appSecurityManager.getProposta().getId());
        propitem = listaPropostaItem.get(1);
        proposta = propitem.getProposta();
        status = proposta.getStatus();
        equipe = proposta.getEquipe();
        listaUsuarios = usuarioDao.listarPorEquipe(equipe.getId());
        verificarItens();
        return "SubmeterProposta";
    }

    private void verificarItens() {
        int cont = 0;
        for (PropostaItem propitem : listaPropostaItem) {
            if (propitem.getStatus().getId() == 6) {
                cont++;
            }
        }
        if (cont == 5 && (proposta.getStatus().getId() == 1)) {
            desabilita = false;
        } else {
            desabilita = true;
            if (cont < 5) {
                MessageManagerImpl.setMensagem(FacesMessage.SEVERITY_INFO, "aviso", "itens.nao.concluidos");
            } else {
                MessageManagerImpl.setMensagem(FacesMessage.SEVERITY_WARN, "aviso", "proposta.submetida");
            }
        }
    }

    public void submeterProposta() {
        try {
            status.setId(6);
            proposta.setStatus(status);
            proposta.setDataSubmissao(new Date());
            propostaDao.salvar(proposta);
            MessageManagerImpl.setMensagem(FacesMessage.SEVERITY_ERROR, "sucesso", "sucesso.submeter.proposta_detail");
        } catch (Exception e) {
            MessageManagerImpl.setMensagem(FacesMessage.SEVERITY_ERROR, "erro", "erro.submeter.proposta_detail");
        }
    }

    public String prepararEdicao() {
        proposta = (Proposta) objDatatablePropostaItem.getRowData();
        status = proposta.getStatus();
        equipe = proposta.getEquipe();
        return "alterar";
    }

    public void pesquisar() {
    }

    /**
	 * @return the objDatatablePropostaItem
	 */
    public UIData getObjDatatablePropostaItem() {
        return objDatatablePropostaItem;
    }

    /**
	 * @param objDatatablePropostaItem the objDatatablePropostaItem to set
	 */
    public void setObjDatatablePropostaItem(UIData objDatatablePropostaItem) {
        this.objDatatablePropostaItem = objDatatablePropostaItem;
    }

    /**
	 * @return the listaPropostaItem
	 */
    public List<PropostaItem> getListaPropostaItem() {
        return listaPropostaItem;
    }

    /**
	 * @param listaProposta the listaProposta to set
	 */
    public void setListaPropostaItem(List<PropostaItem> listaPropostaItem) {
        this.listaPropostaItem = listaPropostaItem;
    }

    /**
	 * @return the proposta
	 */
    public Proposta getProposta() {
        return proposta;
    }

    /**
	 * @param proposta the proposta to set
	 */
    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }

    /**
	 * @return the item
	 */
    public Item getItem() {
        return item;
    }

    /**
	 * @param item the item to set
	 */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
	 * @return the propitem
	 */
    public PropostaItem getPropitem() {
        return propitem;
    }

    /**
	 * @param propitem the propitem to set
	 */
    public void setPropitem(PropostaItem propitem) {
        this.propitem = propitem;
    }

    /**
	 * @return the equipe
	 */
    public Equipe getEquipe() {
        return equipe;
    }

    /**
	 * @param equipe the equipe to set
	 */
    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    /**
	 * @return the status
	 */
    public Status getStatus() {
        return status;
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
	 * @return the propostaDao
	 */
    public FacadeProposta getPropostaDao() {
        return propostaDao;
    }

    /**
	 * @param propostaDao the propostaDao to set
	 */
    public void setPropostaDao(FacadeProposta propostaDao) {
        this.propostaDao = propostaDao;
    }

    /**
	 * @return the itemDao
	 */
    public FacadeItem getItemDao() {
        return itemDao;
    }

    /**
	 * @param itemDao the itemDao to set
	 */
    public void setItemDao(FacadeItem itemDao) {
        this.itemDao = itemDao;
    }

    /**
	 * @return the propitemDao
	 */
    public FacadePropostaItem getPropitemDao() {
        return propitemDao;
    }

    /**
	 * @param propitemDao the propitemDao to set
	 */
    public void setPropitemDao(FacadePropostaItem propitemDao) {
        this.propitemDao = propitemDao;
    }

    /**
	 * @return the equipeDao
	 */
    public FacadeEquipe getEquipeDao() {
        return equipeDao;
    }

    /**
	 * @param equipeDao the equipeDao to set
	 */
    public void setEquipeDao(FacadeEquipe equipeDao) {
        this.equipeDao = equipeDao;
    }

    /**
	 * @return the statusDao
	 */
    public FacadeStatus getStatusDao() {
        return statusDao;
    }

    /**
	 * @param statusDao the statusDao to set
	 */
    public void setStatusDao(FacadeStatus statusDao) {
        this.statusDao = statusDao;
    }

    /**
	 * @return the usuarioDao
	 */
    public FacadeUsuario getUsuarioDao() {
        return usuarioDao;
    }

    /**
	 * @param usuarioDao the usuarioDao to set
	 */
    public void setUsuarioDao(FacadeUsuario usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    /**
	 * @return the listaUsuarios
	 */
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    /**
	 * @param listaUsuarios the listaUsuarios to set
	 */
    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    /**
	 * @return the desabilita
	 */
    public boolean isDesabilita() {
        return desabilita;
    }

    /**
	 * @param desabilita the desabilita to set
	 */
    public void setDesabilita(boolean desabilita) {
        this.desabilita = desabilita;
    }
}
