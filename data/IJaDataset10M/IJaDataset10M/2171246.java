package samples.mockself;

public class MockSelfDemo {

    private int hello;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hello;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final MockSelfDemo other = (MockSelfDemo) obj;
        if (hello != other.hello) return false;
        return true;
    }

    public String aMethod() {
        aMethod2();
        return getString("world");
    }

    public void aMethod2() {
    }

    public String getTwoStrings() {
        return getString() + getString("world2");
    }

    private String getString() {
        return "A String";
    }

    public String getString(String string) {
        return "Hello " + string;
    }

    public String getString2(String string) {
        return "Hello " + string;
    }

    public String getString2() {
        return "Hello world";
    }

    public int timesTwo(Integer anInt) {
        return anInt * 2;
    }

    public int timesTwo(int anInt) {
        return anInt * 2;
    }

    public int timesThree(int anInt) {
        return anInt * 3;
    }
}
