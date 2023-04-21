package com.brekeke.hiway.ticket.dao;

import java.util.List;
import com.brekeke.hiway.ticket.dto.TticketStoreDto;
import com.brekeke.hiway.ticket.entity.TticketStore;

/**
 * 站级库存DAO
 * @author LEPING.LI
 * @version 1.0.0
 */
public interface TticketStoreDAO extends BaseDAO {

    /**
	 * 根据票据类型和站代码查询票据库存
	 * @param tsd DTO
	 * @return 查询结果主要包含票据结存数据
	 */
    public List<TticketStore> searchTticStoreByTypeTsCode(TticketStoreDto tsd);

    /**
     * 根据票据类型和站代码
     * 清除掉所有库存结存为0的记录
     * @param tsd
     * @return
     */
    public Integer deleteZeroTicketStore(TticketStoreDto tsd);
}
