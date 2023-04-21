package managedBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ejb.Procedimento;
import ejb.ProcedimentoFacadeRemote;

public class ManterProcedimento {

    @EJB
    private ProcedimentoFacadeRemote pfr;

    private Procedimento currentItem = new Procedimento();

    private Integer currentRow;

    private String message;

    private Set<Integer> updateRowByAjax = new HashSet<Integer>();

    private List<Procedimento> listaProcedimentos;

    @PostConstruct
    public String carregarProcedimentos() {
        listaProcedimentos = pfr.findAll();
        return null;
    }

    public String redirecionaProcedimento() {
        return "Procedimento";
    }

    public String delete() {
        try {
            pfr.delete(currentItem);
            carregarProcedimentos();
            message = "Procedimento deletado com sucesso!";
        } catch (Exception e) {
            message = "Erro ao deletar o Procedimento!";
        }
        clean();
        return null;
    }

    public String save() {
        try {
            currentItem.setIdProcedimento(null);
            pfr.save(currentItem);
            carregarProcedimentos();
            message = "Procedimento cadastrado com sucesso!";
        } catch (Exception e) {
            message = "Erro ao cadastrar o Procedimento!";
        }
        clean();
        return null;
    }

    public String update() {
        try {
            pfr.update(currentItem);
            carregarProcedimentos();
            message = "Procedimento atualizado com sucesso!";
        } catch (Exception e) {
            message = "Erro ao atualizar o Procedimento!";
        }
        clean();
        return null;
    }

    public String clean() {
        currentItem = new Procedimento();
        return null;
    }

    public Procedimento getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Procedimento currentItem) {
        this.currentItem = currentItem;
    }

    public Integer getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(Integer currentRow) {
        this.currentRow = currentRow;
    }

    public List<Procedimento> getListaProcedimentos() {
        return listaProcedimentos;
    }

    public void setListaProcedimentos(List<Procedimento> listaProcedimentos) {
        this.listaProcedimentos = listaProcedimentos;
    }

    public Set<Integer> getUpdateRowByAjax() {
        return updateRowByAjax;
    }

    public void setUpdateRowByAjax(Set<Integer> updateRowByAjax) {
        this.updateRowByAjax = updateRowByAjax;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
