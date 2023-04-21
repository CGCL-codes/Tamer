package jumpingnotes.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "group_member_confirm", catalog = "jumping_notes")
public class GroupMemberConfirm implements java.io.Serializable {

    private Integer groupMemberConfirmId;

    private Group group;

    private Member member;

    private Integer memberIdInvite;

    private String message;

    private Date createTime;

    public GroupMemberConfirm() {
    }

    public GroupMemberConfirm(Group group, Member member, Date createTime) {
        this.group = group;
        this.member = member;
        this.createTime = createTime;
    }

    public GroupMemberConfirm(Group group, Member member, Integer memberIdInvite, String message, Date createTime) {
        this.group = group;
        this.member = member;
        this.memberIdInvite = memberIdInvite;
        this.message = message;
        this.createTime = createTime;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "group_member_confirm_id", unique = true, nullable = false)
    public Integer getGroupMemberConfirmId() {
        return this.groupMemberConfirmId;
    }

    public void setGroupMemberConfirmId(Integer groupMemberConfirmId) {
        this.groupMemberConfirmId = groupMemberConfirmId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Column(name = "member_id_invite")
    public Integer getMemberIdInvite() {
        return this.memberIdInvite;
    }

    public void setMemberIdInvite(Integer memberIdInvite) {
        this.memberIdInvite = memberIdInvite;
    }

    @Column(name = "message", length = 65535)
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false, length = 19)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
