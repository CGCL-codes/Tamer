package ch.laoe.plugin;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import ch.laoe.clip.AClip;
import ch.laoe.clip.AClipSelection;
import ch.laoe.clip.ALayerSelection;
import ch.laoe.operation.AOAmplify;
import ch.laoe.operation.AOHighPass;
import ch.laoe.operation.AOMeasure;
import ch.laoe.operation.AONormalize;
import ch.laoe.operation.AOSaturate;
import ch.laoe.ui.Debug;
import ch.laoe.ui.GClipLayerChooser;
import ch.laoe.ui.GControlTextA;
import ch.laoe.ui.GLanguage;
import ch.laoe.ui.LProgressViewer;
import ch.laoe.ui.LWorker;
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


Class:			GPAmplify
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	plugin to browse the manual.  

History:
Date:			Description:									Autor:
24.10.00		erster Entwurf									oli4
10.11.00		normalize & saturate to 1..32bit			oli4
27.01.01		float-array based								oli4
06.05.01		add DC-removal									oli4
01.12.01		add invert & normalize modes				oli4
28.06.02		coupled normalisation added				oli4

***********************************************************/
public class GPAmplify extends GPluginFrame {

    public GPAmplify(GPluginHandler ph) {
        super(ph);
        initGui();
    }

    public String getName() {
        return "amplify";
    }

    private JCheckBox invert, normalize, saturate, removeDc, coupled;

    private UiControlText amplification, bitWidth;

    private JComboBox normalizeMode;

    private JButton applyConst, applyVar;

    private GClipLayerChooser layerChooser;

    private EventDispatcher eventDispatcher;

    private void initGui() {
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel pConst = new JPanel();
        UiCartesianLayout clt = new UiCartesianLayout(pConst, 10, 6);
        clt.setPreferredCellSize(new Dimension(25, 35));
        pConst.setLayout(clt);
        clt.add(new JLabel(GLanguage.translate("amplification")), 0, 0, 5, 1);
        amplification = new GControlTextA(10, true, true);
        amplification.setDataRange(-10000, 10000);
        amplification.setData(1);
        clt.add(amplification, 5, 0, 5, 1);
        invert = new JCheckBox(GLanguage.translate("invert"));
        clt.add(invert, 0, 1, 5, 1);
        removeDc = new JCheckBox(GLanguage.translate("removeDc"));
        clt.add(removeDc, 5, 1, 5, 1);
        clt.add(new JLabel(GLanguage.translate("bitWidth")), 0, 2, 5, 1);
        bitWidth = new UiControlText(4, true, false);
        bitWidth.setDataRange(1, 32);
        bitWidth.setData(16);
        clt.add(bitWidth, 5, 2, 3, 1);
        normalize = new JCheckBox(GLanguage.translate("normalize"));
        clt.add(normalize, 0, 3, 5, 1);
        String modeItem[] = { GLanguage.translate("peak"), GLanguage.translate("RMS") };
        normalizeMode = new JComboBox(modeItem);
        normalizeMode.setSelectedIndex(0);
        clt.add(normalizeMode, 5, 3, 3, 1);
        saturate = new JCheckBox(GLanguage.translate("saturate"));
        clt.add(saturate, 0, 4, 5, 1);
        coupled = new JCheckBox(GLanguage.translate("coupled"));
        coupled.setSelected(true);
        clt.add(coupled, 5, 4, 5, 1);
        applyConst = new JButton(GLanguage.translate("apply"));
        clt.add(applyConst, 3, 5, 4, 1);
        tabbedPane.add(GLanguage.translate("constant"), pConst);
        JPanel pVar = new JPanel();
        UiCartesianLayout clv = new UiCartesianLayout(pVar, 10, 6);
        clv.setPreferredCellSize(new Dimension(25, 35));
        pVar.setLayout(clv);
        layerChooser = new GClipLayerChooser(Laoe.getInstance(), "envelopeCurve");
        clv.add(layerChooser, 0, 0, 10, 3);
        applyVar = new JButton(GLanguage.translate("apply"));
        clv.add(applyVar, 3, 5, 4, 1);
        tabbedPane.add(GLanguage.translate("envelope"), pVar);
        frame.getContentPane().add(tabbedPane);
        pack();
        updateConstantComponents();
        eventDispatcher = new EventDispatcher();
        normalize.addActionListener(eventDispatcher);
        saturate.addActionListener(eventDispatcher);
        applyConst.addActionListener(new LWorker(eventDispatcher));
        applyVar.addActionListener(new LWorker(eventDispatcher));
    }

