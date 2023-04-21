package com.javaexp.hib.storeproc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cityId")
    String cityId;

    @Type(type = "com.javaexp.hib.usertype.CityName")
    @Column(name = "cityName")
    String cityName;

    @ManyToOne
    @JoinColumn(name = "coruntyid_ref")
    Country country;
}
