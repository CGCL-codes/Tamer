package com.bhms.module.resources.village.mapper;

public class Cycle {

    private Integer cycle_id;

    private Integer city_id;

    private String cycle_name;

    public Integer getCycle_id() {
        return cycle_id;
    }

    public void setCycle_id(Integer cycle_id) {
        this.cycle_id = cycle_id;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public String getCycle_name() {
        return cycle_name;
    }

    public void setCycle_name(String cycle_name) {
        this.cycle_name = cycle_name == null ? null : cycle_name.trim();
    }
}
