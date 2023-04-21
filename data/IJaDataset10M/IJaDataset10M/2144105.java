package br.com.wepa.webapps.orca.controle.actions.construtora;

import br.com.wepa.webapps.logger.TraceLogger;
import br.com.wepa.webapps.orca.controle.actions.struts.GenericAction;
import br.com.wepa.webapps.orca.logica.negocio.facade.FacadeDelegator;
import br.com.wepa.webapps.orca.logica.negocio.facade.basic.GenericFacadeHome;

public class ConstrutoraAction extends GenericAction {

    /**
     * Objeto de Log
     */
    private static TraceLogger logger = new TraceLogger(ConstrutoraAction.class);

    public ConstrutoraAction() {
        super();
    }

    @Override
    protected GenericFacadeHome getFacade() {
        return FacadeDelegator.getCosntrutoraFacade();
    }
}
