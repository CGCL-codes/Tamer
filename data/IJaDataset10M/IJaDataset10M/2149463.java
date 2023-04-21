package host.simpleServices;

public class Calculator {

    private static ServiceDescription sd = new ServiceDescription("Calculator");

    public String getServiceDescription() {
        return sd.getServiceName();
    }

    public static int add(int number1, int number2) {
        return number1 + number2;
    }
}
