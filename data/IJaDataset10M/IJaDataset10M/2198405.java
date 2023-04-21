package org.fb4j.photo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.fb4j.impl.BooleanMethodCallBase;
import org.fb4j.photo.PhotoTag;

/**
 * @author Mino Togna
 * 
 */
public class AddPhotoTagMethodCall extends BooleanMethodCallBase {

    public AddPhotoTagMethodCall(long ownerId, PhotoTag photoTag) {
        super("photos.addTag");
        setParameter("owner_uid", String.valueOf(ownerId));
        setParameter("pid", String.valueOf(photoTag.getPhotoId()));
        setParameter("x", String.valueOf(photoTag.getXCoord()));
        setParameter("y", String.valueOf(photoTag.getYCoord()));
        if (photoTag.getSubjectId() > 0) setParameter("tag_uid", String.valueOf(photoTag.getSubjectId()));
        if (photoTag.getText() != null && !"".equals(photoTag.getText())) setParameter("tag_text", photoTag.getText());
    }

    public AddPhotoTagMethodCall(PhotoTag photoTag) {
        super("photos.addTag");
        setParameter("pid", String.valueOf(photoTag.getPhotoId()));
        setParameter("x", String.valueOf(photoTag.getXCoord()));
        setParameter("y", String.valueOf(photoTag.getYCoord()));
        if (photoTag.getSubjectId() > 0) setParameter("tag_uid", String.valueOf(photoTag.getSubjectId()));
        if (photoTag.getText() != null && !"".equals(photoTag.getText())) setParameter("tag_text", photoTag.getText());
    }

    public AddPhotoTagMethodCall(long ownerId, long photoId, PhotoTag[] photoTags) {
        super("photos.addTag");
        setParameter("owner_uid", String.valueOf(ownerId));
        setParameter("pid", String.valueOf(photoId));
        setParameter("tags", photoTagsToJsonArrayString(photoTags));
    }

    public AddPhotoTagMethodCall(long photoId, PhotoTag[] photoTags) {
        super("photos.addTag");
        setParameter("pid", String.valueOf(photoId));
        setParameter("tags", photoTagsToJsonArrayString(photoTags));
    }

    private String photoTagsToJsonArrayString(PhotoTag... photoTags) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (PhotoTag photoTag : photoTags) {
            list.add(photoTagToMap(photoTag));
        }
        return JSONArray.fromObject(list).toString();
    }

    private Map<String, String> photoTagToMap(PhotoTag photoTag) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("x", String.valueOf(photoTag.getXCoord()));
        map.put("y", String.valueOf(photoTag.getYCoord()));
        if (photoTag.getSubjectId() > 0) map.put("tag_uid", String.valueOf(photoTag.getSubjectId()));
        if (photoTag.getText() != null && !"".equals(photoTag.getText())) map.put("tag_text", photoTag.getText());
        return map;
    }
}
