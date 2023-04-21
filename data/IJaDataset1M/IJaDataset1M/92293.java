package basic;

import alice.cartago.*;
import alice.cartago.util.*;
import alice.cartago.security.*;

public class UserRoleA extends Agent {

    String role;

    UserCredential cred;

    public UserRoleA(String role, UserCredential cred) {
        super(cred.getId());
        this.role = role;
        this.cred = cred;
    }

    public void run() {
        try {
            joinWorkspace("workplace", role, cred);
            ArtifactId console = lookupArtifact("console");
            use(console, new Op("println", "using default console.."));
            ArtifactId console2 = makeArtifact("my-console", "alice.cartago.util.Console");
            use(console2, new Op("println", "using my console"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
