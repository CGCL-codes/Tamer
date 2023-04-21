package tikara.gui.timer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import tikara.gui.images.ImgUtil;
import tikara.gui.main.Context;
import tikara.gui.main.MainWindow;
import tikara.gui.shared.TikaraButton;
import tikara.gui.utilities.Language;
import tikara.gui.utilities.Model;

/**
 * 
    Copyright (c) 2008 by Serge Haenni
    
    This file is part of Tikara.

    Tikara is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Tikara is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Tikara.  If not, see <http://www.gnu.org/licenses/>.
    
 * @author Serge Haenni
 *
 */
@SuppressWarnings("serial")
public class TimerPanel extends JPanel implements ActionListener, MouseListener, Observer {

    private TimerLabel timer;

    private TimerToggleButton start;

    private TikaraButton reset;

    private TimerModel timerModel;

    private GridBagConstraints gbc;

    /**
	 * true if 'start' and 'reset' button should be shown
	 */
    private boolean showButtons;

    /**
	 * Initializes default timer, with 'start' and 'reset' buttons
	 *
	 */
    public TimerPanel() {
        this(180, true);
    }

    /**
	 * Initializes timer, with defined options
	 * @param time Default speaking time
	 * @param showButtons true if 'start' and 'reset' button should be shown
	 */
    public TimerPanel(long time, boolean showButtons) {
        timerModel = new TimerModel();
        timerModel.setSpeakTime(time);
        this.showButtons = showButtons;
        initComponents();
    }

    /**
	 * initializes components and adds it to the JPanel
	 *
	 */
    private void initComponents() {
        setLayout(new GridBagLayout());
        Language lang = Context.getInstance().getTikaraModel().getCurrentLanguage();
        timer = new TimerLabel();
        timer.setForeground(MainWindow.PANEL_FOREGROUND);
        timer.setFont(MainWindow.TIMER_FONT);
        timer.setToolTipText(lang.getTimerChangeSpeakingTimeToolTip());
        timer.addMouseListener(this);
        timerModel.addObserver(timer);
        timerModel.addObserver(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(timer, gbc);
        start = new TimerToggleButton();
        timerModel.addObserver(start);
        start.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(start, gbc);
        reset = new TikaraButton(lang.getButtonReset());
        reset.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(reset, gbc);
        if (!showButtons) {
            start.setVisible(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(start)) {
            timerModel.toggle();
        } else if (e.getSource().equals(reset)) {
            start.reset();
            if (!showButtons) start.setVisible(false);
            timerModel.reset();
        }
    }

    public void update(Observable obs, Object obj) {
        if (obj != null && ((TimerModel) obj).getCurrentState() == TimerModel.STOP_TIMEISUP) start.setVisible(false);
        if (obs != null && obs instanceof TimerModel) {
            TimerModel tm = (TimerModel) obs;
            if (!tm.equals(timerModel)) {
                if (tm.getCurrentState() == TimerModel.STOP_END) timerModel.stop(); else if (tm.getCurrentState() == TimerModel.STOP_BEGINNING) timerModel.stop(); else if (tm.getCurrentState() == TimerModel.STOP_RUNNING) timerModel.stop(); else if (tm.getCurrentState() == TimerModel.STOP_TIMEISUP) timerModel.stop(); else if (tm.getCurrentState() == TimerModel.RUNNING) timerModel.start();
            }
        }
    }

    public void addObserver(Observer obs) {
        timerModel.addObserver(obs);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Language lang = Context.getInstance().getTikaraModel().getCurrentLanguage();
            String newSpeakTime = (String) JOptionPane.showInputDialog(Context.getInstance().getMainWindow(), lang.getTimerNewSpeakingTimeMessage(), lang.getTimerNewSpeakingTimeTitle(), JOptionPane.OK_CANCEL_OPTION, ImgUtil.loadImg("question.png"), null, Model.toMinutes((int) timerModel.getSpeakTime()));
            if (newSpeakTime != null && newSpeakTime.length() > 0) {
                int newTime = Model.parseMinutesString(newSpeakTime);
                if (showButtons) timerModel.setSpeakTime((long) newTime); else timerModel.setSpeakTime((long) newTime, true);
            }
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public TimerModel getTimerModel() {
        return timerModel;
    }

    public void setTimerModel(TimerModel newModel) {
        timerModel.setTime(newModel.getTime());
        timerModel.setCurrentState(newModel.getCurrentState());
        timerModel.setSpeakTime(newModel.getSpeakTime());
    }

    /**
	 * If true, the timer will try not to get bigger then it was at 
	 * its initialization.
	 */
    public void setCompactMode(boolean compactMode) {
        timer.setCompactMode(compactMode);
    }
}
