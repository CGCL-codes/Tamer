package br.com.caelum.recorder.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.repositorybuilder.builder.hibernate.HibernateRepositoryBuilder;
import br.com.caelum.recorder.modelo.Restaurant;
import br.com.caelum.recorder.modelo.RestaurantRepository;

/**
 * @author leonardobessa
 * 
 */
public class HibernateRepositoryBuilderTest {

    @Test
    public void listAllEntities() {
        Mockery mockery = new Mockery();
        final Session session = mockery.mock(Session.class);
        final List<Restaurant> expectedResult = new ArrayList<Restaurant>();
        final Criteria criteria = mockery.mock(Criteria.class);
        mockery.checking(new Expectations() {

            {
                one(session).createCriteria(with(equal(Restaurant.class)));
                will(returnValue(criteria));
                one(criteria).list();
                will(returnValue(expectedResult));
            }
        });
        HibernateRepositoryBuilder recorder = new HibernateRepositoryBuilder(session);
        RestaurantRepository repository = recorder.getRepository(RestaurantRepository.class);
        List<Restaurant> list = repository.findAll();
        Assert.assertEquals(expectedResult, list);
        mockery.assertIsSatisfied();
    }

    @Test
    public void listEntityWithOneAttribute() {
        Mockery mockery = new Mockery();
        final Session session = mockery.mock(Session.class);
        final List<Restaurant> expectedResult = new ArrayList<Restaurant>();
        final Criteria criteria = mockery.mock(Criteria.class);
        mockery.checking(new Expectations() {

            {
                one(session).createCriteria(with(equal(Restaurant.class)));
                will(returnValue(criteria));
                one(criteria).add(with(new SimpleExpressionMatcher(Restrictions.eq("name", "Fasano"))));
                will(returnValue(criteria));
                one(criteria).list();
                will(returnValue(expectedResult));
            }
        });
        HibernateRepositoryBuilder recorder = new HibernateRepositoryBuilder(session);
        RestaurantRepository repository = recorder.getRepository(RestaurantRepository.class);
        List<Restaurant> list = repository.findAllByName("Fasano");
        Assert.assertEquals(expectedResult, list);
        mockery.assertIsSatisfied();
    }

    @Test
    public void shouldListEntityByTwoAttributes() {
        Mockery mockery = new Mockery();
        final Session session = mockery.mock(Session.class);
        final List<Restaurant> expectedResult = new ArrayList<Restaurant>();
        final Criteria criteria = mockery.mock(Criteria.class);
        mockery.checking(new Expectations() {

            {
                one(session).createCriteria(with(equal(Restaurant.class)));
                will(returnValue(criteria));
                one(criteria).add(with(new SimpleExpressionMatcher(Restrictions.eq("name", "Fasano"))));
                will(returnValue(criteria));
                one(criteria).add(with(new SimpleExpressionMatcher(Restrictions.eq("speciality", "Italiana"))));
                will(returnValue(criteria));
                one(criteria).list();
                will(returnValue(expectedResult));
            }
        });
        HibernateRepositoryBuilder recorder = new HibernateRepositoryBuilder(session);
        RestaurantRepository repository = recorder.getRepository(RestaurantRepository.class);
        List<Restaurant> list = repository.findAllByNameAndSpeciality("Fasano", "Italiana");
        Assert.assertEquals(expectedResult, list);
        mockery.assertIsSatisfied();
    }

    @Test
    public void shouldFindEntityById() {
        Mockery mockery = new Mockery();
        final Session session = mockery.mock(Session.class);
        final Restaurant expectedResult = new Restaurant();
        final Serializable id = mockery.mock(Serializable.class);
        mockery.checking(new Expectations() {

            {
                one(session).load(with(equal(Restaurant.class)), with(equal(id)));
                will(returnValue(expectedResult));
            }
        });
        HibernateRepositoryBuilder recorder = new HibernateRepositoryBuilder(session);
        RestaurantRepository repository = recorder.getRepository(RestaurantRepository.class);
        Restaurant restaurante = repository.find(id);
        Assert.assertEquals(expectedResult, restaurante);
        mockery.assertIsSatisfied();
    }

    @Test
    public void shouldFindEntityByName() {
        Mockery mockery = new Mockery();
        final Session session = mockery.mock(Session.class);
        final Restaurant expectedResult = new Restaurant();
        final Criteria criteria = mockery.mock(Criteria.class);
        mockery.checking(new Expectations() {

            {
                one(session).createCriteria(with(equal(Restaurant.class)));
                will(returnValue(criteria));
                one(criteria).add(with(new SimpleExpressionMatcher(Restrictions.eq("name", "Nakombi"))));
                will(returnValue(criteria));
                one(criteria).uniqueResult();
                will(returnValue(expectedResult));
            }
        });
        HibernateRepositoryBuilder recorder = new HibernateRepositoryBuilder(session);
        RestaurantRepository repository = recorder.getRepository(RestaurantRepository.class);
        Restaurant restaurante = repository.findByName("Nakombi");
        Assert.assertEquals(expectedResult, restaurante);
        mockery.assertIsSatisfied();
    }
}
