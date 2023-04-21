package com.docum.view.wrapper;

import com.docum.domain.po.common.CargoPackage;
import com.docum.util.AlgoUtil;

public class CargoPackageTransformer implements AlgoUtil.TransformFunctor<CargoPackagePresentation, CargoPackage> {

    private CargoPresentation cargo;

    public CargoPackageTransformer(CargoPresentation cargo) {
        this.cargo = cargo;
    }

    @Override
    public CargoPackagePresentation transform(CargoPackage from) {
        return new CargoPackagePresentation(from, cargo);
    }
}
