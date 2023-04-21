package com.greentea.relaxation.jnmf.gui.components.project.network.learning.visualization;

import com.greentea.relaxation.algorithms.LearningAlgorithm;
import com.greentea.relaxation.jnmf.gui.MainFrame;
import com.greentea.relaxation.jnmf.gui.components.AbstractComponent;
import com.greentea.relaxation.jnmf.localization.Localizer;
import com.greentea.relaxation.jnmf.localization.StringId;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 27.03.2009 Time: 18:05:36 To change this template
 * use File | Settings | File Templates.
 */
public class VisualizationComponent extends AbstractComponent {

    private ProgressByClassesComponent progressByClassesComponent;

    private Network2i1oVisualization network2i1oVisualization;

    public VisualizationComponent() {
        super(Localizer.getString(StringId.VISUALIZATION));
        progressByClassesComponent = new ProgressByClassesComponent();
        network2i1oVisualization = new Network2i1oVisualization();
        addChild(progressByClassesComponent);
        addChild(network2i1oVisualization);
        setIcon(new ImageIcon(MainFrame.ICONS_DIR + "vis.png"));
    }

    public void onStartLearning(LearningAlgorithm learningAlgorithm) {
        progressByClassesComponent.onStartLearning(learningAlgorithm);
        network2i1oVisualization.onStartLearning(learningAlgorithm);
    }

    public void onCancelLearning(LearningAlgorithm learningAlgorithm) {
        progressByClassesComponent.onCancelLearning(learningAlgorithm);
    }
}
