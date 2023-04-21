package org.pachyderm.woc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.log4j.Logger;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.assetdb.eof.AssetDBRecord;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSTimestamp;

public class EditMediaCaptionPage extends MCPage implements EditPageInterface {

    private static Logger LOG = Logger.getLogger(EditMediaCaptionPage.class.getName());

    private static final long serialVersionUID = 523345994512895004L;

    private WOComponent _searchResultsPage;

    public AssetDBRecord mediaRecord;

    public NSData captionData;

    public String captionFileName;

    public String captionMimeType;

    public EditMediaCaptionPage(WOContext context) {
        super(context);
        LOG.info("[CONSTRUCT]");
    }

    public void setAsset(AssetDBRecord assetEO) {
        mediaRecord = assetEO;
    }

    public WOComponent uploadAction() {
        LOG.info("[[ UPLOAD ]]");
        if ((captionData != null) && (!(captionData.isEqualToData(new NSData())))) {
            String synchronizedCaption = new String(captionData.bytes());
            mediaRecord.setSyncCaption(synchronizedCaption);
        }
        return this;
    }

    public WOComponent submitAction() {
        LOG.info("[[ SUBMIT ]]- asset-EO (" + mediaRecord.__editingContext() + ")");
        ((EditMediaPage) getNextPage()).setNextPage(getSearchResultsPage());
        return getNextPage();
    }

    public WOComponent cancelAction() {
        LOG.info("[[ CANCEL ]]");
        ((EditMediaPage) getNextPage()).setNextPage(getSearchResultsPage());
        return getNextPage();
    }

    public String resourceURL() {
        LOG.info("resourceURL ...");
        if (mediaRecord != null) {
            String location = mediaRecord.location();
            String fileFormat = mediaRecord.format();
            if ((fileFormat == null || fileFormat.equals("application/octet-stream") || fileFormat.equals("video/x-flv")) && location != null) {
                String fileExtension = "";
                if (location != null && location.length() > 0) {
                    int lastDot = location.lastIndexOf(".");
                    if (lastDot >= 0) {
                        fileExtension = location.substring(lastDot + 1, location.length()).toLowerCase();
                    }
                }
                if (fileExtension.equals("flv")) {
                    fileFormat = "flv-application/octet-stream";
                }
            }
            if (fileFormat != null && fileFormat.equals("flv-application/octet-stream")) {
                String previewPath = "";
                try {
                    previewPath = URLEncoder.encode(location, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                String flv_preview_path = CXDefaults.sharedDefaults().getString("FLVPreviewHTMLURL");
                if (flv_preview_path != null && flv_preview_path != "") {
                    location = flv_preview_path + "?previewPath=" + previewPath;
                }
            }
            return location;
        }
        return null;
    }

    public String thumbnailImage() {
        return ((CXManagedObject) getObject()).previewImage().url().toString();
    }

    public String getDateModified() {
        NSTimestamp nstValue = mediaRecord.dateModified();
        return (nstValue == null ? "None" : nstValue.toString());
    }

    public String getDateSubmitted() {
        NSTimestamp nstValue = mediaRecord.dateSubmitted();
        return (nstValue == null ? "None" : nstValue.toString());
    }

    public WOComponent getSearchResultsPage() {
        return _searchResultsPage;
    }

    public void setSearchResultsPage(WOComponent page) {
        _searchResultsPage = page;
    }
}
