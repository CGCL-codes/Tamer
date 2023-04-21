package ch.laoe.plugin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import ch.laoe.clip.AClip;
import ch.laoe.ui.Debug;
import ch.laoe.ui.GLanguage;
import ch.laoe.ui.GPersistence;
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


Class:			GPFileNew
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	plugin to create a new clip-file.  

History:
Date:			Description:									Autor:
09.10.00		erster Entwurf									oli4
09.12.01		separate from clip-properties, add units
				to samples										oli4

***********************************************************/
public class GPFileNew extends GPluginFrame {

    public GPFileNew(GPluginHandler ph) {
        super(ph);
        initGui();
    }

    public String getName() {
        return "new";
    }

    public JMenuItem createMenuItem() {
        return super.createMenuItem(KeyEvent.VK_N);
    }

    protected UiControlText samples;

    protected UiControlText sampleRate;

    protected UiControlText sampleWidth;

    protected UiControlText channels;

    protected UiControlText layers;

    protected JTextArea comments;

    protected JButton apply;

    private EventDispatcher eventDispatcher;

    protected void initGui() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel pData = new JPanel();
        UiCartesianLayout lData = new UiCartesianLayout(pData, 16, 3);
        lData.setPreferredCellSize(new Dimension(25, 35));
        pData.setLayout(lData);
        lData.add(new JLabel(GLanguage.translate("samples")), 0, 0, 4, 1);
        samples = new SamplesControl(10, true, true);
        samples.setUnit("s");
        samples.setDataRange(0, 1e10);
        lData.add(samples, 4, 0, 6, 1);
        lData.add(new JLabel(GLanguage.translate("sampleRate")), 0, 1, 4, 1);
        sampleRate = new UiControlText(8, true, false);
        sampleRate.setNumberFormat(UiControlText.FLOATING_POINT_FORMAT);
        sampleRate.setDataRange(100, 100000);
        lData.add(sampleRate, 4, 1, 4, 1);
        lData.add(new JLabel(GLanguage.translate("width")), 11, 1, 2, 1);
        sampleWidth = new UiControlText(3, true, false);
        sampleWidth.setNumberFormat(UiControlText.INTEGER_FORMAT);
        sampleWidth.setDataRange(1, 32);
        lData.add(sampleWidth, 13, 1, 3, 1);
        lData.add(new JLabel(GLanguage.translate("layers")), 0, 2, 4, 1);
        layers = new UiControlText(3, true, false);
        layers.setNumberFormat(UiControlText.INTEGER_FORMAT);
        layers.setDataRange(1, 32);
        lData.add(layers, 4, 2, 3, 1);
        lData.add(new JLabel(GLanguage.translate("channels")), 10, 2, 3, 1);
        channels = new UiControlText(3, true, false);
        channels.setNumberFormat(UiControlText.INTEGER_FORMAT);
        channels.setDataRange(1, 32);
        lData.add(channels, 13, 2, 3, 1);
        tabbedPane.addTab(GLanguage.translate("data"), pData);
        JPanel pComments = new JPanel();
        UiCartesianLayout lComments = new UiCartesianLayout(pComments, 15, 3);
        lComments.setPreferredCellSize(new Dimension(25, 35));
        pComments.setLayout(lComments);
        comments = new JTextArea(7, 35);
        comments.setLineWrap(true);
        comments.setWrapStyleWord(true);
        JScrollPane scrolledComents = new JScrollPane(comments);
        lComments.add(scrolledComents, 0, 0, 15, 3);
        tabbedPane.addTab(GLanguage.translate("comments"), pComments);
        p.add(tabbedPane, BorderLayout.CENTER);
        JPanel pApply = new JPanel();
        apply = new JButton(GLanguage.translate("apply"));
        pApply.add(apply);
        p.add(pApply, BorderLayout.SOUTH);
        samples.setEditable(true);
        sampleRate.setEditable(true);
        sampleWidth.setEditable(true);
        channels.setEditable(true);
        layers.setEditable(true);
        comments.setEditable(true);
        comments.setText(GPersistence.createPersistance().getString("clip.defaultComment"));
        samples.setData(10);
        sampleRate.setData(44100);
        sampleWidth.setData(16);
        channels.setData(2);
        layers.setData(1);
        eventDispatcher = new EventDispatcher();
        apply.addActionListener(eventDispatcher);
        frame.getContentPane().add(p);
        pack();
    }

    public void reload() {
        super.reload();
        samples.refresh();
        sampleRate.refresh();
        sampleWidth.refresh();
        channels.refresh();
        layers.refresh();
    }

    private class EventDispatcher implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == apply) {
                Debug.println(1, "plugin " + getName() + " [apply] clicked");
                storeValues();
                autoCloseNow();
            }
        }
    }

    protected void storeValues() {
        LProgressViewer.getInstance().entrySubProgress(getName());
        int s;
        switch(samples.getUnitIndex()) {
            case 0:
                s = (int) samples.getData();
                break;
            case 1:
                s = (int) (samples.getData() * sampleRate.getData());
                break;
            case 2:
                s = (int) (samples.getData() * sampleRate.getData() / 1000);
                break;
            default:
                s = (int) samples.getData();
        }
        AClip c = new AClip((int) layers.getData(), (int) channels.getData(), s);
        c.setSampleRate((float) sampleRate.getData());
        c.setSampleWidth((int) sampleWidth.getData());
        c.setComments(comments.getText());
        Laoe.getInstance().addClipFrame(c);
        c.getPlotter().autoScaleX();
        int w = 1 << c.getSampleWidth();
        c.getPlotter().setYRange(-w / 2, w);
        LProgressViewer.getInstance().exitSubProgress();
    }

    protected void loadValues() {
        try {
            samples.setData(pluginHandler.getFocussedClip().getMaxSampleLength());
            sampleRate.setData(pluginHandler.getFocussedClip().getSampleRate());
            sampleWidth.setData(pluginHandler.getFocussedClip().getSampleWidth());
            channels.setData(pluginHandler.getFocussedClip().getMaxNumberOfChannels());
            layers.setData(pluginHandler.getFocussedClip().getNumberOfLayers());
            comments.setText(pluginHandler.getFocussedClip().getComments());
        } catch (NullPointerException npe) {
        }
    }

    private class SamplesControl extends UiControlText {

        public SamplesControl(int digits, boolean incrementVisible, boolean unitVisible) {
            super(digits, incrementVisible, unitVisible);
        }

        protected void fillUnits() {
            addUnit(" ");
            addUnit("s");
            addUnit("ms");
        }
    }
}
