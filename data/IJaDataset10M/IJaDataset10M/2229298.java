package org.jaffa.modules.printing.components.outputcommandlookup.ui;

import java.util.*;
import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.component.Component;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.middleware.Factory;
import org.jaffa.datatypes.*;
import org.jaffa.metadata.*;
import org.jaffa.components.finder.*;
import org.jaffa.components.lookup.*;
import org.jaffa.components.maint.*;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.components.codehelper.ICodeHelper;
import org.jaffa.components.codehelper.dto.*;
import org.jaffa.modules.printing.components.outputcommandlookup.IOutputCommandLookup;
import org.jaffa.modules.printing.components.outputcommandlookup.dto.OutputCommandLookupInDto;
import org.jaffa.modules.printing.components.outputcommandlookup.dto.OutputCommandLookupOutDto;
import org.jaffa.modules.printing.domain.OutputCommandMeta;
import org.jaffa.modules.printing.components.outputcommandmaintenance.ui.OutputCommandMaintenanceComponent;
import org.jaffa.modules.printing.components.outputcommandviewer.ui.OutputCommandViewerComponent;
import org.jaffa.modules.printing.components.outputcommandmaintenance.ui.OutputCommandMaintenanceComponent;
import org.jaffa.modules.printing.components.outputcommandmaintenance.ui.OutputCommandMaintenanceComponent;

/** The controller for the OutputCommandLookup.
 */
public class OutputCommandLookupComponent extends LookupComponent2 {

    private static Logger log = Logger.getLogger(OutputCommandLookupComponent.class);

    private String m_outputCommandId = null;

    private String m_outputCommandIdDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_outputType = null;

    private String m_outputTypeDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_sequenceNo = null;

    private String m_sequenceNoDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_osPattern = null;

    private String m_osPatternDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_commandLine = null;

    private String m_commandLineDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_createdOn = null;

    private String m_createdOnDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_createdBy = null;

    private String m_createdByDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_lastChangedOn = null;

    private String m_lastChangedOnDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_lastChangedBy = null;

    private String m_lastChangedByDd = CriteriaField.RELATIONAL_EQUALS;

    private IOutputCommandLookup m_tx = null;

    private OutputCommandMaintenanceComponent m_createComponent = null;

    private ICreateListener m_createListener = null;

    private OutputCommandMaintenanceComponent m_updateComponent = null;

    private IUpdateListener m_updateListener = null;

    private OutputCommandMaintenanceComponent m_deleteComponent = null;

    private IDeleteListener m_deleteListener = null;

    public OutputCommandLookupComponent() {
        super();
        super.setSortDropDown("OutputCommandId");
    }

    /** Returns the Struts GlobalForward for the Criteria screen.
     * @return the Struts GlobalForward for the Criteria screen.
     */
    protected String getCriteriaFormName() {
        return "jaffa_printing_outputCommandLookupCriteria";
    }

    /** Returns the Struts GlobalForward for the Results screen.
     * @return the Struts GlobalForward for the Results screen.
     */
    protected String getResultsFormName() {
        return "jaffa_printing_outputCommandLookupResults";
    }

    /** Returns the Struts GlobalForward for the ConsolidatedCriteriaAndResults screen.
     * @return the Struts GlobalForward for the ConsolidatedCriteriaAndResults screen.
     */
    protected String getConsolidatedCriteriaAndResultsFormName() {
        return "jaffa_printing_outputCommandLookupConsolidatedCriteriaAndResults";
    }

    /** This should be invoked when done with the component.
     */
    public void quit() {
        if (m_tx != null) {
            m_tx.destroy();
            m_tx = null;
        }
        if (m_createComponent != null) {
            m_createComponent.quit();
            m_createComponent = null;
        }
        m_createListener = null;
        if (m_updateComponent != null) {
            m_updateComponent.quit();
            m_updateComponent = null;
        }
        m_updateListener = null;
        if (m_deleteComponent != null) {
            m_deleteComponent.quit();
            m_deleteComponent = null;
        }
        m_deleteListener = null;
        super.quit();
    }

    /** Getter for property outputCommandId.
     * @return Value of property outputCommandId.
     */
    public String getOutputCommandId() {
        return m_outputCommandId;
    }

    /** Setter for property outputCommandId.
     * @param outputCommandId New value of property outputCommandId.
     */
    public void setOutputCommandId(String outputCommandId) {
        m_outputCommandId = outputCommandId;
    }

    /** Getter for property outputCommandIdDd.
     * @return Value of property outputCommandIdDd.
     */
    public String getOutputCommandIdDd() {
        return m_outputCommandIdDd;
    }

