package com.eshop.dao;

import java.sql.*;
import java.util.Vector;
import com.core.util.BaseDAO;
import com.util.vo.IConnectionPropertiesVO;
import com.eshop.vo.*;

public class ItemViewDAO extends BaseDAO {

    private String sql = "select * from eshop.item_view";

    public ItemViewDAO(IConnectionPropertiesVO cp) {
        super(cp);
    }

    public ItemViewVO[] getData() throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ItemViewVO[] itemviewvo = fill(rs);
                return itemviewvo;
            } finally {
                release(conn);
            }
        }
        return null;
    }

    private ItemViewVO[] fill(ResultSet rs) throws SQLException {
        if (rs != null) {
            Vector<ItemViewVO> data = new Vector<ItemViewVO>();
            while (rs.next()) {
                ItemViewVO vo = new ItemViewVO();
                vo.setId(rs.getLong("id"));
                vo.setCodeAn(rs.getString("code_an"));
                vo.setItemNumber(rs.getLong("item_number"));
                vo.setSupplierID(rs.getLong("supplier_id"));
                vo.setName(rs.getString("name"));
                vo.setTypeNum(rs.getLong("type_num"));
                vo.setType(rs.getString("type"));
                vo.setGuarantee(rs.getLong("guarantee"));
                vo.setAvailability(rs.getString("availability"));
                vo.setCount(rs.getLong("count"));
                vo.setDescription(rs.getString("description"));
                vo.setRemarks(rs.getString("remarks"));
                vo.setPrice(rs.getBigDecimal("price"));
                vo.setDiscount(rs.getBigDecimal("discount"));
                vo.setDiscountPercent(rs.getDouble("discount_percent"));
                vo.setVatID(rs.getLong("vat_id"));
                vo.setPictureUrl(rs.getString("picture_url"));
                vo.setModified(rs.getTimestamp("modified"));
                vo.setValidFrom(rs.getDate("valid_from"));
                vo.setValidTo(rs.getDate("valid_to"));
                data.add(vo);
            }
            ItemViewVO[] array = new ItemViewVO[data.size()];
            data.toArray(array);
            return array;
        }
        return null;
    }

    public void setData(ItemViewVO[] itemviewvo) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("insert into eshop.item_view (code_an,item_number,supplier_id,name,type_num,type,guarantee,availability,count,description,remarks,price,discount,discount_percent,vat_id,picture_url,modified,valid_from,valid_to) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                for (int i = 0; i < itemviewvo.length; i++) {
                    stmt.setString(1, itemviewvo[i].getCodeAn());
                    stmt.setLong(2, itemviewvo[i].getItemNumber());
                    stmt.setLong(3, itemviewvo[i].getSupplierID());
                    stmt.setString(4, itemviewvo[i].getName());
                    stmt.setLong(5, itemviewvo[i].getTypeNum());
                    stmt.setString(6, itemviewvo[i].getType());
                    stmt.setLong(7, itemviewvo[i].getGuarantee());
                    stmt.setString(8, itemviewvo[i].getAvailability());
                    stmt.setLong(9, itemviewvo[i].getCount());
                    stmt.setString(10, itemviewvo[i].getDescription());
                    stmt.setString(11, itemviewvo[i].getRemarks());
                    stmt.setBigDecimal(12, itemviewvo[i].getPrice());
                    stmt.setBigDecimal(13, itemviewvo[i].getDiscount());
                    stmt.setDouble(14, itemviewvo[i].getDiscountPercent());
                    stmt.setLong(15, itemviewvo[i].getVatID());
                    stmt.setString(16, itemviewvo[i].getPictureUrl());
                    stmt.setTimestamp(17, itemviewvo[i].getModified());
                    stmt.setDate(18, itemviewvo[i].getValidFrom());
                    stmt.setDate(19, itemviewvo[i].getValidTo());
                    stmt.executeUpdate();
                }
            } finally {
                release(conn);
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
