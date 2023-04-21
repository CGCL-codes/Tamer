package org.datascooter.test.zero;

import org.datascooter.inface.ILongData;

public class Manufacturer implements ILongData {

    private Long id;

    private String name;

    private Country country;

    private Integer workersCount;

    public Manufacturer() {
    }

    public Manufacturer(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public Manufacturer(String name, Country country, Integer workersCount) {
        this.name = name;
        this.country = country;
        this.workersCount = workersCount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Integer getWorkersCount() {
        return workersCount;
    }

    public void setWorkersCount(Integer workersCount) {
        this.workersCount = workersCount;
    }
}
