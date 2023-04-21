package edu.unika.aifb.rules.result;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.semanticweb.kaon2.api.owl.elements.DataProperty;
import org.semanticweb.kaon2.api.owl.elements.OWLClass;
import org.semanticweb.kaon2.api.owl.elements.OWLEntity;
import org.semanticweb.kaon2.api.owl.elements.ObjectProperty;
import edu.unika.aifb.rules.combination.Combination;
import edu.unika.aifb.rules.combination.ManualWeightsSigmoid;
import edu.unika.aifb.rules.input.MyOntology;
import edu.unika.aifb.rules.input.Structure;
import edu.unika.aifb.rules.rules.ManualRuleSimple;
import edu.unika.aifb.rules.rules.Rules;
import edu.unika.aifb.rules.rules.feature.entity.EntityLabel;
import edu.unika.aifb.rules.util.UserInterface;

/**
 * A class to save the results as files.
 * 
 * @author Marc Ehrig
 */
public class Save {

    /**
	 * Saves in the format:
	 * entity1, entity2, confidence value
	 * 
	 * @param list the results
	 * @param fileName the file to save the results to
	 */
    public static void saveShort(ResultList list, String fileName) {
        try {
            File result = new File(fileName);
            FileWriter out = new FileWriter(result);
            Iterator iter = list.objectList().iterator();
            while (iter.hasNext()) {
                Object object1 = iter.next();
                for (int i = 0; i < list.maxRanks(); i++) {
                    Object object2 = list.getObject(object1, i);
                    if (object2 != null) {
                        try {
                            OWLEntity entity1 = (OWLEntity) object1;
                            String label1 = entity1.getURI();
                            out.write(label1 + ";");
                        } catch (Exception e) {
                            out.write(object1.toString() + ";");
                        }
                        double value = list.getValue(object1, i);
                        try {
                            OWLEntity entity2 = (OWLEntity) object2;
                            String label2 = entity2.getURI();
                            out.write(label2 + ";" + value + ";\n");
                        } catch (Exception e) {
                            out.write(object2.toString() + ";" + value + ";\n");
                        }
                    }
                }
            }
            out.close();
            UserInterface.print("Saved " + fileName + "\n");
        } catch (Exception e) {
            UserInterface.print(e.getMessage());
        }
    }

    /**
	 * Saves the most important similarity results including the correct mappings if they are
	 * provided in a file. Format: 
	 * entity1, label1, entity2, label2, overall confidence, (individual rule results)*n, correct value
	 * 
	 * @param list the results
	 * @param evalFile the file containing the correct mappings
	 * @param fileName the file to save the results to
	 */
    public static void saveImportantEval(Structure structure, ResultList list, String evalFile, double cutoff, String fileName) {
        Rules rules = new ManualRuleSimple();
        Combination combination = new ManualWeightsSigmoid(rules.total());
        ResultList resultList = list;
        ResultList resultListLatest = resultList;
        Iterator iter = resultList.objectList().iterator();
        while (iter.hasNext()) {
            Object object1 = iter.next();
            Object object2 = resultListLatest.getObject(object1, 0);
            if (object2 != null) {
                double value = resultListLatest.getValue(object1, 0);
                combination.reset();
                combination.setObjects(object1, object2);
                for (int j = 0; j < rules.total(); j++) {
                    combination.setValue(j, rules.process(object1, object2, j, structure));
                }
                resultList.set(object1, object2, value, combination.getAddInfo());
                resultList.set(object2, object1, value, combination.getAddInfo());
            }
        }
        resultListLatest = resultList;
        saveCompleteEval(structure, resultListLatest, evalFile, cutoff, fileName);
    }

    /**
	 * Saves the complete results including the correct mappings if they are
	 * provided in a file. Format: 
	 * entity1, label1, entity2, label2, overall confidence, (individual rule results)*n, correct value
	 * 
	 * @param list the results
	 * @param evalFile the file containing the correct mappings
	 * @param fileName the file to save the results to
	 */
    public static void saveCompleteEval(Structure structure, ResultList list, String evalFile, double cutoff, String fileName) {
        MyOntology myOntology = (MyOntology) structure;
        Evaluation identity = new Evaluation(evalFile);
        EntityLabel entLabel = new EntityLabel();
        try {
            File result = new File(fileName);
            FileWriter out = new FileWriter(result);
            Iterator iter = list.objectList().iterator();
            while (iter.hasNext()) {
                Object object1 = iter.next();
                for (int i = 0; i < list.maxRanks(); i++) {
                    Object object2 = list.getObject(object1, i);
                    if (object2 != null) {
                        String uri1 = "";
                        String label1 = "";
                        String uri2 = "";
                        String label2 = "";
                        try {
                            OWLEntity entity1 = (OWLEntity) object1;
                            uri1 = entity1.getURI();
                            label1 = (String) entLabel.get(entity1, myOntology);
                            int pos = label1.indexOf('\n');
                            if (pos < 1) pos = label1.length() + 1;
                            label1 = label1.substring(0, pos - 1);
                        } catch (Exception e) {
                        }
                        double value = list.getValue(object1, i);
                        try {
                            OWLEntity entity2 = (OWLEntity) object2;
                            uri2 = entity2.getURI();
                            label2 = (String) entLabel.get(entity2, myOntology);
                            int pos = label2.indexOf('\n');
                            if (pos < 1) pos = label2.length() + 1;
                            label2 = label2.substring(0, pos - 1);
                        } catch (Exception e) {
                        }
                        out.write(uri1 + ";" + label1 + ";" + uri2 + ";" + label2 + ";" + value + ";");
                        Object addInfo = list.getAddInfo(object1, i);
                        if (addInfo != null) {
                            double sims[] = (double[]) addInfo;
                            for (int j = 0; j < sims.length; j++) {
                                out.write(sims[j] + ";");
                            }
                        }
                        out.write(identity.entities(object1, object2) + "\n");
                    }
                }
            }
            UserInterface.print("Saved " + fileName + "\n");
            out.close();
        } catch (Exception e) {
            UserInterface.print(e.getMessage());
        }
    }

