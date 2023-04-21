package org.gosdt.ew.view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import org.gosdt.ew.BaseSynchronize;
import org.gosdt.ew.model.Profiles;
import org.gosdt.ew.model.Topic;

public class MainForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private int fWidth = 420;

    private int fHeight = 320;

    private Profiles profile;

    private JTextField timeIntervalTextField = new JTextField();

    private JTextField askCountTextField = new JTextField();

    private JLabel jlbTimeLeft = new JLabel("mm:ss");

    private JLabel timeIntervalLabel;

    private JLabel askCountLabel = new JLabel();

    private JLabel timeBeforeLessonLabel = new JLabel();

    private JButton startLessonButton = new JButton();

    private JButton settingsButton = new JButton();

    private JButton wordTopicsButton = new JButton();

    private JButton uploadVocabularyButton = new JButton();

    private JButton downloadVocabularyButton = new JButton();

    private JComboBox jcbTopic = new JComboBox();

    private JPanel panel = new JPanel(null);

    private Timer startLessonTimer;

    private Timer eachSecondTimer;

    private int timeLeft = 0;

    public MainForm() {
        doInitForm();
        setVisible(true);
    }

    public void changeTimerStatus() {
        if (startLessonTimer == null) return;
        if (startLessonTimer.isRunning()) {
            startLessonTimer.stop();
            eachSecondTimer.stop();
        } else {
            startLessonTimer.start();
            eachSecondTimer.start();
        }
    }

    private void initTimers() {
        timeLeft = profile.getRestTime();
        startLessonTimer = new Timer(profile.getRestTime() * 1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                startLessonButton.doClick();
            }
        });
        eachSecondTimer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                String timeFormat = Integer.toString(timeLeft / 60) + ":" + Integer.toString(timeLeft % 60);
                jlbTimeLeft.setText(timeFormat);
                if (timeLeft <= 0) startLessonButton.doClick();
            }
        });
        eachSecondTimer.setRepeats(true);
        startLessonTimer.setRepeats(true);
        eachSecondTimer.start();
        startLessonTimer.start();
    }

    private void doInitForm() {
        initProfile();
        setResizable(false);
        setSize(fWidth, fHeight);
        centering();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("EW-Learning");
        setLayout(null);
        setLabelsName();
        timeIntervalTextField.setBounds(getLarge() + 5, 5, getSmall(), 25);
        add(getTimeIntervalLabel());
        add(timeIntervalTextField);
        askCountLabel.setBounds(5, 35, getLarge(), 25);
        askCountTextField.setBounds(getLarge() + 5, 35, getSmall(), 25);
        add(askCountLabel);
        add(askCountTextField);
        timeBeforeLessonLabel.setBounds(5, 65, getLarge(), 25);
        jlbTimeLeft.setBounds(getLarge() + 5, 65, getSmall(), 25);
        add(timeBeforeLessonLabel);
        add(jlbTimeLeft);
        startLessonButton.setBounds(5, 105, getSmall() * 2, 25);
        getJcbTopic().setBounds(getSmall() * 2 + 10, 105, getSmall(), 25);
        settingsButton.setBounds(5, 135, getSmall() * 2, 25);
        wordTopicsButton.setBounds(5, 165, getSmall() * 2, 25);
        panel.setBounds(5, 190, getLarge() + getSmall(), 75);
        uploadVocabularyButton.setBounds(0, 5, getSmall() * 2, 25);
        downloadVocabularyButton.setBounds(0, 35, getSmall() * 2, 25);
        panel.add(uploadVocabularyButton);
        panel.add(downloadVocabularyButton);
        add(startLessonButton);
        add(settingsButton);
        add(wordTopicsButton);
        add(getJcbTopic());
        add(panel);
        setButtonListeners();
    }

    private void centering() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void initProfile() {
        setProfile(new Profiles(""));
        Profiles temp = profile.getProfileLoadedByDefault();
        if (temp != null) {
            setProfile(temp);
            initTimers();
        }
    }

    private int getSmall() {
        return (int) (fWidth * 0.27);
    }

    private int getLarge() {
        return (int) (fWidth * 0.7);
    }

    private void setButtonListeners() {
        settingsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new SettingsForm(MainForm.this);
            }
        });
        startLessonButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isNoProfile()) {
                    showProfileDontExistsMessage();
                    return;
                }
                profile.setRestTime(getTimeInterval());
                profile.setAskCount(getAskCount());
                profile.updateCurrentRecord();
                startLessonTimer.setInitialDelay(profile.getRestTime() * 1000);
                timeLeft = profile.getRestTime();
                new LessonForm(MainForm.this);
            }

            private Integer getAskCount() {
                return Integer.decode(askCountTextField.getText());
            }

            private Integer getTimeInterval() {
                return Integer.decode(timeIntervalTextField.getText());
            }
        });
        wordTopicsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (isNoProfile()) {
                    showProfileDontExistsMessage();
                    return;
                }
                new AddToTopicForm(MainForm.this);
            }
        });
        uploadVocabularyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (isNoProfile()) {
                    showProfileDontExistsMessage();
                    return;
                }
                BaseSynchronize bs;
                if (getJcbTopic().getSelectedIndex() == 0) {
                    bs = new BaseSynchronize(profile, null);
                } else {
                    bs = new BaseSynchronize(profile, new Topic(getJcbTopic().getSelectedItem().toString(), profile.getStudyLanguage()));
                }
                bs.uploadVocabulary(MainForm.this);
            }
        });
        downloadVocabularyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isNoProfile()) {
                    showProfileDontExistsMessage();
                    return;
                } else {
                    try {
                        BaseSynchronize bs = new BaseSynchronize(profile, null);
                        bs.downloadVocabulary(MainForm.this);
                        fillTopicList();
                    } finally {
                        MainForm.this.setCursor(Cursor.getDefaultCursor());
                        JOptionPane.showMessageDialog(MainForm.this, "Загрузка справочника завершена");
                    }
                }
            }
        });
    }

    private void setLabelsName() {
        askCountLabel.setText("Количество вопросов в упражнении");
        timeBeforeLessonLabel.setText("Время до начала урока");
        startLessonButton.setText("Начать урок");
        settingsButton.setText("Настройки");
        wordTopicsButton.setText("Словарь");
        getJcbTopic().addItem("Все");
        uploadVocabularyButton.setText("Выгрузить словарь");
        downloadVocabularyButton.setText("Загрузить словарь");
        fillTopicList();
        updateVisualCounts();
    }

    private void fillTopicList() {
        if (isNoProfile()) {
            return;
        }
        ArrayList<Topic> allTopics = Topic.getTopicList("<>", "''", profile.getStudyLanguage());
        for (Topic ewTopic : allTopics) {
            getJcbTopic().addItem(ewTopic.getTopicName());
        }
    }

    public void updateVisualCounts() {
        askCountTextField.setText("" + profile.getAskCount());
        timeIntervalTextField.setText("" + profile.getRestTime());
    }

    public void callNewLessonForm() {
        new NewLessonForm(this);
        fillTopicList();
    }

    public JComboBox getJcbTopic() {
        return jcbTopic;
    }

    public Profiles getProfile() {
        return profile;
    }

    public void setProfile(Profiles profile) {
        this.profile = profile;
    }

    private JLabel getTimeIntervalLabel() {
        if (timeIntervalLabel == null) {
            timeIntervalLabel = new JLabel();
            timeIntervalLabel.setBounds(5, 5, getLarge(), 25);
            timeIntervalLabel.setText("секунд между занятиями");
        }
        return timeIntervalLabel;
    }

    private void showProfileDontExistsMessage() {
        JOptionPane.showMessageDialog(MainForm.this, "Необходимо создать профиль");
    }

    private boolean isNoProfile() {
        return profile.getStudyLanguage().equals("");
    }
}
