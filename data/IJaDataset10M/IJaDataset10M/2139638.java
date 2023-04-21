package ru.spbspu.staub.model.list;

import java.io.Serializable;

/**
 * Model for sort option represenation.
 *
 * @author Konstantin_Grigoriev
 */
public class SortItem implements Serializable {

    private static final long serialVersionUID = 7612570308215441412L;

    /**
     * Defines sort orders.
     */
    public static enum Order {

        /**
         * Ascent sort direction.
         */
        ASC, /**
         * Descent sort direction.
         */
        DESC
    }

    /**
     * Field name for sort (in case of persistent-style list may be with delimeters).
     */
    private String field;

    /**
     * Order type for this sort.
     */
    private SortItem.Order order;

    /**
     * Creates sort item for specific field and specific order.
     *
     * @param field field name
     * @param order order direction
     */
    public SortItem(String field, Order order) {
        this.field = field;
        this.order = order;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setOrder(SortItem.Order order) {
        this.order = order;
    }

    public SortItem.Order getOrder() {
        return order;
    }

    /**
     * Determines is this item is ascent sort order.
     *
     * @return true or false
     */
    public boolean isAsc() {
        return SortItem.Order.ASC.equals(this.order);
    }

    /**
     * Determines is this item is descent sort order.
     *
     * @return true or false
     */
    public boolean isDesc() {
        return SortItem.Order.DESC.equals(this.order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof SortItem)) {
            return false;
        }
        SortItem otherItem = (SortItem) other;
        return otherItem.getField().equals(this.getField());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.getField().hashCode();
    }
}
