package org.jmesa.core.sort;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.jmesa.core.Name;
import org.jmesa.core.President;
import org.jmesa.core.PresidentDao;
import org.jmesa.limit.Limit;
import org.jmesa.limit.LimitFactory;
import org.jmesa.limit.Order;
import org.jmesa.test.AbstractTestCase;
import org.jmesa.test.ParametersAdapter;
import org.jmesa.test.ParametersBuilder;
import org.jmesa.web.WebContext;
import org.junit.Test;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class ColumnSortTest extends AbstractTestCase {

    @Test
    public void sortItems() {
        WebContext webContext = createWebContext();
        HashMap<String, Object> results = new HashMap<String, Object>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("name.firstName", Order.ASC);
        builder.addSort("name.lastName", Order.DESC);
        webContext.setParameterMap(results);
        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();
        MultiColumnSort itemsSort = new MultiColumnSort();
        Collection<?> items = PresidentDao.getPresidents();
        items = itemsSort.sortItems(items, limit);
        assertNotNull(items);
        Iterator<?> iterator = items.iterator();
        President first = (President) iterator.next();
        assertTrue("the first sort order is wrong", first.getName().getFirstName().equals("Abraham"));
        President second = (President) iterator.next();
        assertTrue("the second sort order is wrong", second.getName().getLastName().equals("Johnson"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sortNullComposedItems() {
        WebContext webContext = createWebContext();
        HashMap<String, Object> results = new HashMap<String, Object>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("name.firstName", Order.ASC);
        builder.addSort("name.lastName", Order.DESC);
        webContext.setParameterMap(results);
        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();
        MultiColumnSort itemsSort = new MultiColumnSort();
        Collection items = new ArrayList<President>();
        President president = new President();
        Name name = new Name("James", "Monroe");
        president.setName(name);
        items.add(president);
        president = new President();
        name = new Name(null, "Washington");
        president.setName(name);
        items.add(president);
        president = new President();
        name = new Name("James", "Madison");
        president.setName(name);
        items.add(president);
        items = itemsSort.sortItems(items, limit);
        assertNotNull(items);
        Iterator iterator = items.iterator();
        President first = (President) iterator.next();
        assertTrue("the first sort order is wrong", first.getName().getFirstName().equals("James"));
        President second = (President) iterator.next();
        assertTrue("the second sort order is wrong", second.getName().getLastName().equals("Madison"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sortEmptyComposedItems() {
        WebContext webContext = createWebContext();
        HashMap<String, Object> results = new HashMap<String, Object>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("name.firstName", Order.ASC);
        builder.addSort("name.lastName", Order.DESC);
        webContext.setParameterMap(results);
        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();
        MultiColumnSort itemsSort = new MultiColumnSort();
        Collection items = new ArrayList<President>();
        President president = new President();
        Name name = new Name("James", "Monroe");
        president.setName(name);
        items.add(president);
        president = new President();
        name = new Name("", "Washington");
        president.setName(name);
        items.add(president);
        president = new President();
        name = new Name("James", "Madison");
        president.setName(name);
        items.add(president);
        items = itemsSort.sortItems(items, limit);
        assertNotNull(items);
        Iterator iterator = items.iterator();
        President first = (President) iterator.next();
        assertTrue("the first sort order is wrong", first.getName().getFirstName().equals(""));
        President second = (President) iterator.next();
        assertTrue("the second sort order is wrong", second.getName().getLastName().equals("Monroe"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sortNullItems() {
        WebContext webContext = createWebContext();
        HashMap<String, Object> results = new HashMap<String, Object>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("born", Order.ASC);
        webContext.setParameterMap(results);
        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();
        MultiColumnSort itemsSort = new MultiColumnSort();
        Collection items = new ArrayList<President>();
        President president = new President();
        Name name = new Name("Thomas", "Jefferson");
        president.setName(name);
        president.setBorn(PresidentDao.getDate("04/13/1743"));
        items.add(president);
        president = new President();
        name = new Name("John", "Adams");
        president.setName(name);
        president.setBorn(null);
        items.add(president);
        president = new President();
        name = new Name("George", "Washington");
        president.setName(name);
        president.setBorn(PresidentDao.getDate("02/22/1732"));
        items.add(president);
        items = itemsSort.sortItems(items, limit);
        assertNotNull(items);
        Iterator iterator = items.iterator();
        President first = (President) iterator.next();
        assertTrue("the first sort order is wrong", first.getName().getFirstName().equals("George"));
        President second = (President) iterator.next();
        assertTrue("the second sort order is wrong", second.getName().getLastName().equals("Jefferson"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sortEmptyItems() {
        WebContext webContext = createWebContext();
        HashMap<String, Object> results = new HashMap<String, Object>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("term", Order.ASC);
        webContext.setParameterMap(results);
        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();
        MultiColumnSort itemsSort = new MultiColumnSort();
        Collection items = new ArrayList<President>();
        President president = new President();
        president.setTerm("");
        items.add(president);
        president = new President();
        president.setTerm("1801-09");
        items.add(president);
        president = new President();
        president.setTerm("1797-1801");
        items.add(president);
        items = itemsSort.sortItems(items, limit);
        assertNotNull(items);
        Iterator iterator = items.iterator();
        President first = (President) iterator.next();
        assertTrue("the first sort order is wrong", first.getTerm().equals(""));
        President second = (President) iterator.next();
        assertTrue("the second sort order is wrong", second.getTerm().equals("1797-1801"));
    }
}
