package net.asfun.jvalog.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.asfun.jvalog.common.InteractException;
import net.asfun.jvalog.common.Jdoer;
import net.asfun.jvalog.common.JdoerFactory;
import net.asfun.jvalog.entity.Label;
import net.asfun.jvalog.entity.Message;
import net.asfun.jvalog.entity.Paper;
import net.asfun.jvalog.entity.Relation;
import net.asfun.jvalog.resource.MessageDao;
import net.asfun.jvalog.resource.MeterDao;
import net.asfun.jvalog.util.Bridge;
import net.asfun.jvalog.util.HtmlUtil;

public class Post {

    protected Paper paper;

    private List<Relation> cats;

    private List<Relation> tgs;

    private List<Message> comms;

    public Post(Paper paper) {
        if (paper == null) {
            throw new InteractException("Post not found");
        }
        this.paper = paper;
    }

    public List<Comment> getComments() {
        setComments();
        List<Comment> list = new ArrayList<Comment>();
        for (Message msg : comms) {
            list.add(new Comment(msg));
        }
        return list;
    }

    private void setComments() {
        if (null == comms) {
            comms = (List<Message>) MessageDao.listPublished(paper.getKey());
        }
    }

    public int getCommentCount() {
        return MeterDao.getCount(paper.getKey());
    }

    public String getTitle() {
        return paper.getTitle();
    }

    public String getSlogan() {
        return paper.getSlogan();
    }

    public String getExcerpt() {
        return paper.getExcerpt();
    }

    public String getSource() {
        return paper.getSource();
    }

    public Long getId() {
        return paper.getKey().getId();
    }

    public String getLink() {
        return Site.getInstance().getUrl() + paper.getUri() + ".html";
    }

    public String getPreview() {
        String content = paper.getContent().getValue();
        if (content.length() > 400) {
            return HtmlUtil.preview(content, 400) + "<a href='" + getLink() + "'>More...</a>";
        } else {
            return content;
        }
    }

    public List<Tag> getTags() {
        setTags();
        List<Tag> list = new ArrayList<Tag>();
        for (Relation rel : tgs) {
            list.add(Tag.load(rel.getRight().getId()));
        }
        return list;
    }

    public String getTagNames() {
        setTags();
        StringBuffer buff = new StringBuffer();
        Jdoer<Label> jdl = JdoerFactory.jdoLabel;
        for (Relation rel : tgs) {
            Label label = jdl.load(rel.getRight());
            if (label != null) {
                buff.append(label.getName()).append(",");
            }
        }
        if (buff.length() > 0) {
            buff.deleteCharAt(buff.length() - 1);
        }
        return buff.toString();
    }

    private void setTags() {
        if (null == tgs) {
            tgs = Bridge.getRelationsByLeft(paper.getKey(), Bridge.POST_TAG);
        }
    }

    private void setCategories() {
        if (null == cats) {
            cats = Bridge.getRelationsByLeft(paper.getKey(), Bridge.POST_CATEGORY);
        }
    }

    public List<Category> getCategories() {
        setCategories();
        List<Category> list = new ArrayList<Category>();
        for (Relation rel : cats) {
            list.add(Category.load(rel.getRight().getId()));
        }
        return list;
    }

    public List<Long> getCategoryIds() {
        setCategories();
        List<Long> cids = new ArrayList<Long>();
        for (Relation rel : cats) {
            cids.add(rel.getRight().getId());
        }
        return cids;
    }

    public String getArticle() {
        return paper.getContent().getValue();
    }

    public Date getDate() {
        return paper.getDate();
    }

    public boolean isPublished() {
        return paper.isPublished();
    }

    protected static Post load(long id) {
        return new Post(JdoerFactory.jdoPaper.load(id));
    }
}
