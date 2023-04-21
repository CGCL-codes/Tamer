package de.mordred.gui.pruefung;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.mordred.Frage;
import de.mordred.Main;
import de.mordred.gui.AntwortenPanel;
import de.mordred.gui.FixedPanel;
import de.mordred.gui.FragePanel;
import de.mordred.gui.TopLeiste;
import de.mordred.gui.einstellungen.Einstellungen;
import de.mordred.util.Utility;

public class PruefungsFrageGui extends FixedPanel {

    private static final long serialVersionUID = -6262051369247643631L;

    private FragePanel _frage;

    private AntwortenPanel _antworten;

    private JLabel _zeit;

    private JButton _leftarrow;

    private JButton _rightarrow;

    private JLabel _fortschritt = new JLabel("0 / 0");

    private ArrayList<Frage> _fragen;

    private Frage _currentFrage;

    private int _currentFrageNummer;

    private JButton _auswerten;

    private TopLeiste _topleiste;

    private int _secondsremaining;

    public PruefungsFrageGui() {
        JPanel northpanel = new JPanel();
        _leftarrow = new JButton("<--");
        _leftarrow.addActionListener(changeFrage(false));
        _rightarrow = new JButton("-->");
        _rightarrow.addActionListener(changeFrage(true));
        _auswerten = new JButton("Auswerten");
        _auswerten.setAlignmentX(RIGHT_ALIGNMENT);
        _frage = new FragePanel();
        northpanel.setLayout(new BorderLayout());
        _topleiste = new TopLeiste("Prüfung");
        _topleiste.add(_auswerten, BorderLayout.EAST);
        _topleiste.getPrevious().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                _secondcounter.cancel();
            }
        });
        JPanel linksfragerechts = new JPanel(new GridBagLayout());
        linksfragerechts.add(_leftarrow);
        linksfragerechts.add(_fortschritt);
        linksfragerechts.add(_rightarrow);
        northpanel.add(_topleiste, BorderLayout.NORTH);
        northpanel.add(linksfragerechts, BorderLayout.CENTER);
        northpanel.add(_frage, BorderLayout.SOUTH);
        _antworten = new AntwortenPanel(true);
        this.setLayout(new BorderLayout());
        this.add(northpanel, BorderLayout.NORTH);
        this.add(_antworten, BorderLayout.CENTER);
        _auswerten.addActionListener(auswerten());
    }

    public PruefungsFrageGui(ArrayList<Frage> fragen) {
        this();
        _fragen = fragen;
        for (Frage f : _fragen) {
            f.selectAntwort(-1);
        }
        _zeit = new JLabel();
        _secondsremaining = _fragen.size() * 90;
        _zeit.setText("Zeit: " + Utility.convertSecondstoHHMMSS(_secondsremaining));
        Timer t = new Timer();
        t.scheduleAtFixedRate(_secondcounter, 1000, 1000);
        _topleiste.add(_zeit, BorderLayout.NORTH);
        if (Einstellungen.getInstance().isRandom()) {
            Collections.shuffle(_fragen);
        }
        _fortschritt.setText(_currentFrageNummer + 1 + " / " + fragen.size());
        _currentFrage = _fragen.get(_currentFrageNummer);
        setFrage(_currentFrage);
        this.invalidate();
        this.repaint();
        this.revalidate();
    }

    public void setFrage(Frage frage) {
        if (frage.hatBild()) {
            _frage.setImage(new ImageIcon(frage.getBild()));
        } else {
            _frage.setImage(null);
        }
        _frage.setText(frage.getFragetext());
        _fortschritt.setText(_currentFrageNummer + 1 + " / " + _fragen.size());
        _antworten.setAntworten(frage);
    }

    @Override
    public String getTitel() {
        return "Fragen";
    }

    public ActionListener changeFrage(final boolean up) {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean changed = false;
                if (up && _currentFrageNummer < _fragen.size() - 1) {
                    _currentFrageNummer++;
                    changed = true;
                } else if (!up && _currentFrageNummer > 0) {
                    _currentFrageNummer--;
                    changed = true;
                }
                if (changed) {
                    _currentFrage = _fragen.get(_currentFrageNummer);
                    setFrage(_currentFrage);
                }
            }
        };
    }

    public ActionListener auswerten() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int richtig = 0;
                int falsch = 0;
                for (Frage f : _fragen) {
                    if (f.getSelectedAntwort() == f.getRichtigeantwort()) {
                        richtig++;
                    } else {
                        falsch++;
                    }
                }
                Main.setNextPanel(new Auswertung(richtig, falsch, _fragen));
            }
        };
    }

    private TimerTask _secondcounter = new TimerTask() {

        @Override
        public void run() {
            _secondsremaining--;
            _zeit.setText("Zeit: " + Utility.convertSecondstoHHMMSS(_secondsremaining));
            _zeit.repaint();
            if (_secondsremaining == 0) {
                _leftarrow.setEnabled(false);
                _rightarrow.setEnabled(false);
                _antworten.setButtonsEnabled(false);
                this.cancel();
            }
        }
    };
}
