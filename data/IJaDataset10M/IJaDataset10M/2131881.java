package net.sf.implessbean;

/**
 * @author Kohji Nakamura
 */
public interface ICarSpec {

    int getCarId();

    void setCarId(int id);

    String getPowerTrain();

    void setPowerTrain(String powerTrain);

    String getMaxPower();

    void setMaxPower(String power);

    String getMaxTorque();

    void setMaxTorque(String torque);
}
