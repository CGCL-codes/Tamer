package ar.edu.unlp.info.diseyappweb.model;

import java.util.Set;
import ar.edu.unlp.info.diseyappweb.controller.EntidadFactory;
import ar.edu.unlp.info.diseyappweb.model.exception.ErrorManejoRegla;
import ar.edu.unlp.info.diseyappweb.model.rules.ClienteRuleWrapper;
import ar.edu.unlp.info.diseyappweb.model.ui.IRolVisitor;
import ar.edu.unlp.info.poo.rules.rule.RuleObjectType;

/**
 * 
 * @author Martinelli, Federico
 * @author Ducos, David
 */
public abstract class Cliente extends Rol {

    /**
	 * 
	 */
    public Object aceptar(IRolVisitor visitante) {
        return visitante.visitaCliente(this);
    }

    /**
	 * Agrega una nueva orden de trabajo para el cliente.
	 * 
	 * @param ot Orden de trabajo asociada con el cliente.   
	 */
    public abstract void agregarOrdenTrabajo(OrdenTrabajo ot);

    public abstract ClienteRuleWrapper getClienteRuleWrapper();

    /**
	 * Agrega una regla a un cliente de la compania.
	 * 
	 * @param fabrica Componente que implanta la regla sobre el cliente.
	 * @param nombreOperacion Nombre de la operación sobre la que se aplicará la regla.
	 * @param regla Regla que se desea agregar.
	 * @return Cliente que contempla la aplicación de la regla. 
	 */
    public abstract Cliente agregarReglaACliente(EntidadFactory fabrica, String nombreOperacion, RuleObjectType regla);

    /**
	 * Quita una regla a un cliente.
	 * 
	 * @param fabrica Componente que retira la regla sobre el cliente.
	 * @param nombreOperacion Nombre de la operación sobre la que se aplicaba la regla.
	 * @param regla Regla que se desea quitar.
	 * @return Cliente que deja de contemplar la aplicación de la regla.
	 * @throws ErrorManejoRegla Ocurre al realizar la operación de quitar la regla sobre
	 * una cliente que no contiene ninguna.
	 */
    public abstract Cliente quitarReglaACliente(EntidadFactory fabrica, String nombreOperacion, RuleObjectType regla) throws ErrorManejoRegla;

    public abstract Set<OrdenTrabajo> getOrdenesDeTrabajo();

    public abstract void setOrdenesDeTrabajo(Set<OrdenTrabajo> ordenesDeTrabajo);
}
