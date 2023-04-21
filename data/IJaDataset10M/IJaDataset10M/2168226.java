package ch.ethz.inf.vs.wot.autowot.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import ch.ethz.inf.vs.wot.autowot.builders.BuildMode;
import ch.ethz.inf.vs.wot.autowot.builders.java.AbstractJavaBuilder;
import ch.ethz.inf.vs.wot.autowot.builders.java.JerseyJavaBuilder;
import ch.ethz.inf.vs.wot.autowot.builders.java.RestletJavaBuilder;
import ch.ethz.inf.vs.wot.autowot.builders.xml.XMLBuilder;
import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.ui.ViewStyle;
import ch.ethz.inf.vs.wot.autowot.ui.dialogs.DialogBoxFactory;
import ch.ethz.inf.vs.wot.autowot.ui.manipulatordisplay.ManipulatorDisplay;
import ch.ethz.inf.vs.wot.autowot.ui.navigationdisplay.NavigationDisplay;
import ch.ethz.inf.vs.wot.autowot.ui.resourcedisplay.ResourceDisplay;
import ch.ethz.inf.vs.wot.autowot.ui.resourcedisplay.ResourceDisplayDropListener;

/**
 * Class providing an implementation of UserInterface and drawing
 * all required elements
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */
public class MainUserInterface extends UserInterface {

    private Button createClasses = null;

    private Button prettyButton = null;

    private Button xmlButton = null;

    private Button graphicsButton = null;

    private int splash_delay = 50;

    private String splashLogoPath = "/AutoWoTLogo.png";

    private Color greyLogoColor = new Color(Display.getCurrent(), 171, 171, 171);

    public MainUserInterface(AutoWoT application, Display display) {
        this.application = application;
        this.display = display;
    }

    public void launchUIandApplication() {
        createSplash();
        shell = new Shell(display);
        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
        initComponents();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        System.exit(0);
    }

