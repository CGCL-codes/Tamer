package com.mucas;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CourseList extends ArrayAdapter<Course> {

    public CourseList(Context context, int textViewResourceId, CourseStack courses) {
        super(context, android.R.layout.activity_list_item);
    }
}
