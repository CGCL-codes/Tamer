package backend.tools.statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXIterator;
import backend.core.AbstractRelation;
import backend.core.ConceptName;
import backend.core.RelationKey;
import backend.core.security.Session;
import backend.query.Query;
import backend.query.QueryEnv;

public class CompareAccessionAndNameMapping {

    private int name = 2;

    private double sloppy = 0.5;

    private int depth = 2;

    public static void main(String[] args) {
        if (args.length == 0) {
            new CompareAccessionAndNameMapping(Session.NONE, "EC_GO").compareMappingMethodsInTwoCVs("EC", "GO", 2);
        }
        if (args.length == 3) {
            System.out.println("ALL parameters given (" + Integer.parseInt(args[0]) + ";" + Double.parseDouble(args[1]) + ";" + Integer.parseInt(args[2]) + ")");
            new CompareAccessionAndNameMapping(Session.NONE, "EC_GO").compareMappingMethodsInTwoCVs("EC", "GO", Integer.parseInt(args[0]), Double.parseDouble(args[1]), Integer.parseInt(args[2]));
        }
    }

    private Session s;

    private QueryEnv qenv;

    CompareAccessionAndNameMapping(Session s, QueryEnv qenv) {
        this.s = s;
        this.qenv = qenv;
    }

