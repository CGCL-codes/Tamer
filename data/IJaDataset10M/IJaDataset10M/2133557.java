package com.autoescola.core.entity.questionary;

import com.autoescola.core.entity.Entitie;
import com.autoescola.core.entity.security.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leonardo luz fernandes
 * @since 01/11/2010
 * @version 0.1
 */
@XmlRootElement(name = "UserTest")
@Entitie
@Entity
@Table(name = "user_test")
public class UserTest implements java.io.Serializable {

    private Long id;

    private User user;

    private boolean finished;

    private List<UserAnswer> userAnwsers;

    private Date startAt;

    private Date finishAt;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finish_at", nullable = true)
    public Date getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_at", nullable = false)
    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(targetEntity = UserAnswer.class)
    @JoinColumn(name = "id", referencedColumnName = "user_test_id")
    public List<UserAnswer> getUserAnwsers() {
        return userAnwsers;
    }

    public void setUserAnwsers(List<UserAnswer> userAnwsers) {
        this.userAnwsers = userAnwsers;
    }
}
