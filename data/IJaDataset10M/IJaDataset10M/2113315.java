package org.jaffa.applications.mylife.admin.components.picturecontentlookup.ui;

import org.jaffa.components.lookup.LookupComponent;
import java.util.*;
import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.component.Component;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.middleware.Factory;
import org.jaffa.datatypes.*;
import org.jaffa.metadata.*;
import org.jaffa.components.finder.*;
import org.jaffa.components.maint.*;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.components.codehelper.ICodeHelper;
import org.jaffa.components.codehelper.dto.*;
import org.jaffa.applications.mylife.admin.components.picturecontentlookup.IPictureContentLookup;
import org.jaffa.applications.mylife.admin.components.picturecontentlookup.dto.PictureContentLookupInDto;
import org.jaffa.applications.mylife.admin.components.picturecontentlookup.dto.PictureContentLookupOutDto;
import org.jaffa.applications.mylife.admin.domain.PictureContentMeta;
import org.jaffa.applications.mylife.admin.components.picturecontentmaintenance.ui.PictureContentMaintenanceComponent;
import org.jaffa.applications.mylife.admin.components.picturecontentviewer.ui.PictureContentViewerComponent;
import org.jaffa.applications.mylife.admin.components.picturecontentmaintenance.ui.PictureContentMaintenanceComponent;
import org.jaffa.applications.mylife.admin.components.picturecontentmaintenance.ui.PictureContentMaintenanceComponent;

/** The controller for the PictureContentLookup.
 */
public class PictureContentLookupComponent extends LookupComponent {

    private static Logger log = Logger.getLogger(PictureContentLookupComponent.class);

    private String m_contentId = null;

    private String m_contentIdDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_fileName = null;

    private String m_fileNameDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_fileType = null;

    private String m_fileTypeDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_filePath = null;

    private String m_filePathDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_thumbPath = null;

    private String m_thumbPathDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_importPath = null;

    private String m_importPathDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_byteSize = null;

    private String m_byteSizeDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_pixelWidth = null;

    private String m_pixelWidthDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_pixelHeight = null;

    private String m_pixelHeightDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_duration = null;

    private String m_durationDd = CriteriaField.RELATIONAL_EQUALS;

    private PictureContentLookupOutDto m_outputDto = null;

    private IPictureContentLookup m_tx = null;

    private PictureContentMaintenanceComponent m_createComponent = null;

    private ICreateListener m_createListener = null;

    private PictureContentMaintenanceComponent m_updateComponent = null;

    private IUpdateListener m_updateListener = null;

    private PictureContentMaintenanceComponent m_deleteComponent = null;

    private IDeleteListener m_deleteListener = null;

    public PictureContentLookupComponent() {
        super();
        super.setSortDropDown("ContentId");
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
        m_outputDto = null;
        super.quit();
    }

    /** Getter for property contentId.
     * @return Value of property contentId.
     */
    public String getContentId() {
        return m_contentId;
    }

    /** Setter for property contentId.
     * @param contentId New value of property contentId.
     */
    public void setContentId(String contentId) {
        m_contentId = contentId;
    }

    /** Getter for property contentIdDd.
     * @return Value of property contentIdDd.
     */
    public String getContentIdDd() {
        return m_contentIdDd;
    }

    /** Setter for property contentIdDd.
     * @param contentIdDd New value of property contentIdDd.
     */
    public void setContentIdDd(String contentIdDd) {
        m_contentIdDd = contentIdDd;
    }

    /** Getter for property fileName.
     * @return Value of property fileName.
     */
    public String getFileName() {
        return m_fileName;
    }

    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(String fileName) {
        m_fileName = fileName;
    }

    /** Getter for property fileNameDd.
     * @return Value of property fileNameDd.
     */
    public String getFileNameDd() {
        return m_fileNameDd;
    }

    /** Setter for property fileNameDd.
     * @param fileNameDd New value of property fileNameDd.
     */
    public void setFileNameDd(String fileNameDd) {
        m_fileNameDd = fileNameDd;
    }

    /** Getter for property fileType.
     * @return Value of property fileType.
     */
    public String getFileType() {
        return m_fileType;
    }

    /** Setter for property fileType.
     * @param fileType New value of property fileType.
     */
    public void setFileType(String fileType) {
        m_fileType = fileType;
    }

