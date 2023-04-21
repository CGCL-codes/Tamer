package test;

import com.duniptech.engine.core.modeling.Coupled;
import com.duniptech.engine.core.modeling.api.IAtomic;

public class Ef extends Coupled {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final String in = "in";

    public static final String out = "out";

    public Ef() {
        super("Ef");
        addInport(Ef.in);
        addOutport(Ef.out);
        Generator genr = new Generator("Generator", 5.0);
        IAtomic transd = new Transducer("Transducer", 100.0);
        addComponent(genr);
        addComponent(transd);
        addCoupling(this, Ef.in, transd, Transducer.solved);
        addCoupling(genr, Generator.out, this, Ef.out);
        addCoupling(genr, Generator.out, transd, Transducer.arrived);
        addCoupling(transd, Transducer.out, genr, Generator.stop);
    }

    public Ef(String name, double period, double observationTime) {
        super(name);
        addInport(Ef.in);
        addOutport(Ef.out);
        Generator genr = new Generator("Generator", period);
        IAtomic transd = new Transducer("Transducer", observationTime);
        addComponent(genr);
        addComponent(transd);
        addCoupling(this, Ef.in, transd, Transducer.solved);
        addCoupling(genr, Generator.out, this, Ef.out);
        addCoupling(genr, Generator.out, transd, Transducer.arrived);
        addCoupling(transd, Transducer.out, genr, Generator.stop);
    }
}
