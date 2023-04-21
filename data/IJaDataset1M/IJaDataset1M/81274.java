package org.jw.web.rdc.view.mbean;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.SelectItem;
import org.jw.web.rdc.business.aj.IAssociacaoJuridicaService;
import org.jw.web.rdc.business.saj.ISajService;
import org.jw.web.rdc.integration.dto.AssociacaoJuridica;
import org.jw.web.rdc.integration.dto.Saj;

/**
 * <p>
 * <b>T�tulo:</b> Nome do Projeto.
 * </p>
 * <p>
 * <b>Descri��o:</b>
 * </p>
 * <p>
 * Descri��o da classe.
 * </p>
 * 
 * @author CPM Braxis / Edwin M. A. Costa
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DirectorAJMBean implements Serializable {

    /**
	 * Propriedade da interface de servi�o.
	 */
    private IAssociacaoJuridicaService associacaoJuridicaService;

    /**
	 * Interface de servi�o referente aos SAJ's.
	 */
    private ISajService sajService;

    /**
	 * Propriedade com a lista de Associa��es Jur�dicas.
	 */
    private List<AssociacaoJuridica> associacoesJuridicas;

    /**
	 * Propriedade com a lista de Associa��es Jur�dicas.
	 */
    private List<SelectItem> ajSelectItens;

    /**
	 * �ndice da Associa��o Jur�dica selecionada.
	 */
    private Integer ajSelectedIndex;

    /**
	 * Propriedade com a lista de Sajs.
	 */
    private List<Saj> listSajs;

    /**
	 * Propriedade com a lista de Sajs.
	 */
    private List<SelectItem> sajSelectItens;

    /**
	 * �ndice do saj selecionado.
	 */
    private Integer sajSelectedIndex;

    /**
	 * @return the associacaoJuridicaService
	 */
    public IAssociacaoJuridicaService getAssociacaoJuridicaService() {
        return associacaoJuridicaService;
    }

    /**
	 * @param associacaoJuridicaService
	 *            the associacaoJuridicaService to set
	 */
    public void setAssociacaoJuridicaService(IAssociacaoJuridicaService associacaoJuridicaService) {
        this.associacaoJuridicaService = associacaoJuridicaService;
    }

    /**
	 * @return the sajService
	 */
    public ISajService getSajService() {
        return sajService;
    }

    /**
	 * @param sajService
	 *            the sajService to set
	 */
    public void setSajService(ISajService sajService) {
        this.sajService = sajService;
    }

    /**
	 * @return the associacoesJuridicas
	 */
    public List<AssociacaoJuridica> getAssociacoesJuridicas() {
        return associacoesJuridicas;
    }

    /**
	 * @param associacoesJuridicas
	 *            the associacoesJuridicas to set
	 */
    public void setAssociacoesJuridicas(List<AssociacaoJuridica> associacoesJuridicas) {
        this.associacoesJuridicas = associacoesJuridicas;
    }

    /**
	 * @return the ajSelectItens
	 */
    public List<SelectItem> getAjSelectItens() {
        return ajSelectItens;
    }

    /**
	 * @param ajSelectItens
	 *            the ajSelectItens to set
	 */
    public void setAjSelectItens(List<SelectItem> ajSelectItens) {
        this.ajSelectItens = ajSelectItens;
    }

    /**
	 * @return the ajSelectedIndex
	 */
    public Integer getAjSelectedIndex() {
        return ajSelectedIndex;
    }

    /**
	 * @param ajSelectedIndex
	 *            the ajSelectedIndex to set
	 */
    public void setAjSelectedIndex(Integer ajSelectedIndex) {
        this.ajSelectedIndex = ajSelectedIndex;
    }

    /**
	 * @return the listSajs
	 */
    public List<Saj> getListSajs() {
        return listSajs;
    }

    /**
	 * @param listSajs
	 *            the listSajs to set
	 */
    public void setListSajs(List<Saj> listSajs) {
        this.listSajs = listSajs;
    }

    /**
	 * @return the sajSelectItens
	 */
    public List<SelectItem> getSajSelectItens() {
        return sajSelectItens;
    }

    /**
	 * @param sajSelectItens
	 *            the sajSelectItens to set
	 */
    public void setSajSelectItens(List<SelectItem> sajSelectItens) {
        this.sajSelectItens = sajSelectItens;
    }

    /**
	 * @return the sajSelectedIndex
	 */
    public Integer getSajSelectedIndex() {
        return sajSelectedIndex;
    }

    /**
	 * @param sajSelectedIndex
	 *            the sajSelectedIndex to set
	 */
    public void setSajSelectedIndex(Integer sajSelectedIndex) {
        this.sajSelectedIndex = sajSelectedIndex;
    }
}
