package org.sbfc.converter.sbml2apm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.util.compilers.*;

/**
 * Produces an infix formula like {@link FormulaCompiler} but removes all the piecewise
 * functions. They are replaced by an id that is unique if you are using the same {@link FormulaCompilerNoPiecewise} instance.
 * The content of the piecewise function is put in a {@link HashMap} and is transformed to use if/then/else.
 * 
 * This class is used for example to create an SBML2APM converter where (in APM) the piecewise operator is not supported.
 * 
 * @author Grigsby
 *
 */
public class APMFormulaCompilerNoPiecewise extends FormulaCompiler {

    private LinkedHashMap<String, String> piecewiseMap = new LinkedHashMap<String, String>();

    private String andReplacement = " & ";

    private String orReplacement = " | ";

    public ASTNodeValue piecewise(List<ASTNode> nodes) throws SBMLException {
        int indexLambda = nodes.indexOf("lambda");
        String piecewiseStr = "";
        String piecewiseEquation = "";
        int nbChildren = nodes.size();
        int nbIfThen = nbChildren / 2;
        boolean otherwise = (nbChildren % 2) == 1;
        int id = piecewiseMap.size() + 1;
        String piecewiseId = "piecew" + id;
        piecewiseStr += "\t!Piecewise Function for this variable:" + piecewiseId + "=";
        for (int i = 0; i < nbIfThen; i++) {
            int index = i * 2;
            if (i > 0) {
                piecewiseStr += "(";
            }
            piecewiseStr += " if (" + nodes.get(index + 1).compile(this).toString() + ") then (" + nodes.get(index).compile(this).toString() + ") else ";
        }
        if (otherwise) {
            piecewiseStr += "" + nodes.get(nbChildren - 1).compile(this).toString() + "";
            piecewiseEquation += "" + nodes.get(nbChildren - 1).compile(this).toString() + "";
        }
        if (nbIfThen > 1) {
            for (int i = 1; i < nbIfThen; i++) {
                piecewiseStr += ")";
            }
        }
        if (indexLambda != -1) {
            piecewiseStr = piecewiseStr.replaceAll("lambda", "");
        }
        if (andReplacement != null) {
            piecewiseStr = piecewiseStr.replaceAll(" and ", andReplacement);
        }
        if (orReplacement != null) {
            piecewiseStr = piecewiseStr.replaceAll(" or ", orReplacement);
        }
        piecewiseMap.put(piecewiseId, piecewiseStr);
        return new ASTNodeValue("" + piecewiseEquation + " ", this);
    }

    /**
	 * Gets a Map of the piecewise expressions that have been transformed.
	 * 
	 * @return a Map of the piecewise expressions that have been transformed.
	 */
    public HashMap<String, String> getPiecewiseMap() {
        return piecewiseMap;
    }

    /**
	 * Gets the String that will be used to replace ' and ' (the mathML <and> element) 
	 * in the boolean expressions.
	 * 
	 * @return the String that will be used to replace ' and ' (the mathML <and> element) 
	 * in the boolean expressions.
	 */
    public String getAndReplacement() {
        return andReplacement;
    }

    /**
	 * Sets the String that will be used to replace ' and ' (the mathML <and> element) 
	 * in the boolean expressions. The default value used is ' & '. If null is given, no replacement
	 * will be performed.
	 * 
	 * @param andReplacement
	 */
    public void setAndReplacement(String andReplacement) {
        this.andReplacement = andReplacement;
    }

    /**
	 * Gets the String that will be used to replace ' or ' (the mathML <or> element) 
	 * in the boolean expressions.
	 * 
	 * @return the String that will be used to replace ' or ' (the mathML <or> element) 
	 * in the boolean expressions.
	 */
    public String getOrReplacement() {
        return orReplacement;
    }

    /**
	 *  Sets the String that will be used to replace ' or ' (the mathML <or> element) 
	 * in the boolean expressions. The default value is ' | '. If null is given, no replacement
	 * will be performed.
	 * 
	 * @param orReplacement
	 */
    public void setOrReplacement(String orReplacement) {
        this.orReplacement = orReplacement;
    }
}
