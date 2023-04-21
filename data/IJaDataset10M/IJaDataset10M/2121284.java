package org.apache.axis2.jaxws.sample;

import org.apache.axis2.jaxws.TestLogger;
import org.test.sample.nonwrap.ReturnType;
import org.test.sample.nonwrap.TwoWayHolder;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import java.util.concurrent.ExecutionException;

public class AsyncCallback implements AsyncHandler {

    /**
	 * 
	 */
    public AsyncCallback() {
        super();
    }

    public void handleResponse(Response response) {
        try {
            Object obj = response.get();
            if (obj instanceof ReturnType) {
                ReturnType type = (ReturnType) obj;
                TestLogger.logger.debug(">>Return String = " + type.getReturnStr());
                return;
            }
            if (obj instanceof TwoWayHolder) {
                TwoWayHolder twh = (TwoWayHolder) obj;
                TestLogger.logger.debug("AsyncCallback Holder string =" + twh.getTwoWayHolderStr());
                TestLogger.logger.debug("AsyncCallback Holder int =" + twh.getTwoWayHolderInt());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
