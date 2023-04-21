package org.mitre.scap.xccdf.check;

import gov.nist.checklists.xccdf.x11.CheckType;
import gov.nist.checklists.xccdf.x11.InstanceResultType;
import gov.nist.checklists.xccdf.x11.MessageType;
import gov.nist.checklists.xccdf.x11.ResultEnumType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckResult {

    private final ResultEnumType.Enum result;

    private final CheckType checkResult;

    private List<MessageType> messages;

    private InstanceResultType instance;

    public CheckResult(final Check check, final ResultEnumType.Enum result, final CheckType checkResult) {
        this.result = result;
        this.checkResult = checkResult;
    }

    public List<MessageType> getMessages() {
        if (messages == null) return Collections.emptyList();
        return messages;
    }

    public void addMessage(final MessageType m) {
        if (messages == null) messages = new ArrayList<MessageType>();
        messages.add(m);
    }

    public ResultEnumType.Enum getResult() {
        return result;
    }

    public boolean isInstance() {
        return (instance != null);
    }

    public InstanceResultType getInstance() {
        return instance;
    }

    public CheckType getCheckResult() {
        return checkResult;
    }
}
