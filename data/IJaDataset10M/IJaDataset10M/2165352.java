package com.mobileares.midp.widgets.client.textbox2;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-11-23
 * Time: 17:26:12
 * To change this template use File | Settings | File Templates.
 */
public class LabelContainer {

    private boolean fireValid = false;

    private List<LabelProvider> indexChildren = new ArrayList<LabelProvider>();

    public LabelContainer(boolean fireValid) {
        this.fireValid = fireValid;
    }

    public LabelContainer() {
        this(false);
    }

    public void registerElement(final LabelProvider provider) {
        if (fireValid) {
            if (provider.getWidget() instanceof BaseLabelText) {
                BaseLabelText widget = ((BaseLabelText) provider.getWidget());
                if (widget.getRealWidget() instanceof HasAllFocusHandlers) ((HasAllFocusHandlers) widget.getRealWidget()).addBlurHandler(new BlurHandler() {

                    public void onBlur(BlurEvent event) {
                        List<ILabelValidator> vs = provider.getValidators();
                        if (vs != null && vs.size() > 0) {
                            for (int i = 0; i < vs.size(); i++) {
                                if (!vs.get(i).fire(provider.getWidget())) {
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }
        indexChildren.add(provider);
    }

    public void ddOutModel(Object model) {
        if (indexChildren.size() > 0) {
            for (Iterator it = indexChildren.iterator(); it.hasNext(); ) {
                LabelProvider el = (LabelProvider) it.next();
                el.getProvider().reset(el.getWidget());
                if (model != null) el.getProvider().setValue(model);
            }
        }
    }

    public void ddIntModel(Object model) {
        if (indexChildren.size() > 0) {
            for (Iterator it = indexChildren.iterator(); it.hasNext(); ) {
                LabelProvider el = (LabelProvider) it.next();
                el.getProvider().getValue(model);
            }
        }
    }

    public List<LabelProvider> getChildren() {
        return indexChildren;
    }

    public void reset() {
        if (indexChildren.size() > 0) {
            for (Iterator it = indexChildren.iterator(); it.hasNext(); ) {
                LabelProvider el = (LabelProvider) it.next();
                el.getProvider().reset(el.getWidget());
            }
        }
    }

    public boolean fire() {
        boolean flag = true;
        if (indexChildren.size() > 0) {
            for (Iterator it = indexChildren.iterator(); it.hasNext(); ) {
                LabelProvider el = (LabelProvider) it.next();
                List<ILabelValidator> vs = el.getValidators();
                if (vs != null && vs.size() > 0) {
                    for (int i = 0; i < vs.size(); i++) {
                        if (!vs.get(i).fire(el.getWidget())) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
        }
        return flag;
    }
}
