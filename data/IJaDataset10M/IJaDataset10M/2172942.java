package com.apelon.apps.dts.treebrowser.tree.application.fetchers;

import com.apelon.apps.dts.treebrowser.tree.beans.AssociationTypeBean;
import com.apelon.apps.dts.treebrowser.tree.beans.ConfigureBean;
import com.apelon.apps.dts.treebrowser.tree.beans.ConfigureOptionsBean;
import com.apelon.apps.dts.treebrowser.tree.beans.RootBean;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.StackTracePrinter;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.association.AssociationType;
import com.apelon.dts.client.concept.*;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.dts.client.namespace.NamespaceType;
import com.apelon.dts.common.DataTypeFilter;
import com.apelon.dts.common.subset.Subset;
import com.apelon.dts.common.subset.SubsetDescriptor;
import java.util.*;

/**
 * Makes API calls to get the available tree configuration options such as namespace and association types.
 * <p>
 *
 * @author        All source code copyright (c) 2003 Apelon, Inc.  All rights reserved.
 *
 * @version       Apelon Tree Widget 1.0
 */
public class ConfigureOptionsFetcher {

    ConfigureFetcherImpl fetcher = null;

    public static HashMap namespaceLinkage = new HashMap();

    public ConfigureOptionsFetcher() {
    }

    public ConfigureOptionsFetcher(ConfigureFetcherImpl fetcher) {
        this.fetcher = fetcher;
    }

    public Vector fetchAllNamespaces() {
        Vector allNamespaceOptions = new Vector(0);
        ConceptChild[] subNodeConcepts = null;
        String message = "";
        try {
            if (!(fetcher.namespaceQuery == null)) {
                Namespace[] namespaces = fetcher.namespaceQuery.getNamespaces();
                if (!(namespaces == null)) {
                    int i = 0;
                    while (namespaces.length > i) {
                        if (namespaces[i].getNamespaceType() == NamespaceType.ONTYLOG) {
                            message = "Fetching ontylog namespaces. ";
                            ConceptAttributeSetDescriptor subNodesASD = new ConceptAttributeSetDescriptor("subNodesASD");
                            subNodeConcepts = fetcher.nq.getConceptChildRoots(subNodesASD, namespaces[i].getId());
                            if (subNodeConcepts.length > 0) {
                                allNamespaceOptions.add("<name>" + namespaces[i].getName() + "</name>" + "<type>Ontylog</type>");
                            }
                        }
                        if (namespaces[i].getNamespaceType() == NamespaceType.ONTYLOG_EXTENSION) {
                            message = "Fetching ontylog extension namespaces. ";
                            ConceptAttributeSetDescriptor subNodesEASD = new ConceptAttributeSetDescriptor("subNodesEASD");
                            subNodeConcepts = fetcher.nq.getConceptChildRoots(subNodesEASD, namespaces[i]);
                            int subsNS_id = namespaces[i].getLinkedNamespaceId();
                            Namespace subsNS = fetcher.namespaceQuery.findNamespaceById(subsNS_id);
                            String displayName = subsNS.getName() + "/" + namespaces[i].getName();
                            namespaceLinkage.put(displayName, namespaces[i].getName());
                            if (subNodeConcepts.length > 0) {
                                allNamespaceOptions.add("<name>" + displayName + "</name>" + "<type>OntylogExtension</type>");
                            }
                        }
                        OntylogConcept[] thesaurusRoots = null;
                        if (namespaces[i].getNamespaceType() == NamespaceType.THESAURUS) {
                            message = "Fetching thesaurus namespaces. ";
                            ConceptAttributeSetDescriptor rootASD = new ConceptAttributeSetDescriptor("rootASD");
                            thesaurusRoots = fetcher.nq.getConceptChildRoots(rootASD, namespaces[i].getId());
                            if (thesaurusRoots.length > 0) {
                                allNamespaceOptions.add("<name>" + namespaces[i].getName() + "</name>" + "<type>Thesaurus</type>");
                            }
                        }
                        i++;
                    }
                }
            }
        } catch (DTSException e) {
            Categories.dataServer().error(message + StackTracePrinter.getStackTrace(e));
        }
        allNamespaceOptions = orderByString(allNamespaceOptions);
        return allNamespaceOptions;
    }

