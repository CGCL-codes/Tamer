package joelib.gui.molviewer.java3d.molecule;

import java.util.Hashtable;
import joelib.molecule.JOEAtom;
import joelib.molecule.JOEBond;
import joelib.molecule.JOEMol;
import joelib.molecule.types.AtomPropertyColoring;
import joelib.util.iterator.AtomIterator;
import joelib.util.iterator.BondIterator;

/**
 * Molecule class for Java3D viewer.
 *
 * @author     wegnerj
 * @license    GPL
 * @cvsversion    $Revision: 1.8 $, $Date: 2004/08/29 14:10:10 $
 */
public class ViewerMolecule {

    public Hashtable pickAtomMapping = new Hashtable();

    public Hashtable pickBondMapping = new Hashtable();

    /**
     * Atom id to index map.  Does not require atom ids to start at 1 or be
     *sequential.
     */
    protected Hashtable atomIdToIndex;

    protected JOEMol mol;

    /**
     * The transformation matrix
     */
    protected Matrix3D mat;

    /**
     * List of highlighted atom
     */
    protected ViewerAtoms highlightAtoms = new ViewerAtoms(10);

    /**
     * Atom list
     */
    protected ViewerAtoms myAtoms;

    /**
     * List of selected atoms
     */
    protected ViewerAtoms selectAtoms = new ViewerAtoms(10);

    /**
     * Bond list
     */
    protected ViewerBonds myBonds;

    /**
     * The bounding extents of the molecule
     */
    protected float xmax;

    /**
     * The bounding extents of the molecule
     */
    protected float xmin;

    /**
     * The bounding extents of the molecule
     */
    protected float ymax;

    /**
     * The bounding extents of the molecule
     */
    protected float ymin;

    /**
     * The bounding extents of the molecule
     */
    protected float zmax;

    /**
     * The bounding extents of the molecule
     */
    protected float zmin;

    /**
     * Molecule id
     */
    protected int id;

    /**
     * Number of atoms
     *
     */
    protected int numAtoms;

    /**
     * Number of bonds
     */
    protected int numBonds;

    private AtomPropertyColoring aPropColoring = new AtomPropertyColoring();

    /**
     * Default constructor
     */
    public ViewerMolecule(JOEMol _mol) {
        mol = _mol;
        this.id = id;
        myAtoms = new ViewerAtoms(mol.numAtoms(), 30);
        atomIdToIndex = new Hashtable(mol.numAtoms());
        myBonds = new ViewerBonds(mol.numBonds(), 30);
        numAtoms = numBonds = 0;
        mat = new Matrix3D();
        xmax = xmin = ymax = ymin = zmax = zmin = 0.0f;
        ViewerAtom a;
        JOEAtom atom;
        AtomIterator ait = mol.atomIterator();
        while (ait.hasNext()) {
            atom = ait.nextAtom();
            a = new ViewerAtom(this, atom);
            this.addAtom(a);
        }
        BondIterator bit = mol.bondIterator();
        JOEBond bond;
        while (bit.hasNext()) {
            bond = bit.nextBond();
            int from = bond.getBeginAtomIdx();
            int to = bond.getEndAtomIdx();
            ViewerAtom a1 = myAtoms.getAtom(from - 1);
            ViewerAtom a2 = myAtoms.getAtom(to - 1);
            ViewerBond b = new ViewerBond(this, bond, a1, a2);
            this.addBond(b);
        }
    }

    /**
     * Return an atom in this molecule whose id matches the input id
     *
     * @param atomId  id of queried atom
     * @return        The atomFromId value
     */
    public ViewerAtom getAtomFromId(int atomId) {
        ViewerAtom a;
        for (int i = 0; i < myAtoms.size(); i++) {
            a = myAtoms.getAtom(i);
            if (atomId == a.getId()) {
                return a;
            }
        }
        return null;
    }

    /**
     * Set atomIdToIndex
     *
     * @param index  atomIdToIndex
     */
    public void setAtomIdToIndex(Hashtable index) {
        atomIdToIndex = index;
    }

