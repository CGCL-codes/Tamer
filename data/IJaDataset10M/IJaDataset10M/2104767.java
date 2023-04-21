package com.loribel.tools.web.gui.tn;

import com.loribel.tools.web.abstraction.GBW_BORepository;
import com.loribel.tools.web.abstraction.GBW_FilesReportMgr;

/**
 * GBW_FilesTN. 
 * 
 * @author Gregory Borelli
 */
public class GBW_FilesReportDocsTN extends GBW_FilesReportAbstractTN {

    public GBW_FilesReportDocsTN(GBW_BORepository a_repository) {
        super("Documents", a_repository, AA.DIR_NAME_REPORTS, GBW_FilesReportMgr.NAMES_DOCS);
    }
}
