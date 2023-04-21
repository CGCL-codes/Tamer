package file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadDataFile {

    private BufferedReader student;

    private BufferedReader professor;

    private BufferedReader staff;

    private static List<String> studentList = new ArrayList<String>();

    private static List<String> professorList = new ArrayList<String>();

    private static List<String> staffList = new ArrayList<String>();

    public ReadDataFile() {
        try {
            student = new BufferedReader(new InputStreamReader(new FileInputStream("student.data"), "UTF-8"));
            professor = new BufferedReader(new InputStreamReader(new FileInputStream("professor.data"), "UTF-8"));
            staff = new BufferedReader(new InputStreamReader(new FileInputStream("staff.data"), "UTF-8"));
            makeStudentList(student);
            makeProfessorList(professor);
            makeStaffList(staff);
        } catch (Exception e) {
            System.err.println("파일 읽기 에러" + e);
        } finally {
            try {
                student.close();
                professor.close();
                staff.close();
            } catch (IOException e) {
                System.err.println("파일 닫기 에러" + e);
            }
        }
    }

    private void makeStaffList(BufferedReader br) {
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                staffList.add(line);
            }
        } catch (IOException e) {
            System.err.println("스탭 리스트 생성에러" + e);
        }
    }

    private void makeProfessorList(BufferedReader br) {
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                professorList.add(line);
            }
        } catch (IOException e) {
            System.err.println("교수 리스트 생성에러" + e);
        }
    }

    private void makeStudentList(BufferedReader br) {
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                studentList.add(line);
            }
        } catch (IOException e) {
            System.err.println("학생 리스트 생성에러" + e);
        }
    }

    public static List<String> getStudentList() {
        return studentList;
    }

    public static void setStudentList(List<String> studentList) {
        ReadDataFile.studentList = studentList;
    }

    public static List<String> getProfessorList() {
        return professorList;
    }

    public static void setProfessorList(List<String> professorList) {
        ReadDataFile.professorList = professorList;
    }

    public static List<String> getStaffList() {
        return staffList;
    }

    public static void setStaffList(List<String> staffList) {
        ReadDataFile.staffList = staffList;
    }
}
