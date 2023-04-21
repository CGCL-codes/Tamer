package org.marcont.services.definitions.profile;

import com.ibm.adtech.jastor.*;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Factory for instantiating objects for ontology classes in the Profile.owl ontology.  The
 * get methods leave the model unchanged and return a Java view of the object in the model.  The create methods
 * may add certain baseline properties to the model such as rdf:type and any properties with hasValue restrictions.
 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Profile.owl)</p>
 * <br>
 * RDF Schema Standard Properties <br>
 * 	seeAlso : http://www.daml.org/services/owl-s/1.2/ProfileAdditionalParameters.owl, http://www.daml.org/services/owl-s/1.2/ProfileDeprecatedElements.owl, http://www.daml.org/services/owl-s/1.2/ActorDefault.owl <br>
 * 	comment : 
      OWL ontology for Advertisements (i.e. Profiles).
      This file belongs to the OWL-S Release.
      Initial version created by Terry Payne (trp@ecs.soton.ac.uk).
      Modified by Massimo Paolucci (paolucci@cs.cmu.edu)
      Modified by David Martin and other members of the OWL-S Coalition.

     <br>
 * <br>
 * <br>
 *	@version 
      $Id: Profile_DOT_owlFactory.java 281 2006-12-16 17:14:22Z pawszc $
    
 */
public class Profile_DOT_owlFactory extends com.ibm.adtech.jastor.ThingFactory {

    /**
	 * Create a new instance of ServiceParameter.  Adds the rdf:type property for the given resource to the model.
	 * @param resource The resource of the ServiceParameter
	 * @param model the Jena Model.
	 */
    public static ServiceParameter createServiceParameter(Resource resource, Model model) throws JastorException {
        return org.marcont.services.definitions.profile.ServiceParameterImpl.createServiceParameter(resource, model);
    }

    /**
	 * Create a new instance of ServiceParameter.  Adds the rdf:type property for the given resource to the model.
	 * @param uri The uri of the ServiceParameter
	 * @param model the Jena Model.
	 */
    public static ServiceParameter createServiceParameter(String uri, Model model) throws JastorException {
        ServiceParameter obj = org.marcont.services.definitions.profile.ServiceParameterImpl.createServiceParameter(model.createResource(uri), model);
        return obj;
    }

    /**
	 * Create a new instance of ServiceParameter.  Leaves the model unchanged.
	 * @param uri The uri of the ServiceParameter
	 * @param model the Jena Model.
	 */
    public static ServiceParameter getServiceParameter(String uri, Model model) throws JastorException {
        return getServiceParameter(model.createResource(uri), model);
    }

    /**
	 * Create a new instance of ServiceParameter.  Leaves the model unchanged.
	 * @param resource The resource of the ServiceParameter
	 * @param model the Jena Model.
	 */
    public static ServiceParameter getServiceParameter(Resource resource, Model model) throws JastorException {
        String code = (model.hashCode() * 17 + org.marcont.services.definitions.profile.ServiceParameter.class.hashCode()) + resource.toString();
        org.marcont.services.definitions.profile.ServiceParameterImpl obj = (org.marcont.services.definitions.profile.ServiceParameterImpl) objects.get(code);
        if (obj == null) {
            obj = org.marcont.services.definitions.profile.ServiceParameterImpl.getServiceParameter(resource, model);
            if (obj == null) return null;
            objects.put(code, obj);
        }
        return obj;
    }

    /**
	 * Return an instance of ServiceParameter for every resource in the model with rdf:Type http://www.daml.org/services/owl-s/1.2/Profile.owl#ServiceParameter
	 * @param model the Jena Model
	 * @return a List of ServiceParameter
	 */
    public static java.util.List getAllServiceParameter(Model model) throws JastorException {
        StmtIterator it = model.listStatements(null, RDF.type, ServiceParameter.TYPE);
        java.util.List list = new java.util.ArrayList();
        while (it.hasNext()) {
            Statement stmt = it.nextStatement();
            list.add(getServiceParameter(stmt.getSubject(), model));
        }
        return list;
    }

    /**
	 * Create a new instance of ServiceCategory.  Adds the rdf:type property for the given resource to the model.
	 * @param resource The resource of the ServiceCategory
	 * @param model the Jena Model.
	 */
    public static ServiceCategory createServiceCategory(Resource resource, Model model) throws JastorException {
        return org.marcont.services.definitions.profile.ServiceCategoryImpl.createServiceCategory(resource, model);
    }

    /**
	 * Create a new instance of ServiceCategory.  Adds the rdf:type property for the given resource to the model.
	 * @param uri The uri of the ServiceCategory
	 * @param model the Jena Model.
	 */
    public static ServiceCategory createServiceCategory(String uri, Model model) throws JastorException {
        ServiceCategory obj = org.marcont.services.definitions.profile.ServiceCategoryImpl.createServiceCategory(model.createResource(uri), model);
        return obj;
    }

    /**
	 * Create a new instance of ServiceCategory.  Leaves the model unchanged.
	 * @param uri The uri of the ServiceCategory
	 * @param model the Jena Model.
	 */
    public static ServiceCategory getServiceCategory(String uri, Model model) throws JastorException {
        return getServiceCategory(model.createResource(uri), model);
    }

    /**
	 * Create a new instance of ServiceCategory.  Leaves the model unchanged.
	 * @param resource The resource of the ServiceCategory
	 * @param model the Jena Model.
	 */
    public static ServiceCategory getServiceCategory(Resource resource, Model model) throws JastorException {
        String code = (model.hashCode() * 17 + org.marcont.services.definitions.profile.ServiceCategory.class.hashCode()) + resource.toString();
        org.marcont.services.definitions.profile.ServiceCategoryImpl obj = (org.marcont.services.definitions.profile.ServiceCategoryImpl) objects.get(code);
        if (obj == null) {
            obj = org.marcont.services.definitions.profile.ServiceCategoryImpl.getServiceCategory(resource, model);
            if (obj == null) return null;
            objects.put(code, obj);
        }
        return obj;
    }

