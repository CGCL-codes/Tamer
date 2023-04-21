package edu.java.texbooks.scjp.test05;

public class Student {

    private String name = null;

    private int marks[] = null;

    public Student(String name, int[] marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Illegal input argument - Name is null.");
        }
        this.name = name;
    }

    public int[] getMarks() {
        return marks;
    }

    public void setMarks(int[] marks) {
        if (marks == null) {
            throw new IllegalArgumentException("Illegal input argument - " + "Marks is null-pointed array.");
        }
        this.marks = marks;
    }

    public double getAverageResults() {
        int sum = 0;
        int[] marks = getMarks();
        for (int mark : marks) {
            sum += mark;
        }
        return (sum * 1.0) / marks.length;
    }
}
