package wangzx.newuibinder.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {

    @Override
    public void onModuleLoad() {
        SimpleForm form = new SimpleForm();
        RootPanel.get().add(form);
    }
}
