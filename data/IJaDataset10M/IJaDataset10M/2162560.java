package com.skillworld.webapp.model.courseservice;

public class ExamAlreadyEvaluatedException extends Exception {

    private Long examId;

    private Long userId;

    public ExamAlreadyEvaluatedException(Long examId, Long userId) {
        super("This exam" + examId + "have been already evaluated to user" + userId);
        this.examId = examId;
        this.userId = userId;
    }
}
