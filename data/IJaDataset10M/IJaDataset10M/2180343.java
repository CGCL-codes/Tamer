package gr.konstant.powder.rdfsp.sesame;

class RDFSRules {

    /** rule rdf1: 
	 * xxx aaa yyy --> aaa rdf:type rdf:Property
	 */
    public static final int Rdf1 = 0;

    /** rule rdfs2_1:
	 * xxx aaa yyy &&           (nt)
	 * aaa rdfs:domain zzz -->  (t1)
	 * xxx rdf:type zzz         (t2)
	 */
    public static final int Rdfs2_1 = 1;

    /** rule rdfs2_2:
	 * aaa rdfs:domain zzz &&  (nt)
	 * xxx aaa yyy -->         (t1)
	 * xxx rdf:type zzz        (t2)
	 */
    public static final int Rdfs2_2 = 2;

    /** rule rdfs3_1:
	 * xxx aaa uuu &&          (nt)
	 * aaa rdfs:range zzz -->  (t1)
	 * uuu rdf:type zzz        (t2)
	 */
    public static final int Rdfs3_1 = 3;

    /** rule rdfs3_2.
	 * aaa rdfs:range zzz &&  (nt)
	 * xxx aaa uuu -->        (t1)
	 * uuu rdf:type zzz       (t2)
	 */
    public static final int Rdfs3_2 = 4;

    /** rule rdfs4a: 
	 * xxx aaa yyy --> xxx rdf:type rdfs:Resource
	 */
    public static final int Rdfs4a = 5;

    /** rule rdfs4b: 
	 * xxx aaa uuu --> uuu rdf:type rdfs:Resource
	 */
    public static final int Rdfs4b = 6;

    /** rule rdfs5_1: 
	 * aaa rdfs:subPropertyOf bbb &&   (nt)
	 * bbb rdfs:subPropertyOf ccc -->  (t1)
	 * aaa rdfs:subPropertyOf ccc      (t2)
	 * 
	 * transitivity of rdfs:subPropertyOf
	 */
    public static final int Rdfs5_1 = 7;

    /** rule rdfs5_2:
	 * bbb rdfs:subPropertyOf ccc && (nt)
	 * aaa rdfs:subPropertyOf bbb -->  (t1)
	 * aaa rdfs:subPropertyOf ccc      (t2)
	 * 
	 * transitivity of rdfs:subPropertyOf
	 */
    public static final int Rdfs5_2 = 8;

    /** rule rdfs6: 
	 * xxx rdf:type rdf:Property --> xxx rdfs:subPropertyOf xxx
	 * 
	 * reflexivity of rdfs:subPropertyOf
	 */
    public static final int Rdfs6 = 9;

    /** rule rdfs7_1:
	 * xxx aaa yyy &&                  (nt)
	 * aaa rdfs:subPropertyOf bbb -->  (t1)
	 * xxx bbb yyy                     (t2)
	 */
    public static final int Rdfs7_1 = 10;

    /** rule rdfs7_2:
	 * aaa rdfs:subPropertyOf bbb &&  (nt)
	 * xxx aaa yyy -->                (t1)
	 * xxx bbb yyy                    (t2)
	 */
    public static final int Rdfs7_2 = 11;

    /** rule rdfs8:
	 *  xxx rdf:type rdfs:Class --> xxx rdfs:subClassOf rdfs:Resource 
	 */
    public static final int Rdfs8 = 12;

    /** rule rdfs9_1:
	 * xxx rdfs:subClassOf yyy &&  (nt)
	 * aaa rdf:type xxx -->        (t1)
	 * aaa rdf:type yyy            (t2)
	 */
    public static final int Rdfs9_1 = 13;

    /** rule rdfs9_2:
	 * aaa rdf:type xxx &&          (nt)
	 * xxx rdfs:subClassOf yyy -->  (t1)
	 * aaa rdf:type yyy             (t2)
	 */
    public static final int Rdfs9_2 = 14;

    /** rule rdfs10:
	 * xxx rdf:type rdfs:Class --> xxx rdfs:subClassOf xxx
	 * 
	 * reflexivity of rdfs:subClassOf
	 */
    public static final int Rdfs10 = 15;

    /** rule rdfs11_1:
	 * xxx rdfs:subClassOf yyy &&  (nt)
	 * yyy rdfs:subClassOf zzz -->  (t1)
	 * xxx rdfs:subClassOf zzz      (t2)
	 * 
	 * transitivity of rdfs:subClassOf
	 */
    public static final int Rdfs11_1 = 16;

    /** rule rdfs11_2:
	 * yyy rdfs:subClassOf zzz &&  (nt)
	 * xxx rdfs:subClassOf yyy -->  (t1)
	 * xxx rdfs:subClassOf zzz      (t2)
	 * 
	 * transitivity of rdfs:subClassOf
	 */
    public static final int Rdfs11_2 = 17;

