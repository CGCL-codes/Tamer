package com.simpledata.bc.components.worksheet.dispatcher;

import org.apache.log4j.Logger;
import com.simpledata.bc.components.TarifTreeVisitor;
import com.simpledata.bc.components.bcoption.OptionFuture;
import com.simpledata.bc.datamodel.BCOption;
import com.simpledata.bc.datamodel.WorkSheetContainer;

/**
 * Default Dispatcher for Futures
 */
public class FuturesRoot0 extends DispatcherRoot {

    /** TITLE OF THIS WORK SHEET -- should be translated in Lang Directories **/
    public static final String WORKSHEET_TITLE = "Futures Root V0";

    private static final Logger m_log = Logger.getLogger(FuturesRoot0.class);

    public FuturesRoot0(WorkSheetContainer parent, String title, String id, String key) {
        super(parent, title, id, key);
    }

    /**
     * @see DispatcherRoot#proportionOf(DispatcherRoot)
     */
    public double proportionOf(DispatcherRoot dr) {
        m_log.error("todo");
        return 0;
    }

    /**
	 * @see com.simpledata.bc.datamodel.WorkSheet#getAcceptedNewOptions()
	 */
    public Class[] getAcceptedNewOptions() {
        return new Class[] { OptionFuture.class };
    }

    /**
	 * @see com.simpledata.bc.datamodel.WorkSheet#getAcceptedRemoteOptions()
	 */
    public Class[] getAcceptedRemoteOptions() {
        return new Class[0];
    }

    /**
	 * @see com.simpledata.bc.datamodel.WorkSheet#_canRemoveOption(BCOption bco)
	 */
    public boolean _canRemoveOption(BCOption bco) {
        return true;
    }

    /**
	 * Visitor implementation. 
	 * @param v Visitor to call back to. 
	 */
    public void visit(TarifTreeVisitor v) {
        v.caseFuturesRoot0(this);
    }

    /** XML CONSTRUCTOR **/
    public FuturesRoot0() {
    }
}
