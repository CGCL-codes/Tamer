package org.openintents.tools.simulator.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.openintents.tools.simulator.model.SensorsScenarioModel;
import org.openintents.tools.simulator.model.StateModel;
import org.openintents.tools.simulator.view.SensorsScenarioView;
import org.openintents.tools.simulator.view.StateViewBig;

/**
 * Controller of a state in detailed view.
 * @author ilarele
 *
 */
public class StateControllerBig {

    private StateModel mModel;

    private StateViewBig mView;

    private SensorsScenarioView mSensorScenarioView;

    private SensorsScenarioModel mSensorScenarioModel;

    public StateControllerBig(SensorsScenarioModel sensorsScenarioModel, SensorsScenarioView sensorsScenarioView, StateModel stateModel, StateViewBig stateView) {
        mModel = stateModel;
        mView = stateView;
        mSensorScenarioView = sensorsScenarioView;
        mSensorScenarioModel = sensorsScenarioModel;
        initListeners();
    }

    private void initListeners() {
        final JButton importState = mView.getImportStateButton();
        importState.addActionListener(new ActionListener() {

            private StateModel mOwnState = new StateModel(mModel);

            private boolean mIsOwnState = true;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (mIsOwnState) {
                    mModel.copyState(mSensorScenarioModel.getSensorSimulatorModel());
                    importState.setText("Reverse");
                } else {
                    mModel.copyState(mOwnState);
                    importState.setText("Copy from the Simulator");
                }
                mView.refreshFromModel();
                mIsOwnState = !mIsOwnState;
            }
        });
    }
}
