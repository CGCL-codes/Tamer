package net.itsite.question.remark;

import net.simpleframework.content.AbstractContent;
import net.simpleframework.content.IContentBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.HTMLUtils;

public class QuestionRemarkItem extends AbstractContent implements ITreeBeanAware, IContentBeanAware {

    private static final long serialVersionUID = -8721255256891715140L;

    private ID parentId;

    private ID documentId;

    private String ip;

    private String content;

    private int support, opposition;

    private boolean goodAnswer;

    public boolean isGoodAnswer() {
        return goodAnswer;
    }

    public void setGoodAnswer(boolean goodAnswer) {
        this.goodAnswer = goodAnswer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = HTMLUtils.stripScripts(content);
    }

    @Override
    public ID getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(final ID parentId) {
        this.parentId = parentId;
    }

    public ID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final ID documentId) {
        this.documentId = documentId;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(final int support) {
        this.support = support;
    }

    public int getOpposition() {
        return opposition;
    }

    public void setOpposition(final int opposition) {
        this.opposition = opposition;
    }

    @Override
    public QuestionRemarkItem parent() {
        return null;
    }
}