    private void updateConstantComponents() {
        if (normalize.isSelected() || saturate.isSelected()) {
            bitWidth.setEnabled(true);
        } else {
            bitWidth.setEnabled(false);
        }
        if (normalize.isSelected()) {
            normalizeMode.setEnabled(true);
            coupled.setEnabled(true);
        } else {
            normalizeMode.setEnabled(false);
            coupled.setEnabled(false);
        }
    }

    public void reload() {
        super.reload();
        layerChooser.reload();
        bitWidth.setData(getFocussedClip().getSampleWidth());
        amplification.refresh();
    }

    private class EventDispatcher implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == normalize) {
                    Debug.println(1, "plugin " + getName() + " [normalize] clicked");
                    updateConstantComponents();
                    return;
                } else if (e.getSource() == saturate) {
                    Debug.println(1, "plugin " + getName() + " [saturate] clicked");
                    updateConstantComponents();
                    return;
                }
                LProgressViewer.getInstance().entrySubProgress(getName());
                LProgressViewer.getInstance().entrySubProgress(0.7);
                if (e.getSource() == applyConst) {
                    Debug.println(1, "plugin " + getName() + " [apply const] clicked");
                    onApplyConst();
                } else if (e.getSource() == applyVar) {
                    Debug.println(1, "plugin " + getName() + " [apply var] clicked");
                    onApplyVar();
                }
                updateHistory(GLanguage.translate(getName()));
                LProgressViewer.getInstance().exitSubProgress();
                LProgressViewer.getInstance().exitSubProgress();
                reloadFocussedClipEditor();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    private void onApplyConst() {
        float bitWidthMaxValue = (1 << ((int) bitWidth.getData() - 1)) - 1;
        float a = (float) amplification.getData();
        ALayerSelection ls = getFocussedClip().getSelectedLayer().getSelection();
        if (removeDc.isSelected()) {
            ls.operateEachChannel(new AOHighPass(0.f, 1.f, 5.f / getFocussedClip().getSampleRate()));
        }
        if (invert.isSelected()) {
            a = -a;
        }
        if (normalize.isSelected()) {
            if (coupled.isSelected()) {
                switch(normalizeMode.getSelectedIndex()) {
                    case 0:
                        {
                            AOMeasure o = new AOMeasure((int) bitWidth.getData());
                            ls.operateEachChannel(o);
                            float p = o.getAbsoluteMax();
                            ls.operateEachChannel(new AOAmplify(bitWidthMaxValue / p * a));
                        }
                        break;
                    case 1:
                        {
                            AOMeasure o = new AOMeasure((int) bitWidth.getData());
                            ls.operateEachChannel(o);
                            float p = o.getRms();
                            ls.operateEachChannel(new AOAmplify(bitWidthMaxValue / p * a));
                        }
                        break;
                }
            } else {
                switch(normalizeMode.getSelectedIndex()) {
                    case 0:
                        ls.operateEachChannel(new AONormalize(AONormalize.PEAK, (bitWidthMaxValue * a)));
                        break;
                    case 1:
                        ls.operateEachChannel(new AONormalize(AONormalize.RMS, (bitWidthMaxValue * a)));
                        break;
                }
            }
        } else {
            ls.operateEachChannel(new AOAmplify(a));
        }
        if (saturate.isSelected()) {
            ls.operateEachChannel(new AOSaturate(bitWidthMaxValue));
        }
        autoCloseNow();
    }

    private void onApplyVar() {
        ALayerSelection ls = getFocussedClip().getSelectedLayer().getSelection();
        ALayerSelection ps = layerChooser.getSelectedLayer().createSelection();
        AClipSelection cs = new AClipSelection(new AClip());
        cs.addLayerSelection(ls);
        cs.addLayerSelection(ps);
        cs.operateLayer0WithLayer1(new AOAmplify());
        autoCloseNow();
    }
}
