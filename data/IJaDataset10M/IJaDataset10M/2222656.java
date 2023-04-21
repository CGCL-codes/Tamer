package com.rabincorp.infole.domain.poll;

import com.rabincorp.infole.domain.user.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Isak Rabin
 */
@Entity(name = "poll_question")
public class PollQuestion implements Serializable {

    private static final long serialVersionUID = -1146807357574315751L;

    @Id
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    private User owner;

    @OneToMany
    private List<PollChoice> options = new ArrayList<PollChoice>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PollChoice> getOptions() {
        return options;
    }

    public void setOptions(List<PollChoice> options) {
        this.options = options;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
