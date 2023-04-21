package com.sns.pojo;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Diary entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "diary", catalog = "sns")
public class Diary implements java.io.Serializable {

    private Integer diaryId;

    private String diaryUserName;

    private Integer diaryDkId;

    private String diaryTitle;

    private String diaryContext;

    private String diaryAuthority;

    private Timestamp diaryTime;

    /** default constructor */
    public Diary() {
    }

    /** full constructor */
    public Diary(String diaryUserName, String diaryKind, Integer diaryDkId, String diaryTitle, String diaryContext, String diaryAuthority, Timestamp diaryTime) {
        this.diaryUserName = diaryUserName;
        this.diaryDkId = diaryDkId;
        this.diaryTitle = diaryTitle;
        this.diaryContext = diaryContext;
        this.diaryAuthority = diaryAuthority;
        this.diaryTime = diaryTime;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "diary_id", unique = true, nullable = false)
    public Integer getDiaryId() {
        return this.diaryId;
    }

    public void setDiaryId(Integer diaryId) {
        this.diaryId = diaryId;
    }

    @Column(name = "diary_user_name", nullable = false, length = 30)
    public String getDiaryUserName() {
        return this.diaryUserName;
    }

    public void setDiaryUserName(String diaryUserName) {
        this.diaryUserName = diaryUserName;
    }

    @Column(name = "diary_dk_id", nullable = false)
    public Integer getDiaryDkId() {
        return this.diaryDkId;
    }

    public void setDiaryDkId(Integer diaryDkId) {
        this.diaryDkId = diaryDkId;
    }

    @Column(name = "diary_title", nullable = false, length = 30)
    public String getDiaryTitle() {
        return this.diaryTitle;
    }

    public void setDiaryTitle(String diaryTitle) {
        this.diaryTitle = diaryTitle;
    }

    @Column(name = "diary_context", nullable = false, length = 2000)
    public String getDiaryContext() {
        return this.diaryContext;
    }

    public void setDiaryContext(String diaryContext) {
        this.diaryContext = diaryContext;
    }

    @Column(name = "diary_authority", nullable = false, length = 30)
    public String getDiaryAuthority() {
        return this.diaryAuthority;
    }

    public void setDiaryAuthority(String diaryAuthority) {
        this.diaryAuthority = diaryAuthority;
    }

    @Column(name = "diary_time", nullable = false, length = 19)
    public Timestamp getDiaryTime() {
        return this.diaryTime;
    }

    public void setDiaryTime(Timestamp diaryTime) {
        this.diaryTime = diaryTime;
    }
}
