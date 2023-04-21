package com.acv.common.model.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.acv.common.exception.ACVEntityException;

/**
 * The selected packages for different cities.
 * @author Bin Chen
 * @deprecated
 */
public class ACVCityPackage extends ACVEntity {

    private static final long serialVersionUID = 2460725910096814279L;

    private Map<ACVCity, ACVPackage> cityPackageMap = new HashMap<ACVCity, ACVPackage>();

    private ACVCars acvCars;

    private int engineFlag;

    public int getEngineFlag() {
        return engineFlag;
    }

    public void setEngineFlag(int engineFlag) {
        this.engineFlag = engineFlag;
    }

    public ACVCars getAcvCars() {
        return acvCars;
    }

    public void setAcvCars(ACVCars acvCars) {
        this.acvCars = acvCars;
    }

    public void addCityPackage(ACVCity acvCity, ACVPackage acvPackage) throws ACVEntityException {
        if (acvCity == null || acvPackage == null) throw new ACVEntityException("The ACVCity or/and ACVPackage object is null.");
        int enginflag = acvPackage.getEngineFlag();
        if (cityPackageMap.isEmpty()) {
            setEngineFlag(enginflag);
        } else if (getEngineFlag() != enginflag) {
            throw new ACVEntityException("The ACVPackage object doesn't have the same engine flag.");
        }
        cityPackageMap.put(acvCity, acvPackage);
    }

    public Map<ACVCity, ACVPackage> getCityPackageMap() {
        return cityPackageMap;
    }

    public String toString() {
        String msg = getCode() + " " + engineFlag + " ";
        if (cityPackageMap != null) {
            Set<ACVCity> acvCitySet = cityPackageMap.keySet();
            for (ACVCity acvCity : acvCitySet) {
                if (acvCity == null) continue;
                msg += acvCity.toString() + ": ";
                ACVPackage acvPackage = cityPackageMap.get(acvCity);
                if (acvPackage != null) {
                    msg += acvPackage.toString() + " ";
                }
            }
        }
        if (acvCars != null) msg += acvCars.toString() + " ";
        return msg;
    }

    protected boolean isSameType(ACVEntity o) {
        return (o instanceof ACVCityPackage) ? true : false;
    }
}
