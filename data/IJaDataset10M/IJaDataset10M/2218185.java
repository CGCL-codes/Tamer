package a00720398.data;

import java.util.*;
import a00720398.util.*;

public class Reservation {

    private Integer id = 0, roomNumber = 0, guestId = 0;

    private Payment payment;

    public Reservation() throws DataException {
    }

    public Reservation(String stringData) throws DataException {
        String[] data = stringData.split("\t");
        if (data.length != 4) {
            throw new DataException("Reservationy data.length:\t" + data.length);
        }
        try {
            id = Integer.parseInt(data[0].trim());
            roomNumber = Integer.parseInt(data[1].trim());
            guestId = Integer.parseInt(data[2].trim());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataException(e.getMessage());
        }
        payment = new Payment();
    }

    public Integer getID() {
        return id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public Integer getGuestID() {
        return guestId;
    }

    public String toString() {
        return "\n\n\nRESERVATION\n===========" + "\nid\t\t" + getID() + "\nroomNumber\t" + getRoomNumber() + "\nguestID\t\t" + getGuestID();
    }
}
