package org.opensourcephysics.stp.percolation;

/**
 *  Description of the Class
 *
 *@author     jgould
 *@created    January 27, 2002
 */
public class HoshenKopelman {

    /**
	 *  Description of the Field
	 */
    public static final int OCCUPIED = 1;

    /**
	 *  Description of the Field
	 */
    public static final int EMPTY = 0;

    int[] single_step = new int[2];

    int L;

    int ncluster;

    int rsite[][];

    int[] np;

    static final int ASSIGN_MODE = 0;

    static final int PROPER_MODE = 1;

    int mode = ASSIGN_MODE;

    /**
	 *  Constructor for the HoshenKopelman object
	 *
	 *@param  L        Description of Parameter
	 *@param  lattice  Description of Parameter
	 */
    public HoshenKopelman(PercolationLattice lattice, int L) {
        this.rsite = lattice.rsite;
        np = new int[rsite.length * rsite.length];
        this.L = L;
        single_step[0] = 1;
        single_step[1] = 1;
    }

    /**
	 *  Gets the mode attribute of the HoshenKopelman object
	 *
	 *@return    The mode value
	 */
    public String getMode() {
        if (mode == ASSIGN_MODE) {
            return "Assigning cluster numbers to occupited sites";
        } else if (mode == PROPER_MODE) {
            return "Assigning proper labels to cluster array.";
        } else {
            return "Algorithm finished.";
        }
    }

    public boolean isComplete() {
        return mode != ASSIGN_MODE && mode != PROPER_MODE;
    }

    public void compute() {
        ncluster = 0;
        for (int y = 1; y <= L; y++) {
            for (int x = 1; x <= L; x++) {
                assign(x, y);
            }
        }
        for (int y = 1; y <= L; y++) {
            for (int x = 1; x <= L; x++) {
                properLabel(x, y);
            }
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@return    an array of length 2 of the current site
	 */
    public int[] singleStep() {
        if (mode == ASSIGN_MODE) {
            assign(single_step[0], single_step[1]);
            increaseIndices();
            return single_step;
        } else if (mode == PROPER_MODE) {
            properLabel(single_step[0], single_step[1]);
            increaseIndices();
            return single_step;
        } else {
            return null;
        }
    }

    private void increaseIndices() {
        if (single_step[0] == L) {
            single_step[0] = 1;
            single_step[1]++;
            if (single_step[1] == L + 1) {
                single_step[1] = 1;
                mode++;
            }
        } else {
            single_step[0]++;
        }
    }

    /**
	 *  assigns proper labels to cluster array
	 *
	 *@param  x  Description of Parameter
	 *@param  y  Description of Parameter
	 */
    private void properLabel(int x, int y) {
        if (rsite[x][y] != EMPTY) {
            rsite[x][y] = proper(rsite[x][y]);
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@param  x  Description of Parameter
	 *@param  y  Description of Parameter
	 */
    private void neighbor(int x, int y) {
        int down = y - 1;
        int left = x - 1;
        if (rsite[x][down] * rsite[left][y] > 0) {
            labelMin(x, y, left, down);
        } else if (rsite[x][down] > 0) {
            rsite[x][y] = rsite[x][down];
        } else {
            rsite[x][y] = rsite[left][y];
        }
    }

    /**
	 *  assigns cluster numbers to occupied sites
	 *
	 *@param  x  Description of Parameter
	 *@param  y  Description of Parameter
	 */
    private void assign(int x, int y) {
        if (rsite[x][y] == OCCUPIED) {
            int down = y - 1;
            int left = x - 1;
            if (rsite[x][down] + rsite[left][y] == 0) {
                ncluster++;
                rsite[x][y] = ncluster;
                np[ncluster] = ncluster;
            } else {
                neighbor(x, y);
            }
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@param  label  Description of Parameter
	 *@return        Description of the Returned Value
	 */
    private int proper(int label) {
        if (np[label] == label) {
            return label;
        } else {
            return proper(np[label]);
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@param  x     Description of Parameter
	 *@param  y     Description of Parameter
	 *@param  left  Description of Parameter
	 *@param  down  Description of Parameter
	 */
    private void labelMin(int x, int y, int left, int down) {
        if (rsite[left][y] == rsite[x][down]) {
            rsite[x][y] = rsite[left][y];
        } else {
            int cl_left = proper(rsite[left][y]);
            int cl_down = proper(rsite[x][down]);
            int nMax = Math.max(cl_left, cl_down);
            int nMin = Math.min(cl_left, cl_down);
            rsite[x][y] = nMin;
            if (nMin != nMax) {
                np[nMax] = nMin;
            }
        }
    }
}
