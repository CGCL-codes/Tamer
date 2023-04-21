package de.huxhorn.sulky.conditions;

import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ConditionTestBase extends TestCase {

    public void internalTestCondition(Condition condition) throws CloneNotSupportedException, IOException, ClassNotFoundException {
        internalTestCloneEquals(condition);
        internalTestSerialization(condition);
    }

    public void internalTestSerialization(Condition condition) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(condition);
        oos.close();
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(is);
        Condition deserialized = (Condition) ois.readObject();
        internalTestEquals(condition, deserialized);
    }

    public void internalTestCloneEquals(Condition condition) throws CloneNotSupportedException {
        Condition clone = condition.clone();
        internalTestEquals(condition, clone);
    }

    public void internalTestEquals(Condition original, Condition other) {
        assertEquals(original, other);
        if (other instanceof ConditionGroup) {
            ConditionGroup originalGroup = (ConditionGroup) original;
            ConditionGroup clonedGroup = (ConditionGroup) other;
            assertEquals(originalGroup.getConditions(), clonedGroup.getConditions());
        } else if (other instanceof ConditionWrapper) {
            ConditionWrapper originalWrapper = (ConditionWrapper) original;
            ConditionWrapper clonedWrapper = (ConditionWrapper) other;
            assertEquals(originalWrapper.getCondition(), clonedWrapper.getCondition());
        }
    }
}
