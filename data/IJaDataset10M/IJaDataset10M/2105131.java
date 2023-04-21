package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.MysqlConn;
import bean.Goods;

/**
 * @author Baicai
 * 
 *         从数据库获取主页显示数据的方法类
 */
public class IndexViewMethod {

    /**
	 * 从数据库获取最后的10条数据
	 * 
	 * @return List<Goods>
	 */
    public static List<Goods> getListGoods() {
        List<Goods> list = new ArrayList<Goods>();
        try {
            ResultSet resultSet;
            MysqlConn.setSqlString("select goods_id,goods_name,goods_sort,goods_price,goods_information " + "from goods where goods_state=1 order by goods_id desc limit 20");
            resultSet = MysqlConn.queryDB();
            while (resultSet.next()) {
                Goods goods = new Goods();
                goods.setGoodsId(Integer.parseInt(resultSet.getString("goods_id")));
                goods.setGoodsName(resultSet.getString("goods_name"));
                goods.setGoodsSort(resultSet.getString("goods_sort"));
                goods.setGoodsPrice(Float.valueOf(resultSet.getString("goods_price")));
                goods.setGoodsInformation(resultSet.getString("goods_information"));
                list.add(goods);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MysqlConn.free();
        }
        return null;
    }
}
