package com.express.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import static com.express.matcher.BeanMatchers.hasValidSettersAndGettersExcluding;
import static com.express.matcher.BeanMatchers.usesPersistableEqualityStrategy;
import static com.express.matcher.BeanMatchers.usesPersistableHashCodeStrategy;
import static com.express.matcher.BeanMatchers.usesReflectionToStringBuilder;
import static org.hamcrest.MatcherAssert.assertThat;

public class IssueTest extends UnitilsJUnit4 {

    private static final Log LOG = LogFactory.getLog(IssueTest.class);

    Issue criteria;

    @Before
    public void setUp() {
        criteria = new Issue();
    }

    @Test
    public void shouldSetAndGetProperties() {
        assertThat(criteria, hasValidSettersAndGettersExcluding("equalityStrategy", "version"));
    }

    @Test
    public void shouldBaseEqualityOnThePersistableEqualityStrategy() {
        assertThat(criteria, usesPersistableEqualityStrategy());
    }

    @Test
    public void shouldBaseHashCodeOnThePersistableEqualityStrategy() {
        assertThat(criteria, usesPersistableHashCodeStrategy());
    }

    @Test
    public void shouldUseReflectionToStringBuilder() {
        assertThat(criteria, usesReflectionToStringBuilder());
    }
}
