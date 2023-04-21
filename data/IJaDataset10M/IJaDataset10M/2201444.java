package uk.ac.cisban.saint.client.gui;

import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author morgan on 30-Jul-2009, 17:35:54
 */
public interface SaintImageBundle extends ImageBundle {

    /**
     * btn_cancel_icon.png must be located in the package
     * com.mycompany.myapp.icons (which must be on the classpath).
     */
    @Resource("uk/ac/cisban/saint/public/images/saint-logo250x96.png")
    public AbstractImagePrototype saintLogo();

    /**
     * btn_cancel_icon.png must be located in the package
     * com.mycompany.myapp.icons (which must be on the classpath).
     */
    @Resource("uk/ac/cisban/saint/public/images/logo-small.jpg")
    public AbstractImagePrototype cisbanLogo();
}
