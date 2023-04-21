package structure.grid;

import structure.constants.Constants;
import structure.math.Point3f;
import structure.math.Point3i;
import structure.matter.Atom;

/**
 * Class for representing single grid cells that constitute a Grid object.
 * @author Abdullah Kahraman
 * @version 0.1
 * @since 0.1
 */
public class GridCell {

    /**
     * Length of the grid cell edge.
     * Default {@code size = 0.5}.
     */
    private float size = Constants.DEFAULT_GRID_CELL_SIZE;

    /**
     * Diagonal of the grid cell.
     * Default {@code diagonal = Math.sqrt(0.5)}
     */
    private float diagonal = (float) Math.sqrt(2 * Math.pow(size, 2));

    /**
     * Cartesian coordinates of the grid cell center.
     */
    private Point3f xyzCoordinates;

    /**
     * Indices if grid cell is located in a 3D space.
     */
    private Point3i ijkIndices;

    /**
     * Stores boolean information about whether grid cell is occupied by
     * e.g. a molecule.
     * Default is {@code FALSE}.
     */
    private boolean isOccupied = false;

    /**
     * Stores boolean information about whether grid cell lies on the
     * boundary between occupied and unoccupied grid cells.
     * Default is {@code FALSE}.
     */
    private boolean isBoundary = false;

    /**
     * Stores the distance to some reference GridCell object in a grid.
     * Default is {@code Integer.MAX_VALUE}
     */
    private float distance = Constants.DEFAULT_GRID_DISTANCE;

    /**
     * Constructor.
     * @param xyz
     *        - Point3d object with XYZ coordinates of the grid cell.
     * @param edgeLength
     *        - float value representing the edge length of the gridCell cube.
     */
    public GridCell(final Point3f xyz, final float edgeLength) {
        this.setXYZ(xyz);
        this.setSize(edgeLength);
        this.setDistance(Constants.DEFAULT_GRID_DISTANCE);
    }

    /**
     * Sets new Cartesian coordinates for the center of this grid cell.
     * @param xyz
     *        - Point3d object holding the new Cartesian coordinates.
     */
    public final void setXYZ(final Point3f xyz) {
        this.xyzCoordinates = xyz;
    }

    /**
     * Returns the Cartesian coordinates of the cell center of this grid cell.
     * @return Point3d object holding the Cartesian coordinates.
     */
    public final Point3f getXYZ() {
        return this.xyzCoordinates;
    }

    /**
     * Sets new indices for the grid cell.
     * @param ijk
     *        - Point3i object holding the indices.
     */
    public final void setIndices(final Point3i ijk) {
        this.ijkIndices = ijk;
    }

    /**
     * Returns the indices of this grid cell.
     * @return Point3i object holding the indices.
     */
    public final Point3i getIndices() {
        return this.ijkIndices;
    }

    /**
     * Sets the size, i.e. edge length of this grid cell and its diagonal.
     * @param edgeLength
     *        - float value representing the size of the grid cell.
     */
    private void setSize(final float edgeLength) {
        this.size = edgeLength;
        this.diagonal = (float) Math.sqrt(2 * Math.pow(this.size, 2));
    }

    /**
     * Returns the size, i.e. edge length of this grid cell.
     * @return float value representing the size of the grid cell.
     */
    public final float getSize() {
        return this.size;
    }

    /**
     * Returns the diagonal of this grid cell.
     * @return float value representing the diagonal of the grid cell.
     */
    public final float getDiagonalLength() {
        return this.diagonal;
    }

    /**
     * Sets the occupation status of this grid cell to {@code TRUE}.
     */
    public final void setOccupation() {
        this.isOccupied = true;
    }

    /**
     * Sets the occupation status of this grid cell to {@code FALSE}.
     */
    public final void unsetOccupation() {
        this.isOccupied = false;
    }

    /**
     * Returns the occupation status of this grid cell.
     * @return {@code TRUE} if cell has been labeled as occupied,
     *         {@code FALSE} otherwise.
     */
    public final boolean isOccupied() {
        return this.isOccupied;
    }

