package it.cefriel.glue2;

import it.cefriel.glue2.discovery.NfpEvaluationManager;
import it.cefriel.glue2.discovery.ReasonerController;
import it.cefriel.glue2.discovery.wsml.WsmlReasonerController;
import it.cefriel.glue2.exceptions.GlueException;
import it.unimib.disco.itis.polimar.nfpEvaluation.WsmlNfpEvaluationManager;

/**
  * Factory class to create the main components and the service layer components
 */
public class ComponentFactory {

    private String config_dir = null;

    /**
      * Create a new factory
      * @param config_dir path of the directory containing the config files for the components
      * @throws GlueException
     */
    public ComponentFactory(String config_dir) throws GlueException {
        if ((config_dir == null) || (config_dir == "")) {
            throw new GlueException("You must specify valid directory");
        }
        this.config_dir = config_dir;
    }

    /**
      * Create a new ReasonerController service component
      * @param language_type type of language used (currently WSML)
      * @return a new ReasonerController
      * @throws GlueException
     */
    public ReasonerController createReasonerController(String language_type) throws GlueException {
        if ((language_type == null) || (language_type == "")) {
            throw new GlueException("You must specify a language type to create a new ReasonerController");
        }
        if (language_type.equalsIgnoreCase("wsml")) return new WsmlReasonerController(config_dir); else return null;
    }
}
