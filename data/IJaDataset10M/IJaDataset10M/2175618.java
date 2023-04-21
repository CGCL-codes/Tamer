package de.mogwai.common.web.backingbean.example;

import javax.faces.context.FacesContext;
import de.mogwai.common.web.backingbean.BackingBean;
import de.mogwai.common.web.utils.DownloadUtils;

/**
 * Test - BackingBean f�r den Dateidownload.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:18:25 $
 */
public class DownloadBackingBean extends BackingBean {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6233960974770669631L;

    public String download() {
        DownloadUtils.getInstance().sentFileToBrowser(FacesContext.getCurrentInstance(), "Lala.txt", "Lala", true);
        return null;
    }
}
