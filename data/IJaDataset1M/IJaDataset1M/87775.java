package org.iqual.chaplin.intro.lesson18;

import org.iqual.chaplin.DynaCast;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Zbynek Slajchrt
 * @since Aug 1, 2009 7:16:26 PM
 */
public class App {

    @DynaCast(MyHandler.class)
    public static void main(String[] args) {
        InputStream inputStream = (InputStream) (Object) "person.properties";
        Map data = (Map) inputStream;
        System.out.print("The name is ");
        System.out.print(data.get("firstname"));
        System.out.print(" ");
        System.out.println(data.get("lastname"));
    }
}
