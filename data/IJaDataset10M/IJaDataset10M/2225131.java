package psparser.psobject;

import java.util.*;

/**
 * Implementation of PostScript Array object.
 * Gets implementation of PS Array, to define new
 * procedure, array, matrix.
 *
 * It is a wrapper ArrayList to PSObject
 *
 * @author Micka�l Villers
 */
public class PSArray extends PSObject {

    /**
    * default constructor,
    * Set to non literal by default
    */
    public PSArray() {
        this.value = new ArrayList();
        this.mode = 1;
    }

    /** 
    * constructs a prefined lentgh array fills with PSNull
    *
    * @param a default size of array
    */
    public PSArray(int a) {
        this();
        int i;
        for (i = 0; i < a; i++) {
            this.add(new PSNull());
        }
    }

    /**
    * Visual description for debug
    *
    * @return {} for procedure, [] for classical array
    */
    public String toString() {
        String a;
        int i;
        a = this.isLiteral() ? "[" : "{";
        for (i = 0; i < ((ArrayList) value).size(); i++) {
            a += " " + ((ArrayList) value).get(i) + " ";
        }
        a += this.isLiteral() ? "]" : "}";
        return a;
    }

    /**
    * Pushes an element in array
    *
    * @param obj element to push in this
    */
    public void add(PSObject obj) {
        ((ArrayList) value).add(obj);
    }

    /**
    * Sets an element at an index
    *
    * @param i index to set element 
    * @param obj element to set 
    */
    public void set(int i, PSObject obj) {
        ((ArrayList) value).set(i, obj);
    }

    /**
    * Converts a classical array into a procedure
    */
    public void toProcedure() {
        this.mode = 0;
    }

    /**
    * Executes elements stored in this Array by getting them one by one and 
    * gives them to the stack
    */
    public void process(PSStack psstack) {
        int i;
        for (i = 0; i < ((ArrayList) this.value).size(); i++) {
            psstack.toContext((PSObject) ((ArrayList) this.value).get(i));
            psstack.operandDebug();
        }
    }

    /**
    * Fixes all references that can be fixed in array!
    */
    public void bind(PSStack psstack) {
        int i;
        PSObject name;
        PSObject obj;
        for (i = 0; i < ((ArrayList) this.value).size(); i++) {
            obj = (PSObject) ((ArrayList) this.value).get(i);
            try {
                name = psstack.getFromDict(obj.toString());
                if (name.isOperator()) {
                    System.out.println("On bind un op�rator " + name);
                    ((ArrayList) this.value).set(i, name);
                    System.out.println("Bind�");
                }
            } catch (Exception e) {
            }
        }
    }
}
