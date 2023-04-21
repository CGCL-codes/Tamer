package server.dataProvider.commands;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import common.CampaignData;
import common.util.BinWriter;
import server.dataProvider.ServerCommand;

/**
 * Retrieve all planet information (if the data cache is lost at client side)
 * 
 * @author Imi (immanuel.scholz@gmx.de)
 */
public class BannedAmmo implements ServerCommand {

    /**
     * @see server.dataProvider.ServerCommand#execute(java.util.Date,
     *      java.io.PrintWriter, common.CampaignData)
     */
    public void execute(Date timestamp, BinWriter out, CampaignData data) throws Exception {
        FileInputStream configFile;
        try {
            configFile = new FileInputStream("./campaign/banammo.dat");
        } catch (FileNotFoundException FNFE) {
            FileOutputStream ban = new FileOutputStream("./campaign/banammo.dat");
            PrintStream p = new PrintStream(ban);
            p.println(System.currentTimeMillis());
            p.println("server#");
            ban.close();
            p.close();
            configFile = new FileInputStream("./campaign/banammo.dat");
        }
        BufferedReader config = new BufferedReader(new InputStreamReader(configFile));
        while (config.ready()) {
            out.println(config.readLine(), "BannedAmmo");
        }
        config.close();
        configFile.close();
    }
}
