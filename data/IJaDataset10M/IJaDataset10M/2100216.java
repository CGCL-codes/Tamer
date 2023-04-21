package br.com.javaplanet.dao.impl;

import br.com.javaplanet.dao.BaseDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import java.io.Serializable;

/**
 * User: Urubatan
 * Date: 10/10/2006
 * Time: 19:01:34
 */
public class HibernateBaseDaoImpl<T, K extends Serializable> extends HibernateDaoSupport implements BaseDao<T, K> {
}
