package com.vangent.hieos.DocViewer.client.services.proxy;

import com.vangent.hieos.DocViewer.client.helper.Observer;
import com.vangent.hieos.DocViewer.client.helper.TimeOutHelper;

/**
 * 
 * @author Bernie Thuman
 * 
 */
public class ProxyService {

    private Observer observer;

    private TimeOutHelper timeOutHelper;

    /**
	 * 
	 * @param observer
	 * @param timeOutHelper
	 */
    public ProxyService(Observer observer, TimeOutHelper timeOutHelper) {
        this.observer = observer;
        this.timeOutHelper = timeOutHelper;
    }

    /**
	 * 
	 * @return
	 */
    public Observer getObserver() {
        return observer;
    }

    /**
	 * 
	 * @return
	 */
    public TimeOutHelper getTimeOutHelper() {
        return timeOutHelper;
    }

    /**
	 * 
	 */
    public void startTimer() {
        timeOutHelper.startTimer();
    }

    /**
	 * 
	 */
    public void cancelTimer() {
        timeOutHelper.cancelTimer();
    }

    /**
	 * 
	 * @param object
	 */
    public void update(Object object) {
        observer.update(object);
    }

    /**
	 * 
	 * @return
	 */
    public boolean getAbortFlag() {
        return timeOutHelper.getAbortFlag();
    }
}
