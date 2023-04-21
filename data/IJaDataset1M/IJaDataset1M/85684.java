package admintools;

import javax.naming.*;

/**
 * <p>Title: ShowBindings </p>
 *    
 * <p>Description: Used to watch all bindings on RMI </p>
 * 
 * <p>Copyright: Copyright (c) 2006</p>
 * 
 * <p>Company: Alex</p>
 * @author alexog
 * @version 1.0
 */
public class ShowBindings {

    /**
     * main RUN IT!!!
     * @param args 
     */
    public static void main(String[] args) {
        try {
            Context namingC = new InitialContext();
            NamingEnumeration<NameClassPair> e = namingC.list("java:comp/");
            while (e.hasMoreElements()) {
                System.out.println(e.next().getName());
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }
}
