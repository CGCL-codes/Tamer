package org.javalite.activeweb;

import app.controllers.AbcPersonController;
import app.filters.UnconditionalRenderFilter;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Igor Polevoy
 */
public class FilterRenderSpec extends IntegrationSpec {

    @Before
    public void before() {
        setTemplateLocation("src/test/views");
        addFilter(AbcPersonController.class, new UnconditionalRenderFilter());
    }

    @Test
    public void shouldRenderFromFilter() {
        controller("abc_person").param("user", "Jim").integrateViews().get("index");
        a(responseContent()).shouldBeEqual("Jim");
    }
}
