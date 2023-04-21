package net.sf.ngrease.example.hello;

import net.sf.ngrease.example.helloutils.GreetingBuilder;

public class Hello {

    public static void main(String[] args) {
        System.out.print(getGreeting());
    }

    public static String getGreeting() {
        return GreetingBuilder.getInstance().greeting("Hello").target("ngremental").getGreeting();
    }
}
