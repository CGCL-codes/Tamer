package barsuift.simLife.tree;

import java.math.BigDecimal;
import java.math.RoundingMode;
import barsuift.simLife.PercentHelper;
import barsuift.simLife.environment.Sun;
import barsuift.simLife.j3d.MobileEvent;
import barsuift.simLife.j3d.tree.BasicTreeLeaf3D;
import barsuift.simLife.j3d.tree.TreeLeaf3D;
import barsuift.simLife.message.BasicPublisher;
import barsuift.simLife.message.Publisher;
import barsuift.simLife.message.Subscriber;
import barsuift.simLife.universe.Universe;

public class BasicTreeLeaf implements TreeLeaf {

    private static final BigDecimal ONE = new BigDecimal(1);

    /**
     * 5% decrease
     */
    private static final BigDecimal AGING_EFFICIENCY_DECREASE = PercentHelper.getDecimalValue(95);

    private static final BigDecimal LOWEST_EFFICIENCY_BEFORE_FALLING = PercentHelper.getDecimalValue(10);

    private static final BigDecimal ENERGY_RATIO_TO_KEEP = PercentHelper.getDecimalValue(66);

    private static final BigDecimal MAX_ENERGY = new BigDecimal(100);

    private final TreeLeafState state;

    private BigDecimal efficiency;

    private final long creationMillis;

    private BigDecimal energy;

    private BigDecimal freeEnergy;

    private final BasicTreeLeaf3D leaf3D;

    private Universe universe;

    private final Publisher publisher = new BasicPublisher(this);

    public BasicTreeLeaf(TreeLeafState leafState) {
        if (leafState == null) {
            throw new IllegalArgumentException("null leaf state");
        }
        this.state = leafState;
        this.efficiency = state.getEfficiency();
        this.creationMillis = state.getCreationMillis();
        this.energy = state.getEnergy();
        this.freeEnergy = state.getFreeEnergy();
        this.leaf3D = new BasicTreeLeaf3D(leafState.getLeaf3DState());
        this.leaf3D.addSubscriber(this);
    }

    public void init(Universe universe) {
        if (universe == null) {
            throw new IllegalArgumentException("null universe");
        }
        this.universe = universe;
        this.leaf3D.init(universe.getUniverse3D(), this);
    }

    /**
     * Reduce the efficiency by 5 percent, and notify subscribers. If the leaf is too weak, then fall (notify
     * subscribers).
     */
    @Override
    public void age() {
        efficiency = efficiency.multiply(AGING_EFFICIENCY_DECREASE);
        setChanged();
        notifySubscribers(LeafEvent.EFFICIENCY);
        if (isTooWeak()) {
            fall();
        }
    }

    /**
     * Compute the new leaf energy. It is the old energy + the collected energy.
     * <code>collectedEnergy= sunBrightness * leafEfficiency * energyDensity * leaf Area</code>
     */
    @Override
    public void collectSolarEnergy() {
        BigDecimal lightRate = universe.getEnvironment().getSky().getSun().getSun3D().getBrightness();
        BigDecimal solarEnergyRateCollected = efficiency.multiply(lightRate);
        BigDecimal energyCollected = solarEnergyRateCollected.multiply(Sun.ENERGY_DENSITY).multiply(new BigDecimal(leaf3D.getArea()));
        BigDecimal energyCollectedForLeaf = energyCollected.multiply(ENERGY_RATIO_TO_KEEP);
        BigDecimal freeEnergyCollected = energyCollected.subtract(energyCollectedForLeaf);
        energy = energy.add(energyCollectedForLeaf).setScale(10, RoundingMode.HALF_DOWN);
        energy = energy.min(MAX_ENERGY);
        freeEnergy = freeEnergy.add(freeEnergyCollected).setScale(5, RoundingMode.HALF_DOWN);
    }

    /**
     * Return true if the leaf efficiency is lower than the lowest acceptable value
     */
    public boolean isTooWeak() {
        return efficiency.compareTo(LOWEST_EFFICIENCY_BEFORE_FALLING) < 0;
    }

    /**
     * Send a notifying message of LeafUpdateCode.fall
     */
    private void fall() {
        setChanged();
        notifySubscribers(MobileEvent.FALLING);
        universe.getPhysics().getGravity().addFallingLeaf(this);
    }

    /**
     * Use leaf energy to get more efficiency. One energy point allows to gain 1% of energy.
     */
    @Override
    public void improveEfficiency() {
        BigDecimal maxEfficiencyToAdd = ONE.subtract(efficiency);
        BigDecimal efficiencyToAdd = maxEfficiencyToAdd.min(energy.movePointLeft(2));
        efficiency = efficiency.add(efficiencyToAdd).setScale(10, RoundingMode.HALF_DOWN);
        energy = energy.subtract(efficiencyToAdd.movePointRight(2)).setScale(5, RoundingMode.HALF_DOWN);
        setChanged();
        notifySubscribers(LeafEvent.EFFICIENCY);
    }

    @Override
    public BigDecimal getEfficiency() {
        return efficiency;
    }

    @Override
    public BigDecimal getEnergy() {
        return energy;
    }

    @Override
    public BigDecimal collectFreeEnergy() {
        BigDecimal currentFreeEnergy = freeEnergy;
        freeEnergy = new BigDecimal(0);
        return currentFreeEnergy;
    }

    @Override
    public long getCreationMillis() {
        return creationMillis;
    }

    public TreeLeaf3D getTreeLeaf3D() {
        return leaf3D;
    }

    @Override
    public void update(Publisher publisher, Object arg) {
        if (arg == MobileEvent.FALLEN) {
            setChanged();
            notifySubscribers(arg);
        }
    }

    @Override
    public TreeLeafState getState() {
        synchronize();
        return state;
    }

    @Override
    public void synchronize() {
        state.setEfficiency(efficiency);
        state.setEnergy(energy);
        state.setFreeEnergy(freeEnergy);
        leaf3D.synchronize();
    }

    public void addSubscriber(Subscriber subscriber) {
        publisher.addSubscriber(subscriber);
    }

    public void deleteSubscriber(Subscriber subscriber) {
        publisher.deleteSubscriber(subscriber);
    }

    public void notifySubscribers() {
        publisher.notifySubscribers();
    }

    public void notifySubscribers(Object arg) {
        publisher.notifySubscribers(arg);
    }

    public void deleteSubscribers() {
        publisher.deleteSubscribers();
    }

    public boolean hasChanged() {
        return publisher.hasChanged();
    }

    public int countSubscribers() {
        return publisher.countSubscribers();
    }

    public void setChanged() {
        publisher.setChanged();
    }

    public void clearChanged() {
        publisher.clearChanged();
    }
}
