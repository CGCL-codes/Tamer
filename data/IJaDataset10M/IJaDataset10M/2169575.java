package org.openliberty.arisid.provider.opends.mapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;
import org.opends.server.api.AttributeSyntax;
import org.opends.server.core.DirectoryServer;
import org.opends.server.schema.SchemaConstants;
import org.opends.server.types.AttributeType;
import org.opends.server.types.DirectoryException;
import org.openliberty.arisid.ArisIdService;
import org.openliberty.arisid.AttributeFilter;
import org.openliberty.arisid.DynamicFilter;
import org.openliberty.arisid.Filter;
import org.openliberty.arisid.IAttributeValue;
import org.openliberty.arisid.IInteraction;
import org.openliberty.arisid.URIConst;
import org.openliberty.arisid.log.ILogger;
import org.openliberty.arisid.log.LogHandler;
import org.openliberty.arisid.provider.ldapMapper.mapper.IAttributeMapper;
import org.openliberty.arisid.provider.ldapMapper.mapper.IMapper;
import org.openliberty.arisid.provider.ldapMapper.mapper.IPredicateMapper;
import org.openliberty.arisid.provider.ldapMapper.mapper.IRoleMapper;
import org.openliberty.arisid.provider.ldapMapper.mapper.Mapper;
import org.openliberty.arisid.provider.ldapMapper.types.IEntry;
import org.openliberty.arisid.provider.ldapMapper.types.ILdapFilter;
import org.openliberty.arisid.provider.opends.DsManager;
import org.openliberty.arisid.provider.opends.DsProvider;
import org.openliberty.arisid.provider.opends.types.DsEntry;
import org.openliberty.arisid.provider.opends.types.DsFilter;
import org.openliberty.arisid.schema.AttributeDef;
import org.openliberty.arisid.schema.PredicateDef;
import org.openliberty.arisid.stack.IAttrSvcStack;
import org.openliberty.arisid.stack.MappingException;

public class DsMapper extends Mapper {

    private static ILogger logger = LogHandler.getLogger(DsMapper.class);

    private ArisIdService asvc = null;

    public void initialize(ArisIdService svc, IAttrSvcStack provider, Properties prop) throws MappingException {
        if (!(provider instanceof DsProvider)) {
            logger.error("Attempted to start mapper. Expacting provider class: " + DsProvider.class.toString());
            throw new RuntimeException("OpenDsMapper only supports the DsProvider stack implementation");
        }
        this._prop = prop;
        this.asvc = svc;
        processSearchRoot();
        processSchema();
        Iterator<IInteraction> iter = svc.getCarmlDoc().getInteractions().iterator();
        while (iter.hasNext()) {
            IInteraction ixn = iter.next();
            Filter ifilter = ixn.getFilter();
            if (ifilter != null) {
                if (checkPrimaryPresent(ifilter)) _primaryFilterIxns.add(ixn);
            }
            String propname = IGF_INTERACTION_CLASS + DsProvider.cleanName(ixn.getNameId());
            String oc = prop.getProperty(propname);
            if (oc == null) {
                oc = "inetorgperson";
                prop.setProperty(propname, oc);
                if (logger.isDebugEnabled()) logger.debug("Defaulting " + propname + " to " + oc);
            } else if (logger.isDebugEnabled()) logger.debug("Setting " + propname + " to " + oc);
            _ocmap.put(ixn, oc);
        }
    }

    private boolean checkPrimaryPresent(Filter filter) {
        Iterator<AttributeFilter> aiter = filter.getAttributeFilters().iterator();
        while (aiter.hasNext()) {
            AttributeFilter afilter = aiter.next();
            if (afilter.isPrimaryKey()) return true;
        }
        Iterator<Filter> fiter = filter.getSubFilters().iterator();
        while (fiter.hasNext()) if (checkPrimaryPresent(fiter.next())) return true;
        return false;
    }

    private String genConfigProperty(String type, AttributeType dsAttr, String otherCfg) {
        StringBuffer buf = new StringBuffer();
        buf.append(dsAttr.getPrimaryName()).append('|');
        if (dsAttr.isBinary()) buf.append(IAttributeMapper.BASE_TYPE_BINARY).append('|'); else buf.append(IAttributeMapper.BASE_TYPE_STRING).append('|');
        if (otherCfg != null) buf.append(otherCfg).append('|');
        if (type.equals(TYPE_ATTR)) buf.append(SimpleDsAttributeMapper.class.getName()); else if (type.equals(TYPE_PRED)) buf.append(SimpleDsPredicateMapper.class.getName()); else buf.append(SimpleDsRoleMapper.class.getName());
        return buf.toString();
    }