    /**
     * Return atomIdToIndex
     *
     * @return   The atomIdToIndex value
     */
    public Hashtable getAtomIdToIndex() {
        return atomIdToIndex;
    }

    public AtomPropertyColoring getAtomPropertyColoring() {
        return aPropColoring;
    }

    /**
     * Return a bond in this molecule whose id matches the input id
     *
     * @param bondId  id of queried bond
     * @return        The bondFromId value
     */
    public ViewerBond getBondFromId(int bondId) {
        ViewerBond b;
        for (int i = 0; i < myBonds.size(); i++) {
            b = myBonds.getBond(i);
            if (bondId == b.getId()) {
                return b;
            }
        }
        return null;
    }

    /**
     * Set highlighted atoms vector
     *
     * @param av  atom vector
     */
    public void setHighlightAtoms(ViewerAtoms av) {
        highlightAtoms = av;
    }

    /**
     * Return the highlighted atoms vector
     *
     * @return   The highlightAtoms value
     */
    public ViewerAtoms getHighlightAtoms() {
        return highlightAtoms;
    }

    /**
     * Set id of this molecule
     *
     * @param id  molecule id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Draw the molecule
     * recommend to avoid, but to use renderer approach
     *
     * Returns id of this molecule
     *
     * @return   The id value
     */
    public int getId() {
        return id;
    }

    public JOEMol getJOEMol() {
        return mol;
    }

    /**
     * Set property table
     *
     * Set 3D transformation matrix
     *
     * @param mat   The new matrix3D value
     */
    public void setMatrix3D(Matrix3D mat) {
        this.mat = mat;
    }

    /**
     * Return 3D transformation matrix
     *
     * @return   The matrix3D value
     */
    public Matrix3D getMatrix3D() {
        return mat;
    }

    /**
     * Set atom vector
     *
     * @param atoms  The new myAtoms value
     */
    public void setMyAtoms(ViewerAtoms atoms) {
        myAtoms = atoms;
    }

    /**
     * Returns atom vector of this molecule
     *
     * @return   The myAtoms value
     */
    public ViewerAtoms getMyAtoms() {
        return myAtoms;
    }

    /**
     * Set bond vector
     *
     * @param bv  bond vector
     */
    public void setMyBonds(ViewerBonds bv) {
        myBonds = bv;
    }

    /**
     * Return bond vector of this molecule
     *
     * @return   The myBonds value
     */
    public ViewerBonds getMyBonds() {
        return myBonds;
    }

    /**
     * Returns name of molecule
     *
     * @return   The name value
     */
    public String getName() {
        return mol.getTitle();
    }

    /**
     * Set ring vector
     *
     * Set number of atoms
     *
     * @param numAtoms  The new numAtoms value
     */
    public void setNumAtoms(int numAtoms) {
        this.numAtoms = numAtoms;
    }

    /**
     * Return ring vector of this molecule
     *
     * Return number of atoms in this molecule
     *
     * @return   The numAtoms value
     */
    public int getNumAtoms() {
        return myAtoms.size();
    }

    /**
     * Return number of bonds in this molecule
     *
     * @return   The numBonds value
     */
    public int getNumBonds() {
        return myBonds.size();
    }

    /**
     * Set selected atoms vector
     *
     * @param av  atom vector
     */
    public void setSelectAtoms(ViewerAtoms av) {
        selectAtoms = av;
    }

    /**
     * Return the selected atoms vector
     *
     * @return   The selectAtoms value
     */
    public ViewerAtoms getSelectAtoms() {
        return selectAtoms;
    }

    /**
     * Set maxmum of X-coordinate
     *
     * @param xmax  maximum X-coordinate
     */
    public void setXmax(float xmax) {
        this.xmax = xmax;
    }

    /**
     * Return the maxmum x coordinate
     *
     * @return   The xmax value
     */
    public float getXmax() {
        return xmax;
    }

    /**
     * Set minimum of X-coordinate of this molecule
     *
     * @param xmin  The new xmin value
     */
    public void setXmin(float xmin) {
        this.xmin = xmin;
    }

    /**
     * Return the minimum x coordinate of this molelcule
     *
     * @return   The xmin value
     */
    public float getXmin() {
        return xmin;
    }

