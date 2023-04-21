package com.kakao.jini.java.innerclass;

public class InnerClassNew {

    class InnerClass {

        public void inner() {
            System.out.println("i am inner class.");
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        InnerClassNew innerClassNew = new InnerClassNew();
        InnerClassNew.InnerClass innerClass = innerClassNew.new InnerClass();
        innerClass.inner();
    }
}