    @SuppressWarnings("unchecked")
    protected AttributeSyntax genAttributeSyntax(AttributeDef attr) {
        URI type = attr.getDataType();
        if (type.equals(URIConst.XML_String)) {
            return DirectoryServer.getAttributeSyntax(SchemaConstants.SYNTAX_DIRECTORY_STRING_OID, true);
        } else if (type.equals(URIConst.XML_Boolean)) {
            return DirectoryServer.getAttributeSyntax(SchemaConstants.SYNTAX_BOOLEAN_OID, true);
        } else if (type.equals(URIConst.XML_Date) || type.equals(URIConst.XML_DateTime)) {
            return DirectoryServer.getAttributeSyntax(SchemaConstants.SYNTAX_UTC_TIME_OID, true);
        } else if (type.equals(URIConst.XML_Base64Binary)) {
            return DirectoryServer.getAttributeSyntax(SchemaConstants.SYNTAX_BINARY_OID, true);
        } else if (type.equals(URIConst.XML_Integer)) {
            return DirectoryServer.getAttributeSyntax(SchemaConstants.SYNTAX_INTEGER_OID, true);
        } else if (type.equals(URIConst.XACML_X500Name)) {
            return DirectoryServer.getAttributeSyntax(SchemaConstants.SYNTAX_DN_OID, true);
        }
        logger.debug("Defaulting mapping of " + attr.getNameId() + " to syntax directoryString.");
        return DirectoryServer.getAttributeSyntax(SchemaConstants.SYNTAX_DIRECTORY_STRING_OID, true);
    }

    @SuppressWarnings("unchecked")
    protected AttributeType genAttributeType(AttributeDef attr) {
        AttributeSyntax syntax = genAttributeSyntax(attr);
        AttributeType res = new AttributeType(attr.getDescription(), attr.getNameId(), null, "1.3.6.1.4.1.31052.2.1." + attr.getNameId(), attr.getDescription(), null, syntax, null, false, false, false, (attr.getCardinality() == AttributeDef.CARD_MULTI ? false : true));
        return res;
    }

    private void processSchema() throws MappingException {
        Collection<AttributeDef> carmlAttrs = this.asvc.getSchemaManager().getAttributes();
        Iterator<AttributeDef> aiter = carmlAttrs.iterator();
        while (aiter.hasNext()) {
            AttributeDef attr = aiter.next();
            String propname = IMapper.IGF_ATTRIBUTE + DsProvider.cleanName(attr.getNameId());
            String attrprop = this._prop.getProperty(propname);
            if (attrprop == null) {
                AttributeType dsAttr = DirectoryServer.getAttributeType(attr.getNameId().toLowerCase());
                if (dsAttr == null) {
                    String pwdAttrs = this._prop.getProperty(DsProvider.CARML_APP_PASSWORD_ATTRIBUTES, "pwd,userpassword,password");
                    StringTokenizer tkn = new StringTokenizer(pwdAttrs, ",");
                    HashSet<String> pwdSet = new HashSet<String>();
                    while (tkn.hasMoreTokens()) {
                        String pwdAttr = tkn.nextToken();
                        pwdSet.add(pwdAttr.toLowerCase());
                    }
                    if (pwdSet.contains(attr.getNameId().toLowerCase())) {
                        dsAttr = DirectoryServer.getAttributeType("userpassword");
                    } else {
                        dsAttr = genAttributeType(attr);
                        try {
                            DirectoryServer.registerAttributeType(genAttributeType(attr), false);
                        } catch (DirectoryException e) {
                            logger.error("Unable to register new directory attribute (" + attr.getNameId() + "): " + e.getMessage(), e);
                            throw new MappingException("Unable to register new schema(" + attr.getNameId() + "): " + e.getMessage(), e);
                        }
                    }
                }
                attrprop = genConfigProperty(TYPE_ATTR, dsAttr, null);
                this._prop.setProperty(propname, attrprop);
            }
            initializeAttribute(attrprop, attr);
        }
        Collection<PredicateDef> carmlPreds = this.asvc.getSchemaManager().getPredicates();
        Iterator<PredicateDef> piter = carmlPreds.iterator();
        while (piter.hasNext()) {
            PredicateDef attr = piter.next();
            String propname = IMapper.IGF_PREDICATE + DsProvider.cleanName(attr.getNameId());
            String attrprop = this._prop.getProperty(propname);
            if (attrprop == null) {
                AttributeType dsAttr = DirectoryServer.getAttributeType(attr.getNameId().toLowerCase());
                attrprop = genConfigProperty(TYPE_PRED, dsAttr, "TRUE:FALSE");
                this._prop.setProperty(propname, attrprop);
            }
            initializePredicate(attrprop, attr);
        }
        String roleAttribute = this._prop.getProperty(IMapper.IGF_ROLE_ATTR);
        if (roleAttribute == null) {
            roleAttribute = "description";
            this._prop.setProperty(IMapper.IGF_ROLE_ATTR, roleAttribute);
        }
        Iterator<IInteraction> iter = this.asvc.getCarmlDoc().getInteractions().iterator();
        while (iter.hasNext()) {
            IInteraction ixn = iter.next();
            if (ixn.getRoleIds().size() > 0) {
                String propname = IMapper.IGF_ROLE + DsProvider.cleanName(ixn.getNameId());
                String roleprop = this._prop.getProperty(propname);
                if (roleprop == null) {
                    AttributeType ovdAttr = DirectoryServer.getAttributeType(roleAttribute.toLowerCase());
                    roleprop = genConfigProperty(TYPE_ROLE, ovdAttr, "");
                    this._prop.setProperty(propname, roleprop);
                }
                initializeRole(roleprop, ixn);
            }
        }
    }

