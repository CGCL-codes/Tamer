package com.example;

import java.rmi.*;

public class ExampleClient {

    public static void main(String[] args) {
        try {
            Example example = (Example) Naming.lookup("Example");
            example.setString("Success!");
            System.out.println(example.getString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
