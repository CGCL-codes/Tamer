package net.rmanager.equipment;

import net.rmanager.environment.Restaurant.DepartmentType;
import net.rmanager.equipment.EquipmentVendor.CabinetType;
import net.rmanager.generated.sanitary.SanitaryBean;

/**
 * Sanitary represents all possible sanitaries in the game. 
 * A sanitary produces a specific number of meals.
 *
 */
public class Sanitary extends Cabinet {

    public Sanitary(SanitaryBean type) {
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
        return DepartmentType.Facilities;
    }

    @Override
    public CabinetType getCabinetType() {
        return CabinetType.Sanitary;
    }
}
