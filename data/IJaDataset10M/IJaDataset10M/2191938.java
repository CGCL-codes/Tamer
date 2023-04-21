package org.openscience.miniJmol;

import org.openscience.jmol.PhysicalProperty;
import java.awt.Graphics;
import java.util.*;
import javax.vecmath.Point3f;

public class ChemFrame {

    /**
     * returns the number of atoms that are currently in the "selected"
     * list for future operations
     */
    public int getNpicked() {
        return napicked;
    }

    /**
     * Toggles on/off the flag that decides whether atoms are shown
     * when displaying a ChemFrame
     */
    public void toggleAtoms() {
        ShowAtoms = !ShowAtoms;
    }

    /**
     * Toggles on/off the flag that decides whether bonds are shown
     * when displaying a ChemFrame
     */
    public void toggleBonds() {
        ShowBonds = !ShowBonds;
    }

    /**
     * Toggles on/off the flag that decides whether Hydrogen atoms are
     * shown when displaying a ChemFrame 
     */
    public void toggleHydrogens() {
        ShowHydrogens = !ShowHydrogens;
    }

    /**
     * Sets the screen scaling factor for zooming.
     *
     * @param ss the screenscale factor
     */
    public void setScreenScale(float ss) {
        ScreenScale = ss;
    }

    /**
     * Set the flag that decides whether atoms are shown
     * when displaying a ChemFrame
     *
     * @param sa the value of the flag
     */
    public void setShowAtoms(boolean sa) {
        ShowAtoms = sa;
    }

    /**
     * Set the flag that decides whether bonds are shown
     * when displaying a ChemFrame
     *
     * @param sb the value of the flag
     */
    public void setShowBonds(boolean sb) {
        ShowBonds = sb;
    }

    /**
     * Set the flag that decides whether Hydrogen atoms are shown
     * when displaying a ChemFrame.  Currently non-functional.
     *
     * @param sh the value of the flag
     */
    public void setShowHydrogens(boolean sh) {
        ShowHydrogens = sh;
    }

    public boolean getShowAtoms() {
        return ShowAtoms;
    }

    public boolean getShowBonds() {
        return ShowBonds;
    }

    public boolean getShowHydrogens() {
        return ShowHydrogens;
    }

    public void setBondFudge(float bf) {
        bondFudge = bf;
    }

    public float getBondFudge() {
        return bondFudge;
    }

    public void setAutoBond(boolean ab) {
        AutoBond = ab;
    }

    public boolean getAutoBond() {
        return AutoBond;
    }

    public void matmult(Matrix3D m) {
        mat.mult(m);
    }

    public void matscale(float xs, float ys, float zs) {
        mat.scale(xs, ys, zs);
    }

    public void mattranslate(float xt, float yt, float zt) {
        mat.translate(xt, yt, zt);
    }

    public void matunit() {
        mat.unit();
    }

    /**
     * Constructor for a ChemFrame with a known number of atoms.
     *
     * @param na the number of atoms in the frame
     */
    public ChemFrame(int na) {
        mat = new Matrix3D();
        mat.xrot(0);
        mat.yrot(0);
        frameProps = new Vector();
        atoms = new Atom[na];
        bufferedAtomZs = new float[atoms.length];
        pickedAtoms = new boolean[atoms.length];
        for (int i = 0; i < pickedAtoms.length; ++i) {
            pickedAtoms[i] = false;
        }
    }

    /**
     * Constructor for a ChemFrame with an unknown number of atoms.
     *
     */
    public ChemFrame() {
        this(100);
    }

    /**
     * returns a Vector containing the list of PhysicalProperty descriptors
     * present in this frame
     */
    public Vector getFrameProps() {
        return frameProps;
    }

    /**
     * Sets the information label for the frame
     *
     * @param info the information label for this frame
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Returns this frame's information label
     */
    public String getInfo() {
        return info;
    }

    /**
     * Adds an atom to the frame and finds all bonds between the 
     * new atom and pre-existing atoms in the frame
     *
     * @param name the name of the extended atom type for the new atom
     * @param x the x coordinate of the new atom
     * @param y the y coordinate of the new atom
     * @param z the z coordinate of the new atom
     */
    public int addAtom(String name, float x, float y, float z) {
        return addAtom(BaseAtomType.get(name), x, y, z);
    }