    private void initializeAttribute(String attrProp, AttributeDef attr) throws MappingException {
        String mapClass = null;
        try {
            mapClass = attrProp.substring(attrProp.lastIndexOf("|") + 1);
        } catch (IndexOutOfBoundsException e) {
        }
        if (mapClass == null || mapClass.equals("")) mapClass = SimpleDsAttributeMapper.class.toString();
        try {
            IAttributeMapper mapper = null;
            Object obmapper = Class.forName(mapClass).newInstance();
            if (obmapper instanceof IAttributeMapper) {
                mapper = (IAttributeMapper) obmapper;
            } else {
                logger.warn("Incorrect class for Attribute DsMapper: " + mapClass + ". Defaulting to SimpleDsAttributeMapper.");
                obmapper = Class.forName(SimpleDsAttributeMapper.class.toString());
                mapper = (IAttributeMapper) obmapper;
            }
            mapper.initialize(attr.getNameId(), attrProp);
            this._atmap.put(attr.getNameId(), mapper);
        } catch (InstantiationException e) {
            logger.error("Instantiation exception configuring mapping of " + attr.getNameId(), e);
            throw new MappingException("Instantiation exception configuring mapping of " + attr.getNameId(), e);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccess exception configuring mapping of " + attr.getNameId(), e);
            throw new MappingException("IllegalAccess exception during mapping of " + attr.getNameId(), e);
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFound exception configuring mapping of " + attr.getNameId(), e);
            throw new MappingException("ClassNotFound exception during mapping of " + attr.getNameId(), e);
        }
    }

    private void initializePredicate(String predProp, PredicateDef attr) throws MappingException {
        String mapClass = null;
        try {
            predProp.substring(predProp.lastIndexOf("|") + 1);
        } catch (IndexOutOfBoundsException e) {
        }
        if (mapClass == null || mapClass.equals("")) mapClass = SimpleDsPredicateMapper.class.toString();
        try {
            IPredicateMapper mapper = null;
            Object obmapper = Class.forName(mapClass).newInstance();
            if (obmapper instanceof IPredicateMapper) {
                mapper = (IPredicateMapper) obmapper;
            } else {
                logger.warn("Incorrect class for Predicate DsMapper: " + mapClass + ". Defaulting to SimpleDsPredicateMapper.");
                obmapper = Class.forName(SimpleDsPredicateMapper.class.toString());
                mapper = (IPredicateMapper) obmapper;
            }
            mapper.initialize(attr.getNameId(), predProp);
            this._predmap.put(attr.getNameId(), mapper);
        } catch (InstantiationException e) {
            logger.error("Instantiation exception configuring mapping of " + attr.getNameId(), e);
            throw new MappingException("Instantiation exception configuring mapping of " + attr.getNameId(), e);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccess exception configuring mapping of " + attr.getNameId(), e);
            throw new MappingException("IllegalAccess exception during mapping of " + attr.getNameId(), e);
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFound exception configuring mapping of " + attr.getNameId(), e);
            throw new MappingException("ClassNotFound exception during mapping of " + attr.getNameId(), e);
        }
    }

