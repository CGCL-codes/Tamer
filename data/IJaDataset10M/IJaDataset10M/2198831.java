package com.sheng.dao;

import com.sheng.po.Addwuliao;

public interface InsertwuliaoDAO {

    public boolean addwuliao(Addwuliao aw);

    public int updatewuliao(Addwuliao aw);

    public int findpid(String id);
}
