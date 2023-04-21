package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import particle_simulator_chem.Boid;
import particle_simulator_chem.World;

public class LoadWorld extends Dialog {

    static World world;

    protected Object result;

    protected Shell shell;

    private Text text_1;

    /**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
    @SuppressWarnings("static-access")
    public LoadWorld(Shell parent, int style, World world) {
        super(parent, style);
        setText("SWT Dialog");
        this.world = world;
    }

    /**
	 * Open the dialog.
	 * @return the result
	 */
    public Object open() {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
	 * Create contents of the dialog.
	 */
    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        shell.setSize(450, 138);
        shell.setText(getText());
        Composite composite = new Composite(shell, SWT.BORDER);
        composite.setLayout(null);
        composite.setBounds(10, 10, 424, 89);
        Label lblWorldLoad = new Label(composite, SWT.NONE);
        lblWorldLoad.setText("World Load\r\n");
        lblWorldLoad.setBounds(10, 0, 400, 15);
        Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        label_1.setBounds(0, 21, 420, 2);
        Label lblFilePathcusersworldtxt = new Label(composite, SWT.NONE);
        lblFilePathcusersworldtxt.setText("File Path (\"C:\\Users\\world.txt\")\r\n");
        lblFilePathcusersworldtxt.setBounds(10, 29, 160, 15);
        text_1 = new Text(composite, SWT.BORDER);
        text_1.setText("");
        text_1.setBounds(176, 31, 234, 18);
        Button btnLoadWorld = new Button(composite, SWT.NONE);
        btnLoadWorld.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                File world_file = new File(text_1.getText());
                try {
                    Scanner scan = new Scanner(world_file);
                    world.setWidth(Integer.parseInt(scan.nextLine()));
                    world.setHeight(Integer.parseInt(scan.nextLine()));
                    world.setGravity(Double.parseDouble(scan.nextLine()));
                    world.setGravX(Double.parseDouble(scan.nextLine()));
                    world.setGravY(Double.parseDouble(scan.nextLine()));
                    world.setDefaultGravity(Double.parseDouble(scan.nextLine()));
                    world.setDefaultGX(Double.parseDouble(scan.nextLine()));
                    world.setDefaultGY(Double.parseDouble(scan.nextLine()));
                    world.setAirFriction(Double.parseDouble(scan.nextLine()));
                    ArrayList<Boid> boids = new ArrayList<Boid>();
                    while (scan.hasNextLine()) {
                        double x = Double.parseDouble(scan.nextLine());
                        double y = Double.parseDouble(scan.nextLine());
                        double xvel = Double.parseDouble(scan.nextLine());
                        double yvel = Double.parseDouble(scan.nextLine());
                        String raw_formula = scan.nextLine();
                    }
                    world.setBoids(boids);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnLoadWorld.setText("Load World");
        btnLoadWorld.setBounds(176, 55, 75, 25);
    }
}
