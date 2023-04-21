package objects.production;

import objects.Galaxy;
import objects.Planet;

public final class MaterialsProduction extends StandardProduction {

    @Override
    protected final boolean isMyName(String str) {
        return getName().equalsIgnoreCase(str) || getName().substring(0, 3).equalsIgnoreCase(str);
    }

    @Override
    public final void produce2(Galaxy galaxy, Planet planet) {
        planet.addMaterials(planet.getEffort() * planet.getResources());
    }
}
