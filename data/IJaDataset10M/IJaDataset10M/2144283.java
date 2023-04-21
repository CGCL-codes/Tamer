package backend.query;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.lucene.queryParser.precedence.PrecedenceQueryParser;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXIterator;
import backend.core.AttributeName;
import backend.core.CV;
import backend.core.ConceptClass;
import backend.core.EvidenceType;
import backend.core.index.IndexONDEXGraph;
import backend.core.searchable.LuceneEnv;
import backend.core.security.Session;

/**
 * This class is used by Query class and handles concept queries overgiven by the Query class. 
 * (The class is NOT for user queries).
 * @author sierenk
 */
public class ConceptQuery {

    IndexONDEXGraph graph;

    LuceneEnv lenv;

    Session s;

    protected ConceptQuery(Session s, IndexONDEXGraph graph, LuceneEnv lenv) {
        this.s = s;
        this.graph = graph;
        this.lenv = lenv;
    }

    private ConceptQuery() {
    }

    ;

    /**
	* Searchs for this concept name in ondex graph. 
	* @param a concept name to search for
	* @return hashtable with matching concepts  
	*/
    protected Hashtable<Integer, AbstractConcept> queryForThisConceptName(String coName) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        AbstractONDEXIterator<AbstractConcept> concepts = lenv.searchConceptByConceptName(escapeChars(coName));
        while (concepts.hasNext()) {
            AbstractConcept c = concepts.next();
            searchResult.put(c.getId(s), c);
        }
        return searchResult;
    }

    /**
	* Searchs for this concept description in ondex graph. 
	* @param a concept description to search for
	* @return hashtable with matching concepts  
	*/
    protected Hashtable<Integer, AbstractConcept> queryForThisConceptDescription(String desc) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        AbstractONDEXIterator<AbstractConcept> concepts = lenv.searchConceptByDescription(escapeChars(desc));
        while (concepts.hasNext()) {
            AbstractConcept c = concepts.next();
            searchResult.put(c.getId(s), c);
        }
        return searchResult;
    }

    protected Hashtable<Integer, AbstractConcept> queryForThisConceptAccession(String coAcc) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        AbstractONDEXIterator<AbstractConcept> concepts = lenv.searchConceptByConceptAccession(escapeChars(coAcc));
        while (concepts.hasNext()) {
            AbstractConcept c = concepts.next();
            searchResult.put(c.getId(s), c);
        }
        return searchResult;
    }

    /**
 	 * 	
 	 * Returns a list of AbstractConcepts which have a Concept GDS with 
 	 * this value.
 	 * @param gdsValue the value of the concept GDS 	
 	 * @return hashtable of matching concepts
 	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisConceptGDSValue(String gdsValue) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        AbstractONDEXIterator<AbstractConcept> concepts = lenv.searchConceptByConceptGDS(escapeChars(gdsValue));
        while (concepts.hasNext()) {
            AbstractConcept hit = concepts.next();
            searchResult.put(hit.getId(s), hit);
        }
        return searchResult;
    }

    /**
	 * Returns a list of AbstractConcepts which have a ConceptGDS with 
	 * this name.
	 * @param gdsAttrName the name of the concept GDS
	 *
	 * @return list of concepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisConceptGDSName(String gdsAttrName) {
        Hashtable<Integer, AbstractConcept> tempResult = new Hashtable<Integer, AbstractConcept>();
        Hashtable<Integer, AbstractConcept> finalSearchResult = new Hashtable<Integer, AbstractConcept>();
        AttributeName attrname = graph.getONDEXGraphData(s).getAttributeName(s, gdsAttrName);
        if (attrname != null) {
            IntIterator it = graph.getAllConceptIDs();
            if (it != null) {
                while (it.hasNext()) {
                    AbstractConcept c = graph.getConcept(it.nextInt());
                    if (c.getConceptGDS(s, attrname) != null) {
                        if ((c.getConceptGDS(s, attrname)).getAttrname(s).getName(s).equals(gdsAttrName)) {
                            tempResult.put(c.getId(s), c);
                        }
                    }
                }
            }
            finalSearchResult = tempResult;
            return finalSearchResult;
        } else return finalSearchResult;
    }

    /**
	 * Returns a list of AbstractConcepts which have a ConceptGDS with 
	 * this name and this value.
	 * @param gdsAttrName the name of the concept GDS
	 * @param gdsValue the value of the concept GDS
	 * @return list of concepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisConceptGDS(String gdsAttrName, String gdsValue) {
        Collection<AbstractConcept> searchResultforValue = queryForThisConceptGDSValue(gdsValue).values();
        Hashtable<Integer, AbstractConcept> finalSearchResult = new Hashtable<Integer, AbstractConcept>();
        AttributeName attrname = graph.getONDEXGraphData(s).getAttributeName(s, gdsAttrName);
        if (attrname != null) {
            Iterator<AbstractConcept> it_found = searchResultforValue.iterator();
            while (it_found.hasNext()) {
                AbstractConcept hit = it_found.next();
                if (hit.getConceptGDS(s, attrname) != null) {
                    finalSearchResult.put(hit.getId(s), hit);
                }
            }
            return finalSearchResult;
        } else return finalSearchResult;
    }

    /**
	 * Returns a hashtable of AbstractConcepts with this ConceptClass Attributname.
	 * @param ccname ConceptClass name
	 * @return  a hashtable of matching AbstractConcepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisConceptClass(String ccname) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        ConceptClass cc = graph.getONDEXGraphData(s).getConceptClass(s, ccname);
        if (cc != null) {
            IntOpenHashSet conceptIds = graph.getAllConceptIDsOfCC(ccname);
            if (conceptIds != null) {
                IntIterator it = conceptIds.iterator();
                AbstractConcept concept = null;
                while (it.hasNext()) {
                    concept = graph.getConcept(it.nextInt());
                    searchResult.put(concept.getId(s), concept);
                }
            }
            return searchResult;
        } else return searchResult;
    }

    /**
	 * Returns a hashtable of AbstractConcepts with this Control Vocabulary (CV) name.
	 *
	 * @param cvname CV name to search for
	 * @return  a hashtable of matching Concepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisCV(String cvname) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        CV cv = graph.getONDEXGraphData(s).getCV(s, cvname);
        if (cv != null) {
            IntOpenHashSet conceptIds = graph.getAllConceptIDsOfCV(cvname);
            if (conceptIds != null) {
                IntIterator it = conceptIds.iterator();
                AbstractConcept concept = null;
                while (it.hasNext()) {
                    concept = graph.getConcept(it.nextInt());
                    searchResult.put(concept.getId(s), concept);
                }
            }
            return searchResult;
        } else return searchResult;
    }

    protected Hashtable<Integer, AbstractConcept> queryConceptsForThisEvidenceType(String evname) {
        Hashtable<Integer, AbstractConcept> finalSearchResult = new Hashtable<Integer, AbstractConcept>();
        EvidenceType evidenceType = graph.getONDEXGraphData(s).getEvidenceType(s, evname);
        if (evidenceType != null) {
            IntIterator it = graph.getAllConceptIDs();
            if (it != null) {
                while (it.hasNext()) {
                    AbstractConcept hit = graph.getConcept(s, it.next());
                    Iterator<EvidenceType> it_et = hit.getEvidence(s);
                    while (it_et.hasNext()) {
                        if (it_et.next().equals(evidenceType)) {
                            finalSearchResult.put(hit.getId(s), hit);
                            break;
                        }
                    }
                }
            }
            return finalSearchResult;
        } else return finalSearchResult;
    }

    /**
	 * Returns a hashtable of AbstractConcepts with these Control Vocabulary (CV) name.
	 * The method accepts a <code>String</code> of comma separated CV names. 
	 * @param cvNames comma separated CV names (eg. DRA,KEGG,...)
	 * @return  a hashtable of matching AbstractConcepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisCommaSeparatedCVs(String cvNames) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        StringTokenizer st = new StringTokenizer(cvNames, ",");
        while (st.hasMoreTokens()) {
            String cv = st.nextToken();
            Hashtable<Integer, AbstractConcept> searchTemp = this.queryForThisCV(cv);
            searchResult.putAll(searchTemp);
        }
        return searchResult;
    }

    /**
	 * Returns a hashtable of AbstractConcepts with these GDS values.
	 * The method accepts a <code>String</code> of comma separated GDS values. 
	 * @param gdsValues comma separated GDS values (eg. 123,456,345)
	 * @return  a hashtable of matching AbstractConcepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisCommaSeparatedGDSValues(String gdsValues) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        StringTokenizer st = new StringTokenizer(gdsValues, ",");
        while (st.hasMoreTokens()) {
            String gdsv = st.nextToken();
            Hashtable<Integer, AbstractConcept> searchTemp = this.queryForThisConceptGDSValue(gdsv);
            searchResult.putAll(searchTemp);
        }
        return searchResult;
    }

    /**
	 * Returns a hashtable of AbstractConcepts with these Concept Classes.
	 * The method accepts a <code>String</code> of comma separated Concept Class names. 
	 * @param ccNames comma separated concept class names(eg. family,species,...)
	 * @return  a hashtable of matching AbstractConcepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisCommaSeparatedConceptClasses(String ccNames) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        StringTokenizer st = new StringTokenizer(ccNames, ",");
        while (st.hasMoreTokens()) {
            String cc = st.nextToken();
            Hashtable<Integer, AbstractConcept> searchTemp = this.queryForThisConceptClass(cc);
            searchResult.putAll(searchTemp);
        }
        return searchResult;
    }

    /**
	 * Returns a hashtable of AbstractConcepts with these Concept names.
	 * The method accepts a <code>String</code> of comma separated Concept names. 
	 * @param ccNames comma separated concept names(eg. european beech, norway spruce,...)
	 * @return  a hashtable of matching AbstractConcepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisCommaSeparatedConceptNames(String coNames) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        StringTokenizer st = new StringTokenizer(coNames, ",");
        while (st.hasMoreTokens()) {
            String co = st.nextToken();
            Hashtable<Integer, AbstractConcept> searchTemp = this.queryForThisConceptName(co);
            searchResult.putAll(searchTemp);
        }
        return searchResult;
    }

    /**
	 * Returns a hashtable of AbstractConcepts with these Concept Accession numbers.
	 * The method accepts a <code>String</code> of comma separated Concept Accession numbers . 
	 * @param coAccessions comma separated concept accession numbers (eg. At2G5543,At2G4456,...)
	 * @return  a hashtable of matching AbstractConcepts
	 */
    protected Hashtable<Integer, AbstractConcept> queryForThisCommaSeparatedConceptAccessions(String coAccessions) {
        Hashtable<Integer, AbstractConcept> searchResult = new Hashtable<Integer, AbstractConcept>();
        StringTokenizer st = new StringTokenizer(coAccessions, ",");
        while (st.hasMoreTokens()) {
            String coAcc = st.nextToken();
            Hashtable<Integer, AbstractConcept> searchTemp = this.queryForThisConceptAccession(coAcc);
            searchResult.putAll(searchTemp);
        }
        return searchResult;
    }

    private String escapeChars(String s) {
        return PrecedenceQueryParser.escape(s);
    }
}
