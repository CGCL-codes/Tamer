package org.azrul.epice.jpa.daoimpl.queryimpl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.azrul.epice.domain.Item;

/**
 *
 * @author azrulhasni
 */
public interface JPAItemsFilter {

    Predicate filter(String user, CriteriaBuilder cb, Root<Item> ritem);

    boolean filter(Item item, String user);

    String getType();
}
