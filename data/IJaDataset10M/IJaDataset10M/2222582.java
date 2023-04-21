package root;

public class Main {

    public static MedlemsHaandtering mh;

    public static void main(String[] args) {
        mh = File.getAll();
        SpringB hovedvindue = new SpringB();
        hovedvindue.setLocationRelativeTo(null);
        hovedvindue.setVisible(true);
    }
}
