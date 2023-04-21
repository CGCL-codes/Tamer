package org.easyrec.service;

import org.easyrec.model.core.transfer.TimeConstraintVO;
import java.util.Iterator;
import java.util.List;

/**
 * Base interface for ActionServices, describes methods to access actions (within the recommender engine).
 * <p/>
 * <p><b>Company:&nbsp;</b>
 * SAT, Research Studios Austria</p>
 * <p/>
 * <p><b>Copyright:&nbsp;</b>
 * (c) 2007</p>
 * <p/>
 * <p><b>last modified:</b><br/>
 * $Author: sat-rsa $<br/>
 * $Date: 2011-08-12 10:46:14 -0400 (Fri, 12 Aug 2011) $<br/>
 * $Revision: 113 $</p>
 *
 * @author Roman Cerny
 */
public interface BaseActionService<A, I, AT, IT, T, U> {

    public void importActionsFromCSV(String fileName);

    public int insertAction(A action);

    public int insertAction(A action, boolean useDateFromVO);

    public int removeActionsByTenant(T tenant);

    public Iterator<A> getActionIterator(int bulkSize);

    public Iterator<A> getActionIterator(int bulkSize, TimeConstraintVO timeConstraints);

    public List<A> getActionsFromUser(T tenant, U user, String sessionId);

    public List<I> getItemsOfTenant(T tenant, IT consideredItemType);

    public List<I> getItemsByUserActionAndType(T tenant, U user, String sessionId, AT consideredActionType, IT consideredItemType, Double ratingThreshold, Integer numberOfLastActionsConsidered);

    public void importActionsFromCSV(String fileName, A defaults);
}
