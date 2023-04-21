package jp.co.baka.gae.example.controller.view.test;

import jp.co.baka.gae.lib.MyController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slim3.controller.Navigation;

public class IndexController extends MyController {

    private static Log logger = LogFactory.getLog(IndexController.class);

    @Override
    public Navigation run() {
        logger.info("おーい");
        return forward("index.jsp");
    }
}
