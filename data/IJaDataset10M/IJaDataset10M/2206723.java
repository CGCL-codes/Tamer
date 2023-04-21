package net.bervini.rasael.galacticfreedom.game.space.entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.sql.*;
import net.bervini.rasael.galacticfreedom.*;
import net.bervini.rasael.galacticfreedom.util.XSQLResultSet;

/**
 *
 * @author Rasael Bervini
 */
public class Sector extends SpaceEntity {

    private String description;

    /**
     * Creates a new instance of Sector
     */
    public Sector(String name, double x, double y, int width, int height, Main main) {
        super(x, y, name, main);
        setWidth(width);
        setHeight(height);
    }

    public static Sector createSector(ResultSet rs, Main main) throws SQLException {
        String name = rs.getString("sectors_name");
        double x = rs.getDouble("sectors_x");
        double y = rs.getDouble("sectors_y");
        int w = rs.getInt("sectors_width");
        int h = rs.getInt("sectors_height");
        Sector newSector = new Sector(name, x, y, w, h, main);
        newSector.setID(rs.getInt("sectors_id"));
        newSector.setDescription(rs.getString("sectors_description"));
        return newSector;
    }

    public static Sector createSectorFromXML(XSQLResultSet rs, Main main) throws SQLException {
        String name = rs.getString("sectors_name");
        double x = rs.getDouble("sectors_x");
        double y = rs.getDouble("sectors_y");
        int w = rs.getInt("sectors_width");
        int h = rs.getInt("sectors_height");
        Sector newSector = new Sector(name, x, y, w, h, main);
        newSector.setID(rs.getInt("sectors_id"));
        newSector.setDescription(rs.getString("sectors_description"));
        return newSector;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight());
    }

    public boolean contains(Point p) {
        return getBounds().contains(p);
    }
}
