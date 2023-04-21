package syncclipboard.gui.config;

import java.io.File;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import syncclipboard.clipboard.ClipboardSync;
import syncclipboard.gui.utils.GuiResourcesStore;
import syncclipboard.properties.Properties;
import syncclipboard.sync.serial.SerialSync;
import syncclipboard.utils.LoggerUtil;

/**
 * Configutations for this application.
 * 
 */
public class OptionsShell {

    private static Display display;

    private final Shell optionsShell;

    private static Label infoLabel;

    private static boolean globalIsClipboardTextEnable;

    private static boolean globalIsClipboardFileEnable;

    private static long globalClipboardPollingCycletime;

    private static int globalIncommingReadTimeoout;

    private static long globalMaxTransferBytes;

    private static boolean serialIsEnable;

    private static String serialPort;

    private static int serialBaudrate;

    private static int serialStopBits;

    private static boolean serialParity;

    public OptionsShell(Display dp) {
        getCurrentProperties();
        display = dp;
        optionsShell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        try {
            optionsShell.setImage(GuiResourcesStore.getIcon(new File(Properties.ICONOPTIONS), display));
        } catch (Exception e1) {
            LoggerUtil.logError("Could not load window icon", e1);
        }
        optionsShell.setText("Options");
        Rectangle displayRectangle = display.getBounds();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        optionsShell.setLayout(gridLayout);
        final Tree tConfig = new Tree(optionsShell, SWT.BORDER);
        TreeItem treeItem1 = new TreeItem(tConfig, SWT.NONE);
        treeItem1.setText("General Options");
        treeItem1.setData(1);
        TreeItem treeItem2 = new TreeItem(tConfig, SWT.NONE);
        treeItem2.setText("Serial");
        treeItem2.setData(2);
        tConfig.setFocus();
        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.grabExcessVerticalSpace = true;
        tConfig.setLayoutData(data);
        final Composite cConfig = new Composite(optionsShell, SWT.NONE);
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;
        cConfig.setLayoutData(data);
        infoLabel = new Label(optionsShell, SWT.NONE);
        infoLabel.setText("");
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = 3;
        infoLabel.setLayoutData(data);
        Composite bGroup = new Composite(optionsShell, SWT.PUSH);
        data = new GridData();
        data.horizontalAlignment = GridData.END;
        data.horizontalSpan = 3;
        bGroup.setLayoutData(data);
        FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
        bGroup.setLayout(fillLayout);
        Button bApply = new Button(bGroup, SWT.PUSH);
        bApply.setText("Apply");
        Button bOk = new Button(bGroup, SWT.PUSH);
        bOk.setText("OK");
        Button bClose = new Button(bGroup, SWT.PUSH);
        bClose.setText("Cancel");
        final StackLayout stackLayout = new StackLayout();
        cConfig.setLayout(stackLayout);
        final Composite[] stack = { new GenerialOptions(cConfig, SWT.NONE), new SerialOptions(cConfig, SWT.NONE) };
        tConfig.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem[] tiArr = tConfig.getSelection();
                switch((Integer) tiArr[0].getData()) {
                    case 1:
                        stackLayout.topControl = stack[0];
                        break;
                    case 2:
                        stackLayout.topControl = stack[1];
                        break;
                    default:
                        System.err.println("OptionsShell: stack not found");
                        break;
                }
                cConfig.layout();
            }
        });
        bApply.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    processAndSaveOptions();
                    infoLabel.setText("Configured");
                    infoLabel.setForeground(GuiResourcesStore.getColor("GREEN", display, 0, 220, 0));
                } catch (Exception e) {
                    LoggerUtil.logError("", e);
                    infoLabel.setText(e.getMessage());
                    infoLabel.setForeground(GuiResourcesStore.getColor("RED", display, 255, 0, 0));
                }
            }
        });
        bOk.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    processAndSaveOptions();
                    optionsShell.close();
                } catch (Exception e1) {
                    LoggerUtil.logError("", e1);
                    infoLabel.setText(e1.getMessage());
                    infoLabel.setForeground(GuiResourcesStore.getColor("RED", display, 255, 0, 0));
                }
            }
        });
        bClose.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                optionsShell.close();
            }
        });
        optionsShell.pack();
        Point shellSize = optionsShell.getSize();
        optionsShell.setSize(shellSize);
        Point location = new Point((displayRectangle.width / 2) - (shellSize.x / 2), (displayRectangle.height / 2) - (shellSize.y / 2));
        optionsShell.setLocation(location);
        optionsShell.open();
        while (!optionsShell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    private static class GenerialOptions extends Composite {

        public GenerialOptions(Composite parent, int style) {
            super(parent, style);
            GridLayout gridLayout = new GridLayout(2, false);
            this.setLayout(gridLayout);
            final Group clipEnableGroup = new Group(this, SWT.NONE);
            clipEnableGroup.setText("Clipboard Functions");
            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.horizontalSpan = 2;
            clipEnableGroup.setLayoutData(data);
            FillLayout fillLayout = new FillLayout();
            fillLayout.marginWidth = 5;
            fillLayout.marginHeight = 5;
            fillLayout.spacing = 5;
            clipEnableGroup.setLayout(fillLayout);
            final Button buttonEnableText = new Button(clipEnableGroup, SWT.CHECK);
            buttonEnableText.setText("Text");
            buttonEnableText.setSelection(globalIsClipboardTextEnable);
            final Button buttonEnableFile = new Button(clipEnableGroup, SWT.CHECK);
            buttonEnableFile.setText("File");
            buttonEnableFile.setSelection(globalIsClipboardFileEnable);
            buttonEnableText.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    globalIsClipboardTextEnable = buttonEnableText.getSelection();
                }
            });
            buttonEnableFile.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    globalIsClipboardFileEnable = buttonEnableFile.getSelection();
                }
            });
            Label labeCPC = new Label(this, SWT.NONE);
            labeCPC.setText("Clipboard Polling Cycletime (ms):");
            final Text textCPC = new Text(this, SWT.BORDER);
            textCPC.setText(String.valueOf(globalClipboardPollingCycletime));
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            textCPC.setLayoutData(data);
            Label labelIRT = new Label(this, SWT.NONE);
            labelIRT.setText("Incomming Read Timeout (s):");
            final Text textIRT = new Text(this, SWT.BORDER);
            textIRT.setText(String.valueOf(globalIncommingReadTimeoout));
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            textIRT.setLayoutData(data);
            Label labelMTB = new Label(this, SWT.NONE);
            labelMTB.setText("Max Transfer Bytes (Byte):");
            final Text textMTB = new Text(this, SWT.BORDER);
            textMTB.setText(String.valueOf(globalMaxTransferBytes));
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            textMTB.setLayoutData(data);
            textCPC.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent arg0) {
                    try {
                        globalClipboardPollingCycletime = Long.valueOf(textCPC.getText());
                        infoLabel.setText("");
                    } catch (Exception e) {
                        infoLabel.setText("NumberFormatException: " + e.getMessage());
                        infoLabel.setForeground(GuiResourcesStore.getColor("RED", display, 255, 0, 0));
                    }
                }
            });
            textIRT.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent arg0) {
                    try {
                        globalIncommingReadTimeoout = Integer.valueOf(textIRT.getText());
                        infoLabel.setText("");
                    } catch (Exception e) {
                        infoLabel.setText("NumberFormatException: " + e.getMessage());
                        infoLabel.setForeground(GuiResourcesStore.getColor("RED", display, 255, 0, 0));
                    }
                }
            });
            textMTB.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent arg0) {
                    try {
                        globalMaxTransferBytes = Long.valueOf(textMTB.getText());
                        infoLabel.setText("");
                    } catch (Exception e) {
                        infoLabel.setText("NumberFormatException: " + e.getMessage());
                        infoLabel.setForeground(GuiResourcesStore.getColor("RED", display, 255, 0, 0));
                    }
                }
            });
        }
    }

    private static class SerialOptions extends Composite {

        public SerialOptions(Composite parent, int style) {
            super(parent, style);
            GridLayout gridLayout = new GridLayout(2, false);
            this.setLayout(gridLayout);
            final Button buttonSerialEnable = new Button(this, SWT.CHECK);
            buttonSerialEnable.setText("Serial");
            buttonSerialEnable.setToolTipText("Enable this to enable sync via serial connection.");
            buttonSerialEnable.setSelection(serialIsEnable);
            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.horizontalSpan = 2;
            buttonSerialEnable.setLayoutData(data);
            final Label labelPort = new Label(this, SWT.NONE);
            labelPort.setText("Port:");
            labelPort.setEnabled(serialIsEnable);
            List<String> portList = SerialSync.getAvailableSerialPorts();
            final Combo comboPorts = new Combo(this, SWT.READ_ONLY);
            comboPorts.setItems(portList.toArray(new String[portList.size()]));
            for (int i = 0; i < portList.size(); i++) {
                if (serialPort.equalsIgnoreCase(portList.get(i))) {
                    comboPorts.select(i);
                    break;
                }
            }
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            comboPorts.setLayoutData(data);
            comboPorts.setEnabled(serialIsEnable);
            final Label labelBaudrate = new Label(this, SWT.NONE);
            labelBaudrate.setText("Baurate:");
            labelBaudrate.setEnabled(serialIsEnable);
            String[] availableBaudrates = { "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "921600" };
            final Combo comboBaudrate = new Combo(this, SWT.READ_ONLY);
            comboBaudrate.setItems(availableBaudrates);
            for (int i = 0; i < availableBaudrates.length; i++) {
                if (serialBaudrate == Integer.valueOf(availableBaudrates[i]).intValue()) {
                    comboBaudrate.select(i);
                    break;
                }
            }
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            comboBaudrate.setLayoutData(data);
            comboBaudrate.setEnabled(serialIsEnable);
            final Label labelStopBits = new Label(this, SWT.NONE);
            labelStopBits.setText("StopBits:");
            labelStopBits.setEnabled(serialIsEnable);
            String[] availableStopBits = { "1", "2" };
            final Combo comboStopBits = new Combo(this, SWT.READ_ONLY);
            comboStopBits.setItems(availableStopBits);
            for (int i = 0; i < availableStopBits.length; i++) {
                if (serialStopBits == Integer.valueOf(availableStopBits[i]).intValue()) {
                    comboStopBits.select(i);
                    break;
                }
            }
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            comboStopBits.setLayoutData(data);
            comboStopBits.setEnabled(serialIsEnable);
            final Label labelParity = new Label(this, SWT.NONE);
            labelParity.setText("Parity:");
            labelParity.setEnabled(serialIsEnable);
            final Button buttonParity = new Button(this, SWT.CHECK);
            buttonParity.setText("");
            buttonParity.setToolTipText(null);
            buttonParity.setSelection(serialParity);
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            buttonParity.setLayoutData(data);
            buttonParity.setEnabled(serialIsEnable);
            buttonSerialEnable.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent arg0) {
                    serialIsEnable = buttonSerialEnable.getSelection();
                    labelPort.setEnabled(serialIsEnable);
                    comboPorts.setEnabled(serialIsEnable);
                    labelBaudrate.setEnabled(serialIsEnable);
                    comboBaudrate.setEnabled(serialIsEnable);
                    labelStopBits.setEnabled(serialIsEnable);
                    comboStopBits.setEnabled(serialIsEnable);
                    labelParity.setEnabled(serialIsEnable);
                    buttonParity.setEnabled(serialIsEnable);
                }
            });
            comboPorts.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    serialPort = comboPorts.getText();
                }
            });
            comboBaudrate.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    serialBaudrate = Integer.valueOf(comboBaudrate.getText());
                }
            });
            comboStopBits.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    serialStopBits = Integer.valueOf(comboStopBits.getText());
                }
            });
            buttonParity.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    serialParity = buttonParity.getSelection();
                }
            });
        }
    }

    private static void getCurrentProperties() {
        globalIsClipboardTextEnable = Boolean.valueOf(Properties.CLIPBOARD_TEXT_IS_ENABLE);
        globalIsClipboardFileEnable = Boolean.valueOf(Properties.CLIPBOARD_FILE_IS_ENABLE);
        globalClipboardPollingCycletime = Long.valueOf(Properties.CLIPBOARD_POLLING_CYCLETIME);
        globalIncommingReadTimeoout = Integer.valueOf(Properties.INCOMMING_READ_TIMEOUT);
        globalMaxTransferBytes = Long.valueOf(Properties.MAX_TRANSFER_BYTES);
        serialIsEnable = Boolean.valueOf(Properties.SERIAL_IS_ENABLE);
        serialPort = Properties.SERIAL_PORT;
        serialBaudrate = Integer.valueOf(Properties.SERIAL_BAUDRATE);
        serialStopBits = Integer.valueOf(Properties.SERIAL_STOPBITS);
        if (Integer.valueOf(Properties.SERIAL_PARITY) == 0) {
            serialParity = false;
        } else {
            serialParity = true;
        }
    }

    private static void processAndSaveOptions() throws Exception {
        if (Properties.clipboardSync_internal != null) {
            Properties.clipboardSync_internal.stopSync();
        }
        if (Properties.serialSync_internal != null) {
            Properties.serialSync_internal.close();
        } else {
            Properties.serialSync_internal = new SerialSync();
        }
        Properties.serialSync_internal.initConnection(serialPort, serialBaudrate, serialStopBits, (serialParity ? 1 : 0));
        Properties.clipboardSync_internal = new ClipboardSync(Properties.serialSync_internal, display);
        LoggerUtil.logDebug("register ISyncStatus implementations");
        Properties.clipboardSync_internal.registerSyncStatus(Properties.syncStatus_internal);
        Properties.serialSync_internal.registerSyncStatus(Properties.syncStatus_internal);
        Properties.CLIPBOARD_TEXT_IS_ENABLE = String.valueOf(globalIsClipboardTextEnable);
        Properties.CLIPBOARD_FILE_IS_ENABLE = String.valueOf(globalIsClipboardFileEnable);
        Properties.CLIPBOARD_POLLING_CYCLETIME = String.valueOf(globalClipboardPollingCycletime);
        Properties.INCOMMING_READ_TIMEOUT = String.valueOf(globalIncommingReadTimeoout);
        Properties.MAX_TRANSFER_BYTES = String.valueOf(globalMaxTransferBytes);
        Properties.SERIAL_IS_ENABLE = String.valueOf(serialIsEnable);
        Properties.SERIAL_PORT = serialPort;
        Properties.SERIAL_BAUDRATE = String.valueOf(serialBaudrate);
        Properties.SERIAL_STOPBITS = String.valueOf(serialStopBits);
        if (!serialParity) {
            Properties.SERIAL_PARITY = "0";
        } else {
            Properties.SERIAL_PARITY = "1";
        }
        Properties.writeConf();
    }
}
