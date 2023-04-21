package tablegen;

public class GCSineWindows {

    public static void main(String[] args) {
        Utils.printTable(generateSineWindow(1024 / 4), "sine window long");
        Utils.printTable(generateSineWindow(128 / 4), "sine window short");
    }

    private static float[] generateSineWindow(int len) {
        float[] f = new float[len];
        for (int i = 0; i < len; i++) {
            f[i] = (float) Math.sin((i + 0.5) * (Math.PI / (2.0 * len)));
        }
        return f;
    }
}
