package nl.notenbomer.hanzerent.huurwoningen;

import javax.xml.namespace.QName;

public class ClientTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        EONInterfaceForElementsOfTypeHuurwoningenLocator a = new EONInterfaceForElementsOfTypeHuurwoningenLocator();
        java.util.Iterator ports = a.getPorts();
        QName portname = (QName) ports.next();
        try {
            EONInterfaceForElementsOfTypeHuurwoningenPortType pt = (EONInterfaceForElementsOfTypeHuurwoningenPortType) a.getPort(portname, EONInterfaceForElementsOfTypeHuurwoningenBindingStub.class);
            Huurwoning[] woningen = pt.search("Groningen", 0, 10000, 0, 20);
            for (int i = 0; i < woningen.length; i++) System.out.println(woningen[i].getImage_url());
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return;
    }
}