    /** rule rdfs12:
	 * xxx rdf:type rdfs:ContainerMembershipProperty -->
	 *     xxx rdfs:subPropertyOf rdfs:member
	 */
    public static final int Rdfs12 = 18;

    /** rule rdfs13:  
	 * xxx rdf:type rdfs:Datatype --> xxx rdfs:subClassOf rdfs:Literal
	 */
    public static final int Rdfs13 = 19;

    /** rule X1:
	 * xxx rdf:_* yyy -->
	 * rdf:_* rdf:type rdfs:ContainerMembershipProperty
	 *
	 * This is an extra rule for list membership properties (_1, _2, _3,
	 * ...). The RDF Semantics Recommendation does not specificy a production for this, instead
	 * these statements are considered axiomatic. Since it is an infinite set it can not, in practice,
	 * be asserted axiomatically. 
	 */
    public static final int RX1 = 20;

    /** rule P:
	 * xxx rdf:type rdfs:Resource --> xxx aaa yyy
	 *
	 * This rule implements POWDER.
	 */
    public static final int RP = 21;

    public static final int RULECOUNT = 22;

    public static final String[] RULENAMES = { "   Rdf1", " Rdfs2_1", " Rdfs2_2", " Rdfs3_1", " Rdfs3_2", "  Rdfs4a", "  Rdfs4b", "Rdfs5_1", "Rdfs5_2", "  Rdfs6", " Rdfs7_1", " Rdfs7_2", "  Rdfs8", " Rdfs9_1", " Rdfs9_2", "  Rdfs10", " Rdfs11_1", " Rdfs11_2", "  Rdfs12", "  Rdfs13", "  RX1", "  RP" };

    private static final boolean _ = false;

    private static final boolean X = true;

    /**
	 * Table of triggers for entailment rules. Each column represents the triggers for 
	 * an entailment rule R, that is, it encodes which entailment rules produces statements
	 * that can be used as a premise in rule R.
	 * 
	 * Example: the conclusion of rule rdfs2_1 is a statement of the form: (xxx rdf:type yyy). 
	 * The premise of rule rdfs9_2 is (xxx rdf:type yyy). Hence, rule rdfs2_1 triggers rule
	 * rdfs9_2.  
	 */
    public static final boolean[][] TRIGGERS = { { _, X, _, X, _, X, _, _, _, X, X, _, _, _, X, _, _, _, _, _, _, _ }, { _, X, _, X, _, _, _, _, _, X, X, _, X, _, X, X, _, _, X, X, _, _ }, { _, X, _, X, _, _, _, _, _, X, X, _, X, _, X, X, _, _, X, X, _, _ }, { _, X, _, X, _, _, _, _, _, X, X, _, X, _, X, X, _, _, X, X, _, _ }, { _, X, _, X, _, _, _, _, _, X, X, _, X, _, X, X, _, _, X, X, _, _ }, { _, X, _, X, _, _, _, _, _, _, X, _, _, _, X, _, _, _, _, _, _, _ }, { _, X, _, X, _, _, _, _, _, _, X, _, _, _, X, _, _, _, _, _, _, _ }, { _, _, _, _, _, _, _, X, X, _, X, X, _, _, _, _, _, _, _, _, _, _ }, { _, _, _, _, _, _, _, X, X, _, X, X, _, _, _, _, _, _, _, _, _, _ }, { _, X, _, X, _, _, _, _, _, _, X, X, _, _, _, _, _, _, _, _, _, _ }, { _, X, X, X, X, _, _, X, X, X, X, X, X, X, X, X, X, X, X, X, X, _ }, { _, X, X, X, X, _, _, X, X, X, X, X, X, X, X, X, X, X, X, X, X, _ }, { _, X, _, X, _, _, _, _, _, _, X, _, _, X, _, _, X, X, _, _, _, _ }, { _, _, _, X, _, _, _, _, _, X, X, _, X, _, X, X, _, _, X, X, _, _ }, { _, _, _, X, _, _, _, _, _, X, X, _, X, _, X, X, _, _, X, X, _, _ }, { _, X, _, X, _, _, _, _, _, _, X, _, _, _, _, _, _, _, _, _, _, _ }, { _, _, _, _, _, _, _, _, _, _, X, _, _, X, _, _, X, X, _, _, _, _ }, { _, _, _, _, _, _, _, _, _, _, X, _, _, X, _, _, X, X, _, _, _, _ }, { _, X, _, X, _, _, X, X, X, _, X, X, _, _, _, _, _, _, _, _, _, _ }, { _, X, _, X, _, _, _, _, _, _, X, _, _, X, _, _, X, X, _, _, _, _ }, { _, X, _, X, _, _, _, _, _, _, X, _, _, _, X, _, _, _, X, _, _, _ }, { _, _, _, _, _, _, _, _, _, _, X, X, _, X, X, _, _, _, _, _, _, _ } };
}
