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

package org.makagiga.tools.summary;

import static org.makagiga.commons.UI._;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.makagiga.MainWindow;
import org.makagiga.Sidebar;
import org.makagiga.Vars;
import org.makagiga.commons.Loop;
import org.makagiga.commons.MAction;
import org.makagiga.commons.MArrayList;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.MStringBuilder;
import org.makagiga.commons.TK;
import org.makagiga.commons.Tuple;
import org.makagiga.commons.UI;
import org.makagiga.commons.annotation.Uninstantiable;
import org.makagiga.commons.swing.MNotification;
import org.makagiga.commons.swing.MSystemTray;
import org.makagiga.fs.MetaInfo;
import org.makagiga.todo.Task;
import org.makagiga.todo.TaskState;

final class Reminder {

	// private

	private static final MLogger log = MLogger.get("reminder");
	private static MNotification.Message message;
	private static Tuple.Two<MetaInfo, Task> recentTaskInfo;
	
	// package
	
	static boolean hasBlockedAlarms;

	// public
	
	public synchronized static void hideNotification() {
		hasBlockedAlarms = false;
		recentTaskInfo = null;
		if (message != null) {
			//log.debug("Hide message");
			MNotification.getInstance().hideMessage(message);
		}
/*		else {
			log.debug("No message to hide");
		}
*/
	}

	public synchronized static void showNotification(final Map<TaskState, Set<Tuple.Two<MetaInfo, Task>>> alarmMap, final boolean systemTrayOnly) {
		boolean mainWindowLocked = MainWindow.getInstance().isLocked();
		recentTaskInfo = null;
		MArrayList<Tuple.Two<MetaInfo, Task>> recentAlarms = new MArrayList<>();
		for (Set<Tuple.Two<MetaInfo, Task>> alarmSet : alarmMap.values())
			recentAlarms.addAll(alarmSet);
		
		hasBlockedAlarms = !recentAlarms.isEmpty() && mainWindowLocked;

		// update system tray tool tip
		// do not initialize MSystemTray class if tray icon is disabled
		if (UI.systemTray.get() && !mainWindowLocked) {
			if (recentAlarms.isEmpty()) {
				MSystemTray.setToolTip(null);
			}
			else {
// TODO: show alarm count in system tray icon
				MSystemTray.setToolTip(_("Tasks ({0})", recentAlarms.size()));
			}
		}
		
		if (systemTrayOnly)
			return;
		
		// still loading tasks...
		if (!getSummaryDataPrivileged().isIdle()) {
			//log.debug("Still loading tasks. Exit.");
		
			return;
		}
		
		// locked
		if (mainWindowLocked) {
			//log.debug("Main window is locked. Exit.");
		
			return;
		}

		// disabled
		if (!Vars.alarmEnabled.get())
			return;

		// no more alarms, hide message
		if (recentAlarms.isEmpty()) {
			//log.debug("No more alarms. Exit.");
			hideNotification();
			
			return;
		}

		// show message
		
		//log.debugFormat("Task info: %s", recentTaskInfo);

		String title = _("Tasks ({0})", recentAlarms.size());
		
		MStringBuilder html = new MStringBuilder();
		recentAlarms.sort(new Comparator<Tuple.Two<MetaInfo, Task>>() {
			@Override
			public int compare(final Tuple.Two<MetaInfo, Task> o1, final Tuple.Two<MetaInfo, Task> o2) {
				Task t1 = o1.get2();
				Task t2 = o2.get2();
				
				return t1.getDateTime().compareTo(t2.getDateTime()) * -1;
			}
		} );
		recentTaskInfo = recentAlarms.getFirst();
		
		int max = 3;
		for (Loop<Tuple.Two<MetaInfo, Task>> i : Loop.each(recentAlarms)) {
			MetaInfo metaInfo = i.v().get1();
			Task task = i.v().get2();
			html.escapeXML(TK.rightSqueeze(metaInfo.toString(), 25));
			html.append(": ");
// TODO: common code (task list HTML code)
			html.escapeXML(task.formatSummary(25));
			html.append(" [");
			html.escapeXML(task.formatDate());
			html.append(']');
			
			// max 3 rows...
			if (i.i() == max - 1) {
				if (!i.isLast()) {
					html.append(" (");
					html.append(_("{0} more", "<b>" + (recentAlarms.size() - max) + "</b>"));
					html.append(')');
				}
				
				break; // for
			}
			
			if (!i.isLast())
				html.append("<br>");
		}

		String text = html.toSwingHTML();

		if (message == null) {
			//log.debugFormat("Creating new message: %d", totalAlarmCount);
			
			Action action = new Action();
			message = new MNotification.Message(action) {
				@Override
				protected void onClose() {
					synchronized (Reminder.class) {
						Reminder.message = null;
					}
				}
			};
			message.setText(text);
			message.setTitle(title);
			MNotification.showMessage(message);
			message.toFront();
		}
		else {
			//log.debugFormat("Reusing existing message: %d", totalAlarmCount);
			
			message.setText(text);
			message.setTitle(title);
			if (!message.toFront())
				MNotification.getInstance().updateMessage(message);
		}
	}

	// private

	@Uninstantiable
	private Reminder() { }

	private static SummaryData getSummaryDataPrivileged() {
		SecurityManager sm = System.getSecurityManager();

		if (sm == null)
			return SummaryData.getInstance();

		return AccessController.doPrivileged(new PrivilegedAction<SummaryData>() {
			@Override
			public SummaryData run() {
				return SummaryData.getInstance();
			}
		} );
	}

	// private classes
	
	private static final class Action extends MAction {
		
		// public
		
		@Override
		public void onAction() {
			MainWindow.getInstance().restore();
			Sidebar.getInstance().goTo(Sidebar.Tab.SUMMARY);
			
			if (Reminder.recentTaskInfo == null)
				return;
			
			MetaInfo metaInfo = Reminder.recentTaskInfo.get1();
			Task task = Reminder.recentTaskInfo.get2();
			
			// select task
			boolean found = false;
			for (Map.Entry<TaskState, SummaryData.Node> taskStateEntry : SummaryData.getInstance()) {
				if (found)
					break; // for
			
				TaskState taskState = taskStateEntry.getKey();
				if (SummaryData.isAlarmTrigger(taskState)) {
					for (SummaryData.Node metaInfoNode : taskStateEntry.getValue()) {
						if (found)
							break; // for

						if (metaInfo.equals(metaInfoNode.getMetaInfo())) {
							for (SummaryData.Node taskNode : metaInfoNode) {
								if (task.equals(taskNode.getTask())) {
									SummaryPanel.getInstance().getTree().selectItem(taskNode, true);
									found = true;
								
									break; // for
								}
							}
						}
					}
				}
			}
		}
		
		// private
		
		private Action() {
			super("", "ui/warning");
		}
		
	}

}
