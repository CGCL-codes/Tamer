package planning.model;

import simulation.shell.IDefinedConfiguration;
import simulation.shell.IDriver;

public class Robot extends ModelObject {

    protected IDriver driver;

    protected IDefinedConfiguration target;

    protected boolean communicating;

    public Robot(IDriver driver, Entity<?> entity, IDefinedConfiguration dconfig, IDefinedConfiguration target) {
        super(entity, dconfig);
        this.target = target;
        this.driver = driver;
    }

    public IDriver getDriver() {
        return driver;
    }

    public void setDriver(IDriver driver) {
        this.driver = driver;
    }

    public IDefinedConfiguration getTarget() {
        return target;
    }

    public void setCommunicating(boolean communicating) {
        this.communicating = communicating;
    }

    public boolean isCommunicating() {
        return communicating;
    }
}
