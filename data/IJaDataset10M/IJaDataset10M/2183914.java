package takatuka.drivers;

import takatuka.drivers.hwImpl.sensor.streams.*;
import takatuka.drivers.hwImpl.sensor.*;
import takatuka.drivers.hwImpl.sensor.msp430.*;
import takatuka.drivers.interfaces.sensors.*;

/**
 * <p>Title: </p>
 * <p>Description:
 *  To use the sensors.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class SensorsFactory {

    private static final SensorsFactory factory = new SensorsFactory();

    public static SensorsFactory getInstanceOf() {
        return factory;
    }

    /**
     * @return Accelerometer sensor driver instance
     */
    public IRead readAccelX() {
        return AccelXRead.getInstanceOf();
    }

    /**
     * @return Accelerometer sensor driver instance
     */
    public IReadStream readAccelXStream() {
        return AccelXStream.getInstanceOf();
    }

    /**
     * @return Accelerometer sensor driver instance
     */
    public IRead readAccelY() {
        return AccelYRead.getInstanceOf();
    }

    /**
     * @return Accelerometer sensor driver instance
     */
    public IReadStream readAccelYStream() {
        return AccelYStream.getInstanceOf();
    }

    /**
     * @return magnet sensor driver instance
     */
    public IRead readMagnetX() {
        return MagXRead.getInstanceOf();
    }

    /**
     * @return magnet sensor driver instance
     */
    public IReadStream readMagnetXStream() {
        return MagXStream.getInstanceOf();
    }

    /**
     * @return magnet sensor driver instance
     */
    public IRead readMagnetY() {
        return MagYRead.getInstanceOf();
    }

    /**
     * @return magnet sensor driver instance
     */
    public IReadStream readMagnetYStream() {
        return MagYStream.getInstanceOf();
    }

    /**
     *
     * @return magnet configuration interface
     */
    public IMagConfig getMagnetConfig() {
        return MagConfig.getInstanceOf();
    }

    /**
     *
     * @return mic configuration interface
     */
    public IMicConfig getMicConfig() {
        return MicConfig.getInstanceOf();
    }

    /**
     * @return microphone sensor
     */
    public IRead readMic() {
        return MicRead.getInstanceOf();
    }

    /**
     * @return microphone sensor
     */
    public IReadStream readMicStream() {
        return MicStream.getInstanceOf();
    }

    /**
     * @return temperature
     */
    public IRead readTemp() {
        return TempRead.getInstanceOf();
    }

    /**
     * @return temperature
     */
    public IRead readMSP430InternalTemp() {
        return MSP430TempRead.getInstanceOf();
    }

    /**
     * 
     * @return light/photo sensor
     */
    public IRead readLight() {
        return LightRead.getInstanceOf();
    }

    /**
     *
     * @return total solar radiation stream
     */
    public IReadStream readLightStream() {
        return LightStream.getInstanceOf();
    }

    /**
     *
     * @return total solar radiation
     */
    public IRead readTotalSolarRadiation() {
        return TotalSolarRadiationRead.getInstanceOf();
    }

    /**
     *
     * @return total solar radiation stream
     */
    public IReadStream readTotalSolarRadiationStream() {
        return RadiationStream.getInstanceOf();
    }

    /**
     *
     * @return humidity
     */
    public IRead readHumidity() {
        return HumidityRead.getInstanceOf();
    }

    /**
     *
     * @return Sounder of MTS300.
     */
    public ISounder getSounder() {
        return Sounder.getInstanceOf();
    }

    /**
     *
     * @return
     */
    public IRead readVoltage() {
        return VoltageRead.getInstanceOf();
    }

    /**
     * 
     * @return
     */
    public IReadStream readVoltageStream() {
        return VoltageStream.getInstanceOf();
    }

    /**
     *
     * @return
     */
    public INotify getUserButtonDrivers() {
        return UserButton.getInstanceOf();
    }
}
