package examFall2008.Q2;

public class Residenceexercise {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Residence r = new Residence(120, "K�benhavn");
        Villa v = new Villa(200, "Hellerup", 3000000, 20000, 800);
        Condo c = new Condo(100, "Aarhus", 2000000, 12000, false);
        Apartment a = new Apartment(65, "Odense", 2500, false);
        Residence[] residencedb = { r, v, c, a };
        System.out.println("residencedb[3].elevator: " + ((Apartment) residencedb[3]).elevator);
        for (int i = 0; i < residencedb.length; i++) {
            residencedb[i].print_info();
        }
        int oorcount = 0;
        for (int i = 0; i < residencedb.length; i++) {
            if (residencedb[i] instanceof Owner_occupied_residence) {
                oorcount++;
            }
        }
        System.out.println("Number of owner occupied residences: " + oorcount);
    }
}
