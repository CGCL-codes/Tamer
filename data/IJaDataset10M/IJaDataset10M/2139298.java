package br.com.montax.presentation;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: Y1Z5
 * Date: 13/12/10
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void initContext() {
        new ClassPathXmlApplicationContext(new String[] { "META-INF/spring-context.xml" });
    }

    public void main(String arc[]) {
        initContext();
    }
}
