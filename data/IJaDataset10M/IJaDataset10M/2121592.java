package de.tum.in.botl.ruleSet.implementation;

import de.tum.in.botl.metamodel.interfaces.ClassAssociationEndInterface;
import de.tum.in.botl.metamodel.interfaces.ClassAssociationInterface;
import de.tum.in.botl.ruleSet.interfaces.ModelVariableInterface;
import de.tum.in.botl.ruleSet.interfaces.ObjectVariableAssociationEndInterface;
import de.tum.in.botl.ruleSet.interfaces.ObjectVariableAssociationInterface;
import de.tum.in.botl.ruleSet.interfaces.ObjectVariableInterface;
import de.tum.in.botl.util.InstanceAssociationEnd;

/**
 * <p>?berschrift: Systementwicklungsprojekt</p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Organisation: </p>
 * @author Georgi Todorov
 * @version 1.0
 */
public class ObjectVariableAssociation implements ObjectVariableAssociationInterface {

    /**
   * A unique identifier
   */
    private String id;

    /**
   * A reference to the first object variable association end
   */
    private ObjectVariableAssociationEnd end0;

    /**
   * A reference to the second object variable association end
   */
    private ObjectVariableAssociationEnd end1;

    /**
   * A reference to model variable
   */
    private ModelVariable modelVariable;

    /**
   * A reference to a class association
   */
    private ClassAssociationInterface classAssociation;

    /**
   * The Cardinality
   */
    private int card = 1;

    /**
   * If one of the two ends of an object variable associaton is connected
   * to a negative object variable, then this flag will be set to true 
   */
    private boolean isNegativeContext;

    private ObjectVariableAssociation() {
    }

    protected ObjectVariableAssociation(ClassAssociationEndInterface cae0, ObjectVariableInterface ov0, ClassAssociationEndInterface cae1, ObjectVariableInterface ov1, int card, boolean isNegativeContext) throws RuleSetException {
        ObjectVariableAssociationEnd firstEnd = new ObjectVariableAssociationEnd(cae0, ov0, null);
        ObjectVariableAssociationEnd secondEnd = new ObjectVariableAssociationEnd(cae1, ov1, null);
        init(firstEnd, secondEnd, card, isNegativeContext, null);
    }

    protected ObjectVariableAssociation(ClassAssociationEndInterface cae0, ObjectVariableInterface ov0, ClassAssociationEndInterface cae1, ObjectVariableInterface ov1, int card, boolean isNegativeContext, String ovaId, String end0Id, String end1Id) throws RuleSetException {
        ObjectVariableAssociationEnd firstEnd = new ObjectVariableAssociationEnd(cae0, ov0, end0Id);
        ObjectVariableAssociationEnd secondEnd = new ObjectVariableAssociationEnd(cae1, ov1, end1Id);
        init(firstEnd, secondEnd, card, isNegativeContext, ovaId);
    }

    private void init(ObjectVariableAssociationEnd firstEnd, ObjectVariableAssociationEnd secondEnd, int card, boolean isNegativeContext, String ovaId) throws RuleSetException {
        if (firstEnd.getClassAssociationEnd().getClassAssociation() != secondEnd.getClassAssociationEnd().getClassAssociation()) throw new RuleSetException("Inconsistent end types for new object variable association.");
        if (card < 1) throw new RuleSetException("Invalid cardinality of " + card + " for new object variable association.");
        this.setClassAssociation(firstEnd.getClassAssociationEnd().getClassAssociation());
        this.setEnd0(firstEnd);
        this.setEnd1(secondEnd);
        this.setNegativeContext(isNegativeContext);
        setNegativeContextFlag();
        ((ObjectVariable) firstEnd.getObjectVariable()).addObjectVariableAssociationEnd(firstEnd);
        ((ObjectVariable) secondEnd.getObjectVariable()).addObjectVariableAssociationEnd(secondEnd);
        if (ovaId == null) this.setId(RuleSetFactory.getNewId(this)); else this.setId(ovaId);
    }

    /**
   * Returns the first end of this object variable association
   * @return
   */
    public ObjectVariableAssociationEndInterface getEnd0() {
        return this.end0;
    }

    public ObjectVariableAssociationEndInterface getEnd(ClassAssociationEndInterface cae) {
        if (this.getEnd0().getClassAssociationEnd().getId().equals(cae.getId()) && this.getEnd1().getClassAssociationEnd().getId().equals(cae.getId())) return null; else if (this.getEnd0().getClassAssociationEnd().getId().equals(cae.getId())) return this.getEnd0(); else if (this.getEnd1().getClassAssociationEnd().getId().equals(cae.getId())) return this.getEnd1(); else return null;
    }

