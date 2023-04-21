package com.leclercb.taskunifier.gui.components.models.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.JXColorSelectionButton;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;
import com.leclercb.taskunifier.gui.api.models.GuiGoal;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.commons.converters.ColorConverter;
import com.leclercb.taskunifier.gui.commons.models.GoalContributeModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueGoalLevel;
import com.leclercb.taskunifier.gui.components.models.lists.IModelList;
import com.leclercb.taskunifier.gui.components.models.lists.ModelList;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class GoalConfigurationPanel extends JSplitPane implements IModelList {

    private ModelList modelList;

    public GoalConfigurationPanel() {
        this.initialize();
    }

    @Override
    public Model getSelectedModel() {
        return this.modelList.getSelectedModel();
    }

    @Override
    public void setSelectedModel(Model model) {
        this.modelList.setSelectedModel(model);
    }

    private void initialize() {
        this.setBorder(null);
        final JTextField goalTitle = new JTextField();
        final JComboBox goalLevel = new JComboBox();
        final JComboBox goalContributes = ComponentFactory.createModelComboBox(null, true);
        final JXColorSelectionButton goalColor = new JXColorSelectionButton();
        final JButton removeColor = new JButton();
        goalTitle.setEnabled(false);
        goalLevel.setEnabled(false);
        goalContributes.setEnabled(false);
        goalColor.setEnabled(false);
        removeColor.setEnabled(false);
        this.modelList = new ModelList(new GoalModel(false), goalTitle) {

            private BeanAdapter<Goal> adapter;

            {
                this.adapter = new BeanAdapter<Goal>((Goal) null, true);
                ValueModel titleModel = this.adapter.getValueModel(Model.PROP_TITLE);
                Bindings.bind(goalTitle, titleModel);
                ValueModel levelModel = this.adapter.getValueModel(Goal.PROP_LEVEL);
                goalLevel.setModel(new ComboBoxAdapter<GoalLevel>(GoalLevel.values(), levelModel));
                ValueModel contributesModel = this.adapter.getValueModel(Goal.PROP_CONTRIBUTES);
                goalContributes.setModel(new ComboBoxAdapter<Goal>(new GoalContributeModel(true), contributesModel));
                ValueModel colorModel = this.adapter.getValueModel(GuiModel.PROP_COLOR);
                Bindings.bind(goalColor, "background", new ColorConverter(colorModel));
            }

            @Override
            public Model addModel() {
                return GoalFactory.getInstance().create(Translations.getString("goal.default.title"));
            }

            @Override
            public void removeModel(Model model) {
                GoalFactory.getInstance().markToDelete((Goal) model);
            }

            @Override
            public void modelSelected(Model model) {
                this.adapter.setBean(model != null ? (Goal) model : null);
                goalTitle.setEnabled(model != null);
                goalLevel.setEnabled(model != null);
                goalContributes.setEnabled(model != null);
                goalColor.setEnabled(model != null);
                removeColor.setEnabled(model != null);
                if (model != null) goalContributes.setEnabled(!((Goal) model).getLevel().equals(GoalLevel.LIFE_TIME));
            }
        };
        this.setLeftComponent(this.modelList);
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        rightPanel.setLayout(new BorderLayout());
        this.setRightComponent(ComponentFactory.createJScrollPane(rightPanel, false));
        FormBuilder builder = new FormBuilder("right:pref, 4dlu, fill:default:grow");
        builder.appendI15d("general.goal.title", true, goalTitle);
        builder.appendI15d("general.goal.level", true, goalLevel);
        goalLevel.setRenderer(new DefaultListRenderer(StringValueGoalLevel.INSTANCE));
        goalLevel.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                Goal goal = (Goal) GoalConfigurationPanel.this.modelList.getSelectedModel();
                if (goal == null) {
                    goalContributes.setEnabled(false);
                    return;
                }
                goalContributes.setEnabled(!goal.getLevel().equals(GoalLevel.LIFE_TIME));
            }
        });
        builder.appendI15d("general.goal.contributes", true, goalContributes);
        JPanel p = new JPanel(new BorderLayout(5, 0));
        builder.appendI15d("general.color", true, p);
        goalColor.setPreferredSize(new Dimension(24, 24));
        goalColor.setBorder(BorderFactory.createEmptyBorder());
        removeColor.setIcon(ImageUtils.getResourceImage("remove.png", 16, 16));
        removeColor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((GuiGoal) GoalConfigurationPanel.this.modelList.getSelectedModel()).setColor(null);
            }
        });
        p.add(goalColor, BorderLayout.WEST);
        p.add(removeColor, BorderLayout.EAST);
        rightPanel.add(builder.getPanel(), BorderLayout.CENTER);
        this.setDividerLocation(200);
    }
}
