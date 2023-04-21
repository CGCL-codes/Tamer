package com.patientis.business.settings;

import java.util.ArrayList;
import java.util.List;
import com.patientis.framework.controls.table.DefaultTableColumn;
import com.patientis.framework.controls.table.ISColumn;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.common.Converter;
import com.patientis.model.reference.RecordItemReference;
import com.patientis.model.reference.SettingsPatientListColumnsReference;

/**
 * @author gcaulton
 *
 */
public class DefaultCustomMessageListSettings extends DefaultCustomTableColumnSettings {

    private FormModel form = null;

    public DefaultCustomMessageListSettings(FormModel form) {
        super(form);
        this.form = form;
    }
}
