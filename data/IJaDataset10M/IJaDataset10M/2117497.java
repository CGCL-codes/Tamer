package net.sourceforge.simpleworklog.client.gwt.tabs;

import net.sourceforge.simpleworklog.client.gwt.presenter.Presenter;
import net.sourceforge.simpleworklog.client.gwt.presenter.UsersPresenter;

/**
 * @author: Ignat Alexeyenko
 * @url: http://alexeyenko.net
 * Date: Nov 8, 2010
 */
public class UsersTabType implements ITabType {

    @Override
    public Presenter createTabPresenter() {
        return new UsersPresenter();
    }
}
