package artofillusion;

import artofillusion.object.*;
import artofillusion.texture.*;
import artofillusion.ui.*;
import buoy.event.*;
import buoy.widget.*;
import java.awt.*;

/** This dialog box allows the user to specify options for CSG objects. */
public class CSGDialog extends BDialog {

    private CSGObject theObject;

    private CSGModeller modeller;

    private Texture texture;

    private BComboBox opChoice;

    private BButton okButton, cancelButton;

    private ObjectPreviewCanvas preview;

    private int operation[];

    private boolean ok;

    private static int counter = 1;

    public CSGDialog(EditingWindow window, CSGObject obj) {
        super(window.getFrame(), true);
        theObject = obj;
        Scene scene = window.getScene();
        texture = scene.getDefaultTexture();
        BorderContainer content = new BorderContainer();
        setContent(content);
        RowContainer opRow = new RowContainer();
        content.add(opRow, BorderContainer.NORTH);
        opRow.add(new BLabel(Translate.text("Operation") + ":"));
        opRow.add(opChoice = new BComboBox());
        int i = 0;
        operation = new int[4];
        if (obj.getObject1().object.isClosed() && obj.getObject2().object.isClosed()) {
            opChoice.add(Translate.text("Union"));
            operation[i++] = CSGObject.UNION;
        }
        opChoice.add(Translate.text("Intersection"));
        operation[i++] = CSGObject.INTERSECTION;
        if (obj.getObject2().object.isClosed()) {
            opChoice.add(Translate.text("firstSecond"));
            operation[i++] = CSGObject.DIFFERENCE12;
        }
        if (obj.getObject1().object.isClosed()) {
            opChoice.add(Translate.text("secondFirst"));
            operation[i++] = CSGObject.DIFFERENCE21;
        }
        for (int j = 0; j < i; j++) if (obj.getOperation() == operation[j]) opChoice.setSelectedIndex(j);
        opChoice.addEventLink(ValueChangedEvent.class, this, "makePreview");
        content.add(preview = new ObjectPreviewCanvas(null), BorderContainer.CENTER);
        preview.setPreferredSize(new Dimension(200, 200));
        RowContainer buttons = new RowContainer();
        buttons.add(okButton = Translate.button("ok", this, "doOk"));
        buttons.add(cancelButton = Translate.button("cancel", this, "dispose"));
        content.add(buttons, BorderContainer.SOUTH, new LayoutInfo());
        makePreview();
        pack();
        UIUtilities.centerDialog(this, window.getFrame());
        setVisible(true);
    }

    private void doOk() {
        theObject.setOperation(operation[opChoice.getSelectedIndex()]);
        ok = true;
        dispose();
    }

    private void makePreview() {
        if (modeller == null) {
            double tol = ModellingApp.getPreferences().getInteractiveSurfaceError();
            TriangleMesh mesh1, mesh2;
            mesh1 = theObject.getObject1().object.convertToTriangleMesh(tol);
            mesh2 = theObject.getObject2().object.convertToTriangleMesh(tol);
            modeller = new CSGModeller(mesh1, mesh2, theObject.getObject1().coords, theObject.getObject2().coords);
        }
        TriangleMesh trimesh = modeller.getMesh(operation[opChoice.getSelectedIndex()]);
        trimesh.setTexture(texture, texture.getDefaultMapping());
        preview.setObject(trimesh);
        preview.updateImage();
        preview.repaint();
    }

    public boolean clickedOk() {
        return ok;
    }
}
