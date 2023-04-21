package dynamicMap3D;

import graphics.GLCamera;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.media.opengl.GL;
import utilities.Location;
import utilities.Prism;

/**
 * sorts regions, assumes the regions are distributed over a plane bounded
 * by -width/2 to width/2 on the x-axis and -height/2 to height/2 on the y - axis
 * 
 * version 2
 * 
 * @author Jack
 *
 */
public final class DynamicMap3D {

    private Partition[][][] e;

    private int partitionSize = 50;

    Location center;

    private int width;

    private int height;

    private int depth;

    /**
	 * constructs a DynamicMap of the passed size
	 * @param center the center of the map
	 * @param width the width of the map
	 * @param height the height of the map
	 * @param depth the depth of the map
	 */
    public DynamicMap3D(Location center, int width, int height, int depth) {
        this.center = center;
        setSize(width, height, depth);
    }

    /**
	 * determines how many elements are in the map, does not determine unique
	 * elements only the total
	 * @return
	 */
    public int determineStoredElements() {
        int total = 0;
        for (int i = e.length - 1; i >= 0; i--) {
            for (int a = e[0].length - 1; a >= 0; a--) {
                for (int q = e[0][0].length - 1; q >= 0; q--) {
                    total += e[i][a][q].getElements().size();
                }
            }
        }
        return total;
    }

    public static void main(String[] args) {
        DynamicMap3D dm3 = new DynamicMap3D(new Location(0, 0, 0), 100, 20, 100);
        Prism p = new Prism(new Location(10, 10, -50), 200, 20, 20);
        dm3.addElement(p, 1);
        System.out.println("total elements = " + dm3.determineStoredElements());
        System.out.println("unique elements = " + dm3.getElements().size());
        Location old = p.getLocation();
        p.translate(20, 0, 70);
        dm3.adjustElement(p, old, p.getLocation(), 1);
        System.out.println("total elements = " + dm3.determineStoredElements());
        System.out.println("unique elements = " + dm3.getElements().size());
        dm3.removeElement(p, 1);
        System.out.println("total elements = " + dm3.determineStoredElements());
        System.out.println("unique elements = " + dm3.getElements().size());
    }

    /**
	 * gets the height
	 * @return returns the width of the map
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * gets the depth
	 * @return returns the depth of the map
	 */
    public int getDepth() {
        return depth;
    }

    /**
	 * tests to see if the element migrated into a different partition, if
	 * it did then it is removed and re-added to the correct partition
	 * @param r the element being tested
	 * @param start the elements initial location (prior to movement)
	 * @param end the elements end location
	 * @param id the id of the prism
	 */
    public void adjustElement(Prism r, Location start, Location end, int id) {
        int x1 = (int) start.x / partitionSize;
        int x2 = (int) end.x / partitionSize;
        int y1 = (int) start.y / partitionSize;
        int y2 = (int) end.y / partitionSize;
        int z1 = (int) start.z / partitionSize;
        int z2 = (int) end.z / partitionSize;
        if (x1 != x2 || y1 != y2 || z1 != z2) {
            removeElement(r, start, id);
            addElement(r, id);
        }
    }

    /**
	 * checks to see if the passed prism intersects
	 * with any other element in the map
	 * @param p the boundary of the unit being checked for intersection
	 * @return returns a hash set of prisms that the passed prism intersects
	 */
    public HashSet<Prism> checkIntersection(Prism p) {
        HashSet<Prism> hs = new HashSet<Prism>();
        if (inMap(p.getLocation())) {
            double xoffset = getxOffset();
            int xmin = (int) ((p.getLocation().x - p.getWidth() / 2 + xoffset) / partitionSize);
            int xmax = (int) ((p.getLocation().x + p.getWidth() / 2 + xoffset) / partitionSize);
            double yoffset = getyOffset();
            int ymin = (int) ((p.getLocation().y - p.getHeight() / 2 + yoffset) / partitionSize);
            int ymax = (int) ((p.getLocation().y + p.getHeight() / 2 + yoffset) / partitionSize);
            double zoffset = getzOffset();
            int zmin = (int) ((p.getLocation().z - p.getDepth() / 2 + zoffset) / partitionSize);
            int zmax = (int) ((p.getLocation().z + p.getDepth() / 2 + zoffset) / partitionSize);
            for (int i = xmax; i >= xmin; i--) {
                for (int a = ymax; a >= ymin; a--) {
                    for (int q = zmax; q >= zmin; q--) {
                        if (e[i][a][q].getElements().size() > 0) {
                            ArrayList<Item> elements = e[i][a][q].getElements();
                            Iterator<Item> it = elements.iterator();
                            while (it.hasNext()) {
                                Prism prism = it.next().getPrism();
                                if (!hs.contains(prism) && p.intersects(prism)) {
                                    hs.add(prism);
                                }
                            }
                        }
                    }
                }
            }
        }
        return hs;
    }

