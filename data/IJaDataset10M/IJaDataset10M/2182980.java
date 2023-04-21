package com.herestudio.pojo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * �û�վ�㼶�����¼
 * 
 * @author luzm
 */
public class GainLevelHistory implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3258132449056993337L;

    /**
	 * 
	 */
    private String gainId;

    /**
	 * �û� id
	 */
    private Serializable userId;

    /**
	 * �����
	 */
    private int levelId;

    /**
	 * ��ʱ��
	 */
    private Date gainTime;

    public String toString() {
        return new ToStringBuilder(this).append("gainId", gainId).append("userId", userId).append("levelId", levelId).append("gainYime", gainTime).toString();
    }

    public String getGainId() {
        return gainId;
    }

    public void setGainId(String gainId) {
        this.gainId = gainId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public Serializable getUserId() {
        return userId;
    }

    public void setUserId(Serializable userId) {
        this.userId = userId;
    }

    public Date getGainTime() {
        return gainTime;
    }

    public void setGainTime(Date gainTime) {
        this.gainTime = gainTime;
    }
}