    /**
     * Sets the boundary status of this grid cell to {@code TRUE}.
     */
    public final void setBoundaryStatus() {
        this.isBoundary = true;
    }

    /**
     * Sets the boundary status of this grid cell to {@code FALSE}.
     */
    public final void unsetBoundaryStatus() {
        this.isBoundary = false;
    }

    /**
     * Returns the boundary status of this grid cell.
     * @return {@code TRUE} if cell has been labeled as lying at the boundary
     *          between occupied and unoccupied cells,
     *         {@code FALSE} otherwise.
     */
    public final boolean isBoundary() {
        return this.isBoundary;
    }

    /**
     * Sets the distance of this grid cell to some reference grid cell in a
     * grid.
     * @param dist
     *        - float value representing the distance.
     * @see #getDistance()
     */
    public final void setDistance(final float dist) {
        this.distance = dist;
    }

    /**
     * Returns the distance of this grid cell to some reference grid cell in
     * a grid.
     * @return float value representing the distance.
     * @see #setDistance(float)
     */
    public final float getDistance() {
        return this.distance;
    }

    /**
     * Resets the distance value and occupied status of this grid cells.
     * @see #resetSoft()
     */
    public final void reset() {
        this.setDistance(Constants.DEFAULT_GRID_DISTANCE);
        this.unsetOccupation();
    }

    /**
     * Resets only the distance value of this grid cells, but not the occupied
     * status.
     * @see #reset()
     */
    public final void resetSoft() {
        this.setDistance(Constants.DEFAULT_GRID_DISTANCE);
    }

    /**
     * Creates a copy of this GridCell object.
     * @return A new copy of this GridCell object.
     */
    public final GridCell copy() {
        GridCell copy = new GridCell(this.getXYZ().copy(), this.getSize());
        if (this.getIndices() != null) {
            copy.setIndices(this.getIndices().copy());
        }
        copy.setDistance(this.distance);
        if (this.isOccupied()) {
            copy.setOccupation();
        }
        if (this.isBoundary()) {
            copy.setBoundaryStatus();
        }
        return copy;
    }

    /**
     * Checks whether a second GridCell object is the same as this GridCell
     * object.
     * @param cell
     *        - GridCell object to be compared to this GridCell object.
     * @return {@code TRUE} if both GridCell objects have the same XYZ
     *         Cartesian coordinates, IJK indices and sizes, {@code FALSE}
     *         otherwise.
     */
    public final boolean equals(final GridCell cell) {
        return this.getXYZ().equals(cell.getXYZ()) && this.getIndices().equals(cell.getIndices()) && this.getSize() == cell.getSize() ? true : false;
    }

    /**
     * Returns the grid cell as an Atom Object.
     * @return Atom object with chainID=Y or N for occupied or unoccupied grid
     *         cells respectively and with distance information in the
     *         temperature factor column.
     */
    public final Atom toAtom() {
        Atom atom = new Atom();
        float maxTempFactorValue = Constants.MAX_OCCUPANCY_TEMPERATURE_VALUE;
        atom.setFlag("HETATM");
        atom.setXYZ(new Point3f(this.getXYZ().getX(), this.getXYZ().getY(), this.getXYZ().getZ()));
        try {
            float tempValue = this.getDistance();
            if (tempValue > maxTempFactorValue) {
                tempValue = maxTempFactorValue;
            }
            atom.setTemperatureFactor(tempValue);
        } catch (Exception e) {
            atom.setTemperatureFactor(0.0f);
        }
        if (this.isOccupied()) {
            atom.setChainId('Y');
        } else {
            atom.setChainId('N');
        }
        if (this.isBoundary()) {
            atom.setChainId('B');
        }
        return atom;
    }

    /**
     * Returns this grid cell in PDB format.
     * @return String object holding information about this grid in PDB format.
     */
    public final String toString() {
        return this.toAtom().toString();
    }
}
