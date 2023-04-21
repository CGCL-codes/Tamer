package mbeans;

import controller.FornecedorController;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import modelo.FornecedorModelo;

/**
 * Managed Bean da entidade que representa o Fornecedor.
 * @author Paulo Ilenga
 */
@ManagedBean
@SessionScoped
public class fornecedorMB implements Serializable {

    private FornecedorController controller;

    private FornecedorModelo modelo, modeloTemp;

    private FornecedorModelo[] seleccionados;

    private FacesContext context;

    private Date date;

    /** Constructor da classe sem argumentos */
    public fornecedorMB() {
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

    public FornecedorModelo[] getSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(FornecedorModelo[] seleccionados) {
        this.seleccionados = seleccionados;
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
                                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "GCBoutique", "Registo Guardado Com Sucesso!"));
                                } else {
                                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "GCBoutique", "Erro N.º SG001: Ao Guardar o Registo! Consulte o Suporte"));
                                }
                            } else {
                                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UG002: Deve Digitar o Email!"));
                            }
                        } else {
                            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UG002: Deve Digitar o Fax!"));
                        }
                    } else {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UG002: Deve Digitar o Telefone!"));
                    }
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UG002: Deve Digitar o Endereço!"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UG002: Deve Seleccionar o País e a Província!"));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UG002: Deve Digitar o Nome!"));
        }
    }

    /**
     * Método que actualiza os dados do registo.
     * @param o evento gerado pela página.
     */
    public void actulizar(ActionEvent event) {
        context = FacesContext.getCurrentInstance();
        if (modeloTemp.getId() != 0) {
            if (modeloTemp.getIdProvincia() != 0) {
                if (!modeloTemp.getNome().equals("")) {
                    if (controller.update(modeloTemp)) {
                        this.modeloTemp = new FornecedorModelo();
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "GCBoutique", "Registo Actualizado Com Sucesso!"));
                    } else {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "GCBoutique", "Erro N.º SA001: Ao Actualizado o Registo! Consulte o Suporte."));
                    }
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UA003: Deve Digitar o Nome!"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UG002: Deve Seleccionar a Província!"));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UA002: Deve Seleccioar um Registo!"));
        }
    }

    /**
     * Método que elimina os dados do registo.
     * @param o evento gerado pela página.
     */
    public void eliminar(ActionEvent event) {
        context = FacesContext.getCurrentInstance();
        if (modeloTemp.getId() != 0) {
            if (modeloTemp.getIdProvincia() != 0) {
                if (!modeloTemp.getNome().equals("")) {
                    if (controller.eliminar(modeloTemp.getId(), "tbl_fornecedor")) {
                        modeloTemp = new FornecedorModelo();
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "GCBoutique", "Registo Eliminado Com Sucesso!"));
                    } else {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "GCBoutique", "Erro N.º SE001: Ao eliminar os Dados! Consultar o Suporte"));
                    }
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UE001: Deve Seleccionar um registo!"));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Aviso N.º UG002: Deve Seleccionar a Província!"));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "GCBoutique", "Erro Aviso N.º UE001: Deve Seleccionar um registo."));
        }
    }

    /**
     * Método que limpa os atributos da entidade.
     */
    public void limpar() {
        modeloTemp.setId(0);
        modeloTemp.setNome("");
    }

    /**
     * Métodos que cria uma nova instância do objecto modelo.
     */
    public void novoModelo(ActionEvent event) {
        modeloTemp = new FornecedorModelo();
    }
}
