package fashionstore.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import fashionstore.domain.Human;

public interface HumanDao {

    public int insert(Human h, byte[][] filesBytes, String[] picNames) throws SQLException, IOException;

    public int deleteById(String id) throws SQLException;

    public int queryById(String id, Human h) throws SQLException;

    public int queryByDealerId(Long dealerId, List<Human> humanList) throws SQLException;

    public int updateById(Human h, byte[][] filesBytes, String[] picNames) throws SQLException, IOException;

    public int updateById(Human h) throws SQLException;

    public int removeImage(Human account, String picName) throws SQLException, IOException;

    public int search(String keywords, List<Human> humanList) throws SQLException;
}
