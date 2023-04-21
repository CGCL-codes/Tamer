package com.student.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Area entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "Area", schema = "dbo", catalog = "StudentManager")
public class Area implements java.io.Serializable {

    private Long areaId;

    private Area area;

    private String areaName;

    private String level;

    @Column(name = "Level")
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private String areaSimpleName;

    private Set<EmployCompany> employCompanies = new HashSet<EmployCompany>(0);

    private Set<Area> areas = new HashSet<Area>(0);

    private Set<Student> students = new HashSet<Student>(0);

    /** default constructor */
    public Area() {
    }

    /** minimal constructor */
    public Area(Long areaId) {
        this.areaId = areaId;
    }

    /** full constructor */
    public Area(Long areaId, Area area, String areaName, String areaSimpleName, Set<EmployCompany> employCompanies, Set<Area> areas, Set<Student> students) {
        this.areaId = areaId;
        this.area = area;
        this.areaName = areaName;
        this.areaSimpleName = areaSimpleName;
        this.employCompanies = employCompanies;
        this.areas = areas;
        this.students = students;
    }

    @Id
    @Column(name = "Area_id", unique = true, nullable = false, precision = 18, scale = 0)
    @GeneratedValue
    public Long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Are_Area_id")
    public Area getArea() {
        return this.area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Column(name = "Area_Name", length = 50)
    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Column(name = "Area_Simple_Name", length = 50)
    public String getAreaSimpleName() {
        return this.areaSimpleName;
    }

    public void setAreaSimpleName(String areaSimpleName) {
        this.areaSimpleName = areaSimpleName;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "area")
    public Set<EmployCompany> getEmployCompanies() {
        return this.employCompanies;
    }

    public void setEmployCompanies(Set<EmployCompany> employCompanies) {
        this.employCompanies = employCompanies;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "area")
    public Set<Area> getAreas() {
        return this.areas;
    }

    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "area")
    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
