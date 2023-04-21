package com.org.dao;

import java.util.ArrayList;
import com.org.beans.RowSet;

public interface AttendanceInterface {

    public ArrayList<RowSet> dailyAttendance(String date, String department, String designation);
}
