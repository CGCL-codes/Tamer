package au.edu.monash.merc.capture.struts2.action;

import java.io.InputStream;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import au.edu.monash.merc.capture.config.ConfigSettings;
import au.edu.monash.merc.capture.domain.Dataset;

@Scope("prototype")
@Controller("data.exportDSAction")
public class ExportDSAction extends DMCoreAction {

    private Dataset dataset;

    private String contentType;

    private InputStream dsInputStream;

    private String contentDisposition;

    private int bufferSize;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String exportDataset() {
        try {
            String dataStorePath = configSetting.getPropValue(ConfigSettings.DATA_STORE_LOCATION);
            this.dsInputStream = this.dmService.downloadFile(dataset, dataStorePath);
            this.contentDisposition = "attachment;filename=\"" + dataset.getName() + "\"";
            this.bufferSize = 20480;
            this.contentType = "application/octet-stream";
        } catch (Exception e) {
            addFieldError("export", getText("dataset.export.failed"));
            logger.error(e);
            try {
                retrieveCollection();
                retrieveAllDatasets();
                setNavAfterExcInDS();
            } catch (Exception ex) {
                addFieldError("getCollectionError", getText("dataset.export.get.collection.details.failed"));
                collectionError = true;
                setNavAfterColExc();
                return INPUT;
            }
            return INPUT;
        }
        return SUCCESS;
    }

    /**
	 * validate
	 */
    public void validateExportDataset() {
        boolean hasError = false;
        try {
            collection = this.dmService.getCollection(collection.getId(), collection.getOwner().getId());
        } catch (Exception e) {
            addFieldError("collectionerror", getText("dataset.export.get.collection.details.failed"));
            collectionError = true;
            setNavAfterColExc();
            return;
        }
        if (collection == null) {
            addFieldError("collectionerror", getText("dataset.export.collection.not.exist"));
            collectionError = true;
            setNavAfterColExc();
            return;
        }
        try {
            checkUserPermissions(collection.getId(), collection.getOwner().getId());
        } catch (Exception e) {
            addFieldError("checkPermission", getText("check.permissions.error"));
            collectionError = true;
            setNavAfterColExc();
            return;
        }
        if (!permissionBean.isExportAllowed()) {
            addFieldError("exportPermission", getText("dataset.export.permission.denied"));
            hasError = true;
        }
        try {
            dataset = this.dmService.getDatasetById(dataset.getId());
            if (dataset == null) {
                addFieldError("dataset", getText("dataset.export.failed.nonexisted.dataset.file"));
                hasError = true;
            }
        } catch (Exception e) {
            addFieldError("dataset", getText("dataset.export.failed.check.dataset.error"));
            hasError = true;
        }
        if (hasError) {
            try {
                retrieveCollection();
                retrieveAllDatasets();
                setNavAfterExcInDS();
            } catch (Exception e) {
                addFieldError("getCollectionError", getText("dataset.export.get.collection.details.failed"));
                collectionError = true;
                setNavAfterColExc();
            }
        }
    }

    private void setNavAfterExcInDS() {
        String startNav = null;
        String startNavLink = null;
        String secondNav = collection.getName();
        String thirdNav = getText("export.dataset.error");
        if (viewType != null) {
            if (viewType.equals(ActConstants.UserViewType.USER.toString())) {
                startNav = getText("mycollection.nav.label.name");
                startNavLink = ActConstants.USER_LIST_COLLECTION_ACTION;
            }
            if (viewType.equals(ActConstants.UserViewType.ALL.toString())) {
                startNav = getText("allcollection.nav.label.name");
                startNavLink = ActConstants.LIST_ALL_COLLECTIONS_ACTION;
            }
            setPageTitle(startNav, (secondNav + " - " + thirdNav));
            String secondNavLink = ActConstants.VIEW_COLLECTION_DETAILS_ACTION + "?collection.id=" + collection.getId() + "&collection.owner.id=" + collection.getOwner().getId() + "&viewType=" + viewType;
            navigationBar = generateNavLabel(startNav, startNavLink, secondNav, secondNavLink, thirdNav, null);
        }
    }

    private void setNavAfterColExc() {
        String startNav = null;
        String startNavLink = null;
        String secondNav = getText("export.dataset.error");
        if (viewType != null) {
            if (viewType.equals(ActConstants.UserViewType.USER.toString())) {
                startNav = getText("mycollection.nav.label.name");
                startNavLink = ActConstants.USER_LIST_COLLECTION_ACTION;
            }
            if (viewType.equals(ActConstants.UserViewType.ALL.toString())) {
                startNav = getText("allcollection.nav.label.name");
                startNavLink = ActConstants.LIST_ALL_COLLECTIONS_ACTION;
            }
            setPageTitle(startNav, secondNav);
            navigationBar = generateNavLabel(startNav, startNavLink, secondNav, null, null, null);
        }
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getDsInputStream() {
        return dsInputStream;
    }

    public void setDsInputStream(InputStream dsInputStream) {
        this.dsInputStream = dsInputStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
