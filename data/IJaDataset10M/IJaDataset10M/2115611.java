package net.sourceforge.simulaeco.gui.acoes;

import org.apache.log4j.Logger;
import org.jdesktop.application.Action;

/**
 * @author Rafael Gonzaga Camargo
 * @date 03/03/2010 {@literal Descri��o: ???}
 */
public class AjudaAction {

    private static final Logger LOG = Logger.getLogger(AjudaAction.class);

    private static AjudaAction instance;

    /**
     * Tela de ajuda da aplica��o
     * 
     * @return {@link AjudaAction}
     */
    public static final AjudaAction getInstance() {
        if (instance == null) instance = new AjudaAction();
        return instance;
    }

    /**
     * Construtor oculto pelo Singleton
     */
    private AjudaAction() {
        super();
    }

    /**
     * Mostra a tela de ajuda
     */
    @Action
    public void ajuda() {
        LOG.info("Inicio");
    }
}
