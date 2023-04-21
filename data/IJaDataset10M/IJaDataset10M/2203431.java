package br.usp.poli.mfc.struts.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author Renato
 * @version
 */
public class CadastroRevistaActionForm extends org.apache.struts.action.ActionForm {

    private String titulo;

    private String descricao;

    private String genero;

    private String datapublicacao;

    private int number;

    /**
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param i
     */
    public void setNumber(int i) {
        this.number = i;
    }

    /**
     *
     *
     */
    public CadastroRevistaActionForm() {
        super();
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getTitulo() == null || getTitulo().length() < 1) {
            errors.add("name", new ActionMessage("error.name.required"));
        }
        return errors;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDatapublicacao() {
        return datapublicacao;
    }

    public void setDatapublicacao(String datapublicacao) {
        this.datapublicacao = datapublicacao;
    }
}
