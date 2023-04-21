package net.sourceforge.deco.analyzers;

import net.sourceforge.deco.DependencyConsumer;
import net.sourceforge.deco.parsers.api.AbstractApiHandler;
import net.sourceforge.deco.parsers.api.DelayedApiParser;
import net.sourceforge.deco.parsers.full.ClassDependencyConsumer;
import net.sourceforge.deco.parsers.utils.BasicTypeChecker;
import net.sourceforge.deco.parsers.utils.PackageChecker;
import net.sourceforge.deco.parsers.utils.SelfChecker;
import org.objectweb.asm.Type;

/**
 * Analyze the compile dependencies.
 * This class receive the events of a FullParser for all source classes
 * transfer the events to a consumer, and ask a DelayedApiParser to parse
 * some of the API of the classes used in order to check for transitive
 * API compile dependencies (declared exceptions and attributes of overloaded
 * methods).
 * Inheritance transitive dependencies are not considered by this class.  
 */
public class CompileDependencyAnalyzer implements ClassDependencyConsumer {

    private final DependencyConsumer consumer;

    private final DelayedApiParser pathApiParser;

    private final PackageChecker packageChecker;

    private final BasicTypeChecker basicTypeCheck = new BasicTypeChecker();

    private final SelfChecker selfCheck = new SelfChecker();

    private Type curType;

    public CompileDependencyAnalyzer(DependencyConsumer consumer, DelayedApiParser apiParser, PackageChecker packageChecker) {
        this.pathApiParser = apiParser;
        this.consumer = consumer;
        this.packageChecker = packageChecker;
    }

    public void addDependency(Type depClass) {
        consumer.addDependency(curType, depClass);
    }

    @Override
    public void addInvokedMethod(final Type owner, final String methodName, final String methodDesc) {
        final Type curType = this.curType;
        pathApiParser.parse(owner, new AbstractApiHandler() {

            public void declareMethod(String aMethodName, String aMethodDesc, Type[] exceptions) {
                if (aMethodName.equals(methodName)) {
                    if (aMethodDesc.equals(methodDesc)) {
                        for (Type ex : exceptions) {
                            if (!packageChecker.block(ex)) {
                                consumer.addDependency(curType, ex, owner);
                            }
                        }
                    }
                    Type[] paramTypes = Type.getArgumentTypes(aMethodDesc);
                    selfCheck.setCurrentClass(owner);
                    for (int i = 0; i < paramTypes.length; i++) {
                        Type paramType = paramTypes[i];
                        if (paramType.getSort() == Type.ARRAY) {
                            paramType = paramType.getElementType();
                        }
                        if (!basicTypeCheck.block(paramType) && !selfCheck.block(paramType) && !packageChecker.block(paramType)) {
                            consumer.addDependency(curType, paramType, owner);
                        }
                    }
                }
            }
        });
    }

    public void setCurrentClass(Type parsedClass) {
        curType = parsedClass;
    }
}
