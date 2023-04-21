package org.ces.cagt.langage.vhdl.exceptions;

/**
 * dans cette classe on revoie toutes les erreures relative
 * à la creation d'un objet VHDL
 * **pas encore defini**
 * @author Mhamed Ben Jmaa
 */
public class VHDLExceptions extends Exception {

    /**
     * cree une nouvelle instance de VHDLExceptions
     */
    public VHDLExceptions() {
        super();
    }

    /**
     * cree une nouvelle instance de VHDLException
     * @param msg le message d'erreur qui va etre affiche
     */
    public VHDLExceptions(String msg) {
        super(msg);
    }

    private static final long serialVersionUID = 41;
}
