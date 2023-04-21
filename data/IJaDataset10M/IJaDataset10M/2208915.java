package nl.uva.psy.swi.beangenerator;

/**
 *@author     C.J. van Aart - Acklin, University of Amsterdam
 *@created    November 15, 2002
 *@version    1.0
 */
public class Examples {

    static String getFullExample() {
        StringBuffer sb = new StringBuffer();
        sb.append("public class CD implements Conceptt, Serializable {\n");
        sb.append(" protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);\n");
        sb.append(" public void addPropertyChangeListener(PropertyChangeListener pcl) {\n");
        sb.append("  pcs.addPropertyChangeListener(pcl);\n");
        sb.append(" }\n");
        sb.append(" public void removePropertyChangeListener(PropertyChangeListener pcl) {\n");
        sb.append("  pcs.removePropertyChangeListener(pcl);\n");
        sb.append(" }\n");
        sb.append(" private int price;\n");
        sb.append(" public void setPrice(int value) { \n");
        sb.append("  this.price=value;\n");
        sb.append(" }\n");
        sb.append(" public int getPrice() {\n");
        sb.append("  return this.price;\n");
        sb.append(" }\n");
        sb.append(" private String title;\n");
        sb.append(" public void setTitle(String value) { \n");
        sb.append("  this.title=value;\n");
        sb.append(" }\n");
        sb.append(" public String getTitle() {\n");
        sb.append("  return this.title;\n");
        sb.append(" }\n");
        sb.append("}\n");
        return sb.toString();
    }

    static String getMicroExample() {
        StringBuffer sb = new StringBuffer();
        sb.append("public class CD implements Concept, Introspectable {\n");
        sb.append(" private int price;\n");
        sb.append(" public void setPrice(int value) { \n");
        sb.append("  this.price=value;\n");
        sb.append(" }\n");
        sb.append(" public int getPrice() {\n");
        sb.append("  return this.price;\n");
        sb.append(" }\n");
        sb.append(" private String title;\n");
        sb.append(" public void setTitle(String value) { \n");
        sb.append("  this.title=value;\n");
        sb.append(" }\n");
        sb.append(" public String getTitle() {\n");
        sb.append("  return this.title;\n");
        sb.append(" }\n");
        sb.append(" public void externalise(AbsObject absObj, Ontology onto) throws OntologyException {\n");
        sb.append(" try {\n");
        sb.append("  AbsConcept abs = (AbsConcept) absObj;\n");
        sb.append("  abs.set(TestOntology.CD_PRICE, price);\n");
        sb.append("  abs.set(TestOntology.CD_TITLE, (AbsTerm) onto.fromObject(getTitle()));\n");
        sb.append(" } catch (ClassCastException cce) {\n");
        sb.append(" throw new OntologyException(\"Error externalising CD\");\n");
        sb.append("  }\n");
        sb.append(" }\n");
        sb.append(" public void internalise(AbsObject absObj, Ontology onto) throws UngroundedException, OntologyException {\n");
        sb.append("  try {\n");
        sb.append("   AbsConcept abs = (AbsConcept) absObj;\n");
        sb.append("   price = abs.getInteger(TestOntology.CD_PRICE);\n");
        sb.append("   title = (String)onto.toObject(abs.getAbsObject(TestOntology.CD_TITLE));\n");
        sb.append("  } catch (ClassCastException cce) {\n");
        sb.append("  throw new OntologyException(\"Error internalising CD\");\n");
        sb.append("  }\n");
        sb.append(" }\n");
        sb.append("}\n");
        return sb.toString();
    }

    static String getStandardExample() {
        StringBuffer sb = new StringBuffer();
        sb.append("public class CD implements Concept {\n");
        sb.append(" private int price;\n");
        sb.append(" public void setPrice(int value) { \n");
        sb.append("  this.price=value;\n");
        sb.append(" }\n");
        sb.append(" public int getPrice() {\n");
        sb.append("  return this.price;\n");
        sb.append(" }\n");
        sb.append(" private String title;\n");
        sb.append(" public void setTitle(String value) { \n");
        sb.append("  this.title=value;\n");
        sb.append(" }\n");
        sb.append(" public String getTitle() {\n");
        sb.append("  return this.title;\n");
        sb.append(" }\n");
        sb.append("}\n");
        return sb.toString();
    }
}
