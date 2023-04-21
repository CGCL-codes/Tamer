package org.primordion.user.app.${appName};

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.primordion.busapp.desktop.View;
import org.primordion.xholon.base.IXholon;

/**
 * Commondesktop View
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on November 9, 2009)
*/
public class CommondesktopView extends View {
	
	IXholon desktopPaneNode = null;
	
	/*
	 * @see org.primordion.xholon.base.Xholon#act()
	 */
	public void act()
	{
		if (getApp().getTimeStep() < 2) {
			IXholon menuItemNode = getXPath().evaluate("descendant::JMenu[@roleName='Window']", this);
			if (menuItemNode != null) {
				buildWindowMenu(menuItemNode);
			}
		}
		super.act();
	}
	
	/**
	 * Should the application exit?
	 * @return true or false
	 */
	public boolean shouldExitApplication()
	{
		int answer = JOptionPane.showConfirmDialog(
				null, "Do you want to exit from the application?");
		if (answer == JOptionPane.YES_OPTION) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Reset the Window menu.
	 * Because of the work around described in buildWindowMenu(), this method should not be used.
	 * @param menuItemNode
	 */
	public void resetWindowMenu(IXholon menuItemNode)
	{
		return;
		//if (menuItemNode == null) {return;}
		//JMenu jMenu = (JMenu)menuItemNode.getVal_Object();
		//if (jMenu == null) {return;}
		//jMenu.removeAll();
	}
	
	/**
	 * Build a Window menu that shows all the JInternalFrame instances in the Common Desktop.
	 * The JMenu refuses to validate itself,
	 * so by default it won't display anything the first time it's selected.
	 * I'm keeping track of how many items there are, and only updating when necessary, as a work around.
	 * @param menuItemNode 
	 */
	public void buildWindowMenu(IXholon menuItemNode)
	{
		if (menuItemNode == null) {return;}
		JMenu jMenu = (JMenu)menuItemNode.getVal_Object();
		if (jMenu == null) {return;}
		if (desktopPaneNode == null) {
			desktopPaneNode = getXPath().evaluate("descendant::JFrame/JDesktopPane", this);
			if (desktopPaneNode == null) {return;}
		}
		JDesktopPane jDesktopPane = (JDesktopPane)desktopPaneNode.getVal_Object();
		if (jDesktopPane == null) {return;}
		JInternalFrame[] frameArray = jDesktopPane.getAllFrames();
		//System.out.println("Count of components in jMenu: " + jMenu.getPopupMenu().getComponentCount());
		if (jMenu.getPopupMenu().getComponentCount() == frameArray.length) {
			// the menu is already populated, so there's nothing left to do
			return;
		}
		else {
			jMenu.removeAll();
		}
		for (int i = 0; i < frameArray.length; i++) {
			//System.out.println(frameArray[i].getTitle());
			ChildMenuItem jMenuItem = new ChildMenuItem(frameArray[i]);
			jMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					JInternalFrame frame = ((ChildMenuItem)event.getSource()).getFrame();
					frame.moveToFront();
					try {
						frame.setSelected(true);
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}
				}
			});
			jMenuItem.setIcon(frameArray[i].getFrameIcon());
			jMenu.add(jMenuItem);
		}
	}
	
	class ChildMenuItem extends JMenuItem
	{
		private static final long serialVersionUID = 1L;
		private JInternalFrame frame;
		
		public ChildMenuItem(JInternalFrame frame) {
			super(frame.getTitle());
			this.frame = frame;
		}
		
		public JInternalFrame getFrame()
		{
			return frame;
		}
	}
	
	protected IXholon getDataFromPanel() {return null;}

	protected void setDataToPanel(IXholon recordArg) {}
	
}