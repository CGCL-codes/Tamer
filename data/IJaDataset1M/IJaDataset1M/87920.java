package com.docum.view.wrapper;

import com.docum.domain.po.common.CargoDefectGroup;
import com.docum.util.AlgoUtil;

public class CargoDefectGroupTransformer implements AlgoUtil.TransformFunctor<CargoDefectGroupPresentation, CargoDefectGroup> {

    @Override
    public CargoDefectGroupPresentation transform(CargoDefectGroup from) {
        return new CargoDefectGroupPresentation(from);
    }
}
