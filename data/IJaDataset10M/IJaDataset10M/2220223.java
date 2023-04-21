package de.offis.semanticmm4u.derivation.ontology;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import component_interfaces.semanticmm4u.realization.IMetadataEntry;
import component_interfaces.semanticmm4u.realization.compositor.provided.IImage;
import component_interfaces.semanticmm4u.realization.compositor.provided.IVariable;
import component_interfaces.semanticmm4u.realization.compositor.provided.IVariableList;
import component_interfaces.semanticmm4u.realization.derivation.provided.IDerivator;
import de.offis.semanticmm4u.derivation.DerivationConstants;
import de.offis.semanticmm4u.derivation.location.Location;
import de.offis.semanticmm4u.global.Debug;
import de.offis.semanticmm4u.global.MetadataEntry;
import de.offis.semanticmm4u.media_elements_connector.rdf_uri.RDFConverting;

/**
 * This class uses an ontology to detect the city images. 
 * 
 */
public class InCity implements IDerivator {

    public static final String SOURCE_IDENT = "OntCityDerivator 1.0";

    private String ontologyPath = null;

    /**
	 * Constructor to set the ontology path
	 * @param _ontologyPath
	 */
    public InCity(String _ontologyPath) {
        this.ontologyPath = _ontologyPath;
    }

    public IVariable doDerivate(IVariable rootModel) {
        return this.doDerivate(rootModel, null);
    }

    public IVariable doDerivate(IVariable rootModel, Vector additionalMetadataSources) {
        Debug.println("Running: " + SOURCE_IDENT);
        Debug.println("Used ontology: " + this.ontologyPath);
        Model ontModel = RDFConverting.loadOntology(this.ontologyPath);
        if (ontModel == null) {
            Debug.println("ERROR: Could not open the ontology '" + this.ontologyPath + "'. Skipping this derivator.");
            return rootModel;
        }
        IVariable model = rootModel.getVariables().elementAt(0);
        Enumeration enumeration = model.getVariables().elements();
        while (enumeration.hasMoreElements()) {
            IVariable currentPage = (IVariable) enumeration.nextElement();
            ArrayList allImages = new ArrayList();
            this.findImages(currentPage, allImages);
            for (Iterator iter = allImages.iterator(); iter.hasNext(); ) {
                IImage element = (IImage) iter.next();
                IMetadataEntry[] entries = element.getMetadata().get(DerivationConstants.DC_SUBJECT, DerivationConstants.DUBLIN_CORE_NS);
                if (entries == null) continue;
                String cityName = this.getOntCity(ontModel, entries[0].getStringValue());
                if (cityName != null) {
                    IMetadataEntry locEntry = new MetadataEntry(Location.LOCATION_NAME);
                    locEntry.setNamespace(Location.NAMESPACE);
                    locEntry.setReliability(0.8f);
                    locEntry.setSource(SOURCE_IDENT);
                    locEntry.setValue(cityName);
                    element.getMetadata().put(locEntry);
                }
            }
        }
        return rootModel;
    }

    /**
	 * Gets the city to the given subject value
	 * @param ontModel
	 * @param stringValue
	 * @return the city name or <code>null</code> if no city was found.
	 */
    private String getOntCity(Model ontModel, String stringValue) {
        StmtIterator iter = ontModel.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();
            if (!predicate.toString().equals("http://www.owl-ontologies.com/Ontology1164103530.owl#inCity")) continue;
            if (!subject.hasProperty(ontModel.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), ontModel.createProperty("http://www.owl-ontologies.com/Ontology1164103530.owl#Building")) && !subject.hasProperty(ontModel.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), ontModel.createProperty("http://www.owl-ontologies.com/Ontology1164103530.owl#POI"))) continue;
            Statement nameProp = subject.getProperty(ontModel.createProperty("http://www.owl-ontologies.com/Ontology1164103530.owl#name"));
            String nameValue = this.getValue(nameProp);
            if (nameValue == null) continue;
            if (stringValue.equalsIgnoreCase(nameValue)) {
                if (object.isResource()) {
                    Resource city = (Resource) object;
                    Statement cityName = city.getProperty(ontModel.createProperty("http://www.owl-ontologies.com/Ontology1164103530.owl#name"));
                    String cityValue = this.getValue(cityName);
                    if (cityValue != null) return cityValue;
                }
            }
        }
        return null;
    }

    /**
	 * Get the object value (Subject - Predicate - Object) from an statement
	 * @param nameProp
	 * @return the value or <code>null</code> if no literal value exists
	 */
    private String getValue(Statement nameProp) {
        if (!nameProp.getObject().isLiteral()) return null;
        Literal lit = nameProp.getLiteral();
        if (lit == null) return null;
        return lit.getValue().toString();
    }

    /**
	 * @param element
	 * @param mediaList a list filled with <code>IImage</code> objects.
	 */
    private void findImages(IVariable element, ArrayList mediaList) {
        if (element instanceof IImage) {
            IImage medium = (IImage) element;
            mediaList.add(medium);
        }
        IVariableList list = element.getVariables();
        if (list == null) return;
        Enumeration enumeration = list.elements();
        while (enumeration.hasMoreElements()) {
            IVariable subElement = (IVariable) enumeration.nextElement();
            this.findImages(subElement, mediaList);
        }
    }
}
