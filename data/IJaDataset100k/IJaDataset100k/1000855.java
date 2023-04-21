package com.coshx.jprefs.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import com.coshx.jprefs.Profile;
import com.coshx.jprefs.ProfileType;

/**
 * The component for all the preferences with gui components.
 * 
 * @author Ben Taitelbaum
 * @version 1.0
 */
public class PreferencesEditor extends Box {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    private Hashtable<JToggleButton, JComponent> components;

    private Hashtable<JToggleButton, ProfileSelector> profileSelectors;

    private Hashtable<ProfileType, ButtonGroup> buttonGroups;

    private Hashtable<ProfileType, Box> buttonBoxes;

    private Hashtable<ProfileType, Box> contentBoxes;

    /**
	 * Creates a new <code>PreferencesInternalFrame</code>.
	 */
    public PreferencesEditor() {
        super(BoxLayout.Y_AXIS);
        this.components = new Hashtable<JToggleButton, JComponent>();
        this.profileSelectors = new Hashtable<JToggleButton, ProfileSelector>();
        this.buttonGroups = new Hashtable<ProfileType, ButtonGroup>();
        this.buttonBoxes = new Hashtable<ProfileType, Box>();
        this.contentBoxes = new Hashtable<ProfileType, Box>();
        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        ProfileType type = null;
        Iterator<ProfileType> iter = ProfileType.iterator();
        while (iter.hasNext()) {
            type = iter.next();
            buttonGroups.put(type, new ButtonGroup());
            Box buttonsBox = new Box(BoxLayout.Y_AXIS);
            JScrollPane buttonsScrollPane = new JScrollPane(buttonsBox);
            buttonBoxes.put(type, buttonsBox);
            Box contentBox = new Box(BoxLayout.Y_AXIS);
            contentBoxes.put(type, contentBox);
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, buttonsScrollPane, contentBox);
            splitPane.setDividerLocation(200);
            tabbedPane.addTab(type.toString(), splitPane);
        }
        this.add(tabbedPane);
        this.setSize(600, 400);
    }

    public void selectTab(ProfileType type) {
        int idx = tabbedPane.indexOfTab(type.toString());
        if (idx != -1) {
            tabbedPane.setSelectedIndex(idx);
        }
    }

    /**
	 * Sets the selected tab to the <code>type</code> tab, and selects
	 * <code>button</code>.
	 * 
	 * @todo Scroll the buttons scrollpane so that <code>button</code> is
	 *       visible.
	 */
    public void selectTab(ProfileType type, JToggleButton button) {
        selectTab(type);
        if (button != null) {
            button.setSelected(true);
            button.requestFocusInWindow();
        }
    }

    public void addPreferenceComponent(ProfileType profileType, JComponent comp, JToggleButton button) {
        boolean selected = button.isSelected();
        components.put(button, comp);
        buttonGroups.get(profileType).add(button);
        buttonBoxes.get(profileType).add(button);
        button.addItemListener(new PreferenceSetButtonListener(profileType, button, this));
        if (selected) {
            button.setSelected(false);
            button.setSelected(true);
        }
    }

    /**
	 * associates a profile selector with a given button. This selector will
	 * usually be part of the component added with
	 * {@link #addPreferenceComponent}
	 */
    public void setProfileSelector(JToggleButton button, ProfileSelector selector) {
        if (button != null && selector != null) profileSelectors.put(button, selector);
    }

    /**
	 * if there is a profile selector associated with <code>button</code> then
	 * this selector's value will be set to <code>profile</code>. Otherwise,
	 * nothing will happen.
	 */
    public void setProfile(JToggleButton button, Profile profile) {
        if (profileSelectors.containsKey(button) && profile != null) {
            profileSelectors.get(button).setProfile(profile);
        }
    }

    /**
	 * @return the current profile of the <code>ProfileSelector</code>
	 *         associated with <code>button</code>, or <code>null</code> if
	 *         there is no such association.
	 * @see #setProfileSelector
	 * @see #setProfile
	 */
    public Profile getProfile(JToggleButton button) {
        if (profileSelectors.containsKey(button)) return profileSelectors.get(button).getProfile(); else return null;
    }

    private static class PreferenceSetButtonListener implements ItemListener {

        private ProfileType profileType;

        private JToggleButton button;

        private PreferencesEditor owner;

        PreferenceSetButtonListener(ProfileType profileType, JToggleButton button, PreferencesEditor owner) {
            this.profileType = profileType;
            this.button = button;
            this.owner = owner;
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Box box = owner.contentBoxes.get(profileType);
                box.removeAll();
                box.add(owner.components.get(button));
                owner.repaint();
            }
        }
    }
}
