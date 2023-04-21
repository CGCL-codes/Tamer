package com.gtt.pattern.structural.flyweight;

import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        FlyweightFactory factory = new FlyweightFactory();
        List<Flyweight> flys = new ArrayList<Flyweight>();
        flys.add(factory.getFlyweight("Google"));
        flys.add(factory.getFlyweight("Yahoo"));
        flys.add(factory.getFlyweight("Google"));
        flys.add(factory.getFlyweight("Baidu"));
        flys.add(factory.getFlyweight("Google"));
        flys.add(factory.getFlyweight("Google"));
        flys.add(factory.getFlyweight("Google"));
        for (Flyweight flyweight : flys) {
            flyweight.operation();
        }
        System.out.println("Flyweight Number: " + factory.getFlyweightNumber());
    }
}
