package fr.cnes.sitools.dataset.view.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import fr.cnes.sitools.common.model.IResource;

/**
 * Form component class
 * 
 * @author AKKA
 * 
 */
@XStreamAlias("datasetView")
public final class DatasetView implements IResource {

    /** Id */
    private String id;

    /** JS object associated */
    private String jsObject;

    /** fileUrl */
    private String fileUrl;

    /** imageUrl */
    private String imageUrl;

    /** priority */
    private Integer priority;

    /** Name */
    private String name;

    /** Description */
    private String description;

    /**
   * Basic Constructor
   */
    public DatasetView() {
        super();
    }

    /**
   * Gets the jsObject value
   * 
   * @return the jsObject
   */
    public String getJsObject() {
        return jsObject;
    }

    /**
   * Sets the value of jsObject
   * 
   * @param jsObject
   *          the jsObject to set
   */
    public void setJsObject(String jsObject) {
        this.jsObject = jsObject;
    }

    /**
   * Gets the fileUrl value
   * 
   * @return the fileUrl
   */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
   * Sets the value of fileUrl
   * 
   * @param fileUrl
   *          the fileUrl to set
   */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
   * Gets the imageUrl value
   * 
   * @return the imageUrl
   */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
   * Sets the value of imageUrl
   * 
   * @param imageUrl
   *          the imageUrl to set
   */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
   * Gets the priority value
   * 
   * @return the priority
   */
    public Integer getPriority() {
        return priority;
    }

    /**
   * Sets the value of priority
   * 
   * @param priority
   *          the priority to set
   */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
   * Gets the name value
   * 
   * @return the name
   */
    public String getName() {
        return name;
    }

    /**
   * Sets the value of name
   * 
   * @param name
   *          the name to set
   */
    public void setName(String name) {
        this.name = name;
    }

    /**
   * Gets the description value
   * 
   * @return the description
   */
    public String getDescription() {
        return description;
    }

    /**
   * Sets the value of description
   * 
   * @param description
   *          the description to set
   */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
