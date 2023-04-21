package org.salamy.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.cas.TOP;

/** An abstract parent for sentence elements.

 * Updated by JCasGen Mon Apr 14 19:04:58 CEST 2008
 * XML source: /home/ppalaga/dev-uima/salamy/src/org/salamy/types/types.xml
 * @generated */
public class Node extends TOP {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = JCasRegistry.register(Node.class);

    /** @generated
   * @ordered 
   */
    public static final int type = typeIndexID;

    /** @generated  */
    public int getTypeIndexID() {
        return typeIndexID;
    }

    /** Never called.  Disable default constructor
   * @generated */
    protected Node() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public Node(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public Node(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
    private void readObject() {
    }

    /** getter for id - gets 
   * @generated */
    public String getId() {
        if (Node_Type.featOkTst && ((Node_Type) jcasType).casFeat_id == null) jcasType.jcas.throwFeatMissing("id", "org.salamy.types.Node");
        return jcasType.ll_cas.ll_getStringValue(addr, ((Node_Type) jcasType).casFeatCode_id);
    }

    /** setter for id - sets  
   * @generated */
    public void setId(String v) {
        if (Node_Type.featOkTst && ((Node_Type) jcasType).casFeat_id == null) jcasType.jcas.throwFeatMissing("id", "org.salamy.types.Node");
        jcasType.ll_cas.ll_setStringValue(addr, ((Node_Type) jcasType).casFeatCode_id, v);
    }
}
