package name.huzhenbo.java.patterns.adapter;

class LightAdapter implements Light {

    private ClientLight clientLight;

    public LightAdapter(ClientLight clientLight) {
        this.clientLight = clientLight;
    }

    public void turnOff() {
        clientLight.specificTurnOff();
    }

    public void turnOn() {
        clientLight.specificTurnOn();
    }
}
