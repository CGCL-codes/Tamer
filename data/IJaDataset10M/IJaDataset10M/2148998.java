package org.insia.teamexperts.dao.interfaces.product;

import java.util.List;
import org.insia.teamexperts.dao.interfaces.common.ICommonDAO;
import org.insia.teamexperts.model.product.Brand;

/**
 * Interface DAO pour la gestion des Marques
 * 
 * @author sok hout
 *
 */
public interface IBrandDAO extends ICommonDAO<Brand, Long> {

    public List<Brand> getBrandList(String keyword);
}
