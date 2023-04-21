package application.agents.agents3d;

import domain.components.organs.Pollen;

public class PollenAgent extends Abstract3dAgent {

    private static final long serialVersionUID = -417186591498395084L;

    /**
	 * Constructor
	 * 
	 * @param pollen
	 */
    public PollenAgent(Pollen pollen) {
        super(pollen);
    }

    public double getUniformScale() {
        return 2.0d * getPollen().getCarbon_g();
    }

    public Pollen getPollen() {
        return (Pollen) this.getMyComponent();
    }
}
