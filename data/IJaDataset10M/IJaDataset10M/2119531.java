package net.sf.mogbox.os.macosx;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.internal.Callback;

public class ApplicationMenuHook {

    private static Logger log = Logger.getLogger(ApplicationMenuHook.class.getName());

    private static final int kHICommandPreferences = ('p' << 24) + ('r' << 16) + ('e' << 8) + 'f';

    private static final int kHICommandAbout = ('a' << 24) + ('b' << 16) + ('o' << 8) + 'u';

    public ApplicationMenuHook() throws Exception {
        final Class<?> os = Class.forName("org.eclipse.swt.internal.carbon.OS");
        final Class<?> hiCommand = Class.forName("org.eclipse.swt.internal.carbon.HICommand");
        final Method getEventKindMethod = os.getMethod("GetEventKind", int.class);
        final Method getEventParameterMethod = os.getMethod("GetEventParameter", int.class, int.class, int.class, int[].class, int.class, int[].class, hiCommand);
        final Method installEventHandler = os.getMethod("InstallEventHandler", int.class, int.class, int.class, int[].class, int.class, int[].class);
        final Method getApplicationEventTarget = os.getMethod("GetApplicationEventTarget");
        final Field commandID = hiCommand.getField("commandID");
        final int kEventProcessCommand = os.getField("kEventProcessCommand").getInt(null);
        final int kEventParamDirectObject = os.getField("kEventParamDirectObject").getInt(null);
        final int kEventClassCommand = os.getField("kEventClassCommand").getInt(null);
        final int typeHICommand = os.getField("typeHICommand").getInt(null);
        final int sizeof = hiCommand.getField("sizeof").getInt(null);
        final int noErr = os.getField("noErr").getInt(null);
        final int eventNotHandledErr = os.getField("eventNotHandledErr").getInt(null);
        Object target = new Object() {

            @SuppressWarnings("unused")
            int commandProc(int nextHandler, int theEvent, int userData) {
                try {
                    int kind = (Integer) getEventKindMethod.invoke(null, theEvent);
                    if (kind == kEventProcessCommand) {
                        Object command = hiCommand.newInstance();
                        getEventParameterMethod.invoke(null, kEventParamDirectObject, typeHICommand, null, sizeof, null, command);
                        switch(commandID.getInt(command)) {
                            case kHICommandPreferences:
                                firePreferencesEvent();
                                return noErr;
                            case kHICommandAbout:
                                fireAboutEvent();
                                return noErr;
                        }
                    }
                } catch (InstantiationException e) {
                    log.log(Level.WARNING, null, e);
                } catch (IllegalArgumentException e) {
                    log.log(Level.WARNING, null, e);
                } catch (IllegalAccessException e) {
                    log.log(Level.WARNING, null, e);
                } catch (InvocationTargetException e) {
                    log.log(Level.WARNING, null, e);
                }
                return eventNotHandledErr;
            }
        };
        Callback commandCallback = new Callback(target, "commandProc", 3);
        long commandProc = commandCallback.getAddress();
        if (commandProc == 0) {
            commandCallback.dispose();
        }
        int[] mask = new int[] { kEventClassCommand, kEventProcessCommand };
        installEventHandler.invoke(null, getApplicationEventTarget.invoke(null), commandProc, mask.length / 2, mask, 0, null);
    }

    private void fireAboutEvent() {
    }

    private void firePreferencesEvent() {
    }
}
