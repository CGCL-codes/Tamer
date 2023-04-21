package com.manydesigns.portofino.base;

import com.manydesigns.portofino.base.cache.BlobTokenCache;
import com.manydesigns.portofino.base.cache.UserQueryCache;
import com.manydesigns.portofino.base.calculations.MDFuncCalcType;
import com.manydesigns.portofino.base.navigation.Navigation;
import com.manydesigns.portofino.base.operations.MDClassOperation;
import com.manydesigns.portofino.base.operations.MDGlobalOperation;
import com.manydesigns.portofino.base.operations.MDObjectOperation;
import com.manydesigns.portofino.base.operations.MDObjectSetOperation;
import com.manydesigns.portofino.base.path.MDPathElement;
import com.manydesigns.portofino.base.portal.MDPortlet;
import com.manydesigns.portofino.base.portal.PortletCache;
import com.manydesigns.portofino.base.users.MDMetaUserGroup;
import com.manydesigns.portofino.base.users.User;
import com.manydesigns.portofino.base.workflow.MDWfAction;
import com.manydesigns.portofino.base.workflow.MDWfState;
import com.manydesigns.portofino.base.workflow.MDWfTransition;
import com.manydesigns.portofino.util.LocaleInfo;
import java.util.*;

/**
 *
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public interface MDConfig {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    MDConfigContainer getConfigContainer();

    String getName();

    boolean allowAnonymous();

    LocaleInfo getLocaleInfo();

    Locale getLocale();

    int getMaxPathSearchDepth();

    int getMaxSearchResultPerPage();

    String getSchema2();

    String getSchema1();

    String getSchema0();

    String getStyle();

    String getSystemName();

    String getMdTemplate();

    String getMDCvsWorkingDirectory();

    String getMDCvsRepository();

    String getMDCvsModule();

    String getCvsPath();

    boolean isUpstairs();

    boolean isMetaEnabled();

    void selfTest() throws Exception;

    void selfTest(Collection<String> errors);

    void versionTest() throws Exception;

    MDClass getMDClassById(int id) throws Exception;

    MDAttribute getMDAttributeById(int id) throws Exception;

    MDClass getMDClassByName(String name) throws Exception;

    Collection<MDClass> getAllMDClasses();

    Collection<MDAttribute> getAllAttributes();

    MDPortlet getPortletById(int id) throws Exception;

    MDJasperReports getJasperReportsById(int id) throws Exception;

    Collection<MDMetaUserGroup> getAllMetaUserGroups();

    MDWfAction getWfActionById(int id) throws Exception;

    MDWfState getWfStateById(int id) throws Exception;

    MDWfTransition getWfTransitionById(int id) throws Exception;

    MDAttribute getAttributeById(int id) throws Exception;

    MDPathElement getPathElementById(int id) throws Exception;

    MDObjectOperation getObjectOperationById(int id) throws Exception;

    MDObjectSetOperation getObjectSetOperationById(int id) throws Exception;

    MDClassOperation getClassOperationById(int id) throws Exception;

    Collection<MDObjectOperation> getAllObjectOperations();

    Collection<MDClassOperation> getAllClassOperations();

    Collection<MDGlobalOperation> getAllGlobalOperations();

    Set<MDObjectOperation> getAllObjectOperationsSortedByOrder();

    Set<MDObjectSetOperation> getAllObjectSetOperationsSortedByOrder();

    Set<MDClassOperation> getAllClassOperationsSortedByOrder();

    Set<MDGlobalOperation> getAllGlobalOperationsSortedByOrder();

    MDGlobalOperation getGlobalOperationById(int id) throws Exception;

    Collection<MDFuncCalcType> getAllFuncCalcs();

    MDMetaUserGroup getMetaUserGroupById(int id) throws Exception;

    MDFuncCalcType getFuncCalcTypeByName(String name) throws Exception;

    Collection<MDPortlet> getPortletsOnHomepage();

    void delete(Collection<MDObject> objs, Map<MDObject, MDRelAttribute> relAttrForUpdate) throws Exception;

    List<MDObject> propagateForDelete(List<MDObject> coseDaFare, Map<MDObject, MDRelAttribute> relAttrForUpdate) throws Exception;

    QueryCache getQueryCache();

    UserQueryCache getUserQueryCache();

    BlobTokenCache getBlobTokenCache();

    PortletCache getPortletCache();

    Set<MDClass> getAllMDClassesSortedByOrder();

    Set<MDClass> getAllMDClassesSortedByPrettyName();

    Set<MDClass> getAllMDClassesSortedByPrettyPlural();

    Collection<MDPortlet> getAllPortlets();

    Collection<MDJasperReports> getAllJasperReports();

    Navigation getNavigation();

    int getNextBlobId();

    boolean isLoginEnabled();

    void writeStartTransactionDDL() throws Exception;

    void writeCommitDDL() throws Exception;

    User getCurrentUser();

    void setCurrentUser(User user);

    Transaction getCurrentTransaction();

    @Override
    String toString();

    void visit(MDConfigVisitor visitor);
}
