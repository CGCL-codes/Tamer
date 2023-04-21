package samples.expectvoid;

public class ExpectVoidDemo {

    public void invokeAPrivateVoidMethod(int something) {
        privateInvoke(something);
    }

    private void privateInvoke(int something) {
        System.out.println("Error in test privateInvoke in class " + ExpectVoidDemo.class.getSimpleName());
    }
}
