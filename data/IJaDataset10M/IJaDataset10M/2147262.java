package org.sample.category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author hendro
 */
public class CategoryDAOImpl implements CategoryDAO {

    public CategoryDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Category> list(String key) throws Exception {
        String sql = "SELECT * FROM dbproduct.category WHERE name LIKE ?";
        List<Category> categories = new ArrayList<Category>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql, new Object[] { "%" + key + "%" });
        for (Map m : rows) {
            Category category = new Category();
            category.setId(Integer.parseInt(m.get("id").toString()));
            category.setName(m.get("name").toString());
            categories.add(category);
        }
        return categories;
    }

    public Category select(int id) throws Exception {
        Category category = null;
        String sql = "SELECT * FROM dbproduct.category WHERE id=?";
        category = (Category) ClassConnection.getJdbc().queryForObject(sql, new Object[] { id }, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Category(rs.getInt("id"), rs.getString("name"));
            }
        });
        return category;
    }
}
