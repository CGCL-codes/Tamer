package org.openXpertya.model;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openXpertya.util.CCache;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MImage extends X_AD_Image {

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param AD_Image_ID
     *
     * @return
     */
    public static MImage get(Properties ctx, int AD_Image_ID) {
        Integer key = new Integer(AD_Image_ID);
        MImage retValue = (MImage) s_cache.get(key);
        if (retValue != null) {
            return retValue;
        }
        retValue = new MImage(ctx, AD_Image_ID, null);
        if (retValue.getID() != 0) {
            s_cache.put(key, retValue);
        }
        return retValue;
    }

    /** Descripción de Campos */
    private static CCache s_cache = new CCache("AD_Image", 20);

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param AD_Image_ID
     * @param trxName
     */
    public MImage(Properties ctx, int AD_Image_ID, String trxName) {
        super(ctx, AD_Image_ID, trxName);
        if (AD_Image_ID < 1) {
            setName("-/-");
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MImage(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** Descripción de Campos */
    private ImageIcon m_image = null;

    /** Descripción de Campos */
    private File m_file = null;

    /**
     * Descripción de Método
     *  Retorna la imagen del adjunto y si no la de la URL
     *
     * @return
     */
    public Image getImage() {
        Image aImage = null;
        MAttachment attach = getAttachment(true);
        if (attach != null) {
            ImageIcon aIcon = null;
            MAttachmentEntry entry = attach.getEntry(0);
            byte[] data = entry.getData();
            if (data != null) aIcon = new ImageIcon(data);
            aImage = aIcon.getImage();
        }
        if (aImage == null) {
            URL url = getURL();
            if (url != null) {
                Toolkit tk = Toolkit.getDefaultToolkit();
                aImage = tk.getImage(url);
            }
        }
        return aImage;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public Icon getIcon() {
        URL url = getURL();
        if (url == null) {
            return null;
        }
        return new ImageIcon(url);
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    private URL getURL() {
        String str = getImageURL();
        if ((str == null) || (str.length() == 0)) {
            return null;
        }
        URL url = null;
        try {
            if (str.indexOf("://") != -1) {
                url = new URL(str);
            } else {
                url = getClass().getResource(str);
            }
            if (url == null) {
                log.warning("Not found: " + str);
            }
        } catch (Exception e) {
            log.warning("Not found: " + str + " - " + e.getMessage());
        }
        return url;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        return "MImage[ID=" + getID() + ",Name=" + getName() + "]";
    }
}
