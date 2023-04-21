package openvend.captcha;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import openvend.main.OvLog;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import com.octo.captcha.Captcha;
import com.octo.captcha.CaptchaException;
import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.CaptchaEngineException;
import com.octo.captcha.image.ImageCaptchaFactory;

/**
 * Abstract base class for captcha engine implementations.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.5 $
 * @since 1.0
 */
public abstract class A_OvCaptchaEngine implements CaptchaEngine {

    private static Log log = OvLog.getLog(A_OvCaptchaEngine.class);

    public static final String ACCEPTED_CHARS = "ABCDEFGHJKMNPQRSTWXYZ23456789";

    private List factories;

    private Random random;

    private Integer imgWidth;

    private Integer imgHeight;

    private Integer minWordLen;

    private Integer maxWordLen;

    private Integer minFontSize;

    private Integer maxFontSize;

    private boolean deformEnabled;

    /**
     * Initializes this captcha engine.<p/>
     * 
     * @param params the parameters of the captcha service class
     * @throws Exception
     */
    public void init(Map params) throws Exception {
        this.factories = new ArrayList();
        this.random = new SecureRandom();
        this.imgWidth = asInteger((String) params.get("img-width"), 250);
        this.imgHeight = asInteger((String) params.get("img-height"), 70);
        this.minWordLen = asInteger((String) params.get("min-word-len"), 6);
        this.maxWordLen = asInteger((String) params.get("max-word-len"), 6);
        this.minFontSize = asInteger((String) params.get("min-font-size"), 20);
        this.maxFontSize = asInteger((String) params.get("max-font-size"), 35);
        this.deformEnabled = "true".equalsIgnoreCase((String) params.get("deform-enabled"));
        buildInitialFactories();
        checkFactoriesSize();
    }

    protected abstract void buildInitialFactories();

    protected void checkFactoriesSize() {
        if (factories.size() == 0) {
            throw new CaptchaException("This gimpy has no factories. Please initialize it " + "properly with the buildInitialFactory() called by " + "the constructor or the addFactory() mehtod later!");
        }
    }

    protected Integer asInteger(String value, int defaultValue) throws Exception {
        Integer result = null;
        try {
            if (StringUtils.isNotEmpty(value)) {
                result = Integer.valueOf(value);
            }
        } catch (NumberFormatException e) {
            String errorMessage = "Error parsing value " + value + " as a number!";
            if (log.isErrorEnabled()) {
                log.error(errorMessage, e);
            }
            result = new Integer(defaultValue);
        }
        return result;
    }

    /**
     * Tests if deforming is enabled.<p/>
     * 
     * @return true if deforming is enabled
     */
    public boolean isDeformEnabled() {
        return deformEnabled;
    }

    /**
     * Returns the image height.<p/>
     * 
     * @return the image height
     */
    public Integer getImgHeight() {
        return imgHeight;
    }

    /**
     * Returns the image width.<p/>
     * 
     * @return the image width
     */
    public Integer getImgWidth() {
        return imgWidth;
    }

    /**
     * Returns the max. font size.<p/>
     * 
     * @return the max. font size
     */
    public Integer getMaxFontSize() {
        return maxFontSize;
    }

    /**
     * Returns the max. word length.<p/>
     * 
     * @return the max. word length
     */
    public Integer getMaxWordLen() {
        return maxWordLen;
    }

    /**
     * Returns the min. font size.<p/>
     * 
     * @return the min. font size
     */
    public Integer getMinFontSize() {
        return minFontSize;
    }

    /**
     * Returns the min. word length.<p/>
     * 
     * @return the min. word length
     */
    public Integer getMinWordLen() {
        return minWordLen;
    }

    public com.octo.captcha.image.ImageCaptchaFactory getImageCaptchaFactory() {
        return (ImageCaptchaFactory) factories.get(random.nextInt(factories.size()));
    }

    /**
     * @see com.octo.captcha.engine.CaptchaEngine#getNextCaptcha()
     */
    public Captcha getNextCaptcha() {
        return getImageCaptchaFactory().getImageCaptcha();
    }

    /**
     * @see com.octo.captcha.engine.CaptchaEngine#getNextCaptcha(java.util.Locale)
     */
    public Captcha getNextCaptcha(Locale locale) {
        return getImageCaptchaFactory().getImageCaptcha(locale);
    }

    /**
     * @see com.octo.captcha.engine.CaptchaEngine#getFactories()
     */
    public CaptchaFactory[] getFactories() {
        return (CaptchaFactory[]) factories.toArray(new CaptchaFactory[factories.size()]);
    }

    /**
     * @see com.octo.captcha.engine.CaptchaEngine#setFactories(com.octo.captcha.CaptchaFactory[])
     */
    public void setFactories(CaptchaFactory[] fact) throws CaptchaEngineException {
        if (fact == null || fact.length == 0) {
            throw new CaptchaEngineException("Impossible to set null or empty factories");
        }
        List tempFactories = new ArrayList();
        for (int i = 0; i < fact.length; i++) {
            if (ImageCaptchaFactory.class.isAssignableFrom(fact[i].getClass())) {
                throw new CaptchaEngineException("This factory is not an image captcha factory " + fact[i].getClass());
            }
            tempFactories.add(fact[i]);
        }
        factories = tempFactories;
    }

    public boolean addFactory(CaptchaFactory factory) {
        return factories.add(factory);
    }
}
