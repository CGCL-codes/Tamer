package it.hotel.model.hotel.manager;

import it.hotel.model.structure.manager.StructureManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class HotelManager extends StructureManager implements IHotelManager {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void remove(Object entity) {
        super.remove(entity);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void remove(int id) {
        super.remove(id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public boolean add(Object object) {
        boolean response = super.add(object);
        return response;
    }
}
