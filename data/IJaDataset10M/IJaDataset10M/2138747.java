package com.bluebrim.layout.impl.client;

import java.awt.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.content.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.observable.*;

/**
 * UI for com.bluebrim.layout.shared.CoLayoutContentIF.
 * See CoPageItemUI for details on supplying a layout editor.
 * 
 * @author: Dennis
 */
public class CoLayoutContentUI extends CoAbstractContentUI implements CoContextAcceptingUI {

    public static final String LAYOUT_TAB = "CoLayoutContentUI.layout_tab";

    private CoPageItemHolderUI m_layoutAreaUI;

    public CoLayoutContentUI(CoObjectIF layoutContent, CoUIContext uiContext) {
        this((CoLayoutEditorDialog) null, uiContext);
        setDomain(layoutContent);
    }

    public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
        return m_layoutAreaUI.getCopyOfCurrentRequiredUIContext();
    }

    public void setUIContext(CoUIContext context) {
        m_layoutAreaUI.setUIContext(context);
    }

    public class MyTabData extends CoAbstractContentUI.MyTabData {

        public MyTabData(CoDomainUserInterface ui) {
            super(ui);
        }

        public void initializeTabData() {
            addTabData(new CoAbstractTabUI.TabData(LAYOUT_TAB, null, CoLayoutClientResources.getName(LAYOUT_TAB)) {

                protected CoDomainUserInterface createUserInterface() {
                    return m_layoutAreaUI;
                }

                public Object getValueFor(CoObjectIF subject) {
                    return subject;
                }
            });
            super.initializeTabData();
        }
    }

    public CoLayoutContentUI(CoLayoutEditorDialog editor, CoUIContext uiContext) {
        super();
        m_layoutAreaUI = new CoPageItemHolderUI(editor, uiContext, null, null) {

            protected Insets getDefaultPanelInsets() {
                return null;
            }
        };
        (new CoDefaultServerObjectListener(m_layoutAreaUI)).initialize();
    }

    protected CoServerTabData getTabData() {
        CoServerTabData tabData = new MyTabData(this);
        tabData.initializeTabData();
        return tabData;
    }

    protected Object[] getTabOrder() {
        return new Object[] { LAYOUT_TAB };
    }
}
