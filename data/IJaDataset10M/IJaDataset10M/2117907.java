package cruise.associations;

public class ProgramG {

    private MentorG mentor;

    private StudentG student;

    public ProgramG() {
    }

    public MentorG getMentor() {
        return mentor;
    }

    public StudentG getStudent() {
        return student;
    }

    public boolean setMentor(MentorG newMentor) {
        boolean wasSet = false;
        if (newMentor == null) {
            MentorG existingMentor = mentor;
            mentor = null;
            if (existingMentor != null && existingMentor.getProgram() != null) {
                existingMentor.setProgram(null);
            }
            wasSet = true;
            return wasSet;
        }
        MentorG currentMentor = getMentor();
        if (currentMentor != null && !currentMentor.equals(newMentor)) {
            currentMentor.setProgram(null);
        }
        mentor = newMentor;
        ProgramG existingProgram = newMentor.getProgram();
        if (!equals(existingProgram)) {
            newMentor.setProgram(this);
        }
        wasSet = true;
        return wasSet;
    }

    public boolean setStudent(StudentG newStudent) {
        boolean wasSet = false;
        if (newStudent == null) {
            StudentG existingStudent = student;
            student = null;
            if (existingStudent != null && existingStudent.getProgram() != null) {
                existingStudent.setProgram(null);
            }
            wasSet = true;
            return wasSet;
        }
        StudentG currentStudent = getStudent();
        if (currentStudent != null && !currentStudent.equals(newStudent)) {
            currentStudent.setProgram(null);
        }
        student = newStudent;
        ProgramG existingProgram = newStudent.getProgram();
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
