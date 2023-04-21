package mySBML.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import mySBML.AbstractSbml;
import mySBML.AssignmentRule;
import mySBML.Compartment;
import mySBML.InitialAssignment;
import mySBML.Model;
import mySBML.Parameter;
import mySBML.Reaction;
import mySBML.SBML;
import mySBML.Species;
import mySBML.lists.ListOfModifiers;
import mySBML.lists.ListOfParameters;
import mySBML.lists.ListOfProducts;
import mySBML.lists.ListOfReactants;

public class BioPEPAConverter {

    private Model model;

    public BioPEPAConverter(SBML sbml) {
        this.model = sbml.getModel();
    }

    public String getBioPEPA() {
        String biopepa = "";
        biopepa += getLocations();
        biopepa += getParameters();
        biopepa += getLocalParameters();
        biopepa += getInitialAmounts();
        biopepa += getKineticLaws();
        biopepa += getSequentialComponents();
        biopepa += getModelComponent();
        return biopepa;
    }

    public String getLocations() {
        String biopepa = "";
        for (int i = 0; i < model.getCompartments().size(); i++) {
            Compartment compartment = model.getCompartments().get(i);
            biopepa += "// location " + compartment.getId().toString() + " : ";
            boolean firstEntered = false;
            if (!compartment.getSize().isEmpty()) {
                biopepa += "size=" + compartment.getSize().toString();
                firstEntered = true;
            }
            if (firstEntered) biopepa += ", "; else firstEntered = true;
            biopepa += "type=compartment;\n";
        }
        biopepa += "\n";
        return biopepa;
    }

    public String getParameters() {
        String biopepa = "";
        biopepa += "omega = 1000;\n";
        for (int i = 0; i < model.getParameters().size(); i++) {
            Parameter param = model.getParameters().get(i);
            ArrayList<AbstractSbml> result = model.getSidMap().lookupReferences(param.getId().toString(), InitialAssignment.class);
            if (result.size() > 0) continue;
            result = model.getSidMap().lookupReferences(param.getId().toString(), AssignmentRule.class);
            if (result.size() > 0) continue;
            biopepa += param.getId().toString() + " = ";
            biopepa += param.getValue().toString() + ";";
            biopepa += "\n";
        }
        biopepa += "\n";
        for (int i = 0; i < model.getParameters().size(); i++) {
            Parameter param = model.getParameters().get(i);
            if (!param.getConstant().equalTo(false)) continue;
            ArrayList<AbstractSbml> result = model.getSidMap().lookupReferences(param.getId().toString(), InitialAssignment.class);
            if (result.size() == 0) continue;
            biopepa += param.getId().toString() + " = ";
            biopepa += ((InitialAssignment) result.get(0)).getMath().toExpression() + ";";
            biopepa += "\n";
        }
        for (int i = 0; i < model.getParameters().size(); i++) {
            Parameter param = model.getParameters().get(i);
            if (!param.getConstant().equalTo(false)) continue;
            ArrayList<AbstractSbml> result = model.getSidMap().lookupReferences(param.getId().toString(), AssignmentRule.class);
            if (result.size() == 0) continue;
            biopepa += param.getId().toString() + " = ";
            biopepa += ((AssignmentRule) result.get(0)).getMath().toExpression() + ";";
            biopepa += "\n";
        }
        return biopepa;
    }

    public String getLocalParameters() {
        String biopepa = "";
        for (int i = 0; i < model.getReactions().size(); i++) {
            Reaction reaction = model.getReactions().get(i);
            if (reaction.getKineticLaw() != null) {
                ListOfParameters params = reaction.getKineticLaw().getParameters();
                for (int j = 0; j < params.size(); j++) {
                    biopepa += params.get(j).getId().toString();
                    biopepa += "__" + reaction.getId() + " = ";
                    biopepa += params.get(j).getValue().toString() + ";";
                    biopepa += "\n";
                }
            }
        }
        biopepa += "\n";
        return biopepa;
    }

