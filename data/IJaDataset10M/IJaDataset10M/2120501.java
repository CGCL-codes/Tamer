package org.gjt.universe;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import org.gjt.universe.scheme001.Station_001;
import org.gjt.universe.scheme001.SpaceBattle_001;

public class SaveGame {

    private SaveGame() {
    }

    /** Write the file out to disk.
	 @param filename The filename.
	*/
    public static void fileWrite(String filename) throws IOException {
        Log.debug("Writing Save file: " + filename);
        FileOutputStream outfile = null;
        ObjectOutputStream outstream = null;
        outfile = new FileOutputStream(filename);
        outstream = new ObjectOutputStream(outfile);
        GameEngine.writeObject(outstream);
        OrderEngine.writeObject(outstream);
        GalaxyList.writeObject(outstream);
        SystemList.writeObject(outstream);
        PlanetList.writeObject(outstream);
        CivList.writeObject(outstream);
        StationList.writeObject(outstream);
        FleetList.writeObject(outstream);
        ShipList.writeObject(outstream);
        ShipDesignList.writeObject(outstream);
        KnowledgeList.writeObject(outstream);
        ModuleList.writeObject(outstream);
        ModuleDesignList.writeObject(outstream);
        TechList.writeObject(outstream);
        CostList.writeObject(outstream);
        Station_001.writeStatics(outstream);
        SpaceBattle_001.writeStatics(outstream);
        outstream.flush();
        outfile.close();
    }

    /** Read the file from to disk.
	 @param filename The filename.
	*/
    public static void fileRead(String filename) {
        Log.debug("Reading Save file: " + filename);
        FileInputStream infile = null;
        ObjectInputStream instream = null;
        try {
            infile = new FileInputStream(filename);
            instream = new ObjectInputStream(infile);
        } catch (IOException e) {
        }
        if (instream == null) {
            return;
        }
        try {
            GameEngine.readObject(instream);
            OrderEngine.readObject(instream);
            GalaxyList.readObject(instream);
            SystemList.readObject(instream);
            PlanetList.readObject(instream);
            CivList.readObject(instream);
            StationList.readObject(instream);
            FleetList.readObject(instream);
            ShipList.readObject(instream);
            ShipDesignList.readObject(instream);
            KnowledgeList.readObject(instream);
            ModuleList.readObject(instream);
            ModuleDesignList.readObject(instream);
            TechList.readObject(instream);
            CostList.readObject(instream);
            Station_001.readStatics(instream);
            SpaceBattle_001.readStatics(instream);
            infile.close();
        } catch (IOException e) {
            Log.warning("IOException occured in fileRead: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.warning("ClassNotFoundException occured in fileRead: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
