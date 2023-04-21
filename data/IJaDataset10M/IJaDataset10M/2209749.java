package org.systemsEngineering.core.impl;

import java.util.List;
import org.systemsEngineering.core.PairwiseComparison;
import org.systemsEngineering.core.Weightable;
import org.systemsEngineering.core.util.Matrix;
import org.systemsEngineering.core.util.SimpleMatrix;

/**
 * Implements the <b>Pairwise comparison with total weight sum</b> algorithm.<br>
 * <br>
 * This component eases the task of filling up the comparison matrix with the
 * relative weight values, because only the value for one combination of an item
 * pair need to be set. The counter part is filled up by default. This means if
 * the value is set for the combination columnItem-rowItem, it automatically
 * calculates the correct weight value for the combination rowItem-columnItem
 * and initializes it. Thus only half of the cells need to be initialized. *
 * 
 * @param <ItemType>
 *            the type of the items to manage.
 * @author Mike Werner
 */
public class PairwiseComparisonWithConstantWeightSum<ItemType extends Weightable> implements PairwiseComparison<ItemType> {

    private int maximumWeight;

    private Matrix<ItemType, ItemType, Integer> matrix = new SimpleMatrix<ItemType, ItemType, Integer>();

    /**
     * Creates a new <code>PairwiseComparisonWithConstantWeightSum</code>.
     * 
     * @param maximumWeight
     *            the maximum relative weight value.
     */
    public PairwiseComparisonWithConstantWeightSum(int maximumWeight) {
        this.maximumWeight = maximumWeight;
        if (maximumWeight <= 0) throw new IllegalArgumentException("The maximumWeight must be greater than 0.");
    }

    /**
     * Creates a new <code>PairwiseComparisonWithConstantWeightSum</code> and
     * initializes it wit an initial list of ItemType items.
     * 
     * @param maximumWeight
     *            the maximum relative weight value.
     * @param items
     *            the initial list of ItemType items.
     */
    public PairwiseComparisonWithConstantWeightSum(int maximumWeight, List<ItemType> items) {
        this(maximumWeight);
        for (ItemType item : items) {
            addItem(item);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addItem(ItemType item) {
        addItem(matrix.getNumRows(), item);
    }

    /**
     * Adds a new item to the matrix as a row and as a column and initializes
     * the corresponding value with {@code 0}. 
     * 
     * {@inheritDoc}
     */
    public void addItem(int index, ItemType item) {
        matrix.addRow(index, item);
        matrix.addColumn(index, item);
        matrix.setValue(item, item, 0);
    }

    /**
     * {@inheritDoc}
     */
    public List<ItemType> listItems() {
        return matrix.listColumns();
    }

    /**
     * {@inheritDoc}
     */
    public void removeItem(ItemType item) {
        matrix.removeRow(item);
        matrix.removeColumn(item);
    }

    /**
     * {@inheritDoc}
     */
    public int calculateWeightSum(ItemType rowItem) {
        int result = 0;
        for (ItemType column : matrix.listColumns()) {
            if (rowItem != column) result += matrix.getValue(column, rowItem);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void calculateWeightSums() {
        for (ItemType item : matrix.listRows()) item.setWeight(calculateWeightSum(item));
    }

    /**
     * {@inheritDoc}
     */
    public int getRelativeWeight(ItemType columnItem, ItemType rowItem) {
        if (columnItem == rowItem) throw new IllegalArgumentException("Equal items can not be accessed.");
        return matrix.getValue(columnItem, rowItem);
    }

    /**
     * {@inheritDoc}
     */
    public void setRelativeWeight(ItemType columnItem, ItemType rowItem, int relativeWeight) {
        if (columnItem == rowItem) throw new IllegalArgumentException("Equal items can not be accessed.");
        matrix.setValue(columnItem, rowItem, relativeWeight);
        matrix.setValue(rowItem, columnItem, calculateCounterPartWeight(relativeWeight));
    }

    /**
     * Calculates te weight value to be used for the counter part cell of an
     * item pair.
     * 
     * @param relativeWeight
     *            the relative weight of an item pair.
     * @return the weight value for the counter part cell.
     */
    private Integer calculateCounterPartWeight(int relativeWeight) {
        return maximumWeight - relativeWeight;
    }
}
