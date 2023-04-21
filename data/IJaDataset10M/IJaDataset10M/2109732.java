package com.dcivision.setup.dao;

import java.sql.Connection;
import com.dcivision.framework.SessionContainer;

/**
 *  SetupOptionSalutation main DAO
 *
 *  @author           Kennedy Lam
 *  @company          DCIVision Limited
 *  @creation date    28/07/2003
 *  @version          $Revision: 1.1 $
 */
public class SetupOptionSalutationDAObject extends AbstractSetupOptionDAObject {

    public static final String TABLE_NAME = "SETUP_OPTION_SALUTATION";

    public static final String OPTION_NAME = "SALUTATION_NAME";

    public static final String OPTION_CODE = null;

    public static final boolean HAS_OPTION_CODE = false;

    public static final String DEFAULT_ORDER = OPTION_NAME;

    /**
   * 
   */
    public SetupOptionSalutationDAObject() {
        super();
    }

    /**
   * @param dbConn
   */
    public SetupOptionSalutationDAObject(Connection dbConn) {
        super(dbConn);
    }

    /**
   * @param sessionContainer
   * @param dbConn
   */
    public SetupOptionSalutationDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.sOptionNameCol = OPTION_NAME;
        this.sOptionCodeCol = OPTION_CODE;
        this.sDefaultOrder = DEFAULT_ORDER;
        this.bHasOptionCode = HAS_OPTION_CODE;
        super.initDBSetting();
    }
}
