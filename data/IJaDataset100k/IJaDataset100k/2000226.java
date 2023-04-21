package com.azirar.dna.beans;

import java.util.Comparator;
import java.util.TreeSet;
import com.azirar.dna.dao.ActionDAO;
import com.azirar.dna.entities.Action;
import com.azirar.dna.tools.ApplicationException;
import com.azirar.dna.tools.Response;
import com.azirar.dna.tools.TypeResponse;

/**
 * Class ActionBean.
 */
public class ActionBean {

    /**
     * Adds the.
     *
     * @param idService the id service
     * @param node the node
     * @return the response
     */
    public Response add(int idService, String node) {
        ActionDAO dao = new ActionDAO();
        try {
            dao.addOrUpdate(0, idService, node);
        } catch (ApplicationException ae) {
            return new Response(TypeResponse.ERROR, "Erreur Application : " + ae.getMessage());
        }
        return new Response(TypeResponse.ALERT, "Ajout R�ussi");
    }

    /**
     * Update.
     *
     * @param idAction the id action
     * @param idService the id service
     * @param node the node
     * @return the response
     */
    public Response update(int idAction, int idService, String node) {
        ActionDAO dao = new ActionDAO();
        try {
            dao.addOrUpdate(idAction, idService, node);
        } catch (ApplicationException ae) {
            return new Response(TypeResponse.ERROR, "Erreur Application : " + ae.getMessage());
        }
        return new Response(TypeResponse.ALERT, "Modification R�ussie");
    }

    /**
     * Supprimer.
     *
     * @param idAction the id action
     * @return the response
     */
    public Response supprimer(int idAction) {
        ActionDAO dao = new ActionDAO();
        try {
            dao.remove(idAction);
        } catch (ApplicationException ae) {
            return new Response(TypeResponse.ERROR, "Erreur Application : " + ae.getMessage());
        }
        return new Response(TypeResponse.ALERT, "Suppression R�ussie");
    }

    /**
     * Retourne : all.
     *
     * @return the all
     */
    public Response getAll() {
        ActionDAO dao = new ActionDAO();
        try {
            return new Response(TypeResponse.NOTHING, dao.getAll());
        } catch (ApplicationException ae) {
            return new Response(TypeResponse.ERROR, "Erreur Application : " + ae.getMessage(), null);
        }
    }

    /**
     * Retourne : all by id service.
     *
     * @param idService the id service
     * @return the all by id service
     */
    public Response getAllByIdInteraction(int idInteraction) {
        ActionDAO dao = new ActionDAO();
        try {
            TreeSet<Action> actions = new TreeSet<Action>(new Comparator<Action>() {

                public int compare(Action o1, Action o2) {
                    if (o1.getIdAction() < o2.getIdAction()) {
                        return -1;
                    } else if (o1.getIdAction() > o2.getIdAction()) {
                        return 1;
                    }
                    return 0;
                }
            });
            actions.addAll(dao.getAllByIdInteraction(idInteraction));
            return new Response(TypeResponse.NOTHING, actions);
        } catch (ApplicationException ae) {
            return new Response(TypeResponse.ERROR, "Erreur Application : " + ae.getMessage(), null);
        }
    }

    /**
     * Retourne : by id.
     *
     * @param idAction the id action
     * @return the by id
     */
    public Response getById(int idAction) {
        ActionDAO dao = new ActionDAO();
        try {
            return new Response(TypeResponse.NOTHING, dao.getById(idAction));
        } catch (ApplicationException ae) {
            return new Response(TypeResponse.ERROR, "Erreur Application : " + ae.getMessage(), null);
        }
    }
}
