package net.itsite.pt;

import java.util.Date;
import net.a.ItSiteUtil;
import net.simpleframework.content.IContentBeanAware;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.bean.IOrderBeanAware;
import net.simpleframework.core.bean.IViewsBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.IUserBeanAware;
import net.simpleframework.util.IConstants;

public class PTBean extends AbstractIdDataObjectBean implements IContentBeanAware, IViewsBeanAware, IOrderBeanAware, IUserBeanAware {

    private ID catalogId;

    private ID userId;

    private String project;

    private String content;

    private EPTStatus status;

    private String technology;

    private EPTBudget budget;

    private EPTType ttype;

    private int cycle;

    private int period;

    private long attentions;

    private long remarks;

    private long views;

    private long bid;

    private long todayViews;

    private boolean ttop = false;

    private boolean daily;

    private long oorder;

    private Date publishDate;

    public PTBean() {
        this.publishDate = new Date();
    }

    public void setTtype(EPTType ttype) {
        this.ttype = ttype;
    }

    public EPTType getTtype() {
        return ttype == null ? EPTType.recommend_ : ttype;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public long getBid() {
        return bid;
    }

    public ID getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(ID catalogId) {
        this.catalogId = catalogId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public EPTStatus getStatus() {
        return status == null ? EPTStatus.edit : status;
    }

    public void setStatus(EPTStatus status) {
        this.status = status;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public EPTBudget getBudget() {
        return budget;
    }

    public void setBudget(EPTBudget budget) {
        this.budget = budget;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public long getAttentions() {
        return attentions;
    }

    public void setAttentions(long attentions) {
        this.attentions = attentions;
    }

    public long getRemarks() {
        return remarks;
    }

    public void setRemarks(long remarks) {
        this.remarks = remarks;
    }

    public long getTodayViews() {
        return todayViews;
    }

    public void setTodayViews(long todayViews) {
        this.todayViews = todayViews;
    }

    public boolean isTtop() {
        return ttop;
    }

    public void setTtop(boolean ttop) {
        this.ttop = ttop;
    }

    public boolean isDaily() {
        return daily;
    }

    public void setDaily(boolean daily) {
        this.daily = daily;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public ID getUserId() {
        return userId;
    }

    @Override
    public void setUserId(ID userId) {
        this.userId = userId;
    }

    @Override
    public String getUserText() {
        final IUser user = ItSiteUtil.getUserById(getUserId());
        return user != null ? user.getText() : IConstants.HTML_BLANK_STRING;
    }

    @Override
    public long getOorder() {
        return oorder;
    }

    @Override
    public void setOorder(long oorder) {
        this.oorder = oorder;
    }

    @Override
    public long getViews() {
        return views;
    }

    @Override
    public void setViews(long views) {
        this.views = views;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
