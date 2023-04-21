package com.jadcon.harveycedars.model.dormitory;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("GuestRepository")
public class GuestRepositoryInMemoryImpl implements GuestRepository {

    private List<Guest> guestList = new ArrayList<Guest>();

    public GuestRepositoryInMemoryImpl() {
        Guest adam = new Guest("Adam Confino", "50 Magnolia Drive", "Douglassville PA 19518", "610-327-3973", "aconfino@gmail.com");
        guestList.add(adam);
    }

    public Guest findGuestByFullname(String fullname) {
        for (Guest guest : guestList) {
            if (guest.getFullname().equalsIgnoreCase(fullname)) {
                return guest;
            }
        }
        return null;
    }

    public List<Guest> getGuestList() {
        return guestList;
    }

    public void save(Guest guest) {
        guestList.add(guest);
    }
}
