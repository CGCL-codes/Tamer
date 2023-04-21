package pl.edu.agh.ssm.component.mc.defaults;

import pl.edu.agh.ssm.component.mc.annotations.BasicMC;
import pl.edu.agh.ssm.component.mc.annotations.HealthCheckMethod;
import pl.edu.agh.ssm.component.mc.annotations.IgnoreSuper;

@IgnoreSuper
@BasicMC
public class Log4jBasicMC extends Log4JAppenderMC {

    @HealthCheckMethod
    public int getHealthCheck() {
        return getLastMaxLevel();
    }
}
