package biz.sfsservices.hros.tictactoe;

public class Computer {

    public int setSign(char[] bt2, Settings settings) {
        int computerfield = 0;
        for (int i = 0; i < 10; i++) {
            computerfield = (int) (Math.random() * (bt2.length));
            if (bt2[computerfield] == 0) return computerfield;
        }
        for (int i = 0; i < bt2.length; i++) {
            if (bt2[i] == 0) return i;
        }
        return computerfield;
    }
}
