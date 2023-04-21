package environment.dungeons;

import be.kuleuven.cs.som.annotate.*;
import environment.Direction;
import environment.Position;

/**
 * Een klasse die een schacht voorstelt. Schachten zijn groepjes vakjes
 * die slechts in ��n co�rdinaat kunnen vari�ren. Er kunnen geen opgevulde
 * vakjes in aanwezig zijn.
 * 
 * @author 	Nathan Bekaert & Philippe de Potter de ten Broeck
 * @version 1.0
 */
public class Shaft<T extends TeleportableSquare> extends ElementaryDungeon<T> {

    /**
	 * Initialiseert deze nieuwe schacht met het gegeven maximum voor de lengte van de schacht en met de gegeven richting.
	 * 
	 * @param 	length
	 * 			De limiet voor de lengte van deze nieuwe schacht.
	 * @param 	directionOfShaft
	 * 			De richting voor deze nieuwe schacht.
	 * @post	
	 * 			| new.getDirectionOfShaft() == directionOfShaft
	 * @effect	
	 * 			| if((directionOfShaft == Direction.NORTH) || (directionOfShaft == Direction.SOUTH))
	 * 			|	super.setDimensionLimits(0, length, 0)
	 * 			| else if((directionOfShaft == Direction.EAST) || (directionOfShaft == Direction.WEST))
	 * 			|	super.setDimensionLimits(length, 0, 0)
	 * 			| else if((directionOfShaft == Direction.CEILING) || (directionOfShaft == Direction.FLOOR))
	 * 			|	super.setDimensionLimits(0, 0, length)
	 * @throws	IllegalArgumentException
	 * 			| directionOfShaft == null
	 */
    @Raw
    public Shaft(long length, Direction directionOfShaft) throws IllegalArgumentException {
        super(0, 0, 0);
        if (directionOfShaft == null) throw new IllegalArgumentException("The given direction is not effective.");
        this.directionOfShaft = directionOfShaft;
        if ((directionOfShaft == Direction.NORTH) || (directionOfShaft == Direction.SOUTH)) super.setDimensionLimits(0, length, 0);
        if ((directionOfShaft == Direction.EAST) || (directionOfShaft == Direction.WEST)) super.setDimensionLimits(length, 0, 0);
        if ((directionOfShaft == Direction.CEILING) || (directionOfShaft == Direction.FLOOR)) super.setDimensionLimits(0, 0, length);
    }

    /**
	 * Initialiseert deze nieuwe schacht met de gegeven richting en lengte 10.
	 * 
	 * @param 	directionOfShaft
	 * 			De richting voor deze nieuwe schacht.
	 * @effect	
	 * 			| this(10, directionOfShaft)
	 */
    @Raw
    public Shaft(Direction directionOfShaft) throws IllegalArgumentException {
        this(10, directionOfShaft);
    }

    /**
	 * Variabele die de richting van deze schacht bijhoudt.
	 */
    private final Direction directionOfShaft;

    /**
	 * Retourneert de richting van deze schacht.
	 */
    @Basic
    @Raw
    @Immutable
    public Direction getDirectionOfShaft() {
        return this.directionOfShaft;
    }

    /**
	 * Controleert of deze schacht de gegeven waarden als limieten voor de x-, y- en z-richting kan hebben. 
	 * 
	 * @return	
	 * 			| if(! super.canHaveAsLimits(x, y, z))
	 * 			|	then result == false
	 * 			| else if(getDirectionOfShaft() == null)
	 * 			|	then result == true
	 * 			| else if((getDirectionOfShaft() == Direction.NORTH) || (getDirectionOfShaft() == Direction.SOUTH))
	 * 			|	then result == ((x == 0) && (z == 0))
	 * 			| else if((getDirectionOfShaft() == Direction.EAST) || (getDirectionOfShaft() == Direction.WEST))
	 * 			|	then result == ((y == 0) && (z == 0))
	 * 			| else if((getDirectionOfShaft() == Direction.CEILING) || (getDirectionOfShaft() == Direction.FLOOR))
	 * 			|	then result == ((x == 0) && (y == 0))
	 */
    @Override
    @Raw
    public boolean canHaveAsLimits(long x, long y, long z) {
        if (!super.canHaveAsLimits(x, y, z)) return false;
        if (getDirectionOfShaft() == null) return true;
        if ((getDirectionOfShaft() == Direction.NORTH) || (getDirectionOfShaft() == Direction.SOUTH)) return ((x == 0) && (z == 0));
        if ((getDirectionOfShaft() == Direction.EAST) || (getDirectionOfShaft() == Direction.WEST)) return ((y == 0) && (z == 0));
        if ((getDirectionOfShaft() == Direction.CEILING) || (getDirectionOfShaft() == Direction.FLOOR)) return ((x == 0) && (y == 0));
        assert false;
        return false;
    }

    /**
	 * Controleert of deze schacht het gegeven vakje kan hebben als een nieuw vakje 
	 * in het co�rdinatensysteem op de gegeven positie.
	 * 
	 * @param 	teleportableSquare
	 * 			Het teleportatievakje dat moet worden gecontroleerd.
	 * @param 	position
	 * 			De positie waarvoor het teleportatievakje moet worden gecontroleerd.
	 * @return	
	 * 			| if(! super.canHaveAsSquareAt(teleportableSquare, position))
	 * 			|	then result == false
	 * 			| Let	
	 * 			|	neighbour = getCloseBySquares(position).get(getDirectionOfShaft())
	 * 			| In
	 * 			|	if((neighbour != null) && (hasAsSquare(neighbour)) && 
	 * 			|			((teleportableSquare.getBorderAt(getDirectionOfShaft()).isMaterialized()) || 
	 * 			|			(neighbour.getBorderAt(getDirectionOfShaft().complement()).isMaterialized())))
	 * 			|		then result == false
	 * 			| Let
	 * 			|	neighbour = getCloseBySquares(position).get(getDirectionOfShaft().complement())
	 * 			| In
	 * 			|	if((neighbour != null) && (hasAsSquare(neighbour)) && 
	 * 			|			((teleportableSquare.getBorderAt(getDirectionOfShaft().complement()).isMaterialized()) || 
	 * 			|			(neighbour.getBorderAt(getDirectionOfShaft()).isMaterialized())))
	 * 			|		then result == false
	 * 			| else result == true
	 */
    @Override
    public boolean canHaveAsSquareAt(T teleportableSquare, Position position) {
        if (!super.canHaveAsSquareAt(teleportableSquare, position)) return false;
        Square neighbour = getCloseBySquares(position).get(getDirectionOfShaft());
        if ((neighbour != null) && (hasAsSquare(neighbour)) && ((teleportableSquare.getBorderAt(getDirectionOfShaft()).isMaterialized()) || (neighbour.getBorderAt(getDirectionOfShaft().complement()).isMaterialized()))) return false;
        neighbour = getCloseBySquares(position).get(getDirectionOfShaft().complement());
        if ((neighbour != null) && (hasAsSquare(neighbour)) && ((teleportableSquare.getBorderAt(getDirectionOfShaft().complement()).isMaterialized()) || (neighbour.getBorderAt(getDirectionOfShaft()).isMaterialized()))) return false;
        return true;
    }
}