    /**
     * Adds an atom to the frame and finds all bonds between the 
     * new atom and pre-existing atoms in the frame
     *
     * @param type atom type for the new atom
     * @param x the x coordinate of the new atom
     * @param y the y coordinate of the new atom
     * @param z the z coordinate of the new atom
     */
    public int addAtom(BaseAtomType type, float x, float y, float z) {
        Point3f inputCoordinate = new Point3f(x, y, z);
        if (numberAtoms >= atoms.length) {
            increaseArraySizes(2 * atoms.length);
        }
        atoms[numberAtoms] = new Atom(type, new Point3f(x, y, z), numberAtoms);
        for (int j = 0; j < numberAtoms; j++) {
            if (Atom.closeEnoughToBond(atoms[numberAtoms], atoms[j], bondFudge)) {
                atoms[numberAtoms].addBondedAtom(atoms[j]);
                atoms[j].addBondedAtom(atoms[numberAtoms]);
            }
        }
        return numberAtoms++;
    }

    /**
     * Adds an atom to the frame and finds all bonds between the 
     * new atom and pre-existing atoms in the frame
     *
     * @param atomicNumber the atomicNumber of the extended atom type for the new atom
     * @param x the x coordinate of the new atom
     * @param y the y coordinate of the new atom
     * @param z the z coordinate of the new atom
     */
    public int addAtom(int atomicNumber, float x, float y, float z) {
        BaseAtomType baseType = BaseAtomType.get(atomicNumber);
        if (baseType == null) {
            return -1;
        }
        return addAtom(baseType, x, y, z);
    }

    /**
     * Adds an atom to the frame and finds all bonds between the 
     * new atom and pre-existing atoms in the frame 
     *
     * @param name the name of the extended atom type for the new atom
     * @param x the x coordinate of the new atom
     * @param y the y coordinate of the new atom
     * @param z the z coordinate of the new atom  
     * @param props a Vector containing the properties of this atom
     */
    public int addAtom(String name, float x, float y, float z, Vector props) {
        int i = addAtom(name, x, y, z);
        atoms[i].setProperties(props);
        for (int j = 0; j < props.size(); j++) {
            PhysicalProperty p = (PhysicalProperty) props.elementAt(j);
            String desc = p.getDescriptor();
            if (frameProps.indexOf(desc) < 0) {
                frameProps.addElement(desc);
            }
        }
        return i;
    }

    /**
     * Returns the number of atoms in the ChemFrame
     */
    public int getNumberAtoms() {
        return numberAtoms;
    }

    /**
     * Returns the properties of an atom.
     * 
     * @param i the index of the atom
     */
    public Vector getAtomProperties(int i) {
        return atoms[i].getProperties();
    }

    /**
     * Transform all the points in this model
     */
    public void transform() {
        if (numberAtoms > 0) {
            for (int i = 0; i < numberAtoms; ++i) {
                bufferedAtomZs[i] = atoms[i].transform(mat);
            }
        }
    }

    /**
     * Paint this model to a graphics context.  It uses the matrix
     * associated with this model to map from model space to screen
     * space.  
     *
     * @param g the Graphics context to paint to
    * @param settings the display settings
     */
    public synchronized void paint(Graphics g, DisplaySettings settings) {
        if (atoms == null || numberAtoms <= 0) {
            return;
        }
        transform();
        if (!settings.getFastRendering()) {
            sortAtoms();
        }
        for (int i = 0; i < numberAtoms; i++) {
            int j = zSortedAtomIndicies[i];
            Enumeration bondIter = atoms[j].getBondedAtoms();
            while (bondIter.hasMoreElements()) {
                bondRenderer.paint(g, atoms[j], (Atom) bondIter.nextElement(), settings);
            }
            if (ShowAtoms && !settings.getFastRendering()) {
                atomRenderer.paint(g, atoms[j], pickedAtoms[j], settings);
            }
        }
    }

