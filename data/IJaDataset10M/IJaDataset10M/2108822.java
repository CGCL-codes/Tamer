package drupal.client.nodemaps;

import java.util.HashMap;
import java.util.Map;
import drupal.client.content.ContentTextAndImageAlignVertically;

public class DrupalNodeTextAndImageAlignVertically extends DrupalNode {

    private static final long serialVersionUID = 1L;

    public static String FIELD_PARENT = "field_parent";

    public static String FIELD_IMAGE_TAI_VERTICALLY = "field_image_tai_vertically";

    public DrupalNodeTextAndImageAlignVertically() {
        super();
    }

    public DrupalNodeTextAndImageAlignVertically(Map m) {
        super(m);
    }

    public DrupalNodeTextAndImageAlignVertically(ContentTextAndImageAlignVertically contentTextAndImage) {
        super(contentTextAndImage);
        setFORMAT(DrupalNode.INPUT_FORMAT_FILTERED_HTML);
        setType(DrupalNode.TYPE_TEXT_AND_IMAGE_ALIGN_VERTICALLY);
        Integer pageParentNID = contentTextAndImage.getParentNID();
        if (pageParentNID != null) setFIELD_PARENT(pageParentNID.toString());
    }

    public Integer getFIELD_PARENT() {
        Integer result = null;
        Object o = get(FIELD_PARENT);
        if (o == null) return null;
        Object[] objArray = (Object[]) o;
        if (objArray[0] instanceof HashMap) {
            HashMap<String, Object> map = (HashMap<String, Object>) objArray[0];
            result = new Integer((String) map.get(DrupalNode.NID));
        }
        return result;
    }

    public void setFIELD_PARENT(String nid) {
        Object[] o = new Object[1];
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("nid", nid);
        o[0] = map;
        put(FIELD_PARENT, o);
    }

    public DrupalField_imageMap getFIELD_IMAGE_TAI_VERTICALLY() {
        DrupalField_imageMap result = null;
        Object obj = get(FIELD_IMAGE_TAI_VERTICALLY);
        Object[] objArray = (Object[]) obj;
        HashMap<String, String> mapp;
        if (objArray[0] instanceof HashMap) {
            HashMap<String, Object> map = (HashMap<String, Object>) objArray[0];
            result = new DrupalField_imageMap(map);
        }
        return result;
    }

    public void setFIELD_IMAGE_TAI_VERTICALLY(DrupalField_imageMap map) {
        Object[] o = new Object[1];
        o[0] = map;
        put(FIELD_IMAGE_TAI_VERTICALLY, o);
    }
}
