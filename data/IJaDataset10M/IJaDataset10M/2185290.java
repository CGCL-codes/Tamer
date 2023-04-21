package com.clican.pluto.cms.dao;

import com.clican.pluto.orm.dynamic.inter.IDirectory;

public interface DirectoryDao extends Dao {

    public IDirectory load(String path);

    public IDirectory load(Long id);

    public Object[] getDirectoryModelCount(Long id);
}
