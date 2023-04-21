package midlet;

import controller.CuentaController;
import controller.MovimientoController;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import persistence.Cuenta;
import persistence.Movimiento;
import util.DateUtil;

/**
 * @author alfredo
 */
public class ControlTarjeta extends MIDlet implements CommandListener {

    private boolean midletPaused = false;

    MovimientoController movimientoController;

    CuentaController cuentaController;

    boolean esEntrada = true;

    int reportIndex;

    int reportPageSize = 5;

    private java.util.Hashtable __previousDisplayables = new java.util.Hashtable();

    private Command exitCommand;

    private Command okCommand;

    private Command itemCommand;

    private Command itemCommand1;

    private Command cancelCommand;

    private Command ingresarCommand;

    private Command backCommand;

    private Command backCommand1;

    private Command backCommand2;

    private Command screenCommand;

    private Command backCommand4;

    private Command backCommand3;

    private Command backCommand5;

    private Command prevItemCommand;

    private Command nextItemCommand;

    private Command backCommand6;

    private Command prevItemCommand1;

    private Command nextItemCommand1;

    private Form main;

    private StringItem stringItem;

    private ImageItem imageItem;

    private List movimientoList;

    private Form registrarEntradaForm;

    private TextField montoTextField;

    private TextField referenciaTextField;

    private DateField fechaDateField;

    private Form consultaMovimientosForm;

    private Form consultaSaldoForm;

    private Image image2;

    private Image image1;

    private Image image4;

    private Image image3;

    private Image image5;

    private Image image;

    /**
     * The ControlTarjeta constructor.
     */
    public ControlTarjeta() {
        movimientoController = new MovimientoController();
        cuentaController = new CuentaController();
    }

