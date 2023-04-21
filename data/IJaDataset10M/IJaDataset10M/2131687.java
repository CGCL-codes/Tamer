package com.itroadlabs.maps.tiles;

import com.itroadlabs.maps.tiles.config.Config;
import com.itroadlabs.maps.tiles.index.IndexBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>Command line runner.</p>
 * <p>Uses first argument as configuration file name to run application functions, second - as function name to execute.</p>
 */
public class Run {

    private static final Log LOG = LogFactory.getLog(Run.class);

    public static void main(String[] args) {
        if (args.length != 2) {
            LOG.error("Incorrect command line arguments number. Arguments required: <configFilePath> <command : buildIndex|buildTiles>");
            System.exit(1);
        }
        try {
            long startTime = System.currentTimeMillis();
            FileInputStream in = new FileInputStream(args[0]);
            try {
                LOG.info("Loading configuration from " + args[0] + "...");
                Config config = Config.load(in);
                String command = args[1];
                LOG.info("Executing command: " + command);
                if ("buildIndex".equalsIgnoreCase(command)) {
                    new IndexBuilder(config).run();
                } else if ("buildTiles".equalsIgnoreCase(command)) {
                    new TilesBuilder(config).run();
                } else {
                    LOG.error("Unsupported command: " + command);
                }
                LOG.info("Execution time: " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
            } finally {
                in.close();
            }
        } catch (IOException e) {
            LOG.error(e);
            System.exit(1);
        }
    }
}
