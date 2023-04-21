package sisc.data;

import java.io.*;
import sisc.interpreter.*;
import sisc.io.ValueWriter;
import sisc.ser.Serializer;
import sisc.ser.Deserializer;
import sisc.util.ExpressionVisitor;
import sisc.env.LexicalUtils;

public class Closure extends Procedure implements NamedValue {

    public boolean arity;

    public int fcount, boxes[];

    private transient int bl;

    public Value[] env;

    public Expression body;

    public Closure(boolean arity, int fcount, Expression body, Value[] env, int[] boxes) {
        this.arity = arity;
        this.fcount = fcount;
        this.env = env;
        this.body = body;
        this.boxes = boxes;
        bl = (boxes == null ? -1 : boxes.length - 1);
    }

    private final Value[] matchArgs(Interpreter r) throws ContinuationException {
        Value[] vals;
        if (!arity) {
            if (bl < 0) {
                vals = r.vlr;
            } else {
                vals = r.vlrToArgs();
            }
            if (vals.length != fcount) {
                error(r, liMessage(SISCB, "notenoughargsto", toString(), fcount, r.vlr.length));
                return null;
            }
        } else {
            if (r.vlr.length < (fcount - 1)) {
                error(r, liMessage(SISCB, "notenoughargstoinf", toString(), fcount - 1, r.vlr.length));
                return null;
            }
            vals = r.vlrToRestArgs(fcount);
        }
        for (int i = bl; i >= 0; i--) {
            int bi = boxes[i];
            vals[bi] = new Box(vals[bi]);
        }
        return vals;
    }

    public void apply(Interpreter r) throws ContinuationException {
        r.lcl = matchArgs(r);
        r.vlr = null;
        r.env = env;
        r.nxp = body;
    }

    public void display(ValueWriter w) throws IOException {
        displayNamedOpaque(w, "procedure");
    }

    public void serialize(Serializer s) throws IOException {
        long attr = (long) fcount << 1;
        if (arity) attr |= 1;
        s.writeLong(attr);
        LexicalUtils.writeIntArray(s, boxes);
        s.writeExpressionArray(env);
        s.writeExpression(body);
    }

    public Value express() {
        Pair boxs = LexicalUtils.intArrayToList(boxes);
        return list(sym("closure"), new Pair(truth(arity), new Pair(Quantity.valueOf(fcount), boxs)), valArrayToVec(env), body.express());
    }

    public Closure() {
    }

    public void deserialize(Deserializer s) throws IOException {
        long attr = s.readLong();
        fcount = (int) (attr >> 1);
        arity = (attr & 1) != 0;
        boxes = LexicalUtils.readIntArray(s);
        bl = (boxes == null ? -1 : boxes.length - 1);
        env = s.readValueArray();
        body = s.readExpression();
    }

    public boolean visit(ExpressionVisitor v) {
        return super.visit(v) && CallFrame.visitValueArray(v, env) && v.visit(body);
    }
}
