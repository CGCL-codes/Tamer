package $servicePackage$;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import $serviceClientPackage$.$serviceName$;

public class $serviceName$ClientInvoker implements InitializingBean {

    private static final Log LOG = LogFactory.getLog($serviceName$ClientInvoker.class);

    private Integer delayBeforeSending = 5000;

    private $serviceName$ $serviceId$;

    public $serviceName$ get$serviceName$() {
        return $serviceId$;
    }

    public void set$serviceName$($serviceName$ $serviceId$) {
        this.$serviceId$ = $serviceId$;
    }

    public Integer getDelayBeforeSending() {
        return delayBeforeSending;
    }

    public void setDelayBeforeSending(Integer delayBeforeSending) {
        this.delayBeforeSending = delayBeforeSending;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull($serviceId$);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    performRequest();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, delayBeforeSending);
    }

    private void performRequest() {
        LOG.info("Performing invocation on ...");
        LOG.info("Result of runing is....");
    }
}
