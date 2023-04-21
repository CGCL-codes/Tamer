package com.mascotikas.client.animals;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mascotikas.client.jdos.ImageInfo;
import com.mascotikas.client.services.IUserImageService;
import com.mascotikas.client.services.IUserImageServiceAsync;
import com.mascotikas.shared.UploadedImage;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceImageField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import java.util.List;

public class ImageBlobStoreDs extends DataSource {

    private DataSourceTextField commonNameField = new DataSourceTextField("name", "Nombre");

    private DataSourceTextField commentFiled = new DataSourceTextField("comment", "Descripcion");

    private DataSourceTextField ubicacionField = new DataSourceTextField("locate", "Ubicaci�n");

    private DataSourceTextField typeField = new DataSourceTextField("typwPublish", "Tipo Publicacion");

    private DataSourceTextField infoField = new DataSourceTextField("price", "Precio");

    private DataSourceImageField pictureField = new DataSourceImageField("picture", "Picture");

    IUserImageServiceAsync userImageService = GWT.create(IUserImageService.class);

    public static ImageBlobStoreDs getInstance(String categoria) {
        return new ImageBlobStoreDs("DS-" + categoria, categoria);
    }

    public ImageBlobStoreDs(String id, String categoria) {
        pictureField.setImageWidth(150);
        pictureField.setImageHeight(75);
        userImageService.getRecentlyUploadedByCategory(categoria, new AsyncCallback<List<UploadedImage>>() {

            @Override
            public void onSuccess(List<UploadedImage> images) {
                for (final UploadedImage image : images) {
                    Record record = new Record();
                    record.setAttribute(ImageInfo.NAME, image.getName());
                    record.setAttribute("picture", image.getServingUrl());
                    record.setAttribute(ImageInfo.CATEGORY, image.getCategory());
                    record.setAttribute(ImageInfo.COMMENT, image.getComment());
                    record.setAttribute(ImageInfo.LOCATE, image.getLocate());
                    record.setAttribute(ImageInfo.TYPEPUBLISH, image.getTypePublish());
                    addData(record);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
        commentFiled.setRequired(true);
        commentFiled.setPrimaryKey(true);
        infoField.setLength(1000);
        setFields(commonNameField, commentFiled, ubicacionField, typeField, infoField, pictureField);
        setClientOnly(true);
    }

    public ImageBlobStoreDs() {
        setFields(pictureField);
    }

    @Override
    protected Object transformRequest(DSRequest dsRequest) {
        return super.transformRequest(dsRequest);
    }

    public DataSourceTextField getCommonNameField() {
        return commonNameField;
    }

    public void setCommonNameField(DataSourceTextField commonNameField) {
        this.commonNameField = commonNameField;
    }

    public void setInfoField(DataSourceTextField infoField) {
        this.infoField = infoField;
    }

    public DataSourceTextField getUbicacionField() {
        return ubicacionField;
    }

    public void setUbicacionField(DataSourceTextField ubicacionField) {
        this.ubicacionField = ubicacionField;
    }

    public DataSourceImageField getPictureField() {
        return pictureField;
    }

    public void setPictureField(DataSourceImageField pictureField) {
        this.pictureField = pictureField;
    }

    public DataSourceTextField getCommentFiled() {
        return commentFiled;
    }

    public void setCommentFiled(DataSourceTextField commentFiled) {
        this.commentFiled = commentFiled;
    }

    public DataSourceTextField getTypeField() {
        return typeField;
    }

    public void setTypeField(DataSourceTextField typeField) {
        this.typeField = typeField;
    }
}
