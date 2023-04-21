package com.hk.bean.taobao;

import java.util.Date;
import com.hk.frame.dao.annotation.Column;
import com.hk.frame.dao.annotation.Id;
import com.hk.frame.dao.annotation.Table;

/**
 * 店铺
 * 
 * @author akwei
 */
@Table(name = "tb_shop")
public class Tb_Shop {

    public static String base_logo_url = "http://logo.taobao.com/shop-logo/";

    /**
	 * 火酷系统id
	 */
    @Id
    private long sid;

    /**
	 * 店铺分类id
	 */
    @Column
    private long cid;

    /**
	 * 淘宝店铺id
	 */
    @Column
    private long tb_sid;

    /**
	 * 店铺标题
	 */
    @Column
    private String title;

    /**
	 * 卖家昵称
	 */
    @Column
    private String nick;

    /**
	 * 店铺介绍
	 */
    @Column
    private String intro;

    /**
	 * 图片绝对地址
	 */
    @Column
    private String pic_path;

    /**
	 * 创建时间
	 */
    @Column
    private Date created;

    /**
	 * 最后修改时间
	 */
    @Column
    private Date modified;

    /**
	 * 点评数量
	 */
    @Column
    private int cmt_num;

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getTb_sid() {
        return tb_sid;
    }

    public void setTb_sid(long tbSid) {
        tb_sid = tbSid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String picPath) {
        pic_path = picPath;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public int getCmt_num() {
        return cmt_num;
    }

    public void setCmt_num(int cmtNum) {
        cmt_num = cmtNum;
    }

    public String getPic_url() {
        return base_logo_url + this.getPic_path();
    }
}
