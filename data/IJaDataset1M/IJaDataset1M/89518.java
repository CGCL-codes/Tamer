package cn.myapps.core.homepage.dao;

import java.util.Collection;
import cn.myapps.base.dao.IDesignTimeDAO;

public interface HomePageDAO extends IDesignTimeDAO {

    public Collection findByApplication(String applicationId) throws Exception;
}
