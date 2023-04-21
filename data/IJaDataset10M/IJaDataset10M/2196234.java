package test;

import alice.cartago.*;
import alice.cartago.tools.*;

public class TestMyArtifact {

    public static void main(String[] args) throws Exception {
        CartagoService.installNode();
        new ArtifactUser("user").start();
    }
}
