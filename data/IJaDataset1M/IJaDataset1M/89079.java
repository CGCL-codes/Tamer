// Copyright 2008 Konrad Twardowski
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.makagiga.plugins.pastebin;

import static org.makagiga.commons.UI._;

import javax.swing.JComponent;

import org.makagiga.commons.Attributes;
import org.makagiga.commons.BooleanProperty;
import org.makagiga.commons.StringProperty;
import org.makagiga.commons.form.PropertyPanel;
import org.makagiga.commons.swing.MComboBox;
import org.makagiga.commons.swing.MEditorPane;
import org.makagiga.commons.swing.MPanel;
import org.makagiga.commons.swing.MScrollPane;
import org.makagiga.web.WebService;

public abstract class PastebinService extends WebService {
	
	// public
	
	public static final String AUTHOR_PROPERTY = "author";
	public static final String LINK_PROPERTY = "link";
	public static final String PRIVATE_PROPERTY = "private";
	public static final String SYNTAX_PROPERTY = "syntax";
	public static final String TEXT_PROPERTY = "text";
	
	// protected
	
	protected BooleanProperty privatePaste = new BooleanProperty();
	protected MComboBox<SyntaxList.Item> syntax;
	protected StringProperty author = new StringProperty("");

	// private

	private String syntaxFile;
	
	// public
	
	public PastebinService(final String displayName, final String shortDescription) {
		super(displayName, shortDescription);
	}
	
	@Override
	public JComponent createMainComponent(final Attributes<String, Object> properties) {
		PropertyPanel p = new PropertyPanel(0);

		p.bind(author, _("Author:"));
		
		syntax = new MComboBox<>();
		if (syntaxFile != null) {
			syntax.addAllItems(SyntaxList.read(syntaxFile));
			p.addGap();
			p.add(MPanel.createHLabelPanel(syntax, _("Syntax Highlight:")));
		}
		
		p.bind(privatePaste, _("Private Paste"));
		
		p.addGap();

		String text = properties.get("text", null);
		MEditorPane preview = MEditorPane.newPlainText(text, false);

		MScrollPane previewScrollPane = new MScrollPane(preview);
		previewScrollPane.setMaximumHeight(200);
		String info = properties.get("info", null);
		p.add(MPanel.createVLabelPanel(previewScrollPane, _("Preview: {0}", info)));
		
		return p;
	}
	
	@Override
	public void updateWebProperties(final Attributes<String, Object> properties) {
		properties.set(AUTHOR_PROPERTY, author.get());
		properties.set(PRIVATE_PROPERTY, privatePaste.get());
		properties.set(SYNTAX_PROPERTY, syntax.isEmpty() ? null : syntax.getSelectedItem().getValue());
	}

	// protected

	protected void setSyntaxFile(final String value) { syntaxFile = value; }

}
