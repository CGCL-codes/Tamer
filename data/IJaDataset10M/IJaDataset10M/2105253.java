package org.blueoxygen.cimande.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import static javax.persistence.InheritanceType.SINGLE_TABLE;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Umar Khatab umar@intercitra.com
 */
@Entity
@Table(name = Descriptor.TABLE_NAME)
@Inheritance(strategy = SINGLE_TABLE)
public class Descriptor extends DefaultPersistence {

    private static final long serialVersionUID = 1L;

    static final String TABLE_NAME = "descriptor";

    public static final int TYPE_WINDOW = 3;

    public static final int TYPE_WW = 2;

    public static final int TYPE_ACTION = 1;

    public static final int TYPE_CDML = 0;

    public static final int DESCRIPTOR_YES = 1;

    public static final int DESCRIPTOR_NO = 0;

    @Basic
    @Column(name = "name", length = 128)
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "url_descriptor")
    private String urlDescriptor;

    @Basic
    @Column(name = "url_action")
    private String urlAction;

    @ManyToOne
    @JoinColumn(name = "window_id")
    private GxWindow window;

    @Basic
    @Column(name = "descriptor_flag")
    private int descriptorFlag;

    @Basic
    @Column(name = "type_flag")
    private int typeFlag;

    public Descriptor() {
        urlDescriptor = "";
        urlAction = "";
        descriptorFlag = DESCRIPTOR_YES;
        typeFlag = TYPE_ACTION;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param aDescriptorName The name to set.
     */
    public void setName(String aDescriptorName) {
        name = aDescriptorName;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param aDescription The description to set.
     */
    public void setDescription(String aDescription) {
        description = aDescription;
    }

    /**
     * @return Returns the urlAction.
     */
    public String getUrlAction() {
        return urlAction;
    }

    /**
     * @param aURLAction The urlAction to set.
     */
    public void setUrlAction(String aURLAction) {
        urlAction = aURLAction;
    }

    /**
     * @return Returns the urlDescriptor.
     */
    public String getUrlDescriptor() {
        return urlDescriptor;
    }

    /**
     * @param aURLDescriptor The urlDescriptor to set.
     */
    public void setUrlDescriptor(String aURLDescriptor) {
        urlDescriptor = aURLDescriptor;
    }

    /**
     * @return Returns the descriptorFlag.
     */
    public int getDescriptorFlag() {
        return descriptorFlag;
    }

    /**
     * @param aDescriptorFlag The descriptorFlag to set.
     */
    public void setDescriptorFlag(int aDescriptorFlag) {
        descriptorFlag = aDescriptorFlag;
    }

    /**
     * @return Returns the typeFlag.
     */
    public int getTypeFlag() {
        return typeFlag;
    }

    /**
     * @param aTypeFlag The typeFlag to set.
     */
    public void setTypeFlag(int aTypeFlag) {
        typeFlag = aTypeFlag;
    }

    public GxWindow getWindow() {
        return window;
    }

    public void setWindow(GxWindow aWindow) {
        window = aWindow;
    }
}
