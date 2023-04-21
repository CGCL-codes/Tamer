package a5b.part3;

public class TestFrame {

    private static final int nuos = 33;

    private static final int nuoelevators = 1;

    public static void main(String[] args) {
        BigMushroom bigMushroom = new BigMushroom(4, true);
        for (long i = 0; i < nuoelevators; i++) {
            Elevator elevator = new Elevator(true);
            elevator.setId(i);
            bigMushroom.addElevator(elevator);
            elevator.getThread().start();
        }
        for (int i = 0; i < nuos; i++) {
            try {
                Smurf.waitUntilNextArrival();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Smurf smurf = new Smurf(bigMushroom, 3 + (int) (11 * Math.random()), true);
            smurf.setId(i);
            smurf.getThread().start();
        }
    }
}
