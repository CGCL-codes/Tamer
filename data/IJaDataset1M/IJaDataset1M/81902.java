package de.mangas.modeltransformator.util;

public class SplitTransitionTuple {

    java.util.List<String> ala = new java.util.ArrayList<String>();

    java.util.List<org.eclipse.uml2.uml.Transition> transitionen = new java.util.ArrayList<org.eclipse.uml2.uml.Transition>();

    public boolean addALA(String ala) {
        if (this.ala.contains(ala) == false) {
            this.ala.add(ala);
            return true;
        } else return false;
    }

    public java.util.List<String> getALAs() {
        return this.ala;
    }

    public boolean addTransition(org.eclipse.uml2.uml.Transition t) {
        if (this.transitionen.contains(t) == false) {
            this.transitionen.add(t);
            return true;
        } else return false;
    }

    public java.util.List<org.eclipse.uml2.uml.Transition> getTransitions() {
        return this.transitionen;
    }

    @Override
    public String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("(ALA ={");
        for (String alaAusdruck : ala) sb.append(alaAusdruck + ",");
        sb.append("}, Transitionen ={");
        for (org.eclipse.uml2.uml.Transition t : transitionen) sb.append(t.getName() + ",");
        sb.append("})");
        return sb.toString();
    }
}
