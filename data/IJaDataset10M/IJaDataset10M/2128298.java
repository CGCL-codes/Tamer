package com.tensegrity.palobrowser.editors.dimensioneditor.attr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.palo.api.Attribute;
import org.palo.api.Axis;
import org.palo.api.Cube;
import org.palo.api.CubeView;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.PaloAPIException;
import com.tensegrity.palobrowser.cubequery.CubeQuery;
import com.tensegrity.palobrowser.cubequery.CubeQueryManager;
import com.tensegrity.palobrowser.cubequery.QueryViewMatcher;
import com.tensegrity.palobrowser.util.Assertion;

/**
 * {@<describe>}
 * <p>
 * This class saves and loads instances of type
 * {@link com.tensegrity.palobrowser.editors.dimensioneditor.attr.AttributeCubeWrapper}.
 * </p>
 * {@</describe>}
 * @author AndreasEbbert
 * @version $Id: AttributeCubePersistance.java,v 1.1 2006/11/28 16:56:51
 *          AndreasEbbert Exp $
 */
class AliasPersistence {

    private static final AliasPersistence instance = new AliasPersistence();

    private static final String ATTR_EL_ALIAS_NAME = "Alias";

    private AliasPersistence() {
    }

    /**
   * Returns the shared instance.
   * @return the shared instance.
   */
    static final AliasPersistence getInstance() {
        return instance;
    }

    private static final String PERS_ALIAS = "Alias#";

    private Attribute attrField;

    private void updateAliasesInStoredViews(Dimension dim, AttributeFieldWrapper afw) {
        String oldName = afw.getAttribute() != null ? afw.getAttribute().getName() : afw.getOrgName();
        String newName = afw.getName();
        CubeQuery[] cq = CubeQueryManager.getInstance().findCubeQuerys(dim.getDatabase());
        final QueryViewMatcher qvMatcher = QueryViewMatcher.getInstance();
        for (int i = 0; i < cq.length; i++) {
            boolean viewDirty = false;
            CubeView cubeView = qvMatcher.match(cq[i]);
            Axis[] axes = cubeView.getAxes();
            for (int j = 0; j < axes.length; j++) {
                String[] keys = axes[j].getProperties();
                for (int k = 0; k < keys.length; k++) {
                    if (keys[k].startsWith(PERS_ALIAS)) {
                        String dimName = keys[k].substring(PERS_ALIAS.length());
                        if (!dimName.equals(dim.getName())) continue;
                        String aliasName = axes[j].getPropertyValue(keys[k]);
                        if (aliasName == null || !(aliasName.equals(oldName))) continue;
                        axes[j].addProperty(keys[k], newName);
                        viewDirty = true;
                    }
                }
            }
            if (viewDirty) cubeView.save();
        }
    }

