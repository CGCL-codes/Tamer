package org.eclipse.jdt.internal.compiler.codegen;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class ExceptionLabel extends Label {

    public int ranges[] = { POS_NOT_SET, POS_NOT_SET };

    public int count = 0;

    public TypeBinding exceptionType;

    public ExceptionLabel(CodeStream codeStream, TypeBinding exceptionType) {
        super(codeStream);
        this.exceptionType = exceptionType;
    }

    public void place() {
        this.codeStream.registerExceptionHandler(this);
        this.position = this.codeStream.getPosition();
    }

    public void placeEnd() {
        int endPosition = this.codeStream.position;
        if (this.ranges[this.count - 1] == endPosition) {
            this.count--;
        } else {
            this.ranges[this.count++] = endPosition;
        }
    }

    public void placeStart() {
        int startPosition = this.codeStream.position;
        if (this.count > 0 && this.ranges[this.count - 1] == startPosition) {
            this.count--;
            return;
        }
        int length;
        if (this.count == (length = this.ranges.length)) {
            System.arraycopy(this.ranges, 0, this.ranges = new int[length * 2], 0, length);
        }
        this.ranges[this.count++] = startPosition;
    }

    public String toString() {
        String basic = getClass().getName();
        basic = basic.substring(basic.lastIndexOf('.') + 1);
        StringBuffer buffer = new StringBuffer(basic);
        buffer.append('@').append(Integer.toHexString(hashCode()));
        buffer.append("(type=").append(this.exceptionType == null ? CharOperation.NO_CHAR : this.exceptionType.readableName());
        buffer.append(", position=").append(this.position);
        buffer.append(", ranges = ");
        if (this.count == 0) {
            buffer.append("[]");
        } else {
            for (int i = 0; i < this.count; i++) {
                if ((i & 1) == 0) {
                    buffer.append("[").append(this.ranges[i]);
                } else {
                    buffer.append(",").append(this.ranges[i]).append("]");
                }
            }
            if ((this.count & 1) == 1) {
                buffer.append(",?]");
            }
        }
        buffer.append(')');
        return buffer.toString();
    }
}
