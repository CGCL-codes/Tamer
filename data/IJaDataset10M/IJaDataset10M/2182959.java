/*=====================================================================*\
|*    Copyright 2001 (c) JBrix, Jim Wissner.   All Rights Reserved.    *|
|*    This software is open source, released under the MIT License.    *|
|*    See the bottom of this file for the full text of the licence.    *|
|*_____________________________________________________________________*|
|*                        Email: jim@jbrix.org                         *|
\*=====================================================================*/

package org.jbrix.gui.xform;

import org.jbrix.xml.*;
import org.jbrix.util.*;
import org.w3c.dom.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *   This is an XComponent that uses a JComboBox which provides the
 *   user with a selection of possible items to choose from.
 *
 *   <p/>The following XML example shows the xforms syntax of this component:
 *
 *<XML-EXAMPLE>
 *<combo ref="state">               <---- @ref: required
 *    <caption>State:</caption>    <---- caption: required
 *    <choices>                    <---- choices: required
 *        <item                    <---- choices/item: 1 or more required
 *            value="1">           <---- choices/item/@value: optional
 *            AK
 *        </item>
 *        . . . (more items) . . .
 *    </choices>
 *</combo>
 *</XML-EXAMPLE>
 *
 */

/* gotta finish this first:
 *        <select                  <---- select: optional
 *            ref="/customers/customer"
 *            label="{lastName}, {firstName}"
 *            value="{customerID}"/>
*/
public class XComboField extends XComponent
	implements ActionListener, FocusListener
{
	private static XMLDriver xmlDriver = XMLDriver.getDefaultDriver();
	private JComboBox comboField;
	private Node node;

	/** The "item" used in the combo box when there is no data node value. */
	public static final String NOT_SPECIFIED_ITEM
		= "(Not specified - Choose from list)";

	private Hashtable explicitValues = new Hashtable();
	private Hashtable reverseExplicitValues = new Hashtable();
	private Hashtable selectRefsByKey = new Hashtable();
	private Vector items = new Vector();
	private boolean ignoreComboChanges = false;

	public void clear() {
		node = null;
		if (comboField.getItemCount() > 0) comboField.setSelectedIndex(0);
		comboField.setEnabled(false);
	}

	public void refreshFromDataNodes() {
		Node n = node;
		clear();
		addDataNode(n);
	}

	public void setEnabled(boolean flag) {
		comboField.setEnabled(flag);
	}

	public void addDataNode(Node node) {
		this.node = node;
		comboField.setEnabled(node != null);
		if (node == null) {
			return;
		}
		ignoreComboChanges = true;
		String s = XMLUtil.getStringValue(node);
		String item = null;
		comboField.removeAllItems();
		int i,n = items.size();
		for (i = 0; i < n; i++) {
			String entry = (String)(items.elementAt(i));
			if (entry.startsWith("X-SELECT.")) {
				String ref = (String)(selectRefsByKey.get(entry));
				if (ref != null) {
					Node[] nodes = XMLUtil.select(node,ref);
					for (int j = 0; j < nodes.length; j++) {
						comboField.addItem(XMLUtil.getStringValue(nodes[j]));
					}
				}
			} else {
				comboField.addItem(entry);
			}
		}
		if (s != null) {
			item = (String)(explicitValues.get(s));
			if (item == null) {
				item = getItemNamed(s);
			}
		}
		if (item == null) {
			if (s != null && (s.equals(NOT_SPECIFIED_ITEM) || s.equals(""))) {
				item = NOT_SPECIFIED_ITEM;
				if (comboField.getItemCount() < 1) {
					comboField.insertItemAt(NOT_SPECIFIED_ITEM,0);
				} else if (!comboField.getItemAt(0).equals(
					NOT_SPECIFIED_ITEM)) {
					comboField.insertItemAt(NOT_SPECIFIED_ITEM,0);
				}
			} else {
			}
		}
		if (item != null && !item.equals(NOT_SPECIFIED_ITEM)) {
			if (comboField.getItemCount() > 0) {
				if (comboField.getItemAt(0).equals(NOT_SPECIFIED_ITEM)) {
					comboField.removeItemAt(0);
				}
			}
		} else {
			String dflt = XMLUtil.getStringValue(getComponentElement(),"default");
			if ((!XMLUtil.isAttached(node) || item == null)
				&& (item == null || item.startsWith("(new"))
				&& dflt != null) {
				item = dflt.trim();
				setValueOfNode(node,item);
				fireEditedEvent(node);
// hm. just thinking.  what if fireEditedEvent is ever called from an xcomp
// with a node other than the original one use to map them in xform??!
			} else if (comboField.getItemCount() < 1
				|| !comboField.getItemAt(0).equals(NOT_SPECIFIED_ITEM)) {
				comboField.insertItemAt(NOT_SPECIFIED_ITEM, 0);
			}
		}
		if (item != null) {
			comboField.setSelectedItem(item);
		} else {
			comboField.setSelectedIndex(0);
		}
		ignoreComboChanges = false;
	}

	public void configure(Element componentElement) {
		items.removeAllElements();
		comboField.removeAllItems();
		explicitValues.clear();
		setLabel(getCaption());
		String editable = componentElement.getAttribute("editable");
		if (editable != null && editable.toUpperCase().equals("YES")) {
			comboField.setEditable(true);
		} else {
			comboField.setEditable(false);
		}
		Node choicesNode
			= XMLUtil.selectFirstElement(componentElement,"choices");
		if (choicesNode == null) {
			// WHAT IF NO CHOICES SUBNODE?!
			return;
		}
		NodeList nodes = choicesNode.getChildNodes();
		Node node;
		String value;
		int itemNum = 0;
		for (int i = 0; i < nodes.getLength(); i++) try {
			node = nodes.item(i);
			if (node.getNodeName().equals("item")) {
				value = node.getFirstChild().getNodeValue();
				comboField.addItem(value);
				items.addElement(value);
				if (i == 0) comboField.setSelectedItem(value);
				String expVal = XMLUtil.getAttribute(node,"value");
				if (expVal != null) {
					explicitValues.put(expVal,value);
					reverseExplicitValues.put(value,expVal);
				}
			} else if (node.getNodeName().equals("list")) {
				String cacheName = XMLUtil.getAttribute(node,"cache");
				if (cacheName != null) {
					Cache cache = Cache.getCache(cacheName);
					if (cache != null) {
						Enumeration enum = cache.getKeys();
						while (enum.hasMoreElements()) {
							value = "" + enum.nextElement();
							comboField.addItem(value);
							items.addElement(value);
							if (comboField.getItemCount() == 1) {
								comboField.setSelectedItem(value);
							}
						}
					}
				}
			} else if (node.getNodeName().equals("select")) {
				String key = "X-SELECT." + (itemNum++);
				String ref = XMLUtil.getAttribute(node,"ref");
				items.addElement(key);
				selectRefsByKey.put(key,ref);
			}
		} catch (Exception ex) {
			System.err.print("Warning: couldn't add option (" + (i + 1));
			System.err.println(") to field '" + getCaption() + "'");
		}
	}

	/**
	 *   This method is called when the JComboBox selection has changed,
	 *   which is reflected in the component's data node(s) as necessary.
	 *   @param event The action event.
	 */
	public void actionPerformed(ActionEvent event) {
		if (ignoreComboChanges) {
			return;
		}
		Object obj = comboField.getSelectedItem();
		// I think we can safely do this, because if the value is set and
		// focus lost, then this won't exist when we come back.
		if (obj.equals(NOT_SPECIFIED_ITEM)) {
			return;
		}
		String text = (obj != null? obj.toString() : "");
		if (node == null || text == null) return;
		String s = (String)(reverseExplicitValues.get(text));
		if (s != null) text = s;
		String value = XMLUtil.getStringValue(node);
		if ( (value == null && text.length() > 0) || 
		     (value != null && !value.equals(text))) {
			setValueOfNode(node,text);
			fireEditedEvent(node);
		}
		if (comboField.getItemCount() > 0) {
			if (comboField.getItemAt(0).equals(NOT_SPECIFIED_ITEM)) {
				comboField.removeItemAt(0);
			}
		}
	}

	/**
	 *   Invoked when this component gains the keyboard focus.
	 *
	 *   @param event The focus event.
	 */
	public void focusGained(FocusEvent event) {
	}

	/**
	 *   Invoked when a component loses the keyboard focus; any
	 *   changes to this component's value are flushed back to the
	 *   corresponding data node(s).
	 *
	 *   @param event The focus event.
	 */
	public void focusLost(FocusEvent event) {
		actionPerformed(null);
	}

	/**
	 *   This method searches the combo's items for <code>value</code>;
	 *   if it is found it is returned, otherwise null is returned.
	 *
	 *   @param value The value to be looked for.
	 *   @return <code>value</code> if it exists in the combobox,
	 *   null otherwise.
	 */
	private String getItemNamed(String value) {
		int n = comboField.getItemCount();
		for (int i = 0; i < n; i++) {
			if (value.equals(comboField.getItemAt(i))) {
				return value;
			}
		}
		return null;
	}

	/**
	 *   This method sets the String value of <code>aNode</code> to
	 *   <code>value</code>. If <code>aNode</code> is an attribute, its
	 *   value is set, if it is an element, the first Text child of it is
	 *   set (and added if it doesn't yet exist).
	 *
	 *   @param aNode The node to set the value of. 
	 *   @param value The value to set <CODE>aNode</CODE> to.
	 */
	private void setValueOfNode(Node aNode,String value) {
		if (aNode instanceof Attr) {
			aNode.setNodeValue(value);
		} else {
			Node kid = aNode.getFirstChild();
			if (kid instanceof Text) {
				kid.setNodeValue(value);
			} else {
				aNode.appendChild(
					aNode.getOwnerDocument().createTextNode(value));
			}
		}
	}

	/**
	 *   Constructs an unconfigured instance with <code>label</code> for
	 *   its label text.
	 *
	 *   @param label The label for this instance.
	 */
	public XComboField(String label) {
		comboField = new JComboBox();
		comboField.addFocusListener(this);
		Component[] comps = comboField.getComponents();
		for (int i = 0; i < comps.length; i++) {
			comps[i].addFocusListener(this);
		}
		setLayout(new BorderLayout());
		add("Center",comboField);
		setLabel(label);
		setPreferredSize((new JTextField()).getPreferredSize());
	}

	/**
	 *   The default constructor; creates an unconfigured instance with
	 *   "New field:" for its label. All XComponent subclasses <b>must</b>
	 *   provide a public default constructor that takes no arguments.
	 */
	public XComboField() {
		this("New field:");
	}

	/**
	 *   This method returns the JComboBox of this XComboField instance.
	 *
	 *   @return This component's JComboBox.
	 */
	public JComboBox getComboBox() {
		return comboField;
	}

}

/*=====================================================================*\
|*                                                                     *|
|*  Copyright 2001 (c) JBrix, Jim Wissner.   All Rights Reserved.      *|
|*                                                                     *|
|*  Permission is hereby granted, free of charge, to any person        *|
|*  obtaining a copy of this software and associated documentation     *|
|*  files (the "Software"), to deal in the Software without            *|
|*  restriction, including without limitation the rights to use,       *|
|*  copy, modify, merge, publish, distribute, sublicense, and/or sell  *|
|*  copies of the Software, and to permit persons to whom the Software *|
|*  is furnished to do so, subject to the following conditions:        *|
|*                                                                     *|
|*  The above copyright notice and this permission notice shall be     *|
|*  included in all copies or substantial portions of the Software.    *|
|*                                                                     *|
|*  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,    *|
|*  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES    *|
|*  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND           *|
|*  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT        *|
|*  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,       *|
|*  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING       *|
|*  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR      *|
|*  OTHER DEALINGS IN THE SOFTWARE.                                    *|
|*                                                                     *|
\*=====================================================================*/

