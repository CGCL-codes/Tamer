package pedro.workBench;

public class PedroDeploymentType {

    public static final PedroDeploymentType DESKTOP_DEPLOYMENT = new PedroDeploymentType("desktop_deployment");

    public static final PedroDeploymentType TABLET_DEPLOYMENT = new PedroDeploymentType("tablet_deployment");

    private String name;

    public PedroDeploymentType(String name) {
        this.name = name;
    }
}
