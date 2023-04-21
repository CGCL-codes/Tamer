package test;

import alice.cartago.*;
import alice.cartago.tools.*;
import alice.cartago.util.Agent;

public class TestArtifactIntr2 {

    static class Boot extends Agent {

        public Boot() throws CartagoException {
            super("main");
        }

        public void run() {
            try {
                makeArtifact("myArtifact", "test.ArtifactIntr");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        CartagoService.installNode();
        new Boot().start();
        Thread.sleep(200);
        new ArtifactIntrUser("user1").start();
        Thread.sleep(100);
        new ArtifactIntrUser2("user2", 2).start();
        Thread.sleep(100);
        new ArtifactIntrUser3("user3", 3).start();
    }
}
