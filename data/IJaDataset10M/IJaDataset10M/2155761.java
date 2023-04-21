package es.cea.seamcea.session;

import java.io.Serializable;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import com.enjava.discografica.model.Cliente;

@Stateful
@Name(value = "editaCliente")
@Scope(ScopeType.CONVERSATION)
public class EditaClienteBean implements Serializable, EditaCliente {

    private static final long serialVersionUID = 1L;

    @Logger
    Log log;

    @In
    StatusMessages statusMessages;

    @PersistenceContext
    EntityManager entityManager;

    @In(value = "miComponente", create = true)
    MiComponente miComponente;

    @RequestParameter(value = "id")
    Long id;

    Cliente cliente;

    @Begin(join = true)
    public String inicia() {
        cliente = (Cliente) miComponente.getTabla().getRowData();
        System.out.println("**** acceso al objeto: " + cliente);
        return "/editaCliente.xhtml";
    }

    @End
    public void grabar() {
        log.info("*************** intentando modificar objeto con id: " + id + " ***************");
        entityManager.merge(cliente);
        statusMessages.add("Datos modificados correctamente");
    }

    @End
    public void borrar() {
        cliente = entityManager.find(Cliente.class, cliente.getId());
        log.info("*************** intentando borrar objeto con id: " + id + " ***************");
        entityManager.refresh(cliente);
        entityManager.remove(cliente);
        statusMessages.add("Elemento borrado");
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Destroy
    @Remove
    public void destroy() {
    }
}
