package failure.wm.business.patterns;

import failure.wm.business.*;
import failure.common.*;

/**
 * Rectangle pattern
 * @author Higurashi
 * @version WM 1.0
 * @since WM 1.0
 */
public class WpSquareSurface extends WorldPattern {

    protected SurfaceType type;

    protected boolean passing;

    /**
     * Constructor
     * @param x size (X)
     * @param y size (Y)
     * @param position the base position of the pattern
     * @param type the surface type of the pattern
     * @param passing true if the pattern is passable
     */
    public WpSquareSurface(int x, int y, Position position, SurfaceType type, boolean passing) {
        super(x, y, position);
        this.type = type;
        this.passing = passing;
    }

    /**
     * @see WorldPattern
     */
    @Override
    public void build() throws WorldException {
        this.setBlock(0, 0, type.getBasicSet().NO, this.passing);
        this.setBlock(0, y + 1, type.getBasicSet().SO, this.passing);
        for (int j = 1; j < x + 1; j++) {
            this.setBlock(j, 0, type.getBasicSet().N, this.passing);
            this.setBlock(j, y + 1, type.getBasicSet().S, this.passing);
        }
        this.setBlock(x + 1, 0, type.getBasicSet().NE, this.passing);
        this.setBlock(x + 1, y + 1, type.getBasicSet().SE, this.passing);
        for (int j = 1; j < y + 1; j++) {
            this.setBlock(0, j, type.getBasicSet().O, this.passing);
            this.setBlock(x + 1, j, type.getBasicSet().E, this.passing);
        }
        for (int i = 1; i < x + 1; i++) {
            for (int j = 1; j < y + 1; j++) {
                this.setBlock(i, j, type.getBasicSet().C_I, this.passing);
            }
        }
    }
}
