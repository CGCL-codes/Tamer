package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.*;
import org.compiere.util.*;

/**
 *	Test Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTest.java,v 1.3 2006/07/30 00:58:37 jjanke Exp $
 */
public class MTest extends X_Test {

    /**
	 * 	Constructor
	 *	@param ctx context
	 *	@param Test_ID
	 *	@param trxName transaction
	 */
    public MTest(Properties ctx, int Test_ID, String trxName) {
        super(ctx, Test_ID, trxName);
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MTest(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	Test Object Constructor
	 *	@param ctx context
	 *	@param testString test string
	 *	@param testNo test no
	 */
    public MTest(Properties ctx, String testString, int testNo) {
        super(ctx, 0, null);
        testString = testString + "_" + testNo;
        setName(testString);
        setDescription(testString + " " + testString + " " + testString);
        setHelp(getDescription() + " - " + getDescription());
        setT_Date(new Timestamp(System.currentTimeMillis()));
        setT_DateTime(new Timestamp(System.currentTimeMillis()));
        setT_Integer(testNo);
        setT_Amount(new BigDecimal(testNo));
        setT_Number(Env.ONE.divide(new BigDecimal(testNo), BigDecimal.ROUND_HALF_UP));
        setC_Currency_ID(100);
        setC_Location_ID(109);
        setC_UOM_ID(100);
    }

    /**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
    protected boolean beforeDelete() {
        log.info("***");
        return true;
    }

    /**
	 * 	After Delete
	 *	@param success
	 *	@return success
	 */
    protected boolean afterDelete(boolean success) {
        log.info("*** Success=" + success);
        return success;
    }

    /**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true
	 */
    protected boolean beforeSave(boolean newRecord) {
        log.info("New=" + newRecord + " ***");
        return true;
    }

    /**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 *	@return success
	 */
    protected boolean afterSave(boolean newRecord, boolean success) {
        log.info("New=" + newRecord + ", Seccess=" + success + " ***");
        return success;
    }

    /*************************************************************************
	 * 	Test
	 *	@param args
	 */
    public static void main(String[] args) {
        Adempiere.startup(true);
        Properties ctx = Env.getCtx();
        MTest t1 = new MTest(ctx, 0, null);
        t1.setName("Test1");
        System.out.println("->" + t1.getCharacterData() + "<-");
        t1.save();
        t1.setCharacterData("Long Text JJ");
        t1.save();
        int Test_ID = t1.getTest_ID();
        MTest t2 = new MTest(Env.getCtx(), Test_ID, null);
        System.out.println("->" + t2.getCharacterData() + "<-");
        t2.delete(true);
    }
}
