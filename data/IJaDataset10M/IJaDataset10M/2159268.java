package eu.redseeds.sc.current.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;
import de.uni_koblenz.jgralab.Aggregation;
import de.uni_koblenz.jgralab.Attribute;
import de.uni_koblenz.jgralab.Edge;
import de.uni_koblenz.jgralab.EdgeDirection;
import de.uni_koblenz.jgralab.GraphFactory;
import de.uni_koblenz.jgralab.GraphIO;
import de.uni_koblenz.jgralab.GraphIOException;
import de.uni_koblenz.jgralab.Vertex;
import de.uni_koblenz.jgralab.schema.AggregationClass;
import de.uni_koblenz.jgralab.schema.EdgeClass;
import de.uni_koblenz.jgralab.schema.VertexClass;
import eu.redseeds.common.SCProjectHelper;
import eu.redseeds.scl.SCLGraph;
import eu.redseeds.scl.SCLSchema;
import eu.redseeds.scl.model.BusinessLayerFacade;
import eu.redseeds.scl.model.impl.BusinessLayerFacadeImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.actors.ActorDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.actors.ActorsPackageDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.domainelements.DomainElementRelationshipDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.domainelements.DomainElementRepresentationDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.domainelements.DomainSpecificationDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.notions.DomainStatementDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.notions.NotionDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.notions.NotionSpecialisationDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.notions.NotionsPackageDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.notions.PrimitiveDataTypeDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.phrases.ComplexVerbPhraseDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.phrases.NounLinkDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.phrases.NounPhraseDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.phrases.SimpleVerbPhraseDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.systemelements.SystemElementDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.systemelements.SystemElementsPackageDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.terms.DeterminerDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.terms.ModifierDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.terms.NounDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.terms.PrepositionDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rsldomainelements.terms.VerbDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentations.descriptiverequirementrepresentations.ConstrainedLanguageScenarioDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentations.descriptiverequirementrepresentations.SentenceListDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentationsentences.constrainedlanguagesentences.RejoinSentenceDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentationsentences.controlsentences.ConditionSentenceDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentationsentences.controlsentences.InvocationSentenceDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentationsentences.controlsentences.PostconditionSentenceDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentationsentences.controlsentences.PreconditionSentenceDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentationsentences.representationsentences.NaturalLanguageHypertextSentenceDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirementrepresentationsentences.svosentences.SVOSentenceDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.ConflictsWithDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.ConstrainsDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.DependsOnDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.DerivesFromDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.ElaboratesDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.FulfilsDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.IsSimilarToDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.MakesPossibleDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.OperationalizesDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsrelationships.RequirementVocabularyRelationshipDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsspecifications.RequirementDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsspecifications.RequirementsPackageDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsspecifications.RequirementsSpecificationDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.requirementsspecifications.UseCaseDTOImpl;
import eu.redseeds.scl.model.impl.rsl.rslrequirements.usecaserelationships.InvocationRelationshipDTOImpl;
import eu.redseeds.scl.model.impl.sclkernel.ArchitecturalModelDTOImpl;
import eu.redseeds.scl.model.impl.sclkernel.ClipboardDTOImpl;
import eu.redseeds.scl.model.impl.sclkernel.DetailedDesignModelDTOImpl;
import eu.redseeds.scl.model.impl.sclkernel.SoftwareCaseDTOImpl;
import eu.redseeds.scl.model.impl.sdsl.ClassDTOImpl;
import eu.redseeds.scl.model.impl.sdsl.ComponentDTOImpl;
import eu.redseeds.scl.model.impl.sdsl.InterfaceDTOImpl;
import eu.redseeds.scl.model.impl.sdsl.OperationDTOImpl;
import eu.redseeds.scl.model.impl.sdsl.PackageDTOImpl;
import eu.redseeds.scl.model.impl.traceability.DependencyLinkDTOImpl;
import eu.redseeds.scl.model.impl.traceability.TraceabilityLinkDTOImpl;
import eu.redseeds.scl.model.sclkernel.ClipboardDTO;
import eu.redseeds.scl.model.sclkernel.SoftwareCaseDTO;
import eu.redseeds.scl.rsl.rsldomainelements.actors.Actor;
import eu.redseeds.scl.rsl.rsldomainelements.actors.ActorsPackage;
import eu.redseeds.scl.rsl.rsldomainelements.domainelementrepresentations.DomainElementRepresentation;
import eu.redseeds.scl.rsl.rsldomainelements.domainelements.DomainElementRelationship;
import eu.redseeds.scl.rsl.rsldomainelements.domainelements.DomainElementsPackage;
import eu.redseeds.scl.rsl.rsldomainelements.domainelements.DomainSpecification;
import eu.redseeds.scl.rsl.rsldomainelements.notions.DomainStatement;
import eu.redseeds.scl.rsl.rsldomainelements.notions.Notion;
import eu.redseeds.scl.rsl.rsldomainelements.notions.NotionSpecialisation;
import eu.redseeds.scl.rsl.rsldomainelements.notions.NotionsPackage;
import eu.redseeds.scl.rsl.rsldomainelements.notions.PrimitiveDataType;
import eu.redseeds.scl.rsl.rsldomainelements.phrases.ComplexVerbPhrase;
import eu.redseeds.scl.rsl.rsldomainelements.phrases.NounLink;
import eu.redseeds.scl.rsl.rsldomainelements.phrases.NounPhrase;
import eu.redseeds.scl.rsl.rsldomainelements.phrases.SimpleVerbPhrase;
import eu.redseeds.scl.rsl.rsldomainelements.systemelements.SystemElement;
import eu.redseeds.scl.rsl.rsldomainelements.systemelements.SystemElementsPackage;
import eu.redseeds.scl.rsl.rsldomainelements.terms.Determiner;
import eu.redseeds.scl.rsl.rsldomainelements.terms.Modifier;
import eu.redseeds.scl.rsl.rsldomainelements.terms.Noun;
import eu.redseeds.scl.rsl.rsldomainelements.terms.Preposition;
import eu.redseeds.scl.rsl.rsldomainelements.terms.Verb;
import eu.redseeds.scl.rsl.rslrequirementrepresentations.descriptiverequirementrepresentations.ConstrainedLanguageScenario;
import eu.redseeds.scl.rsl.rslrequirementrepresentations.descriptiverequirementrepresentations.SentenceList;
import eu.redseeds.scl.rsl.rslrequirementrepresentationsentences.constrainedlanguagesentences.RejoinSentence;
import eu.redseeds.scl.rsl.rslrequirementrepresentationsentences.controlsentences.ConditionSentence;
import eu.redseeds.scl.rsl.rslrequirementrepresentationsentences.controlsentences.InvocationSentence;
import eu.redseeds.scl.rsl.rslrequirementrepresentationsentences.controlsentences.PostconditionSentence;
import eu.redseeds.scl.rsl.rslrequirementrepresentationsentences.controlsentences.PreconditionSentence;
import eu.redseeds.scl.rsl.rslrequirementrepresentationsentences.representationsentences.NaturalLanguageHypertextSentence;
import eu.redseeds.scl.rsl.rslrequirementrepresentationsentences.svosentences.SVOSentence;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.ConflictsWith;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.Constrains;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.DependsOn;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.DerivesFrom;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.Elaborates;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.Fulfils;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.IsSimilarTo;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.MakesPossible;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.Operationalizes;
import eu.redseeds.scl.rsl.rslrequirements.requirementrelationships.RequirementVocabularyRelationship;
import eu.redseeds.scl.rsl.rslrequirements.requirementsspecifications.RSLUseCase;
import eu.redseeds.scl.rsl.rslrequirements.requirementsspecifications.Requirement;
import eu.redseeds.scl.rsl.rslrequirements.requirementsspecifications.RequirementsPackage;
import eu.redseeds.scl.rsl.rslrequirements.requirementsspecifications.RequirementsSpecification;
import eu.redseeds.scl.rsl.rslrequirements.usecaserelationships.InvocationRelationship;
import eu.redseeds.scl.sclkernel.ArchitecturalModel;
import eu.redseeds.scl.sclkernel.Clipboard;
import eu.redseeds.scl.sclkernel.DetailedDesignModel;
import eu.redseeds.scl.sclkernel.IsAllocatedTo;
import eu.redseeds.scl.sclkernel.IsDependentOn;
import eu.redseeds.scl.sclkernel.SCLElement;
import eu.redseeds.scl.sclkernel.SCLElementsPackage;
import eu.redseeds.scl.sclkernel.SCLElementsPackageContainsElement;
import eu.redseeds.scl.sclkernel.SCLRelationship;
import eu.redseeds.scl.sclkernel.SoftwareArtifact;
import eu.redseeds.scl.sclkernel.SoftwareCase;
import eu.redseeds.scl.sclkernel.SourceCode;
import eu.redseeds.scl.uml.auxiliaryconstructs.models.Model;

