package coyousoft.jiuhuabook.dao;

import java.sql.SQLException;
import java.util.List;
import coyousoft.jiuhuabook.entity.GoodsRootType;
import coyousoft.jiuhuabook.entity.Pagination;

public interface GoodsRootTypeDao {

    public GoodsRootType selectById(Long gRootTypeId) throws SQLException;

    public Pagination<GoodsRootType> selectByPagination(Long isPublic, Long isDel, int pageNum, int pageSize) throws SQLException;

    public List<GoodsRootType> selectAll() throws SQLException;

    public boolean delete(Long[] gRootTypeId) throws SQLException;

    public boolean recycle(Long[] gRootTypeId) throws SQLException;

    public boolean queue(Long[] gRootTypeId) throws SQLException;

    public boolean update(GoodsRootType gRootType) throws SQLException;

    public boolean addNew(GoodsRootType gRootType) throws SQLException;
}
