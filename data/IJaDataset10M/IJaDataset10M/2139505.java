package org.dreamfly.netshop.manage;

import java.util.List;
import org.dreamfly.netshop.dao.BuyInfoDao;
import org.dreamfly.netshop.entity.BuyInfo;
import org.dreamfly.netshop.entity.OrderInfo;
import org.dreamfly.netshop.entity.ShopCart;
import org.dreamfly.netshop.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.EntityManager;
import org.springside.modules.security.springsecurity.SpringSecurityUtils;

@Service
@Transactional
public class BuyInfoManager extends EntityManager<BuyInfo, Long> {

    @Autowired
    private BuyInfoDao buyInfoDao;

    /**
     * 实现回调函数,为EntityManager基类的CRUD操作提供DAO.
     */
    @Override
    protected BuyInfoDao getEntityDao() {
        return buyInfoDao;
    }

    /**
     * 重载delte函数,演示异常处理及用户行为日志.
     */
    @Override
    public void delete(Long id) {
        if (id == 1) {
            logger.warn("操作员{}尝试删除超级管理员用户", SpringSecurityUtils.getCurrentUserName());
            throw new ServiceException("不能删除超级管理员用户");
        }
        buyInfoDao.delete(id);
    }

    /**
     * 检查用户名是否唯一.
     * 
     * @return loginName在数据库中唯一或等于orgLoginName时返回true.
     */
    @Transactional(readOnly = true)
    public boolean isLoginNameUnique(String loginName, String orgLoginName) {
        return buyInfoDao.isPropertyUnique("loginName", loginName, orgLoginName);
    }

    public void saveAllShopCart(List<ShopCart> list, OrderInfo orderInfo) {
        BuyInfo buyInfo;
        for (ShopCart o : list) {
            buyInfo = new BuyInfo();
            buyInfo.setGoodsInfo(o.getGoodsInfo());
            buyInfo.setMember(o.getMember());
            buyInfo.setNum(o.getNum());
            buyInfo.setOrderInfo(orderInfo);
            buyInfo.setSumPrice(o.getSumPrice());
            save(buyInfo);
        }
    }
}
