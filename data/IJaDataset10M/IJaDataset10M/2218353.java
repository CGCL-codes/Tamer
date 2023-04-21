package it.javalinux.wise.seam.actions;

import it.javalinux.support.WiseTest;
import it.javalinux.wise.jaxCore.Endpoint;
import it.javalinux.wise.jaxCore.WSDynamicClient;
import it.javalinux.wise.jaxCore.WebParameter;
import it.javalinux.wise.seam.entities.SavedWsdl;
import it.javalinux.wise.seam.entities.treeElement.WiseTreeElement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class WSDLManagerTest extends WiseTest {

    public WSDLManagerTest() {
    }

    @Test(groups = { "Fine" })
    public void testPingLong() throws Exception {
        System.out.println("Step #1: wsdlManager client creation...");
        WSDLManagerBean manager = new WSDLManagerBean();
        SavedWsdl wsdl = new SavedWsdl();
        String strUrl = "http://" + getServerHost() + ":8080/100WebServicesTest-WebServicesTest./WSTestAnnotation?wsdl";
        wsdl.setWsdlLink(strUrl);
        manager.selectWsdl(wsdl);
        manager.readEndpoints();
        manager.readOperations();
        manager.selectedRequest = "pingLong";
        manager.prepareCall();
        for (Iterator it = manager.rootElement.getChildren(); it.hasNext(); ) {
            WiseTreeElement wiseElem = (WiseTreeElement) (((Entry) it.next()).setValue(new Long(1)));
        }
        manager.doCall();
        System.out.print(manager.getCode());
    }
}
