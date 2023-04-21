package fr.smile.liferay.portlet.news.service;

/**
 * <p>
 * This class is a wrapper for {@link NewsFlagService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       NewsFlagService
 * @generated
 */
public class NewsFlagServiceWrapper implements NewsFlagService {

    public NewsFlagServiceWrapper(NewsFlagService newsFlagService) {
        _newsFlagService = newsFlagService;
    }

    public fr.smile.liferay.portlet.news.model.NewsFlag addFlag(long userId, long entryId, int value) throws com.liferay.portal.kernel.exception.SystemException {
        return _newsFlagService.addFlag(userId, entryId, value);
    }

    public void deleteFlag(long flagId) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        _newsFlagService.deleteFlag(flagId);
    }

    public void deleteFlags(long entryId) throws com.liferay.portal.kernel.exception.SystemException {
        _newsFlagService.deleteFlags(entryId);
    }

    public fr.smile.liferay.portlet.news.model.NewsFlag getFlag(long userId, long entryId, int value) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        return _newsFlagService.getFlag(userId, entryId, value);
    }

    public NewsFlagService getWrappedNewsFlagService() {
        return _newsFlagService;
    }

    private NewsFlagService _newsFlagService;
}
