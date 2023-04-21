package fr.smile.liferay.portlet.news.service.impl;

import java.util.Date;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import fr.smile.liferay.portlet.news.model.NewsFlag;
import fr.smile.liferay.portlet.news.service.base.NewsFlagLocalServiceBaseImpl;

/**
 * The implementation of the news flag local service.
 * 
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy
 * their definitions into the {@link fr.smile.liferay.portlet.news.service.NewsFlagLocalService} interface.
 * </p>
 * 
 * <p>
 * Never reference this interface directly. Always use
 * {@link fr.smile.liferay.portlet.news.service.NewsFlagLocalServiceUtil} to access the news flag local service.
 * </p>
 * 
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same VM.
 * </p>
 * 
 * @author Brian Wing Shun Chan
 * @see fr.smile.liferay.portlet.news.service.base.NewsFlagLocalServiceBaseImpl
 * @see fr.smile.liferay.portlet.news.service.NewsFlagLocalServiceUtil
 */
public class NewsFlagLocalServiceImpl extends NewsFlagLocalServiceBaseImpl {

    public NewsFlag addFlag(long userId, long entryId, int value) throws SystemException {
        long flagId = counterLocalService.increment();
        NewsFlag flag = newsFlagPersistence.create(flagId);
        flag.setUserId(userId);
        flag.setCreateDate(new Date());
        flag.setEntryId(entryId);
        flag.setValue(value);
        newsFlagPersistence.update(flag, false);
        return flag;
    }

    public void deleteFlag(long flagId) throws PortalException, SystemException {
        newsFlagPersistence.remove(flagId);
    }

    public void deleteFlags(long entryId) throws SystemException {
        newsFlagPersistence.removeByEntryId(entryId);
    }

    public NewsFlag getFlag(long userId, long entryId, int value) throws PortalException, SystemException {
        return newsFlagPersistence.findByU_E_V(userId, entryId, value);
    }
}
