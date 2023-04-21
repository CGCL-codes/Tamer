package example.dto;

import java.util.ArrayList;

public class Rollercoaster {

    private ArrayList<RollercoasterTrain> trains;

    public ArrayList<RollercoasterTrain> getTrains() {
        return trains;
    }

    public void setTrains(ArrayList<RollercoasterTrain> t) {
        trains = t;
    }
}
