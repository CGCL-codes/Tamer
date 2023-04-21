package nl.captcha.obscurity;

import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface BackgroundProducer {

    public static final String SIMPLE_CAPCHA_BCKGRND_CLR_FRM = "cap.background.c.from";

    public static final String SIMPLE_CAPCHA_BCKGRND_CLR_T = "cap.background.c.to";

    public static final String SIMPLE_CAPCHA_BCKGRND_CLR = "cap.background.c";

    public abstract void setProperties(Properties props);

    public abstract BufferedImage addBackground(BufferedImage image);
}
