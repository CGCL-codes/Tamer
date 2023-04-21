package example.htdocs.ajax;

public class UpdaterThread implements Runnable {

    public UpdaterThread() {
        new Thread(this).start();
    }

    private static int count = 0;

    /**
	 * updates the field every second
	 */
    @Override
    public void run() {
        while (true) {
            Index.text1.set("" + ++count);
            Index.text2.set("" + count * 2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
