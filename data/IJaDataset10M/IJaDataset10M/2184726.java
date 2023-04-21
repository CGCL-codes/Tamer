package clump.language.parsing.expression;

import clump.common.EntityUtils;
import clump.language.ast.specification.expression.IEntityInstance;
import clump.language.ast.specification.expression.impl.EntityInstance;
import opala.lexing.ILexeme;
import opala.lexing.LexemeKind;
import opala.lexing.exception.LexemeNotFoundException;
import opala.scanner.impl.CheckPointScanner;
import opala.scanner.IScanner;
import opala.scanner.exception.ScannerException;

public class ObjectEntityImplementationUnit extends EntityImplementationUnit {

    protected IEntityInstance asArray(IEntityInstance type, IScanner scanner) throws ScannerException, LexemeNotFoundException {
        IEntityInstance returnedType = type;
        while (scanner.currentLexeme().isA(LexemeKind.OPERATOR, "[")) {
            CheckPointScanner cp = CheckPointScanner.newInstance(scanner);
            final ILexeme iLexeme = scanner.scan();
            if (scanner.currentLexeme().isA(LexemeKind.OPERATOR, "]") == false) {
                cp.rollback();
                return returnedType;
            } else {
                cp.commit();
                scanner.scan();
                returnedType = new EntityInstance(EntityUtils.asList("clump", "lang"), "Array", EntityUtils.asList(returnedType));
            }
        }
        return returnedType;
    }
}
