package com.atosorigin.nl.jspring2008.buzzword.gsmnode;

import java.io.IOException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jeroen Benckhuijsen (jeroen.benckhuijsen@gmail.com)
 * 
 */
public class Main {

    private ConfigurableApplicationContext context;

    /**
	 * 
	 */
    public Main() {
        super();
    }

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.startMule();
    }

    private void startMule() throws IOException {
        context = new ClassPathXmlApplicationContext("buzzword/gsmnode/applicationContext.xml");
        System.out.println("Now Sleeping - Hit <enter> to terminate.");
        System.in.read();
        context.close();
    }
}
