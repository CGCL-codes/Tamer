package fleetAdmin;

import java.util.ArrayList;

public class TestCommuter {

    private static ArrayList planesInStock = new ArrayList();

    public static void main(String[] args) {
        planesInStock.add(new PassAircraft("Fokker", "50", "PH-QKM", 2, 50));
        planesInStock.add(new PassAircraft("Fokker", "100", "PA-CPM", 2, 85));
        listPlanes();
        planesInStock.remove(0);
        System.out.println("Sold Aircraft # 0 ...");
        System.out.println();
        listPlanes();
        System.out.println("Reaganging plane # 0");
        System.out.println();
        PassAircraft passaircraft = (PassAircraft) planesInStock.get(0);
        System.out.println("BEFORE MOD:" + passaircraft);
        passaircraft.setPassengers(62);
        System.out.println("It now has " + passaircraft.getPassengers() + " passengers");
        System.out.println("Adding planes ...");
        System.out.println();
        planesInStock.add(new PassAircraft("Saab", "A model", "PT-HIO", 3, 120));
        planesInStock.add(new PassAircraft());
        planesInStock.add(new MixedAircraft("Airbus", "A320", "HF-NOP", 2, 150, 8));
        listPlanes();
        MixedAircraft mixedAircraft = (MixedAircraft) planesInStock.get(planesInStock.size() - 1);
        System.out.println("Current aircraft load:" + mixedAircraft.getFreight());
        mixedAircraft.setFreight(16.2);
        System.out.println("New aircraft load:" + mixedAircraft.getFreight());
        planesInStock.add(new PhotoAircraft());
        PhotoAircraft photoplane = (PhotoAircraft) planesInStock.get(planesInStock.size() - 1);
        System.out.println("Give the photoAircraft some camera's (before):" + photoplane.getContiansHasselblad() + " / " + photoplane.getContainsNikon());
        photoplane.setContiansHasselblad(true);
        photoplane.setContiansNikon(true);
        System.out.println("after:" + photoplane.getContiansHasselblad() + " / " + photoplane.getContainsNikon());
        System.out.println();
        System.out.println("final result:");
        listPlanes();
    }

    private static void listPlanes() {
        System.out.println("Listing planes:");
        System.out.println(" # \tPLANE");
        System.out.println("-------------------------------------------------------------------------");
        for (int planeIndex = 0; planeIndex < planesInStock.size(); planeIndex++) {
            System.out.println(" " + planeIndex + "\t" + planesInStock.get(planeIndex));
        }
        System.out.println();
    }
}
