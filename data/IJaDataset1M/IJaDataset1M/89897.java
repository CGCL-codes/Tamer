package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.ShippingDao;
import net.shopxx.entity.Shipping;
import net.shopxx.service.ShippingService;
import net.shopxx.util.SerialNumberUtil;
import org.springframework.stereotype.Service;

/**
 * Service实现类 - 发货
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXEACE05E3DC84AC6892E51AD68CBADA74
 * ============================================================================
 */
@Service
public class ShippingServiceImpl extends BaseServiceImpl<Shipping, String> implements ShippingService {

    @Resource
    private ShippingDao shippingDao;

    @Resource
    public void setBaseDao(ShippingDao shippingDao) {
        super.setBaseDao(shippingDao);
    }

    public String getLastShippingSn() {
        return shippingDao.getLastShippingSn();
    }

    @Override
    public String save(Shipping shipping) {
        shipping.setShippingSn(SerialNumberUtil.buildShippingSn());
        return super.save(shipping);
    }
}
