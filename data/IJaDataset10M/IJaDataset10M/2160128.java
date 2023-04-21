package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.List;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;

/**
 * This class implements value signatures
 *
 */
public class ClassSignature extends ValueSignature {

    protected List<MethodSymbol> constructors;

    public ClassSignature(Type ownerClass, List<Symbol.VarSymbol> fields, List<Symbol.MethodSymbol> methods, List<MethodSymbol> constructors) {
        super(ownerClass, SCollectionType.NO_COLLECTION, fields, methods);
        this.constructors = constructors;
    }

    @Override
    public String toString() {
        return "<ClassSIG type=" + type + " collectionType=" + sColType + ">";
    }

    @Override
    public ClassSignature clone() {
        ClassSignature res = new ClassSignature(this.type, this.fields, this.methods, this.constructors);
        res.setAssociatedExpression(this.getAssociatedExpression());
        res.sColType = this.sColType;
        res.setResultSource(this.getResultSource());
        return res;
    }

    @Override
    protected void initNestedSigs(NestingExpression nestingExpression) {
        super.initNestedSigs(nestingExpression);
        for (MethodSymbol constr : constructors) {
            ConstructorSignature sig = JavaSignatureAbstractFactory.getJavaSignatureFactory().createJavaConstructorSignature(constr);
            nestedSigs.add(sig);
        }
    }
}
