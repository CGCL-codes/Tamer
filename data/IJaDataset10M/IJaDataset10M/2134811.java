package net.sf.xws4j.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import net.sf.xws4j.Activator;
import net.sf.xws4j.preferences.PreferenceConstants;

/**
 * 
 * This file is part of the Eclipse xws4j Plug-in.
 * 
 * Copyright (C) 2008 Johannes Wagener
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 * 
 * @author Johannes Wagener
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("The Extensible Messaging and Presence Protocol (XMPP) " + "is an open XML technology for real-time communications.\n");
    }

    /**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
    public void createFieldEditors() {
        Composite composite_all, composite_general, composite_experts, composite_debug;
        composite_all = createComposite(getFieldEditorParent());
        composite_general = createComposite(composite_all);
        createComposite(composite_all);
        composite_experts = createComposite(createGroupComposite(composite_all, "Advanced options"));
        createComposite(composite_all);
        composite_debug = createComposite(composite_all);
        addField(new StringFieldEditor(PreferenceConstants.P_STRING_SERVER, "&Server:", composite_general));
        addField(new StringFieldEditor(PreferenceConstants.P_STRING_JID, "&Jabber ID:", composite_general));
        new Label(composite_general, SWT.LEFT);
        Label example_jid = new Label(composite_general, SWT.LEFT);
        example_jid.setText("  Example: user@server.example.com");
        example_jid.setEnabled(false);
        StringFieldEditor strFieldEditor = new StringFieldEditor(PreferenceConstants.P_STRING_PASSWORD, "&Password:", composite_general);
        strFieldEditor.getTextControl(composite_general).setEchoChar('*');
        addField(strFieldEditor);
        addField(new StringFieldEditor(PreferenceConstants.P_STRING_RESOURCE, "&Resource:", composite_experts));
        addField(new StringFieldEditor(PreferenceConstants.P_STRING_SERVERPORT, "S&erver Port:", composite_experts));
        addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN_LOGDEFAULT, "&Activate xws4j debug mode by default", composite_debug));
    }

    public void init(IWorkbench workbench) {
    }

    private Composite createComposite(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout());
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        comp.setLayoutData(gd);
        return comp;
    }

    private Group createGroupComposite(Composite parent, String text) {
        Group group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout());
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        group.setText(text);
        return group;
    }
}
