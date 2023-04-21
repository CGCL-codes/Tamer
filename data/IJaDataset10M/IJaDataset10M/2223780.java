package fr.ign.cogit.geoxygene.spatial.coordgeom;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IGriddedSurface;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPointGrid;

/**
 * NON IMPLEMENTE, A FAIRE. Grille.
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.0
 * 
 */
public class GM_GriddedSurface extends GM_ParametricCurveSurface implements IGriddedSurface {

    /**
   * Tableau à deux dimension de points constituant la grille.
   */
    protected IPointGrid controlPoint;

    @Override
    public IPointGrid getControlPoint() {
        return this.controlPoint;
    }

    /**
   * Nombre de lignes dans la grille.
   */
    private int rows;

    protected int getRows() {
        return this.rows;
    }

    /**
   * Nombre de colonnes dans la grille.
   */
    private int columns;

    /**
   * Nombre de colonnes dans la grille. On prend l'hypothèse que toutes les
   * lignes ont le même nombre de colonnes.
   * <p>
   * Number of columns in the grid. We assume that all rows have the same number
   * of columns.
   */
    protected int getColumns() {
        return this.columns;
    }

    @Override
    public IDirectPositionList coord() {
        IDirectPositionList coord = new DirectPositionList();
        int numberOfRows = this.controlPoint.cardRow();
        for (int row = 0; row < numberOfRows; row++) {
            coord.addAll(this.controlPoint.getRow(row));
        }
        return coord;
    }
}
