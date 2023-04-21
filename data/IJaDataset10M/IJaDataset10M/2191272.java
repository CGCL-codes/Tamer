package org.ist.contract.DataStructures;

import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.ist.contract.MonitoringTools.AtomicCondition;
import org.ist.contract.MonitoringTools.State;
import org.ist.contract.MonitoringTools.Arc;

public class clauseStateBck extends State {

    private static final long serialVersionUID = 1L;

    /**
		 * @uml.property  name="stateChilds"
		 * @uml.associationEnd  qualifier="toString:java.lang.String org.ist.contract.MonitoringTools.State"
		 */
    Hashtable<String, State> stateChilds;

    /**
		 * @uml.property  name="clauseDefinitionName"
		 */
    String ClauseDefinitionName;

    /**
		 * @uml.property  name="receiver"
		 */
    String receiver;

    /**
		 * @uml.property  name="sender"
		 */
    String sender;

    /**
		 * @uml.property  name="timeStamp"
		 */
    long timeStamp;

    Vector<String> args = new Vector<String>();

    Vector<AtomicCondition> transitionMatched = null;

    /**
		 * @uml.property  name="currentCondition"
		 * @uml.associationEnd  
		 */
    AtomicCondition currentCondition = null;

    public clauseStateBck() {
        super(null);
        stateChilds = new Hashtable<String, State>();
    }

    public clauseStateBck(State st) {
        super(st.getStateName());
        this.setType(st.getType());
        stateChilds = new Hashtable<String, State>();
        Vector<Arc> transitions = st.getTransition2List();
        this.setTransitionList(transitions);
        System.out.println("CLAUSESTATE: STATE OF CHILDS..." + st.getStateName());
        for (int i = 0; i < transitions.size(); i++) {
            State d = ((Arc) transitions.elementAt(i)).getTargetState();
            int k = 0;
            while (k < ((Arc) transitions.elementAt(i)).getConditions().size()) {
                System.out.println("ENTRER DANS EXPLOSION DES CONDITIONS");
                stateChilds.put("[" + ((Arc) transitions.elementAt(i)).getConditions().elementAt(k).toString() + "]", d);
                System.out.println("CLAUSESTATE: CHILD " + i + " " + d.getStateName());
                k++;
            }
            System.out.println("CLAUSESTATE: CONDITION:.." + (((Arc) transitions.elementAt(i)).getConditions()).toString());
            System.out.println("MISE A JOUR DE TRANSITION MATCHED");
            transitionMatched = ((Arc) transitions.elementAt(i)).getConditions();
        }
    }

    public int getTransition2sNumber() {
        return stateChilds.size();
    }

    public void setChilds(Hashtable<String, State> childs) {
        stateChilds = childs;
    }

    public clauseState getChild(String event, ClauseInterpretation clauseI) {
        clauseState retour = null;
        boolean trouve = false;
        for (Enumeration<String> enu = stateChilds.keys(); enu.hasMoreElements(); ) {
            String z = (String) enu.nextElement();
            if (z.equals("[" + event + "]")) {
                System.out.println("CLAUSE STATE VALUE DE ZZZ" + z);
                System.out.println("CLAUSE STATE VALUE DE ZZZ" + event);
                trouve = true;
                System.out.println("CLAUSESTATE: EUREKA*****************EUREKA");
            }
        }
        if (trouve) {
            System.out.println("CLAUSESTATE: SUCCESSFUL MATCHING WITH EVENT ");
            State s = (State) stateChilds.get("[" + event + "]");
            retour = (clauseState) clauseI.getState(s.getStateName());
        }
        System.out.println("CLAUSESTATE: EVENT CONTENT IN ONE OF CHILDS =" + retour);
        return retour;
    }

