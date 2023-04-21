package org.jaffa.modules.printing.components.printeroutputtypeviewer.ui;

import org.apache.log4j.Logger;
import java.util.EventObject;
import org.jaffa.presentation.portlet.component.Component;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.exceptions.DomainObjectNotFoundException;
import org.jaffa.datatypes.exceptions.MandatoryFieldException;
import org.jaffa.middleware.Factory;
import org.jaffa.components.finder.OrderByField;
import org.jaffa.components.maint.*;
import org.jaffa.components.dto.HeaderDto;
import org.jaffa.modules.printing.components.printeroutputtypeviewer.IPrinterOutputTypeViewer;
import org.jaffa.modules.printing.components.printeroutputtypeviewer.dto.PrinterOutputTypeViewerInDto;
import org.jaffa.modules.printing.components.printeroutputtypeviewer.dto.PrinterOutputTypeViewerOutDto;
import org.jaffa.modules.printing.domain.PrinterOutputType;
import org.jaffa.modules.printing.domain.PrinterOutputTypeMeta;
import org.jaffa.modules.printing.components.outputcommandmaintenance.ui.OutputCommandMaintenanceComponent;
import org.jaffa.modules.printing.components.printeroutputtypemaintenance.ui.PrinterOutputTypeMaintenanceComponent;

/** The controller for the PrinterOutputTypeViewer.
 */
public class PrinterOutputTypeViewerComponent extends Component {

    private static Logger log = Logger.getLogger(PrinterOutputTypeViewerComponent.class);

    private HeaderDto m_headerDto = null;

    private java.lang.String m_outputType;

    private PrinterOutputTypeViewerOutDto m_outputDto = null;

    private IPrinterOutputTypeViewer m_tx = null;

    private PrinterOutputTypeMaintenanceComponent m_updateComponent = null;

    private IUpdateListener m_updateListener = null;

    private OutputCommandMaintenanceComponent m_updateOutputCommand = null;

    private IUpdateListener m_updateListenerOutputCommand = null;

    private OutputCommandMaintenanceComponent m_deleteOutputCommand = null;

    private IDeleteListener m_deleteListenerOutputCommand = null;

    /** This should be invoked when done with the component.
     */
    public void quit() {
        if (m_tx != null) {
            m_tx.destroy();
            m_tx = null;
        }
        if (m_updateComponent != null) {
            m_updateComponent.quit();
            m_updateComponent = null;
        }
        m_updateListener = null;
        if (m_updateOutputCommand != null) {
            m_updateOutputCommand.quit();
            m_updateOutputCommand = null;
        }
        m_updateListenerOutputCommand = null;
        if (m_deleteOutputCommand != null) {
            m_deleteOutputCommand.quit();
            m_deleteOutputCommand = null;
        }
        m_deleteListenerOutputCommand = null;
        m_outputDto = null;
        super.quit();
    }

    /** Getter for property outputType.
     * @return Value of property outputType.
     */
    public java.lang.String getOutputType() {
        return m_outputType;
    }

    /** Setter for property outputType.
     * @param outputType New value of property outputType.
     */
    public void setOutputType(java.lang.String outputType) {
        m_outputType = outputType;
    }

    /** Getter for property outputDto.
     * @return Value of property outputDto.
     */
    public PrinterOutputTypeViewerOutDto getPrinterOutputTypeViewerOutDto() {
        return m_outputDto;
    }

    /** Setter for property outputDto.
     * @param outputDto New value of property outputDto.
     */
    public void setPrinterOutputTypeViewerOutDto(PrinterOutputTypeViewerOutDto outputDto) {
        m_outputDto = outputDto;
    }

