package org.eyrene.javaj.mvc;

/**
 * <p>Title: View.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 * 
 * @deprecated use org.eyrene.javaj.pattern.MVCView
 */
public interface View {

    /**
     * Assegna il model specificato alla view
     * 
     * @param model model da assegnare
     */
    public void assign(Model model);

    /**
     * Deassegna il model specificato dalla view
     * 
     * @param model model da deassegnare
     */
    public void deassign(Model model);

    /**
     * Registra il controller passato alla view
     * 
     * @param controller controller che desidera registrarsi presso la view
     * @throws NullPointerException se il controller e' null
     */
    public void attach(Controller controller);

    /**
     * Cancella il controller dalla view
     * 
     * @param controller controller che desidera cancellarsi dalla view
     * @throws NullPointerException se il controller e' null
     */
    public void detach(Controller controller);

    /**
     * Accetta l'update dal model
     * (presumibilmente controlla se il model era tra quelli attesi..!)
     * 
     * @param model model che effettua la notifica
     * @param obj oggetto (opzionale) notificato
     */
    public void update(Model model, Object obj);
}