/**
 * A container for information that the prototype user is working with: holds a
 * set of clipboards and currently developed software case.
 */
public class SCProject {

    private static final String GRAPH_FILE_NAME = "sclgraph.tg";

    protected Map<IFolder, SoftwareCaseDTO> clipboards;

    protected SoftwareCaseDTO mainCase;

    protected eu.redseeds.scl.SCLGraph graph;

    protected BusinessLayerFacade facade;

    /**
	 * Workspace location as a URI string.
	 */
    protected String workspaceLocation;

    /**
	 * Associated eclipse project
	 */
    protected IProject eclipseProject;

    /**
	 * false if some terms in the project are not connected to the terminology
	 */
    protected boolean termsConnected = true;

    public SCProject(IProject eclipseProject) {
        this.eclipseProject = eclipseProject;
        File path = eclipseProject.getLocation().toFile();
        File graphFile = new File(path, GRAPH_FILE_NAME);
        String file = null;
        if (graphFile.exists()) {
            try {
                file = graphFile.getCanonicalPath();
                graph = (SCLGraph) GraphIO.loadGraphFromFile(file, SCLSchema.instance(), null);
                facade = (BusinessLayerFacade) graph;
                mainCase = (SoftwareCaseDTOImpl) graph.getFirstSoftwareCase();
                mainCase.setName(eclipseProject.getName());
            } catch (Exception ex) {
                Activator.log("Error during reading graph: " + ex.getMessage() + " from file \"" + file + "\".", Status.ERROR);
                ex.printStackTrace();
            }
        } else {
            graph = createInitialGraph();
            facade = (BusinessLayerFacade) graph;
            try {
                file = graphFile.getCanonicalPath();
                GraphIO.saveGraphToFile(graphFile.getCanonicalPath(), graph, null);
            } catch (Exception e) {
                Activator.log("Error during writing graph: " + e.getMessage() + " to file \"" + file + "\".", Status.ERROR);
                e.printStackTrace();
            }
        }
    }

