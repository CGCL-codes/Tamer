package monopoli;

/**
 * Square.java : class for handling squares in Monopoly
 *
 * @version 	Wed Jun 09, 2010
 * @author 	Samuele Mattiuzzo
 */
public abstract class Square {

    protected String sName;

    protected int totalCost;

    protected int buyCost;

    protected boolean sellable;

    protected Player owner;

    Square(String s, int tc, int bc, boolean sell, Player o) {
        sName = s;
        totalCost = tc;
        buyCost = bc;
        sellable = sell;
        owner = o;
    }

    /**
	 * 	Returns the square name
	 * 	@return the square name
	 */
    public String getName() {
        return sName;
    }

    /**
	 * 	Returns the cost to buy this square
	 * 	@return the price of the square
	 */
    public int getBuyCost() {
        return buyCost;
    }

    /**
	 * 	Mortgages the square
	 * 	mortgaged value -> half the buy cost
	 */
    public void mortgage() {
        buyCost /= 2;
    }

    /**
	 * 	Returns the cost of the square
	 */
    public int getPrice() {
        return totalCost;
    }

    /**
	 * 	Sets the square price to @param
	 * 	@param price the new cost
	 */
    public void setPrice(int price) {
        totalCost = price;
    }

    /**
	 * 	Returns the square owner
	 */
    public Player getOwner() {
        return owner;
    }

    /**
	 * 	Sets the new square owner to @param
	 * 	@param p the new owner
	 */
    public void newOwner(Player p) throws Exception {
        owner = p;
    }

    /**
	 * 	Returns true if this square can be sold
	 */
    public boolean sellable() {
        return sellable;
    }

    /**
	 * 	Handles player landed on square
	 * 	@param p current player
	 */
    public abstract void handleSquare(Player p);

    /**
	 * 	Updates square landing rent
	 */
    public abstract void updateStatus();

    /**
	 * 	Destroys everything on that square
	 */
    public abstract void destroy();
}
