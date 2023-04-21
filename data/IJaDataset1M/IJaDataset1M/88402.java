package org.opencube.oms.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.opencube.data.BoundVariable;
import org.opencube.oms.OMSDataFactory;
import org.opencube.oms.OMSData;
import org.opencube.oms.OMSElement;
import org.opencube.oms.OMSStructure;

public class RunTest2 {

    private static SimpleDateFormat m_dt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

    public static void main(String[] args) {
        try {
            System.out.println("Start................................................: " + m_dt.format(new Date()));
            for (int i = 1; i < 100; i++) test(i);
            System.out.println("End..................................................: " + m_dt.format(new Date()));
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void test(int num) throws Exception {
        System.out.println("\n*********TEST " + num);
        BoundVariable[] vars = new BoundVariable[] { new BoundVariable("out_cursor_result", null), new BoundVariable("in_string_username", "username dt: simple192.168.0.118:76e369:fe0b571c8d:-7fe9") };
        System.out.println("After boundvariable..................................: " + m_dt.format(new Date()));
        OMSData omsData = OMSDataFactory.getOMSData("oms_keytec_stage");
        System.out.println("After init omsdata...................................: " + m_dt.format(new Date()));
        System.out.println("+++ First query");
        OMSStructure struct = omsData.getOMSStructureByResource("account", null, vars);
        System.out.println("After get omsstruct..................................: " + m_dt.format(new Date()));
        OMSElement[] ele = struct.getElements();
        for (int i = 0; i < ele.length; i++) {
            System.out.println("+++ Element " + i + ": " + ele[i].getDisplayName());
        }
        System.out.println("End of query 1.......................................: " + m_dt.format(new Date()));
    }
}
