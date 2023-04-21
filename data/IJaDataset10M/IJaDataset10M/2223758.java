package net.sf.istcontract.aws.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import net.sf.istcontract.aws.sensor.ontology.ActionReport;
import uk.solanki.ms.autotrace.CRM;
import uk.solanki.ms.autotrace.Component;

public class TraceWrapper {

    /**
	 * @param args
	 */
    private static String componentName = "supplier";

    private static String pathName = "/home/monika/workspace/TraceGeneration/verics/examples/eContract/";

    private static String taFileName = "SupplierC";

    private static File f = null;

    private static CRM crm;

    private static HashMap<String, String> mapping;

    static {
        mapping = new HashMap<String, String>();
        mapping.put("submit_order", "receivePO");
        mapping.put("notify_purchaser_about_purchaser_order", "acceptPO");
        mapping.put("notify_supplier_about_download", "downloadGoods");
    }

    private static HashMap result = new HashMap();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Entering main");
        crm = new CRM(taFileName, pathName, componentName);
        result = new HashMap();
        f = new File("results.txt");
        if (f.exists()) {
            f.delete();
            f = new File("results.txt");
        }
        checkClauseS001("S001");
        System.out.println("Exiting main");
    }

    public TraceWrapper() {
        crm = new CRM("SupplierC", "/tmp/", "supplier");
    }

    public void trace(Object ar) {
    }

    private static void checkClauseS001(String clause) throws InterruptedException {
        System.out.println("Entering checkClauseS001");
        System.out.println("SO01: Notify Purchaser of acceptance or rejection of Purchase order within 7 days after receipt of purchase order");
        String results = "";
        crm.monitorVar("receivePO", 1);
        result = crm.getMonitoringResults();
        Thread.sleep(1 * 60 * 1000);
        crm.next();
        result = crm.getMonitoringResults();
        System.out.println("step 0..." + result);
        crm.monitorVar("acceptPO", 1);
        result = crm.getMonitoringResults();
        Thread.sleep(1 * 60 * 1000);
        crm.next();
        result = crm.getMonitoringResults();
        System.out.println("step 1..." + result);
        printResult(result);
        System.out.println("Exiting checkClauseS001");
    }

    private static void writeLocationDetailsToFile(String componentName2, String locationName, String locationName2) {
        System.out.println("Entering writeLocationDetailsToFile");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
            out.write(componentName2 + ", " + locationName + ", " + locationName2 + ", ");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Exiting writeLocationDetailsToFile");
    }

    private static void writeResultToFile(String dateTime, String clause, String result) {
        System.out.println("Entering writeResultToFile");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
            out.write(dateTime + ", " + clause + ": " + result + "\n\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Exiting writeResultToFile");
    }

    private static void printResult(HashMap result) {
        System.out.println("Entering printResult()");
        Iterator it = result.keySet().iterator();
        HashMap clock = new HashMap();
        HashMap var = new HashMap();
        while (it.hasNext()) {
            Object nameObj = it.next();
            System.out.println("name ..." + nameObj.toString());
            Object obj = result.get(nameObj);
            if (obj.getClass().toString().contains("Component")) {
                clock = ((Component) obj).getClockValuation();
                var = ((Component) obj).getVarValuation();
                System.out.println("clock: " + clock);
                System.out.println("variable: " + var);
            } else System.out.println(obj.toString());
        }
        System.out.println("Exiting printResult()");
    }
}
