package org.fmi.bioinformatics.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author mhaimel  -  Diploma Thesis
 *
 */
public class Job {

    public static final int STAT_NEW = 0;

    public static final int STAT_READY4RUN = 1;

    public static final int STAT_RUNNING = 2;

    public static final int STAT_FINISHED = 3;

    public static final int STAT_ERROR = 4;

    public static final int STAT_RUNNING_INTERACTIVE = 5;

    public static final Integer ENTRY_ProteinOfInterest = 0;

    public static final Integer ENTRY_ProteinCollection = 1;

    private Integer id = null;

    private Integer status = null;

    private String data = null;

    private String email = null;

    private String path = null;

    private Integer entryPoint = null;

    public Job() {
        super();
        this.setId(null);
        this.setData(null);
        this.setStatus(null);
        this.setEmail(null);
        this.setPath(null);
        this.setEntryPoint(null);
    }

    public Job(Integer status, String data, String email, String path, Integer entryPoint) {
        this(null, status, data, email, path, entryPoint);
    }

    public Job(Integer id, Integer status, String data, String email, String path, Integer entryPoint) {
        this.id = id;
        this.status = status;
        this.data = data;
        this.email = email;
        this.path = path;
        this.entryPoint = entryPoint;
    }

    /**
	 * @return Returns the data.
	 */
    public String getData() {
        return data;
    }

    /**
	 * @param data The data to set.
	 */
    public void setData(String data) {
        this.data = data;
    }

    /**
	 * @return Returns the email.
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * @param email The email to set.
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * @return Returns the id.
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @param id The id to set.
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @return Returns the status.
	 */
    public Integer getStatus() {
        return status;
    }

    /**
	 * @param status The status to set.
	 */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(Integer entryPoint) {
        this.entryPoint = entryPoint;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