    public synchronized boolean isTransition2Exist(String tr) {
        boolean found = false;
        Vector<Arc> transitions = this.getTransition2List();
        int i2 = 0;
        int j = 0;
        int k = 0;
        int k1 = 0;
        String temp = "";
        tr = "[" + tr + "]";
        int i = 0;
        while (i2 < transitions.size() && !found) {
            System.out.println("TRANSITION OF CLAUSESTATE " + ((Arc) transitions.elementAt(i2)).getConditions().toString());
            System.out.println("EVENT TO BE MATCHED " + tr);
            temp = ((Arc) transitions.elementAt(i2)).getConditions().toString();
            System.out.println("VERIFIER CONJONCTION D EVENT OU PAS " + ((Arc) transitions.elementAt(i2)).getConditions().size());
            int m = 0;
            while (m < ((Arc) transitions.elementAt(i2)).getConditions().size() && !found) {
                temp = "[" + ((Arc) transitions.elementAt(i2)).getConditions().elementAt(m).toString() + "]";
                i = temp.indexOf("(");
                System.out.println("MESSAGE TYPE LABELING TRANSITION OF CLAUSE " + temp.substring(0, i));
                System.out.println("EVENT TO BE MATCHED " + tr);
                if ((temp.substring(0, i)).equals(tr.substring(0, i))) {
                    System.out.println("MESSAGE TYPE MATCHED");
                    temp = temp.substring(i + 1);
                    tr = tr.substring(i + 1);
                    j = temp.indexOf(",");
                    System.out.println("VALEUR DE TEMP >>>>>" + temp);
                    System.out.println("VALEUR DE EVENT A MATCHER >>>>" + tr);
                    System.out.println("temp='" + temp + "' tr='" + tr + "'");
                    if (temp.length() == tr.length()) {
                        System.out.println("MESSAGE ACTION LABELING TRANSITION OF CLAUSE " + temp.substring(0, j));
                        System.out.println("EVENT TO BE MATCHED " + tr);
                        if ((temp.substring(0, j)).equals(tr.substring(0, j))) {
                            System.out.println("MESSAGE ACTION MATCHED");
                            temp = temp.substring(j + 1);
                            tr = tr.substring(j + 1);
                            i = temp.indexOf(",");
                            System.out.println("CONSTANTE MESSAGE RECEIVER LABELING TRANSITION OF CLAUSE " + temp.substring(0, i));
                            System.out.println("EVENT TO BE MATCHED " + tr.substring(0, i));
                            if ((temp.substring(0, i)).equals("?")) {
                                System.out.println("MESSAGE RECEIVER MATCHED");
                                k = tr.indexOf(",");
                                receiver = tr.substring(0, k - 1);
                                temp = temp.substring(i + 1);
                                System.out.println("VALEUR 1 DE TEMP ET TR POTENTIAL " + temp + tr.substring(k + 1));
                                tr = tr.substring(i + 1);
                                System.out.println("VALEUR DE TEMP ET TR :" + temp + tr);
                                j = temp.indexOf(")");
                                System.out.println("MESSAGE SENDER LABELING TRANSITION OF CLAUSE " + temp.substring(0, j));
                                System.out.println("EVENT TO BE MATCHED " + tr.substring(0, j));
                                if ((temp.substring(0, j)).equals("?")) {
                                    k1 = tr.indexOf(")");
                                    sender = tr.substring(k + 1, k1 - 1);
                                    System.out.println("MESSAGE SENDER MATCHED IT S OK ????? ");
                                    found = true;
                                } else {
                                    if ((temp.substring(0, j)).equals(tr.substring(0, j))) {
                                        System.out.println("MESSAGE SENDER MATCHED CONSTANTE ");
                                        found = true;
                                    }
                                }
                            } else {
                                if ((temp.substring(0, i)).equals(tr.substring(0, i))) {
                                    System.out.println("MATCHING WITH CONSTANT .....");
                                    System.out.println("MESSAGE RECEIVER MATCHED");
                                    receiver = tr.substring(0, i);
                                    temp = temp.substring(i + 1);
                                    tr = tr.substring(i + 1);
                                    j = temp.indexOf(")");
                                    System.out.println(" CONSTANTE MESSAGE SENDER LABELING TRANSITION OF CLAUSE " + temp.substring(0, j));
                                    System.out.println("EVENT TO BE MATCHED " + tr.substring(0, j));
                                    if ((temp.substring(0, j)).equals("?")) {
                                        k1 = tr.indexOf(")");
                                        sender = tr.substring(k + 1, k1 - 1);
                                        System.out.println("MESSAGE SENDER MATCHED");
                                        found = true;
                                    } else {
                                        if ((temp.substring(0, j)).equals(tr.substring(0, j))) {
                                            sender = tr.substring(0, j);
                                            System.out.println("MESSAGE SENDER MATCHED");
                                            found = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                m++;
            }
            i2++;
        }
        return found;
    }

    public boolean isMatchedParams(String tempP, String trP) {
        StringTokenizer st = new StringTokenizer(tempP, " \t\n\r\f,");
        StringTokenizer st1 = new StringTokenizer(trP, " \t\n\r\f,");
        boolean boolTemp = true;
        if (st.countTokens() == st1.countTokens()) {
            while (st.hasMoreTokens() & boolTemp) {
                boolTemp = (st.nextToken()).equals(st1.nextToken());
            }
        } else {
            System.out.println("The number of paramaters is different");
            boolTemp = false;
        }
        return boolTemp;
    }

    public boolean isTransition(String temp, String tr) {
        boolean found = false;
        int i = 0;
        int j = 0;
        int k = 0;
        int k1 = 0;
        int i1 = 0;
        int j1 = 0;
        int g = 0;
        int g1 = 0;
        System.out.println("ENTRER DANS IS TRANSITION ?????????????" + temp);
        System.out.println("SUITE IS TRANSITION ?????????????" + tr);
        i = temp.indexOf("(");
        i1 = tr.indexOf("(");
        System.out.println("MESSAGE TYPE LABELING TRANSITION OF CLAUSE " + temp.substring(0, i));
        System.out.println("EVENT TO BE MATCHED " + tr.substring(0, i1));
        if ((temp.substring(0, i)).equals(tr.substring(0, i1))) {
            System.out.println("MESSAGE TYPE MATCHED");
            temp = temp.substring(i + 1);
            tr = tr.substring(i1 + 1);
            j = temp.indexOf(",");
            j1 = tr.indexOf(",");
            System.out.println("MESSAGE ACTION LABELING TRANSITION OF CLAUSE " + temp.substring(0, j));
            System.out.println("EVENT TO BE MATCHED " + tr.substring(0, j1));
            g = temp.indexOf("(");
            g1 = tr.indexOf("(");
            if (temp.substring(0, g - 1).equals(tr.substring(0, g1 - 1))) {
                int gP, g1P;
                String tempP, trP;
                gP = temp.indexOf(")");
                g1P = tr.indexOf(")");
                tempP = temp.substring(0, gP - 1);
                trP = tr.substring(0, g1P - 1);
                if (tempP.length() != 0) {
                    isMatchedParams(tempP, trP);
                } else {
                    System.out.println("Cas de aucun parametre");
                }
            }
            if ((temp.substring(0, j)).equals(tr.substring(0, j1))) {
                System.out.println("MESSAGE ACTION MATCHED");
                temp = temp.substring(j + 1);
                tr = tr.substring(j1 + 1);
                i = temp.indexOf(",");
                i1 = tr.indexOf(",");
                System.out.println("MESSAGE RECEIVER LABELING TRANSITION OF CLAUSE " + temp.substring(0, i));
                System.out.println("EVENT TO BE MATCHED " + tr.substring(0, i1));
                if ((temp.substring(0, i)).equals("?")) {
                    System.out.println("MESSAGE RECEIVER MATCHED");
                    receiver = tr.substring(0, i1 - 1);
                    temp = temp.substring(i + 1);
                    tr = tr.substring(i1 + 1);
                    j = temp.indexOf(")");
                    j1 = tr.indexOf(")");
                    System.out.println("MESSAGE SENDER LABELING TRANSITION OF CLAUSE " + temp.substring(0, j));
                    System.out.println("EVENT TO BE MATCHED " + tr.substring(0, j1));
                    if ((temp.substring(0, j)).equals("?")) {
                        sender = tr.substring(0, j1 - 1);
                        System.out.println("MESSAGE SENDER MATCHED");
                        found = true;
                    }
                }
            } else {
                if ((temp.substring(0, i)).equals(tr.substring(0, i))) {
                    System.out.println("MATCHING WITH CONSTANT .....");
                    System.out.println("MESSAGE RECEIVER MATCHED");
                    receiver = tr.substring(0, i);
                    temp = temp.substring(i + 1);
                    j = temp.indexOf(")");
                    System.out.println("MESSAGE SENDER LABELING TRANSITION OF CLAUSE " + temp.substring(0, j));
                    if ((temp.substring(0, j)).equals("?")) {
                        k1 = tr.indexOf(")");
                        sender = tr.substring(k + 1, k1 - 1);
                        System.out.println("MESSAGE SENDER MATCHED");
                        found = true;
                    } else {
                        tr = tr.substring(i + 1);
                        System.out.println("EVENT TO BE MATCHED " + tr);
                        System.out.println("CONSTANTE GOOOOOODDDDD" + tr.substring(0, j));
                        if ((temp.substring(0, j)).equals(tr.substring(0, j))) {
                            sender = tr.substring(0, j - 1);
                            System.out.println("MESSAGE SENDER MATCHED");
                            found = true;
                        }
                    }
                }
            }
        }
        return found;
    }

    public void subTransitionJason(Term stP, Term st1P, int i) {
        if (stP.isAtom()) {
            switch(i) {
                case 0:
                    System.out.println("Action");
                    args.add("");
                    break;
                case 1:
                    System.out.println("Sender");
                    sender = st1P.toString();
                    break;
                case 2:
                    System.out.println("Receiver");
                    receiver = st1P.toString();
                    break;
                case 3:
                    System.out.println("Time Stamp");
                    timeStamp = new Long(st1P.toString()).longValue();
                    break;
            }
        } else {
            System.out.println("Case Predicate");
            switch(i) {
                case 0:
                    System.out.println("Action");
                    args.add("");
                default:
                    System.out.println("The current version doesn't consider predicate for sender, receiver and timeStamp");
            }
        }
    }

    public boolean isTransitionJason(String temp, String tr) {
        boolean boolTemp = true;
        Structure st = Structure.parse(temp);
        Structure st1 = Structure.parse(tr);
        if ((st.getFunctor()).equals(st1.getFunctor())) {
            System.out.println("Event Type Matched");
            if (st.getArity() == st1.getArity()) {
                System.out.println("Parameter Number of the Event is equal");
                for (int i = 0; i < st.getArity(); i++) {
                    Term stP = st.getTerm(i);
                    Term st1P = st1.getTerm(i);
                    if (stP.isGround()) {
                        System.out.println("Case Ground");
                        if (stP.equals(st1P)) {
                            System.out.println("And matching ... " + stP.toString());
                            subTransitionJason(stP, st1P, i);
                            boolTemp = true;
                        } else {
                            System.out.println("But no matching");
                            boolTemp = false;
                        }
                    } else {
                        System.out.println("Case Variable(s)");
                        subTransitionJason(stP, st1P, i);
                    }
                }
            }
        } else {
            System.out.println("Event Type doesn't Matched");
            boolTemp = false;
        }
        return boolTemp;
    }

    public synchronized boolean isTransition2ExistComment(String tr) {
        boolean found = false;
        int i = 0;
        int j = 0;
        Vector<Arc> transitions = this.getTransition2List();
        Vector<AtomicCondition> temp1 = null;
        System.out.println("isTransitionComment " + tr);
        while (i < transitions.size() & !found) {
            System.out.println("TRANSITION OF CLAUSESTATE " + ((Arc) transitions.elementAt(i)).getConditions().toString());
            System.out.println("EVENT TO BE MATCHED " + tr);
            temp1 = ((Arc) transitions.elementAt(i)).getConditions();
            while (j < temp1.size() & !found) {
                System.out.println("ENTREE DANS TRANSITION2EXIST COMMENT");
                found = isTransition((temp1.elementAt(j)).toString(), tr);
                j++;
            }
            if (found) {
                System.out.println("EVENT MATCHED WITH ONE OF THE VECTOR ASSOCIATED TO THE TRANSITION " + (this.getTransition2List().elementAt(i)).getConditions().toString());
                if (transitionMatched.size() > 1) {
                    System.out.println("VALEUR DES DEUX EVENTS " + transitionMatched.elementAt(0).toString());
                    currentCondition = (AtomicCondition) transitionMatched.elementAt(0);
                    transitionMatched.remove(transitionMatched.elementAt(0));
                    System.out.println("THE NEW VECTOR ASSOCIATED TO THE TRANSITION " + transitionMatched.toString());
                }
            }
            i++;
        }
        return found;
    }

    /**
		 * @return
		 * @uml.property  name="receiver"
		 */
    public String getReceiver() {
        return receiver;
    }

    /**
		 * @return
		 * @uml.property  name="sender"
		 */
    public String getSender() {
        return sender;
    }

    public int namedConditionNumber(Arc t) {
        return t.getConditions().size();
    }

    public Vector<AtomicCondition> currentTransitionMatched() {
        System.out.println("ENTRER DANS CURRENT TRANSITION MATCHED AVEC TAILLE ????????" + transitionMatched.size());
        return transitionMatched;
    }

    public AtomicCondition getCurrentNamedCondition() {
        return currentCondition;
    }
}
