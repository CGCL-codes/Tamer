package org.obe.runtime.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.obe.client.api.repository.DocumentHandlerMetaData;
import org.obe.client.api.tool.ToolInvocation;
import javax.activation.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * A tool agent that invokes a command upon an arbitrary document.  The
 * implementation uses the Java Activation Framework to invoke the requested
 * command for the content type implied by the URL.
 *
 * @author Adrian Price
 */
public class DocumentHandler extends AbstractToolAgent {

    private static final Log _log = LogFactory.getLog(DocumentHandler.class);

    private final DocumentHandlerMetaData _metadata;

    private final class ComponentViewer extends JFrame {

        private static final int VERTICAL_MARGIN = 40;

        private static final int HORIZONTAL_MARGIN = 15;

        ComponentViewer() {
            super(_metadata.getTitle());
            initCompViewer();
        }

        ComponentViewer(String name) {
            super(name);
            initCompViewer();
        }

        private void initCompViewer() {
            setSize(_metadata.getWidth(), _metadata.getHeight());
            getContentPane().setLayout(new BorderLayout());
            if (_metadata.getStatus()) {
                TextField statusBar = new TextField();
                statusBar.setName(UpdateProcessAttributes.STATUS);
                add(statusBar, BorderLayout.SOUTH);
            }
            if (_metadata.getScrollbars()) {
            }
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    setVisible(false);
                }
            });
        }

        void setBean(Component bean) {
            getContentPane().add(bean, BorderLayout.CENTER);
            Dimension start_dim = bean.getPreferredSize();
            if (start_dim.width != 0 && start_dim.height != 0) {
                start_dim.height += VERTICAL_MARGIN;
                start_dim.width += HORIZONTAL_MARGIN;
                setSize(start_dim);
                bean.invalidate();
                bean.validate();
                bean.doLayout();
                show();
            } else {
                show();
                start_dim = bean.getPreferredSize();
                start_dim.height += VERTICAL_MARGIN;
                start_dim.width += HORIZONTAL_MARGIN;
                setSize(start_dim);
                bean.validate();
            }
            setSize(getSize());
            validate();
        }
    }

    public static class URLDataSourceExt extends URLDataSource {

        String _contentType;

        public URLDataSourceExt(URL url) {
            super(url);
            _contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(url.getPath());
        }

        public String getContentType() {
            return _contentType;
        }
    }

    public static class NativeCommand implements CommandObject, Runnable {

        private final String _cmdline;

        private String _filename;

        private String _contentType;

        public NativeCommand(String cmdline) {
            _cmdline = cmdline;
        }

        public void setCommandContext(String name, DataHandler dataHandler) throws IOException {
            DataSource ds = dataHandler.getDataSource();
            if (ds instanceof FileDataSource) _filename = ((FileDataSource) ds).getFile().getCanonicalPath(); else if (ds instanceof URLDataSource) _filename = ((URLDataSource) ds).getURL().toExternalForm();
            _contentType = dataHandler.getContentType();
        }

        public void run() {
            Process process = null;
            try {
                String cmdline = _cmdline;
                if (cmdline.indexOf("%s") > -1) cmdline = cmdline.replaceAll("%s", _filename);
                if (cmdline.indexOf("%t") > -1) cmdline = cmdline.replaceAll("%t", _contentType);
                process = Runtime.getRuntime().exec(cmdline);
                process.waitFor();
            } catch (InterruptedException e) {
                process.destroy();
            } catch (IOException e) {
                _log.error(e);
            }
        }
    }

    public static class NativeCommandInfo extends CommandInfo {

        private final String _cmdline;

        public NativeCommandInfo(String cmdName, String cmdline) {
            super(cmdName, NativeCommand.class.getName());
            _cmdline = cmdline;
        }

        public Object getCommandObject(DataHandler dataHandler, ClassLoader classLoader) throws IOException, ClassNotFoundException {
            CommandObject cmdObj = new NativeCommand(_cmdline);
            cmdObj.setCommandContext(getCommandName(), dataHandler);
            return cmdObj;
        }
    }

    public static class NativeMailcapCommandMap extends MailcapCommandMap {

        private static CommandMap _instance = new NativeMailcapCommandMap();

        private MailcapCommandMap _delegate = (MailcapCommandMap) CommandMap.getDefaultCommandMap();

        private final Map _mce = new HashMap();

        private static class MailcapEntry {

            final String mimeType;

            final CommandInfo[] commandInfo;

            MailcapEntry(String rawtext) {
                StringTokenizer strtok = new StringTokenizer(rawtext, ";");
                mimeType = strtok.nextToken().trim();
                List cmds = new ArrayList(strtok.countTokens());
                cmds.add(new NativeCommandInfo("view", strtok.nextToken()));
                while (strtok.hasMoreTokens()) {
                    String cmd = strtok.nextToken();
                    int eqpos = cmd.indexOf('=');
                    if (eqpos != -1) {
                        String cmdName = cmd.substring(0, eqpos - 1);
                        String cmdline = cmd.substring(eqpos + 1);
                        cmds.add(new NativeCommandInfo(cmdName, cmdline));
                    }
                }
                commandInfo = (CommandInfo[]) cmds.toArray(new CommandInfo[cmds.size()]);
            }
        }

        public static CommandMap getDefaultNativeMailcapCommandMap() {
            return _instance;
        }

        private NativeMailcapCommandMap() {
            _delegate = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            initializeNativeCommands();
        }

        private void initializeNativeCommands() {
            String[] mimeTypes = _delegate.getMimeTypes();
            for (int i = 0; i < mimeTypes.length; i++) {
                String mimeType = mimeTypes[i];
                String[] nativeCmds = _delegate.getNativeCommands(mimeType);
                if (nativeCmds.length > 0) {
                    if (nativeCmds.length > 1) {
                        _log.warn("Ignoring additional mailcap entries for " + mimeType);
                    }
                    MailcapEntry mce = new MailcapEntry(nativeCmds[0]);
                    _mce.put(mimeType, mce);
                }
            }
        }

        public synchronized CommandInfo getCommand(String mimeType, String cmdName) {
            String key = cmdName == null ? "view" : cmdName;
            CommandInfo ci = super.getCommand(mimeType, cmdName);
            if (ci == null) {
                MailcapEntry mc = (MailcapEntry) _mce.get(mimeType);
                if (mc != null) {
                    CommandInfo[] cia = mc.commandInfo;
                    for (int i = 0; i < cia.length; i++) {
                        if (cia[i].getCommandName().equalsIgnoreCase(key)) {
                            ci = cia[i];
                            break;
                        }
                    }
                }
            }
            return ci;
        }

        public synchronized CommandInfo[] getAllCommands(String mimeType) {
            return getCommands(mimeType, super.getAllCommands(mimeType));
        }

        public synchronized CommandInfo[] getPreferredCommands(String mimeType) {
            return getCommands(mimeType, super.getPreferredCommands(mimeType));
        }

        private CommandInfo[] getCommands(String mimeType, CommandInfo[] cia) {
            if (cia == null) {
                MailcapEntry mc = (MailcapEntry) _mce.get(mimeType);
                if (mc != null) cia = (CommandInfo[]) mc.commandInfo.clone();
            }
            return cia;
        }
    }

    private static void write(Writer writer, String key, int value) throws IOException {
        writer.write(key);
        writer.write('=');
        writer.write(String.valueOf(value));
        writer.write(',');
    }

    private static void write(Writer writer, String key, boolean value) throws IOException {
        writer.write(key);
        writer.write('=');
        writer.write(value ? "yes" : "no");
        writer.write(',');
    }

    public DocumentHandler(DocumentHandlerMetaData metadata) {
        _metadata = metadata;
    }

    public void renderInvocationScript(ToolInvocation ti, Writer writer) throws IOException {
        writer.write("window.open(\"");
        writer.write(ti.parameters[0].getValue().toString());
        int n = ti.parameters.length;
        if (n > 1) {
            Object value = ti.parameters[1].getValue();
            if (value != null) {
                writer.write('#');
                writer.write(value.toString());
            }
            if (n > 2) {
                writer.write('?');
                for (int i = 2; i < n; i++) {
                    if (i > 2) writer.write('&');
                    writer.write(ti.parameters[i].getFormalParm().getId());
                    writer.write('=');
                    value = ti.parameters[i].getValue();
                    if (value != null) writer.write(value.toString());
                }
            }
        }
        writer.write("\", \"");
        if (_metadata.getTitle() != null) writer.write(_metadata.getTitle());
        writer.write("\", \"");
        write(writer, "height", _metadata.getHeight());
        write(writer, "width", _metadata.getWidth());
        write(writer, UpdateProcessAttributes.STATUS, _metadata.getStatus());
        write(writer, "toolbar", _metadata.getToolbar());
        write(writer, "menubar", _metadata.getMenubar());
        write(writer, "location", _metadata.getLocation());
        write(writer, "scrollbars", _metadata.getScrollbars());
        writer.write("\");");
    }

    protected int _invokeApplication(ToolInvocation ti) throws InterruptedException, InvocationTargetException {
        String resource = ti.parameters[0].getValue().toString();
        DataSource ds;
        try {
            URL url = new URL(resource);
            if (_log.isDebugEnabled()) _log.debug("Looking for content hander for URL: " + url);
            ds = new URLDataSourceExt(url);
        } catch (MalformedURLException e) {
            File file = new File(resource);
            if (!file.exists()) {
                throw new InvocationTargetException(new FileNotFoundException(resource));
            }
            ds = new FileDataSource(file);
        }
        DataHandler dh = new DataHandler(ds);
        dh.setCommandMap(NativeMailcapCommandMap.getDefaultNativeMailcapCommandMap());
        if (_log.isDebugEnabled()) _log.debug("MIME content type: " + dh.getContentType());
        String cmdName = _metadata.getCommandName();
        if (cmdName == null) cmdName = "view";
        CommandInfo ci = dh.getCommand(cmdName);
        if (ci == null) throw new IllegalArgumentException("Command not found: " + cmdName);
        Object bean = dh.getBean(ci);
        if (bean instanceof Runnable) {
            ((Runnable) bean).run();
        } else if (bean instanceof Component) {
            if (_log.isDebugEnabled()) _log.debug("Found content handler: " + bean);
            final ComponentViewer cv = new ComponentViewer();
            cv.addWindowListener(new WindowAdapter() {

                public void windowClosed(WindowEvent e) {
                    synchronized (cv) {
                        cv.notify();
                    }
                }
            });
            cv.setBean((Component) bean);
            synchronized (cv) {
                cv.wait();
            }
        } else {
            _log.warn("Unknown content handler: " + bean);
        }
        return 0;
    }
}