    /** Setter for property outputCommandIdDd.
     * @param outputCommandIdDd New value of property outputCommandIdDd.
     */
    public void setOutputCommandIdDd(String outputCommandIdDd) {
        m_outputCommandIdDd = outputCommandIdDd;
    }

    /** Getter for property outputType.
     * @return Value of property outputType.
     */
    public String getOutputType() {
        return m_outputType;
    }

    /** Setter for property outputType.
     * @param outputType New value of property outputType.
     */
    public void setOutputType(String outputType) {
        m_outputType = outputType;
    }

    /** Getter for property outputTypeDd.
     * @return Value of property outputTypeDd.
     */
    public String getOutputTypeDd() {
        return m_outputTypeDd;
    }

    /** Setter for property outputTypeDd.
     * @param outputTypeDd New value of property outputTypeDd.
     */
    public void setOutputTypeDd(String outputTypeDd) {
        m_outputTypeDd = outputTypeDd;
    }

    /** Getter for property sequenceNo.
     * @return Value of property sequenceNo.
     */
    public String getSequenceNo() {
        return m_sequenceNo;
    }

    /** Setter for property sequenceNo.
     * @param sequenceNo New value of property sequenceNo.
     */
    public void setSequenceNo(String sequenceNo) {
        m_sequenceNo = sequenceNo;
    }

    /** Getter for property sequenceNoDd.
     * @return Value of property sequenceNoDd.
     */
    public String getSequenceNoDd() {
        return m_sequenceNoDd;
    }

    /** Setter for property sequenceNoDd.
     * @param sequenceNoDd New value of property sequenceNoDd.
     */
    public void setSequenceNoDd(String sequenceNoDd) {
        m_sequenceNoDd = sequenceNoDd;
    }

    /** Getter for property osPattern.
     * @return Value of property osPattern.
     */
    public String getOsPattern() {
        return m_osPattern;
    }

    /** Setter for property osPattern.
     * @param osPattern New value of property osPattern.
     */
    public void setOsPattern(String osPattern) {
        m_osPattern = osPattern;
    }

    /** Getter for property osPatternDd.
     * @return Value of property osPatternDd.
     */
    public String getOsPatternDd() {
        return m_osPatternDd;
    }

    /** Setter for property osPatternDd.
     * @param osPatternDd New value of property osPatternDd.
     */
    public void setOsPatternDd(String osPatternDd) {
        m_osPatternDd = osPatternDd;
    }

    /** Getter for property commandLine.
     * @return Value of property commandLine.
     */
    public String getCommandLine() {
        return m_commandLine;
    }

    /** Setter for property commandLine.
     * @param commandLine New value of property commandLine.
     */
    public void setCommandLine(String commandLine) {
        m_commandLine = commandLine;
    }

    /** Getter for property commandLineDd.
     * @return Value of property commandLineDd.
     */
    public String getCommandLineDd() {
        return m_commandLineDd;
    }

    /** Setter for property commandLineDd.
     * @param commandLineDd New value of property commandLineDd.
     */
    public void setCommandLineDd(String commandLineDd) {
        m_commandLineDd = commandLineDd;
    }

    /** Getter for property createdOn.
     * @return Value of property createdOn.
     */
    public String getCreatedOn() {
        return m_createdOn;
    }

    /** Setter for property createdOn.
     * @param createdOn New value of property createdOn.
     */
    public void setCreatedOn(String createdOn) {
        m_createdOn = createdOn;
    }

    /** Getter for property createdOnDd.
     * @return Value of property createdOnDd.
     */
    public String getCreatedOnDd() {
        return m_createdOnDd;
    }

    /** Setter for property createdOnDd.
     * @param createdOnDd New value of property createdOnDd.
     */
    public void setCreatedOnDd(String createdOnDd) {
        m_createdOnDd = createdOnDd;
    }

    /** Getter for property createdBy.
     * @return Value of property createdBy.
     */
    public String getCreatedBy() {
        return m_createdBy;
    }

    /** Setter for property createdBy.
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(String createdBy) {
        m_createdBy = createdBy;
    }

    /** Getter for property createdByDd.
     * @return Value of property createdByDd.
     */
    public String getCreatedByDd() {
        return m_createdByDd;
    }

    /** Setter for property createdByDd.
     * @param createdByDd New value of property createdByDd.
     */
    public void setCreatedByDd(String createdByDd) {
        m_createdByDd = createdByDd;
    }

    /** Getter for property lastChangedOn.
     * @return Value of property lastChangedOn.
     */
    public String getLastChangedOn() {
        return m_lastChangedOn;
    }

    /** Setter for property lastChangedOn.
     * @param lastChangedOn New value of property lastChangedOn.
     */
    public void setLastChangedOn(String lastChangedOn) {
        m_lastChangedOn = lastChangedOn;
    }

