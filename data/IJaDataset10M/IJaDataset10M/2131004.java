package net.sf.theotherpages.business;

import java.util.List;

/**
 * This interface is used for incorporating the pagination framework
 * <p>,
 * <li>,<ui>,<code></code>,
 * 
 * <pre></pre>,<i></i>
 * 
 * <p>
 * <DL>
 * <DT><B>History: </B>
 * <DD>Jan 12, 2007</DD>
 * </DL>
 * 
 * @author ShriKant
 * @version 1.0, Jan 12, 2007
 * 
 * @since v1.0, Jan 12, 2007
 * 
 */
public interface PaginationCallback {

    /**
	 * TODO Mention the description of method over here
	 * <p>
	 * TODO Next paragraph if more than one needed. Otherwise remove
	 * <p>
	 * if not needed.
	 * @param queryParams 
	 * @param otherParams 
	 * @param startIndex 
	 * @param endIndex 
	 * 
	 * @return <code>List</code> TODO description
	 * @throws Exception
	 * 
	 */
    List getPaginationData(Object queryParams, Object[] otherParams, int startIndex, int endIndex) throws Exception;

    /**
	 * TODO Mention the description of method over here
	 * <p>
	 * TODO Next paragraph if more than one needed. Otherwise remove
	 * <p>
	 * if not needed.
	 * @param queryParams 
	 * @param otherParams 
	 * 
	 * @return <code>long</code> TODO description
	 * @throws Exception
	 * 
	 */
    long getRecordsCount(Object queryParams, Object[] otherParams) throws Exception;
}