    /**
   * Sets the first end of this object variable association
   * @param firstEnd
   */
    protected void setEnd0(ObjectVariableAssociationEnd ovae) {
        this.end0 = ovae;
        ovae.setObjectVariableAssociation(this);
    }

    /**
   * Returns the second end of this object variable association
   * @return
   */
    public ObjectVariableAssociationEndInterface getEnd1() {
        return this.end1;
    }

    protected void setNegativeContextFlag() {
        if (this.getEnd0().getObjectVariable().isNegativeContext() || this.getEnd1().getObjectVariable().isNegativeContext()) this.isNegativeContext = true; else this.isNegativeContext = false;
    }

    /**
   * Sets the second end of this object variabel association
   * @param secondEnd
   */
    protected void setEnd1(ObjectVariableAssociationEnd secondEnd) {
        this.end1 = secondEnd;
        secondEnd.setObjectVariableAssociation(this);
    }

    public InstanceAssociationEnd getInstEnd0() {
        return this.getEnd0();
    }

    public InstanceAssociationEnd getInstEnd1() {
        return this.getEnd1();
    }

    /**
   * Returns the ModelVariable, which contains this object variable association
   * @return
   */
    public ModelVariableInterface getModelVariable() {
        return this.modelVariable;
    }

    /**
   * Wird von den Methoden setObjectVariableAssociations() und addObjectVariableAssociation()
   * und removeObjectVariableAssociation() der Klasse ModelVariable aufgerufen.
   * @param modelVariable
   */
    protected void setModelVariableIntern(ModelVariable modelVariable) {
        this.modelVariable = modelVariable;
    }

    /**
   * Returns the ClassAssociation, which is associated with this object variable association
   * @return
   */
    public ClassAssociationInterface getClassAssociation() {
        return this.classAssociation;
    }

    /**
   * Sets the ClassAssociation, which is associated with this object variable association
   * @param classAssociations
   */
    private void setClassAssociation(ClassAssociationInterface classAssociation) {
        this.classAssociation = classAssociation;
    }

    /**
   * Returns the object-id
   * @return
   */
    public String getId() {
        return this.id;
    }

    /**
   * Sets the object-id
   * @param id
   */
    private void setId(String id) {
        this.id = id;
    }

    /**
   * Returns the cardinality
   * @return
   */
    public int getCard() {
        return card;
    }

    /**
   * Sets the cardinality
   * @param card
   */
    public void setCard(int card) {
        this.card = card;
    }

    protected void setNegativeContext(boolean neg) {
        this.isNegativeContext = neg;
    }

    public boolean isNegativeContext() {
        return this.isNegativeContext;
    }

    public int compareTo(Object o) {
        if (!(o instanceof ObjectVariableAssociation)) return -1; else return this.toString().compareTo(o.toString());
    }

    public String toString() {
        String r0 = "";
        if (this.isNegativeContext()) r0 = "NEGATIVE: ";
        r0 = this.getEnd0().getClassAssociationEnd().getRoleName();
        if (r0 == null || r0.trim().length() == 0) r0 = "[unnamed]";
        String r1 = this.getEnd0().getClassAssociationEnd().getRoleName();
        if (r1 == null || r1.trim().length() == 0) r1 = "[unnamed]";
        ObjectVariableInterface ov0 = this.getEnd0().getObjectVariable();
        ObjectVariableInterface ov1 = this.getEnd1().getObjectVariable();
        return ov0.getId() + "[" + ov0.getType().getName() + "]" + "-" + r0 + "--(" + this.getCard() + ")--" + r1 + "-" + ov1.getId() + "[" + ov1.getType().getName() + "]";
    }

    protected void fix() throws RuleSetException {
        fixEnds();
        this.end0.setObjectVariableAssociation(this);
        this.end1.setObjectVariableAssociation(this);
        this.end0.fix();
        this.end1.fix();
        this.setNegativeContextFlag();
    }

    private void fixEnds() throws RuleSetException {
        if (!this.getEnd0().getClassAssociationEnd().getId().equals(this.getClassAssociation().getClassAssociationEnd0().getId()) || !this.getEnd1().getClassAssociationEnd().getId().equals(this.getClassAssociation().getClassAssociationEnd1().getId())) {
            if (this.getEnd0().getClassAssociationEnd().getId().equals(this.getClassAssociation().getClassAssociationEnd1().getId()) || this.getEnd1().getClassAssociationEnd().getId().equals(this.getClassAssociation().getClassAssociationEnd0().getId())) {
                ObjectVariableAssociationEnd tmp = this.end0;
                this.setEnd0(this.end1);
                this.setEnd1(tmp);
            } else throw new RuleSetException("Object variable association " + this.toString() + " in rule " + this.getModelVariable().getRule().getName() + " has ends that are incompatible with " + "the type of its class association " + this.getClassAssociation().toString() + ".");
        } else return;
    }
}
