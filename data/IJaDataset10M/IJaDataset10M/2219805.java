package edu.nctu.csie.jichang.dp.creational;

public class Singleton {

    public static void main(String[] args) {
        Strandard.getInstance();
        LazyInit.getInstance();
        DoubleCheck.getInstance();
    }
}

class Strandard {

    private static Strandard strandard = new Strandard();

    private Strandard() {
    }

    public static Strandard getInstance() {
        return strandard;
    }
}

class LazyInit {

    private static LazyInit LazyInit = null;

    private LazyInit() {
    }

    public static synchronized LazyInit getInstance() {
        if (LazyInit == null) {
            LazyInit = new LazyInit();
        }
        return LazyInit;
    }
}

class DoubleCheck {

    private static DoubleCheck doubleCheck = null;

    private DoubleCheck() {
    }

    public static DoubleCheck getInstance() {
        if (doubleCheck == null) {
            synchronized (DoubleCheck.class) {
                if (doubleCheck == null) {
                    doubleCheck = new DoubleCheck();
                }
            }
        }
        return doubleCheck;
    }
}
