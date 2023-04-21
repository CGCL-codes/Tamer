package be.vds.jtbdive.core.external.com;

public interface SerialDataComInterface extends DataComInterface {

    public abstract int getBaudRate();

    public abstract int getDataBits();

    public abstract int getParity();

    public abstract String getPort();

    public abstract int getStopBits();

    public abstract void setBaudRate(int baudRate);

    public abstract void setDataBits(int dataBits);

    public abstract void setParity(int parity);

    public abstract void setPort(String port);

    public abstract void setStopBits(int stopBits);
}
