package simpleBus;

public class MainSimpleBus {

    public static void main(String[] args) {
        SimpleBus bus = new SimpleBus();
        bus.getWorkUnits().add(new DeviceImpl());
        Object response = bus.postEvent(DeviceImpl.sampleEvent);
        System.out.println(response);
        response = bus.postEvent("UNKNOWN EVENT");
        System.out.println(response);
    }
}