    /** Getter for property fileTypeDd.
     * @return Value of property fileTypeDd.
     */
    public String getFileTypeDd() {
        return m_fileTypeDd;
    }

    /** Setter for property fileTypeDd.
     * @param fileTypeDd New value of property fileTypeDd.
     */
    public void setFileTypeDd(String fileTypeDd) {
        m_fileTypeDd = fileTypeDd;
    }

    /** Getter for property filePath.
     * @return Value of property filePath.
     */
    public String getFilePath() {
        return m_filePath;
    }

    /** Setter for property filePath.
     * @param filePath New value of property filePath.
     */
    public void setFilePath(String filePath) {
        m_filePath = filePath;
    }

    /** Getter for property filePathDd.
     * @return Value of property filePathDd.
     */
    public String getFilePathDd() {
        return m_filePathDd;
    }

    /** Setter for property filePathDd.
     * @param filePathDd New value of property filePathDd.
     */
    public void setFilePathDd(String filePathDd) {
        m_filePathDd = filePathDd;
    }

    /** Getter for property thumbPath.
     * @return Value of property thumbPath.
     */
    public String getThumbPath() {
        return m_thumbPath;
    }

    /** Setter for property thumbPath.
     * @param thumbPath New value of property thumbPath.
     */
    public void setThumbPath(String thumbPath) {
        m_thumbPath = thumbPath;
    }

    /** Getter for property thumbPathDd.
     * @return Value of property thumbPathDd.
     */
    public String getThumbPathDd() {
        return m_thumbPathDd;
    }

    /** Setter for property thumbPathDd.
     * @param thumbPathDd New value of property thumbPathDd.
     */
    public void setThumbPathDd(String thumbPathDd) {
        m_thumbPathDd = thumbPathDd;
    }

    /** Getter for property importPath.
     * @return Value of property importPath.
     */
    public String getImportPath() {
        return m_importPath;
    }

    /** Setter for property importPath.
     * @param importPath New value of property importPath.
     */
    public void setImportPath(String importPath) {
        m_importPath = importPath;
    }

    /** Getter for property importPathDd.
     * @return Value of property importPathDd.
     */
    public String getImportPathDd() {
        return m_importPathDd;
    }

    /** Setter for property importPathDd.
     * @param importPathDd New value of property importPathDd.
     */
    public void setImportPathDd(String importPathDd) {
        m_importPathDd = importPathDd;
    }

    /** Getter for property byteSize.
     * @return Value of property byteSize.
     */
    public String getByteSize() {
        return m_byteSize;
    }

    /** Setter for property byteSize.
     * @param byteSize New value of property byteSize.
     */
    public void setByteSize(String byteSize) {
        m_byteSize = byteSize;
    }

    /** Getter for property byteSizeDd.
     * @return Value of property byteSizeDd.
     */
    public String getByteSizeDd() {
        return m_byteSizeDd;
    }

    /** Setter for property byteSizeDd.
     * @param byteSizeDd New value of property byteSizeDd.
     */
    public void setByteSizeDd(String byteSizeDd) {
        m_byteSizeDd = byteSizeDd;
    }

    /** Getter for property pixelWidth.
     * @return Value of property pixelWidth.
     */
    public String getPixelWidth() {
        return m_pixelWidth;
    }

    /** Setter for property pixelWidth.
     * @param pixelWidth New value of property pixelWidth.
     */
    public void setPixelWidth(String pixelWidth) {
        m_pixelWidth = pixelWidth;
    }

    /** Getter for property pixelWidthDd.
     * @return Value of property pixelWidthDd.
     */
    public String getPixelWidthDd() {
        return m_pixelWidthDd;
    }

    /** Setter for property pixelWidthDd.
     * @param pixelWidthDd New value of property pixelWidthDd.
     */
    public void setPixelWidthDd(String pixelWidthDd) {
        m_pixelWidthDd = pixelWidthDd;
    }

    /** Getter for property pixelHeight.
     * @return Value of property pixelHeight.
     */
    public String getPixelHeight() {
        return m_pixelHeight;
    }

    /** Setter for property pixelHeight.
     * @param pixelHeight New value of property pixelHeight.
     */
    public void setPixelHeight(String pixelHeight) {
        m_pixelHeight = pixelHeight;
    }

    /** Getter for property pixelHeightDd.
     * @return Value of property pixelHeightDd.
     */
    public String getPixelHeightDd() {
        return m_pixelHeightDd;
    }

