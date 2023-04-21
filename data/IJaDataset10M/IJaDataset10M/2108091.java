package fr.kaamelot.tools.ant.logic;

import com.bnpparibas.tools.ant.logic.query.*;

/**
 * @author V_Thoule
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Query_test {

    public static void main(String[] args) throws Exception {
        QueryTask qry = new QueryTask();
        qry.setName("Test");
        qry.setMessage("Test");
        qry.execute();
    }
}
