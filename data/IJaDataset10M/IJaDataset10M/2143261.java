package gui.dialogs.tuningpanels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import utils.defines.Defines;

public class PlaceInputDialog extends Dialog {

    private Shell dialogShell;

    private Composite backgroundComposite;

    private Scrollable inputField;

    private Scrollable vectorInputField;

    private Label message;

    private Composite messageComposite;

    private Composite buttonsComposite;

    private Button colorButton;

    private Button okButton;

    private Listener okListener;

    private Listener colorListener;

    private Canvas colorCanvas;

    private Color color;

    private InputData returnData;

    private Object vectorField;

    public PlaceInputDialog(Shell parent, int style) {
        super(parent, style);
        returnData = new InputData();
    }

    /**
	 * Abre el di�logo y, cuando el mismo es cerrado, retorna una instancia
	 * {@link InputData} con los datos ingresados.
	 * @param text		t�tulo del di�logo.
	 * @param msg		texto que se mostrar� en el di�logo.
	 * @param c			color que se mostrar� en el di�logo.
	 * @param items		listado de �tems para selecci�n (si este par�metro es
	 * 					nulo, entonces permite el ingreso a trav�s de un cuadro
	 * 					de texto).
	 * @return			datos ingresados.
	 */
    public synchronized InputData open(String text, String msg, Color c, String[] items) {
        if (c == null) color = new Color(null, 255, 255, 255); else color = c;
        try {
            if (text == null || msg == null) return null;
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            {
                FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
                dialogShell.setLayout(dialogShellLayout);
                dialogShell.setSize(200, 140);
                dialogShell.setText(text);
            }
            {
                backgroundComposite = new Composite(dialogShell, SWT.NONE);
                GridLayout backgroundCompositeLayout = new GridLayout();
                backgroundCompositeLayout.numColumns = 2;
                backgroundComposite.setLayout(backgroundCompositeLayout);
                {
                    GridData colorCanvasLData = new GridData();
                    colorCanvasLData.horizontalAlignment = GridData.FILL;
                    colorCanvasLData.verticalAlignment = GridData.FILL;
                    colorCanvasLData.horizontalIndent = 7;
                    colorCanvasLData.verticalIndent = 5;
                    colorCanvas = new Canvas(backgroundComposite, SWT.NONE);
                    colorCanvas.setLayoutData(colorCanvasLData);
                    colorCanvas.addPaintListener(new PaintListener() {

                        public void paintControl(PaintEvent evt) {
                            canvasPaintControl(evt);
                        }
                    });
                }
                {
                    messageComposite = new Composite(backgroundComposite, SWT.NONE);
                    GridLayout messageCompositeLayout = new GridLayout();
                    messageCompositeLayout.makeColumnsEqualWidth = true;
                    GridData messageCompositeLData = new GridData();
                    messageCompositeLData.horizontalAlignment = GridData.FILL;
                    messageCompositeLData.verticalAlignment = GridData.FILL;
                    messageCompositeLData.grabExcessHorizontalSpace = true;
                    messageComposite.setLayoutData(messageCompositeLData);
                    messageComposite.setLayout(messageCompositeLayout);
                    {
                        message = new Label(messageComposite, SWT.WRAP);
                        GridData messageLData = new GridData();
                        messageLData.widthHint = 167;
                        messageLData.heightHint = 31;
                        messageLData.horizontalAlignment = GridData.FILL;
                        messageLData.grabExcessHorizontalSpace = true;
                        message.setLayoutData(messageLData);
                        message.setText(msg);
                    }
                    {
                        GridData inputLData = new GridData();
                        inputLData.grabExcessHorizontalSpace = true;
                        inputLData.horizontalAlignment = GridData.FILL;
                        if (items != null) {
                            inputField = new CCombo(messageComposite, SWT.NONE);
                            inputField.setBackground(new Color(null, 255, 255, 255));
                            ((CCombo) inputField).setEditable(false);
                            for (int i = 0; i < items.length; i++) ((CCombo) inputField).add(items[i]);
                            ((CCombo) inputField).select(0);
                            vectorInputField = new CCombo(messageComposite, SWT.NONE);
                            vectorInputField.setBackground(new Color(null, 255, 255, 255));
                            ((CCombo) vectorInputField).setEditable(false);
                            List<String> vector = loadVectorsFromFile(Defines.VECTOR_FILE);
                            for (int i = 0; i < vector.size(); i++) {
                                ((CCombo) vectorInputField).add(vector.get(i));
                            }
                            ((CCombo) vectorInputField).select(0);
                            vectorField = vectorInputField;
                        }
                        inputField.setFocus();
                        inputField.setLayoutData(inputLData);
                        inputField.addKeyListener(new KeyAdapter() {

                            public void keyPressed(KeyEvent evt) {
                                if ((evt.keyCode == SWT.CR || evt.keyCode == SWT.KEYPAD_CR)) {
                                    if (inputField instanceof CCombo) {
                                        returnData.vector = ((CCombo) vectorField).getText();
                                        returnData.textData = ((CCombo) inputField).getText();
                                        returnData.colorData = color;
                                        dialogShell.close();
                                    } else if (inputField instanceof Text && ((Text) inputField).getText().length() > 0) {
                                        returnData.vector = ((CCombo) vectorField).getText();
                                        returnData.textData = ((Text) inputField).getText();
                                        returnData.colorData = color;
                                        dialogShell.close();
                                    }
                                }
                            }
                        });
                    }
                }
                {
                    buttonsComposite = new Composite(backgroundComposite, SWT.NONE);
                    RowLayout buttonsCompositeLayout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
                    buttonsCompositeLayout.justify = true;
                    GridData buttonsCompositeLData = new GridData();
                    buttonsCompositeLData.horizontalSpan = 2;
                    buttonsCompositeLData.horizontalAlignment = GridData.FILL;
                    buttonsCompositeLData.heightHint = 30;
                    buttonsComposite.setLayoutData(buttonsCompositeLData);
                    buttonsComposite.setLayout(buttonsCompositeLayout);
                    colorListener = new Listener() {

                        public void handleEvent(Event event) {
                            ColorDialog cd = new ColorDialog(dialogShell, SWT.NONE);
                            cd.setText("Sample Color Dialog");
                            RGB rgb = cd.open();
                            if (rgb != null) color = new Color(dialogShell.getDisplay(), rgb);
                            colorCanvas.redraw();
                        }
                    };
                    okListener = new Listener() {

                        public void handleEvent(Event event) {
                            returnData.vector = ((CCombo) vectorField).getText();
                            if (inputField instanceof CCombo) {
                                returnData.textData = ((CCombo) inputField).getText();
                            } else if (inputField instanceof Text) returnData.textData = ((Text) inputField).getText();
                            returnData.colorData = color;
                            dialogShell.close();
                        }
                    };
                    {
                        if (c == null) {
                            colorButton = new Button(buttonsComposite, SWT.PUSH | SWT.CENTER);
                            RowData colorButton2LData = new RowData();
                            colorButton2LData.width = 66;
                            colorButton2LData.height = 23;
                            colorButton.setLayoutData(colorButton2LData);
                            colorButton.setText("Color...");
                            colorButton.addListener(SWT.Selection, colorListener);
                        }
                    }
                    {
                        okButton = new Button(buttonsComposite, SWT.PUSH | SWT.CENTER);
                        RowData okButton2LData = new RowData();
                        okButton2LData.width = 66;
                        okButton2LData.height = 23;
                        okButton.setLayoutData(okButton2LData);
                        okButton.setText("Ok");
                        okButton.addListener(SWT.Selection, okListener);
                        if (items == null) okButton.setEnabled(false);
                    }
                }
            }
            dialogShell.open();
            Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
        } catch (Exception e) {
            return null;
        }
        return (InputData) returnData.clone();
    }

    private ArrayList<String> loadVectorsFromFile(String vectorFile) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
        Properties simulationProperties = new Properties();
        simulationProperties.loadFromXML(new FileInputStream("mandafruta.properties.xml"));
        ArrayList<String> vectorNames = new ArrayList<String>();
        for (Object o : simulationProperties.keySet()) {
            String vector = (String) o;
            if (!vector.startsWith("vector")) continue;
            String vectorName = vector.substring("vector".length());
            vectorNames.add(vectorName);
        }
        return vectorNames;
    }

    protected void canvasPaintControl(PaintEvent evt) {
        evt.gc.setBackground(color);
        evt.gc.fillRoundRectangle(0, 0, 50, 50, 2, 2);
    }
}
