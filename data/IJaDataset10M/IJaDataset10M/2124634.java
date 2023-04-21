package com.liferay.portlet.shopping.service.impl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.model.impl.ResourceImpl;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.persistence.UserUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.shopping.CategoryNameException;
import com.liferay.portlet.shopping.model.ShoppingCategory;
import com.liferay.portlet.shopping.model.ShoppingItem;
import com.liferay.portlet.shopping.model.impl.ShoppingCategoryImpl;
import com.liferay.portlet.shopping.service.ShoppingItemLocalServiceUtil;
import com.liferay.portlet.shopping.service.base.ShoppingCategoryLocalServiceBaseImpl;
import com.liferay.portlet.shopping.service.persistence.ShoppingCategoryUtil;
import com.liferay.portlet.shopping.service.persistence.ShoppingItemUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="ShoppingCategoryLocalServiceImpl.java.html"><b><i>View Source</i>
 * </b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ShoppingCategoryLocalServiceImpl extends ShoppingCategoryLocalServiceBaseImpl {

    public ShoppingCategory addCategory(long userId, long plid, long parentCategoryId, String name, String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {
        return addCategory(userId, plid, parentCategoryId, name, description, Boolean.valueOf(addCommunityPermissions), Boolean.valueOf(addGuestPermissions), null, null);
    }

    public ShoppingCategory addCategory(long userId, long plid, long parentCategoryId, String name, String description, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        return addCategory(userId, plid, parentCategoryId, name, description, null, null, communityPermissions, guestPermissions);
    }

    public ShoppingCategory addCategory(long userId, long plid, long parentCategoryId, String name, String description, Boolean addCommunityPermissions, Boolean addGuestPermissions, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        User user = UserUtil.findByPrimaryKey(userId);
        long groupId = PortalUtil.getPortletGroupId(plid);
        parentCategoryId = getParentCategoryId(groupId, parentCategoryId);
        Date now = new Date();
        validate(name);
        long categoryId = CounterLocalServiceUtil.increment();
        ShoppingCategory category = ShoppingCategoryUtil.create(categoryId);
        category.setGroupId(groupId);
        category.setCompanyId(user.getCompanyId());
        category.setUserId(user.getUserId());
        category.setUserName(user.getFullName());
        category.setCreateDate(now);
        category.setModifiedDate(now);
        category.setParentCategoryId(parentCategoryId);
        category.setName(name);
        category.setDescription(description);
        ShoppingCategoryUtil.update(category);
        if ((addCommunityPermissions != null) && (addGuestPermissions != null)) {
            addCategoryResources(category, addCommunityPermissions.booleanValue(), addGuestPermissions.booleanValue());
        } else {
            addCategoryResources(category, communityPermissions, guestPermissions);
        }
        return category;
    }

    public void addCategoryResources(long categoryId, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {
        ShoppingCategory category = ShoppingCategoryUtil.findByPrimaryKey(categoryId);
        addCategoryResources(category, addCommunityPermissions, addGuestPermissions);
    }

    public void addCategoryResources(ShoppingCategory category, boolean addCommunityPermissions, boolean addGuestPermissions) throws PortalException, SystemException {
        ResourceLocalServiceUtil.addResources(category.getCompanyId(), category.getGroupId(), category.getUserId(), ShoppingCategory.class.getName(), category.getCategoryId(), false, addCommunityPermissions, addGuestPermissions);
    }

    public void addCategoryResources(long categoryId, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        ShoppingCategory category = ShoppingCategoryUtil.findByPrimaryKey(categoryId);
        addCategoryResources(category, communityPermissions, guestPermissions);
    }

    public void addCategoryResources(ShoppingCategory category, String[] communityPermissions, String[] guestPermissions) throws PortalException, SystemException {
        ResourceLocalServiceUtil.addModelResources(category.getCompanyId(), category.getGroupId(), category.getUserId(), ShoppingCategory.class.getName(), category.getCategoryId(), communityPermissions, guestPermissions);
    }

    public void deleteCategory(long categoryId) throws PortalException, SystemException {
        ShoppingCategory category = ShoppingCategoryUtil.findByPrimaryKey(categoryId);
        deleteCategory(category);
    }

    public void deleteCategory(ShoppingCategory category) throws PortalException, SystemException {
        Iterator itr = ShoppingCategoryUtil.findByG_P(category.getGroupId(), category.getCategoryId()).iterator();
        while (itr.hasNext()) {
            ShoppingCategory curCategory = (ShoppingCategory) itr.next();
            deleteCategory(curCategory);
        }
        ShoppingItemLocalServiceUtil.deleteItems(category.getCategoryId());
        ResourceLocalServiceUtil.deleteResource(category.getCompanyId(), ShoppingCategory.class.getName(), ResourceImpl.SCOPE_INDIVIDUAL, category.getCategoryId());
        ShoppingCategoryUtil.remove(category.getCategoryId());
    }

    public List getCategories(long groupId) throws SystemException {
        return ShoppingCategoryUtil.findByGroupId(groupId);
    }

    public List getCategories(long groupId, long parentCategoryId, int begin, int end) throws SystemException {
        return ShoppingCategoryUtil.findByG_P(groupId, parentCategoryId, begin, end);
    }

    public int getCategoriesCount(long groupId, long parentCategoryId) throws SystemException {
        return ShoppingCategoryUtil.countByG_P(groupId, parentCategoryId);
    }

    public ShoppingCategory getCategory(long categoryId) throws PortalException, SystemException {
        return ShoppingCategoryUtil.findByPrimaryKey(categoryId);
    }

    public ShoppingCategory getParentCategory(ShoppingCategory category) throws PortalException, SystemException {
        ShoppingCategory parentCategory = ShoppingCategoryUtil.findByPrimaryKey(category.getParentCategoryId());
        return parentCategory;
    }

    public List getParentCategories(long categoryId) throws PortalException, SystemException {
        return getParentCategories(ShoppingCategoryUtil.findByPrimaryKey(categoryId));
    }

    public List getParentCategories(ShoppingCategory category) throws PortalException, SystemException {
        List parentCategories = new ArrayList();
        ShoppingCategory tempCategory = category;
        for (; ; ) {
            parentCategories.add(tempCategory);
            if (tempCategory.getParentCategoryId() == ShoppingCategoryImpl.DEFAULT_PARENT_CATEGORY_ID) {
                break;
            }
            tempCategory = ShoppingCategoryUtil.findByPrimaryKey(tempCategory.getParentCategoryId());
        }
        Collections.reverse(parentCategories);
        return parentCategories;
    }

    public void getSubcategoryIds(List categoryIds, long groupId, long categoryId) throws SystemException {
        Iterator itr = ShoppingCategoryUtil.findByG_P(groupId, categoryId).iterator();
        while (itr.hasNext()) {
            ShoppingCategory category = (ShoppingCategory) itr.next();
            categoryIds.add(new Long(category.getCategoryId()));
            getSubcategoryIds(categoryIds, category.getGroupId(), category.getCategoryId());
        }
    }

    public ShoppingCategory updateCategory(long categoryId, long parentCategoryId, String name, String description, boolean mergeWithParentCategory) throws PortalException, SystemException {
        ShoppingCategory category = ShoppingCategoryUtil.findByPrimaryKey(categoryId);
        parentCategoryId = getParentCategoryId(category, parentCategoryId);
        validate(name);
        category.setModifiedDate(new Date());
        category.setParentCategoryId(parentCategoryId);
        category.setName(name);
        category.setDescription(description);
        ShoppingCategoryUtil.update(category);
        if (mergeWithParentCategory && (categoryId != parentCategoryId) && (parentCategoryId != ShoppingCategoryImpl.DEFAULT_PARENT_CATEGORY_ID)) {
            mergeCategories(category, parentCategoryId);
        }
        return category;
    }

    protected long getParentCategoryId(long groupId, long parentCategoryId) throws SystemException {
        if (parentCategoryId != ShoppingCategoryImpl.DEFAULT_PARENT_CATEGORY_ID) {
            ShoppingCategory parentCategory = ShoppingCategoryUtil.fetchByPrimaryKey(parentCategoryId);
            if ((parentCategory == null) || (groupId != parentCategory.getGroupId())) {
                parentCategoryId = ShoppingCategoryImpl.DEFAULT_PARENT_CATEGORY_ID;
            }
        }
        return parentCategoryId;
    }

    protected long getParentCategoryId(ShoppingCategory category, long parentCategoryId) throws SystemException {
        if (parentCategoryId == ShoppingCategoryImpl.DEFAULT_PARENT_CATEGORY_ID) {
            return parentCategoryId;
        }
        if (category.getCategoryId() == parentCategoryId) {
            return category.getParentCategoryId();
        } else {
            ShoppingCategory parentCategory = ShoppingCategoryUtil.fetchByPrimaryKey(parentCategoryId);
            if ((parentCategory == null) || (category.getGroupId() != parentCategory.getGroupId())) {
                return category.getParentCategoryId();
            }
            List subcategoryIds = new ArrayList();
            getSubcategoryIds(subcategoryIds, category.getGroupId(), category.getCategoryId());
            if (subcategoryIds.contains(new Long(parentCategoryId))) {
                return category.getParentCategoryId();
            }
            return parentCategoryId;
        }
    }

    protected void mergeCategories(ShoppingCategory fromCategory, long toCategoryId) throws PortalException, SystemException {
        Iterator itr = ShoppingCategoryUtil.findByG_P(fromCategory.getGroupId(), fromCategory.getCategoryId()).iterator();
        while (itr.hasNext()) {
            ShoppingCategory category = (ShoppingCategory) itr.next();
            mergeCategories(category, toCategoryId);
        }
        itr = ShoppingItemUtil.findByCategoryId(fromCategory.getCategoryId()).iterator();
        while (itr.hasNext()) {
            ShoppingItem item = (ShoppingItem) itr.next();
            item.setCategoryId(toCategoryId);
            ShoppingItemUtil.update(item);
        }
        ShoppingCategoryUtil.remove(fromCategory.getCategoryId());
    }

    protected void validate(String name) throws PortalException {
        if ((Validator.isNull(name)) || (name.indexOf("\\\\") != -1) || (name.indexOf("//") != -1)) {
            throw new CategoryNameException();
        }
    }
}
