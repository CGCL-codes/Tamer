package p400.srm403;

import utils.ExampleRunner;

/**
 * Created by IntelliJ IDEA.
 * User: smalex
 * Date: 29.05.2008
 * Time: 5:40:04
 */
public class TheLuckyNumbers {

    public int count(int a, int b) {
        int count = 0;
        int i = 1;
        while (true) {
            String s = Integer.toBinaryString(i).replace('0', '4').replace('1', '7');
            i++;
            if (s.length() > 1) {
                if (s.length() > 10) {
                    break;
                }
                int next_num = new Integer(s.substring(1));
                if (next_num >= a && next_num <= b) {
                    count++;
                }
                if (next_num > b) {
                    break;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        ExampleRunner.eq(1, 2, new TheLuckyNumbers().count(1, 10));
        ExampleRunner.eq(2, 0, new TheLuckyNumbers().count(11, 20));
        ExampleRunner.eq(3, 2, new TheLuckyNumbers().count(74, 77));
        ExampleRunner.eq(4, 64, new TheLuckyNumbers().count(1000000, 5000000));
        ExampleRunner.eq(5, 1022, new TheLuckyNumbers().count(1, 1000000000));
        ExampleRunner.eq(6, 2, new TheLuckyNumbers().count(42, 68));
        ExampleRunner.eq(7, 2, new TheLuckyNumbers().count(1, 35));
        ExampleRunner.eq(8, 2, new TheLuckyNumbers().count(25, 70));
        ExampleRunner.eq(9, 2, new TheLuckyNumbers().count(59, 79));
        ExampleRunner.eq(10, 0, new TheLuckyNumbers().count(63, 65));
        ExampleRunner.eq(11, 28, new TheLuckyNumbers().count(42, 18468));
        ExampleRunner.eq(12, 24, new TheLuckyNumbers().count(335, 26501));
        ExampleRunner.eq(13, 24, new TheLuckyNumbers().count(170, 15725));
        ExampleRunner.eq(14, 20, new TheLuckyNumbers().count(479, 29359));
        ExampleRunner.eq(15, 16, new TheLuckyNumbers().count(963, 24465));
        ExampleRunner.eq(16, 28, new TheLuckyNumbers().count(42, 18468));
        ExampleRunner.eq(17, 8, new TheLuckyNumbers().count(6335, 26501));
        ExampleRunner.eq(18, 0, new TheLuckyNumbers().count(15725, 19170));
        ExampleRunner.eq(19, 0, new TheLuckyNumbers().count(11479, 29359));
        ExampleRunner.eq(20, 0, new TheLuckyNumbers().count(24465, 26963));
        ExampleRunner.eq(21, 1020, new TheLuckyNumbers().count(42, 900018467));
        ExampleRunner.eq(22, 1000, new TheLuckyNumbers().count(6335, 900026500));
        ExampleRunner.eq(23, 992, new TheLuckyNumbers().count(19170, 900015724));
        ExampleRunner.eq(24, 992, new TheLuckyNumbers().count(11479, 900029358));
        ExampleRunner.eq(25, 992, new TheLuckyNumbers().count(26963, 900024464));
        ExampleRunner.eq(26, 908, new TheLuckyNumbers().count(47474, 774774747));
        ExampleRunner.eq(27, 1022, new TheLuckyNumbers().count(4, 777777777));
        ExampleRunner.eq(28, 939, new TheLuckyNumbers().count(7, 774747747));
        ExampleRunner.eq(29, 979, new TheLuckyNumbers().count(5, 777474760));
        ExampleRunner.eq(30, 464, new TheLuckyNumbers().count(77447904, 774479991));
        ExampleRunner.eq(31, 1, new TheLuckyNumbers().count(447747444, 447747444));
        ExampleRunner.eq(32, 1, new TheLuckyNumbers().count(447747443, 447747445));
        ExampleRunner.eq(33, 0, new TheLuckyNumbers().count(447747443, 447747443));
        ExampleRunner.eq(34, 1022, new TheLuckyNumbers().count(4, 999999997));
        ExampleRunner.eq(35, 1022, new TheLuckyNumbers().count(1, 999999999));
        ExampleRunner.eq(36, 1020, new TheLuckyNumbers().count(10, 1000000000));
        ExampleRunner.eq(37, 1022, new TheLuckyNumbers().count(1, 999921921));
        ExampleRunner.eq(38, 510, new TheLuckyNumbers().count(1, 100000000));
        ExampleRunner.eq(39, 1022, new TheLuckyNumbers().count(1, 777777777));
        ExampleRunner.eq(40, 512, new TheLuckyNumbers().count(100000000, 1000000000));
        ExampleRunner.eq(41, 672, new TheLuckyNumbers().count(500000, 500000000));
        ExampleRunner.eq(42, 768, new TheLuckyNumbers().count(10000000, 1000000000));
        ExampleRunner.eq(43, 720, new TheLuckyNumbers().count(50000, 500000000));
        ExampleRunner.eq(44, 4, new TheLuckyNumbers().count(500, 850));
        ExampleRunner.eq(45, 20, new TheLuckyNumbers().count(525, 12500));
        ExampleRunner.eq(46, 2, new TheLuckyNumbers().count(4, 7));
        ExampleRunner.eq(47, 414, new TheLuckyNumbers().count(1, 74703586));
        ExampleRunner.eq(48, 1021, new TheLuckyNumbers().count(7, 1000000000));
        ExampleRunner.eq(49, 1, new TheLuckyNumbers().count(4, 4));
        ExampleRunner.eq(50, 1021, new TheLuckyNumbers().count(7, 999999999));
        ExampleRunner.eq(51, 1, new TheLuckyNumbers().count(7, 7));
        ExampleRunner.eq(52, 0, new TheLuckyNumbers().count(50, 51));
        ExampleRunner.eq(53, 1020, new TheLuckyNumbers().count(17, 1000000000));
        ExampleRunner.eq(54, 1022, new TheLuckyNumbers().count(4, 1000000000));
        ExampleRunner.eq(55, 1017, new TheLuckyNumbers().count(77, 1000000000));
        ExampleRunner.eq(56, 16, new TheLuckyNumbers().count(1, 4447));
        ExampleRunner.eq(57, 752, new TheLuckyNumbers().count(2778, 714642470));
        ExampleRunner.eq(58, 1021, new TheLuckyNumbers().count(7, 777777777));
        ExampleRunner.eq(59, 976, new TheLuckyNumbers().count(62419, 987654321));
        ExampleRunner.eq(60, 0, new TheLuckyNumbers().count(999999999, 1000000000));
        ExampleRunner.eq(61, 10, new TheLuckyNumbers().count(1, 605));
        ExampleRunner.eq(62, 0, new TheLuckyNumbers().count(781282001, 972285889));
        ExampleRunner.eq(63, 1017, new TheLuckyNumbers().count(77, 777777777));
        ExampleRunner.eq(64, 0, new TheLuckyNumbers().count(8, 10));
        ExampleRunner.eq(65, 0, new TheLuckyNumbers().count(1, 2));
        ExampleRunner.eq(66, 254, new TheLuckyNumbers().count(1, 44234523));
        ExampleRunner.eq(67, 4, new TheLuckyNumbers().count(1, 47));
    }
}
