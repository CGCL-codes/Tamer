package loengud.yheteistkymnes;

public class Rek {

    public static void main(String[] args) {
        rek(10);
        System.out.println("lqpp");
    }

    public static int rek(int n) {
        int x;
        if (n > 0) {
            int result = rek(n - 1);
            return result;
        } else return 0;
    }
}
