package org.semanticorm.sesame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.semanticorm.DataSet;
import org.semanticorm.DatasetAddException;
import org.semanticorm.DatasetCommitException;
import org.semanticorm.DatasetCreationException;
import org.semanticorm.OrmMetadata;
import org.semanticorm.PrefixMappings;
import org.semanticorm.RdfRelations;
import org.semanticorm.RdfResource;
import org.semanticorm.RelationDemux;
import org.semanticorm.ResourceState;

public class SesameDataSet<PrimaryResourceType extends RdfResource> implements DataSet<PrimaryResourceType> {

    private Set<PrimaryResourceType> _primaryResources = new LinkedHashSet<PrimaryResourceType>();

    private Map<RdfResource, ResourceState> _resources = new LinkedHashMap<RdfResource, ResourceState>();

    private Map<Resource, RdfResource> _resourceCache = new HashMap<Resource, RdfResource>();

    private Map<URI, RelationDemux> _relationDemuxCache = new HashMap<URI, RelationDemux>();

    private final RepositoryConnection _connection;

    private Object _intDatatype;

    private final Class<PrimaryResourceType> _primaryResourceType;

    private ValueFactory _valueFactory;

    private final OrmMetadata _ormMetadata;

    public SesameDataSet(OrmMetadata ormMetadata, RepositoryConnection connection, Class<PrimaryResourceType> primaryResourceType, GraphQueryResult graph) throws DatasetCreationException {
        _ormMetadata = ormMetadata;
        _connection = connection;
        _primaryResourceType = primaryResourceType;
        _valueFactory = _connection.getRepository().getValueFactory();
        try {
            List<Statement> resourceRelations = createResources(graph);
            applyResourceRelationships(resourceRelations);
            recordResourceState();
            _resourceCache = null;
            _relationDemuxCache = null;
        } catch (QueryEvaluationException e) {
            throw new DatasetCreationException("Failed to create dataset", e);
        } catch (IOException e) {
            throw new DatasetCreationException("Failed to serialise created data", e);
        }
    }

    private List<Statement> createResources(GraphQueryResult graphResult) throws QueryEvaluationException, DatasetCreationException {
        List<Statement> resourceRelations = new ArrayList<Statement>();
        Resource currentResourceUri = null;
        Object currentResource = null;
        for (GraphQueryResult pos = graphResult; pos.hasNext(); ) {
            Statement statement = pos.next();
            Resource subject = statement.getSubject();
            Value value = statement.getObject();
            URI predicate = statement.getPredicate();
            if (subject instanceof URI) {
                if (currentResourceUri != subject) {
                    currentResourceUri = subject;
                    currentResource = getResource(currentResourceUri, predicate);
                }
            }
            if (value instanceof Literal) {
                processLiteral(currentResource, (Literal) value, predicate);
            } else if (value instanceof URI) {
                resourceRelations.add(statement);
            }
        }
        return resourceRelations;
    }

    private void processLiteral(Object currentResource, Literal value, URI predicate) throws DatasetCreationException {
        RelationDemux relationDemux = getRelationDemux(predicate);
        Object boxedValue = getValueFast(value);
        if (boxedValue == null) {
            boxedValue = getValueSlow(value);
        }
        relationDemux.setValue(currentResource, boxedValue);
    }

    private void applyResourceRelationships(List<Statement> resourceRelations) throws DatasetCreationException {
        for (Statement resourceRelation : resourceRelations) {
            RdfResource srcResource = _resourceCache.get(resourceRelation.getSubject());
            RdfResource destResource = _resourceCache.get(resourceRelation.getObject());
            RelationDemux relationDemux = getRelationDemux(resourceRelation.getPredicate());
            relationDemux.setValue(srcResource, destResource);
        }
    }

