package com.bhms.module.houses.receive.service;

import com.bhms.module.houses.receive.mapper.HouseMapper;

public class HouseServiceBean implements HouseService {

    private HouseMapper houseMapper;

    public void setHouseMapper(HouseMapper houseMapper) {
        this.houseMapper = houseMapper;
    }
}
