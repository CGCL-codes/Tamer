package com.bukkit.epicsaga.EpicManager.home;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

/**
 * A flat file with usernames and their home locations
 *
 * The file is cached in a TreeMap, and only read if the modified date changes.
 *
 * format is:
 *
 *  <playerName> : <worldId> <xPos> <yPos> <xPos>
 *
 * x,y,z postions are the double value rounded to nearest integer
 * worldId is a semi-unique world identifier returned by
 * 		org.bukkit.World.getId()
 *
 * @author _sir_maniac
 *
 */
public class HomeFile implements HomeStore {

    private File file;

    private long fileTime;

    private Server server;

    private Map<String, Location> homes = new TreeMap<String, Location>();

    public HomeFile(File file, Server server) {
        this.file = file;
        this.server = server;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        load(true);
    }

    public Location getHome(String playerName) {
        checkFile();
        return homes.get(playerName);
    }

    /**
	 * remove user's home
	 *
	 * the file is rewritten in this call, this should not be a problem
	 * since it won't be used very often.
	 */
    public void clearHome(String playerName) {
        homes.remove(playerName);
        save();
    }

    /**
	 * set or replace a player's home.
	 *
	 * the file is rewritten in this call, this should not be a problem
	 * since setHome won't be used very often.
	 */
    public void setHome(String playerName, Location location) {
        homes.put(playerName, new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        save();
    }

    private void checkFile() {
        if (file.lastModified() > fileTime) load(false);
    }

    /**
	 * save the in-memory date to the file, ignores file errors
	 */
    private void save() {
        checkFile();
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.getChannel().truncate(0);
            Writer writer = new BufferedWriter(new OutputStreamWriter(stream));
            Location val;
            long worldId, x, y, z;
            String line;
            try {
                for (Map.Entry<String, Location> home : homes.entrySet()) {
                    val = home.getValue();
                    worldId = val.getWorld().getId();
                    x = val.getBlockX();
                    y = val.getBlockY();
                    z = val.getBlockZ();
                    line = String.format("%s : %s %d %d %d\n", home.getKey(), Long.toString(worldId, 16), x, y, z);
                    writer.write(line);
                }
            } finally {
                writer.close();
            }
            fileTime = file.lastModified();
        } catch (IOException e) {
        }
    }

    /**
	 * fill the in-memory database from the file, ignores file errors
	 * @param replace if true, the in-memory database is replaced, otherwise it
	 *    is merged.
	 */
    private void load(boolean replace) {
        String line;
        String playerName;
        Location location;
        String[] parts;
        World[] worlds = server.getWorlds();
        fileTime = file.lastModified();
        try {
            Scanner scanner = new Scanner(file);
            if (replace) {
                homes.clear();
            }
            try {
                while (scanner.hasNext()) {
                    location = null;
                    line = scanner.nextLine().trim();
                    if (line.startsWith("#") || line.isEmpty()) continue;
                    parts = line.split(":", 2);
                    if (parts.length != 2) continue;
                    playerName = parts[0].trim();
                    location = makeLocation(worlds, parts[1]);
                    if (location == null) continue;
                    homes.put(playerName, location);
                }
            } finally {
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * parses the line (which should have 4 space separated numbers)
	 * @param worlds
	 * @param line
	 * @return
	 */
    private Location makeLocation(World[] worlds, String line) {
        Scanner scanner = new Scanner(line);
        long worldId, x, y, z;
        try {
            scanner.skip("[ \t]*");
            worldId = scanner.nextLong(16);
            x = scanner.nextLong();
            y = scanner.nextLong();
            z = scanner.nextLong();
            return makeLocation(worlds, worldId, x, y, z);
        } catch (InputMismatchException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 *
	 * @return newly created location, or null if world isn't in worlds
	 */
    private Location makeLocation(World[] worlds, long worldId, long x, long y, long z) {
        for (World world : worlds) {
            if (world.getId() == worldId) {
                return new Location(world, x, y, z);
            }
        }
        return null;
    }
}
