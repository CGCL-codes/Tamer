package sis.student;

import java.util.*;

public class ReportCard {

    public static String A_MESSAGE = "Excellent";

    public static String B_MESSAGE = "Very good";

    public static String C_MESSAGE = "Hmmm...";

    public static String D_MESSAGE = "You are not trying";

    public static String F_MESSAGE = "Loser";

    private Map<Student.Grade, String> messages = null;

    public String getMessage(Student.Grade grade) {
        return getMessages().get(grade);
    }

    private Map<Student.Grade, String> getMessages() {
        if (messages == null) {
            loadMessages();
        }
        return messages;
    }

    private void loadMessages() {
        messages = new EnumMap<Student.Grade, String>(Student.Grade.class);
        messages.put(Student.Grade.A, A_MESSAGE);
        messages.put(Student.Grade.B, B_MESSAGE);
        messages.put(Student.Grade.C, C_MESSAGE);
        messages.put(Student.Grade.D, D_MESSAGE);
        messages.put(Student.Grade.F, F_MESSAGE);
    }
}
