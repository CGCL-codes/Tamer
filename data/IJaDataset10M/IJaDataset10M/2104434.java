package cruise.associations;

public class ProgramB {

    private MentorB mentor;

    private StudentB student;

    public ProgramB() {
    }

    public MentorB getMentor() {
        return mentor;
    }

    public StudentB getStudent() {
        return student;
    }

    public boolean setMentor(MentorB newMentor) {
        boolean wasSet = false;
        if (newMentor == null) {
            MentorB existingMentor = mentor;
            mentor = null;
            if (existingMentor != null && existingMentor.getProgram() != null) {
                existingMentor.setProgram(null);
            }
            wasSet = true;
            return wasSet;
        }
        MentorB currentMentor = getMentor();
        if (currentMentor != null && !currentMentor.equals(newMentor)) {
            currentMentor.setProgram(null);
        }
        mentor = newMentor;
        ProgramB existingProgram = newMentor.getProgram();
        if (!equals(existingProgram)) {
            newMentor.setProgram(this);
        }
        wasSet = true;
        return wasSet;
    }

    public boolean setStudent(StudentB newStudent) {
        boolean wasSet = false;
        if (newStudent == null) {
            StudentB existingStudent = student;
            student = null;
            if (existingStudent != null && existingStudent.getProgram() != null) {
                existingStudent.setProgram(null);
            }
            wasSet = true;
            return wasSet;
        }
        StudentB currentStudent = getStudent();
        if (currentStudent != null && !currentStudent.equals(newStudent)) {
            currentStudent.setProgram(null);
        }
        student = newStudent;
        ProgramB existingProgram = newStudent.getProgram();
        if (!equals(existingProgram)) {
            newStudent.setProgram(this);
        }
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        if (mentor != null) {
            mentor.setProgram(null);
        }
        if (student != null) {
            student.setProgram(null);
        }
    }
}
