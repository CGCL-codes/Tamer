package edu.vub.at.aop;

public interface PointCut {

    boolean match(JoinPoint j);
}
