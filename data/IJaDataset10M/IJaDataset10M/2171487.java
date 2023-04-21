package cn.collin.log.domain;

import java.util.Date;

/**
 * 操作日志domain
 * 
 * @author collin.code@gmail.com
 * 
 */
public class Log implements java.io.Serializable {

    /**
	 * log level p1
	 */
    public static final Long LEVEL_P1 = new Long(1L);

    /**
	 * log level p2
	 */
    public static final Long LEVEL_P2 = new Long(2L);

    /**
	 * log level p3
	 */
    public static final Long LEVEL_P3 = new Long(3L);

    /**
	 * 唯一ID
	 */
    private Long id;

    /**
	 * 操作用户ID
	 */
    private Long userId;

    /**
	 * log级别
	 */
    private Long level;

    /**
	 * 操作类型
	 */
    private Long typeId;

    /**
	 * 操作描述
	 */
    private String description;

    /**
	 * 操作时间
	 */
    private Date logTime;

    /**
	 * 备注
	 */
    private String note;

    public Log() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Log other = (Log) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }
}
