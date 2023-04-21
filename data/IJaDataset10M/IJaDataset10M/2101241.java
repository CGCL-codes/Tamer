package com.mascotikas.server.dao;

import com.google.appengine.api.datastore.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.mascotikas.client.jdos.ImageInfo;
import com.mascotikas.shared.UploadedImage;

public class UploadedImageDao {

    DatastoreService datastore;

    public UploadedImageDao() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public UploadedImage get(String encodedKey) {
        Key key = KeyFactory.stringToKey(encodedKey);
        try {
            Entity result = datastore.get(key);
            UploadedImage image = fromEntity(result);
            image.setKey(encodedKey);
            return image;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public List<UploadedImage> getRecent() {
        Query query = new Query("UploadedImage");
        query.addSort(UploadedImage.CREATED_AT, SortDirection.DESCENDING);
        FetchOptions options = FetchOptions.Builder.withLimit(25);
        ArrayList<UploadedImage> results = new ArrayList<UploadedImage>();
        for (Entity result : datastore.prepare(query).asIterable(options)) {
            UploadedImage image = fromEntity(result);
            results.add(image);
        }
        return results;
    }

    public List<UploadedImage> getRecentByCategory(String category) {
        Query query = new Query("UploadedImage");
        query.addFilter(UploadedImage.CATEGORY, Query.FilterOperator.EQUAL, category);
        query.addSort(UploadedImage.CREATED_AT, SortDirection.DESCENDING);
        FetchOptions options = FetchOptions.Builder.withLimit(25);
        ArrayList<UploadedImage> results = new ArrayList<UploadedImage>();
        for (Entity result : datastore.prepare(query).asIterable(options)) {
            UploadedImage image = fromEntity(result);
            results.add(image);
        }
        return results;
    }

    public void delete(String encodedKey) {
        Key key = KeyFactory.stringToKey(encodedKey);
        datastore.delete(key);
    }

    private UploadedImage fromEntity(Entity result) {
        UploadedImage image = new UploadedImage();
        image.setCreatedAt((Date) result.getProperty(UploadedImage.CREATED_AT));
        image.setServingUrl((String) result.getProperty(UploadedImage.SERVING_URL));
        image.setName((String) result.getProperty(ImageInfo.NAME));
        image.setComment((String) result.getProperty(ImageInfo.COMMENT));
        image.setPrice(String.valueOf(result.getProperty(ImageInfo.PRICE)));
        image.setTypePublish((String) result.getProperty(ImageInfo.TYPEPUBLISH));
        image.setLocate((String) result.getProperty(ImageInfo.LOCATE));
        image.setOwnerId((String) result.getProperty(UploadedImage.OWNER_ID));
        if (image.getKey() == null) {
            String encodedKey = KeyFactory.keyToString(result.getKey());
            image.setKey(encodedKey);
        }
        return image;
    }

    public String putImageInfo(ImageInfo infoImage) {
        Key key = KeyFactory.stringToKey(infoImage.getPhotoKey());
        try {
            Entity result = datastore.get(key);
            result.setProperty(UploadedImage.CATEGORY, infoImage.getCategory());
            result.setProperty(ImageInfo.COMMENT, infoImage.getComment());
            result.setProperty(ImageInfo.LOCATE, infoImage.getLocate());
            result.setProperty(ImageInfo.NAME, infoImage.getName());
            result.setProperty(ImageInfo.TYPEPUBLISH, infoImage.getTypePublish());
            result.setProperty(ImageInfo.PRICE, infoImage.getPrice());
            datastore.put(result);
            Entity uploadedImageInfo = new Entity(ImageInfo.class.getSimpleName());
            uploadedImageInfo.setProperty("blobKey", infoImage.getPhotoKey());
            uploadedImageInfo.setProperty(ImageInfo.COMMENT, infoImage.getComment());
            uploadedImageInfo.setProperty(ImageInfo.CATEGORY, infoImage.getCategory());
            uploadedImageInfo.setProperty(ImageInfo.LOCATE, infoImage.getLocate());
            uploadedImageInfo.setProperty(ImageInfo.NAME, infoImage.getName());
            uploadedImageInfo.setProperty(ImageInfo.TYPEPUBLISH, infoImage.getTypePublish());
            uploadedImageInfo.setProperty(ImageInfo.PRICE, infoImage.getPrice());
            datastore.put(uploadedImageInfo);
            return infoImage.getPhotoKey();
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public List<UploadedImage> getImagesByOwnerId(String ownerId) {
        Query query = new Query("UploadedImage");
        query.addSort(UploadedImage.CREATED_AT, SortDirection.DESCENDING);
        query.addFilter(UploadedImage.OWNER_ID, Query.FilterOperator.EQUAL, ownerId);
        FetchOptions options = FetchOptions.Builder.withLimit(25);
        ArrayList<UploadedImage> results = new ArrayList<UploadedImage>();
        for (Entity result : datastore.prepare(query).asIterable(options)) {
            UploadedImage image = fromEntity(result);
            results.add(image);
        }
        return results;
    }

    public UploadedImage updateUploadImageCategory(String encodedKey, String category) {
        Key key = KeyFactory.stringToKey(encodedKey);
        try {
            Transaction tr = datastore.beginTransaction();
            Entity result = datastore.get(key);
            result.setProperty(UploadedImage.CATEGORY, category);
            datastore.put(result);
            UploadedImage image = fromEntity(result);
            image.setKey(encodedKey);
            tr.commit();
            return image;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public ImageInfo getImageInfo(String key) {
        return null;
    }
}
