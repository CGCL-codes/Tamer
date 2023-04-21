package ecskernel.write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Creates a new config.xml file to set up the autorun program to run how the
 * user wants it to.
 * 
 * @author Kate Macarthur
 */
public class AutorunConfigFileWriter {

    static BufferedWriter writer;

    static int repetitions;

    static String[] map;

    static boolean firesOn, blockadesOn;

    /**
	 * Creates a new instance of an AutorunConfigFile and writes given 
	 * parameters to file.
	 * 
	 * @param repetitions the number of times to repeat the simulation
	 * @param map the map to run the simulation on
	 * @param firesOn true iff the firesimulator is to be started
	 * @param blockadesOn true iff the blockadesimulator is to be started
	 */
    public static void write(int repetitions_, String[] map_, boolean firesOn_, boolean blockadesOn_) {
        repetitions = repetitions_;
        map = map_;
        firesOn = firesOn_;
        blockadesOn = blockadesOn_;
        writeFile();
    }

    public static void write(int repetitions_, ArrayList<String> map_, boolean firesOn_, boolean blockadesOn_) {
        String[] mapsArray = map_.toArray(new String[0]);
        write(repetitions_, mapsArray, firesOn_, blockadesOn_);
    }

    /**
	 * Writes values saved into local variables to a file config.xml - read 
	 * in by the autorun program to set values of parameters for the simulations.
	 */
    private static void writeFile() {
        try {
            File file = new File("config.xml");
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" + "\n<CONFIG>" + "\n\t<CONTROLEUR>" + "\n\t\t<KILL_TIMEOUT>30</KILL_TIMEOUT>" + "\n\t\t<NUMBER_OF_REPETITIONS>" + repetitions + "</NUMBER_OF_REPETITIONS>" + "\n\t\t<SERVER_PROCESS_PORT>7755</SERVER_PROCESS_PORT>" + "\n\t\t<KERNEL_IP>127.0.0.1</KERNEL_IP>" + "\n\t\t<KERNEL_PORT>7000</KERNEL_PORT>" + "\n\t\t<MONITORING>false</MONITORING>" + "\n\t\t<AUTOSTARTING>true</AUTOSTARTING>" + "\n\t</CONTROLEUR>");
            writer.newLine();
            writer.write("\n\t<SITUATIONS>");
            for (int i = 0; i < map.length; i++) {
                writer.write("\n\t\t<SITUATION>" + map[i] + "/</SITUATION>");
            }
            writer.write("\n\t</SITUATIONS>");
            writer.newLine();
            writer.write("\n\t<SIMULATOR_PROCESSES>" + "\n\t\t<PROCESS>" + "\n\t\t\t<NAME>Gis</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/0gis.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>" + "\n\t\t<PROCESS>" + "\n\t\t\t<NAME>Kernel</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/1kernel.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>" + "\n\t\t<PROCESS>" + "\n\t\t\t<NAME>Viewer</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/2viewer.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>" + "\n\t\t<PROCESS>" + "\n\t\t\t<NAME>MiscSimulator</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/3miscsimulator.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>" + "\n\t\t<PROCESS>" + "\n\t\t\t<NAME>MorimotoTrafficSimulator</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/4morimototrafficsimulator.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>");
            if (firesOn) {
                writer.write("\n\t\t<PROCESS>" + "\n\t\t\t<NAME>FireSimulator</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/5firesimulator.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>");
            }
            if (blockadesOn) {
                writer.write("\n\t\t<PROCESS>" + "\n\t\t\t<NAME>BlockadesSimulator</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/6blockadessimulator.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>");
            }
            writer.write("\n\t\t<PROCESS>" + "\n\t\t\t<NAME>CollapseSimulator</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/7collapsesimulator.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>" + "\n\t\t<PROCESS>" + "\n\t\t\t<NAME>Civilian</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>xterm -e ./boot/autorunboot/8civilian.sh</COMMAND_LINE>" + "\n\t\t\t<SLEEP>5</SLEEP>" + "\n\t\t</PROCESS>" + "\n\t</SIMULATOR_PROCESSES>");
            writer.write("\n\t<AGENT_PROCESSES>" + "\n\t\t<PROCESS>" + "\n\t\t\t<NAME>ecskernel</NAME>" + "\n\t\t\t<OS>linux</OS>" + "\n\t\t\t<IP>127.0.0.1</IP>" + "\n\t\t\t<COMMAND_LINE>java -Xmx800m -cp ./programs:lib/easyformlib.jar:lib/ecskernelautorun.jar:" + "lib/jcommon-1.0.0.jar:lib/jfreechart-1.0.1.jar:lib/mapreader.jar:lib/trove.jar ecskernel.AutorunLaunch</COMMAND_LINE>" + "\n\t\t</PROCESS>" + "\n\t</AGENT_PROCESSES>" + "\n</CONFIG>");
            writer.flush();
            writer.close();
            System.out.println("Config file written.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
