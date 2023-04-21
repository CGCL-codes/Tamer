package test;

import alice.cartago.*;

@ARTIFACT_INFO(outports = { @OUTPORT(name = "out-1"), @OUTPORT(name = "out-2") })
public class LinkingArtifact extends Artifact {

    @OPERATION
    void init() throws Exception {
        ArtifactId id = (ArtifactId) invokeOp(getFactoryId(), new Op("makeArtifactProc", "myLog", "test.Log"));
        log("artifact created: " + id);
    }

    @OPERATION
    void test() {
        log("op invoked.");
        try {
            triggerOp("out-1", new Op("inc"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OPERATION
    void test2() {
        log("op invoked.");
        try {
            triggerOp("out-2", new Op("inc"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OPERATION
    void test3() {
        log("op invoked.");
        try {
            int v = (Integer) this.invokeOp("out-1", new Op("getValue"));
            signal("count_value", v);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OPERATION
    void test4() {
        log("op invoked.");
        try {
            ArtifactId id = (ArtifactId) invokeOp(getFactoryId(), new Op("makeArtifactProc", "myLog", "test.Log"));
            log("artifact created: " + id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
