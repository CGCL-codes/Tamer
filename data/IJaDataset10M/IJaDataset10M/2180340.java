package com.hszt.structure;

import java.util.Vector;

/**
 * Class that allows to create LabyrinthStar objects with a point in the center surrounded by paths going out form that point.
 *
 * @author danielroth
 * @author adrianchristen
 * @author matthiasschmid
 */
public class LabyrinthStar {

    private Point center;

    private Vector<Path> paths;

    public LabyrinthStar(Point center, Vector<Path> paths) {
        this.center = center;
        this.paths = paths;
    }

    /**
     * Points out the Center of a LabyrinthStar.
     *
     * @return the center
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Lists the paths of a LabyrinthStar.
     *
     * @return the paths
     */
    public Vector<Path> getPaths() {
        return paths;
    }

    /**
     * Adds a path to the list paths if it does not contain this one yet.
     *
     * @param path
     */
    protected void addPath(Path path) {
        if (!paths.contains(path)) {
            paths.add(path);
        }
    }
}
