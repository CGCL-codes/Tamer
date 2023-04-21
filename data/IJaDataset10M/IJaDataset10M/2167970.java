package org.gtd4j.service;

import static org.junit.Assert.assertTrue;
import java.util.List;
import javax.annotation.Resource;
import org.gtd4j.domain.Action;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:gtd-service-test-context.xml" })
public class GtdServiceTest {

    private GtdService service;

    @Resource
    private GtdTestBean bean;

    @Before
    public void setup() {
        service.beginSession(bean.getApiKey());
    }

    @Test
    public void testAddAction() {
        service.addAction(bean.getTaskText());
        List<Action> actions = service.getAllActions();
        Predicate<Action> predicate = new Predicate<Action>() {

            public boolean apply(Action action) {
                return bean.getTaskText().equals(action.getDescription());
            }
        };
        List<Action> filteredActions = (List<Action>) Iterables.filter(actions, predicate);
        assertTrue(filteredActions.size() > 0);
        service.deleteAction(filteredActions.get(0));
    }

    @After
    public void teardown() {
        service.endSession();
    }
}