    void save(IAttributeCubeWrapper attrCubeWrapper, IProgressMonitor pm) {
        if (pm != null) pm.beginTask("Saving Aliases...", 3);
        final Dimension refDim = attrCubeWrapper.getRefDimension();
        if (refDim.getDatabase().getConnection().isLegacy()) return;
        final Attribute aliasAttr = checkForAliasAttribute(refDim);
        final List attrFields = attrCubeWrapper.getAttrFields();
        for (int i = 0; i < attrFields.size(); i++) {
            AttributeFieldWrapper afw = (AttributeFieldWrapper) attrFields.get(i);
            if (afw.isRenamed()) {
                updateAliasesInStoredViews(refDim, afw);
                if (afw.getAttribute() != null) afw.getAttribute().setName(afw.getName());
            }
        }
        List newFieldsNameList = new ArrayList();
        for (int i = 0; i < attrFields.size(); i++) newFieldsNameList.add(((AttributeFieldWrapper) attrFields.get(i)).getName());
        if (pm != null) pm.worked(1);
        Attribute[] aliasAttrChildren = getChildrenOfTypeString(aliasAttr);
        if (aliasAttrChildren.length < attrFields.size()) {
            List rootElementsNameList = new ArrayList();
            for (int i = 0; i < aliasAttrChildren.length; i++) rootElementsNameList.add(aliasAttrChildren[i].getName());
            for (int c = 0; c < attrFields.size(); c++) {
                final AttributeFieldWrapper attrFieldWrapper = (AttributeFieldWrapper) attrFields.get(c);
                if (!rootElementsNameList.contains(attrFieldWrapper.getName())) {
                    final Attribute newAttr = refDim.addAttribute(attrFieldWrapper.getName());
                    attrFieldWrapper.setAttribute(newAttr);
                    List childList = new ArrayList(Arrays.asList(getChildrenOfTypeString(aliasAttr)));
                    childList.add(newAttr);
                    aliasAttr.setChildren((Attribute[]) childList.toArray(new Attribute[childList.size()]));
                }
            }
        } else if (aliasAttrChildren.length > attrFields.size()) {
            for (int c = 0; c < aliasAttrChildren.length; c++) {
                if (!newFieldsNameList.contains(aliasAttrChildren[c].getName())) {
                    refDim.removeAttribute(aliasAttrChildren[c]);
                }
            }
        }
        if (pm != null) {
            pm.worked(1);
            pm.subTask("Updating Aliases...");
        }
        for (int c = 0; c < attrFields.size(); c++) {
            final IAttributeFieldWrapper attrFieldWrapper = (IAttributeFieldWrapper) attrFields.get(c);
            attrField = attrFieldWrapper.getAttribute();
            if (attrField == null) attrField = getAttributeByName(refDim, attrFieldWrapper.getName());
            Assertion.assertNotNull(attrField);
            final Element[] elRows = refDim.getElements();
            final List attrDataWrappers = attrFieldWrapper.getDataWrappers();
            for (int r = 0; r < elRows.length; r++) {
                AttributeDataWrapper actAttrDataWrapper = (AttributeDataWrapper) (r < attrDataWrappers.size() ? attrDataWrappers.get(r) : null);
                if (actAttrDataWrapper == null || actAttrDataWrapper.getName() == null) continue;
                if (!(actAttrDataWrapper.isDirty() || actAttrDataWrapper.isNew())) continue;
                try {
                    Object actVal = attrField.getValue(elRows[r]);
                    if (actVal == null || !actVal.equals(actAttrDataWrapper.getName())) attrField.setValue(elRows[r], actAttrDataWrapper.getName());
                    actAttrDataWrapper.setDirty(false);
                    actAttrDataWrapper.resetNew();
                } catch (PaloAPIException e) {
                    e.printStackTrace();
                    System.err.println(e);
                }
            }
        }
        if (pm != null) {
            pm.worked(1);
            pm.done();
        }
    }

    private void updateAliases() {
    }

    private Attribute[] getChildrenOfTypeString(Attribute p) {
        List l = new ArrayList();
        Attribute[] ch = p.getChildren();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i].getType() == Element.ELEMENTTYPE_STRING) l.add(ch[i]);
        }
        return (Attribute[]) l.toArray(new Attribute[0]);
    }

    IAttributeCubeWrapper load(Dimension refDim) {
        final Cube attrCube = refDim.getAttributeCube();
        Assertion.assertNotNull(attrCube, "AttributeCube for ref dimension " + refDim.getName() + " is null!");
        final AttributeCubeWrapper attrCubeWrapper = new AttributeCubeWrapper(attrCube, refDim);
        final Attribute aliasAttr = getAttributeByName(refDim, ATTR_EL_ALIAS_NAME);
        if (aliasAttr == null) return attrCubeWrapper;
        final Attribute[] attrCols = aliasAttr.getChildren();
        for (int c = 0; c < attrCols.length; c++) {
            AttributeFieldWrapper attrColWrapper = new AttributeFieldWrapper(attrCubeWrapper, attrCols[c], false);
            final Element[] refDimElements = refDim.getElements();
            for (int r = 0; r < refDimElements.length; r++) {
                try {
                    Object value = attrCols[c].getValue(refDimElements[r]);
                    final IAttributeDataWrapper attrDataWrapper = new AttributeDataWrapper(attrColWrapper, refDimElements[r], value.toString(), false);
                    attrColWrapper.addDataWrapper(r, attrDataWrapper);
                } catch (PaloAPIException e) {
                    System.err.println(e);
                    final IAttributeDataWrapper attrDataWrapper = new AttributeDataWrapper(attrColWrapper, refDimElements[r], "", true);
                    attrColWrapper.addDataWrapper(r, attrDataWrapper);
                }
            }
            attrCubeWrapper.addAttrField(attrColWrapper);
        }
        return attrCubeWrapper;
    }

    private Attribute checkForAliasAttribute(Dimension refDim) {
        Attribute[] rootAttr = refDim.getAttributes();
        for (int i = 0; rootAttr != null && i < rootAttr.length; i++) {
            if (ATTR_EL_ALIAS_NAME.equals(rootAttr[i].getName())) return rootAttr[i];
        }
        return refDim.addAttribute(ATTR_EL_ALIAS_NAME);
    }

    private Attribute getAttributeByName(Dimension refDim, String name) {
        Attribute[] attr = refDim.getAttributes();
        for (int i = 0; i < attr.length; i++) {
            if (attr[i].getName().equals(name)) return attr[i];
        }
        return null;
    }
}
