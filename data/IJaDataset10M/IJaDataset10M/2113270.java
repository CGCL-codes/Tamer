package com.googlecode.bdoc.mojo.diff;

import com.googlecode.bdoc.BDocConfig;
import com.googlecode.bdoc.diff.report.DiffReport;

/**
 *  @author Per Otto Bergum Christensen
 */
public interface DiffExecutor {

    public DiffReport createDiffReport(String oldBddDocFileName, String newBddDocFileName, BDocConfig bdocConfig);
}
