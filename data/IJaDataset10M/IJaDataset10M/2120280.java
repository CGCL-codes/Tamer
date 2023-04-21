package org.yehongyu.websale.db.po.mydb;

import java.io.Serializable;
import java.util.Set;

/**
 * �־���
 */
public class SModule implements Serializable {

    private String id;

    private String modulename;

    private String upmodule;

    private String url;

    private long sortnum;

    private Set actions;

    public Set getActions() {
        return actions;
    }

    public void setActions(Set actions) {
        this.actions = actions;
    }

    /**
	 * ȡ��id
	 * 
	 * @return
	 */
    public String getId() {
        return id;
    }

    /**
	 * ����id
	 * 
	 * @param ������id
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * ȡ��modulename
	 * 
	 * @return
	 */
    public String getModulename() {
        return modulename;
    }

    /**
	 * ����modulename
	 * 
	 * @param ������modulename
	 */
    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    /**
	 * ȡ��upmodule
	 * 
	 * @return
	 */
    public String getUpmodule() {
        return upmodule;
    }

    /**
	 * ����upmodule
	 * 
	 * @param ������upmodule
	 */
    public void setUpmodule(String upmodule) {
        this.upmodule = upmodule;
    }

    /**
	 * ȡ��url
	 * 
	 * @return
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * ����url
	 * 
	 * @param ������url
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * ȡ��sortnum
	 * 
	 * @return
	 */
    public long getSortnum() {
        return sortnum;
    }

    /**
	 * ����sortnum
	 * 
	 * @param ������sortnum
	 */
    public void setSortnum(long sortnum) {
        this.sortnum = sortnum;
    }
}
