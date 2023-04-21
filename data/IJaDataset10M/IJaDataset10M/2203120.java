package javabook.ch08;

public class ExceptionError2 {

    public static void main(String args[]) {
        try {
            System.out.println("매개변수로 받은 두 개의 값");
            int a = Integer.parseInt(args[0]);
            int b = Integer.parseInt(args[1]);
            System.out.println(" a = " + a + " b = " + b);
            System.out.println(" a를 b로 나눈 몫 = " + (a / b));
            System.out.println("나눗셈이 원할히 수행되었습니다.");
        } catch (Exception e) {
            System.out.println("==================================");
            System.out.println("Exception 관련 모든 예외 처리 루틴 : ");
            System.out.println(e + " 예외 발생");
        } finally {
            System.out.println("==================================");
            System.out.println("예외 처리를 끝내고 finally 블럭을 수행합니다");
        }
    }
}
