package com.redhat.gs.mrlogistics.seam.detail;

import javax.ejb.Local;

@Local
public interface DetailCompany {

    public void findDivisions();

    public void refreshDivisions();

    public void addDivision();

    public void save();

    public void setCid(Long cid);

    public Long getCid();

    public void create();

    public void destroy();
}
