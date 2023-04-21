package net.simpleframework.my.space;

import java.util.Date;
import net.simpleframework.content.IContentBeanAware;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SapceRemarkBean extends AbstractIdDataObjectBean implements IContentBeanAware {

    private ID logId;

    private String content;

    private Date createDate;

    private ID userId;

    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    public ID getLogId() {
        return logId;
    }

    public void setLogId(final ID logId) {
        this.logId = logId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    public ID getUserId() {
        return userId;
    }

    public void setUserId(final ID userId) {
        this.userId = userId;
    }

    private static final long serialVersionUID = 7222328545443694691L;
}
