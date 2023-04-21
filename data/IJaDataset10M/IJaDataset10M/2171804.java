package net.sf.ahtutils.controller.interfaces;

import java.util.List;
import java.util.Set;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclCategoryGroup;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclCategoryProjectRole;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclCategoryUsecase;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclGroup;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclRole;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclView;
import net.sf.ahtutils.model.interfaces.status.UtilsDescription;
import net.sf.ahtutils.model.interfaces.status.UtilsLang;
import net.sf.ahtutils.xml.access.AclQuery;
import net.sf.ahtutils.xml.access.Category;
import net.sf.ahtutils.xml.access.Group;

public interface AhtAclFacade extends UtilsFacade {

    <L extends UtilsLang, D extends UtilsDescription, CU extends UtilsAclCategoryUsecase<L, D, CU, U>, CR extends UtilsAclCategoryGroup<L, D, CU, CR, U, R>, U extends UtilsAclView<L, D, CU, U>, R extends UtilsAclGroup<L, D, CU, CR, U, R>> List<R> fAclRoles(Class<R> type, List<Group> lRoles) throws UtilsNotFoundException;

    @Deprecated
    <L extends UtilsLang, D extends UtilsDescription, CU extends UtilsAclCategoryUsecase<L, D, CU, U>, CR extends UtilsAclCategoryGroup<L, D, CU, CR, U, R>, U extends UtilsAclView<L, D, CU, U>, R extends UtilsAclGroup<L, D, CU, CR, U, R>> Set<String> findUsecaseCodesForRoles(List<R> roles);

    <L extends UtilsLang, D extends UtilsDescription, CU extends UtilsAclCategoryUsecase<L, D, CU, U>, CR extends UtilsAclCategoryGroup<L, D, CU, CR, U, R>, U extends UtilsAclView<L, D, CU, U>, R extends UtilsAclGroup<L, D, CU, CR, U, R>> Set<U> findUsecasesForRoles(List<R> roles);

    <L extends UtilsLang, D extends UtilsDescription, CU extends UtilsAclCategoryUsecase<L, D, CU, U>, U extends UtilsAclView<L, D, CU, U>> Category getUsecaseCategory(CU aclUsecaseCategory, AclQuery qAcl);

    <L extends UtilsLang, D extends UtilsDescription, CU extends UtilsAclCategoryUsecase<L, D, CU, U>, CR extends UtilsAclCategoryGroup<L, D, CU, CR, U, R>, U extends UtilsAclView<L, D, CU, U>, R extends UtilsAclGroup<L, D, CU, CR, U, R>> Category getRoleCategory(CR aclRoleCategory, AclQuery qAcl);

    <L extends UtilsLang, D extends UtilsDescription, C extends UtilsAclCategoryProjectRole<L, D, C, R>, R extends UtilsAclRole<L, D, C, R>> Category getProjectRoleCategory(C aclProjectRoleCategory, AclQuery qAcl);

    List<Category> getUsecaseCategories(AclQuery qAcl);

    List<Category> getRoleCategories(AclQuery qAcl);

    List<Category> getProjectRoleCategories(AclQuery qAcl);
}
