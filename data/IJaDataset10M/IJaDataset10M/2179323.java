package org.td4j.core.env;

public class HelloServiceImpl implements HelloService {

    final String msg;

    public HelloServiceImpl() {
        this("impl-noarg");
    }

    HelloServiceImpl(String msg) {
        this.msg = msg;
    }

    @Override
    public void sayHello() {
        System.out.println(msg);
    }
}
