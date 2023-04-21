package br.ufes.xpflow.module.validation;

import br.ufes.xpflow.core.flow.execution.ExecutionContext;
import br.ufes.xpflow.core.main.XPFlowException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Welton
 * Date: 26/07/2007
 * Time: 02:13:34
 * Operacao de validacao de um
 * documento xml completo
 */
public class Operation {

    private String id;

    private String errorstatus = "-1";

    private final Collection<Validator> validators = new ArrayList<Validator>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorstatus() {
        return errorstatus;
    }

    public void setErrorstatus(String errorstatus) {
        this.errorstatus = errorstatus;
    }

    public void addValidator(Validator v) {
        validators.add(v);
    }

    public void execute(ExecutionContext ctxt) throws XPFlowException {
        boolean valid = true;
        for (Validator v : validators) {
            if (!v.validate(ctxt)) valid = false;
        }
        if (!valid) ctxt.setStatus(errorstatus);
    }
}
