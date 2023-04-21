package mh.top.service;

import org.apache.log4j.Logger;

/**
 * 该类为项目中定时任务的入口
 * @author Administrator
 *
 */
public class TopTimer {

    private Logger logger = Logger.getLogger(TopTimer.class);

    /**
	 * 记录商品，并生成要展示的商品
	 */
    public void startRegist() {
        DataRegister dr = new DataRegister();
        logger.info("startRegist..........");
        dr.startRegist();
    }
}
