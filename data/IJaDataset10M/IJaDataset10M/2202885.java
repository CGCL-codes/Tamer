package org.dengues.designer.ui.properties.generators;

import org.dengues.designer.ui.properties.controllers.AbstractPropertyController;
import org.dengues.designer.ui.properties.controllers.WebPropertyController;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 1 2006-09-29 17:06:40Z qiang.zhang $
 * 
 */
public class WebControllerGenerator extends AbstractControllerGenerator {

    /**
     * Qiang.Zhang.Adolf@gmail.com WebControllerGenerator constructor comment.
     */
    public WebControllerGenerator() {
    }

    @Override
    public AbstractPropertyController generate() {
        return new WebPropertyController(dps);
    }
}
