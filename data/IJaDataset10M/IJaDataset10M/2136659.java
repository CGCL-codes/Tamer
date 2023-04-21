package com.gobynote.android.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NoteData {

    public static String AUTHORITY = "com.gobynote.data.NoteProvider";

    public NoteData() {
    }

    public static final class NoteColumns implements BaseColumns {

        private NoteColumns() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/note");

        public static final String NT_ID = "id";

        public static final String NT_SORT_SEQ = "sortseq";

        public static final String NT_FLD_ID = "folderid";

        public static final String NT_TYPE = "notetype";

        public static final String NT_TITLE = "title";

        public static final String NT_NOTE_DTL = "notedtl";

        public static final String NT_SHORT_DTL = "shortdtl";

        public static final String NT_DEADLINE = "deadline";

        public static final String NT_FMT_DEADLINE = "fmtdeadline";

        public static final String NT_IMPORTANCE = "importance";

        public static final String NT_ALARM_ME = "alarmme";

        public static final String NT_ALARM_TYPE = "alarmtype";

        public static final String NT_ALARM_INTERVAL = "alarminterval";

        public static final String NT_ALARM_DATE = "alarmdate";

        public static final String NT_REPEAT = "repeat";

        public static final String NT_REPEAT_TYPE = "repeattype";

        public static final String NT_REPEAT_INTERVAL = "repeatinterval";

        public static final String NT_NO_TIME = "notime";

        public static final String NT_COMPLETED = "completed";

        public static final String NT_SEC_NOTE = "secnote";

        public static final String NT_CHECKED = "checked";

        public static final String NT_COMPLETED_DATE = "completedate";

        public static final String NT_DIARY_DATE = "diarydate";

        public static final String NT_FMT_DIARY_DATE = "fmtdiarydate";

        public static final String NT_MOOD = "mood";

        public static final String NT_WEATHER = "weather";

        public static final String NT_LOCATION = "location";

        public static final String NT_GEO_LOC = "loc";

        public static final String NT_GEO_LAT = "lat";

        public static final String NT_CRT_DATE = "crtdate";

        public static final String NT_UPD_DATE = "upddate";
    }

    public static String[] getAllColumns() {
        String[] columns = new String[] { NoteColumns.NT_ID, NoteColumns.NT_SORT_SEQ, NoteColumns.NT_FLD_ID, NoteColumns.NT_TYPE, NoteColumns.NT_TITLE, NoteColumns.NT_NOTE_DTL, NoteColumns.NT_SHORT_DTL, NoteColumns.NT_DEADLINE, NoteColumns.NT_FMT_DEADLINE, NoteColumns.NT_IMPORTANCE, NoteColumns.NT_ALARM_ME, NoteColumns.NT_NO_TIME, NoteColumns.NT_ALARM_DATE, NoteColumns.NT_ALARM_TYPE, NoteColumns.NT_REPEAT, NoteColumns.NT_ALARM_INTERVAL, NoteColumns.NT_REPEAT_TYPE, NoteColumns.NT_REPEAT_INTERVAL, NoteColumns.NT_CHECKED, NoteColumns.NT_COMPLETED_DATE, NoteColumns.NT_COMPLETED, NoteColumns.NT_SEC_NOTE, NoteColumns.NT_DIARY_DATE, NoteColumns.NT_FMT_DIARY_DATE, NoteColumns.NT_MOOD, NoteColumns.NT_WEATHER, NoteColumns.NT_LOCATION, NoteColumns.NT_GEO_LOC, NoteColumns.NT_GEO_LAT, NoteColumns.NT_CRT_DATE, NoteColumns.NT_UPD_DATE };
        return columns;
    }

    public static String[] getRowColumns() {
        String[] columns = new String[] { NoteColumns.NT_ID, NoteColumns.NT_FLD_ID, NoteColumns.NT_TITLE, NoteColumns.NT_SHORT_DTL, NoteColumns.NT_DEADLINE, NoteColumns.NT_FMT_DEADLINE, NoteColumns.NT_IMPORTANCE, NoteColumns.NT_COMPLETED, NoteColumns.NT_ALARM_ME, NoteColumns.NT_REPEAT };
        return columns;
    }

    public static String[] getDiaryRowColumns() {
        String[] columns = new String[] { NoteColumns.NT_ID, NoteColumns.NT_TITLE, NoteColumns.NT_SHORT_DTL, NoteColumns.NT_DIARY_DATE, NoteColumns.NT_FMT_DIARY_DATE, NoteColumns.NT_IMPORTANCE, NoteColumns.NT_MOOD, NoteColumns.NT_WEATHER, NoteColumns.NT_LOCATION };
        return columns;
    }

    public static String[] getChecklistRowColumns() {
        String[] columns = new String[] { NoteColumns.NT_ID, NoteColumns.NT_FLD_ID, NoteColumns.NT_TITLE, NoteColumns.NT_SHORT_DTL, NoteColumns.NT_CHECKED, NoteColumns.NT_IMPORTANCE, NoteColumns.NT_COMPLETED_DATE };
        return columns;
    }
}
