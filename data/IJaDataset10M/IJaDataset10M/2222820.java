package com.liferay.portal.model;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.ejb.LayerManagerUtil;
import com.liferay.portal.ejb.LayerPK;

/**
 * <a href="Skin.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.14 $
 *
 */
public class Skin extends SkinModel {

    public static final String DEFAULT_SKIN_ID = "01";

    public static final String[][] SKIN_CLASSES = { { "alpha_background", "Heading Background" }, { "alpha_foreground", "Heading Text" }, { "beta_background", "Sub-Head 1 Background" }, { "beta_foreground", "Sub-Head 1 Text" }, { "gamma_background", "Sub-Head 2 Background" }, { "gamma_foreground", "Sub-Head 2 Text" }, { "bg_background", "Page Background" }, { "bg_foreground", "Page Text" }, { "gamma_href", "Link Color" }, { "gamma_neg_alert", "Negative Alert" }, { "gamma_pos_alert", "Positive Alert" } };

    public Skin() {
        super();
    }

    public Skin(String skinId) {
        super(skinId);
    }

    public Skin(String skinId, String name, String imageId, String alphaLayerId, String alphaSkinId, String betaLayerId, String betaSkinId, String gammaLayerId, String gammaSkinId, String bgLayerId, String bgSkinId) {
        super(skinId, name, imageId, alphaLayerId, alphaSkinId, betaLayerId, betaSkinId, gammaLayerId, gammaSkinId, bgLayerId, bgSkinId);
    }

    public Skin(String skinId, String name, String imageId, Layer alpha, Layer beta, Layer gamma, Layer bg) {
        super(skinId, name, imageId, null, null, null, null, null, null, null, null);
        setAlpha(alpha);
        setBeta(beta);
        setGamma(gamma);
        setBg(bg);
    }

    public Layer getAlpha() throws PortalException, SystemException {
        if (_alpha == null) {
            _alpha = LayerManagerUtil.getLayer(new LayerPK(getAlphaLayerId(), getAlphaSkinId()));
        }
        return _alpha;
    }

    public void setAlpha(Layer alpha) {
        _alpha = alpha;
        setAlphaLayerId(alpha.getLayerId());
        setAlphaSkinId(alpha.getSkinId());
    }

    public Layer getBeta() throws PortalException, SystemException {
        if (_beta == null) {
            _beta = LayerManagerUtil.getLayer(new LayerPK(getBetaLayerId(), getBetaSkinId()));
        }
        return _beta;
    }

    public void setBeta(Layer beta) {
        _beta = beta;
        setBetaLayerId(beta.getLayerId());
        setBetaSkinId(beta.getSkinId());
    }

    public Layer getGamma() throws PortalException, SystemException {
        if (_gamma == null) {
            _gamma = LayerManagerUtil.getLayer(new LayerPK(getGammaLayerId(), getGammaSkinId()));
        }
        return _gamma;
    }

    public void setGamma(Layer gamma) {
        _gamma = gamma;
        setGammaLayerId(gamma.getLayerId());
        setGammaSkinId(gamma.getSkinId());
    }

    public Layer getBg() throws PortalException, SystemException {
        if (_bg == null) {
            _bg = LayerManagerUtil.getLayer(new LayerPK(getBgLayerId(), getBgSkinId()));
        }
        return _bg;
    }

    public void setBg(Layer bg) {
        _bg = bg;
        setBgLayerId(bg.getLayerId());
        setBgSkinId(bg.getSkinId());
    }

    private Layer _alpha;

    private Layer _beta;

    private Layer _gamma;

    private Layer _bg;
}
