package com.rbnb.plot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.rbnb.utility.ToString;

public class UserControl extends JComponent implements Runnable, ActionListener {

    private Dimension size = null;

    private LWContainer buttons = null;

    private JButton bof = null;

    private JButton revPlay = null;

    private JButton revStep = null;

    private JButton stop = null;

    private JButton fwdStep = null;

    private JButton fwdPlay = null;

    private JButton eof = null;

    private JButton realTime = null;

    private JButton channelButton = null;

    Time[] position = new Time[3];

    private JScrollBar posSlider = null;

    int psval = 0;

    private int psblock = 100, psunit = 10, psmin = 0, psmax = 1000;

    FWLabel posText = null;

    FWField posLabel = null;

    FWLabel updateLabel = null;

    FWLabel rawTimeLabel = null;

    Time duration = null;

    private JScrollBar durSlider = null;

    int dsval = 20;

    private int dsblock = 1, dsunit = 1, dsmin = 0, dsmax = 40;

    FWField durLabel = null;

    private RegChannel[] availableChans = null;

    private RunModeCubby runModeCubby = null;

    private LayoutCubby layoutCubby = null;

    private RBNBCubby rbnbCubby = null;

    PosDurCubby posDurCubby = null;

    DurationListener durListener = null;

    private String timeLabel = null;

    private boolean showStartTime = false;

    private int oldTimeFormat = Time.Unspecified;

    private JChannelDialog jcd = null;

    private boolean jcdShown = false;

    private static NumberFormat numberformat = NumberFormat.getInstance();

