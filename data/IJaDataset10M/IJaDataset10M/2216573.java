package gridsim.example07;

import java.util.*;
import gridsim.*;

/**
 * About Submit - Pause - Resume - Move - Cancel - Finish
 */
class TestCase8 extends GridSim {

    private int myId_;

    private String name_;

    private GridletList list_;

    private GridletList receiveList_;

    private double delay_;

    /**
     * Allocates a new TestCase7 object
     * @param name  the Entity name of this object
     * @param bandwidth     the communication speed
     * @param delay    simulation delay
     * @param totalGridlet    the number of Gridlets should be created
     * @param glLength  an array that contains various Gridlet's lengths
     * @throws Exception This happens when creating this entity before
     *                   initializing GridSim package or the entity name is
     *                   <tt>null</tt> or empty
     * @see gridsim.GridSim#Init(int, Calendar, boolean, String[], String[],
     *          String)
     */
    TestCase8(String name, double bandwidth, double delay, int totalGridlet, int[] glLength) throws Exception {
        super(name, bandwidth);
        this.name_ = name;
        this.delay_ = delay;
        this.receiveList_ = new GridletList();
        this.list_ = new GridletList();
        this.myId_ = super.getEntityId(name);
        System.out.println("Creating a grid user entity with name = " + name + ", and id = " + this.myId_);
        System.out.println(name + ":Creating " + totalGridlet + " Gridlets");
        this.createGridlet(myId_, totalGridlet, glLength);
    }

    /**
     * The core method that handles communications among GridSim entities.
     */
    public void body() {
        super.gridSimHold(3.0);
        LinkedList resList = super.getGridResourceList();
        int totalResource = resList.size();
        int resourceID[] = new int[totalResource];
        String resourceName[] = new String[totalResource];
        int i = 0;
        for (i = 0; i < totalResource; i++) {
            resourceID[i] = ((Integer) resList.get(i)).intValue();
            resourceName[i] = GridSim.getEntityName(resourceID[i]);
        }
        int index = myId_ % totalResource;
        if (index >= totalResource) {
            index = 0;
        }
        Gridlet gl = null;
        boolean success;
        for (i = 0; i < list_.size(); i++) {
            gl = (Gridlet) list_.get(i);
            if (i % 2 == 0) {
                success = super.gridletSubmit(gl, resourceID[index], 0.0, true);
                System.out.println(name_ + ": Sending Gridlet #" + gl.getGridletID() + " with status = " + success + " to " + resourceName[index]);
            } else {
                success = super.gridletSubmit(gl, resourceID[index], 0.0, false);
                System.out.println(name_ + ": Sending Gridlet #" + gl.getGridletID() + " with NO ACK so status = " + success + " to " + resourceName[index]);
            }
        }
        super.gridSimHold(15);
        System.out.println("<<<<<<<<< pause for 15 >>>>>>>>>>>");
        for (i = 0; i < list_.size(); i++) {
            if (i % 3 == 0) {
                success = super.gridletPause(i, myId_, resourceID[index], 0.0, true);
                System.out.println(name_ + ": Pausing Gridlet #" + i + " at time = " + GridSim.clock() + " success = " + success);
            }
        }
        super.gridSimHold(15);
        System.out.println("<<<<<<<<< pause for 15 >>>>>>>>>>>");
        for (i = 0; i < list_.size(); i++) {
            if (i % 3 == 0) {
                success = super.gridletResume(i, myId_, resourceID[index], 0.0, true);
                System.out.println(name_ + ": Resume Gridlet #" + i + " at time = " + GridSim.clock() + " success = " + success);
            }
        }
        super.gridSimHold(45);
        System.out.println("<<<<<<<<< pause for 45 >>>>>>>>>>>");
        if (resourceID.length == 1) {
            System.out.println("Can't move a Gridlet since GridResource is 1");
        } else {
            int move = 0;
            if (index == 0) {
                move = resourceID.length - 1;
            } else {
                move = index - 1;
            }
            for (i = 0; i < list_.size(); i++) {
                if (i % 3 == 0) {
                    success = super.gridletMove(i, myId_, resourceID[index], resourceID[move], 0.0, true);
                    System.out.println(name_ + ": Move Gridlet #" + i + " at time = " + GridSim.clock() + " success = " + success);
                }
            }
        }
        super.gridSimHold(25);
        System.out.println("<<<<<<<<< pause for 25 >>>>>>>>>>>");
        for (i = 0; i < list_.size(); i++) {
            if ((i % 3 == 0) || (i % 2 == 0)) {
                gl = super.gridletCancel(i, myId_, resourceID[index], 0.0);
                System.out.print(name_ + ": Canceling Gridlet #" + i + " at time = " + GridSim.clock());
                if (gl == null) {
                    System.out.println(" result = NULL");
                } else {
                    System.out.println(" result = NOT null");
                    receiveList_.add(gl);
                }
            }
        }
        super.gridSimHold(1000);
        System.out.println("<<<<<<<<< pause for 1000 >>>>>>>>>>>");
        int size = list_.size() - receiveList_.size();
        for (i = 0; i < size; i++) {
            gl = (Gridlet) super.receiveEventObject();
            receiveList_.add(gl);
            System.out.println(name_ + ": Receiving Gridlet #" + gl.getGridletID() + " at time = " + GridSim.clock());
        }
        System.out.println(this.name_ + ":%%%% Exiting body() at time " + GridSim.clock());
        shutdownUserEntity();
        terminateIOEntities();
        printGridletList(receiveList_, name_);
    }

    /**
     * This method will show you how to create Gridlets
     */
    private void createGridlet(int userID, int numGridlet, int[] data) {
        int k = 0;
        for (int i = 0; i < numGridlet; i++) {
            if (k == data.length) {
                k = 0;
            }
            Gridlet gl = new Gridlet(i, data[k], data[k], data[k]);
            gl.setUserID(userID);
            this.list_.add(gl);
            k++;
        }
    }

    /**
     * Prints the Gridlet objects
     */
    private void printGridletList(GridletList list, String name) {
        int size = list.size();
        Gridlet gridlet = null;
        String indent = "    ";
        System.out.println();
        System.out.println("============= OUTPUT for " + name + " ==========");
        System.out.println("Gridlet ID" + indent + "STATUS" + indent + "Resource ID" + indent + "Cost");
        int i = 0;
        for (i = 0; i < size; i++) {
            gridlet = (Gridlet) list.get(i);
            System.out.print(indent + gridlet.getGridletID() + indent + indent);
            System.out.print(gridlet.getGridletStatusString());
            System.out.println(indent + indent + gridlet.getResourceID() + indent + indent + gridlet.getProcessingCost());
        }
        for (i = 0; i < size; i++) {
            gridlet = (Gridlet) list.get(i);
            System.out.println(gridlet.getGridletHistory());
            System.out.print("Gridlet #" + gridlet.getGridletID());
            System.out.println(", length = " + gridlet.getGridletLength() + ", finished so far = " + gridlet.getGridletFinishedSoFar());
            System.out.println("===========================================\n");
        }
    }
}
