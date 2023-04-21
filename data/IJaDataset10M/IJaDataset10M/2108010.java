package engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import engine.expr.AbstractBlock.BlockExecution;

public abstract class Command implements Executable {

    private final String name;

    /**
	 * Initialise this Command with its class name.
	 */
    public Command() {
        name = this.getClass().getSimpleName().substring(1);
    }

    public Command(final String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    /**
	 * The runtime instructions for this command.
	 */
    @Override
    public abstract void run(BlockExecution exec);

    @Override
    public String toString() {
        return getName();
    }

    public static Command getCommand(final String name) {
        if (!CmdList.dict.containsKey(name)) if (CmdList.dictNames.containsKey(name)) loadClass2(CmdList.dict, CmdList.dictNames.get(name)); else return null;
        return CmdList.dict.get(name);
    }

    public static void loadClass2(Map<String, Command> dict, final String name) {
        try {
            final Class<?> c = Class.forName(name);
            final Constructor<?> con = c.getConstructor();
            final Command c1 = (Command) con.newInstance();
            dict.put(c1.getName(), c1);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final VerifyError e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            System.out.println(e);
        }
    }
}
