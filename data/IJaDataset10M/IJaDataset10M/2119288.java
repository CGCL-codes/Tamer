package sears.gui.search;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import sears.gui.MainWindow;
import sears.tools.Utils;

public class FindBox extends JComponent implements FindDialog, EventListener {

    private static final long serialVersionUID = 3347745500011278383L;

    private JSearchField _textField;

    private JButton _nextBtn;

    private JButton _previousBtn;

    private MainWindow _mw;

    public FindBox(MainWindow mw) {
        initComponents();
        addActions();
        _mw = mw;
        _mw.addFB(this);
        setFindEnabled(false);
        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
            }
        });
    }

    private void initComponents() {
        _textField = new JSearchField();
        _nextBtn = new JButton(">");
        _previousBtn = new JButton("<");
        _previousBtn.setEnabled(false);
        _nextBtn.setEnabled(false);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(_textField);
        this.add(_previousBtn);
        this.add(_nextBtn);
        _previousBtn.setVisible(false);
        _nextBtn.setVisible(false);
    }

    private void addActions() {
        _textField.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                String text = ((JTextField) e.getSource()).getText();
                if (text.trim().length() > 0) {
                    _previousBtn.setEnabled(true);
                    _nextBtn.setEnabled(true);
                    Thread next = new Thread(new Runnable() {

                        public void run() {
                            findStringInSubtitle(false);
                        }
                    });
                    next.start();
                } else {
                    _mw.fireFindStop();
                    _previousBtn.setEnabled(false);
                    _nextBtn.setEnabled(false);
                }
            }
        });
        _textField.addKeyListener(new KeyAdapter() {

            private Boolean[] actionRequiredForKeyEvent(KeyEvent e) {
                Boolean[] result = new Boolean[2];
                int keyCode = e.getKeyCode();
                result[0] = (keyCode == KeyEvent.VK_ENTER) || (Utils.isMacPlatform && keyCode == KeyEvent.VK_G && e.isMetaDown());
                result[1] = e.isShiftDown();
                return result;
            }

            public void keyPressed(KeyEvent e) {
                Boolean[] ar = actionRequiredForKeyEvent(e);
                if (ar[0]) {
                    if (ar[1]) {
                        _previousBtn.doClick();
                    } else {
                        _nextBtn.doClick();
                    }
                }
            }
        });
        _nextBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                Thread next = new Thread(new Runnable() {

                    public void run() {
                        findStringInSubtitle(false);
                    }
                });
                next.start();
            }
        });
        _previousBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                Thread previous = new Thread(new Runnable() {

                    public void run() {
                        findStringInSubtitle(true);
                    }
                });
                previous.start();
            }
        });
    }

    /**
	 * performs a find action in the subtitle.
	 * @param previous if true call a previous find action, if false it's a next one
	 */
    private void findStringInSubtitle(boolean previous) {
        _mw.find(this, previous);
    }

    public String getText() {
        return _textField.getText();
    }

    public void setOccurencesCount(int occurencesCount) {
        _textField.setToolTipText(String.valueOf(occurencesCount));
    }

    public void setFindEnabled(Boolean inFlag) {
        this.setEnabled(inFlag);
        _textField.setEnabled(inFlag);
    }
}
