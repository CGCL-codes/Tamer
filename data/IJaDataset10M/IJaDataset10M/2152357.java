package view.table.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import model.MMIngredient;
import model.MMRecipeElement;
import model.MMShopPoint;
import model.MMUnit;

/**
 * @author cmaurice2
 * 
 */
public class MMShopListTableModel extends AbstractTableModel {

    /**
	 * Auto-generated SVUID
	 */
    private static final long serialVersionUID = 833954043151307708L;

    public static final int COLUMN_COUNT = 4;

    public static final int COL_SHOPPOINT = 0;

    public static final int COL_TOTAL = 1;

    public static final int COL_UNIT = 2;

    public static final int COL_INGREDIENT = 3;

    private String[] columnNames = { "Shop point", "Total", "Unit", "Ingredient" };

    private ArrayList<MMRecipeElement> shopList;

    public MMShopListTableModel(ArrayList<MMRecipeElement> shopList) {
        super();
        this.shopList = shopList;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case COL_SHOPPOINT:
                return MMShopPoint.class;
            case COL_TOTAL:
                return Float.class;
            case COL_UNIT:
                return MMUnit.class;
            case COL_INGREDIENT:
                return MMIngredient.class;
            default:
                return Object.class;
        }
    }

    @Override
    public int getRowCount() {
        return shopList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column < COLUMN_COUNT) {
            return columnNames[column];
        } else {
            return null;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if ((rowIndex >= 0 && rowIndex < shopList.size()) && (columnIndex >= 0 && columnIndex < COLUMN_COUNT)) {
            MMRecipeElement element = shopList.get(rowIndex);
            switch(columnIndex) {
                case COL_SHOPPOINT:
                    return element.getIngredient().getShopPoint();
                case COL_TOTAL:
                    return element.getQuantity();
                case COL_UNIT:
                    return element.getIngredient().getUnit();
                case COL_INGREDIENT:
                    return element.getIngredient();
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if ((rowIndex >= 0 && rowIndex < shopList.size()) && (columnIndex >= 0 && columnIndex < COLUMN_COUNT)) {
            if (aValue instanceof Float) {
                shopList.get(rowIndex).setQuantity(Float.parseFloat(aValue.toString()));
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COL_TOTAL;
    }
}
