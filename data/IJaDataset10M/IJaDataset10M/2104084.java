package cz.fi.muni.xkremser.editor.shared.rpc;

import java.io.Serializable;
import java.util.ArrayList;
import cz.fi.muni.xkremser.editor.shared.domain.DigitalObjectModel;

/**
 * @author Jiri Kremser
 * @version 29.10.2011
 */
public class NewDigitalObject implements Serializable {

    private static final long serialVersionUID = -8145898237441105426L;

    private int pageIndex;

    private String name;

    private ArrayList<NewDigitalObject> children = new ArrayList<NewDigitalObject>();

    private DigitalObjectModel model;

    private MetadataBundle bundle;

    private String uuid;

    private boolean exist;

    private String sysno;

    private String path;

    private boolean visible;

    private String pageType;

    private String dateIssued;

    private String altoPath;

    private String ocrPath;

    @SuppressWarnings("unused")
    private NewDigitalObject() {
    }

    public NewDigitalObject(String name) {
        super();
        this.name = name;
    }

    public NewDigitalObject(int pageIndex, String name, DigitalObjectModel model, MetadataBundle bundle, String uuid, boolean exist) {
        super();
        this.pageIndex = pageIndex;
        this.name = name;
        this.model = model;
        this.bundle = bundle;
        this.uuid = uuid;
        this.exist = exist;
    }

    public NewDigitalObject(NewDigitalObject newDigitalObject) {
        super();
        this.pageIndex = newDigitalObject.getPageIndex();
        this.name = newDigitalObject.getName();
        this.children = newDigitalObject.getChildren();
        this.model = newDigitalObject.getModel();
        this.bundle = newDigitalObject.getBundle();
        this.uuid = newDigitalObject.getUuid();
        this.exist = newDigitalObject.getExist();
        this.sysno = newDigitalObject.getSysno();
        this.path = newDigitalObject.getPath();
        this.visible = newDigitalObject.getVisible();
        this.pageType = newDigitalObject.getPageType();
        this.dateIssued = newDigitalObject.getDateIssued();
        this.altoPath = newDigitalObject.getAltoPath();
        this.ocrPath = newDigitalObject.getOcrPath();
    }

    /**
     * @return the sysno
     */
    public String getSysno() {
        return sysno;
    }

    /**
     * @param sysno
     *        the sysno to set
     */
    public void setSysno(String sysno) {
        this.sysno = sysno;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public DigitalObjectModel getModel() {
        return model;
    }

    public void setModel(DigitalObjectModel model) {
        this.model = model;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ArrayList<NewDigitalObject> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<NewDigitalObject> children) {
        this.children = children;
    }

    public boolean getExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MetadataBundle getBundle() {
        return bundle;
    }

    public void setBundle(MetadataBundle bundle) {
        this.bundle = bundle;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    /**
     * @return the dateIssued
     */
    public String getDateIssued() {
        return dateIssued;
    }

    /**
     * @param dateIssued
     *        the dateIssued to set
     */
    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    /**
     * @return the altoPath
     */
    public String getAltoPath() {
        return altoPath;
    }

    /**
     * @param altoPath
     *        the altoPath to set
     */
    public void setAltoPath(String altoPath) {
        this.altoPath = altoPath;
    }

    /**
     * @return the ocrPath
     */
    public String getOcrPath() {
        return ocrPath;
    }

    /**
     * @param ocrPath
     *        the ocrPath to set
     */
    public void setOcrPath(String ocrPath) {
        this.ocrPath = ocrPath;
    }

    @Override
    public String toString() {
        return model + "  --  " + name;
    }
}