    private SCLGraph createInitialGraph() {
        SCLGraph graph = SCLSchema.instance().createSCLGraph(10000, 20000);
        eu.redseeds.scl.sclkernel.SoftwareCase sc = graph.createSoftwareCase();
        graph.createTerminology();
        RequirementsSpecification rs = graph.createRequirementsSpecification();
        rs.setName("RequirementsSpecification");
        sc.addArtifact(rs);
        DomainSpecification ds = graph.createDomainSpecification();
        ds.setName("DomainSpecification");
        rs.addDomainSpecification(ds);
        NotionsPackage np = graph.createNotionsPackage();
        np.setName("Notions");
        ds.addDomainElementsPackage(np);
        ActorsPackage ap = graph.createActorsPackage();
        ap.setName("Actors");
        ds.addDomainElementsPackage(ap);
        SystemElementsPackage sp = graph.createSystemElementsPackage();
        sp.setName("SystemElements");
        ds.addDomainElementsPackage(sp);
        ArchitecturalModel architecture = graph.createArchitecturalModel();
        sc.addArtifact(architecture);
        DetailedDesignModel design = graph.createDetailedDesignModel();
        sc.addArtifact(design);
        SourceCode code = graph.createSourceCode();
        sc.addArtifact(code);
        mainCase = (SoftwareCaseDTOImpl) sc;
        mainCase.setName(eclipseProject.getName());
        return graph;
    }

