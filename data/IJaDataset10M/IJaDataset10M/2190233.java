package spacefaring.game.resources;

import java.util.Vector;
import static spacefaring.game.resources.ResourceDepot.ResourceCategory.*;
import static spacefaring.util.RandomizationUtility.*;
import static java.lang.Math.*;

/**
 * Holds an amount of a specific type of resource.
 *
 * @author Administrator
 */
public class ResourceDepot {

    /**
     * Describes the categoy of a resource type. This is used to
     * distinguish between those resources that planet can have,
     * and those resource that a colony can have.
     */
    public static enum ResourceCategory {

        Planetary, Manufactured
    }

    public static enum ResourceType {

        METAL(Planetary, 0.95f, 10000f, "Metal"), RARE_METAL(Planetary, 0.2f, 700f, "Rare Metal"), CRYSTALS(Planetary, 0.5f, 400f, "Crystals"), RARE_CRYSTALS(Planetary, 0.1f, 30f, "Rare Crystals"), FOOD(Manufactured, 0.0f, 0.0f, "Food"), FUEL(Manufactured, 0.0f, 0.0f, "Fuel"), TRADE_GOODS(Manufactured, 0.0f, 0.0f, "Trade Goods");

        /**
         * The category
         */
        private final ResourceCategory category;

        private final float occurance;

        private final float quantity;

        private final String gametext;

        ResourceType(ResourceCategory cat, float occ, float quan, String gtext) {
            this.occurance = occ;
            this.quantity = quan;
            this.gametext = gtext;
            this.category = cat;
        }

        public ResourceCategory getCategory() {
            return category;
        }

        public float probability() {
            return occurance;
        }

        public float amount() {
            return quantity;
        }

        public String gameName() {
            return gametext;
        }

        /**
         * Returns the list of resource types that are of the category
         * planetary.
         */
        public static Vector<ResourceType> getOnlyPlanetary() {
            Vector<ResourceType> onlyplanetary = new Vector<ResourceType>(1, 1);
            for (ResourceType rt : ResourceType.values()) {
                if (rt.category == Planetary) onlyplanetary.add(rt);
            }
            return onlyplanetary;
        }
    }

    /**
     * Amount resources, measured in
     * tons, these values (when generated randomly) depend on the size of the
     * planet
     */
    public int amount;

    public final ResourceType type;

    /**
     * Creates a new instance of ResourceDepot with with a
     * resource amount of zero.
     */
    public ResourceDepot(ResourceType rtype) {
        this(rtype, 0);
    }

    /**
     * Creates a new instance of ResourceDepot with a specific
     * resource amount.
     */
    public ResourceDepot(ResourceType rtype, int ramount) {
        type = rtype;
        amount = ramount;
    }

    /**
     * Creates a new instance of ResourceDepot and randomizes according to the
     * parameter (planet size)
     */
    public ResourceDepot(float planetsize, ResourceType rtype) {
        this(rtype);
        randomizeForPlanet(planetsize);
    }

    /**
     * Returns the depot's resource type.
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Sets the specified resource amount.
     */
    public void setAmount(int newamount) {
        amount = newamount;
    }

    /**
     * Increases the resource amount by a specified reduce value.
     */
    public void increaseAmount(int increasevalue) {
        amount += increasevalue;
    }

    /**
     * Reduces the resource amount by a specified reduce value.
     */
    public void reduceAmount(int reducevalue) {
        amount -= reducevalue;
    }

    /**
     * Returns the specified resource amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Randomizes the vaules of the resources for a new planet.
     */
    public final void randomizeForPlanet(float planetsize) {
        if (random() > type.probability()) {
            amount = 0;
            return;
        }
        amount = (int) (abs(distributeStandardNormal()) * type.amount() * 503 * pow(planetsize / 7500f, 2));
        if (amount > 50000) {
            amount = (int) (amount / 10000f);
            amount = amount * 10000;
        } else if (amount > 5000) {
            amount = (int) (amount / 1000f);
            amount = amount * 1000;
        } else if (amount > 500) {
            amount = (int) (amount / 100f);
            amount = amount * 100;
        } else {
            amount = (int) (amount / 10f);
            amount = amount * 10;
        }
    }

    /**
     * Creates an identical copy of the resource depot.
     */
    @Override
    public Object clone() {
        return this.copy();
    }

    /**
     * Creates an identical copy of the resource depot.
     */
    public ResourceDepot copy() {
        return new ResourceDepot(type, amount);
    }
}
