package org.xaware.ide.xadev.gui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Vector;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.xaware.ide.xadev.datamodel.Functoid;
import org.xaware.shared.util.FunctoidConfigUtil;
import org.xaware.shared.util.XAwareException;

/**
 * @author jweaver
 *
 */
public class SinglePredefinedFunctoidPanel extends PredefinedFunctoidsPanel {

    protected final String m_functoidName;

    /**
	 * @param inParent
	 * @param param
	 * @param inputParams
	 * @param sourceRoot
     * @param p_selectedElement the element the path will be applied on.  If
     * the path is being applied on an attribute then the parent element
     * for that attribute should be passed in.
	 * @throws XAwareException if functoid string is invalid.
	 * @throws UnsupportedEncodingException if error occurs while decoding functoid params.
	 */
    public SinglePredefinedFunctoidPanel(final Composite inParent, final String param, final Vector inputParams, final Element sourceRoot, final String functoidName, final Element p_selectedElement) throws XAwareException, UnsupportedEncodingException {
        super(inParent, param, inputParams, sourceRoot, p_selectedElement);
        m_functoidName = functoidName;
        populateFunctions();
        handleCategorySelection();
        functionsList.select(0);
        handleFunctionListSelection();
    }

    /**
     * Method to populate all functions.
     */
    @Override
    public void populateFunctions() {
        functionArray = new Functoid[1];
        if (m_functoidName == null) {
            return;
        }
        final Element el = FunctoidConfigUtil.getInstance().getXAFunctoidsElement();
        final java.util.List fList = getFunctionsList(el);
        if (fList != null) {
            final ArrayList<String> categoryList = new ArrayList<String>();
            for (int i = 0; i < fList.size(); i++) {
                final Functoid obj = (Functoid) fList.get(i);
                if (m_functoidName.equals(obj.getName())) {
                    functionArray[0] = obj;
                    if (!obj.getCategory().trim().equals("") && !obj.getCategory().trim().equalsIgnoreCase("All") && !categoryList.contains(obj.getCategory())) {
                        categoryList.add(obj.getCategory());
                    }
                    break;
                }
            }
            transferListToCategoryCB(categoryList);
        }
        populateListData();
    }

    @Override
    protected void addAllCategory() {
    }
}
