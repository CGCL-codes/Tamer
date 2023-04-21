package model;

import java.util.Hashtable;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * @author cmaurice2
 * 
 */
public class MMRecipeElement implements Comparable<MMRecipeElement> {

    public static final String NODE_NAME_ELEMENT = "element";

    public static final String ATTR_NAME_INGREDIENT = "ingredient";

    public static final String ATTR_NAME_QTY = "quantity";

    private MMIngredient ingredient;

    private double quantity;

    public MMRecipeElement(MMIngredient ingredient, double quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public MMRecipeElement(Element elementElement, Hashtable<Integer, MMIngredient> ingredients) {
        this.ingredient = ingredients.get(Integer.parseInt(elementElement.getAttributeValue(ATTR_NAME_INGREDIENT)));
        this.quantity = Double.parseDouble(elementElement.getAttributeValue(ATTR_NAME_QTY));
    }

    public MMIngredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(MMIngredient ingredient) {
        MMData.setModificationsSaved(false);
        this.ingredient = ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        MMData.setModificationsSaved(false);
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return quantity + " " + ingredient.getUnit().toString() + " of " + ingredient.toString();
    }

    @Override
    public int compareTo(MMRecipeElement o) {
        return ((Integer) this.getIngredient().getShopPoint().getPriority()).compareTo(o.getIngredient().getShopPoint().getPriority());
    }

    public Element toXML() {
        Element ingredientElement = new Element(NODE_NAME_ELEMENT);
        ingredientElement.setAttribute(new Attribute(ATTR_NAME_INGREDIENT, Integer.toString(ingredient.getID())));
        ingredientElement.setAttribute(new Attribute(ATTR_NAME_QTY, Double.toString(quantity)));
        return ingredientElement;
    }
}
