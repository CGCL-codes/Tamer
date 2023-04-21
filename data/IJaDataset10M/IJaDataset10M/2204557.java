package org.baljinder.presenter.dataacess;

/**
 * One possibility is handle these event through aspects if that works 
 * @author Baljinder Randhawa
 *
 */
public interface IEventHandler {

    public void beforeTransition(ITransitionController transitionController);

    public void afterTransition(ITransitionController transitionController);

    public void beforeInitialize(IDataController dataController);

    public void afterInitialize(IDataController dataController);

    public void beforeDataFetch(IDataController dataController);

    public void afterDataFetch(IDataController dataController);

    public void beforeSave(IDataController dataController);

    public void afterSave(IDataController dataController);

    public void beforeDelete(IDataController dataController);

    public void afterDelete(IDataController dataController);

    public void beforeUpdate(IDataController dataController);

    public void afterUpdate(IDataController dataController);

    /**
	 * @param dataController
	 */
    public void beforeInsert(IDataController dataController);

    /**
	 * @param dataController
	 */
    public void afterInsert(IDataController dataController);

    /**
	 * @param pageController
	 */
    public void beforeInitialize(IPageController pageController);

    /**
	 * @param pageController
	 */
    public void afterInitialize(IPageController pageController);

    public void beforeTransition(IPageController pageController);

    public void afterTransition(IPageController pageController);

    /**
	 * @param pageController
	 */
    public void beforeSave(IPageController pageController);

    /**
	 * @param pageController
	 */
    public void afterSave(IPageController pageController);

    /**
	 * @param dataController
	 */
    public void beforeRefresh(IDataController dataController);

    /**
	 * @param dataController
	 */
    public void afterRefresh(IDataController dataController);
}