    /**
     * Set maxmum of Y-coordinate
     *
     * @param ymax  maximum Y-coordinate
     */
    public void setYmax(float ymax) {
        this.ymax = ymax;
    }

    /**
     * Return the maxmum y coordinate
     *
     * @return   The ymax value
     */
    public float getYmax() {
        return ymax;
    }

    /**
     * Set minimum of Y-coordinate
     *
     * @param ymin  minimum Y-coordinate
     */
    public void setYmin(float ymin) {
        this.ymin = ymin;
    }

    /**
     * Return the minimum y coordinate
     *
     * @return   The ymin value
     */
    public float getYmin() {
        return ymin;
    }

    /**
     * Set maxmum of Z-coordinate
     *
     * @param zmax  maximum Z-coordinate
     */
    public void setZmax(float zmax) {
        this.zmax = zmax;
    }

    /**
     * Return the maxmum z coordinate
     *
     * @return   The zmax value
     */
    public float getZmax() {
        return zmax;
    }

    /**
     * Set minimum of Z-coordinate
     *
     * @param zmin  The new zmin value
     */
    public void setZmin(float zmin) {
        this.zmin = zmin;
    }

    /**
     * Return the minimum z coordinate
     *
     * @return   The zmin value
     */
    public float getZmin() {
        return zmin;
    }

    /**
     * Add an atom to this molecule
     *
     * @param a  atom to be added
     */
    public void addAtom(ViewerAtom a) {
        myAtoms.append(a);
        atomIdToIndex.put(new Integer(a.getId()), new Integer(numAtoms));
        numAtoms++;
    }

    /**
     * Add a bond to this molecule
     *
     * @param b  bond to be added
     */
    public void addBond(ViewerBond b) {
        myBonds.append(b);
        numBonds++;
    }

    /**
     * Calculates 2D vector perpendicular to bond
     *
     * @param x1   Description of the Parameter
     * @param y1   Description of the Parameter
     * @param x2   Description of the Parameter
     * @param y2   Description of the Parameter
     * @param pex  Description of the Parameter
     * @param pey  Description of the Parameter
     */
    public void calcPerpUnitVec(float x1, float y1, float x2, float y2, float[] pex, float[] pey) {
        float len = (float) java.lang.Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
        float ex = (x2 - x1) / len;
        float ey = (y2 - y1) / len;
        pex[0] = -1.0f * ey;
        pey[0] = ex;
    }

    public void clear() {
        myAtoms.clear();
        myBonds.clear();
        atomIdToIndex.clear();
        pickBondMapping.clear();
        pickAtomMapping.clear();
    }

    /**
     * Return the Renderer which draws this molecule
     *
     * Return true iff the atom in the parameter is one of the atoms in this molecule
     *
     * @param a  Description of the Parameter
     * @return   Description of the Return Value
     */
    public boolean contains(ViewerAtom a) {
        return myAtoms.contains(a);
    }

    /**
     * Return true iff the bond in the parameter is one of the bonds in this molecule
     *
     * @param b  bond in this query
     * @return   Description of the Return Value
     */
    public boolean contains(ViewerBond b) {
        return myBonds.contains(b);
    }

    /**
     * Dehighlighted all highlighted atoms.
     */
    public void dehighlight() {
        if (highlightAtoms != null) {
            for (int i = 0; i < highlightAtoms.size(); i++) {
                ViewerAtom a = highlightAtoms.getAtom(i);
                a.highlight = false;
            }
            highlightAtoms = new ViewerAtoms(10);
        }
    }

    /**
     * Deselects all <tt>selected</tt> atoms
     *
     */
    public void deselect() {
        if (selectAtoms != null) {
            for (int i = 0; i < selectAtoms.size(); i++) {
                ViewerAtom a = selectAtoms.getAtom(i);
                a.select = false;
            }
        }
        selectAtoms = new ViewerAtoms(10);
    }

