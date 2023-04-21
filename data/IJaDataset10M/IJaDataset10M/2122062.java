package com.keggview.application.datatypes;

import java.util.ArrayList;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Pablo
 * 
 * The reaction element specifies chemical reaction between a substrate and a
 * product indicated by an arrow connecting two circles in the KEGG pathways.
 * The reaction element has the substrate element and the product element as
 * subelements. The attributes of this element are as follows.
 */
public class Reaction extends Base {

    /**
	 * The name attribute contains the KEGG identifier of the REACTION database.
	 * 
	 * format: rn:(accession)
	 */
    String name;

    /**
	 * 
	 * The type attribute specifies the distinction of reversible and
	 * irreversible reactions, which are indicated by bi-directional and
	 * uni-directional arrows in the KEGG pathways. Note that the terms
	 * "reversible" and "irreversible" do not necessarily reflect biochemical
	 * properties of each reaction. They rather indicate the direction of the
	 * reaction drawn on the pathway map that is extracted from text books and
	 * literatures.
	 * 
	 * 
	 */
    String type;

    /**
	 * irreversible reaction
	 */
    public static final String T_IRREV = "irreversible";

    /**
	 * reversible reaction
	 */
    public static final String T_REVER = "reversible";

    /**
	 * [1,n)
	 */
    private ArrayList<Substrate> substrates;

    /**
	 * [1,n)
	 */
    private ArrayList<Product> products;

    /**
	 * @param name
	 * @param type
	 */
    public Reaction(String name, String type) {
        super();
        this.name = name;
        this.type = type;
        this.substrates = new ArrayList<Substrate>(0);
    }

    public Reaction(DefaultElement element) {
        this((Element) element);
    }

    public Reaction(Element element) {
        this.name = element.attribute("name").getValue();
        String temp = element.attribute("type").getValue();
        if (temp.equals(Reaction.T_REVER)) {
            this.type = Reaction.T_REVER;
        } else {
            this.type = Reaction.T_IRREV;
        }
        this.substrates = new ArrayList<Substrate>(0);
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @return the substrates
	 */
    public ArrayList<Substrate> getSubstrates() {
        return substrates;
    }

    /**
	 * @param substrates the substrates to set
	 */
    public void setSubstrates(ArrayList<Substrate> substrates) {
        this.substrates = substrates;
    }

    /**
	 * @return the products
	 */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
	 * @param products the products to set
	 */
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public TreeIterator<EObject> eAllContents() {
        return null;
    }

    @Override
    public EClass eClass() {
        return null;
    }

    @Override
    public EObject eContainer() {
        return null;
    }

    @Override
    public EStructuralFeature eContainingFeature() {
        return null;
    }

    @Override
    public EReference eContainmentFeature() {
        return null;
    }

    @Override
    public EList<EObject> eContents() {
        return null;
    }

    @Override
    public EList<EObject> eCrossReferences() {
        return null;
    }

    @Override
    public Object eGet(EStructuralFeature arg0) {
        return null;
    }

    @Override
    public Object eGet(EStructuralFeature arg0, boolean arg1) {
        return null;
    }

    @Override
    public boolean eIsProxy() {
        return false;
    }

    @Override
    public boolean eIsSet(EStructuralFeature arg0) {
        return false;
    }

    @Override
    public Resource eResource() {
        return null;
    }

    @Override
    public void eSet(EStructuralFeature arg0, Object arg1) {
    }

    @Override
    public void eUnset(EStructuralFeature arg0) {
    }

    @Override
    public EList<Adapter> eAdapters() {
        return null;
    }

    @Override
    public boolean eDeliver() {
        return false;
    }

    @Override
    public void eNotify(Notification arg0) {
    }

    @Override
    public void eSetDeliver(boolean arg0) {
    }
}
