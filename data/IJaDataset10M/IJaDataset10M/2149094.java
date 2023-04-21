package com.kosongkosong.model;

import com.google.appengine.api.datastore.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlRootElement;
import org.joda.time.DateTime;

/**
 *
 * @author ifnu
 */
@PersistenceCapable
@XmlRootElement(name = "match")
public class Match {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String id;

    @Persistent
    private String group;

    @Persistent
    private String teamA;

    @Persistent
    private String teamB;

    /**
     * schedule in GMT
     */
    @Persistent
    private Date date;

    @Persistent
    private String score;

    @Persistent
    private String stage;

    @Persistent
    private String stadium;

    private int gmt;

    public int getGmt() {
        return gmt;
    }

    public void setGmt(int gmt) {
        this.gmt = gmt;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateString(String s) {
    }

    public String getDateString() {
        DateTime dateTime = new DateTime(date.getTime());
        dateTime.plusHours(gmt);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dateTime.toDate());
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
