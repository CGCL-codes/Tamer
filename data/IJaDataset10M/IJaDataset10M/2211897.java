package org.fudaa.fudaa.tr.post;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.memoire.bu.*;
import org.fudaa.ctulu.CtuluDurationFormatter;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.editor.CtuluValueEditorTime;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.fudaa.tr.common.TrResource;

/**
 * @author fred deniger
 * @version $Id: TrPostInspectorPanelBuilder.java,v 1.4 2006-07-27 13:35:34 deniger Exp $
 */
public class TrPostInspectorPanelBuilder implements ProgressionInterface {

    BuProgressBar bar_ = new BuProgressBar();

    DateFormat dateFmt_;

    BuTextField lbLastAdded_ = new BuTextField();

    BuTextField lbLastUpdate_ = new BuTextField();

    BuTextField lbTime_;

    BuPanel pn_ = new BuPanel();

    BuToolButton stop_ = new BuToolButton(BuResource.BU.getToolIcon("arreter"));

    BuToolToggleButton pause_ = new BuToolToggleButton(EbliResource.EBLI.getToolIcon("anim-pause"));

    CtuluValueEditorTime time_ = new CtuluValueEditorTime();

    DateFormat timeFmt_;

    TrPostInspectorPanelBuilder(final TrPostInspector _builder) {
        this(_builder, true);
    }

    TrPostInspectorPanelBuilder(final TrPostInspector _builder, final boolean _small) {
        final CtuluDurationFormatter fmt = new CtuluDurationFormatter(true, false);
        fmt.setAlwaysDisplayZero(false);
        time_.setFmt(fmt);
        lbTime_ = (BuTextField) time_.createEditorComponent();
        lbTime_.setEditable(false);
        lbTime_.setFocusable(false);
        build(_builder, _small);
    }

    TrPostInspectorPanelBuilder(final TrPostInspector _builder, final TrPostInspectorPanelBuilder _ui) {
        this(_builder, true);
        if (_ui != null) {
            lbLastUpdate_.setText(_ui.lbLastUpdate_.getText());
            lbLastAdded_.setText(_ui.lbLastAdded_.getText());
            lbTime_.setText(_ui.lbTime_.getText());
            bar_.setValue(_ui.bar_.getValue());
            bar_.setString(_ui.bar_.getString());
        }
    }

    private void add(final JPanel _dest, final JComponent _cp, final Font _ft) {
        if (_ft != null) {
            _cp.setFont(_ft);
        }
        _dest.add(_cp);
    }

    private void build(final TrPostInspector _builder, final boolean _small) {
        pn_.setLayout(new BuBorderLayout(1, 1, true, false));
        pn_.setSize(pn_.getPreferredSize());
        pn_.setBorder(BuBorders.EMPTY1111);
        BuPanel pn = new BuPanel();
        pn.setLayout(new BuGridLayout(2, 3, 1));
        final Font ft = _small ? BuLib.deriveFont("Label", Font.PLAIN, -2) : null;
        add(pn, new BuLabel(TrResource.getS("Derni�re actualisation")), ft);
        lbLastUpdate_.setEditable(false);
        lbLastAdded_.setEditable(false);
        add(pn, lbLastUpdate_, ft);
        add(pn, new BuLabel(TrResource.getS("Pas de temps lus")), ft);
        add(pn, lbLastAdded_, ft);
        add(pn, new BuLabel(TrResource.getS("D�lai actualisation")), ft);
        lbTime_.setText(((CtuluDurationFormatter) time_.getFmt()).format(_builder.nbSec_));
        lbTime_.setMargin(BuInsets.INSETS0000);
        add(pn, lbTime_, ft);
        pn_.add(pn, BuBorderLayout.CENTER);
        pn = new BuPanel();
        pn.setLayout(new BuBorderLayout(0, 0, true, false));
        bar_.setMaximum(100);
        bar_.setStringPainted(true);
        bar_.setMinimum(0);
        pause_.setBorderPainted(false);
        pause_.setToolTipText(EbliLib.getS("Pause"));
        stop_.setBorderPainted(false);
        stop_.setToolTipText(TrResource.getS("Arr�ter l'actualisation automatique"));
        stop_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                _builder.stop();
            }
        });
        pause_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                _builder.pause();
                bar_.setString(_builder.isPause() ? pause_.getToolTipText() : CtuluLibString.EMPTY_STRING);
            }
        });
        pn.add(bar_, BuBorderLayout.CENTER);
        final BuPanel pnBts = new BuPanel();
        pnBts.setLayout(new BuButtonLayout(0, SwingConstants.LEFT));
        pnBts.add(pause_);
        pnBts.add(stop_);
        pn.add(pnBts, BuBorderLayout.EAST);
        pn_.add(pn, BuBorderLayout.SOUTH);
    }

    protected BuPanel getPn() {
        return pn_;
    }

    public void reset() {
        setDesc(CtuluLibString.EMPTY_STRING);
        setProgression(0);
    }

    public void setDesc(final String _s) {
        bar_.setString(_s);
    }

    public void setProgression(final int _v) {
        bar_.setValue(_v);
    }

    public void update(final int _nbTimeStepAdded) {
        if (dateFmt_ == null) {
            dateFmt_ = DateFormat.getDateInstance();
            timeFmt_ = DateFormat.getTimeInstance();
        }
        final Date time = Calendar.getInstance().getTime();
        lbLastUpdate_.setText(timeFmt_.format(time));
        lbLastUpdate_.setToolTipText(dateFmt_.format(time) + CtuluLibString.ESPACE + lbLastUpdate_.getText());
        lbLastAdded_.setText(CtuluLibString.getString(_nbTimeStepAdded));
    }
}
