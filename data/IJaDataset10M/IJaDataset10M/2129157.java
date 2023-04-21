package com.google.code.projects.BlogSrc.HelloWorldEJB.ejb3.stateful;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Init;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateful;

@Stateful
public class StatefulHelloWorldBean implements StatefulHelloWorldRemote {

    public static int postConstructCounter = 0;

    public static int preDestroy = 0;

    public static int prePassivateCounter = 0;

    public static int postActivateCounter = 0;

    private int statefulValue = 0;

    public StatefulHelloWorldBean() {
        System.out.println("<< Stateful >> constructor");
    }

    public int getStatefulValue() {
        return statefulValue;
    }

    public void setStatefulValue(int statefulValue) {
        this.statefulValue = statefulValue;
    }

    public String sayHello(String name) {
        return "Stateful Hello, " + name + " !";
    }

    public String sayHello(int name) {
        return "Stateful Hello, " + name + " !";
    }

    @PostConstruct
    public void postConstructAction() {
        postConstructCounter++;
        System.out.println("postconstruct called ! counter : " + postConstructCounter);
    }

    @PreDestroy
    public void preDestroyAction() {
        preDestroy++;
        System.out.println("postconstruct called ! counter : " + preDestroy);
    }

    @PrePassivate
    public void prePassivateAction() {
        prePassivateCounter++;
        System.out.println("postconstruct called ! counter : " + prePassivateCounter);
    }

    @PostActivate
    public void postActivateAction() {
        postActivateCounter++;
        System.out.println("postconstruct called ! counter : " + postActivateCounter);
    }

    @Init
    public void sInit() {
        System.out.println("stateful @Init hook called");
    }
}
