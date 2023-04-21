package com.example.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 04-26-2008
 * 
 * XDoclet definition:
 * 
 * @struts.form name="geneStrainQueryForm"
 */
public class GeneStrainQueryForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String function = "enter value";

    private String localization = "enter value";

    private String proteinSize = "enter value";

    private String signalSequence = "enter value";

    private String lipoProtein = "enter value";

    private String functionValue = "no";

    private String localizationValue = "no";

    private String proteinSizeValue = "no";

    private String signalSequenceValue = "no";

    private String lipoProteinValue = "no";

    private String[] selectedItems = new String[100];

    private String checked = "no";

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getProteinSize() {
        return proteinSize;
    }

    public void setProteinSize(String proteinSize) {
        this.proteinSize = proteinSize;
    }

    public String getSignalSequence() {
        return signalSequence;
    }

    public void setSignalSequence(String signalSequence) {
        this.signalSequence = signalSequence;
    }

    public String getLipoProtein() {
        return lipoProtein;
    }

    public void setLipoProtein(String lipoProtein) {
        this.lipoProtein = lipoProtein;
    }

    /**
	 * Method validate
	 * 
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    /**
	 * Method reset
	 * 
	 * @param mapping
	 * @param request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public String getFunctionValue() {
        return functionValue;
    }

    public void setFunctionValue(String functionValue) {
        this.functionValue = functionValue;
    }

    public String getLocalizationValue() {
        return localizationValue;
    }

    public void setLocalizationValue(String localizationValue) {
        this.localizationValue = localizationValue;
    }

    public String getProteinSizeValue() {
        return proteinSizeValue;
    }

    public void setProteinSizeValue(String proteinSizeValue) {
        this.proteinSizeValue = proteinSizeValue;
    }

    public String getSignalSequenceValue() {
        return signalSequenceValue;
    }

    public void setSignalSequenceValue(String signalSequenceValue) {
        this.signalSequenceValue = signalSequenceValue;
    }

    public String getLipoProteinValue() {
        return lipoProteinValue;
    }

    public void setLipoProteinValue(String lipoProteinValue) {
        this.lipoProteinValue = lipoProteinValue;
    }

    public String[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(String[] selectedItems) {
        this.selectedItems = selectedItems;
    }
}