    public Vector fetchSubsets(ConfigureBean configEntity) {
        Vector subsetOptions = new Vector(0);
        ConceptChild[] subNodeConcepts = null;
        try {
            SubsetDescriptor subsetDesc = new SubsetDescriptor();
            DataTypeFilter filter = new DataTypeFilter();
            filter.setFilterBy(filter.FILTER_BY_NAMESPACE);
            String namespaceType = configEntity.getTreeType();
            String namespaceName = null;
            if (namespaceType.equals("ontylog")) {
                namespaceName = configEntity.getOntylogNamespace();
            }
            if (namespaceType.equals("ontylogExtension")) {
                namespaceName = configEntity.getOntyExtensionNamespace();
            }
            if (namespaceType.equals("thesaurus")) {
                namespaceName = configEntity.getThesaurusNamespace();
            }
            Namespace namespace = fetcher.namespaceQuery.findNamespaceByName(namespaceName);
            filter.setNamespaceId(namespace.getId());
            Subset[] subsets = fetcher.subsetQuery.find(filter, subsetDesc);
            int i = 0;
            while (subsets.length > i) {
                subsetOptions.add(subsets[i]);
                i++;
            }
        } catch (DTSException e) {
            Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
        }
        return subsetOptions;
    }

    public Vector fetchAssocTreeSubsets(ConfigureBean configEntity, ConfigureOptionsBean optionsEntity) {
        Vector subsetOptions = new Vector(0);
        ConceptChild[] subNodeConcepts = null;
        String root_ns = configEntity.getRootNamespace();
        if ((root_ns != null) && (!root_ns.equals("")) && (!root_ns.equals("null"))) {
            try {
                SubsetDescriptor subsetDesc = new SubsetDescriptor();
                DataTypeFilter filter = new DataTypeFilter();
                filter.setFilterBy(DataTypeFilter.FILTER_BY_NAMESPACE);
                String namespaceName = null;
                namespaceName = configEntity.getRootNamespace();
                Namespace namespace = fetcher.namespaceQuery.findNamespaceByName(namespaceName);
                filter.setNamespaceId(namespace.getId());
                Subset[] subsets = fetcher.subsetQuery.find(filter, subsetDesc);
                int i = 0;
                while (subsets.length > i) {
                    subsetOptions.add(subsets[i]);
                    i++;
                }
            } catch (DTSException e) {
                Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
            }
        } else if (!(optionsEntity.getRootBeans() == null) && (optionsEntity.getRootBeans().size() > 0)) {
            try {
                SubsetDescriptor subsetDesc = new SubsetDescriptor();
                DataTypeFilter filter = new DataTypeFilter();
                filter.setFilterBy(DataTypeFilter.FILTER_BY_NAMESPACE);
                String namespaceName = null;
                namespaceName = ((RootBean) optionsEntity.getRootBeans().get(0)).getRootNamespaceName();
                Namespace namespace = fetcher.namespaceQuery.findNamespaceByName(namespaceName);
                filter.setNamespaceId(namespace.getId());
                Subset[] subsets = fetcher.subsetQuery.find(filter, subsetDesc);
                int i = 0;
                while (subsets.length > i) {
                    subsetOptions.add(subsets[i]);
                    i++;
                }
            } catch (DTSException e) {
                Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
            }
        }
        return subsetOptions;
    }

    public Vector fetchOntylogNamespaces() {
        Vector ontylogNamespaceOptions = new Vector(0);
        ConceptChild[] subNodeConcepts = null;
        try {
            Namespace[] namespaces = fetcher.namespaceQuery.getNamespaces();
            int i = 0;
            while (namespaces.length > i) {
                if (namespaces[i].getNamespaceType() == NamespaceType.ONTYLOG) {
                    ConceptAttributeSetDescriptor subNodesASD = new ConceptAttributeSetDescriptor("subNodesASD");
                    subNodeConcepts = fetcher.nq.getConceptChildRoots(subNodesASD, namespaces[i].getId());
                    if (subNodeConcepts.length > 0) {
                        ontylogNamespaceOptions.add(namespaces[i].getName());
                    }
                }
                i++;
            }
        } catch (DTSException e) {
            Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
        }
        return ontylogNamespaceOptions;
    }

