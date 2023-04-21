package us.gibb.dev.gwt.presenter;

import us.gibb.dev.gwt.view.View;

public interface Presenter<V extends View<?>> {

    V getView();
}
