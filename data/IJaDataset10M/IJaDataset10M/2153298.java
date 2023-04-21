package org.colimas.test.mapper;

import java.sql.Timestamp;
import java.util.Calendar;
import org.colimas.db.mapper.SQLQueriesCodes;
import org.colimas.db.transaction.TxController;
import org.colimas.entity.WebServiceEntity;
import org.colimas.mapper.WebServiceEntityMapper;

/**
 * <h3>WebServiceEntityMapperCase.java</h3>
 *
 * <P>
 * Function:<BR />
 * 
 * </P>
 * @author zhao lei
 * @version 1.0
 * <br>
 * Modification History:
 * <PRE>
 * SEQ DATE       ORDER DEVELOPER      DESCRIPTION
 * --- ---------- ----- -------------- -----------------------------
 * 001 2006/01/08          tyrone        INIT
 * </PRE>
 */
public class WebServiceEntityMapperCase implements SQLQueriesCodes {

    /**
     * <p>Test</p>
     * @param args
     */
    public static void main(String[] args) {
        if (args[0] == null || args[0] == "") {
            System.out.println("please give the installedpath");
            return;
        }
        System.setProperty("installedpath", args[0]);
        WebServiceEntityMapper ceMap = WebServiceEntityMapper.getInstance();
        WebServiceEntity ce = new WebServiceEntity();
        TxController txc = TxController.get();
        try {
            txc.begin();
            ce.setUrn("colimas.org/webservice/test0");
            ce.setComponentSerialNo("123456789012345678901234561");
            ce.setDefaultMappingRegistryClass("org.colimas.test0");
            ce.setDeployedBy("ZhaoLei");
            ce.setDeployedDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            ce.setFunction("Test webservice for Colimas");
            ce.setIsPublic(1);
            ce.setIsStaticClass(0);
            ce.setMethods("execute");
            ce.setProviderClass("org.colimas.test1");
            ce.setScope(0);
            ce.setServerUrl("colimas.org/webserivce");
            ce.setVisitedTimes(1);
            int record = ceMap.update(ce);
            if (record == 0) System.out.println("failure."); else System.out.println(record + " records have benn updated");
            txc.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
