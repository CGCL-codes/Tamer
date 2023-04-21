package ppltrainer.components;

import javax.swing.text.DefaultStyledDocument;
import ppltrainer.model.Answer;

/**
 *
 * @author  marc
 */
public class AnswerPanel extends javax.swing.JPanel {

    private Answer answer;

    private String answerLetter;

    private boolean showColor;

    private AnswerPanelGroup answerPanelGroup;

    private boolean selected;

    /** Creates new form AnswerPanel */
    public AnswerPanel() {
        super();
        this.showColor = false;
        initComponents();
    }

    public AnswerPanel(String answerLetter) {
        this();
        this.answerLetter = answerLetter;
    }

    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        answerColorConverter = new ppltrainer.components.converters.AnswerColorConverter();
        answerToDocumentConverter = new ppltrainer.components.converters.AnswerToDocumentConverter();
        scrollPane = new javax.swing.JScrollPane();
        answerTextPane = new javax.swing.JTextPane();
        answerToggleButton = new javax.swing.JToggleButton();
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${answer}"), answerColorConverter, org.jdesktop.beansbinding.BeanProperty.create("answer"));
        bindingGroup.addBinding(binding);
        setName("Form");
        scrollPane.setName("scrollPane");
        answerTextPane.setEditable(false);
        answerTextPane.setName("answerTextPane");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${showColor}"), answerTextPane, org.jdesktop.beansbinding.BeanProperty.create("background"));
        binding.setConverter(answerColorConverter);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${answer}"), answerTextPane, org.jdesktop.beansbinding.BeanProperty.create("styledDocument"));
        binding.setSourceNullValue(new DefaultStyledDocument());
        binding.setConverter(answerToDocumentConverter);
        bindingGroup.addBinding(binding);
        scrollPane.setViewportView(answerTextPane);
        answerToggleButton.setName("answerToggleButton");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${selected}"), answerToggleButton, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), answerToggleButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${answerLetter}"), answerToggleButton, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(0, 0, 0).add(answerToggleButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(answerToggleButton).addContainerGap(29, Short.MAX_VALUE)).add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE));
        bindingGroup.bind();
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        boolean oldShowColor = this.isShowColor();
        if (oldShowColor) {
            this.setShowColor(false);
        }
        Answer oldValue = this.answer;
        this.answer = answer;
        firePropertyChange("answer", oldValue, this.answer);
        this.setShowColor(oldShowColor);
    }

    public String getAnswerLetter() {
        return answerLetter;
    }

    public void setAnswerLetter(String answerLetter) {
        String oldValue = this.answerLetter;
        this.answerLetter = answerLetter;
        firePropertyChange("answerLetter", oldValue, this.answerLetter);
    }

    public boolean isShowColor() {
        return showColor;
    }

    public void setShowColor(boolean showColor) {
        boolean oldValue = this.showColor;
        this.showColor = showColor;
        firePropertyChange("showColor", oldValue, this.showColor);
    }

    public boolean isAnswered() {
        return answerToggleButton.isSelected();
    }

    public void setAnswered(boolean answered) {
        this.answerToggleButton.setSelected(answered);
    }

    public AnswerPanelGroup getAnswerPanelGroup() {
        return answerPanelGroup;
    }

    public void setAnswerPanelGroup(AnswerPanelGroup answerPanelGroup) {
        AnswerPanelGroup oldValue = this.answerPanelGroup;
        this.answerPanelGroup = answerPanelGroup;
        if (oldValue != null && oldValue.containsAnswerPanel(this)) {
            oldValue.removeAnswerPanel(this);
        }
        if (answerPanelGroup != null && !answerPanelGroup.containsAnswerPanel(this)) {
            answerPanelGroup.addAnswerPanel(this);
        }
        firePropertyChange("answerPanelGroup", oldValue, this.answerPanelGroup);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean oldValue = this.selected;
        this.selected = selected;
        firePropertyChange("selected", oldValue, this.selected);
    }

    private ppltrainer.components.converters.AnswerColorConverter answerColorConverter;

    private javax.swing.JTextPane answerTextPane;

    private ppltrainer.components.converters.AnswerToDocumentConverter answerToDocumentConverter;

    private javax.swing.JToggleButton answerToggleButton;

    private javax.swing.JScrollPane scrollPane;

    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
}
