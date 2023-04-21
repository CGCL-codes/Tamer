package ch.laoe.plugin;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import ch.laoe.clip.AChannel;
import ch.laoe.clip.AChannelSelection;
import ch.laoe.clip.MMArray;
import ch.laoe.operation.AOToolkit;
import ch.laoe.ui.Debug;
import ch.laoe.ui.GControlTextX;
import ch.laoe.ui.GControlTextY;
import ch.laoe.ui.GLanguage;
import ch.laoe.ui.LProgressViewer;
import ch.laoe.ui.Laoe;
import ch.oli4.ui.UiCartesianLayout;
import ch.oli4.ui.control.UiControlText;

/***********************************************************

This file is part of LAoE.

LAoE is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published
by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

LAoE is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LAoE; if not, write to the Free Software Foundation,
Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


Class:			GPFind
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.4

Desctription:	general find tool.

History:
Date:			Description:									Autor:
07.01.2002	first draft										oli4
16.06.2002	use channelstack as channel-chooser		oli4

***********************************************************/
public class GPFind extends GPluginFrame {

    public GPFind(GPluginHandler ph) {
        super(ph);
        initGui();
    }

    public String getName() {
        return "find";
    }

    public JMenuItem createMenuItem() {
        return super.createMenuItem(KeyEvent.VK_F);
    }

    private JComboBox findAbsolute, findRelative;

    private UiControlText silenceThreshold, minimumWidth;

    private JButton find, previous, next;

    private EventDispatcher eventDispatcher;

    private void initGui() {
        JTabbedPane tab = new JTabbedPane();
        JPanel p1 = new JPanel();
        UiCartesianLayout l1 = new UiCartesianLayout(p1, 10, 4);
        l1.setPreferredCellSize(new Dimension(25, 35));
        p1.setLayout(l1);
        l1.add(new JLabel(GLanguage.translate("find")), 0, 0, 3, 1);
        String findAbsoluteItems[] = { GLanguage.translate("minimumValue"), GLanguage.translate("maximumValue") };
        findAbsolute = new JComboBox(findAbsoluteItems);
        l1.add(findAbsolute, 3, 0, 7, 1);
        find = new JButton(GLanguage.translate("find"));
        l1.add(find, 3, 3, 4, 1);
        tab.add(GLanguage.translate("absolute"), p1);
        JPanel p2 = new JPanel();
        UiCartesianLayout l2 = new UiCartesianLayout(p2, 10, 4);
        l2.setPreferredCellSize(new Dimension(25, 35));
        p2.setLayout(l2);
        l2.add(new JLabel(GLanguage.translate("find")), 0, 0, 3, 1);
        String findRelativeItems[] = { GLanguage.translate("noise"), GLanguage.translate("silence"), GLanguage.translate("clipped") };
        findRelative = new JComboBox(findRelativeItems);
        l2.add(findRelative, 3, 0, 7, 1);
        previous = new JButton(GLanguage.translate("previous"), loadIcon("left"));
        l2.add(previous, 1, 3, 4, 1);
        next = new JButton(GLanguage.translate("next"), loadIcon("right"));
        l2.add(next, 5, 3, 4, 1);
        tab.add(GLanguage.translate("relative"), p2);
        JPanel p3 = new JPanel();
        UiCartesianLayout l3 = new UiCartesianLayout(p3, 10, 4);
        l3.setPreferredCellSize(new Dimension(25, 35));
        p3.setLayout(l3);
        l3.add(new JLabel(GLanguage.translate("silenceThreshold")), 0, 0, 5, 1);
        silenceThreshold = new GControlTextY(Laoe.getInstance(), 9, true, true);
        silenceThreshold.setDataRange(0, 1e9);
        silenceThreshold.setData(33);
        l3.add(silenceThreshold, 5, 0, 5, 1);
        l3.add(new JLabel(GLanguage.translate("minimumWidth")), 0, 1, 5, 1);
        minimumWidth = new GControlTextX(Laoe.getInstance(), 9, true, true);
        minimumWidth.setDataRange(1, 1e9);
        minimumWidth.setData(100);
        l3.add(minimumWidth, 5, 1, 5, 1);
        tab.add(GLanguage.translate("parameter"), p3);
        frame.getContentPane().add(tab);
        pack();
        eventDispatcher = new EventDispatcher();
        find.addActionListener(eventDispatcher);
        previous.addActionListener(eventDispatcher);
        next.addActionListener(eventDispatcher);
    }

