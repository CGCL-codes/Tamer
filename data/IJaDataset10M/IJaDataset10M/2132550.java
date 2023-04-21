package org.zkoss.eclipse.setting.zklib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.zkoss.eclipse.setting.ImageProvider;
import org.zkoss.eclipse.setting.ZKStudioPlugin;
import org.zkoss.eclipse.setting.zklib.archive.ZkPackage;
import org.zkoss.eclipse.util.BundleResourceManager;

/**
 * @author Ian Tsai
 *
 */
public class ZkPackageImageProvider extends ImageProvider {

    /**
	 * 
	 * @param pack
	 * @return
	 */
    public Image getImage(ZkPackage pack) {
        long start = System.currentTimeMillis();
        Image img = getImage0(pack);
        return img;
    }

    /**
	 * 
	 * @param pack
	 * @return
	 */
    public Image getImage0(ZkPackage pack) {
        int dir = pack.isDirectory() ? Icons.FOLDER : Icons.ARCHIVE;
        if (!pack.exists()) return this.getImage(Display.getCurrent(), Icons.getIconPath(dir | Icons.WORN));
        return this.getImage(Display.getCurrent(), Icons.getIconPath(dir));
    }
}