    /**
	 * Saves the complete results including the correct mappings if they are
	 * provided in a file. Format: 
	 * entity1, label1, entity2, label2, overall confidence, (individual rule results)*n, correct value
	 * 
	 * @param list the results
	 * @param evalFile the file containing the correct mappings
	 * @param fileName the file to save the results to
	 */
    public static void saveCompleteEvalwoInstances(Structure structure, ResultList list, String evalFile, double cutoff, String fileName) {
        MyOntology myOntology = (MyOntology) structure;
        Evaluation identity = new Evaluation(evalFile);
        EntityLabel entLabel = new EntityLabel();
        Set entitySet = new HashSet();
        try {
            entitySet = myOntology.ontology.createEntityRequest(OWLClass.class).getAll();
            entitySet.addAll(myOntology.ontology.createEntityRequest(ObjectProperty.class).getAll());
            entitySet.addAll(myOntology.ontology.createEntityRequest(DataProperty.class).getAll());
        } catch (Exception e) {
        }
        try {
            File result = new File(fileName);
            FileWriter out = new FileWriter(result);
            Iterator iter = list.objectList().iterator();
            while (iter.hasNext()) {
                Object object1 = iter.next();
                if (entitySet.contains(object1)) {
                    for (int i = 0; i < list.maxRanks(); i++) {
                        Object object2 = list.getObject(object1, i);
                        if (object2 != null) {
                            String uri1 = "";
                            String label1 = "";
                            String uri2 = "";
                            String label2 = "";
                            try {
                                OWLEntity entity1 = (OWLEntity) object1;
                                uri1 = entity1.getURI();
                                label1 = (String) entLabel.get(entity1, myOntology);
                                int pos = label1.indexOf('\n');
                                if (pos < 1) pos = label1.length() + 1;
                                label1 = label1.substring(0, pos - 1);
                            } catch (Exception e) {
                            }
                            double value = list.getValue(object1, i);
                            try {
                                OWLEntity entity2 = (OWLEntity) object2;
                                uri2 = entity2.getURI();
                                label2 = (String) entLabel.get(entity2, myOntology);
                                int pos = label2.indexOf('\n');
                                if (pos < 1) pos = label2.length() + 1;
                                label2 = label2.substring(0, pos - 1);
                            } catch (Exception e) {
                            }
                            out.write(uri1 + ";" + label1 + ";" + uri2 + ";" + label2 + ";" + value + ";");
                            Object addInfo = list.getAddInfo(object1, i);
                            if (addInfo != null) {
                                double sims[] = (double[]) addInfo;
                                for (int j = 0; j < sims.length; j++) {
                                    out.write(sims[j] + ";");
                                }
                            }
                            out.write(identity.entities(object1, object2) + "\n");
                        }
                    }
                }
            }
            UserInterface.print("Saved " + fileName + "\n");
            out.close();
        } catch (Exception e) {
            UserInterface.print(e.getMessage());
        }
    }

    /**
	 * Saves the complete results including the correct mappings if they are
	 * provided in a file. Format: 
	 * entity1, label1, entity2, label2, overall confidence, (individual rule results)*n, correct value
	 * 
	 * @param list the results
	 * @param evalFile the file containing the correct mappings
	 * @param fileName the file to save the results to
	 */
    public static void saveEval(Structure structure, ResultList list, Evaluation identity, double cutoff, String fileName) {
        MyOntology myOntology = (MyOntology) structure;
        try {
            File result = new File(fileName);
            FileWriter out = new FileWriter(result);
            Iterator iter = list.objectList().iterator();
            while (iter.hasNext()) {
                Object object1 = iter.next();
                for (int i = 0; i < list.maxRanks(); i++) {
                    Object object2 = list.getObject(object1, i);
                    if (object2 != null) {
                        String uri1 = "";
                        String uri2 = "";
                        try {
                            OWLEntity entity1 = (OWLEntity) object1;
                            uri1 = entity1.getURI();
                        } catch (Exception e) {
                        }
                        double value = list.getValue(object1, i);
                        try {
                            OWLEntity entity2 = (OWLEntity) object2;
                            uri2 = entity2.getURI();
                        } catch (Exception e) {
                        }
                        out.write(uri1 + ";" + uri2 + ";" + value + ";");
                        out.write(identity.entities(object1, object2) + "\n");
                    }
                }
            }
            UserInterface.print("Saved " + fileName + "\n");
            out.close();
        } catch (Exception e) {
            UserInterface.print(e.getMessage());
        }
    }

    /**
	 * Saves the result vector to disk. Format as in saveShort:
	 * entity1, entity2, confidence value
	 * 
	 * @param vector the result vector
	 * @param fileName obviously where to save the results to
	 */
    public static void saveVector(Vector vector, String fileName) {
        try {
            File result = new File(fileName);
            FileWriter out = new FileWriter(result);
            Iterator iter = vector.iterator();
            while (iter.hasNext()) {
                String[] element = (String[]) iter.next();
                out.write(element[0] + ";" + element[1] + ";" + element[2] + "\n");
            }
            out.close();
            UserInterface.print("Saved " + fileName + "\n");
        } catch (Exception e) {
            UserInterface.print(e.getMessage());
        }
    }
}