    public void reload() {
        super.reload();
        silenceThreshold.refresh();
        minimumWidth.refresh();
    }

    private class EventDispatcher implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            if (e.getSource() == find) {
                Debug.println(1, "plugin " + getName() + " [find] clicked");
                onFind();
            } else if (e.getSource() == previous) {
                Debug.println(1, "plugin " + getName() + " [previous] clicked");
                onPrevious();
            } else if (e.getSource() == next) {
                Debug.println(1, "plugin " + getName() + " [next] clicked");
                onNext();
            }
            repaintFocussedClipEditor();
            LProgressViewer.getInstance().exitSubProgress();
        }
    }

    private void onFind() {
        AChannel ch = getFocussedClip().getSelectedLayer().getSelectedChannel();
        AChannelSelection chs = ch.getNonEmptySelection();
        MMArray s = ch.getSamples();
        int o = chs.getOffset();
        int l = chs.getLength();
        switch(findAbsolute.getSelectedIndex()) {
            case 0:
                ch.setSelection(new AChannelSelection(ch, AOToolkit.getNegativePeakIndex(s, o, l), 1));
                break;
            case 1:
                ch.setSelection(new AChannelSelection(ch, AOToolkit.getPositivePeakIndex(s, o, l), 1));
                break;
        }
    }

    private void onPrevious() {
        AChannel ch = getFocussedClip().getSelectedLayer().getSelectedChannel();
        AChannelSelection chs = ch.getNonEmptySelection();
        MMArray s = ch.getSamples();
        int o = chs.getOffset();
        float st = (float) silenceThreshold.getData();
        int mw = (int) minimumWidth.getData();
        int clippedThreshold = 1 << (getFocussedClip().getSampleWidth() - 1);
        switch(findRelative.getSelectedIndex()) {
            case 0:
                ch.setSelection(AOToolkit.expandNoise(new AChannelSelection(ch, AOToolkit.getNextLowerNoiseIndex(s, o - 1, o - 1, st, mw), 1), st, mw));
                break;
            case 1:
                ch.setSelection(AOToolkit.expandSilence(new AChannelSelection(ch, AOToolkit.getNextLowerSilenceIndex(s, o - 1, o - 1, st, mw), 1), st, mw));
                break;
            case 2:
                ch.setSelection(AOToolkit.expandNoise(new AChannelSelection(ch, AOToolkit.getNextLowerNoiseIndex(s, o - 1, o - 1, clippedThreshold, 1), 1), clippedThreshold, 1));
                break;
        }
    }

    private void onNext() {
        AChannel ch = getFocussedClip().getSelectedLayer().getSelectedChannel();
        AChannelSelection chs = ch.getNonEmptySelection();
        MMArray s = ch.getSamples();
        int o = chs.getOffset();
        int l = chs.getLength();
        float st = (float) silenceThreshold.getData();
        int mw = (int) minimumWidth.getData();
        int clippedThreshold = 1 << (getFocussedClip().getSampleWidth() - 1);
        switch(findRelative.getSelectedIndex()) {
            case 0:
                ch.setSelection(AOToolkit.expandNoise(new AChannelSelection(ch, AOToolkit.getNextUpperNoiseIndex(s, o + l + 1, s.getLength() - o - l - 1, st, mw), 1), st, mw));
                break;
            case 1:
                ch.setSelection(AOToolkit.expandSilence(new AChannelSelection(ch, AOToolkit.getNextUpperSilenceIndex(s, o + l + 1, s.getLength() - o - l - 1, st, mw), 1), st, mw));
                break;
            case 2:
                ch.setSelection(AOToolkit.expandNoise(new AChannelSelection(ch, AOToolkit.getNextUpperNoiseIndex(s, o + l + 1, s.getLength() - o - l - 1, clippedThreshold, 1), 1), clippedThreshold, 1));
                break;
        }
    }
}
