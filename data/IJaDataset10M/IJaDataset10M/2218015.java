package com.googlecode.progobots.io;

import com.googlecode.progobots.Direction;
import com.googlecode.progobots.Location;
import com.googlecode.progobots.Progobot;
import com.googlecode.progobots.World;
import com.googlecode.progobots.WorldObject;

public class ProgobotReader implements WorldObjectReader {

    public WorldObject read(World world, String value) throws WorldParseException {
        try {
            String[] tokens = value.split("\\s+");
            if (tokens.length != 4) {
                throw new RuntimeException();
            }
            int x = Integer.parseInt(tokens[0]);
            int y = Integer.parseInt(tokens[1]);
            Direction d = Direction.userValueOf(tokens[3]);
            if (d == null) {
                throw new RuntimeException();
            }
            Progobot progobot = new Progobot(world, tokens[2], new Location(x, y), d);
            world.add(progobot);
            return progobot;
        } catch (Exception e) {
            throw new WorldParseException(String.format("Invalid %s: %s", Progobot.class.getSimpleName(), value));
        }
    }
}
