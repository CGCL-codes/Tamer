
package org.makagiga.plugins.pdfviewer;

import static org.makagiga.commons.UI._;

import org.makagiga.commons.swing.MLabel;
import org.makagiga.commons.swing.MNumberSpinner;
import org.makagiga.commons.swing.MPanel;

public final class NavigationComponent extends MPanel {

	// private
	
	private MNumberSpinner<Integer> page;
	
	// public
	
	public NavigationComponent() {
		super(5, 5);
	
		page = new MNumberSpinner<>();
		
		addWest(MLabel.createFor(page, _("Page:")));
		addCenter(page);
	}
	
	// package
	
	void init(final Core core) {
		page.setNumber(1);
		page.setRange(1, core.getPageCount());
	}
	
	void update(final Core core) {
		page.setNumber(core.getCurrentPage());
	}

}
