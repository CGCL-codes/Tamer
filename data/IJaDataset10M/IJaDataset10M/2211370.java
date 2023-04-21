package cn.myapps.core.gismap.dao;

import java.util.Collection;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.gismap.ejb.MapSegmentVO;

public interface MapSegmentDAO extends IRuntimeDAO {

    public Collection<MapSegmentVO> queryAll() throws Exception;
}
