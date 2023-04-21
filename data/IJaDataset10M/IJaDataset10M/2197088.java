package org.esfinge.comparison.example;

import org.esfinge.comparison.annotation.CompareSubstring;
import org.esfinge.comparison.annotation.DeepComparison;
import org.esfinge.comparison.annotation.IgnoreInComparison;

public class Person {

    private String name;

    private double weight;

    private int age;

    private Address address;

    public Person(String name, float weight, int age) {
        this.name = name;
        this.weight = weight;
        this.age = age;
    }

    @DeepComparison
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address adddress) {
        this.address = adddress;
    }

    @CompareSubstring(begin = 3)
    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    @Weight
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @IgnoreInComparison
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
