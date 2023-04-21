package jset.model.code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.InstructionList;

public class JMethod {

    private JClass inClass;

    private String signature;

    private Method peer;

    private List<JInvoke> invokes = new LinkedList<JInvoke>();

    private List<JInvoke> invokedBy = new LinkedList<JInvoke>();

    private InstructionList instructionList;

    private String bytecodeForComparison;

    public JMethod() {
    }

    public static JMethod create(JClass inClass, String signature, Method peer) {
        return new JMethod(inClass, signature, peer);
    }

    private JMethod(JClass inClass, String signature, Method peer) {
        this.inClass = inClass;
        this.signature = signature;
        this.peer = peer;
    }

    public List<JInvoke> getInvokes() {
        return invokes;
    }

    public void setInvokes(List<JInvoke> invokes) {
        if (invokes != null) {
            this.invokes = invokes;
        } else {
            this.invokes.clear();
        }
    }

    public List<JInvoke> getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(List<JInvoke> invokedBy) {
        if (invokedBy != null) {
            this.invokedBy = invokedBy;
        } else {
            this.invokedBy.clear();
        }
    }

    public String getSignature() {
        return signature;
    }

    public void setPeer(Method method) {
        this.peer = method;
    }

    public Method getPeer() {
        return peer;
    }

    private void initInstructionList() {
        if (peer.getCode() != null && peer.getCode().getCode() != null) {
            instructionList = new InstructionList(peer.getCode().getCode());
        }
    }

    public InstructionList getInstructionList() {
        if (instructionList == null) {
            initInstructionList();
        }
        return instructionList;
    }

    public JClass getInClass() {
        return inClass;
    }

    public String bytecodeToString() {
        if (bytecodeForComparison == null) {
            if (peer.getCode() != null && peer.getCode().getCode() != null) {
                bytecodeForComparison = Utility.codeToString(peer.getCode().getCode(), peer.getConstantPool(), 0, -1, false);
            } else {
                bytecodeForComparison = new String();
            }
        }
        return bytecodeForComparison;
    }

    public Set<JMethod> getDirectApplicationCallTargets() {
        Set<JMethod> calls = new HashSet<JMethod>();
        for (JInvoke invoke : invokes) {
            if (invoke.getTarget().getInClass().isApplicationClass() == true) {
                calls.add(invoke.getTarget());
            }
        }
        return calls;
    }

    public Set<JMethod> getDirectCallTargets() {
        Set<JMethod> calls = new HashSet<JMethod>();
        for (JInvoke invoke : invokes) {
            calls.add(invoke.getTarget());
        }
        return calls;
    }

    public Set<JMethod> getTransitiveApplicationCallTargets() {
        ArrayList<JMethod> workList = new ArrayList<JMethod>(getDirectApplicationCallTargets());
        Set<JMethod> calledMethods = new HashSet<JMethod>();
        while (workList.size() > 0) {
            JMethod current = workList.remove(0);
            calledMethods.add(current);
            for (JMethod method : current.getDirectApplicationCallTargets()) {
                if (calledMethods.contains(method) == false && workList.contains(method) == false) {
                    workList.add(method);
                }
            }
        }
        return calledMethods;
    }

    public boolean equalBytecode(JMethod method) {
        return bytecodeToString().equals(method.bytecodeToString());
    }

    public boolean equalsWithBytecode(JMethod method) {
        return equalsWithoutBytecode(method) && equalBytecode(method);
    }

    public boolean equalsWithoutBytecode(JMethod method) {
        if (method == null) {
            return false;
        }
        if (inClass.getName().equals(method.getInClass().getName()) == false) {
            return false;
        }
        if (signature.equals(method.getSignature()) == false) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((inClass == null) ? 0 : inClass.hashCode());
        result = prime * result + ((signature == null) ? 0 : signature.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        JMethod other = (JMethod) obj;
        if (inClass == null) {
            if (other.inClass != null) return false;
        } else if (!inClass.equals(other.inClass)) return false;
        if (signature == null) {
            if (other.signature != null) return false;
        } else if (!signature.equals(other.signature)) return false;
        return true;
    }

    @Override
    public String toString() {
        return getSignature();
    }
}