    private void initializeRole(String attrProp, IInteraction ixn) throws MappingException {
        String mapClass = null;
        try {
            mapClass = attrProp.substring(attrProp.lastIndexOf("|") + 1);
        } catch (IndexOutOfBoundsException e) {
        }
        if (mapClass == null || mapClass.equals("")) mapClass = SimpleDsRoleMapper.class.toString();
        try {
            IRoleMapper mapper = null;
            Object obmapper = Class.forName(mapClass).newInstance();
            if (obmapper instanceof IRoleMapper) {
                mapper = (IRoleMapper) obmapper;
            } else {
                logger.warn("Incorrect class for Role DsMapper: " + mapClass + ". Defaulting to SimpleDsRoleMapper.");
                obmapper = Class.forName(SimpleDsRoleMapper.class.toString());
                mapper = (IRoleMapper) obmapper;
            }
            mapper.initialize(ixn, attrProp);
            this._rolemap.put(ixn, mapper);
        } catch (InstantiationException e) {
            logger.error("Instantiation exception configuring role mapping for interaction: " + ixn.getNameId(), e);
            throw new MappingException("Instantiation exception configuring role mapping for interaction: " + ixn.getNameId(), e);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccess exception configuring role mapping for interaction: " + ixn.getNameId(), e);
            throw new MappingException("IllegalAccess exception configuring role mapping for interaction: " + ixn.getNameId(), e);
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFound exception configuring role mapping for interaction: " + ixn.getNameId(), e);
            throw new MappingException("ClassNotFound exception configuring role mapping for interaction: " + ixn.getNameId(), e);
        }
    }

    private void processSearchRoot() {
        String root = DsManager.getMainRoot();
        String sRoot = "dc=com";
        String sRdn = "cn";
        if (this._prop != null) {
            sRoot = this._prop.getProperty(IMapper.Ldap_SEARCH_BASE, root);
            sRdn = this._prop.getProperty(IMapper.Ldap_DEFAULT_RDN, "cn");
        }
        this._base = sRoot;
        this._rdn = sRdn;
    }

    public IEntry genNewEntry(IInteraction ixn, IAttributeValue[] vals, String[] roles) throws MappingException {
        String id = genDistinguishedName(ixn, vals);
        IEntry res = new DsEntry(id);
        return res;
    }

    public String genDistinguishedName(IInteraction ixn, IAttributeValue[] vals) {
        UUID uid = UUID.randomUUID();
        StringBuffer dnbuf = new StringBuffer();
        String rdnval = uid.toString();
        dnbuf.append(getRdn());
        dnbuf.append('=').append(rdnval).append(',');
        dnbuf.append(getSearchBase(ixn));
        return dnbuf.toString();
    }

    /**
	 * @deprecated Use {@link #mapToLdapFilter(IInteraction,Filter,List<IAttributeValue>,List,boolean)} instead
	 */
    public ILdapFilter mapToLdapFilter(IInteraction ixn, Filter filter, List<IAttributeValue> avals, boolean keyTermsOnly) throws MappingException {
        return mapToLdapFilter(ixn, filter, avals, null, keyTermsOnly);
    }

    public ILdapFilter mapToLdapFilter(IInteraction ixn, Filter filter, List<IAttributeValue> avals, List<DynamicFilter> dynFilters, boolean keyTermsOnly) throws MappingException {
        StringBuffer buf = new StringBuffer();
        List<IAttributeValue> clonevals = new ArrayList<IAttributeValue>(avals);
        filterToStringBuf(filter, clonevals, null, buf, ixn, keyTermsOnly);
        String strFilter = buf.toString();
        if (logger.isDebugEnabled()) logger.debug("Search filter: " + strFilter);
        return new DsFilter(strFilter);
    }

    public DsEntry mapToEntry(IInteraction ixn, IAttributeValue[] vals, String[] roles) throws MappingException {
        DsEntry entry = (DsEntry) genNewEntry(ixn, vals, roles);
        if (!entry.containsKey(OC_CLASS)) {
            String obclass = _ocmap.get(ixn);
            if (obclass != null) {
                List<String> ocvals = new ArrayList<String>();
                ocvals.add(obclass);
                entry.put(OC_CLASS, ocvals);
            }
        }
        for (int i = 0; i < vals.length; i++) {
            IAttributeValue avalue = vals[i];
            IAttributeMapper amap = this._atmap.get(avalue.getNameIdRef());
            amap.mapAttributeValue(avalue, entry);
        }
        IRoleMapper rmap = this.getRoleMapper(ixn);
        rmap.mapRolesToEntry(roles, entry);
        return entry;
    }
}
