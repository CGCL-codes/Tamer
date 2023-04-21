package rpg.editor.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import rpg.editor.Constants;
import rpg.editor.core.DisplayHelper;
import rpg.editor.core.ImageHelper;
import rpg.editor.core.TileSelectionStub;
import rpg.editor.model.RpgMap;

/**
 * MapEditor is the main component in the application.  It allows users to add
 * selected tiles to a map and build up the map that will feature in the game.
 * This class also facilitates editing the information (eg. level data) required
 * by the game engine.
 * 
 * @author seldred
 */
public class MapEditor extends Composite {

    private ScrolledComposite canvasHolder;

    private MapEditorCanvas mapEditorCanvas;

    public MapEditor(Composite parent) {
        this(parent, SWT.NONE);
    }

    public MapEditor(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout());
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Group radioGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
        radioGroup.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        radioGroup.setLayout(new RowLayout());
        Button addButton = new Button(radioGroup, SWT.RADIO);
        addButton.setText("Add");
        addButton.setSelection(true);
        Button insertButton = new Button(radioGroup, SWT.RADIO);
        insertButton.setText("Insert");
        insertButton.setSelection(false);
        Button noneButton = new Button(radioGroup, SWT.RADIO);
        noneButton.setText("None");
        noneButton.setSelection(false);
        canvasHolder = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        canvasHolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mapEditorCanvas = new MapEditorCanvas(canvasHolder);
        canvasHolder.setContent(mapEditorCanvas);
        canvasHolder.setExpandHorizontal(true);
        canvasHolder.setExpandVertical(true);
        Label tileLabel = new Label(this, SWT.CENTER);
        tileLabel.setText(Constants.NO_SELECTION_LABEL);
        tileLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        mapEditorCanvas.tileLabel = tileLabel;
        addButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                mapEditorCanvas.editMode = EditMode.ADD;
            }
        });
        insertButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                mapEditorCanvas.editMode = EditMode.REPLACE;
            }
        });
        noneButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                mapEditorCanvas.editMode = EditMode.EDIT;
            }
        });
    }

    public RpgMap getMap() {
        return mapEditorCanvas.map;
    }

    public void setMap(RpgMap map) {
        mapEditorCanvas.map = map;
        updateCanvas(map);
    }

    public void resize(int left, int right, int top, int bottom) {
        RpgMap map = mapEditorCanvas.map;
        if (map != null) {
            if (map.resize(left, right, top, bottom)) {
                updateCanvas(map);
            }
        }
    }

    private void updateCanvas(RpgMap map) {
        mapEditorCanvas.tileImage = map.getMapImage();
        Rectangle rect = map.getMapImage().getBounds();
        canvasHolder.setMinSize(rect.width, rect.height);
        mapEditorCanvas.redraw();
        canvasHolder.redraw();
    }

    public static void main(String[] args) throws Exception {
        Display display = DisplayHelper.getDisplay();
        Shell shell = DisplayHelper.getShell();
        shell.setLayout(new GridLayout());
        MapEditor mapEditor = new MapEditor(shell);
        mapEditor.mapEditorCanvas.tileSelection = TileSelectionStub.getInstance();
        RpgMap rpgMap = RpgMap.newRpgMap();
        mapEditor.setMap(rpgMap);
        shell.setSize(600, 600);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        mapEditor.dispose();
        ImageHelper.dispose();
        display.dispose();
    }
}
