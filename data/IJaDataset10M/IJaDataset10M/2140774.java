package pointscalc;

public class Points {

    private int points = 0;

    private int fat = 0;

    private int fiber = 0;

    private int calories = 0;

    public Points() {
    }

    public int getFat() {
        return this.fat;
    }

    public int getFiber() {
        return this.fiber;
    }

    public int getCalories() {
        return this.calories;
    }

    public Points(int calories, int fiber, int fat) {
        if (fat > 0) {
            this.fat = fat;
        }
        if (calories > 0) {
            this.calories = calories;
        }
        if (fiber > 0) {
            this.fiber = fiber;
        }
        this.calcPoints();
    }

    public int getPoints() {
        return this.points;
    }

    public boolean setFat(int f) {
        if (f >= 0) {
            this.fat = f;
            this.calcPoints();
            return true;
        }
        return false;
    }

    public boolean setFiber(int f) {
        if (f >= 0) {
            this.fiber = f;
            this.calcPoints();
            return true;
        }
        return false;
    }

    public boolean setCalories(int c) {
        if (c >= 0) {
            this.calories = c;
            this.calcPoints();
            return true;
        }
        return false;
    }

    public void calcPoints() {
        double c = (double) this.calories;
        double f = (double) this.fat;
        double p = 0;
        if (this.fiber > 0) {
            if (this.fiber > 4) {
                this.fiber = 4;
            }
            c -= (double) (fiber * 10);
        }
        p = (c / 50) + (f / 12) + 0.5;
        if (p < 0) {
            p = 0;
        }
        this.points = (int) Math.floor(p);
    }
}
