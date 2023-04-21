package com.keggview.application.datatypes;

import org.dom4j.Element;
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
 * The substrate element specifies the substrate node of this reaction. The
 * attribute of this element is as follows.
 */
public class Substrate extends Base {

    /**
	 * The name attribute contains the KEGG identifier of the COMPOUND database
	 * or the GLYCAN database.
	 */
    private String name;

    private Alt alt;

    private Entry substrate;

    /**
	 * @param name
	 */
    public Substrate(String name) {
        super();
        this.name = name;
        this.alt = null;
        this.substrate = null;
    }

    public Substrate(Element element) {
        this.name = element.attributeValue("name");
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
	 * @return the alt
	 */
    public Alt getAlt() {
        return alt;
    }

    /**
	 * @param alt the alt to set
	 */
    public void setAlt(Alt alt) {
        this.alt = alt;
    }

    /**
	 * @return the substrate
	 */
    public Entry getSubstrate() {
        return substrate;
    }

    /**
	 * @param substrate the substrate to set
	 */
    public void setSubstrate(Entry substrate) {
        this.substrate = substrate;
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
