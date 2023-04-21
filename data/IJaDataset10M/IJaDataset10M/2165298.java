package com.luzan.app.map.bean.user;

import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import com.luzan.app.map.bean.MapTrack;
import com.luzan.common.httprpc.annotation.HttpParameter;
import com.luzan.common.httprpc.annotation.HttpHiddenField;
import com.luzan.bean.User;
import com.luzan.bean.SemanticTag;
import java.util.Collection;

/**
 * UserMapTrack
 *
 * @author Alexander Bondar
 */
@Entity
@Proxy(lazy = false)
@Table(name = "map_track")
@SqlResultSetMapping(name = "RS.MapTrack", entities = { @EntityResult(entityClass = UserMapTrack.class, fields = { @FieldResult(name = "id", column = "id"), @FieldResult(name = "guid", column = "guid"), @FieldResult(name = "name", column = "name"), @FieldResult(name = "description", column = "description"), @FieldResult(name = "user", column = "user_id"), @FieldResult(name = "SWLat", column = "sw_lat"), @FieldResult(name = "SWLon", column = "sw_lon"), @FieldResult(name = "NELat", column = "ne_lat"), @FieldResult(name = "NELon", column = "ne_lon"), @FieldResult(name = "state", column = "state"), @FieldResult(name = "subState", column = "substate"), @FieldResult(name = "modified", column = "modified"), @FieldResult(name = "created", column = "created") }) })
public class UserMapTrack extends MapTrack {

    public enum State {

        UPLOAD, DELETE, PUBLIC
    }

    public enum SubState {

        INPROC, COMPLETE, BROKEN
    }

    protected User user;

    protected State state;

    protected SubState substate;

    protected Collection<UserMapTrackPoint> points;

    protected Collection<SemanticTag> subjects;

    public UserMapTrack() {
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "track", fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @LazyCollection(LazyCollectionOption.TRUE)
    @HttpHiddenField
    public Collection<UserMapTrackPoint> getPoints() {
        return points;
    }

    public void setPoints(Collection<UserMapTrackPoint> points) {
        this.points = points;
    }

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Column(name = "substate", nullable = false)
    @Enumerated(EnumType.STRING)
    @HttpParameter(name = "substate")
    public SubState getSubstate() {
        return substate;
    }

    public void setSubstate(SubState state) {
        this.substate = state;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @HttpHiddenField
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany
    @JoinTable(name = "map_tag", joinColumns = { @JoinColumn(name = "obj_track_id") }, inverseJoinColumns = { @JoinColumn(name = "sbj_tag_id") })
    @LazyCollection(LazyCollectionOption.TRUE)
    @HttpHiddenField
    public Collection<SemanticTag> getSubjects() {
        return subjects;
    }

    public void setSubjects(Collection<SemanticTag> subjects) {
        this.subjects = subjects;
    }
}
