package org.xmedia.oms.adapter.kaon2.ex;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.aifb.xxplore.shared.util.PropertyUtils;
import org.xmedia.businessobject.IBusinessObject;
import org.xmedia.oms.adapter.kaon2.persistence.Kaon2ConceptDao;
import org.xmedia.oms.adapter.kaon2.persistence.Kaon2ConnectionProvider;
import org.xmedia.oms.adapter.kaon2.persistence.Kaon2DaoManager;
import org.xmedia.oms.adapter.kaon2.persistence.Kaon2PropertyMemberDao;
import org.xmedia.oms.adapter.kaon2.query.SparqlQueryEvaluator;
import org.xmedia.oms.metaknow.IProvenance;
import org.xmedia.oms.metaknow.IReifiedElement;
import org.xmedia.oms.metaknow.IReifiedElementDao;
import org.xmedia.oms.model.api.IAxiom;
import org.xmedia.oms.model.api.IIndividual;
import org.xmedia.oms.model.api.INamedConcept;
import org.xmedia.oms.model.api.INamedIndividual;
import org.xmedia.oms.model.api.IOntology;
import org.xmedia.oms.model.api.IProperty;
import org.xmedia.oms.model.api.IPropertyMember;
import org.xmedia.oms.model.api.IResource;
import org.xmedia.oms.model.impl.NamedIndividual;
import org.xmedia.oms.model.impl.PropertyMember;
import org.xmedia.oms.persistence.DatasourceException;
import org.xmedia.oms.persistence.IConnectionProvider;
import org.xmedia.oms.persistence.ISession;
import org.xmedia.oms.persistence.ISessionFactory;
import org.xmedia.oms.persistence.ITransaction;
import org.xmedia.oms.persistence.InvalidParameterException;
import org.xmedia.oms.persistence.KbEnvironment;
import org.xmedia.oms.persistence.MissingParameterException;
import org.xmedia.oms.persistence.OntologyLoadException;
import org.xmedia.oms.persistence.OpenSessionException;
import org.xmedia.oms.persistence.PersistenceUtil;
import org.xmedia.oms.persistence.SessionFactory;
import org.xmedia.oms.persistence.StatelessSession;
import org.xmedia.oms.persistence.dao.DaoUnavailableException;
import org.xmedia.oms.persistence.dao.IIndividualDao;
import org.xmedia.oms.persistence.dao.IPropertyDao;
import org.xmedia.oms.persistence.dao.IPropertyMemberAxiomDao;
import org.xmedia.oms.query.IQueryResult;
import org.xmedia.oms.query.QueryWrapper;

/**
 * @author Administrator
 *
 */
public class MetaviewExample {

    private static IOntology m_onto;

    private static IOntology m_metaknow;

    private static IOntology m_metaviewExtension;

    private static IOntology m_policyOntology;

    private static IOntology loadOntology(Kaon2ConnectionProvider provider, Properties props, String uri) throws DatasourceException, MissingParameterException, InvalidParameterException, OntologyLoadException {
        props.setProperty(KbEnvironment.PHYSICAL_ONTOLOGY_URI, uri);
        return provider.getConnection().loadOntology(PropertyUtils.convertToMap(props));
    }

