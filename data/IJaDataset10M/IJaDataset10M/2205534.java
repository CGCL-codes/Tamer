package com.gusto.test.colfil;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.gusto.engine.colfil.Evaluation;
import com.gusto.engine.colfil.service.CollaborativeService;

public class TestCollaborativeService {

    private CollaborativeService collaborativeService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext factory = new ClassPathXmlApplicationContext("config-colfil.xml");
        this.collaborativeService = (CollaborativeService) factory.getBean("collaborativeService");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetRatings() {
        Evaluation eval = collaborativeService.getRating(1, 33);
        assertEquals(new Double(4.0), eval.getValue());
    }
}
