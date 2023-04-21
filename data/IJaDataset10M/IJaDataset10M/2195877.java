package jaguar.machine.structures;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import jaguar.machine.util.*;
import jaguar.structures.State;
import jaguar.structures.jstructures.JState;
import jaguar.machine.structures.exceptions.DeltaNotFoundException;
import jaguar.structures.exceptions.StateNotFoundException;
import jaguar.structures.exceptions.SymbolNotFoundException;

public abstract class Delta implements Cloneable {

    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    public static final String ELEMENT_NAME = "delta";

    /**
     * El tag con el que se define el inicio del objeto de un 
     * en un archivo
     */
    public static final String BEG_TAG = "<" + ELEMENT_NAME + ">";

    /**
     * El tag con el que se define el fin del objeto de un 
     * en un archivo
     */
    public static final String END_TAG = "</" + ELEMENT_NAME + ">";

    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    public static final String TRANS_ELEMENT_NAME = "trans";

    /**
     * El tag con el que se define el inicio del objeto de un 
     * en un archivo
     */
    public static final String TRANS_BEG_TAG = "<" + TRANS_ELEMENT_NAME + ">";

    /**
     * El tag con el que se define el fin del objeto de un 
     * en un archivo
     */
    public static final String TRANS_END_TAG = "</" + TRANS_ELEMENT_NAME + ">";

    /**
     * La estructura donde se guardan las transiciones
     */
    protected Hashtable delta;

    /**
     * Regresa las llaves, i.e. los estados que tienen transiciones definidas.
     */
    public Enumeration keys() {
        return delta.keys();
    }

    /**
     * Iinicializa una funci�n de transici�n delta
     */
    public Delta() {
        delta = new Hashtable();
    }

    /**
     * Obtiene todas las transiciones de un estado p en delta
     * @return un vector de estados donde est�n todos los estoaos a los que se transfiere a partir de estado p 
     */
    public abstract Vector getTransitions(State p);

    /**
     * La representaci�n como cadena de la func�on de transici�n
     */
    public String toString() {
        String s = "";
        Object oKeys[] = delta.keySet().toArray();
        for (int i = 0; i < oKeys.length; i++) s += "\n\t\t " + oKeys[i] + " " + delta.get(oKeys[i]);
        return s;
    }

    public Hashtable getD() {
        return delta;
    }

    protected void setD(Hashtable d) {
        delta = d;
    }

    public Set keySet() {
        return delta.keySet();
    }

    /** 
     * Escribe la representaci�n de la funci�n de transici�n delta en un archivo con el formato definido por el DTD correspondiente
     * Escribe la delta con su representaci�n correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardar� la delta
     */
    public abstract void toFile(FileWriter fw);

    /** 
      * Regresa la representaci�n como cadena en html de las transiciones del estado <code>p</code> para los tooltips .
      *
      * @param p El estado del cual queremos todas las transiciones definidas.
      * @return la cadena en formato html para los tooltips.
      *
      */
    public abstract String getStringTransitions(State p);

    /**
     * Regresa una cadena con formato  html para los tooltips que nos muestra todas las transiciones deifinidas para el estado dado 
     * @param p el estado de quien queremos ver todas sus transiciones definidas
     **/
    public abstract String getToolTipString(State p);

    /**
     * Crea y regresa una copia de este objeto
     * @return crea y regresa la copia de este objeto
     */
    public Object clone() throws CloneNotSupportedException {
        try {
            Delta nuevo = (Delta) super.clone();
            nuevo.setD((Hashtable) getD().clone());
            return nuevo;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }
}
