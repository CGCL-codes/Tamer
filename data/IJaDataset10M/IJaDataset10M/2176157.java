package ServiceDDS.exception;

public class ImpossibleToCreateDDSTopic extends Exception {

    String topicTypeName;

    public ImpossibleToCreateDDSTopic(String name) {
        super();
        this.topicTypeName = name;
    }

    public String toString() {
        return super.toString() + " with topic type " + topicTypeName;
    }
}
