package objects.production;

import objects.Galaxy;
import objects.Planet;
import objects.Race;
import java.util.Properties;

public final class Medicine_Production extends PostStandardProduction {

    private double cost;

    public Medicine_Production() {
        super("_Medicine");
    }

    @Override
    public void init(String name, Properties props) {
        cost = Double.parseDouble(props.getProperty("Galaxy.Production._Medicine.Cost", "500"));
    }

    @Override
    public String getType() {
        return "medicine";
    }

    @Override
    public final void production5(Galaxy galaxy, Planet planet) {
        double delta = planet.getEffort() / cost;
        Race owner = planet.getOwner();
        owner.setPIP(owner.getPIP() + delta);
    }
}