    private void recordResourceState() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        for (RdfResource resource : _resources.keySet()) {
            objectOutputStream.writeObject(resource);
        }
        objectOutputStream.flush();
    }

    private Object getValueSlow(Literal literalValue) {
        Object datatype = literalValue.getDatatype();
        if ("http://www.w3.org/2001/XMLSchema#int".equals(datatype.toString())) {
            _intDatatype = datatype;
            return literalValue.intValue();
        }
        throw new RuntimeException("Failed to parse value");
    }

    private Object getValueFast(Literal literalValue) {
        Object datatype = literalValue.getDatatype();
        if (datatype == null) {
            return literalValue.stringValue();
        } else if (datatype == _intDatatype) {
            return literalValue.intValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private RdfResource getResource(Resource resourceUri, URI predicate) throws DatasetCreationException {
        RdfResource resource = _resourceCache.get(resourceUri);
        if (resource == null) {
            RelationDemux relationDemux = getRelationDemux(predicate);
            resource = relationDemux.newInstance();
            resource.setUri(resourceUri.stringValue());
            _resourceCache.put(resourceUri, resource);
            if (_primaryResourceType.isInstance(resource)) {
                _primaryResources.add((PrimaryResourceType) resource);
            }
            _resources.put(resource, ResourceState.RETRIEVED);
        }
        return resource;
    }

    private RelationDemux getRelationDemux(URI predicate) throws DatasetCreationException {
        RelationDemux relationDemux = _relationDemuxCache.get(predicate);
        if (relationDemux == null) {
            relationDemux = new RelationDemux(predicate.toString());
            _relationDemuxCache.put(predicate, relationDemux);
        }
        return relationDemux;
    }

    public Set<PrimaryResourceType> getResources() {
        return _primaryResources;
    }

    public void commit() throws DatasetCommitException {
        try {
            for (Entry<RdfResource, ResourceState> resourceEntry : _resources.entrySet()) {
                ResourceState state = resourceEntry.getValue();
                if (state == ResourceState.REMOVED) {
                    _connection.remove(_connection.getRepository().getValueFactory().createURI(resourceEntry.getKey().getUri()), null, null);
                }
                if (state == ResourceState.ADDED) {
                    applyResource(resourceEntry.getKey());
                }
                if (state == ResourceState.RETRIEVED) {
                    _connection.remove(_connection.getRepository().getValueFactory().createURI(resourceEntry.getKey().getUri()), null, null);
                    applyResource(resourceEntry.getKey());
                }
            }
            _connection.commit();
        } catch (RepositoryException e) {
            throw new DatasetCommitException("Failed to commit to the repository", e);
        }
    }

    private void applyResource(RdfResource resource) {
        PrefixMappings prefixMappings = _ormMetadata.getPrefixMappings(resource.getClass());
        try {
            Map<String, BNode> blankNodeCache = new HashMap<String, BNode>();
            Field[] relationalFields = _ormMetadata.getRelationalFields(resource.getClass());
            for (Field field : relationalFields) {
                RdfRelations relations = field.getAnnotation(RdfRelations.class);
                URI subject = _valueFactory.createURI(resource.getUri());
                field.setAccessible(true);
                if (Collection.class.isAssignableFrom(field.getType())) {
                    Collection<RdfResource> collection = (Collection<RdfResource>) field.get(resource);
                    for (RdfResource referencedResource : collection) {
                        referencedResource.getUri();
                        URI referencedUri = _valueFactory.createURI(referencedResource.getUri());
                        createAndApplyStatements(prefixMappings, subject, referencedUri, relations.value(), blankNodeCache);
                    }
                } else if (RdfResource.class.isAssignableFrom(field.getType())) {
                    RdfResource referencedResource = ((RdfResource) field.get(resource));
                    URI referencedUri = _valueFactory.createURI(referencedResource.getUri());
                    createAndApplyStatements(prefixMappings, subject, referencedUri, relations.value(), blankNodeCache);
                } else {
                    Literal literal = null;
                    if (field.getType() == String.class) {
                        literal = _valueFactory.createLiteral((String) field.get(resource));
                    } else if (field.getType() == int.class) {
                        literal = _valueFactory.createLiteral((int) (Integer) field.get(resource));
                    }
                    createAndApplyStatements(prefixMappings, subject, literal, relations.value(), blankNodeCache);
                }
            }
        } catch (IllegalAccessException e) {
            throw new DatasetAddException("Could not add object due to insufficient access", e);
        } catch (RepositoryException e) {
            throw new DatasetAddException("Failed to add statement to the repository", e);
        }
    }

    private void createAndApplyStatements(PrefixMappings prefixMappings, URI subject, Value value, String[] relations, Map<String, BNode> blankNodeCache) throws RepositoryException {
        Resource context = subject;
        for (int count = 0; count < relations.length; count++) {
            String relation = prefixMappings.dereference(relations[count]);
            Statement statement;
            if (count != relations.length - 1) {
                BNode node = blankNodeCache.get(relation);
                if (node == null) {
                    node = _valueFactory.createBNode();
                    blankNodeCache.put(relation, node);
                    statement = _valueFactory.createStatement(context, _valueFactory.createURI(relation), node);
                    _connection.add(statement);
                }
                context = node;
            } else {
                statement = _valueFactory.createStatement(context, _valueFactory.createURI(relation), value);
                _connection.add(statement);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void add(RdfResource resource) {
        ResourceState state = _resources.get(resource);
        if (state == null) {
            if (_primaryResourceType.isInstance(resource)) {
                _primaryResources.add((PrimaryResourceType) resource);
            }
            _resources.put(resource, ResourceState.ADDED);
        } else if (state == ResourceState.REMOVED) {
            _resources.put(resource, ResourceState.RETRIEVED);
        }
    }

    public void addGraph(RdfResource resource, Class... classes) {
        addGraph(resource, new HashSet<Resource>());
    }

    @SuppressWarnings("unchecked")
    private void addGraph(RdfResource resource, Set<Resource> processedResources) {
        if (!processedResources.contains(resource)) {
            try {
                add(resource);
                Field[] relationalFields = _ormMetadata.getRelationalFields(resource.getClass());
                for (Field field : relationalFields) {
                    field.setAccessible(true);
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        Collection<RdfResource> collection = (Collection<RdfResource>) field.get(resource);
                        for (RdfResource nestedResource : collection) {
                            addGraph(nestedResource);
                        }
                    }
                    if (RdfResource.class.isAssignableFrom(field.getType())) {
                        addGraph((RdfResource) field.get(resource));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new DatasetAddException("Could not add object due to insufficient access", e);
            }
        }
    }

    public void remove(RdfResource resource) {
        ResourceState state = _resources.get(resource);
        if (state != null) {
            if (_primaryResourceType.isInstance(resource)) {
                _primaryResources.remove((PrimaryResourceType) resource);
            }
            if (state == ResourceState.ADDED) {
                _resources.remove(resource);
            } else {
                _resources.put(resource, ResourceState.REMOVED);
            }
        }
    }

    @Override
    public void removeGraph(RdfResource resource, Class... classes) {
    }
}
