package es.upm.dit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Container;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasonerFactory;

public class OldFoafParser {

    List<Property> propertyAccount = new ArrayList<Property>();

    List<Property> propertyAccountName = new ArrayList<Property>();

    List<Property> propertyAccountProfile = new ArrayList<Property>();

    List<Resource> agentResource = new ArrayList<Resource>();

    public static void main(String[] args) throws Exception {
        FoafParser foaf = new FoafParser();
        foaf.foafAgent("dir/foafAndrea.rdf");
    }

    public void foafAgent(String inputFileName) {
        String foafNamespace = "http://xmlns.com/foaf/0.1/";
        Model model = ModelFactory.createOntologyModel();
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }
        model.read(in, "", null);
        System.out.println("Base Namespace:" + model.getNsPrefixURI(""));
        if (model.getNsPrefixURI("foaf") != null) {
            foafNamespace = model.getNsPrefixURI("foaf");
            System.out.println("foaf namespace:" + foafNamespace);
        }
        Resource conf = model.createResource();
        RDFSRuleReasoner reasoner = (RDFSRuleReasoner) RDFSRuleReasonerFactory.theInstance().create(conf);
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);
        model = infModel;
        propertyAccountName.add(ResourceFactory.createProperty(foafNamespace, "accountName"));
        propertyAccountProfile.add(ResourceFactory.createProperty(foafNamespace, "accountProfilePage"));
        propertyAccount.add(ResourceFactory.createProperty(foafNamespace, "account"));
        propertyAccount.add(ResourceFactory.createProperty(foafNamespace, "holdsAccount"));
        agentResource.add(ResourceFactory.createResource(foafNamespace + "Agent"));
        agentResource.add(ResourceFactory.createResource(foafNamespace + "Person"));
        Resource person = ResourceFactory.createResource(foafNamespace + "Person");
        for (Resource agent : agentResource) {
            ResIterator iters = model.listResourcesWithProperty(RDF.type, agent);
            if (iters.hasNext()) {
                System.out.println("The database contains resource Person for:");
                while (iters.hasNext()) {
                    Resource resource = iters.nextResource();
                    String resourceName = null;
                    if (resource.getLocalName() != null) {
                        resourceName = resource.getLocalName();
                    } else if (resource.getId() != null) {
                        if (resource.getId().getLabelString() != null) {
                            resourceName = resource.getId().getLabelString();
                        } else {
                            resourceName = resource.getId().toString();
                        }
                    } else if (resource.getURI() != null) {
                        resourceName = resource.getURI();
                    }
                    System.out.println("  " + resourceName + " class:" + resource.getClass());
                    NodeIterator nodes = model.listObjectsOfProperty(resource, RDF.type);
                    while (nodes.hasNext()) {
                        RDFNode node = nodes.nextNode();
                        if (node.isResource()) {
                            System.out.println("   type " + node.asResource().getURI());
                        }
                    }
                    StmtIterator stmtI = model.listStatements(resource, null, (RDFNode) null);
                    while (stmtI.hasNext()) {
                        Statement statement = stmtI.nextStatement();
                        System.out.println("   triple " + statement.getPredicate() + " - " + statement.getObject());
                    }
                    for (Property property : propertyAccount) {
                        StmtIterator stmtI1 = model.listStatements(resource, property, (RDFNode) null);
                        while (stmtI1.hasNext()) {
                            Statement statement = stmtI1.nextStatement();
                            System.out.println("   OnlineAccount " + statement.getObject());
                            if (statement.getObject().isResource()) {
                                Resource onlineAccount = statement.getObject().asResource();
                                NodeIterator nodess = model.listObjectsOfProperty(onlineAccount, RDF.type);
                                while (nodess.hasNext()) {
                                    RDFNode node = nodess.nextNode();
                                    if (node.isResource()) {
                                        System.out.println("      type " + node.asResource().getURI());
                                    }
                                }
                                for (Property property2 : propertyAccountName) {
                                    StmtIterator stmtI2 = model.listStatements(onlineAccount, property2, (RDFNode) null);
                                    Statement statement2 = stmtI2.nextStatement();
                                    System.out.println("      AccountName " + statement2.getObject());
                                }
                                for (Property property2 : propertyAccountProfile) {
                                    StmtIterator stmtI2 = model.listStatements(onlineAccount, property2, (RDFNode) null);
                                    Statement statement2 = stmtI2.nextStatement();
                                    System.out.println("      AccountProfile " + statement2.getObject());
                                }
                            }
                        }
                    }
                }
            }
        }
        ResIterator iters = model.listSubjectsWithProperty(RDF.type, foafNamespace + "Person");
        if (iters.hasNext()) {
            System.out.println("The database contains literal person for:");
            while (iters.hasNext()) {
                Resource resource = iters.nextResource();
                System.out.println("  " + resource.getLocalName());
            }
        } else {
            System.out.println("No simple String foafNamespace+Person were found in the database");
        }
        Property propertyOnlineAccount = ResourceFactory.createProperty(foafNamespace, "OnlineAccount");
        iters = model.listSubjectsWithProperty(propertyOnlineAccount);
        if (iters.hasNext()) {
            System.out.println("The database contains OnlineAccount for:");
            while (iters.hasNext()) {
                Resource resource = iters.nextResource();
                System.out.println("  " + resource.getLocalName());
            }
        } else {
            System.out.println("No PROPERTY OnlineAccount were found in the database");
        }
        iters = model.listSubjectsWithProperty(RDF.type);
        if (iters.hasNext()) {
            System.out.println("The database contains RDF.type for:");
            while (iters.hasNext()) {
                Resource resource = iters.nextResource();
                NodeIterator nodes = model.listObjectsOfProperty(resource, RDF.type);
                String resourceName = null;
                if (resource.getLocalName() != null) {
                    resourceName = resource.getLocalName();
                } else if (resource.getId() != null) {
                    if (resource.getId().getLabelString() != null) {
                        resourceName = resource.getId().getLabelString();
                    } else {
                        resourceName = resource.getId().toString();
                    }
                } else if (resource.getURI() != null) {
                    resourceName = resource.getURI();
                }
                System.out.println("  " + resourceName);
                while (nodes.hasNext()) {
                    RDFNode node = nodes.nextNode();
                    if (node.isLiteral()) {
                        System.out.println("   l " + node.asLiteral().getString());
                    } else if (node.isResource()) {
                        System.out.println("   r " + node.asResource().getURI());
                    }
                }
            }
        } else {
            System.out.println("No subject with RDF.type were found in the database");
        }
        Resource resourceOnlineAccount = ResourceFactory.createResource(foafNamespace + "OnlineAccount");
        StmtIterator stmtI = model.listStatements(null, RDF.type, resourceOnlineAccount);
        if (stmtI.hasNext()) {
            System.out.println("The database contains RDF.type OnlineAccount for:");
            while (stmtI.hasNext()) {
                Statement statement = stmtI.nextStatement();
                System.out.println("  " + statement.getSubject() + " - " + statement.getPredicate() + " - " + statement.getObject());
            }
        } else {
            System.out.println("No subject with RDF.type OnlineAccount were found in the database");
        }
    }

    public void getContainerResources(Resource resource, Model model, String resourceName) {
        try {
            Container resourceBag = (Container) resource;
            NodeIterator nodeI = resourceBag.iterator();
            while (nodeI.hasNext()) {
                RDFNode node = nodeI.nextNode();
                System.out.println("   node to check:" + node);
                if (node.isResource()) {
                    NodeIterator nodess = model.listObjectsOfProperty((Resource) node, null);
                    while (nodess.hasNext()) {
                        RDFNode profile = nodess.nextNode();
                        System.out.println("     " + profile);
                    }
                }
            }
        } catch (java.lang.ClassCastException e) {
            System.out.println("   " + resourceName + " does not have resources inside");
        }
    }
}
