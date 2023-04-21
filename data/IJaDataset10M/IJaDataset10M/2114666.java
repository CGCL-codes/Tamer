package manager.ui.app.inspect;

import prisms.arch.PrismsSession;
import prisms.arch.event.PrismsEvent;
import prisms.records.HistoryViewer.TimeZoneType;
import prisms.ui.tree.CategoryNode;
import prisms.util.ArrayUtils;
import prisms.util.ProgramTracker;
import prisms.util.ProgramTracker.TrackNode;
import prisms.util.preferences.Preference;

/** A tree that displays performance data selected by the user */
public class PerformanceDisplayTree extends prisms.ui.tree.DataTreeMgrPlugin {

    Preference<Float> theDisplayPref;

    Preference<Float> theAccentPref;

    Preference<TimeZoneType> theTimeZonePref;

    Preference<String> theTimeIntSelectPref;

    long theTotalTime;

    long theSnapshotTime;

    prisms.util.TrackerSet.TrackConfig theSelection;

    ProgramTracker theCurrentValue;

    float theDisplayThresh;

    float theAccentThresh;

    TimeZoneType theTimeZone;

    @Override
    public void initPlugin(PrismsSession session, prisms.arch.PrismsConfig config) {
        super.initPlugin(session, config);
        theDisplayPref = new Preference<Float>("Performance Display", "Display Threshold", Preference.Type.PROPORTION, Float.class, true);
        theDisplayPref.setDescription("The percentage of the total interval below which tasks" + " will not be displayed in performance data");
        theAccentPref = new Preference<Float>("Performance Display", "Accent Threshold", Preference.Type.PROPORTION, Float.class, true);
        theAccentPref.setDescription("The percentage of the total interval above which tasks" + " will be displayed in red");
        theTimeZonePref = new Preference<TimeZoneType>("Performance Display", "Local Time", Preference.Type.ENUM, TimeZoneType.class, true);
        theTimeZonePref.setDescription("Sets whether performance data will be displayed in" + " local time or GMT");
        theTimeIntSelectPref = new Preference<String>("Performance Display", "Time Interval Selection", Preference.Type.STRING, String.class, false);
        theTimeIntSelectPref.setDescription("Stores the user's time interval choice so they don't" + " have to re-choose that time interval every time");
        Float disp = session.getPreferences().get(theDisplayPref);
        if (disp != null) theDisplayThresh = disp.floatValue(); else {
            theDisplayThresh = .005f;
            session.getPreferences().set(theDisplayPref, Float.valueOf(theDisplayThresh));
        }
        Float accent = session.getPreferences().get(theAccentPref);
        if (accent != null) theAccentThresh = accent.floatValue(); else {
            theAccentThresh = .08f;
            session.getPreferences().set(theAccentPref, Float.valueOf(theAccentThresh));
        }
        theTimeZone = session.getPreferences().get(theTimeZonePref);
        if (theTimeZone == null) {
            theTimeZone = TimeZoneType.GMT;
            session.getPreferences().set(theTimeZonePref, theTimeZone);
        }
        session.addEventListener("preferencesChanged", new prisms.arch.event.PrismsEventListener() {

            public void eventOccurred(PrismsSession session2, prisms.arch.event.PrismsEvent evt) {
                if (!(evt instanceof prisms.util.preferences.PreferenceEvent)) return;
                prisms.util.preferences.PreferenceEvent pEvt = (prisms.util.preferences.PreferenceEvent) evt;
                if (pEvt.getPreference().equals(theDisplayPref)) {
                    theDisplayThresh = ((Float) pEvt.getNewValue()).floatValue();
                    setPerformanceData(theCurrentValue);
                } else if (pEvt.getPreference().equals(theAccentPref)) {
                    theAccentThresh = ((Float) pEvt.getNewValue()).floatValue();
                    setPerformanceData(theCurrentValue);
                } else if (pEvt.getPreference().equals(theTimeZonePref)) {
                    theTimeZone = (TimeZoneType) pEvt.getNewValue();
                    setPerformanceData(theCurrentValue);
                }
            }

            @Override
            public String toString() {
                return "Manager Performance Display Preference Applier";
            }
        });
        session.addEventListener("showPerformanceData", new prisms.arch.event.PrismsEventListener() {

            public void eventOccurred(PrismsSession session2, PrismsEvent evt) {
                refresh();
            }

            @Override
            public String toString() {
                return "Manager Performance Data Shower";
            }
        });
        CategoryNode root = new CategoryNode(this, null, "No Data Selected");
        root.setIcon("manager/time");
        root.setChildren(new TrackTreeNode[0]);
        root.addAction(new javax.swing.AbstractAction("Refresh") {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshInternal();
            }
        });
        setRoot(root);
        session.addPropertyChangeListener(manager.app.ManagerProperties.performanceData, new prisms.arch.event.PrismsPCL<ProgramTracker>() {

            public void propertyChange(prisms.arch.event.PrismsPCE<ProgramTracker> evt) {
                setPerformanceData(evt.getNewValue());
            }

            @Override
            public String toString() {
                return "Manager Performance Data Display Tree";
            }
        });
    }

    void refresh() {
        PrismsEvent evt = new PrismsEvent("getPerformanceOptions");
        getSession().fireEvent(evt);
        final prisms.util.TrackerSet.TrackConfig[] configs = (prisms.util.TrackerSet.TrackConfig[]) evt.getProperty("options");
        if (configs == null || configs.length == 0) {
            getSession().getUI().error("No performance data available");
            return;
        }
        final String[] options = new String[configs.length];
        for (int o = 0; o < options.length; o++) options[o] = prisms.util.PrismsUtils.printTimeLength(configs[o].getKeepTime());
        final String preSelectOption = getSession().getPreferences().get(theTimeIntSelectPref);
        int selIdx = ArrayUtils.indexOf(options, preSelectOption);
        if (selIdx < 0) selIdx = 0;
        prisms.ui.UI.SelectListener sl = new prisms.ui.UI.SelectListener() {

            public void selected(String option) {
                if (option == null) return;
                theSelection = configs[ArrayUtils.indexOf(options, option)];
                PrismsEvent evt2 = new PrismsEvent("getPerformanceData");
                evt2.setProperty("config", theSelection);
                getSession().fireEvent(evt2);
                theSnapshotTime = System.currentTimeMillis();
                getSession().setProperty(manager.app.ManagerProperties.performanceData, (ProgramTracker) evt2.getProperty("data"));
                if (!option.equals(preSelectOption)) getSession().getPreferences().set(theTimeIntSelectPref, option);
            }
        };
        getSession().getUI().select("Choose a time interval", options, selIdx, sl);
    }

    void refreshInternal() {
        PrismsEvent evt = new PrismsEvent("getPerformanceData");
        evt.setProperty("config", theSelection);
        theSnapshotTime = System.currentTimeMillis();
        getSession().fireEvent(evt);
        getSession().setProperty(manager.app.ManagerProperties.performanceData, (ProgramTracker) evt.getProperty("data"));
    }

    void setPerformanceData(ProgramTracker tracker) {
        theCurrentValue = tracker;
        final CategoryNode root = (CategoryNode) getRoot();
        theTotalTime = 0;
        if (tracker == null) {
            root.setText("No Data Selected");
            root.setChildren(new TrackTreeNode[0]);
        } else {
            for (TrackNode track : tracker.getData()) theTotalTime += track.getLength();
            long actualLength = 0;
            if (tracker.getData().length > 0) actualLength = theSnapshotTime - tracker.getData()[0].getFirstStart();
            final long displayThresh = Math.round(theDisplayThresh * actualLength);
            StringBuilder text = new StringBuilder();
            text.append(tracker.getName()).append("--Actual Interval: ");
            prisms.util.PrismsUtils.printTimeLength(actualLength, text, true);
            text.append(" Snapshot from ").append(prisms.util.PrismsUtils.TimePrecision.SECONDS.print(theSnapshotTime, theTimeZone == TimeZoneType.LOCAL));
            root.setText(text.toString());
            root.setChildren(ArrayUtils.adjust((TrackTreeNode[]) root.getChildren(), tracker.getData(), new ArrayUtils.DifferenceListener<TrackTreeNode, TrackNode>() {

                public boolean identity(TrackTreeNode o1, TrackNode o2) {
                    return o1.getTrackNode().getName() == o2.getName();
                }

                public TrackTreeNode added(TrackNode o, int mIdx, int retIdx) {
                    if (o.getLength() < displayThresh) return null;
                    return new TrackTreeNode(root, o);
                }

                public TrackTreeNode removed(TrackTreeNode o, int oIdx, int incMod, int retIdx) {
                    return null;
                }

                public TrackTreeNode set(TrackTreeNode o1, int idx1, int incMod, TrackNode o2, int idx2, int retIdx) {
                    if (o2.getLength() < displayThresh) return null;
                    o1.setTrackNode(o2);
                    return o1;
                }
            }));
        }
        root.changed(true);
    }

    class TrackTreeNode extends CategoryNode {

        private TrackNode theTrackNode;

        TrackTreeNode(CategoryNode parent, TrackNode track) {
            super(PerformanceDisplayTree.this, parent, track.getName());
            setTrackNode(track);
        }

        TrackNode getTrackNode() {
            return theTrackNode;
        }

        void setTrackNode(TrackNode node) {
            theTrackNode = node;
            final long displayThresh = Math.round(theDisplayThresh * (theSnapshotTime - theCurrentValue.getData()[0].getFirstStart()));
            long lastTime = 0;
            if (getParent() instanceof TrackTreeNode) lastTime = ((TrackTreeNode) getParent()).getTrackNode().getFirstStart();
            StringBuilder text = new StringBuilder();
            text.append(node.getName());
            if (node.getCount() > 1) {
                text.append(" (x").append(node.getCount());
                if (node.unfinished > 0) text.append(", ").append(node.unfinished).append(" un");
                text.append(')');
            }
            text.append(" @");
            TrackNode.printTime(node.getFirstStart(), lastTime, text, theTimeZone == TimeZoneType.LOCAL);
            text.append(": ");
            long localTime = node.getLocalLength(null);
            prisms.util.PrismsUtils.printTimeLength(localTime, text, true);
            if (localTime > 0) text.append(" (").append(ProgramTracker.PERCENT_FORMAT.format(localTime * 100.0 / theTotalTime)).append("%)");
            setText(text.toString());
            text.setLength(0);
            long realLength = node.getRealLength();
            prisms.util.PrismsUtils.printTimeLength(realLength, text, true);
            text.append(" total");
            if (realLength > 0) text.append(" (").append(ProgramTracker.PERCENT_FORMAT.format(realLength * 100.0 / theTotalTime)).append("%)");
            text.append("\n            ");
            if (node.getLengthStats() != null && node.getLengthStats().isInteresting()) text.append(node.getLengthStats().toString(ProgramTracker.NANO_FORMAT));
            setDescription(text.toString());
            setIcon("manager/time");
            if (node.getLength() > theAccentThresh * theTotalTime) setForeground(java.awt.Color.red); else setForeground(java.awt.Color.black);
            TrackTreeNode[] children;
            if (getChildren() instanceof TrackTreeNode[]) children = (TrackTreeNode[]) getChildren(); else children = new TrackTreeNode[0];
            setChildren(ArrayUtils.adjust(children, node.getChildren(), new ArrayUtils.DifferenceListener<TrackTreeNode, TrackNode>() {

                public boolean identity(TrackTreeNode o1, TrackNode o2) {
                    return o1.getTrackNode().getName() == o2.getName();
                }

                public TrackTreeNode added(TrackNode o, int mIdx, int retIdx) {
                    if (o.getLength() < displayThresh) return null;
                    return new TrackTreeNode(TrackTreeNode.this, o);
                }

                public TrackTreeNode removed(TrackTreeNode o, int oIdx, int incMod, int retIdx) {
                    return null;
                }

                public TrackTreeNode set(TrackTreeNode o1, int idx1, int incMod, TrackNode o2, int idx2, int retIdx) {
                    if (o2.getLength() < displayThresh) return null;
                    o1.setTrackNode(o2);
                    return o1;
                }
            }));
        }
    }
}
