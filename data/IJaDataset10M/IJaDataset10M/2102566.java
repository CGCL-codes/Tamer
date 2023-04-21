package com.tomgibara.cluster.gvm.demo.city;

import com.tomgibara.cluster.gvm.GvmSimpleKeyer;

public class SingleCityKeyer extends GvmSimpleKeyer<City> {

    @Override
    protected City combineKeys(City city1, City city2) {
        return city1.pop < city2.pop ? city2 : city1;
    }
}
