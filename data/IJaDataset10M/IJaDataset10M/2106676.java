package org.eiichiro.monophony;

import java.text.DateFormat;
import org.eiichiro.monophony.annotation.Body;
import org.eiichiro.monophony.annotation.Endpoint;
import org.eiichiro.monophony.annotation.Generates;
import org.eiichiro.monophony.annotation.Negotiated;
import org.eiichiro.monophony.annotation.Query;

@Endpoint("/greeter")
public class Greeter {

    @Negotiated
    public Greeting sayHello(@Query("name") String name) {
        Greeting greeting = new Greeting();
        greeting.message = "Hello, " + name + "!";
        greeting.from = "Monophony";
        greeting.to = name;
        return greeting;
    }

    @Generates(MediaType.TEXT_PLAIN)
    public String postGreeting(@Body Greeting greeting) {
        return "Greeting from " + greeting.from + " has been posted! [" + DateFormat.getDateInstance(DateFormat.SHORT).format(greeting.date) + "]";
    }
}
