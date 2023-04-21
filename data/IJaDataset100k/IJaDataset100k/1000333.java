package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.BinaryDesc;
import org.imogene.web.gwt.client.BinaryTools;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.upload.ImogBinaryUploader;
import org.imogene.web.gwt.remote.UtilServicesAsyncFacade;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Composite to manage the display of Photo fields
 * @author Medes-IMPS
 */
public class ImogPhotoField extends ImogFieldAbstract<String> implements ImogUploader {

    private String thisLabel;

    private String thisValue;

    private boolean edited = false;

    private HorizontalPanel main;

    private VerticalPanel editPanel;

    private ImogBinaryUploader uploader;

    private HorizontalPanel infoPanel;

    private VerticalPanel downloadPanel;

    private HTML nameLabel;

    private HTML sizeLabel;

    private HTML downloadLink;

    private Image thumbnail;

    public ImogPhotoField() {
        layout();
    }

    public ImogPhotoField(String label) {
        this();
        thisLabel = label;
    }

    private void layout() {
        main = new HorizontalPanel();
        thumbnail = new Image(GWT.getModuleBaseURL() + "images/no_photo.png");
        thumbnail.setPixelSize(50, 65);
        editPanel = new VerticalPanel();
        uploader = new ImogBinaryUploader();
        editPanel.add(uploader);
        infoPanel = new HorizontalPanel();
        downloadPanel = new VerticalPanel();
        thumbnail = new Image(GWT.getModuleBaseURL() + "images/no_photo.png");
        thumbnail.setPixelSize(65, 50);
        nameLabel = new HTML(BaseNLS.messages().field_binary_file(BaseNLS.constants().binary_nofile()));
        sizeLabel = new HTML(BaseNLS.messages().field_binary_size("-"));
        downloadLink = new HTML();
        downloadPanel.add(nameLabel);
        downloadPanel.add(sizeLabel);
        infoPanel.add(downloadPanel);
        infoPanel.add(downloadLink);
        main.add(thumbnail);
        main.add(infoPanel);
        initWidget(main);
        properties();
    }

    private void properties() {
        main.setSpacing(3);
        main.setWidth("100%");
        main.setCellWidth(thumbnail, "50px");
        nameLabel.setStyleName("imogene-greytext");
        sizeLabel.setStyleName("imogene-greytext");
    }

    @Override
    public String getLabel() {
        if (thisLabel != null) return thisLabel;
        return "";
    }

    @Override
    public String getValue() {
        if (uploader.getEntityId() != null) return uploader.getEntityId();
        return thisValue;
    }

    @Override
    public void setLabel(String label) {
        thisLabel = label;
    }

    @Override
    public void setValue(String value) {
        if (value != null && !"".equals(value)) {
            setThumbnail(value);
            setDownloadLink(value);
            if (value != thisValue) setBinaryMetadata(value);
        }
        thisValue = value;
    }

    @Override
    public void setValue(String value, boolean notifyChange) {
        setValue(value);
        if (notifyChange) notifyValueChange();
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void setEnabled(boolean editable) {
        if (!edited && editable) {
            main.remove(infoPanel);
            main.add(editPanel);
            main.setCellVerticalAlignment(editPanel, HorizontalPanel.ALIGN_MIDDLE);
        }
        if (edited && !editable) {
            main.remove(editPanel);
            main.add(infoPanel);
        }
        edited = editable;
    }

    @Override
    public boolean isEdited() {
        return edited;
    }

    /**
	 * set the thumbnail image 
	 */
    private void setThumbnail(String value) {
        thumbnail.setUrl(BinaryTools.LINK_THUMB + value);
    }

    /**
	 * display the download link.
	 */
    private void setDownloadLink(String value) {
        downloadLink.setHTML(BinaryTools.DOWNLOAD_TMPL.replace("%PARAM_ID%", value));
    }

    @Override
    public boolean isUploading() {
        return uploader.isUploading();
    }

    /**
	 * display the binary meta-data
	 */
    public void setBinaryMetadata(String value) {
        UtilServicesAsyncFacade.getInstance().getBinaryDesc(value, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                BinaryDesc binaryDesc = BinaryTools.createBinaryDesc(result);
                nameLabel.setHTML(BaseNLS.messages().field_binary_file(binaryDesc.getName()));
                sizeLabel.setHTML(BaseNLS.messages().field_binary_size(BinaryTools.getSizeAsString(binaryDesc.getSize())));
            }

            @Override
            public void onFailure(Throwable arg0) {
            }
        });
    }
}
