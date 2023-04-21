package mbeans;

import controller.FornecedorController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import modelo.FornecedorModelo;
import org.richfaces.component.UIExtendedDataTable;

/**
 * Managed Bean da entidade que representa o Fornecedor.
 * @author Paulo Ilenga
 */
@ManagedBean
@SessionScoped
public class FornecedorMB implements Serializable {

    private final String NOME_TABELA = "tbl_fornecedor";

    private final String NOME_ENTIDADE = "FORNECEDOR";

    private FornecedorController controller;

    private FornecedorModelo modelo, modeloTemp;

    private List<FornecedorModelo> seleccionados = new ArrayList<FornecedorModelo>();

    private Collection<Object> selection;

    private FacesContext context;

    private Date date;

    /** Constructor da classe sem argumentos */
    public FornecedorMB() {
        controller = new FornecedorController();
        modelo = new FornecedorModelo();
        modeloTemp = new FornecedorModelo();
    }

    public FornecedorController getController() {
        return controller;
    }

    public void setController(FornecedorController controller) {
        this.controller = controller;
    }

    public FornecedorModelo getModelo() {
        modelo.setDataRegisto(new Date());
        return modelo;
    }

    public void setModelo(FornecedorModelo modelo) {
        this.modelo = modelo;
    }

    public FornecedorModelo getModeloTemp() {
        return modeloTemp;
    }

    public void setModeloTemp(FornecedorModelo modeloTemp) {
        this.modeloTemp = modeloTemp;
    }

    public List<FornecedorModelo> getTodos() {
        return controller.getTodos();
    }

    public List<FornecedorModelo> getSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(List<FornecedorModelo> seleccionados) {
        this.seleccionados = seleccionados;
    }

    public Collection<Object> getSelection() {
        return selection;
    }

    public void setSelection(Collection<Object> selection) {
        this.selection = selection;
    }

    public void selectionListener(AjaxBehaviorEvent event) {
        UIExtendedDataTable dataTable = (UIExtendedDataTable) event.getComponent();
        Object originalKey = dataTable.getRowKey();
        seleccionados.clear();
        for (Object selectionKey : selection) {
            dataTable.setRowKey(selectionKey);
            if (dataTable.isRowAvailable()) {
                seleccionados.add((FornecedorModelo) dataTable.getRowData());
            }
        }
        if (modeloTemp.getId() != seleccionados.get(0).getId()) {
            modeloTemp = seleccionados.get(0);
        }
        dataTable.setRowKey(originalKey);
    }

    /**
     * Método salvar da classe
     */
    public void salvar() {
        context = FacesContext.getCurrentInstance();
        if (!modelo.getNome().equals("")) {
            if (modelo.getIdProvincia() != 0) {
                if (!modelo.getEndereco().equals("")) {
                    if (!modelo.getTelefone().equals("")) {
                        if (!modelo.getFax().equals("")) {
                            if (!modelo.getEmail().equals("")) {
                                if (controller.save(modelo)) {
                                    modelo = new FornecedorModelo();
                                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "REGISTO GUARDADO COM SUCESSO!"));
                                } else {
                                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "ERRO N.º SG001: AO GUARDAR O REGISTO! CONSULTE O SUPORTE."));
                                }
                            } else {
                                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UG002: DEVE DIGITAR O EMAIL!"));
                            }
                        } else {
                            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UG002: DEVE DIGITAR O FAX!"));
                        }
                    } else {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UG002: DEVE DIGITAR O TELEFONE!"));
                    }
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UG002: DEVE DIGITAR O ENDEREÇO!"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UG002: DEVE SELECCIONAR O PAÍS E A PROVÍNCIA!"));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UG002: DEVE DIGITAR O NOME"));
        }
    }

    /**
     * Método que actualiza os dados do registo.
     * @param o evento gerado pela página.
     */
    public void actulizar() {
        context = FacesContext.getCurrentInstance();
        if (modeloTemp.getId() != 0) {
            if (modeloTemp.getIdProvincia() != 0) {
                if (!modeloTemp.getNome().equals("")) {
                    if (controller.update(modeloTemp)) {
                        this.modeloTemp = new FornecedorModelo();
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "REGISTO ACTUALIZADO COM SUCESSO!"));
                    } else {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "ERRO N.º SA001: AO ACTUALIZADO O REGISTO! CONSULTE O SUPORTE."));
                    }
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UA003: DEVE DIGITAR O NOME!"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UG002: DEVE SELECCIONAR A PROVÍNCIA!"));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "AVISO N.º UA002: DEVE SELECCIOAR UM REGISTO!"));
        }
    }

    /**
     * Método que elimina os dados do registo.
     * @param o evento gerado pela página.
     */
    public void eliminar() {
        context = FacesContext.getCurrentInstance();
        if (!seleccionados.isEmpty()) {
            for (FornecedorModelo modeloEliminar : seleccionados) {
                if (controller.eliminar(modeloEliminar.getId(), NOME_TABELA)) {
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "ERRO N.º SG001: AO ELIMINAR O(S) REGISTO(S)! CONSULTE O SUPORTE."));
                    break;
                }
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", seleccionados.size() + " REGISTO(S) ELIMINADO(S) COM SUCESSO!"));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "ERRO AVISO N.º UE001: DEVE SELECCIONAR PELO MENOS UM REGISTO."));
        }
    }

    /**
     * Métodos que cria uma nova instância do objecto modelo.
     */
    public void novoModelo() {
        modelo = new FornecedorModelo();
        modeloTemp = new FornecedorModelo();
        seleccionados.clear();
        selection.clear();
    }
}