    public Vector fetchThesaurusNamespaces() {
        Vector thesaurusNamespaceOptions = new Vector(0);
        ConceptChild[] subNodeConcepts = null;
        OntylogConcept[] thesaurusRoots = null;
        try {
            Namespace[] namespaces = fetcher.namespaceQuery.getNamespaces();
            int i = 0;
            while (namespaces.length > i) {
                if (namespaces[i].getNamespaceType() == NamespaceType.THESAURUS) {
                    ConceptAttributeSetDescriptor rootASD = new ConceptAttributeSetDescriptor("rootASD");
                    thesaurusRoots = fetcher.nq.getConceptChildRoots(rootASD, namespaces[i].getId());
                    if (thesaurusRoots.length > 0) {
                        thesaurusNamespaceOptions.add(namespaces[i].getName());
                    }
                }
                i++;
            }
        } catch (DTSException e) {
            Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
        }
        return thesaurusNamespaceOptions;
    }

    public Vector fetchAssociationTypeBeans() {
        AssociationType[] assocTypes;
        AssociationTypeBean assocBean = null;
        Vector namespaceObjects = new Vector(0);
        Vector assocTypeNames = new Vector(0);
        Vector assocBeans = new Vector(0);
        try {
            Namespace[] namespaces = fetcher.namespaceQuery.getNamespaces();
            int i = 0;
            while (namespaces.length > i) {
                namespaceObjects.add(namespaces[i]);
                i++;
            }
            namespaceObjects = orderByNamespace(namespaceObjects);
            i = 0;
            while (namespaceObjects.size() > i) {
                Namespace namespace = (Namespace) namespaceObjects.get(i);
                assocTypes = fetcher.associationQuery.getConceptAssociationTypes(((Namespace) namespaceObjects.get(i)).getId());
                if (assocTypes.length > 0) {
                    assocBean = new AssociationTypeBean();
                    assocBean.setAssocNamespaceName(namespace.getName());
                    if (namespace.getName().equals(fetcher.configureEntity.associationNamespace)) {
                        int x = 0;
                        while (assocTypes.length > x) {
                            assocTypeNames.add(assocTypes[x].getName());
                            x++;
                        }
                    }
                    if (fetcher.configureEntity.associationNamespace.equals("") || fetcher.configureEntity.associationNamespace.equals("null")) {
                        int x = 0;
                        while (assocTypes.length > x) {
                            assocTypeNames.add(assocTypes[x].getName());
                            x++;
                        }
                        fetcher.configureEntity.setAssociationNamespace("none_selected");
                    }
                    assocBean.setAssocTypeNames(assocTypeNames);
                }
                if (!(assocBean == null)) {
                    assocBeans.add(assocBean);
                }
                assocBean = null;
                assocTypeNames = new Vector(0);
                i++;
            }
        } catch (DTSException e) {
            Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
        }
        return assocBeans;
    }

