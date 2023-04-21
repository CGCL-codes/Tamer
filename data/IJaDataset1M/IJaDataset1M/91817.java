package com.calipso.reportgenerator.userinterface;

import com.calipso.reportgenerator.common.ReportFilterSpec;
import com.calipso.reportgenerator.common.ReportSpec;

/**
 * Representa un elemento de tipo range de
 * la coleccion <code>UPCollection</code>.
 * Conoce todos los datos necesarios de 1 filtro para una dimension.
 */
public class UPRangeElement extends UPCollectionElement {

    private String startingArgument;

    private String endingArgument;

    private String startingTfCaption;

    private String endingTfCaption;

    public UPRangeElement(ReportFilterSpec filterDefinition, ReportSpec reportSpec, String startingArgument, String endingArgument, String startingTfCaption, String endingTfCaption) {
        super(filterDefinition, reportSpec);
        this.startingArgument = startingArgument;
        this.endingArgument = endingArgument;
        this.startingTfCaption = startingTfCaption;
        this.endingTfCaption = endingTfCaption;
    }

    public String getStartingTfCaption() {
        return startingTfCaption;
    }

    public String getEndingTfCaption() {
        return endingTfCaption;
    }

    public String getStartingArgument() {
        return startingArgument;
    }

    public String getEndingArgument() {
        return endingArgument;
    }

    public ReportSpec getReportSpec() {
        return super.getReportSpec();
    }

    public ReportFilterSpec getFilterDefinition() {
        return super.getFilterDefinition();
    }

    public UPPanel getVisualComponent() {
        return new UPRangePanel(this);
    }
}
