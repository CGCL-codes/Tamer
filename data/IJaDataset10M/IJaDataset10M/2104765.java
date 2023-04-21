package org.openliberty.arisidbeans;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openliberty.arisid.AttrSvcInitializedException;
import org.openliberty.arisid.AttributeFilter;
import org.openliberty.arisid.ArisIdService;
import org.openliberty.arisid.ArisIdServiceFactory;
import org.openliberty.arisid.CarmlDoc;
import org.openliberty.arisid.Filter;
import org.openliberty.arisid.IGFException;
import org.openliberty.arisid.IInteraction;
import org.openliberty.arisid.ISearchInteraction;
import org.openliberty.arisid.PredicateFilter;
import org.openliberty.arisid.RoleFilter;
import org.openliberty.arisid.URIConst;
import org.openliberty.arisid.schema.AttributeDef;
import org.openliberty.arisid.schema.AttributeRef;
import org.openliberty.arisid.schema.PredicateDef;
import org.openliberty.arisid.schema.PredicateRef;
import org.openliberty.arisid.schema.RoleDef;
import org.openliberty.arisid.stack.AuthenticationException;
import org.openliberty.arisid.stack.NoSuchContextException;
import org.openliberty.arisid.stack.NoSuchSubjectException;
import org.openliberty.arisid.stack.SubjectNotUniqueException;

/**
 * Class for parsing the CARML file and creating InteractionDescriptor list and
 * AttributeDescriptor list for all interactions and attributes in the CARML
 * file
 *
 * InteractionDescriptor and AttributeDescriptor lists are used by Bean
 * Generator for generating the bean classes
 */
public class CarmlParser {

    private String appName = "";

    private String carmlURI = "";

    private List<String> entityNames = new ArrayList<String>();

    private Map<String, List<InteractionDescriptor>> interactions = new HashMap<String, List<InteractionDescriptor>>();

    private Map<String, List<AttributeDescriptor>> attributes = new HashMap<String, List<AttributeDescriptor>>();

    private Map<String, String> defaultFindInteraction = new HashMap<String, String>();

    private Map<String, String> defaultReadInteraction = new HashMap<String, String>();

    private Map<String, String> normalizedEntityMap = new HashMap<String, String>();

    private boolean useDefaultEntity = false;

    private List<AttributeDescriptor> entityAttributes;

    private Set<String> getAttributesList;

    private Set<String> getPredicatesList;

    private Set<String> getRolesList;

    private Set<String> setAttributesList;

    private Set<String> addAttributesList;

    private Set<String> deleteAttributesList;

    private Set<String> filterAttributesList;

    private Set<String> filterPredicatesList;

    private Set<String> filterRolesList;

    private ArisIdService asvc = null;

    private CarmlDoc carmldoc = null;

    private final String defaultmethod_prefix = "default_";

