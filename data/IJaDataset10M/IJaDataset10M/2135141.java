package com.powerhua.fms.dao;

import java.util.List;

/**
 *
 * @author Administrator
 */
public interface IFileDao {

    public List getFiles(String sort, int start, int count);
}
