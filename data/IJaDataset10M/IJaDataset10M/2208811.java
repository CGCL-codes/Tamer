package org.spirit.bean.impl;

import java.io.Serializable;
import org.spirit.bean.impl.base.BotListBeanBase;

/**
 * This is class is used by botverse.
 * 
 * @author Berlin Brown
 * 
 */
public class BotListAdminMainBanner extends BotListBeanBase implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String headline;

    private String section;

    private String enabled;

    private boolean bannerEnabled;

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
        this.bannerEnabled = ((this.enabled != null) && (this.enabled.equalsIgnoreCase("Y"))) ? true : false;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isBannerEnabled() {
        return bannerEnabled;
    }

    public void setBannerEnabled(boolean bannerEnabled) {
        this.bannerEnabled = bannerEnabled;
        this.enabled = this.bannerEnabled ? "Y" : "N";
    }
}
