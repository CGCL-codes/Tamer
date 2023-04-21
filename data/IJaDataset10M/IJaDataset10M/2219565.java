package trstudio.blueboxalife.graphic.physic;

import java.io.Serializable;
import trstudio.blueboxalife.state.ALifeException;
import trstudio.classlibrary.drivers.ErrorManager;

/**
 * Représente le segment d'une ligne avec ses coordonnées définie dans un espace.
 *
 * @author Sebastien Villemain
 */
public class AlifeLine implements Comparable<AlifeLine>, Cloneable, Serializable {

    /**
	 * AlifePoint de départ de la ligne.
	 */
    private AlifePoint start = null;

    /**
	 * Extrémité de la ligne.
	 */
    private AlifePoint end = null;

    private float xIntercept = 0;

    private float yIntercept = 0;

    /**
	 * La pente.
	 */
    private float slope = 0;

    /**
	 * Type de ligne.
	 */
    private LineType type = null;

    /**
	 * Nouvelle ligne suivant les points
	 *
	 * @param s AlifePoint du début de ligne
	 * @param e AlifePoint de fin de ligne
	 */
    public AlifeLine(AlifePoint s, AlifePoint e) {
        if (s.x > e.x) {
            start = e;
            end = s;
        } else {
            start = s;
            end = e;
        }
        if (start.x == end.x) {
            type = LineType.VERTICAL;
            xIntercept = start.x;
            yIntercept = 0;
            slope = 1;
        } else if (start.y == end.y) {
            type = LineType.HORIZONTAL;
            slope = 0;
            xIntercept = 0;
            yIntercept = start.y;
        } else {
            type = LineType.NORMAL;
            slope = (end.y - start.y) / (end.x - start.x);
            yIntercept = start.y - slope * start.x;
            xIntercept = -yIntercept / slope;
        }
    }

    /**
	 * Retourne le type de la ligne.
	 *
	 * @return
	 */
    public LineType getType() {
        return type;
    }

    public float xIntercept() {
        return xIntercept;
    }

    public float yIntercept() {
        return yIntercept;
    }

    /**
	 * Retourne le coefficient de pente.
	 *
	 * @return
	 */
    public float getSlope() {
        return slope;
    }

    /**
	 * Coordonnée du début de la ligne.
	 *
	 * @return
	 */
    public AlifePoint getStart() {
        return start;
    }

    /**
	 * Coordonnée de fin de la ligne.
	 *
	 * @return
	 */
    public AlifePoint getEnd() {
        return end;
    }

    /**
	 * Retourne le point à l'emplacement X.
	 *
	 * @param x
	 * @return
	 */
    public AlifePoint getPointAtX(float x) {
        AlifePoint p = null;
        switch(type) {
            case HORIZONTAL:
                p = new AlifePoint(x, start.y);
                break;
            case VERTICAL:
                throw new ALifeException("Bad line type.");
            default:
                p = new AlifePoint(x, (x - start.x) * slope + start.y);
                break;
        }
        return p;
    }

    /**
	 * Retourne le point à l'emplacement Y.
	 *
	 * @param y
	 * @return
	 */
    public AlifePoint getPointAtY(float y) {
        AlifePoint p = null;
        switch(type) {
            case HORIZONTAL:
                throw new ALifeException("Bad line type.");
            case VERTICAL:
                p = new AlifePoint(start.x, y);
                break;
            default:
                p = new AlifePoint((y - start.y) / slope + start.x, y);
                break;
        }
        return p;
    }

    /**
	 * Vérifie si X est dans la ligne.
	 *
	 * @param x
	 * @return
	 */
    public boolean containsX(float x) {
        return (x >= start.x && x <= end.x);
    }

    /**
	 * Vérifie si Y est dans la ligne.
	 *
	 * @param x
	 * @return
	 */
    public boolean containsY(float y) {
        boolean rslt = false;
        if (start.y > end.y) {
            rslt = (y <= start.y && y >= end.y);
        } else {
            rslt = (y >= start.y && y <= end.y);
        }
        return rslt;
    }

