package managedBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import ejb.Recurso;
import ejb.RecursoFacadeRemote;
import ejb.Pessoa;
import ejb.Projeto;
import ejb.ProjetoFacadeRemote;
import org.hibernate.validator.Digits;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.Max;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.hibernate.validator.Patterns;

public class ManterRecurso {

    @EJB
    RecursoFacadeRemote recursoFacadeRemote;

    @EJB
    ProjetoFacadeRemote projetoFacadeRemote;

    @NotNull
    private Recurso currentItemRecurso = new Recurso();

    @NotNull
    private Projeto currentItemProjeto = new Projeto();

    private Integer currentRow;

    private Set<Integer> updateRowByAjax = new HashSet<Integer>();

    private List<Recurso> listaRecurso = new ArrayList<Recurso>();

    private List<Projeto> listaProjeto = new ArrayList<Projeto>();

    private List<String> lista = new ArrayList<String>();

    private SelectItem[] selectItemRecurso;

    private SelectItem[] selectItemProjeto;

    @NotNull
    private String nome;

    @NotNull
    private String descricao;

    @PostConstruct
    public String carregarRecurso() {
        listaRecurso = recursoFacadeRemote.findAll();
        listaProjeto = projetoFacadeRemote.findAll();
        this.popSelectItemProjeto();
        this.setNome("");
        this.setDescricao("");
        return null;
    }

    public String clean() {
        currentItemRecurso = new Recurso();
        currentItemProjeto = new Projeto();
        this.setNome("");
        this.setDescricao("");
        return null;
    }

    public String deletarRecurso() {
        recursoFacadeRemote.delete(currentItemRecurso.getIdRecurso());
        carregarRecurso();
        clean();
        this.carregarRecurso();
        return null;
    }

    public String atualizarRecurso() {
        recursoFacadeRemote.update(this.currentItemRecurso);
        this.carregarRecurso();
        return null;
    }

    public String cadastrarRecurso() {
        currentItemRecurso.setIdRecurso(null);
        recursoFacadeRemote.save(this.currentItemRecurso);
        this.carregarRecurso();
        return null;
    }

    public String reloadRecurso() {
        System.out.println(">> reloadRecurso();");
        this.setNome(this.currentItemRecurso.getNomeRecurso());
        this.setDescricao(this.currentItemRecurso.getDescricaoRecurso());
        return null;
    }

    public void popSelectItemProjeto() {
        ArrayList<SelectItem> l = new ArrayList<SelectItem>();
        SelectItem selectItem;
        ArrayList<Projeto> lista = new ArrayList<Projeto>(this.listaProjeto);
        Iterator<Projeto> iterator = lista.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            Projeto projeto = (Projeto) element;
            try {
                selectItem = new SelectItem(projeto.getIdProjeto(), projeto.getDescricaoProjeto());
                l.add(selectItem);
            } catch (Exception ex) {
                Logger.getLogger(Pessoa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.setSelectItemProjeto(l.toArray(new SelectItem[l.size()]));
    }

    public Recurso getCurrentItemRecurso() {
        return currentItemRecurso;
    }

    public void setCurrentItemRecurso(Recurso currentItemRecurso) {
        this.currentItemRecurso = currentItemRecurso;
    }

    public Projeto getCurrentItemProjeto() {
        return currentItemProjeto;
    }

    public void setCurrentItemProjeto(Projeto currentItemProjeto) {
        this.currentItemProjeto = currentItemProjeto;
    }

    public Integer getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(Integer currentRow) {
        this.currentRow = currentRow;
    }

    public Set<Integer> getUpdateRowByAjax() {
        return updateRowByAjax;
    }

    public void setUpdateRowByAjax(Set<Integer> updateRowByAjax) {
        this.updateRowByAjax = updateRowByAjax;
    }

    public List<Recurso> getListaRecurso() {
        return listaRecurso;
    }

    public void setListaRecurso(List<Recurso> listaRecurso) {
        this.listaRecurso = listaRecurso;
    }

    public List<Projeto> getListaProjeto() {
        return listaProjeto;
    }

    public void setListaProjeto(List<Projeto> listaProjeto) {
        this.listaProjeto = listaProjeto;
    }

    public SelectItem[] getSelectItemProjeto() {
        return selectItemProjeto;
    }

    public void setSelectItemProjeto(SelectItem[] selectItemProjeto) {
        this.selectItemProjeto = selectItemProjeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getLista() {
        return lista;
    }

    public void setLista(List<String> lista) {
        this.lista = lista;
    }
}
