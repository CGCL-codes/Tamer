package net.shopxx.dao.impl;

import net.shopxx.dao.CartItemDao;
import net.shopxx.entity.CartItem;
import org.springframework.stereotype.Repository;

/**
 * Dao实现类 - 购物车项
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX1BCC0CDFEEE26A589F809A8B426E5595
 * ============================================================================
 */
@Repository
public class CartItemDaoImpl extends BaseDaoImpl<CartItem, String> implements CartItemDao {

    @Override
    public String save(CartItem cartItem) {
        String hql = "from CartItem cartItem where cartItem.member = ? and cartItem.product = ?";
        CartItem persistent = (CartItem) getSession().createQuery(hql).setParameter(0, cartItem.getMember()).setParameter(1, cartItem.getProduct()).uniqueResult();
        if (persistent == null) {
            return super.save(cartItem);
        } else {
            persistent.setQuantity(persistent.getQuantity() + cartItem.getQuantity());
            super.update(persistent);
            return persistent.getId();
        }
    }
}
