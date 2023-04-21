package com.faithbj.shop.dao.impl;

import org.springframework.stereotype.Repository;
import com.faithbj.shop.dao.FooterDao;
import com.faithbj.shop.model.entity.Footer;

/**
 * Dao实现类 - 网页底部信息
 * <p>Copyright: Copyright (c) 2011</p> 
 * 
 * <p>Company: www.faithbj.com</p>
 * 
 * @author 	faithbj
 * @date 	2011-12-16
 * @version 1.0
 */
@Repository
public class FooterDaoImpl extends BaseDaoImpl<Footer, String> implements FooterDao {

    public Footer getFooter() {
        return load(Footer.FOOTER_ID);
    }
}
