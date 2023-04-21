package net.kortsoft.gameportlet.culandcon;

import static net.kortsoft.gameportlet.culandcon.ResourceCategory.*;
import java.util.Collection;

public class NaturalResource extends Resource {

    public enum NaturalResourceType implements ResourceType, Scorable {

        Grain(Food), Fish(Food), Iron(Military), Horses(Military), Precious_Metals(Luxury), Fruits(Food), Livestock(Food), Wood(Build), Stone(Build), Spices(Luxury), Fibers(Clothing), Salt(Food), Shellfish(Food), Incense(Luxury), Vegetables(Food), Bronze(Military), Furs(Clothing), Clay(Build), Brick(Build), Gems(Luxury);

        private ResourceCategory category;

        NaturalResourceType(ResourceCategory resourceCategory) {
            this.category = resourceCategory;
        }

        @Override
        public ResourceCategory getCategory() {
            return category;
        }

        @Override
        public boolean isCategory(ResourceCategory category) {
            return this.category == category;
        }

        @Override
        public boolean hasRequirements(Culture currentCulture) {
            if (this == Iron) return currentCulture.hasResource(Bronze);
            return true;
        }

        @Override
        public int pointsFor(Culture culture) {
            if (hasTheMost(culture)) return 5; else return 0;
        }

        private boolean hasTheMost(Culture culture) {
            Collection<Culture> cultures = culture.getCulAndConGame().getCultures();
            int count = culture.getResourceCount(this);
            for (Culture otherCulture : cultures) if (!culture.equals(otherCulture)) if (count < otherCulture.getResourceCount(this)) {
                return false;
            }
            return true;
        }
    }

    private NaturalResourceType type;

    public NaturalResource(NaturalResourceType naturalResourceType, int i) {
        this.type = naturalResourceType;
        this.count = i;
    }

    @Override
    public ResourceType getType() {
        return this.type;
    }
}