    public Vector fetchRootBeans() {
        int namespaceId = 0;
        int rootNamespaceId = 0;
        DTSConcept[] concepts = null;
        DTSConcept[] rootConcepts = null;
        AssociationType[] assocTypes = null;
        AssociationType assocType = null;
        RootBean rootBean = null;
        String rootNamespaceName = null;
        Vector rootNames = new Vector(0);
        Vector rootBeans = new Vector(0);
        try {
            Namespace[] namespaces = fetcher.namespaceQuery.getNamespaces();
            String assoctype = fetcher.configureEntity.getAssociationType();
            if (!(assoctype.equals(""))) {
                String assocTypeName = fetcher.configureEntity.getAssociationType();
                Namespace namespace = fetcher.namespaceQuery.findNamespaceByName(fetcher.configureEntity.getAssociationNamespace());
                if (!(namespace == null)) {
                    namespaceId = namespace.getId();
                    assocType = fetcher.associationQuery.findAssociationTypeByName(assocTypeName, namespaceId);
                    Namespace rootNamespace = fetcher.namespaceQuery.findNamespaceByName(fetcher.configureEntity.getRootNamespace());
                    rootNamespaceId = rootNamespace.getId();
                } else {
                    assoctype = "";
                }
            }
            if (assoctype.equals("") || assocType == null) {
                int i = 0;
                while (namespaces.length > i) {
                    namespaceId = namespaces[i].getId();
                    rootNamespaceId = namespaces[i].getId();
                    assocTypes = fetcher.associationQuery.getConceptAssociationTypes(namespaceId);
                    if (assocTypes.length > 0) {
                        break;
                    }
                    i++;
                }
                if (!(assocTypes == null) && assocTypes.length > 0) {
                    assocType = assocTypes[0];
                }
            }
            if (!(assocType == null)) {
                int i = 0;
                while (namespaces.length > i) {
                    if (namespaces[i].getId() == rootNamespaceId) {
                        ConceptAttributeSetDescriptor asd = new ConceptAttributeSetDescriptor("asd");
                        asd.addConceptAssociationType(assocType);
                        DTSSearchOptions options = new DTSSearchOptions(500, rootNamespaceId, asd);
                        if (!(fetcher.configureEntity.getSearchTerm().equals(""))) {
                            concepts = fetcher.searchQuery.findConceptsWithNameMatching(fetcher.configureEntity.getSearchTerm(), options);
                        }
                    }
                    rootBean = new RootBean();
                    rootBean.setRootNamespaceName(namespaces[i].getName());
                    if (!(concepts == null)) {
                        if (concepts.length > 0) {
                            int x = 0;
                            while (concepts.length > x) {
                                if (concepts[x].getFetchedConceptAssociations().length > 0) {
                                    rootNames.add(concepts[x].getName());
                                }
                                x++;
                            }
                            rootBean.setRootNames(rootNames);
                        }
                    }
                    if (!(rootBean == null)) {
                        rootBeans.add(rootBean);
                    }
                    rootBean = null;
                    rootNames = new Vector(0);
                    concepts = null;
                    i++;
                }
            }
        } catch (DTSException e) {
            Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
        }
        rootBeans = orderByRootBean(rootBeans);
        return rootBeans;
    }

    private Vector orderByNamespace(Vector attributes) {
        TreeMap orderer = new TreeMap();
        int i = 0;
        while (attributes.size() > i) {
            Namespace namespace = (Namespace) attributes.get(i);
            String key = namespace.getName();
            orderer.put(key, attributes.get(i));
            i++;
        }
        Vector tempResultConcepts = new Vector(0);
        Collection ordererCol = orderer.values();
        Iterator ordererIterator = ordererCol.iterator();
        while (ordererIterator.hasNext()) {
            tempResultConcepts.add((Namespace) ordererIterator.next());
        }
        attributes = tempResultConcepts;
        return attributes;
    }

    private Vector orderByRootBean(Vector attributes) {
        TreeMap orderer = new TreeMap();
        int i = 0;
        while (attributes.size() > i) {
            RootBean rBean = (RootBean) attributes.get(i);
            String key = rBean.getRootNamespaceName();
            orderer.put(key, attributes.get(i));
            i++;
        }
        Vector tempResultConcepts = new Vector(0);
        Collection ordererCol = orderer.values();
        Iterator ordererIterator = ordererCol.iterator();
        while (ordererIterator.hasNext()) {
            tempResultConcepts.add((RootBean) ordererIterator.next());
        }
        attributes = tempResultConcepts;
        return attributes;
    }

    private Vector orderByString(Vector strings) {
        TreeMap orderer = new TreeMap();
        int i = 0;
        while (strings.size() > i) {
            orderer.put(strings.get(i), strings.get(i));
            i++;
        }
        Vector tempResultConcepts = new Vector(0);
        Collection ordererCol = orderer.values();
        Iterator ordererIterator = ordererCol.iterator();
        while (ordererIterator.hasNext()) {
            tempResultConcepts.add((String) ordererIterator.next());
        }
        strings = tempResultConcepts;
        return strings;
    }
}
