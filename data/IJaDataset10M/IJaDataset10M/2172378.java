package welo.page.admin.media;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.file.Folder;

/**
 * Manage Media
 * 
 * @author james.yong
 * 
 */
public class PnCmsMgtMedia extends Panel {

    Panel panel;

    public PnCmsMgtMedia(String id) throws Exception {
        super(id);
        this.panel = new MediaUploadPanel("panel", new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads"));
        add(panel);
    }
}
