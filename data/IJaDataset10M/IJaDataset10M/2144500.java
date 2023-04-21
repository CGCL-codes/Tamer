package protopeer.network.flowbased.test;

import protopeer.network.flowbased.*;

public class NetworkModelDelayTest extends NetworkModelTest {

    private double delay = 500;

    @Override
    public IDelayModel getDelayModel() {
        return new ConstantDelayModel(delay);
    }

    @Override
    public double getDelay() {
        return delay;
    }
}
