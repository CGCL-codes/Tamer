package legacy;

public class KernelModule {

    private KernelModuleDriver driver;

    private int max_devices;

    public KernelModule() throws Exception {
        this.max_devices = 25;
        driver = new KernelModuleDriver("/proc/emulation_data", max_devices);
        openLinks();
        printLinks();
    }

    public synchronized int[] getLinks() throws Exception {
        int[] conf;
        try {
            conf = driver.getModuleConfiguration();
        } catch (Exception e) {
            throw new Exception("Kernel module driver failed:\n" + e.getMessage());
        }
        return conf;
    }

    public synchronized void setLink(int source, int destination, boolean state) throws Exception {
        if (state) {
            driver.enableLink(source, destination);
        } else {
            driver.disableLink(source, destination);
        }
        try {
            driver.setModuleConfiguration();
        } catch (Exception e) {
            throw new Exception("Kernel module driver failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            KernelModule m = new KernelModule();
            m.closeLinks();
            m.printLinks();
            m.openLinks();
            m.printLinks();
            m.closeLinks();
            m.printLinks();
        } catch (Exception e) {
            System.out.println("Main error.");
            e.printStackTrace();
        }
    }

    public void closeLinks() throws Exception {
        setAll(false);
    }

    public void openLinks() throws Exception {
        setAll(true);
    }

    private void setAll(boolean state) throws Exception {
        for (int i = 0; i < max_devices; i++) {
            for (int j = 0; j < max_devices; j++) {
                setLink(i, j, state);
            }
        }
    }

    public void printLinks() throws Exception {
        int[] l = getLinks();
        for (int i = 0; i < max_devices * max_devices; i++) {
            if (((i + 1) % max_devices) == 0) {
                System.out.println(l[i]);
            } else {
                System.out.print(l[i]);
            }
        }
    }
}