    /** Getter for property lastChangedOnDd.
     * @return Value of property lastChangedOnDd.
     */
    public String getLastChangedOnDd() {
        return m_lastChangedOnDd;
    }

    /** Setter for property lastChangedOnDd.
     * @param lastChangedOnDd New value of property lastChangedOnDd.
     */
    public void setLastChangedOnDd(String lastChangedOnDd) {
        m_lastChangedOnDd = lastChangedOnDd;
    }

    /** Getter for property lastChangedBy.
     * @return Value of property lastChangedBy.
     */
    public String getLastChangedBy() {
        return m_lastChangedBy;
    }

    /** Setter for property lastChangedBy.
     * @param lastChangedBy New value of property lastChangedBy.
     */
    public void setLastChangedBy(String lastChangedBy) {
        m_lastChangedBy = lastChangedBy;
    }

    /** Getter for property lastChangedByDd.
     * @return Value of property lastChangedByDd.
     */
    public String getLastChangedByDd() {
        return m_lastChangedByDd;
    }

    /** Setter for property lastChangedByDd.
     * @param lastChangedByDd New value of property lastChangedByDd.
     */
    public void setLastChangedByDd(String lastChangedByDd) {
        m_lastChangedByDd = lastChangedByDd;
    }

    /** This performs the actual query to obtain the FinderOutDto.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return the FinderOutDto object.
     */
    protected FinderOutDto doInquiry() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = null;
        OutputCommandLookupInDto inputDto = new OutputCommandLookupInDto();
        inputDto.setMaxRecords(getMaxRecords());
        try {
            if (getOutputCommandId() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getOutputCommandIdDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getOutputCommandIdDd())) inputDto.setOutputCommandId(IntegerCriteriaField.getIntegerCriteriaField(getOutputCommandIdDd(), getOutputCommandId(), (IntegerFieldMetaData) OutputCommandMeta.META_OUTPUT_COMMAND_ID));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        if (getOutputType() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getOutputTypeDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getOutputTypeDd())) inputDto.setOutputType(StringCriteriaField.getStringCriteriaField(getOutputTypeDd(), getOutputType(), null));
        try {
            if (getSequenceNo() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getSequenceNoDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getSequenceNoDd())) inputDto.setSequenceNo(IntegerCriteriaField.getIntegerCriteriaField(getSequenceNoDd(), getSequenceNo(), (IntegerFieldMetaData) OutputCommandMeta.META_SEQUENCE_NO));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        if (getOsPattern() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getOsPatternDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getOsPatternDd())) inputDto.setOsPattern(StringCriteriaField.getStringCriteriaField(getOsPatternDd(), getOsPattern(), null));
        if (getCommandLine() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getCommandLineDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getCommandLineDd())) inputDto.setCommandLine(StringCriteriaField.getStringCriteriaField(getCommandLineDd(), getCommandLine(), null));
        try {
            if (getCreatedOn() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getCreatedOnDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getCreatedOnDd())) inputDto.setCreatedOn(DateTimeCriteriaField.getDateTimeCriteriaField(getCreatedOnDd(), getCreatedOn(), (DateTimeFieldMetaData) OutputCommandMeta.META_CREATED_ON));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        if (getCreatedBy() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getCreatedByDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getCreatedByDd())) inputDto.setCreatedBy(StringCriteriaField.getStringCriteriaField(getCreatedByDd(), getCreatedBy(), null));
        try {
            if (getLastChangedOn() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getLastChangedOnDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getLastChangedOnDd())) inputDto.setLastChangedOn(DateTimeCriteriaField.getDateTimeCriteriaField(getLastChangedOnDd(), getLastChangedOn(), (DateTimeFieldMetaData) OutputCommandMeta.META_LAST_CHANGED_ON));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        if (getLastChangedBy() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getLastChangedByDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getLastChangedByDd())) inputDto.setLastChangedBy(StringCriteriaField.getStringCriteriaField(getLastChangedByDd(), getLastChangedBy(), null));
        if (appExps != null && appExps.size() > 0) throw appExps;
        inputDto.setHeaderDto(getHeaderDto());
        addSortCriteria(inputDto);
        if (m_tx == null) m_tx = (IOutputCommandLookup) Factory.createObject(IOutputCommandLookup.class);
        FinderOutDto finderOutDto = m_tx.find(inputDto);
        return finderOutDto;
    }

    /** Calls the Jaffa.Printing.OutputCommandMaintenance component for creating a new OutputCommand object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createFromCriteria() throws ApplicationExceptions, FrameworkException {
        return createObject(getCriteriaFormKey());
    }

    /** Calls the Jaffa.Printing.OutputCommandMaintenance component for creating a new OutputCommand object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createFromResults() throws ApplicationExceptions, FrameworkException {
        return createObject(getResultsFormKey());
    }

    /** Calls the Jaffa.Printing.OutputCommandMaintenance component for creating a new OutputCommand object.
     * @param formKey The FormKey object for the screen invoking this method
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createObject(FormKey formKey) throws ApplicationExceptions, FrameworkException {
        if (m_createComponent == null || !m_createComponent.isActive()) m_createComponent = (OutputCommandMaintenanceComponent) run("Jaffa.Printing.OutputCommandMaintenance");
        m_createComponent.setReturnToFormKey(formKey);
        if (getFinderOutDto() != null) addListeners(m_createComponent);
        if (m_createComponent instanceof IMaintComponent) ((IMaintComponent) m_createComponent).setMode(IMaintComponent.MODE_CREATE);
        return m_createComponent.display();
    }

    private ICreateListener getCreateListener() {
        if (m_createListener == null) {
            m_createListener = new ICreateListener() {

                public void createDone(EventObject source) {
                    try {
                        performInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the Results screen after the Create", e);
                    }
                }
            };
        }
        return m_createListener;
    }

    /** Calls the Jaffa.Printing.OutputCommandViewer component for viewing the OutputCommand object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the View screen.
     */
    public FormKey viewObject(java.lang.Long outputCommandId) throws ApplicationExceptions, FrameworkException {
        OutputCommandViewerComponent viewComponent = (OutputCommandViewerComponent) run("Jaffa.Printing.OutputCommandViewer");
        viewComponent.setReturnToFormKey(FormKey.getCloseBrowserFormKey());
        viewComponent.setOutputCommandId(outputCommandId);
        return viewComponent.display();
    }

    /** Calls the Jaffa.Printing.OutputCommandMaintenance component for updating the OutputCommand object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Update screen.
     */
    public FormKey updateObject(java.lang.Long outputCommandId) throws ApplicationExceptions, FrameworkException {
        if (m_updateComponent == null || !m_updateComponent.isActive()) {
            m_updateComponent = (OutputCommandMaintenanceComponent) run("Jaffa.Printing.OutputCommandMaintenance");
            m_updateComponent.setReturnToFormKey(getResultsFormKey());
            addListeners(m_updateComponent);
        }
        m_updateComponent.setOutputCommandId(outputCommandId);
        if (m_updateComponent instanceof IMaintComponent) ((IMaintComponent) m_updateComponent).setMode(IMaintComponent.MODE_UPDATE);
        return m_updateComponent.display();
    }

    private IUpdateListener getUpdateListener() {
        if (m_updateListener == null) {
            m_updateListener = new IUpdateListener() {

                public void updateDone(EventObject source) {
                    try {
                        performInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the Results screen after the Update", e);
                    }
                }
            };
        }
        return m_updateListener;
    }

    /** Calls the Jaffa.Printing.OutputCommandMaintenance component for deleting the OutputCommand object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Delete screen.
     */
    public FormKey deleteObject(java.lang.Long outputCommandId) throws ApplicationExceptions, FrameworkException {
        if (m_deleteComponent == null || !m_deleteComponent.isActive()) {
            m_deleteComponent = (OutputCommandMaintenanceComponent) run("Jaffa.Printing.OutputCommandMaintenance");
            m_deleteComponent.setReturnToFormKey(getResultsFormKey());
            addListeners(m_deleteComponent);
        }
        m_deleteComponent.setOutputCommandId(outputCommandId);
        if (m_deleteComponent instanceof IMaintComponent) ((IMaintComponent) m_deleteComponent).setMode(IMaintComponent.MODE_DELETE);
        return m_deleteComponent.display();
    }

    private IDeleteListener getDeleteListener() {
        if (m_deleteListener == null) {
            m_deleteListener = new IDeleteListener() {

                public void deleteDone(EventObject source) {
                    try {
                        performInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the Results screen after the Delete", e);
                    }
                }
            };
        }
        return m_deleteListener;
    }

    private void addListeners(Component comp) {
        if (comp instanceof ICreateComponent) ((ICreateComponent) comp).addCreateListener(getCreateListener());
        if (comp instanceof IUpdateComponent) ((IUpdateComponent) comp).addUpdateListener(getUpdateListener());
        if (comp instanceof IDeleteComponent) ((IDeleteComponent) comp).addDeleteListener(getDeleteListener());
    }

    /** This will retrieve the set of codes for dropdowns, if any are required
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     */
    protected void initializeCriteriaScreen() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = null;
        CodeHelperInDto input = null;
    }
}
