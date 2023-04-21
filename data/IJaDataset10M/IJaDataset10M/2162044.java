package hs485;

import java.beans.*;
import java.io.*;

public class JCU10State {

    public float temperature = 0;

    public int humidity = 0;

    public JCU10State() {
    }

    JCU10State(float temperature, int humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public static JCU10State readObject(InputStream in) throws IOException {
        JCU10State result = null;
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(in));
        result = (JCU10State) d.readObject();
        d.close();
        return result;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String toString() {
        return "[temperature=" + temperature + "°C, humidity=" + humidity + "%]";
    }

    public void writeObject(OutputStream out) throws IOException {
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(out));
        e.writeObject(this);
        e.close();
    }
}
