package tests;

import legoass.Hammer;
import legoass.NXT_Navigator;
import lejos.nxt.*;

public class Test_Drive {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        NXT_Navigator navi = new NXT_Navigator(5.3, 10.0, Motor.A, Motor.B);
        Hammer hammer = new Hammer(Motor.C, true);
        navi.moveTo(25, 0);
        navi.moveTo(25, 25);
        navi.moveTo(0, 25);
        navi.moveTo(0, 50);
        navi.moveTo(25, 50);
        navi.moveTo(25, 75);
        navi.moveTo(0, 75);
        navi.moveTo(0, 0);
    }
}