    /**
     * Add all atoms in this frame to the list of picked atoms
     */
    public void selectAll() {
        if (numberAtoms <= 0) return;
        napicked = 0;
        for (int i = 0; i < numberAtoms; i++) {
            pickedAtoms[i] = true;
            napicked++;
        }
    }

    /**
     * Remove all atoms in this frame from the list of picked atoms
     */
    public void deselectAll() {
        if (numberAtoms <= 0) return;
        for (int i = 0; i < numberAtoms; i++) {
            pickedAtoms[i] = false;
        }
        napicked = 0;
    }

    public int pickMeasuredAtom(int x, int y) {
        return getNearestAtom(x, y);
    }

    /**
     * Clear out the list of picked atoms, find the nearest atom to a
     * set of screen coordinates and add this new atom to the picked
     * list.
     *
     * @param x the screen x coordinate of the selection point
     * @param y the screen y coordinate of the selection point
     */
    public void selectAtom(int x, int y) {
        int smallest = getNearestAtom(x, y);
        if (pickedAtoms[smallest]) {
            pickedAtoms[smallest] = false;
            napicked = 0;
        } else {
            pickedAtoms[smallest] = true;
            napicked = 1;
        }
        for (int i = 0; i < numberAtoms; i++) {
            if (i != smallest) pickedAtoms[i] = false;
        }
    }

    /**
     * Find the nearest atom to a set of screen coordinates and add
     * this new atom to the picked list.
     *
     * @param x the screen x coordinate of the selection point
     * @param y the screen y coordinate of the selection point 
     */
    public void shiftSelectAtom(int x, int y) {
        int smallest = getNearestAtom(x, y);
        if (pickedAtoms[smallest]) {
            pickedAtoms[smallest] = false;
            napicked--;
        } else {
            pickedAtoms[smallest] = true;
            napicked++;
        }
    }

    /**
     * Clear out the list of picked atoms, find all atoms within
     * designated region and add these atoms to the picked list.
     *
     * @param x1 the x coordinate of point 1 of the region's bounding rectangle
     * @param y1 the y coordinate of point 1 of the region's bounding rectangle
     * @param x2 the x coordinate of point 2 of the region's bounding rectangle
     * @param y2 the y coordinate of point 2 of the region's bounding rectangle
     */
    public void selectRegion(int x1, int y1, int x2, int y2) {
        if (numberAtoms <= 0) {
            return;
        }
        transform();
        napicked = 0;
        for (int i = 0; i < numberAtoms; i++) {
            if (isAtomInRegion(i, x1, y1, x2, y2)) {
                pickedAtoms[i] = true;
                napicked++;
            } else {
                pickedAtoms[i] = false;
            }
        }
    }

    /**
     * Find all atoms within designated region and add these atoms to
     * the picked list.
     *
     * @param x1 the x coordinate of point 1 of the region's bounding rectangle
     * @param y1 the y coordinate of point 1 of the region's bounding rectangle
     * @param x2 the x coordinate of point 2 of the region's bounding rectangle
     * @param y2 the y coordinate of point 2 of the region's bounding rectangle
     */
    public void shiftSelectRegion(int x1, int y1, int x2, int y2) {
        if (numberAtoms <= 0) {
            return;
        }
        transform();
        for (int i = 0; i < numberAtoms; i++) {
            if (isAtomInRegion(i, x1, y1, x2, y2)) {
                if (!pickedAtoms[i]) {
                    pickedAtoms[i] = true;
                    napicked++;
                }
            }
        }
    }

    private boolean isAtomInRegion(int n, int x1, int y1, int x2, int y2) {
        int x = (int) atoms[n].getScreenPosition().x;
        int y = (int) atoms[n].getScreenPosition().y;
        if (x > x1 && x < x2) {
            if (y > y1 && y < y2) {
                return true;
            }
        }
        return false;
    }

    private int getNearestAtom(int x, int y) {
        if (numberAtoms <= 0) {
            return -1;
        }
        transform();
        int dx, dy, dr2;
        int smallest = -1;
        int smallr2 = Integer.MAX_VALUE;
        for (int i = 0; i < numberAtoms; i++) {
            dx = (int) atoms[i].getScreenPosition().x - x;
            dy = (int) atoms[i].getScreenPosition().y - y;
            dr2 = dx * dx + dy * dy;
            if (dr2 < smallr2) {
                smallest = i;
                smallr2 = dr2;
            }
        }
        if (smallest >= 0) {
            return smallest;
        }
        return -1;
    }