    /** Setter for property pixelHeightDd.
     * @param pixelHeightDd New value of property pixelHeightDd.
     */
    public void setPixelHeightDd(String pixelHeightDd) {
        m_pixelHeightDd = pixelHeightDd;
    }

    /** Getter for property duration.
     * @return Value of property duration.
     */
    public String getDuration() {
        return m_duration;
    }

    /** Setter for property duration.
     * @param duration New value of property duration.
     */
    public void setDuration(String duration) {
        m_duration = duration;
    }

    /** Getter for property durationDd.
     * @return Value of property durationDd.
     */
    public String getDurationDd() {
        return m_durationDd;
    }

    /** Setter for property durationDd.
     * @param durationDd New value of property durationDd.
     */
    public void setDurationDd(String durationDd) {
        m_durationDd = durationDd;
    }

    /** Getter for property outputDto.
     * @return Value of property outputDto.
     */
    public PictureContentLookupOutDto getPictureContentLookupOutDto() {
        return m_outputDto;
    }

    /** If the displayResultsScreen property has not been set or has been set to false, it will return the FormKey for the Criteria screen.
     * If the displayResultsScreen property has been set to true, then a Search will be performed & the FormKey for the Results screen will be returned.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Criteria screen.
     */
    public FormKey display() throws ApplicationExceptions, FrameworkException {
        if (getDisplayResultsScreen() != null && getDisplayResultsScreen().booleanValue()) return displayResults(); else return displayCriteria();
    }

    /** Returns the FormKey for the Criteria screen.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Criteria screen.
     */
    public FormKey displayCriteria() throws ApplicationExceptions, FrameworkException {
        initCriteriaScreen();
        return new FormKey(PictureContentLookupCriteriaForm.NAME, getComponentId());
    }

    /** Performs a search and returns the FormKey for the Results screen.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Results screen.
     */
    public FormKey displayResults() throws ApplicationExceptions, FrameworkException {
        doInquiry();
        return new FormKey(PictureContentLookupResultsForm.NAME, getComponentId());
    }

