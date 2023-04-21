package alice.semantics.data.assertion;

import java.io.Serializable;
import java.util.List;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * this class represents an semantic individual 
 * @author nardinielena
 *
 */
@SuppressWarnings("serial")
public class IndividualDescr2 implements Serializable {

    private String name;

    private String concept;

    private AbstractRoleFiller[] fillers;

    private Struct struct;

    /**
 * construct the individual specifying name
 * @param name : 
 */
    public IndividualDescr2(String name) {
        super();
        this.name = name;
        this.concept = null;
        this.fillers = null;
        this.struct = this.toStruct();
    }

    /**
	 * construct the individual specifying name concept and roles
	 * @param name : individual name
	 * @param concept : individual appertain concept 
	 * @param fillers : list of individual roles
	 */
    public IndividualDescr2(String name, String concept, AbstractRoleFiller[] fillers) {
        super();
        this.name = name;
        this.concept = concept;
        this.fillers = fillers;
        this.struct = this.toStruct();
    }

    /**
	 * construct the individual specifying name concept and roles
	 * @param name : individual name
	 * @param concept : individual appertain concept 
	 * @param fillers : list of individual roles
	 * @param struct : tuProlog struct of individual 
	 */
    public IndividualDescr2(String name, String concept, AbstractRoleFiller[] fillers, Struct struct) {
        super();
        this.name = name;
        this.concept = concept;
        this.fillers = fillers;
        this.struct = struct;
    }

    /**
	 * get indivual's name
	 * 
	 */
    public String getName() {
        return name;
    }

    /**
	 * get indivual's Concept
	 * 
	 */
    public String getConcept() {
        return concept;
    }

    /**
	 * get indivual's role list
	 * 
	 */
    public AbstractRoleFiller[] getFillers() {
        return fillers;
    }

    /**
	 * get indivual toProlog-like struct
	 * 
	 */
    public Struct getStruct() {
        return struct;
    }

    private Struct toStruct() {
        Struct struct = null;
        if (this.concept == null && this.fillers == null) {
            struct = new Struct("individual", Term.createTerm("name(" + getName() + ")"));
        } else {
            String pvList = "pvlist([";
            for (int i = 0; i < fillers.length; i++) {
                if (fillers[i] instanceof IntegerRoleFiller) {
                    IntegerRoleFiller iRF = (IntegerRoleFiller) fillers[i];
                    List<Integer> values = iRF.getValues();
                    pvList += "pv(" + iRF.getRoleName() + ",";
                    if (values.size() == 1) {
                        if (i == fillers.length - 1) pvList += values.get(0) + ")"; else pvList += values.get(0) + "),";
                    } else {
                        pvList += "[";
                        int j = 0;
                        for (j = 0; j < values.size() - 1; j++) pvList += values.get(j) + ",";
                        if (i == fillers.length - 1) pvList += values.get(j) + "])"; else pvList += values.get(j) + "]),";
                    }
                } else if (fillers[i] instanceof ObjectRoleFiller) {
                    ObjectRoleFiller oRF = (ObjectRoleFiller) fillers[i];
                    List<String> names = oRF.getNames();
                    pvList += "pv(" + oRF.getRoleName() + ",";
                    if (names.size() == 1) {
                        if (i == fillers.length - 1) pvList += "'" + names.get(0) + "')"; else pvList += "'" + names.get(0) + "'),";
                    } else {
                        pvList += "[";
                        int j = 0;
                        for (j = 0; j < names.size() - 1; j++) pvList += names.get(j) + ",";
                        if (i == fillers.length - 1) pvList += names.get(j) + "])"; else pvList += names.get(j) + "]),";
                    }
                } else if (fillers[i] instanceof StringRoleFiller) {
                    StringRoleFiller sRF = (StringRoleFiller) fillers[i];
                    List<String> values = sRF.getValues();
                    pvList += "pv(" + sRF.getRoleName() + ",";
                    if (values.size() == 1) {
                        if (i == fillers.length - 1) pvList += "'" + values.get(0) + "')"; else pvList += "'" + values.get(0) + "'),";
                    } else {
                        pvList += "[";
                        int j = 0;
                        for (j = 0; j < values.size() - 1; j++) pvList += "'" + values.get(j) + "',";
                        if (i == fillers.length - 1) pvList += values.get(j) + "])"; else pvList += values.get(j) + "]),";
                    }
                }
            }
            pvList += "])";
            struct = new Struct("individual", Term.createTerm("name(" + getName() + ")"), Term.createTerm("class('" + getConcept() + "')"), Term.createTerm(pvList));
        }
        return struct;
    }

    /**
	 * get toProlog string of individual
	 * 
	 */
    public String toString() {
        return struct.toString();
    }
}
