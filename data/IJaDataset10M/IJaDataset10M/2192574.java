package session.beans;

import java.util.Calendar;
import java.util.List;
import entity.beans.Exam;

public interface ExamManager {

    public Exam getExam(int id);

    public void requireExam(String examName, Calendar date, int medicalReportID, int specialistDoctorID);

    public void cancelExam(int examID);

    public void editExam(int examID, String examName, Calendar date, int specialistDoctorID);

    public void setExamResult(int examID, String result, int nurseID);

    public List<Exam> findReportExams(int medicalReportID);
}
