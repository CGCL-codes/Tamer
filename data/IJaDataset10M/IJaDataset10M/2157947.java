package SPI;

import environment.*;
import SPI.*;
import Log.*;

/**
 @author Francesco De Comite (decomite at lifl.fr)
 @version $Revision: 1.0 $ 
 @author Pedro Colla (pcolla@frsf.utn.edu.ar)
*/
public class SPIState extends AbstractState {

    /**
	 *      
	 */
    private static final long serialVersionUID = 1L;

    public SPIPlan spi;

    private String name = "SPI Name v1.0";

    private int length = 40;

    public double NPV = 0.0;

    public double RDF = 0.0;

    public SPIState(IEnvironment ct) {
        super(ct);
        this.spi = new SPIPlan(this.name, this.length);
        this.spi.org = this.spi.addOrganization("MyOrg");
        this.spi.org.addGroup("DEV", "Development Group", this.spi.org.length);
        this.spi.org.resetGroup("DEV", 4.0);
        this.spi.addTask("RM", "Requirements Management", 2, 2.0);
        this.spi.addTask("PPQA", "Product & Process QA", 2, 2.0);
        this.spi.addTask("PP", "Project Planning", 2, 2.0);
        this.spi.addTask("PMC", "Project Management & Control", 2, 2.0);
        this.spi.addTask("MA", "Measurement Analysis", 2, 2.0);
        this.spi.addTask("CM", "Configuration Management", 2, 2.0);
        this.spi.addTask("SAM", "Supplier Agreement Management", 2, 2.0);
        this.spi.addRelation("START", "PPQA");
        this.spi.addRelation("START", "MA");
        this.spi.addRelation("START", "CM");
        this.spi.addRelation("START", "SAM");
        this.spi.addRelation("MA", "PPQA");
        this.spi.addRelation("MA", "PP");
        this.spi.addRelation("PPQA", "END");
        this.spi.addRelation("PP", "PMC");
        this.spi.addRelation("CM", "RM");
        this.spi.addRelation("RM", "PP");
        this.spi.addRelation("PMC", "END");
        this.spi.addRelation("RM", "END");
        this.spi.addRelation("SAM", "END");
        this.spi.addResource("PPQA", "DEV", 2.0);
        this.spi.addResource("PP", "DEV", 2.0);
        this.spi.addResource("MA", "DEV", 2.0);
        this.spi.addResource("PMC", "DEV", 2.0);
        this.spi.addResource("RM", "DEV", 2.0);
        this.spi.addResource("CM", "DEV", 2.0);
        this.spi.addResource("SAM", "DEV", 2.0);
        this.spi.addOwner("PPQA", "DEV");
        this.spi.addOwner("PP", "DEV");
        this.spi.addOwner("MA", "DEV");
        this.spi.addOwner("PMC", "DEV");
        this.spi.addOwner("RM", "DEV");
        this.spi.addOwner("CM", "DEV");
        this.spi.addOwner("SAM", "DEV");
        this.spi.computeCPM();
        this.spi.computeWorkload();
    }

    public IState copy() {
        SPIState st = new SPIState(myEnvironment);
        st.spi = this.spi.clone();
        st.spi.computeCPM();
        st.spi.computeWorkload();
        return (IState) st;
    }

    public int count() {
        return spi.org.count();
    }

    public IState clone() {
        return this.copy();
    }

    /** Q-Learning memorizing techniques use hashcoding : it is necessary to redefine it for each problem/game */
    public int hashCode() {
        int hc = this.spi.hashCode();
        return hc;
    }

    /** Q-Learning memorizing techniques use equality: it is necessary to redefine it for each problem/game */
    public boolean equals(Object o) {
        if (!(o instanceof SPIState)) return false;
        SPIState st = (SPIState) o;
        boolean fl = this.spi.equals(st.spi);
        return fl;
    }

    public String toString() {
        return spi.toString();
    }

    public int nnCodingSize() {
        return 4;
    }

    public double[] nnCoding() {
        double code[] = new double[4];
        code[0] = 1.0;
        code[1] = 1.0;
        code[2] = 1.0;
        code[3] = 1.0;
        return code;
    }
}
