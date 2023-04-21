package com.google.code.sagetvaddons.sjq.server.tests;

import java.util.Calendar;

public class FieldDayOfWeek extends FieldNumber {

    public FieldDayOfWeek(Object obj, String op, String input) {
        super(obj, op, input);
    }

    @Override
    public boolean run() {
        Calendar c = Calendar.getInstance();
        try {
            return super.run(c.get(Calendar.DAY_OF_WEEK), Double.parseDouble(getInput()));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
