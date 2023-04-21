package cn.csust.net2.manager.shared.po;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import com.extjs.gxt.ui.client.data.BeanModelTag;
import cn.csust.net2.manager.shared.xml.XmlInfo;
import cn.csust.net2.manager.shared.xml.XmlResources;

/**
 * Vote entity. @author MyEclipse Persistence Tools
 */
public class Vote extends PO implements Serializable, BeanModelTag {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Long voteId;

    private Long pubUserId;

    private String title;

    private Timestamp startTime;

    private Timestamp endTime;

    private String remark;

    private Integer type;

    private Set<VoteAnswer> answerSet;

    /** default constructor */
    public Vote() {
    }

    public Vote(Long voteId, Long pubUserId, String title, Timestamp startTime, Timestamp endTime, String remark, Integer type, Set<VoteAnswer> answerSet, Set<Student> studentSet) {
        super();
        this.voteId = voteId;
        this.pubUserId = pubUserId;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remark = remark;
        this.type = type;
        this.answerSet = answerSet;
    }

    public Set<VoteAnswer> getAnswerSet() {
        return answerSet;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    public Timestamp getEndTime() {
        return this.endTime;
    }

    public Long getPubUserId() {
        return this.pubUserId;
    }

    public String getRemark() {
        return this.remark;
    }

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getType() {
        return type;
    }

    public Long getVoteId() {
        return this.voteId;
    }

    @Override
    public XmlInfo getXmlInfo(XmlResources instance) {
        XmlInfo xmlInfo = new XmlInfo();
        xmlInfo.setTextResource(instance.vote());
        return xmlInfo;
    }

    public void setAnswerSet(Set<VoteAnswer> answerSet) {
        HashSet<VoteAnswer> answers = new HashSet<VoteAnswer>();
        for (VoteAnswer an : answerSet) {
            answers.add(an);
        }
        this.answerSet = answers;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setPubUserId(Long pubUserId) {
        this.pubUserId = pubUserId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }
}
