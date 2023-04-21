package net.sf.ij_plugins.vtk.prototype.surface;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import net.sf.ij_plugins.vtk.VTKException;
import net.sf.ij_plugins.vtk.utils.VTKImageDataFactory;
import net.sf.ij_plugins.vtk.utils.VTKLoader;
import net.sf.ij_plugins.vtk.utils.VTKUtil;
import vtk.vtkImageData;
import vtk.vtkOutputWindow;
import vtk.vtkStructuredPointsReader;
import javax.swing.*;
import java.io.IOException;

/**
 * Simple surface rendering.
 *
 * @author Jarek Sacha
 * @version $Revision: 1.8 $
 */
public class SurfaceRendringPlugin implements PlugIn {

    private static final String CAPTION = "VTK Surfrace Rendering Plugin";

    public void run(final String s) {
        try {
            VTKLoader.loadAll();
        } catch (VTKException ex) {
            ex.printStackTrace();
            VTKUtil.showErrorMessage(CAPTION, ex);
            return;
        }
        final ImagePlus imp = IJ.getImage();
        if (imp == null) {
            IJ.noImage();
            return;
        }
        if (imp.getType() == ImagePlus.COLOR_RGB) {
            IJ.showMessage(CAPTION, "RGB images are not supported.");
            return;
        }
        if (imp.getStackSize() < 2) {
            IJ.showMessage(CAPTION, "Cannot render images with less than two slices.");
            return;
        }
        vtkImageData inputImageData;
        try {
            inputImageData = VTKImageDataFactory.create(imp);
        } catch (IOException e) {
            IJ.showMessage(CAPTION, "Cannot render images with less than two slices.");
            return;
        }
        final SurfaceRenderingView view = new SurfaceRenderingView();
        view.setImageData(inputImageData);
        final JFrame frame = new JFrame(CAPTION);
        frame.getContentPane().add(view);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.pack();
                frame.setVisible(true);
                view.updatePipelineInNewThread();
            }
        });
    }

    public static void main(final String[] args) {
        final SurfaceRendringPlugin plugin = new SurfaceRendringPlugin();
        final vtk.vtkStructuredPointsReader reader = new vtkStructuredPointsReader();
        reader.SetFileName("dummy_name");
        vtkOutputWindow outputWindow = new vtkOutputWindow();
        System.out.println("OutputWindow: " + outputWindow);
        outputWindow = outputWindow.GetInstance();
        System.out.println("OutputWindow: " + outputWindow);
        outputWindow.DebugOn();
        System.out.println("Testing vtkOutputWindow");
        System.out.println("OutputWindow: " + outputWindow.GetInstance().getClass());
        outputWindow.DisplayErrorText("Standard Error Text\n");
        outputWindow.DisplayDebugText("Standard Debug Text\n");
        outputWindow.DisplayGenericWarningText("Standard GenericWarning Text\n");
        outputWindow.DisplayText("Standard Text\n");
        outputWindow.DisplayWarningText("Standard Warning Text\n");
        System.out.println("Testing MyOutputWindow");
        final MyOutputWindow myOutputWindow = new MyOutputWindow();
        outputWindow.SetInstance(myOutputWindow);
        System.out.println("OutputWindow: " + outputWindow.GetInstance().getClass());
        myOutputWindow.DisplayErrorText("My Error Text\n");
        myOutputWindow.DisplayDebugText("My Debug Text\n");
        myOutputWindow.DisplayGenericWarningText("My GenericWarning Text\n");
        myOutputWindow.DisplayText("My Text\n");
        myOutputWindow.DisplayWarningText("My Warning Text\n");
        reader.Update();
        System.out.println("Done");
    }

    public static class MyOutputWindow extends vtkOutputWindow {

        public void displayText(final String s) {
            System.out.println("TEXT>: " + s);
        }

        public void displayErrorText(final String s) {
            System.out.println("ERROR>: " + s);
        }
    }
}
