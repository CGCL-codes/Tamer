package com.softwarepearls.apps.ide.netbeans.ui;

import com.softwarepearls.lego.comms.net.svs.SourceViewerService;
import java.awt.Color;

/**********************************************************************************************
 * All the constants used in Netbeans {@link SourceViewerService} plugin.
 *
 * @author Ivo Tripunovic
 *********************************************************************************************/
public class PluginConstants {

    /** Default TCP Port number */
    public static final int DEFAULT_PORT_NUMBER = 5000;

    /** Key value of TCP Port number, for storing in internal NetBeans registry */
    public static final String TCP_PORT_KEY = "tcpPort";

    /** Color of background, for showing error in text component */
    public static final Color ERROR_BACKGROUND_COLOR = Color.PINK;

    private PluginConstants() {
        throw new AssertionError();
    }
}
