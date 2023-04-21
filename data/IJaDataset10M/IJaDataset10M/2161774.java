package com.gusto.test.evaluation;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.gusto.engine.evaluation.RealEvaluation;
import com.gusto.engine.evaluation.indicators.Coverage;
import com.gusto.engine.evaluation.indicators.MAE;
import com.gusto.engine.evaluation.indicators.RMSE;
import com.gusto.engine.evaluation.service.EvaluationService;

public class TestEvaluationService {

    private EvaluationService evaluationService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext factory = new ClassPathXmlApplicationContext("config-evaluation.xml");
        this.evaluationService = (EvaluationService) factory.getBean("evaluationService");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIndicatorsSimple() {
        evaluationService.start();
        evaluationService.addEvaluation(new RealEvaluation(12, 25, 3), 2.5);
        evaluationService.addEvaluation(new RealEvaluation(84, 35, 4), 4.0);
        evaluationService.addEvaluation(new RealEvaluation(76, 48, 2), null);
        evaluationService.finish();
        assertEquals(new Double(0.25), ((MAE) evaluationService).getMAE());
        assertEquals(new Double(0.3535533905932738), ((RMSE) evaluationService).getRMSE());
        assertEquals(new Double(66.66666666666666), ((Coverage) evaluationService).getCoverage());
    }

    @Test
    public void testData() {
        evaluationService.start();
        for (RealEvaluation real : evaluationService.getData()) {
            assertNotNull(real);
            Double prediction = 2.5;
            evaluationService.addEvaluation(real, prediction);
        }
        evaluationService.finish();
        assertEquals(new Double(1.5041), ((MAE) evaluationService).getMAE(), 0.0001);
        assertEquals(new Double(1.6981), ((RMSE) evaluationService).getRMSE(), 0.0001);
        assertEquals(new Double(100), ((Coverage) evaluationService).getCoverage());
    }
}
