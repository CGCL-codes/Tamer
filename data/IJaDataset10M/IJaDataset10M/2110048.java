package de.objectcode.time4u.android.client.ui.test.mock;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import de.objectcode.time4u.android.client.ui.adapter.ProjectAndTaskAdapter;
import de.objectcode.time4u.android.entities.IEntity;

public class MockProjectAndTaskAdapter extends ProjectAndTaskAdapter {

    private List<View> views;

    public MockProjectAndTaskAdapter(Context context, List<IEntity> entitys) {
        super(context, entitys);
        initViews(entitys);
    }

    private void initViews(List<IEntity> entities) {
        this.views = new ArrayList<View>();
        for (int i = 0; i < entities.size(); i++) {
            getView(i, null, null);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        views.add(view);
        return view;
    }

    public View getViewAt(int position) {
        return views.get(position);
    }

    public List<View> getViews() {
        return views;
    }

    public void refillViews() {
        views.clear();
        List<IEntity> newEntities = new ArrayList<IEntity>();
        for (int i = 0; i < getCount(); i++) {
            IEntity entity = getItem(i);
            newEntities.add(entity);
        }
        initViews(newEntities);
    }
}
