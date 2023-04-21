package org.jazzteam.edu.lang.threads.daemonsExample;

/**
 * 
 * 
 * @author Hor1zonT
 * @version $Rev: $
 */
public class RunThreads {

    public static void main(String[] args) {
        new myThread(32);
        new MyDaemon();
    }
}
