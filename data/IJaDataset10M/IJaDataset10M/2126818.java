package moda.lab3;

public class BabyBird implements Runnable {

    private Dish dish;

    private int babyNo;

    public boolean stopRunning = false;

    public BabyBird(Dish d, int n) {
        this.dish = d;
        this.babyNo = n;
        this.stopRunning = false;
    }

    public void shootDownBird() {
        System.out.println("BabyBird " + this.babyNo + " stopped!");
        this.stopRunning = true;
    }

    @Override
    public void run() {
        while (!this.stopRunning) {
            if (dish.eat() == false) {
                dish.parentGoHunting();
            }
            System.out.println("BabyBird " + babyNo + " eat a worm, will sleep know");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}
