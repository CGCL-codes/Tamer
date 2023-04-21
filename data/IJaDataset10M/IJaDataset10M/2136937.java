package visualizer;

class CalFlow implements Runnable {

    int carnum, count;

    double carwt;

    int pauss;

    double time0, time1, timelap;

    double carflow[] = new double[40];

    Thread flow;

    CalFlow() {
        carnum = 0;
        carwt = 0;
        pauss = 2000;
        time0 = 0;
        time1 = 0;
        for (int k = 0; k < 40; k++) carflow[k] = 0;
        count = 0;
    }

    public void run() {
        while (true) {
            time1 = System.currentTimeMillis();
            timelap = time1 - time0;
            if (timelap > 50) carflow[count] = ((double) (carnum) / timelap) * 1000;
            count = (count + 1) % 40;
            try {
                Thread.sleep(pauss);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void start() {
        flow = new Thread(this);
        flow.start();
    }

    public void stop() {
        flow.stop();
    }
}
