package functions;

class LeftException04 extends Exception {
}

class RightException04 extends Exception {
}

interface LeftInterface04 {

    public void foo() throws LeftException04;
}

interface RightInterface04 {

    public void foo() throws RightException04;
}

public interface Multiple04 extends LeftInterface04, RightInterface04 {
}

class TryIt04 implements Multiple04 {

    public void foo() {
    }
}
