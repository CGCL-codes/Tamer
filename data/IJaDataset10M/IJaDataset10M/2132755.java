package gallery.service.pages;

import common.dao.IGenericDAO;
import core.dao.IPagesDAO;
import gallery.model.beans.Pages;
import common.services.generic.GenericServiceImpl;
import gallery.web.controller.pages.submodules.ASubmodule;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesServiceImpl extends GenericServiceImpl<Pages, Long> implements IPagesService {

    protected static final String[] ORDER_BY = new String[] { "type", "sort", "name" };

    protected static final String[] ORDER_HOW = new String[] { "ASC", "ASC", "ASC" };

    protected IPagesDAO<Pages, Long> pagesDAO;

    @Override
    public void init() {
        StringBuilder sb = new StringBuilder();
        common.utils.MiscUtils.checkNotNull(pagesDAO, "pagesDAO", sb);
        if (sb.length() > 0) {
            throw new NullPointerException(sb.toString());
        }
    }

    @Override
    public void save(Pages entity) {
        if (entity.getId_pages() != null) pagesDAO.updatePropertiesById(LAST_PSEUDONYM, new Object[] { Boolean.FALSE }, entity.getId_pages());
        super.save(entity);
    }

    @Override
    public int deleteById(Long id) {
        Long id_pages = (Long) pagesDAO.getSinglePropertyU("id_pages", "id", id);
        if (id_pages != null) {
            Long count = (Long) pagesDAO.getSinglePropertyU("count(*)", "id_pages", id_pages);
            if (count < 2) pagesDAO.updatePropertiesById(LAST_PSEUDONYM, new Object[] { Boolean.TRUE }, id_pages);
        }
        return super.deleteById(id);
    }

    @Override
    public int deleteById(Long[] id) {
        throw new UnsupportedOperationException("unimplemented yet");
    }

    @Override
    public IPagesDAO<Pages, Long> getDao() {
        return pagesDAO;
    }

    public void setDao(IPagesDAO<Pages, Long> dao) {
        super.dao = dao;
        this.pagesDAO = dao;
    }

    @Override
    public void setDao(IGenericDAO<Pages, Long> dao) {
        super.dao = dao;
        this.pagesDAO = (IPagesDAO<Pages, Long>) dao;
    }

    protected static final String[] ALL_SHORT_CMS = new String[] { "id", "name", "sort", "active", "last", "type" };

    @Override
    public List<Pages> getAllShortCms() {
        return dao.getAllShortOrdered(ALL_SHORT_CMS, ORDER_BY, ORDER_HOW);
    }

    @Override
    public List<Pages> getShortOrderedByPropertyValueCms(String propertyName, Object propertyValue) {
        return pagesDAO.getByPropertyValuePortionOrdered(ALL_SHORT_CMS, ALL_SHORT_CMS, propertyName, propertyValue, 0, -1, ORDER_BY, ORDER_HOW);
    }

    @Override
    public List<Pages> getPagesForRelocate(Long id) {
        return pagesDAO.getPagesForRelocateOrdered(id, ORDER_BY, ORDER_HOW);
    }

    protected static final String[] RELOCATE_PSEUDONYMS = new String[] { "id_pages" };

    protected static final String[] LAST_PSEUDONYM = new String[] { "last" };

    @Override
    public boolean relocatePages(Long id, Long newIdPages) {
        boolean rez = pagesDAO.checkRecursion(id, newIdPages);
        if (rez) {
            Long id_pages = (Long) pagesDAO.getSinglePropertyU("id_pages", "id", id);
            if (id_pages != null) {
                Long count = (Long) pagesDAO.getSinglePropertyU("count(*)", "id_pages", id_pages);
                if (count < 2) pagesDAO.updatePropertiesById(LAST_PSEUDONYM, new Object[] { Boolean.TRUE }, id_pages);
            }
            pagesDAO.updatePropertiesById(RELOCATE_PSEUDONYMS, new Object[] { newIdPages }, id);
            if (newIdPages != null) pagesDAO.updatePropertiesById(LAST_PSEUDONYM, new Object[] { Boolean.FALSE }, newIdPages);
        }
        return rez;
    }

    @Override
    public List<Pages> getAllPagesParents(Long id, String[] property_names) {
        List<Pages> temp = pagesDAO.getAllParentsRecursive(id, property_names, property_names);
        if (temp == null) return null;
        List<Pages> rez = new Vector<Pages>(temp.size() + 1);
        for (int i = temp.size(); i > 0; i--) {
            rez.add(temp.get(i - 1));
        }
        return rez;
    }

    protected static final String[] CATEGORIES_SELECT_PSEUDONYMES = new String[] { "id", "name", "last" };

    protected static final String[] CATEGORIES_WHERE = new String[] { "id_pages", "type", "active" };

    @Override
    public List<Pages> getCategories(Long id, String type) {
        if (id != null) {
            Object[] values = new Object[] { id, type, Boolean.TRUE };
            return dao.getByPropertiesValuePortionOrdered(CATEGORIES_SELECT_PSEUDONYMES, CATEGORIES_SELECT_PSEUDONYMES, CATEGORIES_WHERE, values, 0, -1, ORDER_BY, ORDER_HOW);
        } else {
            return null;
        }
    }

    /**
	 * constant for properties to be selected from DB for select html element for choosing a category
	 */
    protected static final String[] COMBOBOX_PROPERTIES = new String[] { "id", "name" };

    protected static final String[] COMBOBOX_ORDER_BY = new String[] { "name" };

    protected static final String[] COMBOBOX_ORDER_HOW = new String[] { "ASC" };

    /**
     * returns only id and name properties
     * result is ordered by name
     */
    @Override
    public List<Pages> getAllCombobox(Boolean active, Boolean last, String type) {
        int len = 0;
        String[] names = new String[3];
        Object[] items = new Object[3];
        if (active != null) {
            names[len] = "active";
            items[len] = active;
            len++;
        }
        if (last != null) {
            names[len] = "last";
            items[len] = last;
            len++;
        }
        if (type != null) {
            names[len] = "type";
            items[len] = type;
            len++;
        }
        if (len == 0) {
            return dao.getAllShortOrdered(COMBOBOX_PROPERTIES, COMBOBOX_ORDER_BY, COMBOBOX_ORDER_HOW);
        } else if (len == 1) {
            return dao.getByPropertyValuePortionOrdered(COMBOBOX_PROPERTIES, COMBOBOX_PROPERTIES, names[0], items[0], 0, 0, COMBOBOX_ORDER_BY, COMBOBOX_ORDER_HOW);
        } else {
            names = java.util.Arrays.copyOf(names, len);
            items = java.util.Arrays.copyOf(items, len);
            return dao.getByPropertiesValuePortionOrdered(COMBOBOX_PROPERTIES, COMBOBOX_PROPERTIES, names, items, 0, 0, COMBOBOX_ORDER_BY, COMBOBOX_ORDER_HOW);
        }
    }

    @Override
    public List<Long> getAllActiveChildrenId(Long id) {
        return pagesDAO.getAllActiveChildrenId(id, null, null);
    }

    protected static String[] SUBMODULES_WHERE = new String[] { "active", "id_pages", "type" };

    protected static String[] SUBMODULES_PSEUDONYMES = new String[] { "id", "name", "type" };

    @Override
    public List<Pages> activateSubmodules(List<Pages> navigation, Map<String, ASubmodule> submodules) {
        List<Pages> rez = new Vector<Pages>(submodules.size());
        HashSet<String> submodules_types = new HashSet<String>(submodules.keySet());
        List<Pages> temp;
        int i = navigation.size();
        Object[][] values = new Object[SUBMODULES_WHERE.length][];
        values[0] = new Object[] { Boolean.TRUE };
        while (submodules_types.size() > 0 && i > 0) {
            i--;
            values[1] = new Object[] { navigation.get(i).getId() };
            values[2] = submodules_types.toArray();
            temp = dao.getByPropertiesValuesPortionOrdered(SUBMODULES_PSEUDONYMES, SUBMODULES_PSEUDONYMES, SUBMODULES_WHERE, values, 0, -1, null, null);
            for (Pages p : temp) {
                rez.add(p);
                ASubmodule sub = submodules.get(p.getType());
                sub.setActive(Boolean.TRUE);
                sub.setPage(p);
                submodules_types.remove(p.getType());
            }
        }
        if (submodules_types.size() > 0) {
            values[1] = null;
            values[2] = submodules_types.toArray();
            temp = dao.getByPropertiesValuesPortionOrdered(SUBMODULES_PSEUDONYMES, SUBMODULES_PSEUDONYMES, SUBMODULES_WHERE, values, 0, -1, null, null);
            for (Pages p : temp) {
                rez.add(p);
                ASubmodule sub = submodules.get(p.getType());
                sub.setActive(Boolean.TRUE);
                sub.setPage(p);
                submodules_types.remove(p.getType());
            }
        }
        return rez;
    }

    @Override
    public List<Pages> getPagesChildrenRecurciveOrderedWhere(String[] properties, String[] propertyNames, Object[][] propertyValues) {
        return pagesDAO.getPagesChildrenRecurciveOrderedWhere(properties, properties, propertyNames, propertyValues, ORDER_BY, ORDER_HOW, null);
    }

    @Override
    public List<Pages> getPagesChildrenRecurciveOrderedWhere(String[] properties, String[] propPseudonyms, String[] propertyNames, Object[][] propertyValues) {
        return pagesDAO.getPagesChildrenRecurciveOrderedWhere(properties, propPseudonyms, propertyNames, propertyValues, ORDER_BY, ORDER_HOW, null);
    }

    @Override
    public List<Pages> getPagesChildrenRecurciveOrderedWhere(String[] properties, String[] propPseudonyms, String[] propertyNames, Object[][] propertyValues, Long first_id) {
        return pagesDAO.getPagesChildrenRecurciveOrderedWhere(properties, propPseudonyms, propertyNames, propertyValues, ORDER_BY, ORDER_HOW, first_id);
    }

    protected static final String[] ID_PSEUDONYM = new String[] { "id" };

    @Override
    public void recalculateLast(Long id) {
        if (id == null) {
            List<Pages> ids = pagesDAO.getAllShortOrdered(ID_PSEUDONYM, null, null);
            Long count;
            for (Pages p : ids) {
                count = (Long) pagesDAO.getSinglePropertyU("count(*)", "id_pages", p.getId());
                if (count > 0) {
                    pagesDAO.updatePropertiesById(LAST_PSEUDONYM, new Object[] { Boolean.FALSE }, p.getId());
                } else {
                    pagesDAO.updatePropertiesById(LAST_PSEUDONYM, new Object[] { Boolean.TRUE }, p.getId());
                }
            }
        } else {
            Long count = (Long) pagesDAO.getSinglePropertyU("count(*)", "id_pages", id);
            if (count > 0) {
                pagesDAO.updatePropertiesById(LAST_PSEUDONYM, new Object[] { Boolean.FALSE }, id);
            } else {
                pagesDAO.updatePropertiesById(LAST_PSEUDONYM, new Object[] { Boolean.TRUE }, id);
            }
        }
    }

    @Override
    public List<Pages> getShortByPropertiesValuesOrdered(String[] propNames, String[] propertyName, Object[][] propertyValue) {
        return super.getByPropertiesValuesOrdered(propNames, propertyName, propertyValue, ORDER_BY, ORDER_HOW);
    }
}
