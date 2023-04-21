package ppltrainer.components;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import ppltrainer.converters.LongConverter;
import ppltrainer.service.statistics.SimpleStatistics;

/**
 *
 * @author  marc
 */
public class StatisticsLabel extends javax.swing.JPanel implements ListCellRenderer {

    private SimpleStatistics statistics;

    /** Creates new form StatisticsLabel */
    public StatisticsLabel() {
        super();
        initComponents();
    }

    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        millisStringConverter = new ppltrainer.components.converters.MillisStringConverter();
        dateStringConverter = new ppltrainer.components.converters.DateStringConverter();
        askedQuestionsLabel = new javax.swing.JLabel();
        questionRatioBarLabel = new javax.swing.JLabel();
        questionRatioBar = new javax.swing.JProgressBar();
        rightQuestionsLabel = new javax.swing.JLabel();
        askedQuestionsValueLabel = new javax.swing.JLabel();
        rightQuestionsValueLabel = new javax.swing.JLabel();
        strategyLabel = new javax.swing.JLabel();
        strategyValueLabel = new javax.swing.JLabel();
        subjectLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        dateValueLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        timeValueLabel = new javax.swing.JLabel();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        millisStringConverter.setDateFormat(dateFormat);
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ppltrainer.OpenPPLTrainerApp.class).getContext().getResourceMap(StatisticsLabel.class);
        askedQuestionsLabel.setText(resourceMap.getString("askedQuestionsLabel.text"));
        askedQuestionsLabel.setName("askedQuestionsLabel");
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), askedQuestionsLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        questionRatioBarLabel.setText(resourceMap.getString("questionRatioBarLabel.text"));
        questionRatioBarLabel.setName("questionRatioBarLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), questionRatioBarLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        questionRatioBar.setName("questionRatioBar");
        questionRatioBar.setStringPainted(true);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${statistics.numberOfFalseAnswers + statistics.numberOfRightAnswers}"), questionRatioBar, org.jdesktop.beansbinding.BeanProperty.create("maximum"));
        binding.setSourceNullValue(1);
        binding.setConverter(new LongConverter());
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${statistics.numberOfRightAnswers}"), questionRatioBar, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);
        rightQuestionsLabel.setText(resourceMap.getString("rightQuestionsLabel.text"));
        rightQuestionsLabel.setName("rightQuestionsLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), rightQuestionsLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        askedQuestionsValueLabel.setName("askedQuestionsValueLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), askedQuestionsValueLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${statistics.numberOfFalseAnswers + statistics.numberOfRightAnswers}"), askedQuestionsValueLabel, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("0");
        bindingGroup.addBinding(binding);
        rightQuestionsValueLabel.setName("rightQuestionsValueLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), rightQuestionsValueLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${statistics.numberOfRightAnswers}"), rightQuestionsValueLabel, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        strategyLabel.setText(resourceMap.getString("strategyLabel.text"));
        strategyLabel.setName("strategyLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), strategyLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        strategyValueLabel.setName("strategyValueLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), strategyValueLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${statistics.strategyName}"), strategyValueLabel, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        subjectLabel.setText(resourceMap.getString("subjectLabel.text"));
        subjectLabel.setName("subjectLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), subjectLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        jLabel1.setName("jLabel1");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), jLabel1, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${statistics.subjectName}"), jLabel1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        dateLabel.setText(resourceMap.getString("dateLabel.text"));
        dateLabel.setName("dateLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), dateLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        dateValueLabel.setName("dateValueLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), dateValueLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${statistics.date}"), dateValueLabel, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue(null);
        binding.setConverter(dateStringConverter);
        bindingGroup.addBinding(binding);
        timeLabel.setText(resourceMap.getString("timeLabel.text"));
        timeLabel.setName("timeLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), timeLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        timeValueLabel.setName("timeValueLabel");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${foreground}"), timeValueLabel, org.jdesktop.beansbinding.BeanProperty.create("foreground"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${statistics.time}"), timeValueLabel, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue(null);
        binding.setConverter(millisStringConverter);
        bindingGroup.addBinding(binding);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(strategyLabel).addComponent(subjectLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE).addComponent(strategyValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addComponent(questionRatioBarLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(questionRatioBar, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(askedQuestionsLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(askedQuestionsValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(dateLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(dateValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(timeLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(timeValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(rightQuestionsLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rightQuestionsValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(subjectLabel).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(strategyLabel).addComponent(strategyValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(dateLabel).addComponent(timeLabel).addComponent(dateValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(timeValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(askedQuestionsLabel).addComponent(askedQuestionsValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(rightQuestionsLabel).addComponent(rightQuestionsValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(questionRatioBarLabel).addComponent(questionRatioBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        bindingGroup.bind();
    }

    public SimpleStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(SimpleStatistics statistics) {
        SimpleStatistics oldStatistics = this.statistics;
        this.statistics = statistics;
        this.firePropertyChange("statistics", oldStatistics, statistics);
    }

    private javax.swing.JLabel askedQuestionsLabel;

    private javax.swing.JLabel askedQuestionsValueLabel;

    private javax.swing.JLabel dateLabel;

    private ppltrainer.components.converters.DateStringConverter dateStringConverter;

    private javax.swing.JLabel dateValueLabel;

    private javax.swing.JLabel jLabel1;

    private ppltrainer.components.converters.MillisStringConverter millisStringConverter;

    private javax.swing.JProgressBar questionRatioBar;

    private javax.swing.JLabel questionRatioBarLabel;

    private javax.swing.JLabel rightQuestionsLabel;

    private javax.swing.JLabel rightQuestionsValueLabel;

    private javax.swing.JLabel strategyLabel;

    private javax.swing.JLabel strategyValueLabel;

    private javax.swing.JLabel subjectLabel;

    private javax.swing.JLabel timeLabel;

    private javax.swing.JLabel timeValueLabel;

    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof SimpleStatistics) {
            this.setStatistics((SimpleStatistics) value);
            if (isSelected) {
                this.setBackground(list.getSelectionBackground());
                this.setForeground(list.getSelectionForeground());
            } else {
                this.setBackground(list.getBackground());
                this.setForeground(list.getForeground());
            }
        }
        return this;
    }
}
