package de.fhdarmstadt.fbi.dtree.ui.wizzards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import de.fhdarmstadt.fbi.dtree.model.EmptyAlphabetException;
import de.fhdarmstadt.fbi.dtree.model.IllegalPatternException;
import de.fhdarmstadt.fbi.dtree.model.SimplePattern;
import de.fhdarmstadt.fbi.dtree.testdata.DataGenerator;
import de.fhdarmstadt.fbi.dtree.ui.components.ComponentFactory;
import de.fhdarmstadt.fbi.dtree.ui.components.PatternTextField;
import de.fhdarmstadt.fbi.dtree.ui.components.ResourceBundleSupport;
import de.fhdarmstadt.fbi.dtree.ui.components.WizzardPanel;

public final class SelectPatternWizzardPanel extends WizzardPanel {

    private final class PatternChangeHandler implements ChangeListener {

        public PatternChangeHandler() {
        }

        /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
        public final void stateChanged(final ChangeEvent e) {
            updatePattern();
        }
    }

    private GeneratorWizzardData generatorData;

    private PatternTextField patternTextField;

    private JTextArea errorMessage;

    public SelectPatternWizzardPanel(final GeneratorWizzardData data) {
        this.generatorData = data;
        this.patternTextField = new PatternTextField();
        this.patternTextField.addChangeListener(new PatternChangeHandler());
        this.errorMessage = ComponentFactory.createInfoArea("");
        this.errorMessage.setBackground(Color.red);
        setHasNext(true);
        setHasPrev(true);
        setHasFinish(false);
        setTitle(createTitle());
        setPane(createContent());
    }

    private JPanel createTitle() {
        final JPanel title = new JPanel();
        title.setBorder(new EmptyBorder(5, 5, 5, 5));
        title.setLayout(new BorderLayout());
        title.setBackground(UIManager.getColor("controlShadow"));
        title.add(ComponentFactory.createInfoArea(ResourceBundleSupport.getString("wordgenerator.pattern.title")));
        return title;
    }

    private JPanel createContent() {
        final JPanel content = new JPanel();
        content.setBorder(new EmptyBorder(5, 5, 5, 5));
        content.setLayout(new GridBagLayout());
        final JLabel patternLabel = new JLabel(ResourceBundleSupport.getString("wordgenerator.pattern.label"));
        patternLabel.setMinimumSize(new Dimension(120, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 2);
        gbc.ipadx = 60;
        content.add(patternLabel, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 2, 2, 0);
        content.add(patternTextField, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 2, 2, 0);
        content.add(errorMessage, gbc);
        final JPanel contentCarrier = new JPanel();
        contentCarrier.setLayout(new BorderLayout());
        contentCarrier.add(content, BorderLayout.NORTH);
        return contentCarrier;
    }

    public final void start() {
        super.start();
        patternTextField.setSimplePattern(generatorData.getPattern());
    }

    protected final void updatePattern() {
        if (patternTextField.getText().length() == 0) {
            generatorData.setPattern(null);
            generatorData.setGenerator(null);
            errorMessage.setText(ResourceBundleSupport.getString("wordgenerator.pattern.error-empty-pattern"));
            setHasNext(false);
            return;
        }
        try {
            final SimplePattern simplePattern = patternTextField.createSimplePattern();
            generatorData.setGenerator(new DataGenerator(simplePattern, generatorData.getDataBackend().getAlphabet()));
            generatorData.setPattern(simplePattern);
            errorMessage.setText("");
        } catch (EmptyAlphabetException ea) {
            generatorData.setPattern(null);
            generatorData.setGenerator(null);
            errorMessage.setText(ResourceBundleSupport.getString("wordgenerator.pattern.error-empty-alphabet"));
        } catch (IllegalPatternException ipe) {
            generatorData.setPattern(null);
            generatorData.setGenerator(null);
            errorMessage.setText(ResourceBundleSupport.formatMessage("wordgenerator.pattern.error-invalid-char", ipe.getInvalidCharacter()));
        } catch (Exception e) {
            generatorData.setPattern(null);
            generatorData.setGenerator(null);
            errorMessage.setText("wordgenerator.pattern.error-invalid-pattern");
        }
        setHasNext(generatorData.getPattern() != null);
    }
}
