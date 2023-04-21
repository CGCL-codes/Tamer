package org.springcart.dao;

import java.util.List;
import org.springcart.model.entities.Review;

public interface IReviewDAO {

    public void save(Review review);

    public void delete(Review review);

    public void update(Review review);

    public List getReviewsByProduct(long productId);
}
