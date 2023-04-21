package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;

public class ViewNameLoader implements IChildLoaderCallback {

    private final String name;

    private final PaloServerModel model;

    private ILoadViewCallback callback;

    private final IChildLoadCallback loadCallback = new IChildLoadCallback() {

        public void onChildLoaded(XObject object) {
            XView view = (XView) object;
            onViewLoaded(view);
        }
    };

    public ViewNameLoader(PaloServerModel model, String name) {
        this.model = model;
        this.name = name;
    }

    public void onChildLoaded(XObject xObject) {
        XCube cube = (XCube) xObject;
        XView[] views = cube.getViews();
        if (views == null) {
            model.loadViewByName(cube, name, loadCallback);
        } else {
            XView view = (XView) XArrays.findByName(views, name);
            onViewLoaded(view);
        }
    }

    private void onViewLoaded(XView view) {
        if (callback != null) {
            callback.onViewLoaded(null, view);
        }
    }

    public void setCallback(ILoadViewCallback callback) {
        this.callback = callback;
    }
}
