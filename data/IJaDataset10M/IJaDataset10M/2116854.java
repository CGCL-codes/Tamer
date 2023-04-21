package fi.tuska.jalkametri.data;

import java.io.Serializable;
import java.util.List;
import fi.tuska.jalkametri.Common;
import fi.tuska.jalkametri.dao.DrinkCategory;
import fi.tuska.jalkametri.db.DBDataObject;

/**
 * Basic data-holding implementation for drink categories (for editing, before
 * information is stored to DB).
 * 
 * @author Tuukka Haapasalo
 */
public class CategorySelection extends DBDataObject implements DrinkCategory, Serializable {

    private static final long serialVersionUID = -8375927023878631885L;

    private String name = "";

    private String icon = Common.DEFAULT_ICON_NAME;

    public CategorySelection() {
        super();
    }

    public CategorySelection(DrinkCategory category) {
        super();
        name = category.getName();
        icon = category.getIcon();
    }

    @Override
    public String getIconText() {
        return icon;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Drink createDrink(String name, float strength, String icon, DrinkSize[] sizes) {
        throw new UnsupportedOperationException("addDrink() not supported for the category selection type");
    }

    @Override
    public Drink getDrink(long index) {
        throw new UnsupportedOperationException("getDrink() not supported for the category selection type");
    }

    @Override
    public List<Drink> getDrinks() {
        throw new UnsupportedOperationException("getDrinks() not supported for the category selection type");
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return String.format("Category %s, icon %s", name, icon);
    }

    @Override
    public boolean deleteDrink(long index) {
        throw new UnsupportedOperationException("deleteDrink() not supported for the category selection type");
    }

    @Override
    public boolean updateDrink(long index, Drink drinkInfo) {
        throw new UnsupportedOperationException("updateDrink() not supported for the category selection type");
    }
}
