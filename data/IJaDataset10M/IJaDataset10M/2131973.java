package net.sourceforge.jdefprog.annorules.syntactic;

import java.util.logging.Logger;
import javax.lang.model.type.TypeKind;
import net.sourceforge.jdefprog.reflection.*;
import net.sourceforge.jdefprog.annorules.SyntacticRules;
import net.sourceforge.jdefprog.msg.MessageEmitter;
import net.sourceforge.jdefprog.msg.MessageType;

public class OnNumbersInParam implements SyntacticRules<Parameter> {

    private static final Logger logger = Logger.getLogger(OnNumbersInParam.class.getCanonicalName());

    @Override
    public boolean checkPosition(Parameter element, MessageEmitter msgEmitter) {
        logger.finest("Checking " + element);
        boolean failure = false;
        TypeKind km = element.getTypeKind();
        logger.finest("Parameter with typekind " + km);
        if (!km.equals(TypeKind.BYTE) && !km.equals(TypeKind.DOUBLE) && !km.equals(TypeKind.FLOAT) && !km.equals(TypeKind.INT) && !km.equals(TypeKind.SHORT) && !km.equals(TypeKind.LONG)) {
            msgEmitter.emit(MessageType.ERROR, "should be positioned only on numbers (byte,float,double,short,int,long)");
            failure = true;
        }
        return !failure;
    }
}