    private void doInquiry() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = null;
        PictureContentLookupInDto inputDto = new PictureContentLookupInDto();
        inputDto.setMaxRecords(getMaxRecords());
        if (getContentId() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getContentIdDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getContentIdDd())) inputDto.setContentId(StringCriteriaField.getStringCriteriaField(getContentIdDd(), getContentId(), null));
        if (getFileName() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getFileNameDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getFileNameDd())) inputDto.setFileName(StringCriteriaField.getStringCriteriaField(getFileNameDd(), getFileName(), null));
        if (getFileType() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getFileTypeDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getFileTypeDd())) inputDto.setFileType(StringCriteriaField.getStringCriteriaField(getFileTypeDd(), getFileType(), null));
        if (getFilePath() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getFilePathDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getFilePathDd())) inputDto.setFilePath(StringCriteriaField.getStringCriteriaField(getFilePathDd(), getFilePath(), null));
        if (getThumbPath() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getThumbPathDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getThumbPathDd())) inputDto.setThumbPath(StringCriteriaField.getStringCriteriaField(getThumbPathDd(), getThumbPath(), null));
        if (getImportPath() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getImportPathDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getImportPathDd())) inputDto.setImportPath(StringCriteriaField.getStringCriteriaField(getImportPathDd(), getImportPath(), null));
        try {
            if (getByteSize() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getByteSizeDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getByteSizeDd())) inputDto.setByteSize(IntegerCriteriaField.getIntegerCriteriaField(getByteSizeDd(), getByteSize(), null));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(PictureContentMeta.META_BYTE_SIZE.getLabelToken());
            appExps.add(e);
        }
        try {
            if (getPixelWidth() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getPixelWidthDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getPixelWidthDd())) inputDto.setPixelWidth(IntegerCriteriaField.getIntegerCriteriaField(getPixelWidthDd(), getPixelWidth(), null));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(PictureContentMeta.META_PIXEL_WIDTH.getLabelToken());
            appExps.add(e);
        }
        try {
            if (getPixelHeight() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getPixelHeightDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getPixelHeightDd())) inputDto.setPixelHeight(IntegerCriteriaField.getIntegerCriteriaField(getPixelHeightDd(), getPixelHeight(), null));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(PictureContentMeta.META_PIXEL_HEIGHT.getLabelToken());
            appExps.add(e);
        }
        try {
            if (getDuration() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getDurationDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getDurationDd())) inputDto.setDuration(DecimalCriteriaField.getDecimalCriteriaField(getDurationDd(), getDuration(), null));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(PictureContentMeta.META_DURATION.getLabelToken());
            appExps.add(e);
        }
        if (appExps != null && appExps.size() > 0) throw appExps;
        inputDto.setHeaderDto(getHeaderDto());
        addSortCriteria(inputDto);
        if (m_tx == null) m_tx = (IPictureContentLookup) Factory.createObject(IPictureContentLookup.class);
        m_outputDto = m_tx.find(inputDto);
        invokeFinderListener();
    }

    /** Calls the Admin.PictureContentMaintenance component for creating a new PictureContent object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createFromCriteria() throws ApplicationExceptions, FrameworkException {
        return createObject(new FormKey(PictureContentLookupCriteriaForm.NAME, getComponentId()));
    }

    /** Calls the Admin.PictureContentMaintenance component for creating a new PictureContent object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createFromResults() throws ApplicationExceptions, FrameworkException {
        return createObject(new FormKey(PictureContentLookupResultsForm.NAME, getComponentId()));
    }

    /** Calls the Admin.PictureContentMaintenance component for creating a new PictureContent object.
     * @param formKey The FormKey object for the screen invoking this method
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createObject(FormKey formKey) throws ApplicationExceptions, FrameworkException {
        if (m_createComponent == null || !m_createComponent.isActive()) m_createComponent = (PictureContentMaintenanceComponent) run("Admin.PictureContentMaintenance");
        m_createComponent.setReturnToFormKey(formKey);
        if (m_outputDto != null) addListeners(m_createComponent);
        if (m_createComponent instanceof IMaintComponent) ((IMaintComponent) m_createComponent).setMode(IMaintComponent.MODE_CREATE);
        return m_createComponent.display();
    }

    private ICreateListener getCreateListener() {
        if (m_createListener == null) {
            m_createListener = new ICreateListener() {

                public void createDone(EventObject source) {
                    try {
                        doInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the Results screen after the Create", e);
                    }
                }
            };
        }
        return m_createListener;
    }

    /** Calls the Admin.PictureContentViewer component for viewing the PictureContent object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the View screen.
     */
    public FormKey viewObject(String contentId) throws ApplicationExceptions, FrameworkException {
        PictureContentViewerComponent viewComponent = (PictureContentViewerComponent) run("Admin.PictureContentViewer");
        viewComponent.setReturnToFormKey(FormKey.getCloseBrowserFormKey());
        viewComponent.setContentId(contentId);
        return viewComponent.display();
    }

    /** Calls the Admin.PictureContentMaintenance component for updating the PictureContent object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Update screen.
     */
    public FormKey updateObject(String contentId) throws ApplicationExceptions, FrameworkException {
        if (m_updateComponent == null || !m_updateComponent.isActive()) {
            m_updateComponent = (PictureContentMaintenanceComponent) run("Admin.PictureContentMaintenance");
            m_updateComponent.setReturnToFormKey(new FormKey(PictureContentLookupResultsForm.NAME, getComponentId()));
            addListeners(m_updateComponent);
        }
        m_updateComponent.setContentId(contentId);
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

    /** Calls the Admin.PictureContentMaintenance component for deleting the PictureContent object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Delete screen.
     */
    public FormKey deleteObject(String contentId) throws ApplicationExceptions, FrameworkException {
        if (m_deleteComponent == null || !m_deleteComponent.isActive()) {
            m_deleteComponent = (PictureContentMaintenanceComponent) run("Admin.PictureContentMaintenance");
            m_deleteComponent.setReturnToFormKey(new FormKey(PictureContentLookupResultsForm.NAME, getComponentId()));
            addListeners(m_deleteComponent);
        }
        m_deleteComponent.setContentId(contentId);
        if (m_deleteComponent instanceof IMaintComponent) ((IMaintComponent) m_deleteComponent).setMode(IMaintComponent.MODE_DELETE);
        return m_deleteComponent.display();
    }

    private IDeleteListener getDeleteListener() {
        if (m_deleteListener == null) {
            m_deleteListener = new IDeleteListener() {

                public void deleteDone(EventObject source) {
                    try {
                        doInquiry();
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
     */
    private void initCriteriaScreen() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = null;
        CodeHelperInDto input = null;
    }
}
