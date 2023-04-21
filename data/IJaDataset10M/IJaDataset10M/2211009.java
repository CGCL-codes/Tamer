package coyousoft.jiuhuabook.entity;

import coyousoft.mvc.util.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * 竞拍活动包含的商品
 *
 * @author SCM
 *
 */
public class AuctionRelGoods {

    private Long auctionRelGoodsId;

    private Long auctionId;

    private Long goodsId;

    private Long occurNumber;

    public AuctionRelGoods() {
    }

    public AuctionRelGoods(Long auctionRelGoodsId) {
        this.auctionRelGoodsId = auctionRelGoodsId;
    }

    public void setAuctionRelGoodsId(Long auctionRelGoodsId) {
        this.auctionRelGoodsId = auctionRelGoodsId;
    }

    public Long getAuctionRelGoodsId() {
        return auctionRelGoodsId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setOccurNumber(Long occurNumber) {
        this.occurNumber = occurNumber;
    }

    public Long getOccurNumber() {
        return occurNumber;
    }

    public AuctionRelGoods fill(Map<String, Object> eachRow) {
        Object obj = null;
        if ((obj = eachRow.get("AUCTION_REL_GOODS_ID")) != null) {
            auctionRelGoodsId = ((Number) obj).longValue();
        }
        if ((obj = eachRow.get("AUCTION_ID")) != null) {
            auctionId = ((Number) obj).longValue();
        }
        if ((obj = eachRow.get("GOODS_ID")) != null) {
            goodsId = ((Number) obj).longValue();
        }
        if ((obj = eachRow.get("OCCUR_NUMBER")) != null) {
            occurNumber = ((Number) obj).longValue();
        }
        return this;
    }

    public AuctionRelGoods fill(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        Map<String, Object> map = new HashMap<String, Object>();
        Object obj = null;
        int sqlType = 0;
        for (int i = 1; i <= numberOfColumns; i++) {
            sqlType = rsmd.getColumnType(i);
            if (sqlType == java.sql.Types.DATE) {
                obj = rs.getTimestamp(i);
            } else if (sqlType == java.sql.Types.CLOB) {
                Clob clob = rs.getClob(i);
                obj = (clob != null ? clob.getSubString(1, (int) clob.length()) : null);
            } else {
                obj = rs.getObject(i);
            }
            map.put(rsmd.getColumnName(i).toUpperCase(), obj);
        }
        return fill(map);
    }

    public AuctionRelGoods fill(RequestWrapper wrapper) throws Exception {
        auctionRelGoodsId = wrapper.getLong("auctionRelGoodsId");
        auctionId = wrapper.getLong("auctionId");
        goodsId = wrapper.getLong("goodsId");
        occurNumber = wrapper.getLong("occurNumber");
        return this;
    }
}
