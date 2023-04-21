package googlechartwrapper.color;

import java.util.List;

/**
 * The interface for chart types which supports FillArea.
 * 
 * @author steffan
 *
 */
public interface IFillAreaable {

    /**
	 * Adds a new FillArea to the Chart.
	 * 
	 * @param fa the new FillArea object
	 * 
	 * @throws IllegalArgumentException if you try to add null
	 */
    public void addFillArea(FillArea fa);

    /**
	 * Returns a unmodifiable list of FillAreas
	 * 
	 * @return list of FillAreas, can be empty
	 */
    public List<FillArea> getFillAreas();

    /**
	 * Removes FillArea at the given position.
	 * 
	 * @param index
	 * 
	 * @return the removed FillArea
	 * 
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
    public FillArea removeFillArea(int index);

    /**
	 * Removes an given FillArea object. 
	 * 
	 * @param fa the FillArea object in the list
	 * 
	 * @return true if the remove was successful
	 */
    public boolean removeFillArea(FillArea fa);

    /**
	 * Removes all FillArea in the list.
	 * 
	 */
    public void removeAllFillAreas();
}
