package am.ik.reading.effective_java.chap02.item06;

public class CorrectStackDriver {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        CorrectStack stack = new CorrectStack();
        for (int i = 0; i < 16; i++) {
            stack.push(i);
        }
        for (int j = 0; j < 16; j++) {
            System.out.println(stack.pop());
        }
    }
}
