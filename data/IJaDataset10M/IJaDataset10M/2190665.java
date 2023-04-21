package examFall2008.Q2;

public class Condo extends Owner_occupied_residence {

    boolean elevator = false;

    public Condo(int area, String country, int cashprice, int rent, boolean elevator) {
        super(area, country, cashprice, rent);
        this.elevator = elevator;
    }

    public void print_info() {
        super.print_info();
        String yesno = elevator ? "yes" : "no";
        System.out.println("Elevator: " + yesno);
    }
}
