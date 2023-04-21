package org.apache.jetspeed.om.profile.psml;

import org.apache.jetspeed.services.idgenerator.JetspeedIdGenerator;
import org.apache.jetspeed.om.profile.*;

/**
 * Base simple bean-like implementation of the IdentityElement interface
 * suitable for Castor XML serialization.
 * 
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: PsmlIdentityElement.java,v 1.6 2004/02/23 03:02:54 jford Exp $
 */
public class PsmlIdentityElement extends PsmlConfigElement implements IdentityElement, java.io.Serializable {

    private String id = null;

    private MetaInfo metaInfo = null;

    private Skin skin = null;

    private Layout layout = null;

    private Control control = null;

    private Controller controller = null;

    public PsmlIdentityElement() {
    }

    /** @see org.apache.jetspeed.om.profile.IdentityElement#getId */
    public String getId() {
        if (this.id == null) {
            this.id = JetspeedIdGenerator.getNextPeid();
        }
        return this.id;
    }

    /** @see org.apache.jetspeed.om.profile.IdentityElement#setId */
    public void setId(String id) {
        this.id = id;
    }

    /** @see org.apache.jetspeed.om.profile.IdentityElement#getSkin */
    public Skin getSkin() {
        return this.skin;
    }

    /** @see org.apache.jetspeed.om.profile.IdentityElement#setSkin */
    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    /** @see org.apache.jetspeed.om.profile.IdentityElement#getLayout */
    public Layout getLayout() {
        return this.layout;
    }

    /** @see org.apache.jetspeed.om.profile.IdentityElement#setLayout */
    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    /** @see org.apache.jetspeed.om.profile.IdentityElement#getControl */
    public Control getControl() {
        return this.control;
    }

    /** @see org.apache.jetspeed.om.profile.IdentityElement#setControl */
    public void setControl(Control control) {
        this.control = control;
    }

    public Controller getController() {
        return this.controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Required by Castor 0.8.11 XML serialization for retrieving the metainfo
     * @see org.apache.jetspeed.om.profile.IdentityElement#getMetaInfo 
     */
    public MetaInfo getMetaInfo() {
        return this.metaInfo;
    }

    /** 
     * Required by Castor 0.8.11 XML serialization for setting the entry
     * metainfo
     * @see org.apache.jetspeed.om.profile.IdentityElement#setMetaInfo 
     */
    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    /** @see org.apache.jetspeed.om.profile.MetaInfo#getTitle */
    public String getTitle() {
        if (this.metaInfo != null) {
            return this.metaInfo.getTitle();
        }
        return null;
    }

    /** @see org.apache.jetspeed.om.profile.MetaInfo#setTitle */
    public void setTitle(String title) {
        if (this.metaInfo == null) {
            this.metaInfo = new PsmlMetaInfo();
        }
        this.metaInfo.setTitle(title);
    }

    /** @see org.apache.jetspeed.om.profile.MetaInfo#getDescription */
    public String getDescription() {
        if (this.metaInfo != null) {
            return this.metaInfo.getDescription();
        }
        return null;
    }

    /** @see org.apache.jetspeed.om.profile.MetaInfo#setDescription */
    public void setDescription(String description) {
        if (this.metaInfo == null) {
            this.metaInfo = new PsmlMetaInfo();
        }
        this.metaInfo.setDescription(description);
    }

    /** @see org.apache.jetspeed.om.profile.MetaInfo#getImage */
    public String getImage() {
        if (this.metaInfo != null) {
            return this.metaInfo.getImage();
        }
        return null;
    }

    /** @see org.apache.jetspeed.om.profile.MetaInfo#setImage */
    public void setImage(String image) {
        if (this.metaInfo == null) {
            this.metaInfo = new PsmlMetaInfo();
        }
        this.metaInfo.setImage(image);
    }

    /**
     * Create a clone of this object
     */
    public Object clone() throws java.lang.CloneNotSupportedException {
        Object cloned = super.clone();
        ((PsmlIdentityElement) cloned).metaInfo = ((this.metaInfo == null) ? null : (MetaInfo) this.metaInfo.clone());
        ((PsmlIdentityElement) cloned).skin = ((this.skin == null) ? null : (Skin) this.skin.clone());
        ((PsmlIdentityElement) cloned).layout = ((this.layout == null) ? null : (Layout) this.layout.clone());
        ((PsmlIdentityElement) cloned).control = ((this.control == null) ? null : (Control) this.control.clone());
        ((PsmlIdentityElement) cloned).controller = ((this.controller == null) ? null : (Controller) this.controller.clone());
        return cloned;
    }
}