    /**
	 * removes an element from its current location
	 * @param p the element to be removed
	 */
    public void removeElement(Prism p, int id) {
        removeElement(p, p.getLocation(), id);
    }

    /**
	 * removes an element at an old location
	 * @param p the element
	 * @param oldLocation the old location of the element
	 */
    private void removeElement(Prism p, Location oldLocation, int id) {
        double xoffset = getxOffset();
        int xmin = (int) ((oldLocation.x - p.getWidth() / 2 + xoffset) / partitionSize);
        int xmax = (int) ((oldLocation.x + p.getWidth() / 2 + xoffset) / partitionSize);
        double yoffset = getyOffset();
        int ymin = (int) ((oldLocation.y - p.getHeight() / 2 + yoffset) / partitionSize);
        int ymax = (int) ((oldLocation.y + p.getHeight() / 2 + yoffset) / partitionSize);
        double zoffset = getzOffset();
        int zmin = (int) ((oldLocation.z - p.getDepth() / 2 + zoffset) / partitionSize);
        int zmax = (int) ((oldLocation.z + p.getDepth() / 2 + zoffset) / partitionSize);
        if (xmax >= e.length) {
            xmax = e.length - 1;
        }
        if (ymax >= e[0].length) {
            ymax = e[0].length - 1;
        }
        if (zmax >= e[0][0].length) {
            zmax = e[0][0].length - 1;
        }
        if (xmin < 0) {
            xmin = 0;
        }
        if (ymin < 0) {
            ymin = 0;
        }
        if (zmin < 0) {
            zmin = 0;
        }
        for (int i = xmax; i >= xmin; i--) {
            for (int a = ymax; a >= ymin; a--) {
                for (int q = zmax; q >= zmin; q--) {
                    Iterator<Item> it = e[i][a][q].getElements().iterator();
                    while (it.hasNext()) {
                        Item item = it.next();
                        if (item.getID() == id) {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    /**
	 * gets the height
	 * @return returns the height of the map
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * sets the partitions size, adjusts the map afterwards to accomodate changes
	 * @param setter the new partition size
	 */
    public void setPartitionSize(int setter) {
        partitionSize = setter;
        adjustMap();
    }

    /**
	 * sets the map's size
	 * @param width the new width of the map
	 * @param height the new height of the map
	 */
    public void setSize(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        adjustMap();
    }

    private void adjustMap() {
        ArrayList<Item> l = null;
        if (e != null) {
            l = getItems();
        }
        e = new Partition[(width / partitionSize) + 1][(height / partitionSize) + 1][(depth / partitionSize) + 1];
        for (int i = (width / partitionSize); i >= 0; i--) {
            for (int a = (height / partitionSize); a >= 0; a--) {
                for (int q = (depth / partitionSize); q >= 0; q--) {
                    e[i][a][q] = new Partition();
                }
            }
        }
        if (l != null) {
            for (int i = l.size() - 1; i >= 0; i--) {
                addElement(l.get(i));
            }
        }
    }

    private void addElement(Item i) {
        addElement(i.getPrism(), i.getID());
    }

    /**
	 * adds an element to the map, puts it in the proper partition automatically,
	 * if the element is large enough it will be put into multiple partitions
	 * @param element the element to be added
	 */
    public void addElement(Prism p, int id) {
        if (inMap(p.getLocation())) {
            double xoffset = getxOffset();
            int xmin = (int) ((p.getLocation().x - p.getWidth() / 2 + xoffset) / partitionSize);
            int xmax = (int) ((p.getLocation().x + p.getWidth() / 2 + xoffset) / partitionSize);
            double yoffset = getyOffset();
            int ymin = (int) ((p.getLocation().y - p.getHeight() / 2 + yoffset) / partitionSize);
            int ymax = (int) ((p.getLocation().y + p.getHeight() / 2 + yoffset) / partitionSize);
            double zoffset = getzOffset();
            int zmin = (int) ((p.getLocation().z - p.getDepth() / 2 + zoffset) / partitionSize);
            int zmax = (int) ((p.getLocation().z + p.getDepth() / 2 + zoffset) / partitionSize);
            Item item = new Item(p, id);
            for (int i = xmax; i >= xmin; i--) {
                for (int a = ymax; a >= ymin; a--) {
                    for (int q = zmax; q >= zmin; q--) {
                        try {
                            e[i][a][q].addElement(item);
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
            }
        } else {
        }
    }

    /**
	 * writes the map to standard output
	 */
    public void printMap() {
        int total = 0;
        for (int y = e[0].length - 1; y >= 0; y--) {
            System.out.print("| ");
            for (int x = e.length - 1; x >= 0; x--) {
                for (int z = e[0][0].length - 1; z >= 0; z--) {
                    total += e[x][y][z].getElements().size();
                    System.out.print(e[x][y][z].getElements().size() + " | ");
                }
                System.out.println("\n---------------------------");
            }
            System.out.println("=======================================");
        }
        System.out.println("total elements = " + total);
        System.out.println("total unique elements = " + getElements().size());
        System.out.println("done");
    }

    /**
	 * determines the x offset needed to correct for the center of the map
	 * @return returns the distance until the left most point as defined by center.x-width/2 is 0
	 */
    private double getxOffset() {
        return -(center.x - width / 2);
    }

    /**
	 * determines the y offset needed to correct for the center of the map
	 * @return returns the distance until the bottom most point as defined by center.y-height/2 is 0
	 */
    private double getyOffset() {
        return -(center.y - height / 2);
    }

    /**
	 * determines the z offset needed to correct for the center of the map
	 * @return returns the distance until the back most point as defined by center.z-depth/2 is 0
	 */
    private double getzOffset() {
        return -(center.z - depth / 2);
    }

    /**
	 * checks to see if a given location resides in the map
	 * @param l the location to be tested
	 * @return returns whether or not the location passed is in the map
	 */
    public boolean inMap(Location l) {
        Prism p = new Prism(center, width, height, depth);
        return p.contains(l);
    }

    /**
	 * gets all the maps elements
	 * @return returns all elements in the map
	 */
    public ArrayList<Prism> getElements() {
        HashSet<Prism> hs = new HashSet<Prism>();
        for (int i = e.length - 1; i >= 0; i--) {
            for (int a = e[i].length - 1; a >= 0; a--) {
                for (int q = e[i][a].length - 1; q >= 0; q--) {
                    Iterator<Item> it = e[i][a][q].getElements().iterator();
                    while (it.hasNext()) {
                        hs.add(it.next().getPrism());
                    }
                }
            }
        }
        return new ArrayList<Prism>(hs);
    }

    /**
	 * gets all the items stored in the map
	 * @return
	 */
    private ArrayList<Item> getItems() {
        HashSet<Item> hs = new HashSet<Item>();
        for (int i = e.length - 1; i >= 0; i--) {
            for (int a = e[i].length - 1; a >= 0; a--) {
                for (int q = e[i][a].length - 1; q >= 0; q--) {
                    hs.addAll(e[i][a][q].getElements());
                }
            }
        }
        return new ArrayList<Item>(hs);
    }

    /**
	 * gets all the elements in range
	 * @param l the location to start from
	 * @param range the maximum distance to the region's center
	 * from the starting location
	 * @return returns a hash set of all the regions in range of the
	 * starting location
	 */
    public HashSet<Prism> getElementsInRange(Location l, double range) {
        HashSet<Prism> hs = new HashSet<Prism>();
        double xoffset = getxOffset();
        int xmin = (int) ((l.x - range / 2 + xoffset) / partitionSize);
        int xmax = (int) ((l.x + range / 2 + xoffset) / partitionSize);
        double yoffset = getyOffset();
        int ymin = (int) ((l.y - range / 2 + yoffset) / partitionSize);
        int ymax = (int) ((l.y + range / 2 + yoffset) / partitionSize);
        double zoffset = getzOffset();
        int zmin = (int) ((l.z - range / 2 + zoffset) / partitionSize);
        int zmax = (int) ((l.z + range / 2 + zoffset) / partitionSize);
        if (xmax >= e.length) {
            xmax = e.length - 1;
        }
        if (ymax >= e[0].length) {
            ymax = e[0].length - 1;
        }
        if (zmax >= e[0][0].length) {
            zmax = e[0][0].length - 1;
        }
        if (xmin < 0) {
            xmin = 0;
        }
        if (ymin < 0) {
            ymin = 0;
        }
        if (zmin < 0) {
            zmin = 0;
        }
        for (int i = xmax; i >= xmin; i--) {
            for (int a = ymax; a >= ymin; a--) {
                for (int q = zmax; q >= zmin; q--) {
                    if (e[i][a][q].getElements().size() > 0) {
                        Iterator<Item> it = e[i][a][q].getElements().iterator();
                        while (it.hasNext()) {
                            Prism p = it.next().getPrism();
                            if (!hs.contains(p) && p.getLocation().distanceTo(l) <= range) {
                                hs.add(p);
                            }
                        }
                    }
                }
            }
        }
        return hs;
    }

    /**
	 * returns the partition size of this map
	 * @return
	 */
    public int getPartitionSize() {
        return partitionSize;
    }

    /**
	 * draws the map for diagnostic purposes
	 * @param gl
	 */
    public void drawMap(GL gl, GLCamera c) {
        gl.glPushMatrix();
        gl.glColor3d(0, 0, 0);
        gl.glScaled(5, 5, 5);
        gl.glTranslated(-e.length / 2, -e[0].length / 2, -e[0][0].length / 2);
        gl.glBegin(GL.GL_LINES);
        for (int x = e.length - 1; x >= 0; x--) {
            for (int z = e[0][0].length - 1; z >= 0; z--) {
                gl.glVertex3d(x, 0, z);
                gl.glVertex3d(x, e[0].length - 1, z);
            }
        }
        for (int x = e.length - 1; x >= 0; x--) {
            for (int y = e[0].length - 1; y >= 0; y--) {
                gl.glVertex3d(x, y, 0);
                gl.glVertex3d(x, y, e[0][0].length - 1);
            }
        }
        for (int y = e[0].length - 1; y >= 0; y--) {
            for (int z = e[0][0].length - 1; z >= 0; z--) {
                gl.glVertex3d(0, y, z);
                gl.glVertex3d(e.length - 1, y, z);
            }
        }
        for (int x = e.length - 1; x >= 0; x--) {
            for (int y = e[0].length - 1; y >= 0; y--) {
                for (int z = e[0][0].length - 1; z >= 0; z--) {
                    if (e[x][y][z].getElements().size() != 0) {
                        gl.glColor3d(255, 0, 0);
                        gl.glVertex3d(x, y, z);
                        gl.glVertex3d(x + 1, y, z + 1);
                        gl.glVertex3d(x, y, z + 1);
                        gl.glVertex3d(x + 1, y, z);
                    } else {
                        gl.glColor3d(0, 0, 255);
                        gl.glVertex3d(x, y, z);
                        gl.glVertex3d(x + .1, y, z + .1);
                    }
                }
            }
        }
        gl.glEnd();
        gl.glPopMatrix();
    }
}
