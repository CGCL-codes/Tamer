package org.starobjects.sprung;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.starobjects.sprung.SpringContextLoaderDefault;

public class SpringContextLoaderDefault_Load {

    private SpringContextLoaderDefault loader;

    @Before
    public void setUp() throws Exception {
        loader = new SpringContextLoaderDefault();
    }

    @After
    public void tearDown() throws Exception {
        loader = null;
    }

    @Test
    public void shouldBeAbleToLoadApplicationContextThatExists() throws Exception {
        loader.load("test.xml");
    }

    @Test
    public void shouldBeAbleToLoadApplicationContextThatExistsInAPackage() throws Exception {
        SpringContextLoaderDefault loader = new SpringContextLoaderDefault();
        loader.load("org/nakedobjects/boot/system/spring/test2.xml");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToLoadApplicationContextThatDoesNotExists() throws Exception {
        SpringContextLoaderDefault loader = new SpringContextLoaderDefault();
        loader.load("nonexistent.xml");
    }

    @Test
    public void shouldBeAbleToFetchOutBeanFromApplicationContext() throws Exception {
        SpringContextLoaderDefault loader = new SpringContextLoaderDefault();
        loader.load("test.xml");
        assertThat(loader.getBean("someBean"), is(not(nullValue())));
    }

    @Test
    public void shouldBeAbleToGetUnderlyingSpringApplicationContext() throws Exception {
        SpringContextLoaderDefault loader = new SpringContextLoaderDefault();
        loader.load("test.xml");
        assertThat(loader.getContext(), is(not(nullValue())));
    }

    @Test
    public void shouldLoadApplicationContextBasedOnComponentClass() throws Exception {
        SpringContextLoaderDefault loader = new SpringContextLoaderDefault();
        loader.load(DummyComponentWithCorrespondingSpringApplicationContext.class);
        assertThat(loader.getContext(), is(not(nullValue())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToLoadApplicationContextBasedOnComponentClassIfDoesntExist() throws Exception {
        SpringContextLoaderDefault loader = new SpringContextLoaderDefault();
        loader.load(DummyComponentWithNoCorrespondingSpringApplicationContext.class);
        assertThat(loader.getContext(), is(not(nullValue())));
    }
}
