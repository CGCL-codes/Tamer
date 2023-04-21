package com.whale.entity;

import java.util.ArrayList;
import java.util.List;
import com.whale.util.image.BitmapContainer;

public class Stream {

    /** 主键 */
    public String id;

    /** 用户 */
    public User user;

    /** 用户ID */
    public String uid;

    /** 标签 */
    public String tags;

    /** 分享的内容 */
    public String content;

    /** 发布时间 */
    public long time;

    /** 其他信息，如图片 */
    public List<StreamPart> parts = new ArrayList<StreamPart>();

    /** 回复列表 */
    public List<Comment> comments;

    /** 查看类型 */
    public String type;

    public StreamExtra streamExtra;

    public Stream() {
    }

    public Stream(String id, String uid, String tags, String content) {
        super();
        this.id = id;
        this.uid = uid;
        this.tags = tags;
        this.content = content;
    }

    public Stream addStreamPart(StreamPart part) {
        parts.add(part);
        return this;
    }

    public BitmapContainer cache_avatar = new BitmapContainer();

    public BitmapContainer cache_image = new BitmapContainer();
}
