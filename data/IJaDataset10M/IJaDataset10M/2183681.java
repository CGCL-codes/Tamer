package com.sin.client.ui.east;

import com.google.gwt.user.client.ui.IsWidget;

public interface EastFacebookView extends IsWidget {

    public interface Presenter {

        void gotoChatPlace();
    }

    void setPresenter(Presenter presenter);
}