    public UserControl(JFrame f, RunModeCubby rmc, LayoutCubby loc, RBNBCubby rbc, PosDurCubby pdc, Environment env) {
        setFont(Environment.FONT12);
        JFrame frame = f;
        runModeCubby = rmc;
        layoutCubby = loc;
        rbnbCubby = rbc;
        posDurCubby = pdc;
        timeLabel = new String(env.TIME_LABEL);
        while ((duration = pdc.getDuration(true)) == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
        buttons = new LWContainer();
        bof = new JButton("| <");
        bof.setFont(Environment.FONT10);
        bof.addActionListener(new ButtonListener(buttons, bof, runModeCubby, RunModeDefs.bof));
        bof.setName(Integer.toString(RunModeDefs.bof));
        revPlay = new JButton("<");
        revPlay.setFont(Environment.FONT10);
        revPlay.addActionListener(new ButtonListener(buttons, revPlay, runModeCubby, RunModeDefs.revPlay));
        revPlay.setName(Integer.toString(RunModeDefs.revPlay));
        revStep = new JButton("< |");
        revStep.setFont(Environment.FONT10);
        revStep.addActionListener(new ButtonListener(buttons, revStep, runModeCubby, RunModeDefs.revStep));
        revStep.setName(Integer.toString(RunModeDefs.revStep));
        stop = new JButton("||");
        stop.setFont(Environment.FONT10);
        stop.addActionListener(new ButtonListener(buttons, stop, runModeCubby, RunModeDefs.stop));
        stop.setName(Integer.toString(RunModeDefs.stop));
        fwdStep = new JButton("| >");
        fwdStep.setFont(Environment.FONT10);
        fwdStep.addActionListener(new ButtonListener(buttons, fwdStep, runModeCubby, RunModeDefs.fwdStep));
        fwdStep.setName(Integer.toString(RunModeDefs.fwdStep));
        fwdPlay = new JButton(">");
        fwdPlay.setFont(Environment.FONT10);
        fwdPlay.addActionListener(new ButtonListener(buttons, fwdPlay, runModeCubby, RunModeDefs.fwdPlay));
        fwdPlay.setName(Integer.toString(RunModeDefs.fwdPlay));
        eof = new JButton("> |");
        eof.setFont(Environment.FONT10);
        eof.addActionListener(new ButtonListener(buttons, eof, runModeCubby, RunModeDefs.eof));
        eof.setName(Integer.toString(RunModeDefs.eof));
        realTime = new JButton("RT");
        realTime.setFont(Environment.FONT10);
        realTime.addActionListener(new ButtonListener(buttons, realTime, runModeCubby, RunModeDefs.realTime));
        realTime.setName(Integer.toString(RunModeDefs.realTime));
        posSlider = new JScrollBar(JScrollBar.HORIZONTAL, psval, psblock, psmin, psmax);
        posSlider.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.black));
        posSlider.addAdjustmentListener(new PositionListener());
        try {
            if (env.MAXDURATION != null) {
                double maxdur = env.MAXDURATION.getDoubleValue();
                double startdur = duration.getDoubleValue();
                int dsdelta = (int) (3 * (Math.log(maxdur) - Math.log(startdur)) / Math.log(10));
                if (dsdelta > 0) dsmax = dsval + dsdelta + 1;
            }
        } catch (Exception e) {
        }
        durSlider = new JScrollBar(JScrollBar.HORIZONTAL, dsval, dsblock, dsmin, dsmax);
        durSlider.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.black));
        durSlider.setUnitIncrement(dsunit);
        durListener = new DurationListener();
        durSlider.addAdjustmentListener(durListener);
        channelButton = new JButton("Channels");
        channelButton.setFont(Environment.FONT10);
        channelButton.addActionListener(this);
        while ((availableChans = rbc.getAvailableChannels()) == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
        jcd = new JChannelDialog(frame, layoutCubby, rbnbCubby);
        jcd.setAvailableChannels(availableChans);
        if (availableChans.length <= 4) rbnbCubby.setSelectedChannels(availableChans, true); else {
            RegChannel[] selectedChans = new RegChannel[4];
            for (int i = 0; i < 4; i++) selectedChans[i] = availableChans[i];
            rbnbCubby.setSelectedChannels(selectedChans, true);
        }
        setLayout(new BorderLayout());
        buttons.setLayout(new GridLayout(1, 0));
        buttons.add(bof);
        buttons.add(revPlay);
        buttons.add(revStep);
        buttons.add(stop);
        buttons.add(fwdStep);
        buttons.add(fwdPlay);
        buttons.add(eof);
        buttons.add(realTime);
        LWContainer sliders = new LWContainer();
        sliders.setLayout(new GridLayout(0, 1));
        LWContainer posCont = new LWContainer();
        posCont.setLayout(new BorderLayout());
        try {
            posLabel = new FWField("", duration.getFormattedString(Time.AbsoluteSeconds1970, Time.Full, -1), SwingConstants.LEFT);
            PositionTextListener ptl = new PositionTextListener();
            posLabel.addActionListener(ptl);
            posLabel.addFocusListener(ptl);
        } catch (Exception e) {
        }
        try {
            rawTimeLabel = new FWLabel("", "1234567890  Updates/Sec ", SwingConstants.RIGHT);
        } catch (Exception e) {
        }
        updateLabel = new FWLabel("", "1234567890 Updates/Sec ", SwingConstants.RIGHT);
        posText = new FWLabel(">Position:", ">Duration: ", SwingConstants.LEFT);
        posCont.add(posText, BorderLayout.WEST);
        posCont.add(posLabel, BorderLayout.CENTER);
        posCont.add(rawTimeLabel, BorderLayout.EAST);
        sliders.add(posCont);
        sliders.add(posSlider);
        LWContainer durCont = new LWContainer();
        durCont.setLayout(new BorderLayout());
        try {
            durLabel = new FWField(makeDurLabel(duration), duration.getFormattedString(Time.AbsoluteSeconds1970, Time.Full, -1), SwingConstants.LEFT);
            DurationTextListener dtl = new DurationTextListener();
            durLabel.addActionListener(dtl);
            durLabel.addFocusListener(dtl);
        } catch (Exception e) {
        }
        durCont.add(new FWLabel("Duration:", ">Duration: ", SwingConstants.LEFT), BorderLayout.WEST);
        durCont.add(durLabel, BorderLayout.CENTER);
        durCont.add(updateLabel, BorderLayout.EAST);
        sliders.add(durCont);
        sliders.add(durSlider);
        posSlider.setValues(psval, psblock, psmin, psmax);
        posSlider.setBlockIncrement(psblock);
        posSlider.setUnitIncrement(psunit);
        durSlider.setValues(dsval, dsblock, dsmin, dsmax);
        durSlider.setBlockIncrement(dsblock);
        durSlider.setUnitIncrement(dsunit);
        LWContainer butslid = new LWContainer();
        butslid.setLayout(new BorderLayout());
        butslid.add(sliders, BorderLayout.SOUTH);
        butslid.add(buttons, BorderLayout.CENTER);
        add(channelButton, BorderLayout.EAST);
        add(butslid, BorderLayout.CENTER);
        oldTimeFormat = posDurCubby.getTimeFormat();
        Thread ucThread = new Thread(this, "ucThread");
        ucThread.start();
    }

    private String makeDurLabel(Time duration) {
        String labelString = null;
        int timeFormat = posDurCubby.getTimeFormat();
        int precision = posDurCubby.getPrecision();
        try {
            if (timeFormat == Time.AbsoluteSeconds1970 || timeFormat == Time.RelativeSeconds) {
                labelString = duration.getFormattedString(Time.RelativeSeconds, Time.Full, precision);
                int firstColon = labelString.indexOf(':');
                if (firstColon == -1) labelString = labelString.trim() + " Sec"; else {
                    int lastColon = labelString.lastIndexOf(':');
                    if (firstColon == lastColon) labelString = labelString.trim() + " Min:Sec"; else labelString = labelString.trim() + " H:M:S";
                }
                int day = labelString.indexOf("Day");
                if (day != -1) labelString = labelString.substring(0, day + 4).trim();
            } else {
                labelString = duration.getFormattedString(timeFormat, Time.Fine, 3).trim() + timeLabel;
            }
        } catch (Exception e1) {
        }
        return labelString;
    }

    private void makePosTextLabel(int runMode) {
        if (runMode == RunModeDefs.realTime || runMode == RunModeDefs.eof) {
            showStartTime = false;
        } else if (runMode == RunModeDefs.bof || runMode == RunModeDefs.revPlay || runMode == RunModeDefs.revStep || runMode == RunModeDefs.fwdStep || runMode == RunModeDefs.fwdPlay || runMode == RunModeDefs.allData) {
            showStartTime = true;
        }
        if (showStartTime) posText.setText("<Position: "); else posText.setText(">Position: ");
        posDurCubby.setPositionAtStart(showStartTime);
    }

    public void clearChannelDialog() {
        if (jcd != null) {
            jcd.setVisible(false);
            jcd = null;
        }
    }

    public void run() {
        Time[] newPosition = new Time[3];
        Time dur = null;
        String newUpdateRate = null;
        RegChannel[] newChans = null;
        Integer runModeInt = null;
        Boolean streaming = null;
        Time[] zoom = null;
        while (true) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                System.out.println("ucThread sleep error: " + e);
            }
            if ((newPosition = posDurCubby.getPosition(true)) != null) {
                changePosition(newPosition);
            }
            if ((dur = posDurCubby.getDuration(true)) != null) {
                synchronized (this) {
                    duration = dur;
                    durListener.setDurTimeValues();
                    durLabel.setText(makeDurLabel(duration));
                }
            }
            if ((newUpdateRate = posDurCubby.getUpdateRate()) != null) {
                updateLabel.setText(newUpdateRate);
            }
            if ((zoom = posDurCubby.getZoom()) != null) {
                posLabel.requestFocusInWindow();
                durLabel.setText(zoom[1].toString());
                durLabel.postActionEvent();
                posLabel.setText(zoom[0].getFormattedString(posDurCubby.getTimeFormat(), Time.Full, posDurCubby.getPrecision()));
                posLabel.postActionEvent();
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                System.out.println("ucThread sleep error: " + e);
            }
            if ((newChans = rbnbCubby.getAvailableChannels()) != null) {
                if (jcd != null) {
                    jcd.setAvailableChannels(newChans);
                }
            }
            if ((newChans = rbnbCubby.getSelectedChannels(true)) != null) {
                jcd.setSelectedChannels(newChans);
            } else if ((runModeInt = runModeCubby.get(true)) != null) {
                makePosTextLabel(runModeInt.intValue());
                for (int i = 0; i < buttons.getComponentCount(); i++) {
                    buttons.getComponent(i).setBackground(Environment.BGCOLOR);
                    if (buttons.getComponent(i).getName().equals(runModeInt.toString())) buttons.getComponent(i).setBackground(Environment.BGCOLOR.darker());
                }
            } else if ((streaming = runModeCubby.getStreaming()) != null) {
                if (streaming.booleanValue()) realTime.setText("RT*"); else realTime.setText("RT");
            }
        }
    }

    private synchronized void changePosition(Time[] newPosition) {
        position = newPosition;
        if (position[0].compareTo(position[1]) == -1) psval = 0; else if (position[0].compareTo(position[2]) == 1) psval = 1000; else psval = (int) Math.round(1000 * position[0].subtractTime(position[1]).getDoubleValue() / position[2].subtractTime(position[1]).getDoubleValue());
        AdjustmentListener[] listeners = posSlider.getAdjustmentListeners();
        for (int adjIdx = 0; adjIdx < listeners.length; ++adjIdx) {
            posSlider.removeAdjustmentListener(listeners[adjIdx]);
        }
        posSlider.setValue(psval);
        psblock = (int) (1000 * duration.getDoubleValue() / (position[2].subtractTime(position[1]).getDoubleValue()) + 0.5);
        if (psblock < 1) psblock = 1;
        if (psblock > 1000) psblock = 1000;
        posSlider.setVisibleAmount(psblock);
        posSlider.setBlockIncrement(psblock);
        for (int adjIdx = 0; adjIdx < listeners.length; ++adjIdx) {
            posSlider.addAdjustmentListener(listeners[adjIdx]);
        }
        int timeFormat = posDurCubby.getTimeFormat();
        if (oldTimeFormat != timeFormat) {
            oldTimeFormat = timeFormat;
            durListener.updateTimeFormat(timeFormat);
        }
        try {
            if (showStartTime) posLabel.setText(position[0].getFormattedString(timeFormat, Time.Full, posDurCubby.getPrecision())); else posLabel.setText(position[0].addTime(duration).getFormattedString(timeFormat, Time.Full, posDurCubby.getPrecision()));
            if (timeFormat != Time.Unspecified) {
                if (showStartTime) rawTimeLabel.setText(position[0].getFormattedString(Time.Unspecified, Time.Full, posDurCubby.getPrecision()) + " Sec"); else rawTimeLabel.setText(position[0].addTime(duration).getFormattedString(Time.Unspecified, Time.Full, posDurCubby.getPrecision()) + " Sec");
            } else {
                rawTimeLabel.setText("");
            }
        } catch (Exception e) {
        }
    }

    public void paint(Graphics g) {
        size = getSize();
        g.drawRect(0, 0, size.width - 1, size.height - 1);
        super.paint(g);
    }

    public void actionPerformed(ActionEvent e) {
        if (jcd == null) {
            System.err.println("JChannelDialog null!");
        } else {
            jcd.firstShow();
        }
    }

    class ButtonListener implements ActionListener {

        LWContainer lwCont = null;

        JButton btn = null;

        private RunModeCubby runModeCubby = null;

        int runMode = RunModeDefs.stop;

        public ButtonListener(LWContainer lwc, JButton b, RunModeCubby rmc, int rm) {
            lwCont = lwc;
            btn = b;
            runModeCubby = rmc;
            runMode = rm;
        }

        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < lwCont.getComponentCount(); i++) lwCont.getComponent(i).setBackground(Environment.BGCOLOR);
            btn.setBackground(Environment.BGCOLOR.darker());
            runModeCubby.set(runMode, true);
            makePosTextLabel(runMode);
        }
    }

    class PositionListener implements AdjustmentListener {

        public void adjustmentValueChanged(AdjustmentEvent e) {
            synchronized (this) {
                showStartTime = true;
                posDurCubby.setPositionAtStart(showStartTime);
                psval = posSlider.getValue();
                if (psval == 0) position[0] = position[1]; else if (psval == 1000) position[0] = position[2].subtractTime(duration); else position[0] = new Time((psval * (position[2].getDoubleValue() - position[1].getDoubleValue()) / 1000) + position[1].getDoubleValue());
                posDurCubby.setPosition(position, true);
                try {
                    posLabel.setText(position[0].getFormattedString(posDurCubby.getTimeFormat(), Time.Full, posDurCubby.getPrecision()));
                    if (posDurCubby.getTimeFormat() != Time.Unspecified) {
                        rawTimeLabel.setText(position[0].getFormattedString(Time.Unspecified, Time.Full, posDurCubby.getPrecision()) + " Sec");
                    } else {
                        rawTimeLabel.setText("");
                    }
                } catch (Exception e1) {
                }
            }
        }
    }

    class DurationListener implements AdjustmentListener {

        Time[] durTime = null;

        Time[] secTime = new Time[41];

        Time[] hmsTime = new Time[41];

        private long day = 24 * 60 * 60;

        private long[] hms = { 30, 60, 120, 300, 600, 1800, 3600, 7200, 18000, 43200 };

        private int Day = 3;

        private int HourMin = 2;

        private int Sec = 1;

        Time dur = null;

        boolean setByText = false;

        int lastValue = 20;

        public DurationListener() {
            setDurTimeValues();
        }

        public void setFromText(Time durI) {
            dur = durI;
            setByText = true;
            for (int t = durTime.length - 1; t >= 0; t--) {
                if (dur.compareTo(durTime[t]) >= 0) {
                    durSlider.setValue(t);
                    lastValue = t;
                    break;
                }
            }
        }

        public void setDurTimeValues() {
            long mant, m;
            byte exp, e;
            int timeFormat = posDurCubby.getTimeFormat();
            double dur = duration.getDoubleValue();
            exp = (byte) Math.floor(Math.log(dur) / Math.log(10));
            mant = (long) Math.floor(dur * Math.pow(10, -1 * exp));
            if (mant >= 5) mant = 5; else if (mant >= 2) mant = 2; else if (mant >= 1) mant = 1; else {
                mant = 5;
                exp -= 1;
            }
            m = mant;
            e = exp;
            secTime[20] = new Time(mant, exp);
            for (int i = 19; i >= 0; i--) {
                if (mant == 5) mant = 2; else if (mant == 2) mant = 1; else {
                    mant = 5;
                    exp -= 1;
                }
                secTime[i] = new Time(mant, exp);
            }
            secTime[0] = new Time(0.0);
            mant = m;
            exp = e;
            for (int i = 21; i <= 40; i++) {
                if (mant == 1) mant = 2; else if (mant == 2) mant = 5; else {
                    mant = 1;
                    exp += 1;
                }
                secTime[i] = new Time(mant, exp);
            }
            mant = m;
            exp = e;
            int startRegime = 0;
            int regime = 0;
            int startIdx = 0;
            int idx = 0;
            if (dur >= hms[0] && dur <= hms[9]) {
                while (dur > hms[startIdx]) startIdx++;
                startRegime = HourMin;
            } else if (dur > hms[9]) {
                startRegime = Day;
                double days = Math.rint(dur / day);
                exp = (byte) Math.floor(Math.log(days) / Math.log(10));
                mant = (long) Math.floor(days * Math.pow(10, -1 * exp));
                if (mant >= 5) mant = 5; else if (mant >= 2) mant = 2; else if (mant >= 1) mant = 1; else {
                    mant = 5;
                    exp -= 1;
                }
                m = mant;
                e = exp;
            } else startRegime = Sec;
            regime = startRegime;
            idx = startIdx;
            if (regime == Sec) {
                hmsTime[20] = new Time(mant, exp);
                for (int i = 19; i >= 0; i--) {
                    if (mant == 5) mant = 2; else if (mant == 2) mant = 1; else {
                        mant = 5;
                        exp -= 1;
                    }
                    hmsTime[i] = new Time(mant, exp);
                }
            } else if (regime == HourMin) {
                hmsTime[20] = new Time(hms[idx], (byte) 0);
                for (int i = 19; i >= 0; i--) {
                    if (--idx == -1) {
                        regime = Sec;
                        mant = 2;
                        exp = 1;
                    }
                    if (regime == HourMin) hmsTime[i] = new Time(hms[idx], (byte) 0); else {
                        if (mant == 5) mant = 2; else if (mant == 2) mant = 1; else {
                            mant = 5;
                            exp -= 1;
                        }
                        hmsTime[i] = new Time(mant, exp);
                    }
                }
            } else {
                hmsTime[20] = new Time(day * mant, exp);
                for (int i = 19; i >= 0; i--) {
                    idx--;
                    if (mant == 5) mant = 2; else if (mant == 2) mant = 1; else {
                        mant = 5;
                        exp -= 1;
                    }
                    if (regime == Day && mant * Math.pow(10, exp) < 1) {
                        regime = HourMin;
                        idx = 9;
                    } else if (regime == HourMin && idx < 0) {
                        regime = Sec;
                        mant = 1;
                        exp = 1;
                    }
                    if (regime == Day) hmsTime[i] = new Time(day * mant, exp); else if (regime == HourMin) hmsTime[i] = new Time(hms[idx], (byte) 0); else hmsTime[i] = new Time(mant, exp);
                }
            }
            hmsTime[0] = new Time(0.0);
            regime = startRegime;
            idx = startIdx;
            mant = m;
            exp = e;
            if (regime == Day) {
                for (int i = 21; i <= 40; i++) {
                    if (mant == 1) mant = 2; else if (mant == 2) mant = 5; else {
                        mant = 1;
                        exp++;
                    }
                    hmsTime[i] = new Time(day * mant, exp);
                }
            } else if (regime == HourMin) {
                for (int i = 21; i <= 40; i++) {
                    idx++;
                    if (mant == 1) mant = 2; else if (mant == 2) mant = 5; else {
                        mant = 1;
                        exp++;
                    }
                    if (regime == HourMin && idx > 9) {
                        regime = Day;
                        mant = 1;
                        exp = 0;
                    }
                    if (regime == HourMin) hmsTime[i] = new Time(hms[idx], (byte) 0); else hmsTime[i] = new Time(day * mant, exp);
                }
            } else {
                for (int i = 21; i <= 40; i++) {
                    idx++;
                    if (mant == 1) mant = 2; else if (mant == 2) mant = 5; else {
                        mant = 1;
                        exp++;
                    }
                    if (regime == Sec && mant * Math.pow(10, exp) > 10) {
                        regime = HourMin;
                        idx = 0;
                    } else if (regime == HourMin && idx > 9) {
                        regime = Day;
                        mant = 1;
                        exp = 0;
                    }
                    if (regime == Sec) hmsTime[i] = new Time(mant, exp); else if (regime == HourMin) hmsTime[i] = new Time(hms[idx], (byte) 0); else hmsTime[i] = new Time(day * mant, exp);
                }
            }
            if (timeFormat == Time.Unspecified) durTime = secTime; else durTime = hmsTime;
            duration = durTime[20];
            posDurCubby.setDuration(duration, true);
            durSlider.setValue(20);
        }

        public void updateTimeFormat(int timeFormat) {
            if (timeFormat == Time.AbsoluteSeconds1970 || timeFormat == Time.RelativeSeconds) durTime = hmsTime; else durTime = secTime;
            updateSlider();
        }

        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (durSlider.getValue() == lastValue) {
            } else {
                updateSlider();
            }
            lastValue = durSlider.getValue();
        }

        private synchronized void updateSlider() {
            dsval = durSlider.getValue();
            duration = durTime[dsval];
            if (setByText) {
                setByText = false;
                if (duration.compareTo(dur) < 0) {
                    durSlider.setValue(dsval + 1);
                    duration = durTime[dsval + 1];
                }
            }
            posDurCubby.setDuration(duration, true);
            double power = Math.log(duration.getDoubleValue() / 100) / Math.log(10);
            if (power >= 0) posDurCubby.setPrecision(0); else posDurCubby.setPrecision((int) Math.ceil(Math.abs(power)));
            durLabel.setText(makeDurLabel(duration));
        }
    }

    class LWContainer extends JComponent {

        public LWContainer() {
        }

        public void paint(Graphics g) {
            super.paint(g);
        }
    }

    class FWField extends JTextField {

        private Font f = Environment.FONT10;

        public FWField(String text, String refWidth, int align) {
            super(text, refWidth.length());
            setFont(f);
        }
    }

    class PositionTextListener implements ActionListener, FocusListener {

        public void actionPerformed(ActionEvent ae) {
            Time[] newpos = new Time[3];
            try {
                String pos = ((JTextField) ae.getSource()).getText().trim();
                if (pos.indexOf(":") == -1) {
                    newpos[0] = new Time(numberformat.parse(pos).doubleValue());
                } else {
                    newpos[0] = Time.fromFormattedString(pos);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            newpos[1] = position[1];
            newpos[2] = position[2];
            changePosition(newpos);
            posDurCubby.setPosition(newpos, true);
        }

        public void focusGained(FocusEvent fe) {
            showStartTime = true;
            posDurCubby.setPositionAtStart(showStartTime);
            runModeCubby.set(RunModeDefs.current, true);
            makePosTextLabel(RunModeDefs.current);
            for (int i = 0; i < buttons.getComponentCount(); i++) {
                buttons.getComponent(i).setBackground(Environment.BGCOLOR);
                if (buttons.getComponent(i).getName().equals(Integer.toString(RunModeDefs.stop))) buttons.getComponent(i).setBackground(Environment.BGCOLOR.darker());
            }
        }

        public void focusLost(FocusEvent fe) {
        }
    }

    class DurationTextListener implements ActionListener, FocusListener {

        public void actionPerformed(ActionEvent ae) {
            Time newdur = null;
            try {
                String dur = ((JTextField) ae.getSource()).getText().trim();
                newdur = new Time(numberformat.parse(dur).doubleValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            duration = newdur;
            posDurCubby.setDuration(duration, true);
            AdjustmentListener[] listener = durSlider.getAdjustmentListeners();
            for (int i = 0; i < listener.length; i++) {
                durSlider.removeAdjustmentListener(listener[i]);
            }
            for (int i = 0; i < listener.length; i++) {
                if (listener[i] instanceof DurationListener) {
                    ((DurationListener) listener[i]).setFromText(duration);
                }
                durSlider.addAdjustmentListener(listener[i]);
            }
        }

        public void focusGained(FocusEvent fe) {
            showStartTime = true;
            posDurCubby.setPositionAtStart(showStartTime);
            changePosition(position);
            runModeCubby.set(RunModeDefs.current, true);
            makePosTextLabel(RunModeDefs.current);
            for (int i = 0; i < buttons.getComponentCount(); i++) {
                buttons.getComponent(i).setBackground(Environment.BGCOLOR);
                if (buttons.getComponent(i).getName().equals(Integer.toString(RunModeDefs.stop))) buttons.getComponent(i).setBackground(Environment.BGCOLOR.darker());
            }
        }

        public void focusLost(FocusEvent fe) {
        }
    }

    class FWLabel extends JComponent {

        private Font f = Environment.FONT10;

        private FontMetrics fm = getFontMetrics(f);

        private String label = null;

        private int alignment = SwingConstants.LEFT;

        private boolean newLabel = false;

        private int width = 0;

        private int height = 0;

        private int length = 0;

        private int stringOffset = 0;

        private Dimension oldSize = new Dimension(0, 0);

        private Image bufferImage = null;

        public FWLabel(String text, String refWidth, int align) {
            label = text;
            alignment = align;
            width = fm.stringWidth(refWidth);
            height = fm.getAscent() + fm.getDescent();
            stringOffset = fm.getAscent();
        }

        public void setText(String text) {
            synchronized (this) {
                label = text;
                length = fm.stringWidth(label);
                newLabel = true;
            }
            repaint();
        }

        public Dimension getMinimumSize() {
            return new Dimension(width, height + 2);
        }

        public Dimension getPreferredSize() {
            return new Dimension(width, height + 2);
        }

        public void update(Graphics g) {
            paint(g);
        }

        public void paint(Graphics g) {
            boolean newSize = false;
            synchronized (this) {
                Dimension size = getSize();
                if (size.width != oldSize.width || size.height != oldSize.height) newSize = true;
                if (newSize || newLabel) {
                    if (newSize) bufferImage = createImage(size.width, size.height);
                    Graphics bi = bufferImage.getGraphics();
                    bi.clearRect(0, 0, size.width - 1, size.height - 1);
                    bi.setFont(f);
                    if (alignment == SwingConstants.LEFT) bi.drawString(label, 0, stringOffset); else if (alignment == SwingConstants.RIGHT) bi.drawString(label, size.width - length, stringOffset);
                    bi.dispose();
                    oldSize.width = size.width;
                    oldSize.height = size.height;
                    newLabel = false;
                }
                g.drawImage(bufferImage, 0, 0, null);
            }
            super.paint(g);
        }
    }
}
