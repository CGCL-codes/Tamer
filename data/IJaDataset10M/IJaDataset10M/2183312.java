package Utilities.NeuralNet;

public class TrainingNetwork {

    private String extension = ".txt";

    private String txtPath = "NNet/encoder/source/";

    private String snetPath = "NNet/encoder/snet/";

    private final String[] characters = { "ad", "bd", "cd", "dd", "ed", "fd", "gd", "hd", "id", "jd", "kd", "ld", "md", "nd", "od", "pd", "qd", "rd", "sd", "td", "ud", "vd", "wd", "xd", "yd", "zd", "Au", "Bu", "Cu", "Du", "Eu", "Fu", "Gu", "Hu", "Iu", "Ju", "Ku", "Lu", "Mu", "Nu", "Ou", "Pu", "Qu", "Ru", "Su", "Tu", "Uu", "Vu", "Wu", "Xu", "Yu", "Zu", "0d", "1d", "2d", "3d", "4d", "5d", "6d", "7d", "8d", "9d" };

    public static void main(String args[]) {
        TrainingNetwork tn = new TrainingNetwork();
        tn.startTraining();
    }

    /**
	 * Run encoders training
	 */
    public void startTraining() {
        for (String s : characters) {
            new Store(txtPath + s + extension, snetPath + s + ".snet");
            System.out.println("Training complete for the classes : " + s);
        }
    }
}
