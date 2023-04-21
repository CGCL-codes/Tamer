package com.lingway.webapp.antui.control.impl.ant;

import com.lingway.webapp.antui.control.impl.AEchoSpringController;
import com.lingway.webapp.antui.control.impl.EchoSpringControllerFactory;

/**
 * <dl>
 * <dt><b>Creation date :</b></dt>
 * <dd>9 mars 2007</dd>
 *
 * @author Cedric CHAMPEAU <cedric.champeau[at]lingway[dot]com>
 *         </dl>
 */
public class AntTaskRunController extends AEchoSpringController {

    private String theTitle;

    public AntTaskRunController(EchoSpringControllerFactory factory) {
        super(factory);
    }

    public void setup() {
    }

    public String getTitle() {
        return theTitle;
    }

    public void setTitle(String aTitle) {
        theTitle = aTitle;
    }
}