    /**
	 * Creates and shows the Splash Screen and progress bar.
	 */
    private void createSplash() {
        final Shell splash = new Shell(SWT.ON_TOP);
        splash.setBackground(greyLogoColor);
        final ProgressBar bar = new ProgressBar(splash, SWT.NONE);
        bar.setMaximum(splash_delay);
        FormData progressData = new FormData();
        progressData.left = new FormAttachment(0, 5);
        progressData.right = new FormAttachment(100, -5);
        progressData.bottom = new FormAttachment(100, -5);
        bar.setLayoutData(progressData);
        Image image = new Image(display, this.getClass().getResourceAsStream(splashLogoPath));
        Label label = new Label(splash, SWT.NONE);
        label.setImage(image);
        FormLayout layout = new FormLayout();
        splash.setLayout(layout);
        FormData labelData = new FormData();
        labelData.right = new FormAttachment(100, 0);
        labelData.bottom = new FormAttachment(90, 0);
        label.setLayoutData(labelData);
        splash.pack();
        Rectangle splashRect = splash.getBounds();
        Rectangle displayRect = display.getBounds();
        int x = (displayRect.width - splashRect.width) / 2;
        int y = (displayRect.height - splashRect.height) / 2;
        splash.setLocation(x, y);
        splash.open();
        display.asyncExec(new Runnable() {

            public void run() {
                for (int i = 0; i < splash_delay + 10; i++) {
                    bar.setSelection(i);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                splash.dispose();
            }
        });
        while (!splash.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    /**
	 * Initialize basic GUI components
	 */
    private void initComponents() {
        shell.setText("AutoWoT Prototyper");
        shell.setSize(1000, 600);
        Rectangle bds = shell.getDisplay().getBounds();
        Point p = shell.getSize();
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;
        shell.setBounds(nLeft, nTop, p.x, p.y);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        shell.setLayout(gridLayout);
        GridData gridData;
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 1;
        Label navLabel = new Label(shell, SWT.ARROW);
        navLabel.setText("Navigation");
        navLabel.setLayoutData(gridData);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 1;
        Label displayLabel = new Label(shell, SWT.ARROW);
        displayLabel.setText("Display");
        displayLabel.setLayoutData(gridData);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 1;
        gridData.horizontalIndent = 40;
        prettyButton = new Button(shell, SWT.RADIO);
        prettyButton.setText("Plain");
        prettyButton.setSelection(true);
        prettyButton.setLayoutData(gridData);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 1;
        gridData.horizontalIndent = 40;
        xmlButton = new Button(shell, SWT.RADIO);
        xmlButton.setText("XML");
        xmlButton.setSelection(false);
        xmlButton.setLayoutData(gridData);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 1;
        gridData.horizontalIndent = 40;
        graphicsButton = new Button(shell, SWT.RADIO);
        graphicsButton.setText("Graphical");
        graphicsButton.setSelection(false);
        graphicsButton.setLayoutData(gridData);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 1;
        Label paddLabel = new Label(shell, SWT.ARROW);
        paddLabel.setText("Manipulators");
        paddLabel.setLayoutData(gridData);
        prettyButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                resourceDisplay.setViewStyle(ViewStyle.PRETTY);
                resourceDisplay.printCurrentResourceInfo();
            }
        });
        xmlButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                resourceDisplay.setViewStyle(ViewStyle.XML);
                resourceDisplay.printCurrentResourceInfo();
            }
        });
        graphicsButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                resourceDisplay.setViewStyle(ViewStyle.GRAPHICAL);
                resourceDisplay.printCurrentResourceInfo();
            }
        });
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = p.x / 5;
        navigationDisplay = new NavigationDisplay(shell, this, application);
        navigationDisplay.getTree().setLayoutData(gridData);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 4;
        resourceDisplay = new ResourceDisplay(application, this, shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
        resourceDisplay.setLayoutData(gridData);
        int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
        DropTarget target = new DropTarget(resourceDisplay, operations);
        Transfer[] types = new Transfer[] { FileTransfer.getInstance(), TextTransfer.getInstance() };
        target.setTransfer(types);
        target.addDropListener(new ResourceDisplayDropListener(application, this));
        gridData = new GridData(GridData.FILL_BOTH);
        manipulatorDisplay = new ManipulatorDisplay(application, this, shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
        manipulatorDisplay.setLayoutData(gridData);
        manipulatorDisplay.updateDisplay();
        operations = DND.DROP_MOVE | DND.DROP_COPY;
        DragSource source = new DragSource(manipulatorDisplay, operations);
        types = new Transfer[] { TextTransfer.getInstance() };
        source.setTransfer(types);
        source.addDragListener(new DragSourceListener() {

            public void dragStart(DragSourceEvent event) {
                if (manipulatorDisplay.getItem(manipulatorDisplay.getSelectionIndex()).getText().length() == 0) {
                    event.doit = false;
                }
            }

            public void dragSetData(DragSourceEvent event) {
                if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
                    event.data = manipulatorDisplay.getItem(manipulatorDisplay.getSelectionIndex()).getText();
                }
            }

            public void dragFinished(DragSourceEvent event) {
                if (event.detail == DND.DROP_MOVE) {
                    manipulatorDisplay.getItem(manipulatorDisplay.getSelectionIndex()).getText();
                }
            }
        });
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 1;
        createClasses = new Button(shell, SWT.PUSH);
        createClasses.setText("Write Java Webserver");
        createClasses.setLayoutData(gridData);
        createClasses.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (application.getCurrentProject().getRootResource() == null) return;
                DialogBoxFactory dbf = new DialogBoxFactory(application, shell);
                dbf.getJavaCreationData();
                if (!dbf.cancelled) {
                    application.getCurrentProject().setProjectName(dbf.returnName);
                    application.getCurrentProject().setPackageCanonical(dbf.returnJavaPackage);
                    application.getCurrentProject().setHandlerCanonical(dbf.returnHandlerPackage);
                    application.getCurrentProject().setMakeStandalone(dbf.returnIsStandalone);
                    if (!dbf.returnArgumentType.endsWith("/")) dbf.returnArgumentType += "/";
                    application.getCurrentProject().setFileSystemPath(dbf.returnArgumentType);
                    XMLBuilder.createXMLStructure(application.getCurrentProject());
                    AbstractJavaBuilder myJavaBuilder = null;
                    if (dbf.returnBuildMode == BuildMode.RESTLET) {
                        myJavaBuilder = new JerseyJavaBuilder(application.getCurrentProject());
                    } else if (dbf.returnBuildMode == BuildMode.JERSEY) {
                        myJavaBuilder = new JerseyJavaBuilder(application.getCurrentProject());
                    }
                    myJavaBuilder.build();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 4;
        Label paddingLabel = new Label(shell, SWT.ARROW);
        paddingLabel.setText("");
        paddingLabel.setLayoutData(gridData);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 1;
        Button instructionsButton = new Button(shell, SWT.PUSH);
        instructionsButton.setText("Show Instructions");
        instructionsButton.setLayoutData(gridData);
        instructionsButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                DialogBoxFactory newDiag = new DialogBoxFactory(application, shell);
                newDiag.showInstructions();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });
    }

    public void refresh() {
        navigationDisplay.refresh();
        resourceDisplay.printCurrentResourceInfo();
        manipulatorDisplay.updateDisplay();
    }
}