    public CarmlParser(String carmlFile, String defaultEntityName) throws FileNotFoundException, AttrSvcInitializedException, AuthenticationException, NoSuchContextException, NoSuchSubjectException, SubjectNotUniqueException, InstantiationException, IGFException, IllegalAccessException, URISyntaxException {
        try {
            System.setProperty(ArisIdConstants.ATTRIBUTE_SERVICE_PROVIDER, "");
            asvc = ArisIdServiceFactory.parseCarmlOnly(new URI(carmlFile));
            carmldoc = asvc.getCarmlDoc();
            this.appName = carmldoc.getApplicationNameId();
            this.carmlURI = carmldoc.getCarmlURI().toString();
            Collection entities = carmldoc.getEntityNames();
            if (entities.size() == 0) {
                useDefaultEntity = true;
                parseInteractions(defaultEntityName, true);
            } else {
                Iterator<String> eIter = entities.iterator();
                while (eIter.hasNext()) {
                    parseInteractions(eIter.next(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseInteractions(String entityName, boolean defaultEntity) {
        Collection<IInteraction> carmlInteractions = null;
        List<InteractionDescriptor> entityInteractions = new ArrayList<InteractionDescriptor>();
        entityAttributes = new ArrayList<AttributeDescriptor>();
        String entityDefaultFindInteraction = "";
        String entityDefaultReadInteraction = "";
        getAttributesList = new HashSet<String>();
        getPredicatesList = new HashSet<String>();
        getRolesList = new HashSet<String>();
        setAttributesList = new HashSet<String>();
        addAttributesList = new HashSet<String>();
        deleteAttributesList = new HashSet<String>();
        filterAttributesList = new HashSet<String>();
        filterPredicatesList = new HashSet<String>();
        filterRolesList = new HashSet<String>();
        int numReadAttributes = 0;
        String nEntityName = getNormalizedEntityName(entityName);
        entityNames.add(nEntityName);
        normalizedEntityMap.put(nEntityName, entityName);
        try {
            if (defaultEntity == true) carmlInteractions = carmldoc.getInteractions(); else carmlInteractions = carmldoc.getInteractionsByEntity(entityName);
            Iterator<IInteraction> iter = carmlInteractions.iterator();
            String readInteractionWithMaxAttrs = "";
            while (iter.hasNext()) {
                IInteraction carmlInteraction = iter.next();
                InteractionDescriptor interaction = new InteractionDescriptor(carmlInteraction.getNameId(), carmlInteraction.getDescription());
                interaction.setMethodName(getBeanMethodName(carmlInteraction.getNameId()));
                interaction.setAttrRefCount(carmlInteraction.getAttributeRefs().size());
                interaction.setPredRefCount(carmlInteraction.getPredicateRefs().size());
                interaction.setRoleRefCount(carmlInteraction.getRoleIds().size());
                interaction.setAdd(carmlInteraction.isAdd());
                interaction.setModify(carmlInteraction.isModify());
                interaction.setDelete(carmlInteraction.isDelete());
                interaction.setFind(carmlInteraction.isFind());
                interaction.setSearch(carmlInteraction.isSearch());
                interaction.setCompare(carmlInteraction.isCompare());
                interaction.setRead(carmlInteraction.isRead());
                interaction.setEntityName(nEntityName);
                if (interaction.isSearch()) {
                    interaction.setPageSize(((ISearchInteraction) carmlInteraction).getPageSize());
                }
                int numPrimaryKeyFilters = 0;
                int numMandatoryFilters = 0;
                String primaryKeyFilterName = "";
                String secondFilterName = "";
                Filter filter = carmlInteraction.getFilter();
                List<AttributeFilter> attFilters = null;
                if (filter != null && (attFilters = filter.getAttributeFilters()) != null) {
                    Iterator<AttributeFilter> fIter = attFilters.iterator();
                    while (fIter.hasNext()) {
                        AttributeFilter attFilter = fIter.next();
                        if (attFilter.isPrimaryKey()) {
                            numPrimaryKeyFilters++;
                            numMandatoryFilters++;
                            primaryKeyFilterName = attFilter.getNameRef();
                        } else if (attFilter.isOptional() == false) {
                            numMandatoryFilters++;
                            secondFilterName = attFilter.getNameRef();
                        }
                    }
                }
                interaction.setNumPrimaryKeyFilters(numPrimaryKeyFilters);
                interaction.setNumMandatoryFilters(numMandatoryFilters);
                interaction.setPrimaryKeyFilterName(primaryKeyFilterName);
                interaction.setSecondFilterName(secondFilterName);
                if (carmlInteraction.isFind()) {
                    if ((!interaction.getName().equals(interaction.getMethodName()) || entityDefaultFindInteraction == "") && numPrimaryKeyFilters == 1 && numMandatoryFilters == 1) entityDefaultFindInteraction = interaction.getMethodName();
                }
                if (carmlInteraction.isRead()) {
                    int nAttrs = 0;
                    if (carmlInteraction.getAttributeRefs() != null) nAttrs = carmlInteraction.getAttributeRefs().size();
                    if (nAttrs > numReadAttributes) {
                        numReadAttributes = nAttrs;
                        readInteractionWithMaxAttrs = carmlInteraction.getNameId();
                    }
                    if (!interaction.getName().equals(interaction.getMethodName())) entityDefaultReadInteraction = interaction.getMethodName();
                }
                entityInteractions.add(interaction);
                addAttributes(carmlInteraction);
            }
            if (entityDefaultReadInteraction.equals("")) entityDefaultReadInteraction = readInteractionWithMaxAttrs;
            interactions.put(nEntityName, entityInteractions);
            defaultReadInteraction.put(nEntityName, entityDefaultReadInteraction);
            defaultFindInteraction.put(nEntityName, entityDefaultFindInteraction);
            Collections.sort(entityAttributes);
            attributes.put(nEntityName, entityAttributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getApplicationName() {
        return appName;
    }

    public String getCarmlURI() {
        return carmlURI;
    }

    public boolean useDefaultEntityName() {
        return useDefaultEntity;
    }

    public List<InteractionDescriptor> getCarmlInteractions(String entity) {
        return interactions.get(entity);
    }

    public List<AttributeDescriptor> getCarmlAttributes(String entity) {
        return attributes.get(entity);
    }

    public Map<String, List<AttributeDescriptor>> getCarmlAttributes() {
        return attributes;
    }

    private void addAttributes(IInteraction interaction) {
        int numMandatoryAttrs = 0;
        String mandatoryAttr = "";
        if (interaction.isModify() == true) {
            Collection<AttributeRef> attrs = interaction.getAttributeRefs();
            Iterator<AttributeRef> aIter = attrs.iterator();
            while (aIter.hasNext()) {
                AttributeRef attr = aIter.next();
                if (attr.isOptional() == false) {
                    numMandatoryAttrs++;
                    mandatoryAttr = attr.getNameRef();
                }
            }
        }
        if (interaction.isRead() || interaction.isFind() || interaction.isSearch()) {
            Collection<AttributeRef> attrs = interaction.getAttributeRefs();
            Iterator<AttributeRef> aIter = attrs.iterator();
            while (aIter.hasNext()) {
                AttributeDef attrDef = carmldoc.getSchemaManager().getAttribute(aIter.next().getNameRef());
                if (getAttributesList.contains(attrDef.getNameId()) == false) {
                    AttributeDescriptor attr = new AttributeDescriptor(attrDef.getNameId(), attrDef.getDescription());
                    String dataType = attrDef.getDataType().toString();
                    if (dataType.equalsIgnoreCase(URIConst.XML_HexBinary) || dataType.equalsIgnoreCase(URIConst.XML_Base64Binary)) {
                        attr.setBinary(true);
                    }
                    attr.setGetMethodName("get" + attr.getName().substring(0, 1).toUpperCase() + attr.getName().substring(1));
                    getAttributesList.add(attrDef.getNameId());
                    entityAttributes.add(attr);
                }
            }
            Collection<PredicateRef> predicates = interaction.getPredicateRefs();
            Iterator<PredicateRef> pIter = predicates.iterator();
            while (pIter.hasNext()) {
                PredicateDef predDef = carmldoc.getSchemaManager().getPredicate(pIter.next().getNameRef());
                if (getPredicatesList.contains(predDef.getNameId()) == false) {
                    AttributeDescriptor attr = new AttributeDescriptor(predDef.getNameId(), predDef.getDescription());
                    attr.setPredicate(true);
                    attr.setGetMethodName(attr.getName());
                    getPredicatesList.add(predDef.getNameId());
                    entityAttributes.add(attr);
                }
            }
            Set<String> roles = interaction.getRoleIds();
            Iterator<String> rIter = roles.iterator();
            while (rIter.hasNext()) {
                RoleDef roleDef = carmldoc.getSchemaManager().getRole(rIter.next());
                if (getRolesList.contains(roleDef.getNameId()) == false) {
                    AttributeDescriptor attr = new AttributeDescriptor(roleDef.getNameId(), roleDef.getDescription());
                    attr.setRole(true);
                    attr.setGetMethodName("get" + attr.getName().substring(0, 1).toUpperCase() + attr.getName().substring(1));
                    getRolesList.add(roleDef.getNameId());
                    entityAttributes.add(attr);
                }
            }
        } else if (interaction.isModify() == true && (numMandatoryAttrs == 0 || numMandatoryAttrs == 1)) {
            Collection<AttributeRef> attrs = interaction.getAttributeRefs();
            Iterator<AttributeRef> aIter = attrs.iterator();
            while (aIter.hasNext()) {
                AttributeRef attrRef = aIter.next();
                AttributeDef attrDef = carmldoc.getSchemaManager().getAttribute(attrRef.getNameRef());
                if (numMandatoryAttrs == 1 && attrDef.getNameId() != mandatoryAttr) continue;
                String dataType = attrDef.getDataType().toString();
                boolean isBinary = false;
                if (dataType.equalsIgnoreCase(URIConst.XML_HexBinary) || dataType.equalsIgnoreCase(URIConst.XML_Base64Binary)) {
                    isBinary = true;
                }
                if (setAttributesList.contains(attrDef.getNameId()) == false) {
                    AttributeDescriptor attr = new AttributeDescriptor(attrDef.getNameId(), attrDef.getDescription());
                    attr.setModifyInteraction(interaction.getNameId());
                    if (isBinary == true) attr.setBinary(true);
                    attr.setSetMethodName("set" + attr.getName().substring(0, 1).toUpperCase() + attr.getName().substring(1));
                    setAttributesList.add(attrDef.getNameId());
                    entityAttributes.add(attr);
                }
                if (attrDef.getCardinality().equals(AttributeDef.CARD_MULTI) && addAttributesList.contains(attrDef.getNameId()) == false) {
                    AttributeDescriptor attr = new AttributeDescriptor(attrDef.getNameId(), attrDef.getDescription());
                    attr.setModifyInteraction(interaction.getNameId());
                    if (isBinary == true) attr.setBinary(true);
                    attr.setSetMethodName("add" + attr.getName().substring(0, 1).toUpperCase() + attr.getName().substring(1));
                    addAttributesList.add(attrDef.getNameId());
                    entityAttributes.add(attr);
                }
                if (attrDef.getCardinality().equals(AttributeDef.CARD_MULTI) && deleteAttributesList.contains(attrDef.getNameId()) == false) {
                    AttributeDescriptor attr = new AttributeDescriptor(attrDef.getNameId(), attrDef.getDescription());
                    attr.setModifyInteraction(interaction.getNameId());
                    if (isBinary == true) attr.setBinary(true);
                    attr.setSetMethodName("delete" + attr.getName().substring(0, 1).toUpperCase() + attr.getName().substring(1));
                    deleteAttributesList.add(attrDef.getNameId());
                    entityAttributes.add(attr);
                }
            }
        }
        if (!interaction.isDelete()) {
            Filter filter = interaction.getFilter();
            if (filter != null) {
                List<AttributeFilter> attFilters = filter.getAttributeFilters();
                Iterator<AttributeFilter> afIter = attFilters.iterator();
                while (afIter.hasNext()) {
                    String attrName = afIter.next().getNameRef();
                    if (!getAttributesList.contains(attrName) && !setAttributesList.contains(attrName) && !addAttributesList.contains(attrName) && !deleteAttributesList.contains(attrName) && !filterAttributesList.contains(attrName)) {
                        AttributeDef attrDef = carmldoc.getSchemaManager().getAttribute(attrName);
                        AttributeDescriptor attr = new AttributeDescriptor(attrDef.getNameId(), attrDef.getDescription());
                        String dataType = attrDef.getDataType().toString();
                        if (dataType.equalsIgnoreCase(URIConst.XML_HexBinary) || dataType.equalsIgnoreCase(URIConst.XML_Base64Binary)) {
                            attr.setBinary(true);
                        } else {
                            attr.setBinary(false);
                        }
                        filterAttributesList.add(attrDef.getNameId());
                        entityAttributes.add(attr);
                    }
                }
                List<PredicateFilter> predFilters = filter.getPredicateFilters();
                Iterator<PredicateFilter> pfIter = predFilters.iterator();
                while (pfIter.hasNext()) {
                    String predName = pfIter.next().getNameRef();
                    if (!getPredicatesList.contains(predName) && !filterPredicatesList.contains(predName)) {
                        PredicateDef predDef = carmldoc.getSchemaManager().getPredicate(predName);
                        AttributeDescriptor attr = new AttributeDescriptor(predDef.getNameId(), predDef.getDescription());
                        attr.setPredicate(true);
                        filterPredicatesList.add(predDef.getNameId());
                        entityAttributes.add(attr);
                    }
                }
                List<RoleFilter> roleFilters = filter.getRoleFilters();
                Iterator<RoleFilter> rfIter = roleFilters.iterator();
                while (rfIter.hasNext()) {
                    String roleName = rfIter.next().getNameRef();
                    if (!getRolesList.contains(roleName) && !filterRolesList.contains(roleName)) {
                        RoleDef roleDef = carmldoc.getSchemaManager().getRole(roleName);
                        AttributeDescriptor attr = new AttributeDescriptor(roleDef.getNameId(), roleDef.getDescription());
                        attr.setRole(true);
                        filterRolesList.add(roleDef.getNameId());
                        entityAttributes.add(attr);
                    }
                }
            }
        }
    }

    public List<String> getEntityNames() {
        return entityNames;
    }

    public String getDefaultReadInteraction(String entity) {
        return defaultReadInteraction.get(entity);
    }

    public String getDefaultFindInteraction(String entity) {
        return defaultFindInteraction.get(entity);
    }

    public String getReadInteraction(String normEntityName, String fetchAttrName) {
        String readInteractionName = "";
        Collection<IInteraction> carmlInteractions = null;
        if (useDefaultEntity == true) carmlInteractions = carmldoc.getInteractions(); else carmlInteractions = carmldoc.getInteractionsByEntity(normalizedEntityMap.get(normEntityName));
        if (carmlInteractions != null) {
            Iterator<IInteraction> iter = carmlInteractions.iterator();
            while (iter.hasNext()) {
                IInteraction carmlInteraction = iter.next();
                if (carmlInteraction.isRead() != true) continue;
                if (fetchAttrName.equalsIgnoreCase("subjectname") || isValidFetchAttribute(carmlInteraction, fetchAttrName)) return getBeanMethodName(carmlInteraction.getNameId());
            }
        }
        return readInteractionName;
    }

    public String getFindInteraction(String normEntityName, String fetchAttrName, String attrFilterName) {
        String findInteractionName = "";
        Collection<IInteraction> carmlInteractions = null;
        if (useDefaultEntity == true) carmlInteractions = carmldoc.getInteractions(); else carmlInteractions = carmldoc.getInteractionsByEntity(normalizedEntityMap.get(normEntityName));
        if (carmlInteractions != null) {
            Iterator<IInteraction> iter = carmlInteractions.iterator();
            while (iter.hasNext()) {
                IInteraction carmlInteraction = iter.next();
                if (carmlInteraction.isFind() != true) continue;
                if (isValidFilter(carmlInteraction, attrFilterName) && (fetchAttrName.equalsIgnoreCase("subjectname") || isValidFetchAttribute(carmlInteraction, fetchAttrName))) return getBeanMethodName(carmlInteraction.getNameId());
            }
        }
        return findInteractionName;
    }

    public String getSearchInteraction(String normEntityName, String fetchAttrName, String attrFilterName) {
        String searchInteractionName = "";
        Collection<IInteraction> carmlInteractions = null;
        if (useDefaultEntity == true) carmlInteractions = carmldoc.getInteractions(); else carmlInteractions = carmldoc.getInteractionsByEntity(normalizedEntityMap.get(normEntityName));
        if (carmlInteractions != null) {
            Iterator<IInteraction> iter = carmlInteractions.iterator();
            while (iter.hasNext()) {
                IInteraction carmlInteraction = iter.next();
                if (carmlInteraction.isSearch() != true || ((ISearchInteraction) carmlInteraction).getPageSize() > 0) continue;
                if (isValidFilter(carmlInteraction, attrFilterName) && (fetchAttrName.equalsIgnoreCase("subjectname") || isValidFetchAttribute(carmlInteraction, fetchAttrName))) return getBeanMethodName(carmlInteraction.getNameId());
            }
        }
        return searchInteractionName;
    }

    private boolean isValidFilter(IInteraction interaction, String attrFilterName) {
        Filter filter = interaction.getFilter();
        List<AttributeFilter> attFilters = null;
        int numMandatoryFilters = 0;
        String mandatoryFilterName = "";
        boolean filterExists = false;
        if (filter != null && (attFilters = filter.getAttributeFilters()) != null) {
            Iterator<AttributeFilter> fIter = attFilters.iterator();
            while (fIter.hasNext()) {
                AttributeFilter attrFilter = fIter.next();
                if (attrFilter.isPrimaryKey() || attrFilter.isOptional() == false) {
                    numMandatoryFilters++;
                    mandatoryFilterName = attrFilter.getNameRef();
                }
                if (attrFilter.getNameRef().equals(attrFilterName)) {
                    filterExists = true;
                }
            }
        }
        if (filterExists && (numMandatoryFilters == 0 || (numMandatoryFilters == 1 && mandatoryFilterName.equals(attrFilterName)))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidFetchAttribute(IInteraction interaction, String attrName) {
        Collection<AttributeRef> attrRefs = interaction.getAttributeRefs();
        for (Iterator<AttributeRef> iter = attrRefs.iterator(); iter.hasNext(); ) {
            AttributeRef attrRef = iter.next();
            if (attrRef.getNameRef().equals(attrName)) return true;
        }
        return false;
    }

    private String getBeanMethodName(String carmlInteractionName) {
        if (carmlInteractionName.toLowerCase().startsWith(defaultmethod_prefix)) return carmlInteractionName.substring(defaultmethod_prefix.length()); else return carmlInteractionName;
    }

    private String getNormalizedEntityName(String entityName) {
        String normalizedName = "";
        if (entityName != null && entityName.length() > 0) {
            normalizedName = entityName.toLowerCase();
            normalizedName = normalizedName.substring(0, 1).toUpperCase() + normalizedName.substring(1);
        }
        return normalizedName;
    }
}
