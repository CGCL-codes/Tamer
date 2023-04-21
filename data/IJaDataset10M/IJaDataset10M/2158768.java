package org.libreplan.web.planner.tabs;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import org.apache.commons.lang.Validate;
import org.libreplan.web.planner.tabs.Mode.ModeTypeChangedListener;
import org.zkoss.ganttz.extensions.ITab;
import org.zkoss.zk.ui.Component;

/**
 * @author Óscar González Fernández <ogonzalez@igalia.com>
 *
 */
public class TabOnModeType implements ITab {

    private final Mode mode;

    private final EnumMap<ModeType, ITab> tabs;

    private boolean beingShown = false;

    public static WithType forMode(Mode mode) {
        return new WithType(mode, new EnumMap<ModeType, ITab>(ModeType.class));
    }

    public static class WithType {

        private final Mode mode;

        private EnumMap<ModeType, ITab> tabs;

        private WithType(Mode mode, EnumMap<ModeType, ITab> tabs) {
            this.mode = mode;
            this.tabs = tabs;
        }

        public WithType forType(ModeType modeType, ITab tab) {
            tabs.put(modeType, tab);
            return this;
        }

        public TabOnModeType create() {
            return new TabOnModeType(mode, tabs);
        }
    }

    public TabOnModeType(Mode mode, EnumMap<ModeType, ITab> tabs) {
        Validate.notNull(mode);
        Validate.isTrue(handleAllCases(tabs), "must handle all ModeTypes: " + Arrays.toString(ModeType.values()) + ". It only handles: " + tabs.keySet());
        this.mode = mode;
        this.tabs = new EnumMap<ModeType, ITab>(tabs);
        this.mode.addListener(new ModeTypeChangedListener() {

            @Override
            public void typeChanged(ModeType oldType, ModeType newType) {
                if (beingShown) {
                    changeTab(oldType, newType);
                }
            }
        });
    }

    private void changeTab(ModeType oldType, ModeType newType) {
        ITab previousTab = tabs.get(oldType);
        previousTab.hide();
        ITab newTab = tabs.get(newType);
        newTab.show();
    }

    private boolean handleAllCases(EnumMap<ModeType, ITab> tabs) {
        for (ModeType modeType : ModeType.values()) {
            if (tabs.get(modeType) == null) {
                return false;
            }
        }
        return true;
    }

    private ITab getCurrentTab() {
        return tabs.get(mode.getType());
    }

    @Override
    public void addToParent(Component parent) {
        Collection<ITab> values = tabs.values();
        for (ITab tab : values) {
            tab.addToParent(parent);
        }
    }

    @Override
    public String getName() {
        return getCurrentTab().getName();
    }

    @Override
    public String getCssClass() {
        return getCurrentTab().getCssClass();
    }

    @Override
    public void hide() {
        beingShown = false;
        getCurrentTab().hide();
    }

    @Override
    public void show() {
        beingShown = true;
        getCurrentTab().show();
    }
}