    public Collection<ClipboardDTO> getClipboardList() {
        List<ClipboardDTO> result = new ArrayList<ClipboardDTO>();
        for (Clipboard clipboard : graph.getClipboardVertices()) {
            result.add((ClipboardDTO) clipboard);
        }
        return result;
    }

    /**
	 * Imports a slice given as parameter <code>sc</code> into this SCProject.
	 * Basically, it copies all elements that belong to the slice from the graph
	 * of the slice to the graph of the current project.
	 * 
	 * @param sc
	 */
    public void importSlice(eu.redseeds.sc.slicing.Slice sc) {
        SCLGraph source = (SCLGraph) sc.getGraph();
        SCLGraph target = getSCLGraph();
        Iterable<Vertex> elementsToCopy = sc.getVertices();
        copyElements(source, target, elementsToCopy);
        save();
        SCProjectHelper.refreshSCNavigator();
    }

    /**
	 * Copies all elements in the <code>elementsToCopy</code> set from the graph
	 * <code>source</code> to the graph <code>target</code> together with the
	 * edges in the graph <code>source</code> that start and end at an element
	 * of <code>elementsToCopy</code>.
	 */
    @SuppressWarnings("unchecked")
    private void copyElements(SCLGraph source, SCLGraph target, Iterable<? extends Vertex> elementsToCopy) {
        Map<Integer, Integer> vertexIdMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> edgeIdMap = new HashMap<Integer, Integer>();
        Queue<SCLElement> vertexQueue = new LinkedList<SCLElement>();
        Queue<SCLRelationship> relationQueue = new LinkedList<SCLRelationship>();
        for (Vertex v : elementsToCopy) {
            if (v instanceof SCLElement) {
                if (v instanceof SoftwareCase) continue;
                if (v instanceof SoftwareArtifact) continue;
                vertexQueue.add((SCLElement) v);
            } else if (v instanceof SCLRelationship) relationQueue.add((SCLRelationship) v);
        }
        Set<Vertex> completeElements = new HashSet<Vertex>();
        while (!vertexQueue.isEmpty()) {
            SCLElement sclElement = vertexQueue.poll();
            if (!(sclElement instanceof SoftwareCase) && !(sclElement instanceof SoftwareArtifact) && (!(sclElement instanceof DomainSpecification))) {
                completeElements.add(sclElement);
                for (Edge e : sclElement.incidences(Aggregation.class)) {
                    AggregationClass ac = (AggregationClass) e.getAttributedElementClass();
                    if (ac.isAggregateFrom() != e.isNormal() && (e.getThat() instanceof SCLElement)) {
                        SCLElement that = (SCLElement) e.getThat();
                        if (!completeElements.contains(that) && !(that instanceof SoftwareCase) && !(that instanceof SoftwareArtifact)) {
                            completeElements.add(that);
                            vertexQueue.add(that);
                        }
                    }
                }
                Edge nameEdge = sclElement.getFirstRepresentableElementContainsName(EdgeDirection.OUT);
                if (nameEdge != null) {
                    SCLElement name = (SCLElement) nameEdge.getThat();
                    completeElements.add(name);
                }
            }
        }
        while (!relationQueue.isEmpty()) {
            SCLRelationship relationship = relationQueue.poll();
            Edge sourceEdge = relationship.getFirstSCLRelationshipLinksToSource();
            Edge targetEdge = relationship.getFirstSCLRelationshipLinksToTarget();
            if (sourceEdge != null) {
                SCLElement sourceVertex = (SCLElement) sourceEdge.getThat();
                if (targetEdge != null) {
                    SCLElement targetVertex = (SCLElement) targetEdge.getThat();
                    if (completeElements.contains(sourceVertex) && completeElements.contains(targetVertex)) {
                        completeElements.add(relationship);
                    }
                }
            }
        }
        elementsToCopy = completeElements;
        Clipboard cp = target.createClipboard();
        cp.setName("ImportedSlice " + source.getFirstSoftwareCase().getName());
        RequirementsSpecification reqModel = target.createRequirementsSpecification();
        DomainSpecification domSpec = target.createDomainSpecification();
        graph.createRequirementsSpecificationLinksToDomainSpecification(reqModel, domSpec);
        cp.addArtifact(reqModel);
        ArchitecturalModel arcModel = target.createArchitecturalModel();
        cp.addArtifact(arcModel);
        DetailedDesignModel designModel = target.createDetailedDesignModel();
        cp.addArtifact(designModel);
        for (Vertex oldVertex : elementsToCopy) {
            Vertex newVertex = target.createVertex((Class<? extends Vertex>) oldVertex.getM1Class());
            VertexClass vc = (VertexClass) newVertex.getAttributedElementClass();
            for (Attribute attr : vc.getAttributeList()) {
                try {
                    newVertex.setAttribute(attr.getName(), oldVertex.getAttribute(attr.getName()));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            if (vertexIdMap.containsKey(oldVertex.getId())) {
                System.out.println("Error in importing slice");
                throw new RuntimeException("Error in importing slice - failure in algorithm!");
            }
            vertexIdMap.put(oldVertex.getId(), newVertex.getId());
        }
        for (Edge oldEdge : source.edges()) {
            if (!vertexIdMap.containsKey(oldEdge.getAlpha().getId())) continue;
            if (!vertexIdMap.containsKey(oldEdge.getOmega().getId())) continue;
            Vertex alpha = target.getVertex(vertexIdMap.get(oldEdge.getAlpha().getId()));
            Vertex omega = target.getVertex(vertexIdMap.get(oldEdge.getOmega().getId()));
            Edge newEdge = target.createEdge((Class<? extends Edge>) oldEdge.getM1Class(), alpha, omega);
            EdgeClass ec = (EdgeClass) newEdge.getAttributedElementClass();
            for (Attribute attr : ec.getAttributeList()) {
                try {
                    newEdge.setAttribute(attr.getName(), oldEdge.getAttribute(attr.getName()));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            edgeIdMap.put(oldEdge.getId(), newEdge.getId());
        }
        for (Vertex v : elementsToCopy) {
            Vertex newVertex = target.getVertex(vertexIdMap.get(v.getId()));
            Edge previousIncidence = null;
            for (Edge oldIncidence : v.incidences()) {
                Edge newIncidence = null;
                Integer oldEdgeId = oldIncidence.getNormalEdge().getId();
                if (edgeIdMap.containsKey(oldEdgeId)) {
                    newIncidence = target.getEdge(edgeIdMap.get(oldEdgeId));
                    if (!oldIncidence.isNormal()) {
                        newIncidence = newIncidence.getReversedEdge();
                    }
                    if (previousIncidence == null) {
                        if (newVertex.getFirstEdge() != null) {
                            if (newVertex.getFirstEdge() != newIncidence) newIncidence.putEdgeBefore(newVertex.getFirstEdge());
                        }
                    } else {
                        if (previousIncidence != newIncidence) newIncidence.putEdgeAfter(previousIncidence);
                    }
                    previousIncidence = newIncidence;
                }
            }
        }
        for (Vertex v : elementsToCopy) {
            Vertex newVertex = target.getVertex(vertexIdMap.get(v.getId()));
            if (newVertex instanceof SCLElementsPackage) {
                SCLElementsPackage pkg = (SCLElementsPackage) newVertex;
                SCLElementsPackageContainsElement edge = pkg.getFirstSCLElementsPackageContainsNestedPackage(EdgeDirection.IN);
                if (edge == null) {
                    if (pkg instanceof RequirementsPackage) {
                        graph.createRequirementsSpecificationContainsRequirementsPackage(reqModel, (RequirementsPackage) pkg);
                    } else if (pkg instanceof DomainElementsPackage) {
                        graph.createDomainSpecificationContainsDomainElementsPackage(domSpec, (DomainElementsPackage) pkg);
                    }
                }
            } else if (newVertex instanceof Model) {
                Model oldModel = (Model) v;
                Edge edge1 = oldModel.getFirstArtifactContainsUmlModel(EdgeDirection.IN);
                if (edge1.getThat() instanceof ArchitecturalModel) {
                    graph.createArtifactContainsUmlModel(arcModel, (Model) newVertex);
                } else if (edge1.getThat() instanceof DetailedDesignModel) {
                    graph.createArtifactContainsUmlModel(designModel, (Model) newVertex);
                }
            }
        }
    }

    public String getName() {
        return mainCase.getName();
    }

    public void setName(String name) {
        mainCase.setName(name);
    }

    public String getWorkspaceLocation() {
        return workspaceLocation;
    }

    public void setWorkspaceLocation(String workspaceLocation) {
        this.workspaceLocation = workspaceLocation;
    }

    public void deleteClipboard(IFolder clipboardFolder) {
        clipboards.remove(clipboardFolder);
    }

    public boolean containsClipboard(IFolder clipboardFolder) {
        return clipboards.containsKey(clipboardFolder);
    }

    public IProject getEclipseProject() {
        return eclipseProject;
    }

    public SCLGraph getSCLGraph() {
        return graph;
    }

    public BusinessLayerFacade getBusinessLayerFacade() {
        return facade;
    }

    public SoftwareCaseDTO getMainCase() {
        return mainCase;
    }

    /**
	 * saves the SCProject, i.e. writes changes to the disk
	 */
    public void save() {
        File path = eclipseProject.getLocation().toFile();
        File graphFile = new File(path, GRAPH_FILE_NAME);
        try {
            GraphIO.saveGraphToFile(graphFile.toString(), graph, null);
        } catch (GraphIOException ex) {
            Activator.log("Error during saving graph: " + ex.getMessage(), Status.ERROR);
        }
    }

    public static void setGraphImplementationClasses() {
        GraphFactory factory = SCLSchema.instance().getGraphFactory();
        factory.setVertexImplementationClass(SVOSentence.class, SVOSentenceDTOImpl.class);
        factory.setVertexImplementationClass(NounPhrase.class, NounPhraseDTOImpl.class);
        factory.setVertexImplementationClass(Noun.class, NounDTOImpl.class);
        factory.setVertexImplementationClass(NounLink.class, NounLinkDTOImpl.class);
        factory.setVertexImplementationClass(SimpleVerbPhrase.class, SimpleVerbPhraseDTOImpl.class);
        factory.setVertexImplementationClass(ComplexVerbPhrase.class, ComplexVerbPhraseDTOImpl.class);
        factory.setVertexImplementationClass(SoftwareCase.class, SoftwareCaseDTOImpl.class);
        factory.setVertexImplementationClass(RequirementsSpecification.class, RequirementsSpecificationDTOImpl.class);
        factory.setVertexImplementationClass(ArchitecturalModel.class, ArchitecturalModelDTOImpl.class);
        factory.setVertexImplementationClass(DetailedDesignModel.class, DetailedDesignModelDTOImpl.class);
        factory.setVertexImplementationClass(RequirementsPackage.class, RequirementsPackageDTOImpl.class);
        factory.setVertexImplementationClass(DomainSpecification.class, DomainSpecificationDTOImpl.class);
        factory.setVertexImplementationClass(ActorsPackage.class, ActorsPackageDTOImpl.class);
        factory.setVertexImplementationClass(NotionsPackage.class, NotionsPackageDTOImpl.class);
        factory.setVertexImplementationClass(SystemElementsPackage.class, SystemElementsPackageDTOImpl.class);
        factory.setVertexImplementationClass(Requirement.class, RequirementDTOImpl.class);
        factory.setVertexImplementationClass(RSLUseCase.class, UseCaseDTOImpl.class);
        factory.setVertexImplementationClass(Actor.class, ActorDTOImpl.class);
        factory.setVertexImplementationClass(Notion.class, NotionDTOImpl.class);
        factory.setVertexImplementationClass(DomainStatement.class, DomainStatementDTOImpl.class);
        factory.setVertexImplementationClass(SystemElement.class, SystemElementDTOImpl.class);
        factory.setVertexImplementationClass(ConstrainedLanguageScenario.class, ConstrainedLanguageScenarioDTOImpl.class);
        factory.setVertexImplementationClass(Clipboard.class, ClipboardDTOImpl.class);
        factory.setGraphImplementationClass(eu.redseeds.scl.SCLGraph.class, BusinessLayerFacadeImpl.class);
        factory.setVertexImplementationClass(NaturalLanguageHypertextSentence.class, NaturalLanguageHypertextSentenceDTOImpl.class);
        factory.setVertexImplementationClass(SentenceList.class, SentenceListDTOImpl.class);
        factory.setVertexImplementationClass(Determiner.class, DeterminerDTOImpl.class);
        factory.setVertexImplementationClass(Modifier.class, ModifierDTOImpl.class);
        factory.setVertexImplementationClass(Verb.class, VerbDTOImpl.class);
        factory.setVertexImplementationClass(Preposition.class, PrepositionDTOImpl.class);
        factory.setVertexImplementationClass(ConditionSentence.class, ConditionSentenceDTOImpl.class);
        factory.setVertexImplementationClass(InvocationSentence.class, InvocationSentenceDTOImpl.class);
        factory.setVertexImplementationClass(PreconditionSentence.class, PreconditionSentenceDTOImpl.class);
        factory.setVertexImplementationClass(PostconditionSentence.class, PostconditionSentenceDTOImpl.class);
        factory.setVertexImplementationClass(RejoinSentence.class, RejoinSentenceDTOImpl.class);
        factory.setVertexImplementationClass(DomainElementRepresentation.class, DomainElementRepresentationDTOImpl.class);
        factory.setVertexImplementationClass(DomainElementRelationship.class, DomainElementRelationshipDTOImpl.class);
        factory.setVertexImplementationClass(NotionSpecialisation.class, NotionSpecialisationDTOImpl.class);
        factory.setVertexImplementationClass(InvocationRelationship.class, InvocationRelationshipDTOImpl.class);
        factory.setVertexImplementationClass(ConflictsWith.class, ConflictsWithDTOImpl.class);
        factory.setVertexImplementationClass(Constrains.class, ConstrainsDTOImpl.class);
        factory.setVertexImplementationClass(DependsOn.class, DependsOnDTOImpl.class);
        factory.setVertexImplementationClass(DerivesFrom.class, DerivesFromDTOImpl.class);
        factory.setVertexImplementationClass(Elaborates.class, ElaboratesDTOImpl.class);
        factory.setVertexImplementationClass(Fulfils.class, FulfilsDTOImpl.class);
        factory.setVertexImplementationClass(IsSimilarTo.class, IsSimilarToDTOImpl.class);
        factory.setVertexImplementationClass(MakesPossible.class, MakesPossibleDTOImpl.class);
        factory.setVertexImplementationClass(Operationalizes.class, OperationalizesDTOImpl.class);
        factory.setVertexImplementationClass(RequirementVocabularyRelationship.class, RequirementVocabularyRelationshipDTOImpl.class);
        factory.setVertexImplementationClass(eu.redseeds.scl.uml.classes.kernel.Package.class, PackageDTOImpl.class);
        factory.setVertexImplementationClass(eu.redseeds.scl.uml.usecases.Actor.class, eu.redseeds.scl.model.impl.sdsl.ActorDTOImpl.class);
        factory.setVertexImplementationClass(eu.redseeds.scl.uml.classes.kernel.Class.class, ClassDTOImpl.class);
        factory.setVertexImplementationClass(eu.redseeds.scl.uml.classes.interfaces.Interface.class, InterfaceDTOImpl.class);
        factory.setVertexImplementationClass(eu.redseeds.scl.uml.components.basiccomponents.Component.class, ComponentDTOImpl.class);
        factory.setVertexImplementationClass(eu.redseeds.scl.uml.classes.kernel.Operation.class, OperationDTOImpl.class);
        factory.setVertexImplementationClass(IsAllocatedTo.class, TraceabilityLinkDTOImpl.class);
        factory.setVertexImplementationClass(IsDependentOn.class, DependencyLinkDTOImpl.class);
        factory.setVertexImplementationClass(PrimitiveDataType.class, PrimitiveDataTypeDTOImpl.class);
    }

    static {
        setGraphImplementationClasses();
    }

    public boolean isTermsConnected() {
        return termsConnected;
    }

    public void setTermsConnected(boolean termsConnected) {
        this.termsConnected = termsConnected;
    }
}
