package domain.simplex;

import domain.model.Equals;
import domain.model.LinearTerm;
import domain.model.Restriction;

/**
 *  Clase que representan a las restricci�n convertidas.
 */
public class SimplexRestriction {

    private Restriction orig;

    private Restriction rest;

    private LinearTerm slackTerm;

    private LinearTerm fictionalTerm;

    /**
     * Contruye una restricci�n simplex.
     * @param rest Restricci�n original
     * @param slackTerm Termino slack a agregarle o null si no tiene
     * @param fictionalTerm Termino ficticio a agregarle o null si no tiene.
     */
    public SimplexRestriction(Restriction rest, LinearTerm slackTerm, LinearTerm fictionalTerm) {
        this.orig = rest.clone();
        this.rest = rest;
        this.slackTerm = slackTerm;
        this.fictionalTerm = fictionalTerm;
        if (slackTerm != null) this.rest.getLe().add(slackTerm);
        if (fictionalTerm != null) this.rest.getLe().add(fictionalTerm);
        this.rest.setRelationShip(Equals.getInstance());
    }

    /**
     * Obtiene la restricci�n expandida.
     * @return La restricci�n expandida.
     */
    public Restriction getExpandedRest() {
        return this.rest;
    }

    /**
     * Obtiene la restricci�n original.
     * @return La restricci�n original.
     */
    public Restriction getOriginalRest() {
        return this.orig;
    }

    /**
     * Devuelve el termino que figurar� en la base del simplex.
     * @return Si tiene variable ficticia devuelve el termino ficticio, sino
     * devuelve el termino slack.
     */
    public LinearTerm getBaseTerm() {
        return (fictionalTerm != null) ? fictionalTerm : slackTerm;
    }

    /**
     * Devuelve el termino slack adicional.
     * @return Si tiene variable slack devuelve el terminok.
     */
    public LinearTerm getSlackTerm() {
        return slackTerm;
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append(this.rest.getName() + ") " + this.rest.getLe() + " = " + this.rest.getB() + System.getProperty("line.separator"));
        res.append(this.rest.getLe().getValue() + " = " + this.rest.getB() + ", is fullfill: " + this.rest.isFulfil());
        return res.toString();
    }
}
