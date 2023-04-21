package com.liferay.portlet.tasks.service;

/**
 * <a href="TasksReviewService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.liferay.portlet.tasks.service.impl.TasksReviewServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.tasks.service.TasksReviewServiceUtil
 *
 */
public interface TasksReviewService {

    public com.liferay.portlet.tasks.model.TasksReview approveReview(long proposalId, int stage) throws java.rmi.RemoteException, com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public com.liferay.portlet.tasks.model.TasksReview rejectReview(long proposalId, int stage) throws java.rmi.RemoteException, com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void updateReviews(long proposalId, long[][] userIdsPerStage) throws java.rmi.RemoteException, com.liferay.portal.PortalException, com.liferay.portal.SystemException;
}
