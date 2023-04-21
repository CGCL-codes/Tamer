package ti.series;

/**
 * A Region provides bounded time access to a consecutive 
 * set of elements within a Index.  A client can manipulate the
 * Region in such a way that it appears to move back and forth within the 
 * the Index.  Depending upon the way that the client specifies
 * which elements are interesting, the Region may be notified when new 
 * elements are available within the Index.  
 * <p>
 * Clients specify their element selection criteria by providing a  
 * RegionSpec object.  The RegionSpec specifies a range of elements
 * within the Index.  One RegionSpec policy calls for a specific
 * number of elements that start at a particular location.   A different
 * RegionSpec policy calls for a specific number of the most recent 
 * elements.  See RegionSpec for other important details.
 * <p>
 * A Region has a major modality called <quote>staleness</quote>.  By definition, a 
 * Region becomes stale when the view of the set of elements it provides
 * its user is different than the set of elements it would provide the user
 * if the Region consulted the Index for an update.  
 * <p>
 * The process by which a Region consults a Index for an update is 
 * known as a refresh operation.  Refresh operations are intended
 * to occur in <quote>user</quote> time, or in other words, only as frequently
 * as a flesh-n-blood user can realistically handle.  
 * <p>
 * A Region engages a Index through attaching and detaching to/from it.
 * The client should be careful that during these operations, the 
 * client should not cache element references (ElementRefs) in such a 
 * way that the Region would attempt to use those element references 
 * to retrive values from the wrong Index.  See ElementRef.
 *
 * @author Brad Hyslop, Rob Clark
 */
public interface Region {

    /**
   * Used by the Client to update the current set of selected elements 
   * with any changes.  This method causes the Region to 
   * consult with the Index to get the set of elements that are selected 
   * by the current range specification.  This method may be invoked at any 
   * time, but it will most frequently be called in response to a notification
   * from the Index that the current Region is stale.
   * <p>
   * Implementations of this method should be synchronized.
   */
    public void refresh() throws InvalidElementRefException, SeriesException;

    /**
   * Counts the elements that are currently selected.  The value 
   * returned by this method only changes after a call to refresh.
   */
    public int getCount();

    /**
   * Used by the Client to set the Index to which this Region
   * is attached.
   */
    public void setIndex(Index index);

    /**
   * Used by the Client to get the Index to which this Region
   * is attached
   */
    public Index getIndex();

    /** 
   * Used by the Index to notify the Region that 
   * a subsequent refresh operation would change the set of elements that the
   * Region identifies.
   * 
   * @param min    first effected element in the index
   * @param max    last effected element in the index
   */
    void notifyStale(ElementRef min, ElementRef max) throws SeriesException;

    /**
   * Used by the Client to specify which set of elements are to be members.
   * To guarantee correct behavior, this method should be synchronized.
   * 
   * @param rs         The RegionSpec object defining the range
   */
    public void setRegionSpec(RegionSpec rs);

    /**
   * Used by the Index to determine which elements
   * are desired by the Region object during a
   * refresh operation.
   *
   * Used by a Client to adjust the current RegionSpec.
   */
    public RegionSpec getRegionSpec();
}
