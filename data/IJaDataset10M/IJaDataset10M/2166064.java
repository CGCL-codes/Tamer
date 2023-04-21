package com.seitenbau.testing.shared.tracer.flow;

import org.aspectj.lang.JoinPoint;
import com.seitenbau.testing.shared.model.RecordedTest;
import com.seitenbau.testing.shared.tracer.AbstractTracer;

public class MockTracer extends AbstractTracer implements IFlowControl {

    private RecordedTest myTest;

    @Override
    protected RecordedTest createRecordedTest() {
        myTest = new RecordedTest();
        return myTest;
    }

    @Override
    protected void saveRecordedTest(RecordedTest theRecordedTest) {
    }

    public Object subMethodReturningValue(JoinPoint theJoinPoint, Object theOldValue) {
        return theOldValue;
    }

    public boolean subMethodSchouldThrowException(JoinPoint theJoinPoint, Throwable throwable) {
        return throwable != null;
    }

    public boolean subMethodShouldProceed(JoinPoint theJoinPoint) {
        return true;
    }

    public Throwable subMethodThrowingException(JoinPoint theJoinPoint, Throwable theOldException) {
        return theOldException;
    }

    public Object subNewOperatorReturningValue(JoinPoint theJoinPoint, Object theOldValue) {
        return theOldValue;
    }

    public boolean subNewOperatorSchouldThrowException(JoinPoint theJoinPoint, Throwable throwable) {
        return throwable != null;
    }

    public boolean subNewOperatorShoudProceed(JoinPoint theJoinPoint) {
        return true;
    }

    public Throwable subNewOperatorThrowingException(JoinPoint theJoinPoint, Throwable theOldException) {
        return theOldException;
    }

    public Object targetMethodReturningValue(JoinPoint theJoinPoint, Object theOldValue) {
        return theOldValue;
    }

    public boolean targetMethodSchouldThrowException(JoinPoint theJoinPoint, Throwable throwable) {
        return throwable != null;
    }

    public boolean targetMethodShouldProceed(JoinPoint theJoinPoint) {
        return true;
    }

    public Throwable targetMethodThrowingException(JoinPoint theJoinPoint, Throwable theOldException) {
        return theOldException;
    }

    public RecordedTest getTest() {
        return myTest;
    }

    public void setTest(RecordedTest test) {
        myTest = test;
    }
}
