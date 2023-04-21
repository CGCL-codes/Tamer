package de.fraunhofer.isst.axbench.test;

import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Signal;

/**
 * Test class for generics
 * @author ekleinod
 *
 */
public class Generics2 extends Generics1 {

    public Signal getElement() {
        System.out.println("Signal gen2.getElement()");
        return null;
    }

    public void setElement(Signal newElement) {
        System.out.println("void gen2.setElement(Signal newElement)");
    }

    public <T extends IAXLangElement> void setElement2(T newElement) {
        if (newElement instanceof Signal) {
            System.out.println("void gen2.setElement2(T newElement) - Signal");
        } else {
            System.out.println("void gen2.setElement2(T newElement) - Other");
        }
    }

    public void setElement2(Signal newElement) {
        System.out.println("void gen2.setElement2(Signal newElement)");
    }

    public void setElement3(Signal newElement) {
        System.out.println("void gen1.setElement3(IAXLangElement newElement)");
    }

    public void getA(IAXLangElement e) {
    }

    public void getA(Signal e) {
    }

    public Signal setAndReturnElement(Signal newElement) {
        System.out.println("Signal gen2.setAndReturnElement(Signal newElement)");
        return newElement;
    }
}
