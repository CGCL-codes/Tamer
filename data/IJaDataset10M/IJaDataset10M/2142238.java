package p500.srm524;

import utils.ExampleRunner;

/**
 * Created by IntelliJ IDEA.
 * User: smalex
 * Date: 1/6/12
 * Time: 7:26 PM
 */
public class ShippingCubes {

    public int minimalCost(int N) {
        int result = N + 2;
        for (int i = 1; i <= 200; i++) {
            if (N % i == 0) {
                for (int j = 1; j <= 200; j++) {
                    if (N % (i * j) == 0) {
                        result = Math.min(result, i + j + N / (i * j));
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        ExampleRunner.eq(1, 3, new ShippingCubes().minimalCost(1));
        ExampleRunner.eq(2, 6, new ShippingCubes().minimalCost(6));
        ExampleRunner.eq(3, 9, new ShippingCubes().minimalCost(7));
        ExampleRunner.eq(4, 18, new ShippingCubes().minimalCost(200));
        ExampleRunner.eq(5, 32, new ShippingCubes().minimalCost(58));
        ExampleRunner.eq(6, 99, new ShippingCubes().minimalCost(97));
        ExampleRunner.eq(7, 16, new ShippingCubes().minimalCost(128));
        ExampleRunner.eq(8, 15, new ShippingCubes().minimalCost(120));
        ExampleRunner.eq(9, 8, new ShippingCubes().minimalCost(18));
        ExampleRunner.eq(10, 18, new ShippingCubes().minimalCost(110));
        ExampleRunner.eq(11, 22, new ShippingCubes().minimalCost(38));
        ExampleRunner.eq(12, 153, new ShippingCubes().minimalCost(151));
        ExampleRunner.eq(13, 6, new ShippingCubes().minimalCost(6));
        ExampleRunner.eq(14, 115, new ShippingCubes().minimalCost(113));
        ExampleRunner.eq(15, 33, new ShippingCubes().minimalCost(31));
        ExampleRunner.eq(16, 46, new ShippingCubes().minimalCost(86));
        ExampleRunner.eq(17, 82, new ShippingCubes().minimalCost(158));
        ExampleRunner.eq(18, 11, new ShippingCubes().minimalCost(28));
        ExampleRunner.eq(19, 46, new ShippingCubes().minimalCost(86));
        ExampleRunner.eq(20, 201, new ShippingCubes().minimalCost(199));
        ExampleRunner.eq(21, 69, new ShippingCubes().minimalCost(67));
        ExampleRunner.eq(22, 15, new ShippingCubes().minimalCost(105));
        ExampleRunner.eq(23, 105, new ShippingCubes().minimalCost(103));
        ExampleRunner.eq(24, 17, new ShippingCubes().minimalCost(175));
        ExampleRunner.eq(25, 21, new ShippingCubes().minimalCost(51));
        ExampleRunner.eq(26, 26, new ShippingCubes().minimalCost(46));
        ExampleRunner.eq(27, 11, new ShippingCubes().minimalCost(25));
        ExampleRunner.eq(28, 23, new ShippingCubes().minimalCost(85));
        ExampleRunner.eq(29, 17, new ShippingCubes().minimalCost(55));
        ExampleRunner.eq(30, 63, new ShippingCubes().minimalCost(61));
        ExampleRunner.eq(31, 9, new ShippingCubes().minimalCost(15));
        ExampleRunner.eq(32, 33, new ShippingCubes().minimalCost(31));
        ExampleRunner.eq(33, 7, new ShippingCubes().minimalCost(12));
        ExampleRunner.eq(34, 45, new ShippingCubes().minimalCost(164));
        ExampleRunner.eq(35, 25, new ShippingCubes().minimalCost(95));
        ExampleRunner.eq(36, 9, new ShippingCubes().minimalCost(27));
        ExampleRunner.eq(37, 5, new ShippingCubes().minimalCost(3));
        ExampleRunner.eq(38, 74, new ShippingCubes().minimalCost(142));
        ExampleRunner.eq(39, 34, new ShippingCubes().minimalCost(62));
        ExampleRunner.eq(40, 22, new ShippingCubes().minimalCost(182));
        ExampleRunner.eq(41, 31, new ShippingCubes().minimalCost(161));
        ExampleRunner.eq(42, 27, new ShippingCubes().minimalCost(169));
        ExampleRunner.eq(43, 56, new ShippingCubes().minimalCost(106));
        ExampleRunner.eq(44, 13, new ShippingCubes().minimalCost(80));
        ExampleRunner.eq(45, 15, new ShippingCubes().minimalCost(120));
        ExampleRunner.eq(46, 10, new ShippingCubes().minimalCost(30));
        ExampleRunner.eq(47, 50, new ShippingCubes().minimalCost(94));
        ExampleRunner.eq(48, 34, new ShippingCubes().minimalCost(62));
        ExampleRunner.eq(49, 16, new ShippingCubes().minimalCost(140));
        ExampleRunner.eq(50, 17, new ShippingCubes().minimalCost(180));
        ExampleRunner.eq(51, 92, new ShippingCubes().minimalCost(178));
        ExampleRunner.eq(52, 57, new ShippingCubes().minimalCost(159));
        ExampleRunner.eq(53, 6, new ShippingCubes().minimalCost(6));
        ExampleRunner.eq(54, 63, new ShippingCubes().minimalCost(61));
        ExampleRunner.eq(55, 14, new ShippingCubes().minimalCost(96));
        ExampleRunner.eq(56, 25, new ShippingCubes().minimalCost(152));
        ExampleRunner.eq(57, 14, new ShippingCubes().minimalCost(84));
        ExampleRunner.eq(58, 9, new ShippingCubes().minimalCost(7));
        ExampleRunner.eq(59, 4, new ShippingCubes().minimalCost(2));
        ExampleRunner.eq(60, 5, new ShippingCubes().minimalCost(3));
        ExampleRunner.eq(61, 5, new ShippingCubes().minimalCost(4));
        ExampleRunner.eq(62, 7, new ShippingCubes().minimalCost(5));
        ExampleRunner.eq(63, 9, new ShippingCubes().minimalCost(7));
        ExampleRunner.eq(64, 6, new ShippingCubes().minimalCost(8));
        ExampleRunner.eq(65, 15, new ShippingCubes().minimalCost(13));
        ExampleRunner.eq(66, 13, new ShippingCubes().minimalCost(56));
        ExampleRunner.eq(67, 7, new ShippingCubes().minimalCost(9));
        ExampleRunner.eq(68, 175, new ShippingCubes().minimalCost(173));
        ExampleRunner.eq(69, 159, new ShippingCubes().minimalCost(157));
        ExampleRunner.eq(70, 16, new ShippingCubes().minimalCost(26));
        ExampleRunner.eq(71, 15, new ShippingCubes().minimalCost(33));
        ExampleRunner.eq(72, 9, new ShippingCubes().minimalCost(24));
        ExampleRunner.eq(73, 12, new ShippingCubes().minimalCost(42));
        ExampleRunner.eq(74, 20, new ShippingCubes().minimalCost(198));
        ExampleRunner.eq(75, 15, new ShippingCubes().minimalCost(125));
        ExampleRunner.eq(76, 19, new ShippingCubes().minimalCost(17));
        ExampleRunner.eq(77, 12, new ShippingCubes().minimalCost(64));
        ExampleRunner.eq(78, 133, new ShippingCubes().minimalCost(131));
    }
}