    /**
     * Compares this molecule with another
     *
     * @param jmol  molecule to compare with
     * @return      <tt>true</tt> if they are equal, else <tt>false</tt>
     */
    public boolean equals(Object obj) {
        if (obj instanceof ViewerMolecule && (obj != null)) {
            if (((ViewerMolecule) obj).mol.equals(mol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns closest atom to point within FINDRADIUS, <tt>null</tt> if nothing is found,
     *
     * @param x           Description of the Parameter
     * @param y           Description of the Parameter
     * @param z           Description of the Parameter
     * @param FINDRADIUS  Description of the Parameter
     * @return            Description of the Return Value
     */
    public ViewerAtom findAtom(float x, float y, float z, float FINDRADIUS) {
        int result = -1;
        float minDist = 1000000000.0f;
        for (int i = 0; i < numAtoms; i++) {
            ViewerAtom a = myAtoms.getAtom(i);
            float dist = ((a.tx - x) * (a.tx - x)) + ((a.ty - y) * (a.ty - y));
            if ((dist < FINDRADIUS) && (dist < minDist)) {
                minDist = dist;
                result = i;
            }
        }
        if (result == -1) {
            return null;
        } else {
            return myAtoms.getAtom(result);
        }
    }

    /**
     * Determines bounding box of a molecule (sets xmax,xmin,ymax,ymin,zmax,zmin)
     */
    public void findBB() {
        if (numAtoms == 0) {
            xmin = xmax = ymin = ymax = zmin = zmax = 0;
            return;
        }
        ViewerAtom a = myAtoms.getAtom(0);
        xmin = xmax = a.getX();
        ymin = ymax = a.getY();
        zmin = zmax = a.getZ();
        for (int i = 1; i < numAtoms; i++) {
            a = myAtoms.getAtom(i);
            if (a.getX() < xmin) {
                xmin = a.getX();
            }
            if (a.getX() > xmax) {
                xmax = a.getX();
            }
            if (a.getY() < ymin) {
                ymin = a.getY();
            }
            if (a.getY() > ymax) {
                ymax = a.getY();
            }
            if (a.getZ() < zmin) {
                zmin = a.getZ();
            }
            if (a.getZ() > zmax) {
                zmax = a.getZ();
            }
        }
    }

    /**
     * Returns <tt>true</tt> if molecule has <tt>selected</tt> atoms
     *
     * @return   Description of the Return Value
     */
    public boolean hasSelectedAtoms() {
        return (selectAtoms.size() > 0);
    }

    public int hashCode() {
        if (mol == null) {
            return 0;
        } else {
            return mol.hashCode();
        }
    }

    /**
     * Designates an atom as <tt>highlighted</tt>.  The specified atom gets
     * appended to the current highlight list.
     *
     * @param a  atom to highlight
     */
    public void highlight(ViewerAtom a) {
        if ((a == null) || highlightAtoms.contains(a)) {
            return;
        }
        highlightAtoms.append(a);
        a.highlight = true;
    }

    /**
     * Designates an atom list as <tt>highlighted</tt>.  The new list replaces
     * any current list of highlighted atoms.
     *
     * @param av  list (vector) of atoms to highlight
     */
    public void highlight(ViewerAtoms av) {
        if (av == null) {
            return;
        }
        if (highlightAtoms != null) {
            for (int i = 0; i < highlightAtoms.size(); i++) {
                ViewerAtom a = highlightAtoms.getAtom(i);
                a.highlight = false;
            }
        }
        highlightAtoms = new ViewerAtoms(10);
        for (int i = 0; i < av.size(); i++) {
            ViewerAtom a = av.getAtom(i);
            if (a != null) {
                highlightAtoms.append(a);
                a.highlight = true;
            }
        }
    }

    /**
     * Flags specified atom as <tt>selected</tt>
     *
     * @param a  atom to be selected
     */
    public void select(ViewerAtom a) {
        if ((a == null) || a.select) {
            return;
        }
        a.select = true;
        selectAtoms.append(a);
    }

    /**
     * Returns <tt>true</tt> if molecule has <tt>highlighted</tt> atoms
     *
     * @return   Description of the Return Value
     */
    protected boolean hasHighlightedAtoms() {
        return (highlightAtoms.size() > 0);
    }
}
