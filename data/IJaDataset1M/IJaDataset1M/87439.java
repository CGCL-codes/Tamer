package com.nccsjz.pojo;

import java.util.Date;

/**
 * 新闻
 */
public class News implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6L;

    private Long newsid;

    private String newstitle;

    private String newscontent;

    private Date newsdate;

    /** default constructor */
    public News() {
    }

    /** full constructor */
    public News(String newstitle, String newscontent, Date newsdate) {
        this.newstitle = newstitle;
        this.newscontent = newscontent;
        this.newsdate = newsdate;
    }

    /**
	 * 重载构造方法
	 * 
	 * @param newsid
	 * @param newstitle
	 * @param newscontent
	 * @param newsdate
	 */
    public News(Long newsid, String newstitle, String newscontent, Date newsdate) {
        super();
        this.newsid = newsid;
        this.newstitle = newstitle;
        this.newscontent = newscontent;
        this.newsdate = newsdate;
    }

    /**
	 * 重载构造方法
	 * 
	 * @param newsid
	 * @param newstitle
	 * @param newsdate
	 */
    public News(Long newsid, String newstitle, Date newsdate) {
        super();
        this.newsid = newsid;
        this.newstitle = newstitle;
        this.newsdate = newsdate;
    }

    public Long getNewsid() {
        return this.newsid;
    }

    public void setNewsid(Long newsid) {
        this.newsid = newsid;
    }

    public String getNewstitle() {
        return this.newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle;
    }

    public String getNewscontent() {
        return this.newscontent;
    }

    public void setNewscontent(String newscontent) {
        this.newscontent = newscontent;
    }

    public Date getNewsdate() {
        return this.newsdate;
    }

    public void setNewsdate(Date newsdate) {
        this.newsdate = newsdate;
    }
}