    /**
	 * Init the connection with Kaon2. 
	 * Here, you need to provide connection parameter to a store, e.g. a hsqldb, if the statements are to be stored persistently. The connection provider
	 * can be also configured to run in memory. Via the connection provider, ontologies can be loaded. Wihtin a session and using data access objects  
	 * provided by a DAO manager, retrieval operations can be performed on the loaded ontologies.
	 */
    private static void init() {
        Kaon2ConnectionProvider provider = new Kaon2ConnectionProvider();
        Properties props = new Properties();
        props.setProperty(KbEnvironment.CONNECTION_URL, "");
        props.setProperty(KbEnvironment.USER, "");
        props.setProperty(KbEnvironment.PASS, "");
        props.setProperty(KbEnvironment.DB_PRODUCT_NAME, "");
        props.setProperty(KbEnvironment.DB_DRIVER_CLASS, "");
        props.setProperty(KbEnvironment.TRANSACTION_CLASS, "org.xmedia.oms.adapter.kaon2.persistence.Kaon2Transaction");
        props.setProperty("policy.user", "http://www.domainontologies/hypermedia/odahs#testuser1");
        props.setProperty("policy.ontology_uri", "file:../OMS/res/org/xmedia/oms/model/onto/metaknow/TaskPolicyFiat.owl");
        provider.configure(props);
        try {
            m_metaknow = loadOntology(provider, props, "file:../OMS/res/org/xmedia/oms/model/onto/metaknow/metaknow.owl");
            m_metaviewExtension = loadOntology(provider, props, "file:res/metaknowledge-mw-ext.xml");
            m_onto = loadOntology(provider, props, "file:res/metaknow-example.xml");
        } catch (DatasourceException e1) {
            e1.printStackTrace();
        } catch (MissingParameterException e1) {
            e1.printStackTrace();
        } catch (InvalidParameterException e1) {
            e1.printStackTrace();
        } catch (OntologyLoadException e1) {
            e1.printStackTrace();
        }
        ISessionFactory factory = SessionFactory.getInstance();
        factory.configure(PropertyUtils.convertToMap(props));
        PersistenceUtil.setSessionFactory(factory);
        try {
            factory.openSession(provider.getConnection(), m_onto);
        } catch (DatasourceException e) {
            e.printStackTrace();
        } catch (OpenSessionException e) {
            e.printStackTrace();
        }
        PersistenceUtil.setDaoManager(Kaon2DaoManager.getInstance());
    }

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        init();
        StatelessSession session = (StatelessSession) PersistenceUtil.getSessionFactory().getCurrentSession();
        Kaon2ConceptDao conceptDao = (Kaon2ConceptDao) PersistenceUtil.getDaoManager().getConceptDao();
        System.out.println(conceptDao.findSubconcepts(conceptDao.findByUri("http://kaon2.semanticweb.org/example10-ontology#Person"), true));
        IIndividualDao individualDao = PersistenceUtil.getDaoManager().getIndividualDao();
        INamedIndividual adam = individualDao.findByUri("http://kaon2.semanticweb.org/example10-ontology#Adam");
        INamedIndividual eve = individualDao.findByUri("http://kaon2.semanticweb.org/example10-ontology#Eve");
        IPropertyDao propDao = PersistenceUtil.getDaoManager().getPropertyDao();
        IProperty hasRelation = propDao.findByUri("http://kaon2.semanticweb.org/example10-ontology#hasRelation");
        IPropertyMemberAxiomDao propMemberDao = PersistenceUtil.getDaoManager().getPropertyMemberDao();
        List<IPropertyMember> propMembers = propMemberDao.findAll();
        System.out.println("List of property members:");
        for (IPropertyMember propMember : propMembers) {
            System.out.println(" " + propMember);
            System.out.println("  agents: " + propMemberDao.getAgents(propMember));
            Double[] cdegrees = propMemberDao.getConfidenceDegrees(propMember);
            System.out.print("  confidence degrees: ");
            for (Double cdegree : cdegrees) System.out.print(cdegree + " ");
            System.out.println();
            System.out.println("  creation times: " + propMemberDao.getCreationDates(propMember));
        }
        System.out.println("Inserting new property member.");
        ITransaction trans = session.beginTransaction();
        IPropertyMember newpm = propMemberDao.insert(eve, hasRelation, adam);
        trans.commit();
        System.out.println(newpm);
        System.out.println("provenances: ");
        System.out.println(propMemberDao.getProvenances(newpm));
        IProvenance p = propMemberDao.createProvenance(null, 0.4, new Date(), null);
        System.out.println("attaching");
        ((Kaon2PropertyMemberDao) propMemberDao).attachProvenance(newpm, p);
        System.out.println("new prov:");
        System.out.println(propMemberDao.getProvenances(newpm));
        System.out.println("List of property members:");
        propMembers = propMemberDao.findAll();
        for (IPropertyMember propMember : propMembers) {
            System.out.println(" " + propMember);
        }
        System.out.println("All property members with confidence degree 0.4:");
        Set<IPropertyMember> pms = propMemberDao.findByConfidenceDegree(0.4, 0);
        for (IResource res : pms) {
            System.out.println(" " + res);
        }
        System.out.println("Axioms with confidence degree higher than 0.5:");
        pms = propMemberDao.findByConfidenceDegree(0.5, IReifiedElementDao.CONFIDENCE_DEGREE_HIGHER);
        printResources(pms);
        System.out.println("Axioms with creation date now:");
        pms = propMemberDao.findByCreationDate(new Date(), IReifiedElementDao.DATE_OLDER);
        printResources(pms);
        System.out.println("Axioms with agent #Thanh:");
        pms = propMemberDao.findByAgent("http://www.x-media.org/metaknow-ext#Thanh");
        printResources(pms);
    }

    private static void printResources(Set<IPropertyMember> pms) {
        for (IResource res : pms) {
            System.out.println(" " + res);
        }
    }
}
