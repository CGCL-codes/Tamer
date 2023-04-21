package org.mars_sim.msp.ui.standard.monitor;

import org.mars_sim.msp.simulation.*;
import org.mars_sim.msp.simulation.structure.Settlement;
import org.mars_sim.msp.simulation.malfunction.Malfunction;
import org.mars_sim.msp.simulation.vehicle.*;

/**
 * The VehicleTableModel that maintains a list of Vehicle objects.
 * It maps key attributes of the Vehicle into Columns.
 */
public class VehicleTableModel extends UnitTableModel {

    private static final int NAME = 0;

    private static final int DESCRIPTION = 1;

    private static final int DESTINATION = 2;

    private static final int DESTDIST = 3;

    private static final int LOCATION = 4;

    private static final int CREW = 5;

    private static final int SPEED = 6;

    private static final int DRIVER = 7;

    private static final int STATUS = 8;

    private static final int MALFUNCTION = 9;

    private static final int OXYGEN = 10;

    private static final int METHANE = 11;

    private static final int WATER = 12;

    private static final int FOOD = 13;

    private static final int ROCK_SAMPLES = 14;

    private static final int COLUMNCOUNT = 15;

    private static String columnNames[];

    private static Class columnTypes[];

    /**
     * Class initialiser creates the static names and classes.
     */
    static {
        columnNames = new String[COLUMNCOUNT];
        columnTypes = new Class[COLUMNCOUNT];
        columnNames[NAME] = "Name";
        columnTypes[NAME] = String.class;
        columnNames[DESCRIPTION] = "Description";
        columnTypes[DESCRIPTION] = String.class;
        columnNames[DRIVER] = "Driver";
        columnTypes[DRIVER] = String.class;
        columnNames[STATUS] = "Status";
        columnTypes[STATUS] = String.class;
        columnNames[LOCATION] = "Location";
        columnTypes[LOCATION] = String.class;
        columnNames[SPEED] = "Speed";
        columnTypes[SPEED] = Integer.class;
        columnNames[MALFUNCTION] = "Malfunction";
        columnTypes[MALFUNCTION] = String.class;
        columnNames[CREW] = "Crew";
        columnTypes[CREW] = Integer.class;
        columnNames[DESTINATION] = "Destination";
        columnTypes[DESTINATION] = Coordinates.class;
        columnNames[DESTDIST] = "Dest. Dist.";
        columnTypes[DESTDIST] = Integer.class;
        columnNames[FOOD] = "Food";
        columnTypes[FOOD] = Integer.class;
        columnNames[OXYGEN] = "Oxygen";
        columnTypes[OXYGEN] = Integer.class;
        columnNames[WATER] = "Water";
        columnTypes[WATER] = Integer.class;
        columnNames[METHANE] = "Methane";
        columnTypes[METHANE] = Integer.class;
        columnNames[ROCK_SAMPLES] = "Rock Samples";
        columnTypes[ROCK_SAMPLES] = Integer.class;
    }

    /**
     * Constructs a VehicleTableModel object. It creates the list of possible
     * Vehicles from the Unit manager.
     *
     * @param unitManager Proxy manager contains displayable Vehicles.
     */
    public VehicleTableModel(UnitManager unitManager) {
        super("All Vehicles", " vehicles", columnNames, columnTypes);
        VehicleIterator iter = unitManager.getVehicles().sortByName().iterator();
        while (iter.hasNext()) {
            add(iter.next());
        }
    }

    /**
     * Return the value of a Cell
     * @param rowIndex Row index of the cell.
     * @param columnIndex Column index of the cell.
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        Vehicle vehicle = (Vehicle) getUnit(rowIndex);
        switch(columnIndex) {
            case NAME:
                {
                    result = vehicle.getName();
                }
                break;
            case DESCRIPTION:
                {
                    result = vehicle.getDescription();
                }
                break;
            case CREW:
                {
                    if (vehicle instanceof Crewable) result = new Integer(((Crewable) vehicle).getCrewNum()); else result = new Integer(0);
                }
                break;
            case WATER:
                {
                    double water = vehicle.getInventory().getResourceMass(Resource.WATER);
                    result = new Integer((int) water);
                }
                break;
            case FOOD:
                {
                    double food = vehicle.getInventory().getResourceMass(Resource.FOOD);
                    result = new Integer((int) food);
                }
                break;
            case OXYGEN:
                {
                    double oxygen = vehicle.getInventory().getResourceMass(Resource.OXYGEN);
                    result = new Integer((int) oxygen);
                }
                break;
            case METHANE:
                {
                    double methane = vehicle.getInventory().getResourceMass(Resource.METHANE);
                    result = new Integer((int) methane);
                }
                break;
            case ROCK_SAMPLES:
                {
                    double rockSamples = vehicle.getInventory().getResourceMass(Resource.ROCK_SAMPLES);
                    result = new Integer((int) rockSamples);
                }
                break;
            case SPEED:
                {
                    result = new Integer(new Float(vehicle.getSpeed()).intValue());
                }
                break;
            case DRIVER:
                {
                    if (vehicle.getDriver() != null) {
                        result = vehicle.getDriver().getName();
                    } else {
                        result = null;
                    }
                }
                break;
            case STATUS:
                {
                    result = vehicle.getStatus();
                }
                break;
            case MALFUNCTION:
                {
                    Malfunction failure = vehicle.getMalfunctionManager().getMostSeriousMalfunction();
                    if (failure != null) result = failure.getName();
                }
                break;
            case LOCATION:
                {
                    Settlement settle = vehicle.getSettlement();
                    if (settle != null) {
                        result = settle.getName();
                    } else {
                        result = vehicle.getCoordinates().getFormattedString();
                    }
                }
                break;
            case DESTINATION:
                {
                    if (!vehicle.getDestinationType().equals("None")) {
                        Settlement settle = vehicle.getDestinationSettlement();
                        if (settle != null) {
                            result = settle.getName();
                        } else {
                            result = vehicle.getDestination().getFormattedString();
                        }
                    }
                }
                break;
            case DESTDIST:
                {
                    result = new Integer(new Float(vehicle.getDistanceToDestination()).intValue());
                }
                break;
        }
        return result;
    }
}