    public String getInitialAmounts() {
        String biopepa = "";
        for (int i = 0; i < model.getSpecies().size(); i++) {
            Species species = model.getSpecies().get(i);
            ArrayList<AbstractSbml> result = model.getSidMap().lookupReferences(species.getId().toString(), InitialAssignment.class);
            if (result.size() > 0) continue;
            if (!species.getInitialAmount().isEmpty()) biopepa += "Initial_" + species.getId().toString() + " = " + species.getInitialAmount().toString() + ";\n"; else if (!species.getInitialConcentration().isEmpty()) {
                biopepa += "Initial_conc_" + species.getId().toString() + " = " + species.getInitialConcentration().toString() + ";\n";
                biopepa += "Initial_" + species.getId().toString() + " = floor(Initial_conc_" + species.getId().toString() + "*omega);\n";
            }
        }
        biopepa += "\n";
        for (int i = 0; i < model.getSpecies().size(); i++) {
            Species species = model.getSpecies().get(i);
            if (!species.getConstant().equalTo(false)) continue;
            ArrayList<AbstractSbml> result = model.getSidMap().lookupReferences(species.getId().toString(), InitialAssignment.class);
            if (result.size() == 0) continue;
            biopepa += "Initial_" + species.getId().toString() + " = ";
            biopepa += ((InitialAssignment) result.get(0)).getMath().toExpression() + ";";
            biopepa += "\n";
        }
        return biopepa;
    }

    public String getKineticLaws() {
        String biopepa = "";
        for (int i = 0; i < model.getReactions().size(); i++) {
            Reaction reaction = model.getReactions().get(i);
            if (reaction.getKineticLaw() != null) if (reaction.getKineticLaw().getAstNode() != null) {
                biopepa += "kineticLawOf ";
                biopepa += reaction.getId() + " : ";
                String expression = reaction.getKineticLaw().getAstNode().toExpression();
                String[] localIds = getLocalParameterIds(reaction);
                for (int j = 0; j < localIds.length; j++) expression = expression.replaceAll(localIds[j] + " ", " " + localIds[j] + "__" + reaction.getId() + " ");
                biopepa += expression;
                biopepa += " ;\n";
            }
        }
        biopepa += "\n";
        return biopepa;
    }

    public String getSequentialComponents() {
        String bioPEPA = "";
        for (int i = 0; i < model.getSpecies().size(); i++) {
            Species current = model.getSpecies().get(i);
            bioPEPA += current.getId() + " = ";
            boolean firstEntered = false;
            for (int j = 0; j < model.getReactions().size(); j++) {
                Reaction reaction = model.getReactions().get(j);
                ListOfReactants reactants = reaction.getReactants();
                for (int k = 0; k < reactants.size(); k++) if (current.getId().equalTo(reactants.get(k).getSpeciesID())) if (firstEntered) bioPEPA += " + " + reaction.getId().toString() + " << "; else {
                    bioPEPA += reaction.getId().toString() + " << ";
                    firstEntered = true;
                }
                ListOfProducts products = reaction.getProducts();
                for (int k = 0; k < products.size(); k++) if (current.getId().equalTo(products.get(k).getSpeciesID())) if (firstEntered) bioPEPA += " + " + reaction.getId().toString() + " >> "; else {
                    bioPEPA += reaction.getId().toString() + " >> ";
                    firstEntered = true;
                }
                ListOfModifiers modifiers = reaction.getModifiers();
                for (int k = 0; k < modifiers.size(); k++) if (current.getId().equalTo(modifiers.get(k).getSpeciesID())) if (firstEntered) bioPEPA += " + " + reaction.getId().toString() + " (.) "; else {
                    bioPEPA += reaction.getId().toString() + " (.) ";
                    firstEntered = true;
                }
            }
            bioPEPA += ";\n";
        }
        return bioPEPA;
    }

    public String getModelComponent() {
        String biopepa = "\n\n";
        for (int i = 0; i < model.getSpecies().size(); i++) if (i != model.getSpecies().size() - 1) biopepa += model.getSpecies().get(i).getId().toString() + " [" + "Initial_" + model.getSpecies().get(i).getId().toString() + "] <*>\n"; else biopepa += model.getSpecies().get(i).getId().toString() + " [" + "Initial_" + model.getSpecies().get(i).getId().toString() + "]\n";
        return biopepa;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) throw new IllegalArgumentException("SBML file argument missing!");
        InputStream instream = new FileInputStream(args[0]);
        SBML sbmlDocument = new SBML();
        sbmlDocument.loadModel(instream);
        BioPEPAConverter converter = new BioPEPAConverter(sbmlDocument);
        String biopepaPath = args[0].substring(0, args[0].lastIndexOf(".")) + ".biopepa";
        FileOutputStream stream = new FileOutputStream(biopepaPath);
        String bioPEPA = converter.getBioPEPA();
        for (int i = 0; i < bioPEPA.length(); i++) stream.write(bioPEPA.charAt(i));
    }

    private static String[] getLocalParameterIds(Reaction reaction) {
        ListOfParameters params = reaction.getKineticLaw().getParameters();
        String[] ids = new String[params.size()];
        for (int i = 0; i < params.size(); i++) ids[i] = params.get(i).getId().toString();
        return ids;
    }
}
