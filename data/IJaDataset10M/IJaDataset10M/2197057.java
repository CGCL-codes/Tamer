package net.rmanager.equipment;

import net.rmanager.environment.Restaurant.DepartmentType;
import net.rmanager.equipment.EquipmentVendor.CabinetType;
import net.rmanager.generated.interior.InteriorBean;

/**
 * Interior represents all possible interior in the game. 
 * An interior produces a specific number of meals.
 *
 */
public class Interior extends Cabinet {

    public Interior(InteriorBean type) {
        super(type.getType(), type.getPrice().intValue(), type.getCapacity().intValue(), type.getDuration().intValue(), type.getDepletion().intValue(), type.getMonthlyCosts().intValue());
        super.setImage(type.getImage());
    }

    public int produce() {
        int meals = super.produce();
        return meals;
    }

    public int compareTo(Cabinet c) {
        if (this.getPrice() < c.getPrice()) return -1; else if (this.getPrice() > c.getPrice()) {
            return 1;
        }
        return 0;
    }

    @Override
    public DepartmentType getDepartment() {
        return DepartmentType.Dininghall;
    }

    @Override
    public CabinetType getCabinetType() {
        return CabinetType.Interior;
    }
}
