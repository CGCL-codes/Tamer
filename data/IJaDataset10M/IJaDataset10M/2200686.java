package com.greentea.relaxation.jnmf.gui.components.project.network.qualitycontrol;

import com.greentea.relaxation.jnmf.gui.MainFrame;
import com.greentea.relaxation.jnmf.gui.components.AbstractComponent;
import com.greentea.relaxation.jnmf.localization.Localizer;
import com.greentea.relaxation.jnmf.localization.StringId;
import com.greentea.relaxation.jnmf.model.Network;
import com.greentea.relaxation.jnmf.model.IForecastPerformer;
import com.greentea.relaxation.jnmf.util.data.TrainingDataset;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 22.02.2009 Time: 17:21:20 To change this template
 * use File | Settings | File Templates.
 */
public class QualityControlParentComponent extends AbstractComponent {

    private IForecastPerformer forecastPerformer;

    private TrainingDataset allData;

    private TrainingDataset learningData;

    private TrainingDataset testData;

    private StandardQualityControlComponent standardQualityControlComponent;

    private ROCQualityControlComponent rocQualityControlComponent;

    public QualityControlParentComponent() {
        super(Localizer.getString(StringId.QUALITY_CONTROL));
        standardQualityControlComponent = new StandardQualityControlComponent();
        rocQualityControlComponent = new ROCQualityControlComponent();
        addChild(standardQualityControlComponent);
        addChild(rocQualityControlComponent);
        setIcon(new ImageIcon(MainFrame.ICONS_DIR + "autor.gif"));
    }

    public JPanel getControlPanel() {
        return null;
    }

    public void analize() {
        synchronized (forecastPerformer) {
            if (forecastPerformer != null && forecastPerformer.isReady() && allData != null && learningData != null && testData != null) {
                forecastPerformer.setLearningEnabled(false);
                standardQualityControlComponent.analyze(forecastPerformer, allData, learningData, testData);
                rocQualityControlComponent.analyze(forecastPerformer, allData, learningData, testData);
                forecastPerformer.setLearningEnabled(true);
                System.gc();
            }
        }
    }

    @Override
    public void signalParametersChanged(boolean inThisComponent) {
        analize();
    }

    public IForecastPerformer getForecastPerformer() {
        return forecastPerformer;
    }

    public void setForecastPerformer(IForecastPerformer forecastPerformer) {
        this.forecastPerformer = forecastPerformer;
    }

    public TrainingDataset getAllData() {
        return allData;
    }

    public void setAllData(TrainingDataset allData) {
        this.allData = allData;
    }

    public TrainingDataset getLearningData() {
        return learningData;
    }

    public void setLearningData(TrainingDataset learningData) {
        this.learningData = learningData;
    }

    public TrainingDataset getTestData() {
        return testData;
    }

    public void setTestData(TrainingDataset testData) {
        this.testData = testData;
    }
}
