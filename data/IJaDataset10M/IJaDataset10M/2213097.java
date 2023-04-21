package helloworldjavaimpl;

import hello.HelloData;
import hello.HelloDataPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;
import ops.Participant;

public class MainPublish {

    public static void main(String[] args) {
        try {
            Participant participant = Participant.getInstance("HelloDomain");
            participant.addTypeSupport(new HelloWorld.HelloWorldTypeFactory());
            HelloDataPublisher publisher = new HelloDataPublisher(participant.createTopic("HelloTopic"));
            HelloData data = new HelloData();
            data.helloString = "Hello World From Java!!!";
            data.setKey("java_sample");
            while (true) {
                publisher.write(data);
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(MainPublish.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
