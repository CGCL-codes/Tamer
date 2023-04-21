package com.sjt.pi.engine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.sjt.chain.core.ChainManager;
import com.sjt.chain.core.DataContainer;

/**
 * This class extends {@link com.sjt.pi.engine.Engine} class and provides main functionality for the ProjectIncubator.
 * It loads beans from a "beans.xml" file and configure {@link com.sjt.chain.core.ChainManager} and
 * {@link com.sjt.chain.core.DataContainer} classes. Only one instance may be created in application.
 * 
 * @author Alexey Pashkevich
 * 
 */
public class PIEngine extends Engine {

    private static PIEngine instance;

    private static DataContainer<HttpServletRequest, HttpServletResponse> dc;

    private static ChainManager cm;

    private static ApplicationContext context;

    private PIEngine() {
        context = new ClassPathXmlApplicationContext("beans.xml");
        dc = (DataContainer<HttpServletRequest, HttpServletResponse>) context.getBean("dataContainer");
        cm = (ChainManager) context.getBean("chainManager");
    }

    public static PIEngine getInstance() {
        if (instance == null) instance = new PIEngine();
        return instance;
    }

    public static DataContainer getDataContainer() {
        return dc;
    }

    public static ChainManager getChainManager() {
        return cm;
    }

    public void setupDataContainer(HttpServletRequest request, HttpServletResponse response) {
        dc.setRequestData(request);
        dc.setResponseData(response);
    }
}
