package com.illunus.di.trans.steps.trimcut;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * @author Sven Boden (svenboden@illunus.com)
 * @since 28 September 2007
 */
public class TrimCutData extends BaseStepData implements StepDataInterface {

    public int inStreamNrs[];

    public int outStreamNrs[];

    public RowMetaInterface outputRowMeta;

    /** Runtime trim operators */
    public int trimOperators[];

    /** sizes to cut to */
    public int cutSizes[];

    /**
	 * Default constructor.
	 */
    public TrimCutData() {
        super();
    }
}
