package de.iritgo.aktera.aktario.db;

import java.io.Serializable;

/**
 * @persist.persistent
 *   id="ApplicationInstance"
 *   name="ApplicationInstance"
 *   table="ApplicationInstance"
 *   schema="aktera"
 *   descrip="ApplicationInstance"
 *   page-size="5"
 *   securable="true"
 *   am-bypass-allowed="true"
 */
public class ApplicationInstance implements Serializable {

    /** */
    private Long id;

    /** */
    private String applicationId;

    /** */
    private String name;

    /**
	 * @persist.field
	 *   name="id"
	 *   db-name="id"
	 *   type="bigint"
	 *   primary-key="true"
	 *   null-allowed="false"
	 */
    public Long getId() {
        return id;
    }

    /**
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @persist.field
	 *   name="applicationId"
	 *   db-name="applicationId"
	 *   type="varchar"
	 *   length="80"
	 */
    public String getApplicationId() {
        return applicationId;
    }

    /**
	 */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
	 * @persist.field
	 *   name="name"
	 *   db-name="name"
	 *   type="varchar"
	 *   length="80"
	 */
    public String getName() {
        return name;
    }

    /**
	 */
    public void setName(String name) {
        this.name = name;
    }
}
