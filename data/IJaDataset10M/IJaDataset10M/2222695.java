package com.redhipps.hips.client.widget;

import java.util.List;
import com.google.gwt.user.client.ui.Hyperlink;
import com.redhipps.hips.client.command.CreateScheduleCommand;
import com.redhipps.hips.client.controller.EditCallback;
import com.redhipps.hips.client.controller.ModelEditDelegate;
import com.redhipps.hips.client.model.Context;
import com.redhipps.hips.client.model.DateTime;
import com.redhipps.hips.client.model.Schedule;

public class ScheduleList extends ModelList<Schedule> {

    private static final String[] HEADERS = { "Start time" };

    public ScheduleList(Context ctx, List<Schedule> values) {
        super(ctx, values);
        setEditingDelegate(new ScheduleEditDelegate());
        refresh();
    }

    @Override
    protected Object[] headers() {
        return HEADERS;
    }

    @Override
    protected Object[] writeModel(Schedule model, int row) {
        StringBuilder buffy = new StringBuilder();
        DateTime startTime = model.getStartTime();
        buffy.append(startTime.getYear());
        buffy.append('/');
        buffy.append(startTime.getMonth());
        buffy.append('/');
        buffy.append(startTime.getDay());
        Hyperlink hyperlink = new Hyperlink(buffy.toString(), "scheduleList");
        hyperlink.addClickListener(createModelSelectionClickListener(model));
        return new Object[] { hyperlink };
    }

    private static class ScheduleEditDelegate implements ModelEditDelegate<Schedule> {

        public void createModel(Context context, EditCallback<Schedule> callback) {
            invokeWithEditCallback(new CreateScheduleCommand(), context, callback);
        }

        public void deleteModel(Context context, Schedule model, EditCallback<Schedule> callback) {
        }

        public void editModel(Context context, Schedule model, EditCallback<Schedule> callback) {
        }
    }
}
