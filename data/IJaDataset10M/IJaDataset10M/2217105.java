package com.tensegrity.palobrowser.actions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.subsets.SubsetHandler;
import com.tensegrity.palobrowser.cubequery.CubeQuery;
import com.tensegrity.palobrowser.cubequery.CubeQueryManager;
import com.tensegrity.palobrowser.subsetmanager.SubsetManager;
import com.tensegrity.palobrowser.util.RunQueue;
import com.tensegrity.palobrowser.wizards.addcube.AddCubeWizard;
import com.tensegrity.palobrowser.wizards.adddatabase.AddDatabaseWizard;
import com.tensegrity.palobrowser.wizards.adddimension.AddDimensionWizard;
import com.tensegrity.palobrowser.wizards.addelement.AddElementWizard;
import com.tensegrity.palobrowser.wizards.addsubset.AddSubsetWizard;
import com.tensegrity.palobrowser.wizards.addview.AddCubeQueryWizard;
import com.tensegrity.palobrowser.wizards.dimensionorcube.DimensionOrCubeWizard;

/**
 * <code>AddHelper</code>
 * <p>
 * A helper class to simplify the various add actions for the databases, 
 * dimensions, cubes and elements. 
 * </p>
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public class AddHelper {

    public static boolean OPENEDITOR_ON_ADD_DATABASE = true, OPENEDITOR_ON_ADD_DIMENSION = true, OPENEDITOR_ON_ADD_CUBE = true, OPENEDITOR_ON_ADD_SUBSET = true, OPENEDITOR_ON_ADD_CUBEQUERY = true;

    public static void addDatabase(IWorkbenchWindow window, Connection connection) {
        Set existingElements = new LinkedHashSet();
        for (int i = 0, n = connection.getDatabaseCount(); i < n; ++i) {
            Database db = connection.getDatabaseAt(i);
            if (db != null) existingElements.add(db.getName().toLowerCase());
        }
        RunQueue queue = new RunQueue();
        AddDatabaseWizard wizard = new AddDatabaseWizard(window, connection, existingElements, queue, true);
        wizard.init(window.getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
        dialog.create();
        if (Window.OK == dialog.open()) {
            runQueueSafely(window, queue);
        }
    }

    public static void addDimension(IWorkbenchWindow window, Database database) {
        Set existingElements = new LinkedHashSet();
        for (int i = 0, n = database.getDimensionCount(); i < n; ++i) {
            Dimension dimension = database.getDimensionAt(i);
            if (dimension != null) existingElements.add(dimension.getName().toLowerCase());
        }
        RunQueue queue = new RunQueue();
        AddDimensionWizard wizard = new AddDimensionWizard(window, database, existingElements, queue, true);
        wizard.init(window.getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
        dialog.create();
        if (Window.OK == dialog.open()) {
            runQueueSafely(window, queue);
        }
    }

    public static void addCube(IWorkbenchWindow window, Database database) {
        Set existingElements = new LinkedHashSet();
        for (int i = 0, n = database.getCubeCount(); i < n; ++i) {
            Cube cube = database.getCubeAt(i);
            if (cube != null) existingElements.add(cube.getName().toLowerCase());
        }
        RunQueue queue = new RunQueue();
        AddCubeWizard wizard = new AddCubeWizard(window, database, existingElements, queue, true);
        wizard.init(window.getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
        dialog.create();
        if (Window.OK == dialog.open()) {
            runQueueSafely(window, queue);
        }
    }

    public static void addElement(IWorkbenchWindow window, Dimension dimension) {
        Set existingElements = new LinkedHashSet();
        for (int i = 0, n = dimension.getElementCount(); i < n; ++i) {
            Element element = dimension.getElementAt(i);
            if (element != null) existingElements.add(element.getName().toLowerCase());
        }
        RunQueue queue = new RunQueue();
        AddElementWizard wizard = new AddElementWizard(dimension, existingElements, queue);
        wizard.init(window.getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
        dialog.create();
        if (Window.OK == dialog.open()) {
            runQueueSafely(window, queue);
        }
    }

    public static void addCubeOrDimension(IWorkbenchWindow window, Database database) {
        RunQueue queue = new RunQueue();
        DimensionOrCubeWizard wizard = new DimensionOrCubeWizard(window, database, queue);
        wizard.init(window.getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(window.getShell(), wizard) {

            public void create() {
                super.create();
                Button b = getButton(IDialogConstants.FINISH_ID);
                if (b == null) return;
                b.setText(IDialogConstants.NEXT_LABEL);
            }
        };
        dialog.create();
        if (Window.OK == dialog.open()) {
            runQueueSafely(window, queue);
        }
    }

    public static void addSubset2(IWorkbenchWindow window, Dimension dimension) {
        dimension = SubsetManager.getLeafDimension(dimension);
        SubsetHandler subsetHandler = dimension.getSubsetHandler();
        Set existingSubsetNames = new HashSet(Arrays.asList(subsetHandler.getSubsetNames()));
        RunQueue queue = new RunQueue();
        AddSubsetWizard wizard = new AddSubsetWizard(window, dimension, existingSubsetNames, queue, true);
        wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        dialog.create();
        if (Window.OK == dialog.open()) {
            runQueueSafely(window, queue);
        }
    }

    public static void addCubeQuery(final IWorkbenchWindow window, final Cube cube) {
        CubeQuery queries[] = CubeQueryManager.getInstance().findCubeQueries(cube.getDatabase(), cube.getId());
        Set existingCubeQueryNames = new LinkedHashSet();
        for (int i = 0; i < queries.length; ++i) {
            existingCubeQueryNames.add(queries[i].getName().toLowerCase());
        }
        RunQueue queue = new RunQueue();
        AddCubeQueryWizard wizard = new AddCubeQueryWizard(window, cube, existingCubeQueryNames, queue, true);
        wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        dialog.create();
        if (Window.OK == dialog.open()) {
            try {
                queue.run();
            } catch (RuntimeException e) {
                System.err.println("error when running queue: " + e.getMessage());
            }
        }
    }

    private static void runQueueSafely(final IWorkbenchWindow window, final RunQueue queue) {
        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    queue.run();
                } catch (RuntimeException e) {
                    MessageDialog.openError(window.getShell(), ActionsMessages.getString("AddHelper.ErrorDlgTitle"), e.getMessage());
                }
            }
        };
        BusyIndicator.showWhile(window.getShell().getDisplay(), runnable);
    }
}