    /**
	 * Positionne la ligne aux coordonnées indiquées.
	 *
	 * @param l
	 */
    public void relocateAt(AlifeLine l) {
        start = l.start;
        end = l.end;
        xIntercept = l.xIntercept;
        yIntercept = l.yIntercept;
        slope = l.slope;
        type = l.type;
    }

    /**
	 * Vérifie s'il y a une intersection entre la ligne horizontale et une autre ligne (de tout type).
	 *
	 * @param horizontalLine La ligne horizontale.
	 * @param line La ligne susceptible d'être en intersection.
	 * @param where Le point d'intersection trouvé.
	 * @return true si il y a une intersection.
	 */
    public static boolean intersectHorizontal(AlifeLine horizontalLine, AlifeLine line, AlifePoint where) {
        if (horizontalLine.type != LineType.HORIZONTAL) {
            throw new ALifeException("Only for a horizontal line.");
        }
        boolean rslt = false;
        switch(line.type) {
            case HORIZONTAL:
                rslt = (horizontalLine.contains(line.start) || horizontalLine.contains(line.end));
                break;
            case VERTICAL:
                if (line.containsY(horizontalLine.start.y) && horizontalLine.containsX(line.start.x)) {
                    where.x = line.start.x;
                    where.y = horizontalLine.start.y;
                    rslt = true;
                }
                break;
            default:
                float x = (horizontalLine.start.y - line.yIntercept) / line.slope;
                if (line.containsX(x) && horizontalLine.containsX(x)) {
                    where.x = x;
                    where.y = horizontalLine.start.y;
                    rslt = true;
                }
                break;
        }
        return rslt;
    }

    /**
	 * Vérifie s'il y a une intersection entre la ligne verticale et une autre ligne (de tout type).
	 *
	 * @param verticalLine La ligne verticale.
	 * @param line La ligne susceptible d'être en intersection.
	 * @param where Le point d'intersection trouvé.
	 * @return true si il y a une intersection.
	 */
    public static boolean intersectVertical(AlifeLine verticalLine, AlifeLine line, AlifePoint where) {
        if (verticalLine.type != LineType.VERTICAL) {
            throw new ALifeException("Only for a vertical line.");
        }
        boolean rslt = false;
        switch(line.type) {
            case HORIZONTAL:
                if (line.containsX(verticalLine.start.x) && verticalLine.containsY(line.start.y)) {
                    where.x = verticalLine.start.x;
                    where.y = line.start.y;
                    rslt = true;
                }
                break;
            case VERTICAL:
                rslt = (verticalLine.contains(line.start) || verticalLine.contains(line.end));
                break;
            default:
                if (line.containsX(verticalLine.start.x)) {
                    where = line.getPointAtX(verticalLine.start.x);
                    rslt = verticalLine.containsY(where.y);
                }
                break;
        }
        return rslt;
    }

    /**
	 * Vérifie s'il y a une intersection entre la ligne normale et une autre ligne (de tout type).
	 *
	 * @param normalLine Une ligne qui n'est pas verticale, ni horizontale.
	 * @param line La seconde ligne.
	 * @param where Le point d'intersection trouvé.
	 * @return true si il y a une intersection.
	 */
    public static boolean intersectNormal(AlifeLine normalLine, AlifeLine line, AlifePoint where) {
        if (normalLine.type != LineType.NORMAL) {
            throw new ALifeException("Only for a normal line.");
        }
        boolean rslt = false;
        if (normalLine.slope != line.slope) {
            float x = (normalLine.yIntercept - line.yIntercept) / (line.slope - normalLine.slope);
            float y = (normalLine.slope * x) + normalLine.yIntercept;
            if (normalLine.containsX(x) && line.containsX(x)) {
                where.x = x;
                where.y = y;
                rslt = true;
            }
        }
        return rslt;
    }