    /** This retrieves the details for the PrinterOutputType.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set, or if no data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the View screen.
     */
    public FormKey display() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = null;
        if (getOutputType() == null) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(new MandatoryFieldException(PrinterOutputTypeMeta.META_OUTPUT_TYPE.getLabelToken()));
        }
        if (appExps != null && appExps.size() > 0) throw appExps;
        doInquiry();
        return getViewerFormKey();
    }

    private void doInquiry() throws ApplicationExceptions, FrameworkException {
        PrinterOutputTypeViewerInDto inputDto = new PrinterOutputTypeViewerInDto();
        inputDto.setOutputType(m_outputType);
        inputDto.setHeaderDto(createHeaderDto());
        if (m_tx == null) m_tx = (IPrinterOutputTypeViewer) Factory.createObject(IPrinterOutputTypeViewer.class);
        m_outputDto = m_tx.read(inputDto);
        getUserSession().getWidgetCache(getComponentId()).clear();
        if (m_outputDto == null) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DomainObjectNotFoundException(PrinterOutputTypeMeta.getLabelToken()));
            throw appExps;
        }
    }

    /** Calls the Jaffa.Printing.OutputCommandMaintenance component for updating the OutputCommand object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Update screen.
     */
    public FormKey updateOutputCommand(java.lang.Long outputCommandId) throws ApplicationExceptions, FrameworkException {
        if (m_updateOutputCommand == null || !m_updateOutputCommand.isActive()) {
            m_updateOutputCommand = (OutputCommandMaintenanceComponent) run("Jaffa.Printing.OutputCommandMaintenance");
            m_updateOutputCommand.setReturnToFormKey(getViewerFormKey());
            addListenersOutputCommand(m_updateOutputCommand);
        }
        m_updateOutputCommand.setOutputCommandId(outputCommandId);
        if (m_updateOutputCommand instanceof IMaintComponent) ((IMaintComponent) m_updateOutputCommand).setMode(IMaintComponent.MODE_UPDATE);
        return m_updateOutputCommand.display();
    }

    private IUpdateListener getUpdateListenerOutputCommand() {
        if (m_updateListenerOutputCommand == null) {
            m_updateListenerOutputCommand = new IUpdateListener() {

                public void updateDone(EventObject source) {
                    try {
                        doInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the screen after the Update", e);
                    }
                }
            };
        }
        return m_updateListenerOutputCommand;
    }

    /** Calls the Jaffa.Printing.OutputCommandMaintenance component for deleting the OutputCommand object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Delete screen.
     */
    public FormKey deleteOutputCommand(java.lang.Long outputCommandId) throws ApplicationExceptions, FrameworkException {
        if (m_deleteOutputCommand == null || !m_deleteOutputCommand.isActive()) {
            m_deleteOutputCommand = (OutputCommandMaintenanceComponent) run("Jaffa.Printing.OutputCommandMaintenance");
            m_deleteOutputCommand.setReturnToFormKey(getViewerFormKey());
            addListenersOutputCommand(m_deleteOutputCommand);
        }
        m_deleteOutputCommand.setOutputCommandId(outputCommandId);
        if (m_deleteOutputCommand instanceof IMaintComponent) ((IMaintComponent) m_deleteOutputCommand).setMode(IMaintComponent.MODE_DELETE);
        return m_deleteOutputCommand.display();
    }

    private IDeleteListener getDeleteListenerOutputCommand() {
        if (m_deleteListenerOutputCommand == null) {
            m_deleteListenerOutputCommand = new IDeleteListener() {

                public void deleteDone(EventObject source) {
                    try {
                        doInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the screen after the Delete", e);
                    }
                }
            };
        }
        return m_deleteListenerOutputCommand;
    }

    private void addListenersOutputCommand(Component comp) {
        if (comp instanceof IUpdateComponent) ((IUpdateComponent) comp).addUpdateListener(getUpdateListenerOutputCommand());
        if (comp instanceof IDeleteComponent) ((IDeleteComponent) comp).addDeleteListener(getDeleteListenerOutputCommand());
    }

    private HeaderDto createHeaderDto() {
        if (m_headerDto == null) {
            m_headerDto = new HeaderDto();
            m_headerDto.setUserId(getUserSession().getUserId());
            m_headerDto.setVariation(getUserSession().getVariation());
        }
        return m_headerDto;
    }

    public FormKey getViewerFormKey() {
        return new FormKey(PrinterOutputTypeViewerForm.NAME, getComponentId());
    }

    /** Calls the Jaffa.Printing.PrinterOutputTypeMaintenance component for updating the PrinterOutputType object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Update screen.
     */
    public FormKey updateObject() throws ApplicationExceptions, FrameworkException {
        if (m_updateComponent == null || !m_updateComponent.isActive()) {
            m_updateComponent = (PrinterOutputTypeMaintenanceComponent) run("Jaffa.Printing.PrinterOutputTypeMaintenance");
            m_updateComponent.setReturnToFormKey(getViewerFormKey());
            addListeners(m_updateComponent);
        }
        m_updateComponent.setOutputType(getOutputType());
        if (m_updateComponent instanceof IMaintComponent) ((IMaintComponent) m_updateComponent).setMode(IMaintComponent.MODE_UPDATE);
        return m_updateComponent.display();
    }

    private IUpdateListener getUpdateListener() {
        if (m_updateListener == null) {
            m_updateListener = new IUpdateListener() {

                public void updateDone(EventObject source) {
                    try {
                        doInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the Results screen after the Update", e);
                    }
                }
            };
        }
        return m_updateListener;
    }

    private void addListeners(Component comp) {
        if (comp instanceof IUpdateComponent) ((IUpdateComponent) comp).addUpdateListener(getUpdateListener());
    }
}
