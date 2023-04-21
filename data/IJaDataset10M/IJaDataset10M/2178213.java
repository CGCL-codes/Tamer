package com.mobfee.business.dao;

import java.util.List;
import com.mobfee.domain.BrandModelsList;
import com.mobfee.domain.Platform;

public interface IPlatformDao {

    public List<Platform> getBrandModels(String brandName);

    public List<String> getBrands();

    public List<Platform> getAllModel();

    public void addPlatform(String brandName, String seriesName, String model);

    public BrandModelsList getBrandModelsList(long gameId);

    public String getDownLoadAddress(long gameId, int platformId);

    public void addDownLoadAddress(long gameId, int platformId, String location);
}