    /**
	 * Vérifie si il y a un point d'intersection entre les lignes.
	 *
	 * @param line1 Première ligne.
	 * @param line2 Deuxième ligne.
	 * @param where AlifePoint d'intersection.
	 * @return true si il y a un point d'intersection.
	 */
    public static boolean intersect(AlifeLine line1, AlifeLine line2, AlifePoint where) {
        boolean rslt = false;
        switch(line1.type) {
            case HORIZONTAL:
                rslt = intersectHorizontal(line1, line2, where);
                break;
            case VERTICAL:
                rslt = intersectVertical(line1, line2, where);
                break;
            default:
                rslt = intersectNormal(line1, line2, where);
                break;
        }
        return rslt;
    }

    /**
	 * Vérifie si il y a un point d'intersection entre les lignes.
	 *
	 * @param l La ligne à vérifier.
	 * @param where Le point d'intersection trouvé.
	 * @return true si il y a un point d'intersection.
	 */
    public boolean intersect(AlifeLine l, AlifePoint where) {
        boolean rslt = false;
        switch(type) {
            case HORIZONTAL:
                rslt = intersectHorizontal(this, l, where);
                break;
            case VERTICAL:
                rslt = intersectVertical(this, l, where);
                break;
            default:
                rslt = intersectNormal(this, l, where);
                break;
        }
        return rslt;
    }

    /**
	 * Vérifie que le point est sur la ligne.
	 *
	 * @param p
	 * @return
	 */
    public boolean contains(AlifePoint p) {
        boolean rslt = false;
        switch(type) {
            case HORIZONTAL:
                rslt = (Math.abs(start.y - p.y) < 1 && containsX(p.x));
                break;
            case VERTICAL:
                rslt = (Math.abs(start.x - p.x) < 1 && containsY(p.y));
                break;
            default:
                AlifePoint pointOnLine = getPointAtX(p.x);
                rslt = (Math.abs(pointOnLine.y - p.y) < 1 && containsX(p.x));
                break;
        }
        return rslt;
    }

    /**
	 * Identifiant de la ligne.
	 *
	 * @return
	 */
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.start != null ? this.start.hashCode() : 0);
        hash = 43 * hash + (this.end != null ? this.end.hashCode() : 0);
        hash = 43 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

    /**
	 * Vérifie si les lignes sont identiques.
	 *
	 * @param obj
	 * @return
	 */
    public boolean equals(Object obj) {
        boolean rslt = false;
        if (this == obj) {
            rslt = true;
        } else if (obj instanceof AlifeLine) {
            AlifeLine l = (AlifeLine) obj;
            rslt = (type == l.type && start.equals(l.start) && end.equals(l.end));
        }
        return rslt;
    }

    /**
	 * Duplique la ligne.
	 *
	 * @return
	 */
    public Object clone() {
        AlifeLine l = null;
        try {
            l = (AlifeLine) super.clone();
            l.start = (AlifePoint) start.clone();
            l.end = (AlifePoint) end.clone();
        } catch (CloneNotSupportedException ex) {
            ErrorManager.getLogger().addCriticalError(ex);
        }
        return l;
    }

    /**
	 * Compare les lignes.
	 * Attention, la comparaison est approximative.
	 *
	 * @param l
	 * @return
	 */
    public int compareTo(AlifeLine l) {
        int rslt = -1;
        if (this.equals(l)) {
            rslt = 0;
        } else {
            switch(type) {
                case HORIZONTAL:
                    if ((start.x + (end.x - start.x)) > (l.start.x + (l.end.x - l.start.x))) {
                        rslt = 1;
                    }
                    break;
                case VERTICAL:
                    if ((start.y + (end.y - start.y)) > (l.start.y + (l.end.y - l.start.y))) {
                        rslt = 1;
                    }
                    break;
                default:
                    if (start.compareTo(l.start) > 0 && end.compareTo(l.end) > 0) {
                        rslt = 1;
                    }
                    break;
            }
        }
        return rslt;
    }

    /**
	 * Retourne l'expression de la ligne.
	 *
	 * @return
	 */
    public String toString() {
        return this.getClass().getSimpleName() + "(" + start + " " + end + " " + type + ");";
    }
}
