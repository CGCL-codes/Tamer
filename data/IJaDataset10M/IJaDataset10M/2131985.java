package po.executable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import po.Scope;
import po.token.Token;

public class Java implements IExecutable {

    private Object object;

    private Class<?> klass;

    private Map<String, IExecutable> names;

    /**
	 * 
	 * 
	 * @param args
	 * @param scope
	 * @throws Exception
	 */
    public Java(Scope scope, List<Token> args) throws Exception {
        klass = Class.forName(scope.getIdentifier(args.get(1)));
        try {
            object = klass.newInstance();
        } catch (Exception e) {
        }
        Field[] fields = klass.getFields();
        Method[] methods = klass.getMethods();
        names = new HashMap<String, IExecutable>();
        for (Field field : fields) {
            names.put(field.getName(), new JavaField(object, field));
        }
        for (Method method : methods) {
            String methodName = method.getName();
            if (names.containsKey(methodName)) {
                ((JavaMethod) names.get(methodName)).addMethod(method);
            } else {
                names.put(methodName, new JavaMethod(object, method));
            }
        }
    }

    @Override
    public Token execute(Scope scope, List<Token> tokens) {
        String methodName = scope.getIdentifier(tokens.get(1));
        return Token.COMMAND(names.get(methodName));
    }
}
