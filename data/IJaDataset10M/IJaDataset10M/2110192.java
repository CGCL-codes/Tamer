package gridsim.example03;

import java.util.*;
import gridsim.*;
import eduni.simjava.Sim_event;

/**
 * Test class establishes Input and Output entities. Then Test class listens
 * to the event simulation waiting to receive one Gridlet from the other
 * GridSim entity, in this case is Example3 class. Afterwards, this class
 * sends back the Gridlet to Example3 class.
 */
class Test extends GridSim {

    /**
     * Allocates a new Test object
     * @param name  the Entity name
     * @param baud_rate  the communication speed
     * @throws Exception This happens when creating this entity before
     *                   initializing GridSim package or the entity name is
     *                   <tt>null</tt> or empty
     * @see gridsim.GridSim#Init(int, Calendar, boolean, String[], String[],
     *          String)
     */
    Test(String name, double baud_rate) throws Exception {
        super(name, baud_rate);
        System.out.println("... Creating a new Test object");
    }

    /**
     * Processes one event at one time. Receives a Gridlet object from the
     * other GridSim entity. Modifies the Gridlet's status and sends it back
     * to the sender.
     */
    public void body() {
        int entityID;
        Sim_event ev = new Sim_event();
        Gridlet gridlet;
        for (sim_get_next(ev); ev.get_tag() != GridSimTags.END_OF_SIMULATION; sim_get_next(ev)) {
            gridlet = (Gridlet) ev.get_data();
            try {
                gridlet.setGridletStatus(Gridlet.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("... Inside Test.body() => Receiving Gridlet " + gridlet.getGridletID() + " from Example3 object");
            entityID = ev.get_src();
            super.send(entityID, GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_RETURN, gridlet);
        }
        super.terminateIOEntities();
    }
}
