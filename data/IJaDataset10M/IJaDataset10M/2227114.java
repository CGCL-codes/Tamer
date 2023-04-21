package com.wpl.ui.factory;

/**
 * 
 * @since 1.0
 */
public interface IComponentFactory {

    /**
	 * @param context
	 * @throws Exception
	 * @since 1.0
	 */
    public void createInstance(IUiFactory uiFactory, ComponentContext context) throws Exception;

    /**
	 * 
	 * @since 1.0
	 * 
	 * @param context
	 */
    public void initialize(ComponentContext context);
}
