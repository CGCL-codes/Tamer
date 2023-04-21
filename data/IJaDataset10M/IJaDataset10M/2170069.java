package carInheritance;

public class FordTaurus extends Car {

    public FordTaurus(String color, int seats, boolean allWheelDrive) {
        super(color, seats, allWheelDrive);
        setColor("red");
        setSeats(4);
        setAllWheelDrive(false);
    }

    public void drive() {
        System.out.println("Driving a Ford Taurus.");
    }
}
