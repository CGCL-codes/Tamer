package wangzx.gwt.databean.client.test;

import wangzx.gwt.databean.client.model.AttachmentContainer;
import wangzx.gwt.databean.client.model.FieldInfo;
import wangzx.gwt.databean.client.model.FieldInfo.UI;
import junit.framework.TestCase;

/**
 * Test features: Can display image and upload image
 *
 */
public class ProductTest extends TestCase {

    public static class Product implements AttachmentContainer {

        int id;

        String name;

        @FieldInfo(ui = UI.image)
        String image = "image";

        public boolean allowAttachments() {
            return id > 0;
        }

        /**
		 * 
		 * in general, the /attachments maps to a servlet which support PathInfo like
		 * /attachments/object-type/id/attach-id path format.
		 * 
		 */
        public String getResourcePath() {
            return "/attachments/product/" + id;
        }
    }

    ;
}
