package mr.davidsanderson.uml.application.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class ExporterEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
        try {
            GWT.create(UMLGraphApplication.class);
        } catch (Exception e) {
            Window.alert(e.getMessage());
        }
    }
}
