package javabook.ch10;

public class StringBufferExam2 {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        StringBuffer str1 = new StringBuffer("안녕요 자바 ");
        System.out.println("버퍼에 들어있는 내용 => " + str1);
        System.out.println("문자열 끼워넣기 = > " + str1.insert(3, "Power"));
        System.out.println("5번째 버퍼의 내용 = > " + str1.charAt(4));
        str1.setCharAt(0, '정');
        str1.setLength(8);
        System.out.println("버퍼의 새로운 값 = > " + str1);
        System.out.println("문자열의 역순출력하기 = > " + str1.reverse());
    }
}
