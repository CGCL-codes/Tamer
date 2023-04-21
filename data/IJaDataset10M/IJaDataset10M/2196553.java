package alice.cartago;

public class ManualArtifact extends Artifact {

    private Manual man;

    @OPERATION
    void init(Manual man) throws Exception {
        this.man = man;
        defineObsProperty("src", man.getSource());
    }
}
