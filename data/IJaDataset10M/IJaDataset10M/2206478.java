package com.faceye.components.blog.dao.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.faceye.core.dao.hibernate.model.BaseObject;
import com.faceye.core.service.security.model.User;

/**
 * 
 * @author：宋海鹏
 * @Connection:E_mail:ecsun@sohu.com/myecsun@hotmail.com QQ:82676683
 * @Copy Right:www.faceye.com
 * @System:www.faceye.com网络支持系统
 * @Create Time:2007-9-22
 * @Package com.faceye.components.blog.dao.model.ArticleClickCount.java
 * @Description:文章点击量统计。点击历史。
 */
public class ArticleClickCount extends BaseObject {

    private Article article;

    private User user;

    private Date createTime = new Date();

    private String ip;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map map() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Map map = new HashMap();
        map.put("id", this.getId());
        map.put("name", this.getName());
        map.put("createTime", dateFormat.format(this.getCreateTime()));
        map.put("userId", this.getUser().getId());
        map.put("username", this.getUser().getUsername());
        map.put("ip", this.getIp());
        map.put("articleId", this.getArticle().getId());
        return map;
    }
}
