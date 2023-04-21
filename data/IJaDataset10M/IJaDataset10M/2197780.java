package net.sf.RecordEditor.layoutWizard;

import javax.swing.JComponent;
import net.sf.JRecord.Common.Constants;
import net.sf.RecordEditor.utils.common.Common;
import net.sf.RecordEditor.utils.wizards.AbstractWizardPanel;

@SuppressWarnings("serial")
public class Pnl2FileFormat extends PnlUnknownFileFormat implements AbstractWizardPanel<Details> {

    private Details wizardDetails;

    private int initRecLength, initFormat;

    /**
	 * 
	 */
    public Pnl2FileFormat() {
        super();
        this.setHelpURL(Common.formatHelpURL(Common.HELP_WIZARD_FILE_STRUCTURE));
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public Details getValues() throws Exception {
        wizardDetails.fileStructure = super.getFileStructure();
        wizardDetails.fontName = super.fontNameTxt.getText();
        wizardDetails.textPct = textPct;
        int len;
        if (wizardDetails.fileStructure == Common.IO_FIXED_LENGTH) {
            try {
                len = Integer.parseInt(super.lengthTxt.getText());
            } catch (Exception e) {
                super.lengthTxt.requestFocus();
                throw new Exception("Invalid Record Length: " + e.getMessage());
            }
            if (len <= 0) {
                super.lengthTxt.requestFocus();
                throw new Exception("Invalid Record Length: " + len);
            }
            wizardDetails.recordLength = len;
        } else {
            try {
                len = Integer.parseInt(super.lengthTxt.getText());
                if (len >= 0) {
                    wizardDetails.recordLength = len;
                }
            } catch (Exception e) {
            }
        }
        if (initFormat != wizardDetails.fileStructure || (initFormat == Constants.IO_FIXED_LENGTH && initRecLength != wizardDetails.recordLength)) {
            wizardDetails.setFieldSearch();
        }
        return wizardDetails;
    }

    @Override
    public void setValues(Details detail) throws Exception {
        wizardDetails = detail;
        super.fontNameTxt.setText(detail.fontName);
        initFormat = detail.fileStructure;
        initRecLength = wizardDetails.recordLength;
        if (wizardDetails.recordLength > 0) {
            super.lengthTxt.setText(Integer.toString(wizardDetails.recordLength));
        }
        super.open(detail.filename);
        if (detail.fileStructure == Constants.IO_FIXED_LENGTH) {
            int structure = super.getFileStructure();
            switch(structure) {
                case (Constants.IO_BIN_TEXT):
                case (Constants.IO_TEXT_LINE):
                case (Constants.IO_UNICODE_TEXT):
                    initFormat = Constants.IO_FIXED_LENGTH;
                    super.structureCombo.setSelectedItem(Integer.valueOf(Constants.IO_FIXED_LENGTH));
            }
        }
    }

    @Override
    public boolean skip() {
        boolean ret = true;
        switch(wizardDetails.fileStructure) {
            case Constants.IO_FIXED_LENGTH:
            case Constants.IO_UNKOWN_FORMAT:
                ret = false;
                break;
            case Constants.IO_DEFAULT:
                int fs = super.getFileStructure();
                switch(fs) {
                    case Constants.IO_VB:
                    case Constants.IO_VB_DUMP:
                    case Constants.IO_VB_FUJITSU:
                    case Constants.IO_VB_OPEN_COBOL:
                    case Constants.IO_MICROFOCUS:
                    case Constants.IO_FIXED_LENGTH:
                        wizardDetails.fileStructure = fs;
                        ret = false;
                }
        }
        return ret;
    }
}
