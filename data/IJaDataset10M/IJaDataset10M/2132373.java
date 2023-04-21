package net.sf.sstk.retry.argumentsaver;

import net.sf.sstk.retry.ArgumentSaver;
import net.sf.sstk.retry.ArgumentSaverException;

/**
 * @author BjÃ¶rn VoÃŸ
 * 
 */
public abstract class AbstractThreadLocalArgumentSaver implements ArgumentSaver {

    private final ThreadLocal data = new ThreadLocal();

    /**
	 * 
	 * @see net.sf.sstk.retry.ArgumentSaver#restoreArgs()
	 */
    public Object[] restoreArgs() throws ArgumentSaverException {
        final Object[] loadData = (Object[]) this.data.get();
        if (loadData == null) {
            throw new ArgumentSaverException("no data saved in current thread");
        }
        final Object[] loadedArgs = new Object[loadData.length];
        for (int i = 0; i < loadData.length; i++) {
            if (loadData[i] != null) {
                loadedArgs[i] = restorArg(loadData[i]);
            }
        }
        return loadedArgs;
    }

    protected abstract Object restorArg(Object data) throws ArgumentSaverException;

    /**
	 * 
	 * @see net.sf.sstk.retry.ArgumentSaver#saveOriginalArgs(java.lang.Object[])
	 */
    public void saveOriginalArgs(final Object[] args) throws ArgumentSaverException {
        final Object[] saveData = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                saveData[i] = saveArg(args[i]);
            }
        }
        this.data.set(saveData);
    }

    protected abstract Object saveArg(Object origArg) throws ArgumentSaverException;

    public void reset() {
        this.data.remove();
    }
}
