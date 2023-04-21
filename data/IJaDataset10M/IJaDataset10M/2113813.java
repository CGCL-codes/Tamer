package de.outofbounds.kinderactive.gui.learn.numbers;

import de.outofbounds.kinderactive.audio.Audio;
import de.outofbounds.kinderactive.audio.NumberSpeaker;
import de.outofbounds.kinderactive.audio.Speaker;
import de.outofbounds.kinderactive.gui.BasicLabel;
import de.outofbounds.kinderactive.gui.CategoryTab;
import de.outofbounds.kinderactive.gui.LearnButton;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

/**
 *
 * @author root
 */
public class LearnAdditionTab extends CategoryTab {

    int op1, op2;

    ShapesPanel shapesOp1, shapesOp2;

    NumberSpeaker speaker = new NumberSpeaker(false);

    /** Creates a new instance of LearnAdditionTab */
    public LearnAdditionTab() {
        super("intro-addieren", "ask-wievielgibt");
    }

    protected JPanel buildCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1, 3));
        shapesOp1 = new ShapesPanel(true);
        container.add(shapesOp1);
        container.add(new BasicLabel("+"));
        shapesOp2 = new ShapesPanel(true);
        container.add(shapesOp2);
        panel.add(container, BorderLayout.CENTER);
        buttonPanel = buildButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    protected JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" };
        panel.setLayout(new GridLayout(2, 10));
        for (int i = 0; i < numbers.length; i++) {
            String text = numbers[i];
            LearnButton button = new LearnButton(text, speaker, this);
            panel.add(button);
        }
        return panel;
    }

    protected boolean supportsPlayback() {
        return false;
    }

    protected boolean supportsShuffle() {
        return false;
    }

    void generateShapes(LearnButton button) {
        int count = Integer.parseInt(button.getText());
        op1 = (int) (Math.random() * Math.min(count, 10));
        op2 = count - op1;
        shapesOp1.generateShapes(op1);
        shapesOp2.generateShapes(op2);
    }

    protected void askQuestion() {
        solution = randomButton();
        generateShapes(solution);
        Audio.playClip(questionClip);
        speaker.speak(Integer.toString(op1));
        Audio.playClip("numbers/plus");
        speaker.speak(Integer.toString(op2));
        scorePanel.resetAttempts();
    }

    public void actionPerformed(ActionEvent e) {
        if (solution == null) {
            generateShapes((LearnButton) e.getSource());
        }
        super.actionPerformed(e);
    }
}
