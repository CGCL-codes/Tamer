package model;

public class Course {

    private String courseCode;

    private String school;

    private String title;

    private String description;

    private String examDate;

    private String examStart;

    private String examEnd;

    /**
     * initialize the course constructor
     * @param courseCode the course code
     * @param school the school of the course
     * @param title the title of the course
     * @param description the description of the course
     * @param examDate the date of the exam
     * @param examStart the start time of the exam
     * @param examEnd the end time of the exam
     */
    public Course(String courseCode, String school, String title, String description, String examDate, String examStart, String examEnd) {
        this.courseCode = courseCode;
        this.school = school;
        this.title = title;
        this.description = description;
        this.examDate = examDate;
        this.examStart = examStart;
        this.examEnd = examEnd;
    }

    /**
     * retrieve the course code
     * @return the course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * sets the course code
     * @param courseCode the course code
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * sets the description of the course
     * @param description the description of the course
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets the exam date
     * @param examDate the date of the exam
     */
    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    /**
     * sets the end time of the exam
     * @param examEnd the end time of the exam
     */
    public void setExamEnd(String examEnd) {
        this.examEnd = examEnd;
    }

    /**
     * sets the start time of the exam
     * @param examStart the start time of the exam
     */
    public void setExamStart(String examStart) {
        this.examStart = examStart;
    }

    /**
     * sets the school of the course
     * @param school the school of the course
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * sets the title of the course
     * @param title the title of the course
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * retrieve the description of the course
     * @return the description of the course
     */
    public String getDescription() {
        return description;
    }

    /**
     * retrieves the exam date
     * @return the exam date
     */
    public String getExamDate() {
        return examDate;
    }

    /**
     * retrieve the end time of the exam
     * @return the end time of the exam
     */
    public String getExamEnd() {
        return examEnd;
    }

    /**
     * retrieve the start time of the exam
     * @return the start time of the exam
     */
    public String getExamStart() {
        return examStart;
    }

    /**
     * retrieve the school of the course
     * @return the school of the course
     */
    public String getSchool() {
        return school;
    }

    /**
     * retrieve the title of the course
     * @return the title of the course
     */
    public String getTitle() {
        return title;
    }
}
