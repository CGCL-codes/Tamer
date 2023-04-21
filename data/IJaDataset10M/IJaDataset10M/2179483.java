package org.simplextensions.ui;

/**
 * Presenter part of a MVP pattern regarding main application window.
 * 
 * @author Tomasz Krzyzak, <a
 *         href="mailto:tomasz.krzyzak@gmail.com">tomasz.krzyzak@gmail.com</a>
 * @since 2010-05-10 09:56:07
 */
public interface IApplicationWindow extends IStatefulUIPart, ISimpleXtensionsUIPart {

    void open();
}
