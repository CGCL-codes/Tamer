package org.happy.commons.patterns.projector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.happy.commons.patterns.version.Version_1x0;

/**
 * basket can have unmodifiable count of items and a value range, where every
 * item is responsible for a part of that range thus you can get one of items by
 * projecting the index which has value from the range to one of the items from
 * the basket
 * 
 * @author Eugen Lofing, Andreas Hollmann, Wjatscheslaw Stoljarski
 * 
 */
public abstract class IndexProjectorImpl_1x0<T> implements IndexProjector_1x0<T>, Version_1x0<Float> {

    private int itemsNumber;

    private int min;

    private int max;

    private T[] items;

    /**
	 * 
	 * @param itemsNumber
	 * @param min
	 * @param max
	 */
    @SuppressWarnings("unchecked")
    public IndexProjectorImpl_1x0(int itemsNumber, int min, int max) {
        super();
        this.itemsNumber = itemsNumber;
        this.min = min;
        this.max = max;
        ArrayList<T> itemsList = new ArrayList<T>();
        for (int i = 0; i < itemsNumber; i++) itemsList.add(this.createItem(i));
        this.items = (T[]) itemsList.toArray();
    }

    /**
	 * solves the index of the item in the basket per default this method
	 * divides the range in the equals parts. The number of parts is equal to
	 * the number of items. The index from the part will be directly projected
	 * to the item in the basket.
	 ****************************range****************************
	 **|***part1***|***part2***|***part3***|***part4***|***part5**
	 *     item1      item2      item3       item4       item5	 * 
	 *if you like you can override this method to implement custom projection
	 * 
	 * @param index
	 * @return
	 */
    protected int projectIndexToItem(int index) {
        if (index < min || max < index) throw new IllegalArgumentException("index is out of range");
        int size = max - min;
        int i = index - min;
        int j = (int) (this.itemsNumber * ((float) i / (float) size));
        return j;
    }

    /**
	 * creates an item for the busket
	 * 
	 * @param itemIndex
	 *            index of the item in the basket
	 * @return new item
	 */
    protected abstract T createItem(int itemIndex);

    public T getItem(int index) {
        return this.items[this.projectIndexToItem(index)];
    }

    public Collection<T> getItems() {
        return Collections.unmodifiableCollection(Arrays.asList(this.items));
    }

    public Float getVersion() {
        return 1F;
    }
}
