package sensorSystem.convertModules;

import dataModels.Data;
import dataModels.DataLaneEnd;

public class ConvertModuleLaneEnd extends ConvertModule {

    public ConvertModuleLaneEnd(Integer delay) {
        super(delay);
    }

    @Override
    protected Data convert(Data data) {
        return data;
    }
}