    /**
     * Find the bounds of this model.
     */
    public void findBounds() {
        if (atoms == null || numberAtoms <= 0) {
            return;
        }
        min = new Point3f(atoms[0].getPosition());
        max = new Point3f(min);
        for (int i = 1; i < numberAtoms; ++i) {
            float x = atoms[i].getPosition().x;
            if (x < min.x) min.x = x;
            if (x > max.x) max.x = x;
            float y = atoms[i].getPosition().y;
            if (y < min.y) min.y = y;
            if (y > max.y) max.y = y;
            float z = atoms[i].getPosition().z;
            if (z < min.z) min.z = z;
            if (z > max.z) max.z = z;
        }
    }

    public Point3f getMinimumBounds() {
        return min;
    }

    public Point3f getMaximumBounds() {
        return max;
    }

    /**
     * Walk through this frame and find all bonds again.
     */
    public void rebond() {
        for (int i = 0; i < numberAtoms; i++) {
            atoms[i].clearBondedAtoms();
        }
        for (int i = 0; i < numberAtoms - 1; i++) {
            for (int j = i; j < numberAtoms; j++) {
                if (Atom.closeEnoughToBond(atoms[i], atoms[j], bondFudge)) {
                    atoms[i].addBondedAtom(atoms[j]);
                    atoms[j].addBondedAtom(atoms[i]);
                }
            }
        }
    }

    private void increaseArraySizes(int newArraySize) {
        Atom newAtoms[] = new Atom[newArraySize];
        System.arraycopy(atoms, 0, newAtoms, 0, atoms.length);
        atoms = newAtoms;
        float newAtomZs[] = new float[newArraySize];
        System.arraycopy(bufferedAtomZs, 0, newAtomZs, 0, bufferedAtomZs.length);
        bufferedAtomZs = newAtomZs;
        boolean np[] = new boolean[newArraySize];
        System.arraycopy(pickedAtoms, 0, np, 0, pickedAtoms.length);
        pickedAtoms = np;
    }

    private void sortAtoms() {
        if (zSortedAtomIndicies == null || zSortedAtomIndicies.length != numberAtoms) {
            zSortedAtomIndicies = new int[numberAtoms];
            for (int i = 0; i < numberAtoms; ++i) {
                zSortedAtomIndicies[i] = i;
            }
        }
        for (int i = numberAtoms - 1; --i >= 0; ) {
            boolean flipped = false;
            for (int j = 0; j <= i; j++) {
                int a = zSortedAtomIndicies[j];
                int b = zSortedAtomIndicies[j + 1];
                if (bufferedAtomZs[a] > bufferedAtomZs[b]) {
                    zSortedAtomIndicies[j + 1] = a;
                    zSortedAtomIndicies[j] = b;
                    flipped = true;
                }
            }
            if (!flipped) {
                break;
            }
        }
    }

    private float bondFudge = 1.12f;

    private float ScreenScale;

    private boolean AutoBond = true;

    private boolean ShowBonds = true;

    private boolean ShowAtoms = true;

    private boolean ShowHydrogens = true;

    private Matrix3D mat;

    private boolean[] pickedAtoms;

    private int napicked;

    /**
     * Information string for this frame.
     */
    String info;

    /**
     * Array of atoms.
     */
    private Atom[] atoms;

    /**
     * Number of atoms in frame.
     */
    private int numberAtoms = 0;

    /**
     * Z coordinates of the atoms, buffered for quick access.
     */
    private float[] bufferedAtomZs;

    /**
     * Array of atom indicies sorted by Z coordinate.
     */
    private int[] zSortedAtomIndicies;

    /**
     * Renderer for atoms.
     */
    private AtomRenderer atomRenderer = new AtomRenderer();

    /**
     * Renderer for bonds.
     */
    private BondRenderer bondRenderer = new BondRenderer();

    /**
     * List of all atom properties contained by atoms in this frame.
     */
    private Vector frameProps;

    private Point3f min;

    private Point3f max;
}
