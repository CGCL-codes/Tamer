package com.jiutian.hotel.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "hoteluniversity")
public class HotelUniversityData {

    private String hotelid;

    private String university;

    @Column(name = "hotelid")
    public String getHotelid() {
        return this.hotelid;
    }

    public void setHotelid(String hotelid) {
        this.hotelid = hotelid;
    }

    @Column(name = "university")
    public String getUniversity() {
        return this.university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
