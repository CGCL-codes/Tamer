package org.monet.kernel.model.definition;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "sync-lock")
public class SyncLockDeclaration extends WorklockDeclaration {

    public static class Wait {

        @Attribute(name = "task")
        protected String task;

        public String getTask() {
            return task;
        }
    }

    @Element(name = "wait")
    protected Wait wait;

    public Wait getWait() {
        return wait;
    }
}
