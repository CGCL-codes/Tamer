package devices;

import java.util.Vector;
import smarthouse.Room;
import commands.CommandInterface;
import commands.HeaterOffCommand;
import commands.HeaterTemperatureCommand;
import annotations.Device;

@Device(name = "Heater", description = "Device for heating the room")
public class Heater extends devices.AbstractDevice {

    public static int STATE = 0;

    public static int MAXTEMPERATURE = 1;

    public static int DESIREDTEMPERATURE = 2;

    public static int ECONOMIC = 3;

    public Heater(int id, Room room) {
        super(id, room);
    }

    @Override
    public Vector<Class<? extends CommandInterface>> getCommands() {
        Vector<Class<? extends CommandInterface>> commands = new Vector<Class<? extends CommandInterface>>();
        commands.add(HeaterOffCommand.class);
        commands.add(HeaterTemperatureCommand.class);
        return commands;
    }
}
