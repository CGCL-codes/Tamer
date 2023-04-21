package samples.innerclassmocking;

/**
 * Demonstrates the ability to mock an inner class (if the inner class is not
 * private).
 * 
 * @author Johan Haleby
 */
public class ClassWithNonPrivateInnerClass {

    public String getMessage() {
        return new InnerClass().getInnerMessage();
    }

    public class InnerClass {

        public String getInnerMessage() {
            return "A message from an inner class!";
        }
    }
}
