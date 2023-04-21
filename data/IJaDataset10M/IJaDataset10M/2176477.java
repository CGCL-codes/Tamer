package star.trek.voyager.module.benz.car_sls_amg;

import org.jiopi.framework.annotation.module.Instantiation;
import org.jiopi.framework.annotation.module.RegisterModule;
import org.jiopi.framework.annotation.module.SocketModule;
import star.trek.voyager.blueprint.car.Car;
import star.trek.voyager.blueprint.engine.Engine;
import star.trek.voyager.blueprint.engine.StatusOutput;
import org.apache.log4j.Logger;

@Instantiation(initMethod = "init")
@RegisterModule({ "star.trek.voyager.blueprint.car.Car" })
public class BenzSlsAmg implements Car, StatusOutput {

    private static Logger logger = Logger.getLogger(BenzSlsAmg.class);

    private String engineStatus = null;

    @SocketModule(id = "engine", module = "voyager.benz.engine_v8", version = "0.1.0.0")
    private Engine engine;

    public void init() {
        logger.info(toString() + " with " + engine);
    }

    public void start() {
        engine.retrofire();
    }

    public void stop() {
        engine.flameout();
    }

    public String getStatus() {
        return "BenzSlsAmg Status : " + (engineStatus == null ? "engine not start" : engineStatus) + "\n" + engine.checkStatus() + "\n";
    }

    public void output(String status) {
        engineStatus = status;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "-0.1.0.0";
    }
}
