package lg.commands.cliente;

import lg.servicios.api.ClientesServicio;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ClienteFormValidator implements Validator {

    public ClientesServicio clientesServicio;

    public boolean supports(Class arg0) {
        return arg0 == ClienteForm.class;
    }

    public void validate(Object command, Errors errors) {
        ClienteForm form = (ClienteForm) command;
        ValidationUtils.rejectIfEmpty(errors, "razonSocial", "error.campo.requerido");
        ValidationUtils.rejectIfEmpty(errors, "mail", "error.campo.requerido");
        ValidationUtils.rejectIfEmpty(errors, "telefono", "error.campo.requerido");
        ValidationUtils.rejectIfEmpty(errors, "responsable", "error.campo.requerido");
        ValidationUtils.rejectIfEmpty(errors, "direccion", "error.campo.requerido");
        ValidationUtils.rejectIfEmpty(errors, "ciudad", "error.campo.requerido");
        ValidationUtils.rejectIfEmpty(errors, "comision", "error.campo.requerido");
    }

    /**
	 * @return the clientesServicio
	 */
    public ClientesServicio getClientesServicio() {
        return clientesServicio;
    }

    /**
	 * @param clientesServicio the clientesServicio to set
	 */
    public void setClientesServicio(ClientesServicio clientesServicio) {
        this.clientesServicio = clientesServicio;
    }
}
