package ru.org.linux.site;

public class BadInputException extends Exception {

    private static final long serialVersionUID = -3198420275016341961L;

    private String problem;

    public BadInputException(String problem) {
        this.problem = problem;
    }

    public String getProblem() {
        return this.problem;
    }
}
