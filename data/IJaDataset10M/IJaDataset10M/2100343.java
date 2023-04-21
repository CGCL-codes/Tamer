package org.kwantu.zakwantu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.kwantu.m2.model.ui.KwantuPickList;
import org.kwantu.m2.xpath.KwantuInvalidXPathException;
import org.kwantu.persistence.AbstractPersistentObject;

/**
 *
 * @author siviwe
 */
public class KwantuPickListPanel extends Panel {

    private static final Log LOG = LogFactory.getLog(KwantuPickListPanel.class);

    private IModel contextModel;

    public KwantuPickListPanel(String id, KwantuWicketXPathModel contextModel, final KwantuPickList kwantuPickList) throws KwantuInvalidXPathException {
        super(id, contextModel);
        this.contextModel = contextModel;
        KwantuWicketXPathModel xpModel = contextModel;
        Object context = xpModel.getContextObject();
        HashMap<String, ArrayList<PicklistItem>> pickListMap = null;
        String listPath = kwantuPickList.getPath();
        if (pickListMap == null) {
            pickListMap = new HashMap<String, ArrayList<PicklistItem>>();
        }
        Set opts = (Set) ((ZakwantuApplication) getApplication()).getController().getFromContext(listPath, context);
        ArrayList<PicklistItem> pickList = new ArrayList<PicklistItem>();
        if (opts != null) {
            for (Object o : opts) {
                String displayValue = null;
                Serializable idnty = null;
                if (o instanceof AbstractPersistentObject) {
                    displayValue = o.toString();
                    idnty = ((ZakwantuApplication) getApplication()).determineSerializableId((AbstractPersistentObject) o);
                } else {
                    displayValue = o.toString();
                }
                pickList.add(new PicklistItem(displayValue, idnty));
            }
            Collections.sort(pickList);
        }
        RepeatingView view = new RepeatingView("pickListView");
        WebMarkupContainer row = new WebMarkupContainer(view.newChildId());
        view.add(row);
        IModel labelModel = new Model(listPath);
        row.add(new Label("labelName", labelModel));
        DropDownChoice dropDown = new DropDownChoice("pickList", pickList);
        dropDown.setType(KwantuPickList.class);
        row.add(dropDown);
        add(view);
    }
}
