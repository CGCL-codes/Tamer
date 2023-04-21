package moda.lab3;

import java.util.ArrayList;

public class BirdsNest {

    private Dish wormsDish;

    private ParentBird parentBird;

    private ArrayList<BabyBird> babyBirds;

    public BirdsNest(int noOfLoops) {
        wormsDish = new Dish();
        parentBird = new ParentBird(wormsDish, noOfLoops);
        babyBirds = new ArrayList<BabyBird>();
        for (int i = 0; i < 13; i++) {
            babyBirds.add(new BabyBird(wormsDish, i));
        }
    }

    public void startLife() {
        ArrayList<Thread> birdThreads = new ArrayList<Thread>();
        Thread parentBirdThread = new Thread(parentBird, "DaddyOwl");
        parentBirdThread.start();
        for (int i = 0; i < babyBirds.size(); i++) {
            Thread t = new Thread(babyBirds.get(i), "BabyOwl " + i);
            t.start();
            birdThreads.add(t);
        }
        try {
            parentBirdThread.join();
            System.out.println("ParentBird has retired, shoot down the kids!");
            for (int i = 0; i < birdThreads.size(); i++) {
                babyBirds.get(i).shootDownBird();
                birdThreads.get(i).interrupt();
                birdThreads.get(i).join();
            }
        } catch (InterruptedException e) {
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        BirdsNest birdsNest = new BirdsNest(10);
        birdsNest.startLife();
    }
}
