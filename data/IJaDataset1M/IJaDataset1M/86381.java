package ru.ssau.university.persistence.entity;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "A_GROUP")
public class Group extends CommonEntity {

    private Speciality speciality;

    private Integer firstYear;

    private String letter;

    @ManyToOne
    @JoinColumn(name = "SPECIALITY_ID")
    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    @Column(name = "FIRST_YEAR")
    public Integer getFirstYear() {
        return firstYear;
    }

    public void setFirstYear(Integer firstYear) {
        this.firstYear = firstYear;
    }

    @Column(name = "LETTER")
    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    @Transient
    public String getTitle() {
        String title = speciality.getFaculty().getId() + getGrade() + letter;
        return title;
    }

    @Transient
    private String getGrade() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int diff = currentYear - firstYear;
        if (currentMonth > 8) {
            diff++;
        }
        return String.valueOf(diff);
    }

    @Transient
    public int getTerm() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int diff = currentYear - firstYear;
        int term = diff * 2;
        if (currentMonth > 8) {
            term++;
        }
        return term;
    }
}