    public CompareAccessionAndNameMapping(Session s, String graphname) {
        String ondexDir = System.getProperty("ondex.dir");
        this.s = s;
        try {
            qenv = new QueryEnv(s, ondexDir + File.separator + "dbs" + File.separator + graphname, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void compareMappingMethodsInTwoCVs(String cv1, String cv2, int nameThreshold, double sloppy, int depth) {
        this.name = nameThreshold;
        this.sloppy = sloppy;
        this.depth = depth;
        compareMappingMethodsInTwoCVs(cv1, cv2, this.name);
    }

    public void compareMappingMethodsInTwoCVs(String cv1, String cv2, int nameThreshold) {
        Query oq = qenv.getQuery();
        String evi1 = "ACC";
        String evi2 = "2SYN";
        String evi3 = "S" + this.depth;
        HashMap<RelationKey, AbstractRelation> accRel = new HashMap<RelationKey, AbstractRelation>();
        AbstractONDEXIterator<AbstractRelation> itr = oq.queryRelationsForThisEvidenceType(evi1);
        while (itr.hasNext()) {
            AbstractRelation r = itr.next();
            accRel.put(r.getKey(s), r);
        }
        itr.close();
        System.out.println(evi1 + " Based Rels " + accRel.size());
        HashMap<RelationKey, AbstractRelation> synReal = new HashMap<RelationKey, AbstractRelation>();
        itr = oq.queryRelationsForThisEvidenceType(evi2);
        while (itr.hasNext()) {
            AbstractRelation r = itr.next();
            synReal.put(r.getKey(s), r);
        }
        itr.close();
        HashMap<RelationKey, AbstractRelation> biDirectionalNameBasedRelations = getBidirectionalMappings(synReal);
        System.out.println(evi2 + " Based Rels " + synReal.size());
        HashMap<RelationKey, AbstractRelation> str2Real = new HashMap<RelationKey, AbstractRelation>();
        itr = oq.queryRelationsForThisEvidenceType(evi3);
        while (itr.hasNext()) {
            AbstractRelation r = itr.next();
            str2Real.put(r.getKey(s), r);
        }
        itr.close();
        System.out.println(evi3 + " Based Rels " + str2Real.size());
        HashMap<RelationKey, AbstractRelation> intersection = getIntersection(synReal, accRel);
        HashMap<RelationKey, AbstractRelation> intersectionstr = getIntersection(str2Real, accRel);
        HashMap<RelationKey, AbstractRelation> intersectionstrname = getIntersection(str2Real, synReal);
        HashMap<RelationKey, AbstractRelation> intersectionall = getIntersection(intersection, intersectionstrname);
        HashMap<RelationKey, AbstractRelation> biDirectionalStrucBasedRelations = getBidirectionalMappings(str2Real);
        System.out.println(evi3 + " BI Based Rels " + biDirectionalStrucBasedRelations.size());
        int intersectionaccnameex = intersection.size() - intersectionall.size();
        int intersectionstrex = intersectionstr.size() - intersectionall.size();
        int intersectionstrnameex = intersectionstrname.size() - intersectionall.size();
        System.out.println("Intesection of " + evi1 + " and " + evi2 + " " + intersection.size());
        System.out.println("Intesection of " + evi1 + " and " + evi3 + " " + intersectionstr.size());
        System.out.println("Intesection of " + evi2 + " and " + evi3 + " " + intersectionstrname.size());
        System.out.println("Intesection of all " + intersectionall.size());
        System.out.println("Intesection of " + evi1 + " and " + evi2 + " exclusive " + intersectionaccnameex);
        System.out.println("Intesection of " + evi2 + " and " + evi3 + " exclusive " + intersectionstrnameex);
        System.out.println("Intesection of " + evi1 + " and " + evi3 + " exclusive " + intersectionstrex);
        HashMap<RelationKey, AbstractRelation> accRelMinussynReal = getInSecondThatisNotInFirst(synReal, accRel);
        System.out.println(evi1 + "s not present in " + evi2 + " " + accRelMinussynReal.size());
        HashMap<RelationKey, AbstractRelation> synRealMinusAccRel = getInSecondThatisNotInFirst(accRel, synReal);
        System.out.println(evi2 + "s not present in " + evi1 + " " + synRealMinusAccRel.size());
        HashMap<RelationKey, AbstractRelation> accRelMinusstrReal = getInSecondThatisNotInFirst(str2Real, accRel);
        System.out.println(evi1 + "s not present in " + evi3 + " " + accRelMinusstrReal.size());
        HashMap<RelationKey, AbstractRelation> strRealMinusAccRel = getInSecondThatisNotInFirst(accRel, str2Real);
        System.out.println(evi3 + "s not present in " + evi1 + " " + strRealMinusAccRel.size());
        HashMap<RelationKey, AbstractRelation> strRealMinussynReal = getInSecondThatisNotInFirst(synReal, str2Real);
        System.out.println(evi3 + "s not present in " + evi2 + " " + strRealMinussynReal.size());
        ArrayList<Integer> mappableNameBasedConceptsInEC = new ArrayList<Integer>();
        AbstractONDEXIterator<AbstractConcept> ECconcepts = oq.queryConceptsForThisCV(cv1);
        while (ECconcepts.hasNext()) {
            AbstractConcept con = ECconcepts.next();
            AbstractONDEXIterator<ConceptName> cnit = con.getConceptNames(s);
            if (cnit.size() >= nameThreshold) {
                mappableNameBasedConceptsInEC.add(con.getId(s));
            }
            cnit.close();
            cnit = null;
        }
        ECconcepts.close();
        ECconcepts = null;
        ArrayList<Integer> mappableNameBasedConceptsInGO = new ArrayList<Integer>();
        AbstractONDEXIterator<AbstractConcept> GOconcepts = oq.queryConceptsForThisCV(cv2);
        while (GOconcepts.hasNext()) {
            AbstractConcept con = GOconcepts.next();
            AbstractONDEXIterator<ConceptName> cnit = con.getConceptNames(s);
            if (cnit.size() >= nameThreshold) {
                mappableNameBasedConceptsInGO.add(con.getId(s));
            }
            cnit.close();
            cnit = null;
        }
        GOconcepts.close();
        GOconcepts = null;
        HashMap<RelationKey, AbstractRelation> nameMappableRelation = new HashMap<RelationKey, AbstractRelation>();
        Iterator<RelationKey> accRelIt = accRel.keySet().iterator();
        while (accRelIt.hasNext()) {
            RelationKey relKey = accRelIt.next();
            AbstractRelation relation = accRel.get(relKey);
            boolean fromConceptMappable = false;
            boolean toConceptMappable = false;
            if (relation.getFromConcept(s).getElementOf(s).getId(s).equals(relation.getToConcept(s).getElementOf(s).getId(s))) {
                System.err.println("self relation in CV of a mapping evidence type!!");
                continue;
            }
            if (mappableNameBasedConceptsInEC.contains(relation.getFromConcept(s).getId(s)) || mappableNameBasedConceptsInGO.contains(relation.getFromConcept(s).getId(s))) {
                fromConceptMappable = true;
            }
            if (mappableNameBasedConceptsInEC.contains(relation.getToConcept(s).getId(s)) || mappableNameBasedConceptsInGO.contains(relation.getToConcept(s).getId(s))) {
                toConceptMappable = true;
            }
            if (fromConceptMappable && toConceptMappable) {
                nameMappableRelation.put(relKey, relation);
            }
        }
        HashMap<RelationKey, AbstractRelation> biDirectionalAccBasedRelations = getBidirectionalMappings(accRel);
        System.out.println(accRel.size() - biDirectionalAccBasedRelations.size() + " Relations mapped by accession based mapping are uni directional ");
        System.out.println("Relations that are mapped in accession that are mappable in Name based = " + nameMappableRelation.size());
        System.out.println("Recall of Name Based Mapping = " + ((float) ((float) intersection.size() / (float) nameMappableRelation.size())) * 100 + "%");
        System.out.println("Precision of Name Based Mapping = " + ((float) ((float) intersection.size() / (float) biDirectionalNameBasedRelations.size())) * 100 + "%");
        System.out.println("Recall of Struct Based Mapping = " + ((float) ((float) intersectionstr.size() / (float) accRel.size())) * 100 + "%");
        System.out.println("Precision of Struct Based Mapping = " + ((float) ((float) intersectionstr.size() / (float) str2Real.size())) * 100 + "%");
        try {
            File f = new File("Evaluation_line.txt");
            f.delete();
            System.out.println(synRealMinusAccRel.size() + " Lines written for evaluation table at " + f.getAbsolutePath());
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            out.write("struct(" + this.depth + ")\t");
            out.write(this.sloppy + "\t");
            out.write(accRel.size() / 2 + "\t");
            out.write(intersectionstr.size() / 2 + "\t");
            out.write(strRealMinusAccRel.size() / 2 + "\t");
            out.write("\t");
            float precision = ((float) ((float) intersectionstr.size() / (float) str2Real.size())) * 100;
            float recall = ((float) ((float) intersectionstr.size() / (float) accRel.size())) * 100;
            out.write(precision + "%\t");
            out.write(recall + "%\t");
            out.write("" + (2 * (precision * recall) / (precision + recall)));
            out.write("\n2SYN(" + this.name + ")\t");
            out.write(this.sloppy + "\t");
            out.write(accRel.size() / 2 + "\t");
            out.write(intersection.size() / 2 + "\t");
            out.write(synRealMinusAccRel.size() / 2 + "\t");
            out.write("\t");
            precision = ((float) ((float) intersection.size() / (float) biDirectionalNameBasedRelations.size())) * 100;
            recall = ((float) ((float) intersection.size() / (float) accRel.size())) * 100;
            out.write(precision + "%\t");
            out.write(recall + "%\t");
            out.write("" + (2 * (precision * recall) / (precision + recall)) + "\t");
            out.write(((float) ((float) intersection.size() / (float) nameMappableRelation.size())) * 100 + "%\t");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        try {
            File f = new File("FalsePositivesMapped.txt");
            f.delete();
            System.out.println(synRealMinusAccRel.size() + " False positives listed at " + f.getAbsolutePath());
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            Iterator<AbstractRelation> it = synRealMinusAccRel.values().iterator();
            while (it.hasNext()) {
                AbstractRelation con = it.next();
                out.write("\nRELATION " + con.getKey(s).toString());
                out.write("\nFROM " + con.getFromConcept(s).getPID(s));
                AbstractONDEXIterator<ConceptName> fromIt = con.getFromConcept(s).getConceptNames(s);
                while (fromIt.hasNext()) {
                    out.write("\tNAME " + fromIt.next().getName(s));
                }
                fromIt.close();
                fromIt = null;
                out.write("\nTO " + con.getToConcept(s).getPID(s));
                AbstractONDEXIterator<ConceptName> toIt = con.getToConcept(s).getConceptNames(s);
                while (toIt.hasNext()) {
                    out.write("\tNAME " + toIt.next().getName(s));
                }
                toIt.close();
                toIt = null;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        count = 0;
        try {
            File f = new File("structnotinnames.txt");
            f.delete();
            System.out.println(strRealMinussynReal.size() + " False structs listed at " + f.getAbsolutePath());
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            Iterator<AbstractRelation> it = strRealMinussynReal.values().iterator();
            while (it.hasNext()) {
                AbstractRelation con = it.next();
                out.write("\nRELATION " + con.getKey(s).toString());
                out.write("\nFROM " + con.getFromConcept(s).getPID(s));
                AbstractONDEXIterator<ConceptName> fromIt = con.getFromConcept(s).getConceptNames(s);
                while (fromIt.hasNext()) {
                    out.write("\tNAME " + fromIt.next().getName(s));
                }
                fromIt.close();
                fromIt = null;
                out.write("\nTO " + con.getToConcept(s).getPID(s));
                AbstractONDEXIterator<ConceptName> toIt = con.getToConcept(s).getConceptNames(s);
                while (toIt.hasNext()) {
                    out.write("\tNAME " + toIt.next().getName(s));
                }
                toIt.close();
                toIt = null;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        count = 0;
        try {
            File f = new File("FalseNegativesMappedStruct.txt");
            f.delete();
            System.out.println(strRealMinusAccRel.size() + " False negatives listed at " + f.getAbsolutePath());
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            Iterator<AbstractRelation> it = accRelMinusstrReal.values().iterator();
            while (it.hasNext()) {
                AbstractRelation con = it.next();
                out.write("\nRELATION " + con.getKey(s).toString());
                out.write("\nFROM " + con.getFromConcept(s).getPID(s));
                AbstractONDEXIterator<ConceptName> fromIt = con.getFromConcept(s).getConceptNames(s);
                while (fromIt.hasNext()) {
                    out.write("\tNAME " + fromIt.next().getName(s));
                }
                fromIt.close();
                fromIt = null;
                out.write("\nTO " + con.getToConcept(s).getPID(s));
                AbstractONDEXIterator<ConceptName> toIt = con.getToConcept(s).getConceptNames(s);
                while (toIt.hasNext()) {
                    out.write("\tNAME " + toIt.next().getName(s));
                }
                toIt.close();
                toIt = null;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        count = 0;
        try {
            File f = new File("FalsePositivesMappedStruct.txt");
            f.delete();
            System.out.println(strRealMinusAccRel.size() + " False positives listed at " + f.getAbsolutePath());
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            Iterator<AbstractRelation> it = strRealMinusAccRel.values().iterator();
            while (it.hasNext()) {
                AbstractRelation con = it.next();
                out.write("\nRELATION " + con.getKey(s).toString());
                out.write("\nFROM " + con.getFromConcept(s).getPID(s));
                AbstractONDEXIterator<ConceptName> fromIt = con.getFromConcept(s).getConceptNames(s);
                while (fromIt.hasNext()) {
                    out.write("\tNAME " + fromIt.next().getName(s));
                }
                fromIt.close();
                fromIt = null;
                out.write("\nTO " + con.getToConcept(s).getPID(s));
                AbstractONDEXIterator<ConceptName> toIt = con.getToConcept(s).getConceptNames(s);
                while (toIt.hasNext()) {
                    out.write("\tNAME " + toIt.next().getName(s));
                }
                toIt.close();
                toIt = null;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File f = new File("GO2EC.txt");
            f.delete();
            System.out.println(accRel.size() + " acc based mappings listed at " + f.getAbsolutePath());
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            Iterator<AbstractRelation> it = accRel.values().iterator();
            while (it.hasNext()) {
                AbstractRelation con = it.next();
                if (con.getFromConcept(s).getElementOf(s).getFullname(s).equals("EC")) {
                    out.write("EC:" + con.getFromConcept(s).getPID(s));
                    AbstractONDEXIterator<ConceptName> toIt = con.getToConcept(s).getConceptNames(s);
                    ConceptName go = toIt.next();
                    out.write(" > GO:" + go.getName(s));
                    out.write(" ; " + con.getToConcept(s).getPID(s) + "\n");
                    toIt.close();
                    toIt = null;
                    count++;
                }
            }
            out.write("\nin total relations: " + count);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<RelationKey, AbstractRelation> getBidirectionalMappings(HashMap<RelationKey, AbstractRelation> map) {
        HashMap<RelationKey, AbstractRelation> biDirectionalRelations = new HashMap<RelationKey, AbstractRelation>();
        Iterator<AbstractRelation> accessRelIt = map.values().iterator();
        while (accessRelIt.hasNext()) {
            AbstractRelation relation = accessRelIt.next();
            if (biDirectionalRelations.containsValue(relation)) continue;
            Iterator<AbstractRelation> accRelItComp = map.values().iterator();
            while (accRelItComp.hasNext()) {
                AbstractRelation relationComp = accRelItComp.next();
                if (relationComp.getToConcept(s).equals(relation.getFromConcept(s)) && relationComp.getFromConcept(s).equals(relation.getToConcept(s))) {
                    biDirectionalRelations.put(relation.getKey(s), relation);
                    biDirectionalRelations.put(relationComp.getKey(s), relationComp);
                }
            }
        }
        return biDirectionalRelations;
    }

    /**
	 * The sum of the two
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
    public HashMap<RelationKey, AbstractRelation> getUnion(HashMap<RelationKey, AbstractRelation> map1, HashMap<RelationKey, AbstractRelation> map2) {
        HashMap<RelationKey, AbstractRelation> newMap = new HashMap<RelationKey, AbstractRelation>();
        newMap.putAll(map1);
        newMap.putAll(map2);
        return newMap;
    }

    /**
	 * i.e. that which is overlaped in both
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
    public HashMap<RelationKey, AbstractRelation> getIntersection(HashMap<RelationKey, AbstractRelation> map1, HashMap<RelationKey, AbstractRelation> map2) {
        HashMap<RelationKey, AbstractRelation> newMap = new HashMap<RelationKey, AbstractRelation>();
        Iterator<RelationKey> relIt = map1.keySet().iterator();
        while (relIt.hasNext()) {
            RelationKey relationKey = relIt.next();
            if (map2.containsKey(relationKey)) {
                newMap.put(relationKey, map1.get(relationKey));
            }
        }
        return newMap;
    }

    /**
	 * I.e. that which is in the second that is not present in the first
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
    public HashMap<RelationKey, AbstractRelation> getInSecondThatisNotInFirst(HashMap<RelationKey, AbstractRelation> map1, HashMap<RelationKey, AbstractRelation> map2) {
        HashMap<RelationKey, AbstractRelation> newMap = new HashMap<RelationKey, AbstractRelation>();
        Iterator<RelationKey> relIt = map2.keySet().iterator();
        while (relIt.hasNext()) {
            RelationKey relationKey = relIt.next();
            if (!map1.containsKey(relationKey)) {
                newMap.put(relationKey, map2.get(relationKey));
            }
        }
        return newMap;
    }
}