    /**
     * Switches a display to previous displayable of the current displayable.
     * The <code>display</code> instance is obtain from the <code>getDisplay</code> method.
     */
    private void switchToPreviousDisplayable() {
        Displayable __currentDisplayable = getDisplay().getCurrent();
        if (__currentDisplayable != null) {
            Displayable __nextDisplayable = (Displayable) __previousDisplayables.get(__currentDisplayable);
            if (__nextDisplayable != null) {
                switchDisplayable(null, __nextDisplayable);
            }
        }
    }

    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {
    }

    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {
        switchDisplayable(null, getMain());
    }

    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {
    }

    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        Display display = getDisplay();
        Displayable __currentDisplayable = display.getCurrent();
        if (__currentDisplayable != null && nextDisplayable != null) {
            __previousDisplayables.put(nextDisplayable, __currentDisplayable);
        }
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }

    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {
        if (displayable == consultaMovimientosForm) {
            if (command == backCommand3) {
                switchDisplayable(null, getMovimientoList());
            } else if (command == nextItemCommand) {
                consultaMovimientosForm = null;
                reportIndex++;
                switchDisplayable(null, getConsultaMovimientosForm());
            } else if (command == prevItemCommand) {
                consultaMovimientosForm = null;
                reportIndex--;
                if (reportIndex < 0) {
                    reportIndex = 0;
                }
                switchDisplayable(null, getConsultaMovimientosForm());
            }
        } else if (displayable == consultaSaldoForm) {
            if (command == backCommand6) {
                switchDisplayable(null, getMovimientoList());
            } else if (command == nextItemCommand1) {
            } else if (command == prevItemCommand1) {
            }
        } else if (displayable == main) {
            if (command == exitCommand) {
                exitMIDlet();
            } else if (command == okCommand) {
                switchDisplayable(null, getMovimientoList());
            }
        } else if (displayable == movimientoList) {
            if (command == List.SELECT_COMMAND) {
                movimientoListAction();
            } else if (command == backCommand5) {
                switchDisplayable(null, getMain());
            }
        } else if (displayable == registrarEntradaForm) {
            if (command == backCommand4) {
                switchToPreviousDisplayable();
            } else if (command == ingresarCommand) {
                movimientoController.create();
                movimientoController.getMovimiento().setReferencia(getReferenciaTextField().getString());
                if (esEntrada) {
                    movimientoController.getMovimiento().setMonto(Float.parseFloat(getMontoTextField().getString()));
                } else {
                    movimientoController.getMovimiento().setMonto(Float.parseFloat(getMontoTextField().getString()) * -1);
                }
                movimientoController.getMovimiento().setFecha(getFechaDateField().getDate());
                movimientoController.save();
                cuentaController.create();
                cuentaController.addMovimiento(movimientoController.getMovimiento());
                switchDisplayable(null, getMovimientoList());
            }
        }
    }

    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {
            exitCommand = new Command("Salir", Command.EXIT, 1);
        }
        return exitCommand;
    }

    /**
     * Returns an initiliazed instance of main component.
     * @return the initialized component instance
     */
    public Form getMain() {
        if (main == null) {
            main = new Form("Control Tarjeta", new Item[] { getStringItem(), getImageItem() });
            main.addCommand(getExitCommand());
            main.addCommand(getOkCommand());
            main.setCommandListener(this);
        }
        return main;
    }

    /**
     * Returns an initiliazed instance of stringItem component.
     * @return the initialized component instance
     */
    public StringItem getStringItem() {
        if (stringItem == null) {
            stringItem = new StringItem("Bienvenido! ", "Presione aceptar para continuar", Item.PLAIN);
            stringItem.setLayout(ImageItem.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_VCENTER | ImageItem.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_SHRINK | Item.LAYOUT_VSHRINK | Item.LAYOUT_EXPAND | Item.LAYOUT_VEXPAND);
        }
        return stringItem;
    }

    /**
     * Returns an initiliazed instance of okCommand component.
     * @return the initialized component instance
     */
    public Command getOkCommand() {
        if (okCommand == null) {
            okCommand = new Command("Entrar", Command.OK, 0);
        }
        return okCommand;
    }

    /**
     * Returns an initiliazed instance of itemCommand component.
     * @return the initialized component instance
     */
    public Command getItemCommand() {
        if (itemCommand == null) {
            itemCommand = new Command("Registrar salida", Command.ITEM, 0);
        }
        return itemCommand;
    }

    /**
     * Returns an initiliazed instance of itemCommand1 component.
     * @return the initialized component instance
     */
    public Command getItemCommand1() {
        if (itemCommand1 == null) {
            itemCommand1 = new Command("Registrar entrada", Command.ITEM, 0);
        }
        return itemCommand1;
    }

    /**
     * Returns an initiliazed instance of movimientoList component.
     * @return the initialized component instance
     */
    public List getMovimientoList() {
        if (movimientoList == null) {
            movimientoList = new List("Movimientos", Choice.IMPLICIT);
            movimientoList.append("Registrar entrada", getImage2());
            movimientoList.append("Registrar salida", getImage3());
            movimientoList.append("Consultar Movimientos", getImage4());
            movimientoList.append("Consultar Saldos", getImage5());
            movimientoList.addCommand(getBackCommand5());
            movimientoList.setCommandListener(this);
            movimientoList.setSelectedFlags(new boolean[] { false, false, false, false });
        }
        return movimientoList;
    }

    /**
     * Performs an action assigned to the selected list element in the movimientoList component.
     */
    public void movimientoListAction() {
        String __selectedString = getMovimientoList().getString(getMovimientoList().getSelectedIndex());
        if (__selectedString != null) {
            if (__selectedString.equals("Registrar entrada")) {
                esEntrada = true;
                referenciaTextField = null;
                montoTextField = null;
                fechaDateField = null;
                registrarEntradaForm = null;
                switchDisplayable(null, getRegistrarEntradaForm());
            } else if (__selectedString.equals("Registrar salida")) {
                esEntrada = false;
                referenciaTextField = null;
                montoTextField = null;
                fechaDateField = null;
                registrarEntradaForm = null;
                switchDisplayable(null, getRegistrarEntradaForm());
            } else if (__selectedString.equals("Consultar Movimientos")) {
                consultaMovimientosForm = null;
                reportIndex = 0;
                switchDisplayable(null, getConsultaMovimientosForm());
            } else if (__selectedString.equals("Consultar Saldos")) {
                consultaSaldoForm = null;
                reportIndex = 0;
                switchDisplayable(null, getConsultaSaldoForm());
            }
        }
    }

    /**
     * Returns an initiliazed instance of registrarEntradaForm component.
     * @return the initialized component instance
     */
    public Form getRegistrarEntradaForm() {
        if (registrarEntradaForm == null) {
            registrarEntradaForm = new Form("Registrar Entrada", new Item[] { getReferenciaTextField(), getMontoTextField(), getFechaDateField() });
            registrarEntradaForm.addCommand(getIngresarCommand());
            registrarEntradaForm.addCommand(getBackCommand4());
            registrarEntradaForm.setCommandListener(this);
            registrarEntradaForm.setTitle("Registrar " + (esEntrada ? "Entrada" : "Salida"));
        }
        return registrarEntradaForm;
    }

    /**
     * Returns an initiliazed instance of cancelCommand component.
     * @return the initialized component instance
     */
    public Command getCancelCommand() {
        if (cancelCommand == null) {
            cancelCommand = new Command("Cancelar", Command.CANCEL, 0);
        }
        return cancelCommand;
    }

    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {
            backCommand = new Command("Back", Command.BACK, 0);
        }
        return backCommand;
    }

    /**
     * Returns an initiliazed instance of backCommand1 component.
     * @return the initialized component instance
     */
    public Command getBackCommand1() {
        if (backCommand1 == null) {
            backCommand1 = new Command("Back", Command.BACK, 0);
        }
        return backCommand1;
    }

    /**
     * Returns an initiliazed instance of backCommand2 component.
     * @return the initialized component instance
     */
    public Command getBackCommand2() {
        if (backCommand2 == null) {
            backCommand2 = new Command("Back", Command.BACK, 0);
        }
        return backCommand2;
    }

    /**
     * Returns an initiliazed instance of consultaMovimientosForm component.
     * @return the initialized component instance
     */
    public Form getConsultaMovimientosForm() {
        if (consultaMovimientosForm == null) {
            consultaMovimientosForm = new Form("Consulta Movimientos", new Item[] {});
            consultaMovimientosForm.addCommand(getBackCommand3());
            consultaMovimientosForm.addCommand(getNextItemCommand());
            consultaMovimientosForm.addCommand(getPrevItemCommand());
            consultaMovimientosForm.setCommandListener(this);
            Movimiento[] mm = movimientoController.list(reportIndex, reportPageSize);
            if (mm != null) {
                consultaMovimientosForm.append(new StringItem("Pagina " + (reportIndex + 1), "\n"));
                for (int i = 0; i < mm.length; i++) {
                    consultaMovimientosForm.append(new StringItem("\n" + DateUtil.dateToString(mm[i].getFecha()), "\n" + mm[i].getReferencia() + "  " + mm[i].getMonto() + "\n"));
                    consultaMovimientosForm.append(new Spacer(16, 2));
                }
            } else {
                consultaMovimientosForm.append(new StringItem("No se encontraron registros.", ""));
            }
        }
        return consultaMovimientosForm;
    }

    /**
     * Returns an initiliazed instance of backCommand3 component.
     * @return the initialized component instance
     */
    public Command getBackCommand3() {
        if (backCommand3 == null) {
            backCommand3 = new Command("Atras", Command.BACK, 1);
        }
        return backCommand3;
    }

    /**
     * Returns an initiliazed instance of ingresarCommand component.
     * @return the initialized component instance
     */
    public Command getIngresarCommand() {
        if (ingresarCommand == null) {
            ingresarCommand = new Command("Ingresar", Command.SCREEN, 0);
        }
        return ingresarCommand;
    }

    /**
     * Returns an initiliazed instance of montoTextField component.
     * @return the initialized component instance
     */
    public TextField getMontoTextField() {
        if (montoTextField == null) {
            montoTextField = new TextField("Monto", null, 32, TextField.DECIMAL);
        }
        return montoTextField;
    }

    /**
     * Returns an initiliazed instance of referenciaTextField component.
     * @return the initialized component instance
     */
    public TextField getReferenciaTextField() {
        if (referenciaTextField == null) {
            referenciaTextField = new TextField("Referencia", null, 32, TextField.ANY);
        }
        return referenciaTextField;
    }

    /**
     * Returns an initiliazed instance of fechaDateField component.
     * @return the initialized component instance
     */
    public DateField getFechaDateField() {
        if (fechaDateField == null) {
            fechaDateField = new DateField("Fecha", DateField.DATE_TIME);
            fechaDateField.setDate(new java.util.Date(System.currentTimeMillis()));
        }
        return fechaDateField;
    }

    /**
     * Returns an initiliazed instance of screenCommand component.
     * @return the initialized component instance
     */
    public Command getScreenCommand() {
        if (screenCommand == null) {
            screenCommand = new Command("Screen", Command.SCREEN, 0);
        }
        return screenCommand;
    }

    /**
     * Returns an initiliazed instance of backCommand4 component.
     * @return the initialized component instance
     */
    public Command getBackCommand4() {
        if (backCommand4 == null) {
            backCommand4 = new Command("Atras", Command.BACK, 1);
        }
        return backCommand4;
    }

    /**
     * Returns an initiliazed instance of imageItem component.
     * @return the initialized component instance
     */
    public ImageItem getImageItem() {
        if (imageItem == null) {
            imageItem = new ImageItem("", getImage1(), ImageItem.LAYOUT_CENTER, "<Missing Image>", Item.PLAIN);
        }
        return imageItem;
    }

    /**
     * Returns an initiliazed instance of image1 component.
     * @return the initialized component instance
     */
    public Image getImage1() {
        if (image1 == null) {
            try {
                image1 = Image.createImage("/img/chequemax.jpg");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return image1;
    }

    /**
     * Returns an initiliazed instance of backCommand5 component.
     * @return the initialized component instance
     */
    public Command getBackCommand5() {
        if (backCommand5 == null) {
            backCommand5 = new Command("Atras", Command.BACK, 0);
        }
        return backCommand5;
    }

    /**
     * Returns an initiliazed instance of image2 component.
     * @return the initialized component instance
     */
    public Image getImage2() {
        if (image2 == null) {
            try {
                image2 = Image.createImage("/img/more.png");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return image2;
    }

    /**
     * Returns an initiliazed instance of image3 component.
     * @return the initialized component instance
     */
    public Image getImage3() {
        if (image3 == null) {
            try {
                image3 = Image.createImage("/img/minus.png");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return image3;
    }

    /**
     * Returns an initiliazed instance of image4 component.
     * @return the initialized component instance
     */
    public Image getImage4() {
        if (image4 == null) {
            try {
                image4 = Image.createImage("/img/report.png");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return image4;
    }

    /**
     * Returns an initiliazed instance of nextItemCommand component.
     * @return the initialized component instance
     */
    public Command getNextItemCommand() {
        if (nextItemCommand == null) {
            nextItemCommand = new Command("Siguiente", Command.ITEM, 0);
        }
        return nextItemCommand;
    }

    /**
     * Returns an initiliazed instance of prevItemCommand component.
     * @return the initialized component instance
     */
    public Command getPrevItemCommand() {
        if (prevItemCommand == null) {
            prevItemCommand = new Command("Anterior", Command.ITEM, 0);
        }
        return prevItemCommand;
    }

    /**
     * Returns an initiliazed instance of nextItemCommand1 component.
     * @return the initialized component instance
     */
    public Command getNextItemCommand1() {
        if (nextItemCommand1 == null) {
            nextItemCommand1 = new Command("Siguiente", Command.ITEM, 0);
        }
        return nextItemCommand1;
    }

    /**
     * Returns an initiliazed instance of prevItemCommand1 component.
     * @return the initialized component instance
     */
    public Command getPrevItemCommand1() {
        if (prevItemCommand1 == null) {
            prevItemCommand1 = new Command("Anterior", Command.ITEM, 0);
        }
        return prevItemCommand1;
    }

    /**
     * Returns an initiliazed instance of backCommand6 component.
     * @return the initialized component instance
     */
    public Command getBackCommand6() {
        if (backCommand6 == null) {
            backCommand6 = new Command("Atras", Command.BACK, 0);
        }
        return backCommand6;
    }

    /**
     * Returns an initiliazed instance of consultaSaldoForm component.
     * @return the initialized component instance
     */
    public Form getConsultaSaldoForm() {
        if (consultaSaldoForm == null) {
            consultaSaldoForm = new Form("Consulta de Saldos");
            consultaSaldoForm.addCommand(getNextItemCommand1());
            consultaSaldoForm.addCommand(getPrevItemCommand1());
            consultaSaldoForm.addCommand(getBackCommand6());
            consultaSaldoForm.setCommandListener(this);
            Cuenta[] cc = cuentaController.list(reportIndex, reportPageSize);
            if (cc != null) {
                consultaSaldoForm.append(new StringItem("Pagina " + (reportIndex + 1), "\n"));
                for (int i = 0; i < cc.length; i++) {
                    consultaSaldoForm.append(new StringItem("Mes: " + cc[i].getMes() + "  Año: " + cc[i].getAnio(), "\nSaldo: " + cc[i].getSaldo() + "\nIngresos:" + cc[i].getIngresos() + "\nEgresos:" + cc[i].getEgresos()));
                    consultaSaldoForm.append(new Spacer(16, 2));
                }
            } else {
                consultaSaldoForm.append(new StringItem("No se encontraron registros.", ""));
            }
        }
        return consultaSaldoForm;
    }

    /**
     * Returns an initiliazed instance of image component.
     * @return the initialized component instance
     */
    public Image getImage() {
        if (image == null) {
            image = Image.createImage(1, 1);
        }
        return image;
    }

    /**
     * Returns an initiliazed instance of image5 component.
     * @return the initialized component instance
     */
    public Image getImage5() {
        if (image5 == null) {
            try {
                image5 = Image.createImage("/img/saldo.png");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return image5;
    }

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }
}
