package a00720398.data;

import java.util.*;
import java.io.*;
import a00720398.util.*;
import a00720398.ui.*;
import a00720398.data.*;

public class BedAndBreakfast {

    private String name = "foo";

    private List<Guest> guestList;

    private SortedSet<Guest> guestSet = new TreeSet<Guest>(new GuestSorter.ByLastName());

    private List<Room> rooms;

    private List<Reservation> reservations;

    private BedAndBreakfast() {
    }

    public BedAndBreakfast(String name) {
        this.name = name;
    }

    public void loadData(a00720398.ui.Console console) {
        guestList = console.scanFile(new File("Guests.txt"), new GuestFactory());
        guestSet.addAll(guestList);
        rooms = console.scanFile(new File("Rooms.txt"), new RoomFactory());
        reservations = console.scanFile(new File("Reservations.txt"), new ReservationFactory());
    }

    public void writeData(a00720398.ui.Console console) {
        console.output(guestSet, new File("out.txt"));
        console.output(rooms, new File("out.txt"));
        console.output(reservations, new File("out.txt"));
    }
}