    /**
	 * Return an instance of ServiceCategory for every resource in the model with rdf:Type http://www.daml.org/services/owl-s/1.2/Profile.owl#ServiceCategory
	 * @param model the Jena Model
	 * @return a List of ServiceCategory
	 */
    public static java.util.List getAllServiceCategory(Model model) throws JastorException {
        StmtIterator it = model.listStatements(null, RDF.type, ServiceCategory.TYPE);
        java.util.List list = new java.util.ArrayList();
        while (it.hasNext()) {
            Statement stmt = it.nextStatement();
            list.add(getServiceCategory(stmt.getSubject(), model));
        }
        return list;
    }

    /**
	 * Create a new instance of Profile.  Adds the rdf:type property for the given resource to the model.
	 * @param resource The resource of the Profile
	 * @param model the Jena Model.
	 */
    public static Profile createProfile(Resource resource, Model model) throws JastorException {
        return org.marcont.services.definitions.profile.ProfileImpl.createProfile(resource, model);
    }

    /**
	 * Create a new instance of Profile.  Adds the rdf:type property for the given resource to the model.
	 * @param uri The uri of the Profile
	 * @param model the Jena Model.
	 */
    public static Profile createProfile(String uri, Model model) throws JastorException {
        Profile obj = org.marcont.services.definitions.profile.ProfileImpl.createProfile(model.createResource(uri), model);
        return obj;
    }

    /**
	 * Create a new instance of Profile.  Leaves the model unchanged.
	 * @param uri The uri of the Profile
	 * @param model the Jena Model.
	 */
    public static Profile getProfile(String uri, Model model) throws JastorException {
        return getProfile(model.createResource(uri), model);
    }

    /**
	 * Create a new instance of Profile.  Leaves the model unchanged.
	 * @param resource The resource of the Profile
	 * @param model the Jena Model.
	 */
    public static Profile getProfile(Resource resource, Model model) throws JastorException {
        String code = (model.hashCode() * 17 + org.marcont.services.definitions.profile.Profile.class.hashCode()) + resource.toString();
        org.marcont.services.definitions.profile.ProfileImpl obj = (org.marcont.services.definitions.profile.ProfileImpl) objects.get(code);
        if (obj == null) {
            obj = org.marcont.services.definitions.profile.ProfileImpl.getProfile(resource, model);
            if (obj == null) return null;
            objects.put(code, obj);
        }
        return obj;
    }

    /**
	 * Return an instance of Profile for every resource in the model with rdf:Type http://www.daml.org/services/owl-s/1.2/Profile.owl#Profile
	 * @param model the Jena Model
	 * @return a List of Profile
	 */
    public static java.util.List getAllProfile(Model model) throws JastorException {
        StmtIterator it = model.listStatements(null, RDF.type, Profile.TYPE);
        java.util.List list = new java.util.ArrayList();
        while (it.hasNext()) {
            Statement stmt = it.nextStatement();
            list.add(getProfile(stmt.getSubject(), model));
        }
        return list;
    }

    /**
	 * Returns an instance of an interface for the given Resource.  The return instance is guaranteed to 
	 * implement the most specific interface in *some* hierarchy in which the Resource participates.  The behavior
	 * is unspecified for resources with RDF types from different hierarchies.
	 * @return an instance of Thing
	 */
    public static Thing getThing(com.hp.hpl.jena.rdf.model.Resource res, com.hp.hpl.jena.rdf.model.Model model) throws JastorException {
        if (res.hasProperty(RDF.type, model.getResource("http://www.daml.org/services/owl-s/1.2/Profile.owl#Profile"))) {
            return getProfile(res, model);
        }
        if (res.hasProperty(RDF.type, model.getResource("http://www.daml.org/services/owl-s/1.2/Profile.owl#ServiceCategory"))) {
            return getServiceCategory(res, model);
        }
        if (res.hasProperty(RDF.type, model.getResource("http://www.daml.org/services/owl-s/1.2/Profile.owl#ServiceParameter"))) {
            return getServiceParameter(res, model);
        }
        return new ThingImpl(res, model);
    }

    /**
	 * Returns an instance of an interface for the given Resource URI.  The return instance is guaranteed to 
	 * implement the most specific interface in *some* hierarchy in which the Resource participates.  The behavior
	 * is unspecified for resources with RDF types from different hierarchies.
	 * @return an instance of Thing
	 */
    public static Thing getThing(String uri, com.hp.hpl.jena.rdf.model.Model model) throws JastorException {
        return getThing(model.getResource(uri), model);
    }

    /**
	 * Return a list of compatible interfaces for the given type.  Searches through all ontology classes
	 * in the Profile.owl ontology.  The list is sorted according to the topological sort
	 * of the class hierarchy
	 * @return a List of type java.lang.Class
	 */
    public static java.util.List listCompatibleInterfaces(com.hp.hpl.jena.rdf.model.Resource type) {
        java.util.List types = new java.util.ArrayList();
        if (type.equals(org.marcont.services.definitions.profile.Profile.TYPE)) {
            types.add(org.marcont.services.definitions.profile.Profile.class);
        }
        if (type.equals(org.marcont.services.definitions.profile.ServiceCategory.TYPE)) {
            types.add(org.marcont.services.definitions.profile.ServiceCategory.class);
        }
        if (type.equals(org.marcont.services.definitions.profile.ServiceParameter.TYPE)) {
            types.add(org.marcont.services.definitions.profile.ServiceParameter.class);
        }
        return types;
    }
}
