/**
 * Jnap - a java-based Napster clone
   Copyright (C) 1999, 2000  Mike Perham

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.perham.jnap.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import net.perham.jnap.*;
import net.perham.jnap.net.*;
import net.perham.jnap.cmd.*;
import net.perham.util.*;


public class ChatPanel extends JPanel implements PropertyChangeListener, ActionListener, INetworkEventListener, IPanel
{
	INetwork network;
	JFrame frame;

	JTabbedPane channelTabs = new JTabbedPane(JTabbedPane.BOTTOM);
	JPanel channelCommands = new JPanel();
	JButton joinButton = new JButton("Join");
	JComboBox joinCombo = new JComboBox();

	final static String JOIN_COMBO_FIRST_ITEM = "-- select a channel --";
	static Icon alertIcon = null;

	boolean haveChannelList = false;

	public ChatPanel(JFrame f)
	{
		super();

		frame = f;
		network = (INetwork) Main.getConfig().getObjectProperty("net");

		// setup listeners
		network.addNetworkEventListener(this);
		joinButton.addActionListener(this);
		channelTabs.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				// remove alert icon from currently selected tab
				int i = channelTabs.getSelectedIndex();
				if (i != -1) {
					channelTabs.setIconAt(i, null);
				}
			}
		});

		// configure components
		joinCombo.setEditable(false);
		joinCombo.addItem(JOIN_COMBO_FIRST_ITEM);
		joinCombo.setEnabled(false);
		joinButton.setEnabled(false);
		channelCommands.add(joinCombo);
		channelCommands.add(joinButton);

		// setup gui
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		add(channelTabs, BorderLayout.CENTER);
		add(channelCommands, BorderLayout.SOUTH);
	}

	public void shutdown() { }
	public String getName() { return "Chat"; }
	public Component getPanel() { return this; }
	public Icon getIcon() { return null; }

	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		Object src = e.getSource();

		if (src == joinButton) {
			String longName = (String) joinCombo.getSelectedItem();
			if (!longName.startsWith("--")) {
				joinCombo.setSelectedIndex(0);
				joinCombo.removeItem(longName);

				String channelName = (String) joinCombo.getClientProperty(longName);
				RoomPanel cp = new RoomPanel(channelName);
				cp.addPropertyChangeListener(this);
				cp.putClientProperty("longname", longName);
				channelTabs.add(cp);
				channelTabs.setSelectedComponent(cp);
			}
		}
	}

	javax.swing.Timer listTimer = new javax.swing.Timer(5000, new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			network.fireNetworkEvent(INetwork.CHANNEL_LIST_RESULT_COMPLETE, null);
			Debug.log(Debug.CHAT, "Channel list timeout.  %@$# Napster server!");
		}
	});

	public void networkEvent(int cmd, String arg)
	{
		switch (cmd)
		{
			case INetwork.LOGGED_IN:
				haveChannelList = false;
				network.sendCommand(new ChannelListCommand());
				break;
			case INetwork.OFF_LINE:
				haveChannelList = false;
				SwingUtilities.invokeLater(new Runnable() {
					public void run()
					{
						joinCombo.removeAllItems();
						joinCombo.addItem(JOIN_COMBO_FIRST_ITEM);
						joinCombo.setEnabled(false);
						joinButton.setEnabled(false);
					}
				});
				break;
			case INetwork.CHANNEL_LIST_RESULT:
				{
					StringTokenizer st = new StringTokenizer(arg);
					final String channelName = st.nextToken();
					final String channelSize = st.nextToken();
					SwingUtilities.invokeLater(new Runnable() {
						public void run()
						{
							String channelNameNum = channelName + " (" + channelSize + " users)";
							insertChannel(channelName, channelNameNum);
						}
					});
				}
				listTimer.restart();
				break;
			case INetwork.CHANNEL_LIST_RESULT_COMPLETE:
				if (!haveChannelList) {
					haveChannelList = true;
					SwingUtilities.invokeLater(new Runnable() {
						public void run()
						{
							joinCombo.setSelectedIndex(0);
							joinCombo.setEnabled(true);
							joinButton.setEnabled(true);
						}
					});
				}
				listTimer.stop();
				break;
			case INetwork.PRIVATE_MESSAGE:
				{
					StringTokenizer st = new StringTokenizer(arg);
					final String nick = st.nextToken();

					int i = arg.indexOf(' ');
					Debug.assert(i != -1);
					final String msg = arg.substring(i+1);
					msg.trim();

					RoomPanel rp = getPrivateRoom(nick);
					if (rp == null) {
						// create a private room for this incoming message
						rp = new RoomPanel(nick, RoomPanel.PRIVATE_CHAT);

						final RoomPanel r = rp;
						final PropertyChangeListener pcl = this;

						SwingUtilities.invokeLater(new Runnable() {
							public void run()
							{
								r.addPropertyChangeListener(pcl);
								r.putClientProperty("private", nick);
								r.addMessage(nick, msg);

								channelTabs.add(r);
								channelTabs.setSelectedComponent(r);
							}
						});
					}

					// select room where this message will be dropped
					final IPanel p = this;
					SwingUtilities.invokeLater(new Runnable() {
						public void run()
						{
							((IGui) Main.getConfig().getObjectProperty("gui")).notifyPanelActivity(p);
						}
					});
				}
				break;
		}
	}


	void insertChannel(String channelName, String longName)
	{
		int i, l, n;

		// simple sorting algorithm
		for (i = 0, l = joinCombo.getItemCount(), n = 1; i < l; i++) {
			String c = (String) joinCombo.getItemAt(i);
			n = c.compareTo(channelName);
			if (n > 0) {
				break;
			}
		}

		// only unique items
		if (n != 0) {
			joinCombo.putClientProperty(longName, channelName);
			joinCombo.insertItemAt(longName, i);
		}
	}

	public void propertyChange(PropertyChangeEvent e)
	{
		if (e.getPropertyName().equals("CLOSED")) {
			RoomPanel cp = (RoomPanel) e.getSource();
			channelTabs.remove(cp);
			String longName = (String) cp.getClientProperty("longname");
			if (longName != null) {
				insertChannel(cp.getName(), longName);
			}
		}
		else if (e.getPropertyName().equals("PRIVATE")) {
			openPrivateRoom((String) e.getNewValue());
		}
		else if (e.getPropertyName().equals("CHANNEL")) {
			// The setdividerlocation trick in roompanel doesn't work when
			// we do this directly.
			final String channelName = (String) e.getNewValue();
			final ChatPanel pcl = this;
			SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					RoomPanel cp = new RoomPanel(channelName);
					cp.addPropertyChangeListener(pcl);
					cp.putClientProperty("channel", channelName);
					channelTabs.add(cp);
					channelTabs.setSelectedComponent(cp);
				}
			});
		}
		else if (e.getPropertyName().equals("ACTIVITY")) {
			((IGui) Main.getConfig().getObjectProperty("gui")).notifyPanelActivity(this);

			if (alertIcon == null) {
				alertIcon = new ImageIcon(getClass().getResource("images/alert.gif"));
			}

			RoomPanel rp = (RoomPanel) e.getNewValue();
			int i = channelTabs.indexOfComponent(rp);
			if (rp != channelTabs.getSelectedComponent() && i != -1) {
				channelTabs.setIconAt(i, alertIcon);
			}
		}
	}

	public void openPrivateRoom(String nick)
	{
		RoomPanel rp = getPrivateRoom(nick);
		if (rp == null) {
			rp = new RoomPanel(nick, RoomPanel.PRIVATE_CHAT);
			rp.addPropertyChangeListener(this);
			rp.putClientProperty("private", nick);
			channelTabs.add(rp);
		}
		channelTabs.setSelectedComponent(rp);
	}

	RoomPanel getPrivateRoom(String nick)
	{
		Component tabs[] = channelTabs.getComponents();

		for (int i = 0; i < tabs.length; i++) {
			RoomPanel p = (RoomPanel) tabs[i];
			String n = (String) p.getClientProperty("private");
			if (nick.equals(n)) {
				return p;
			}
		}

		return null;
	}
}
