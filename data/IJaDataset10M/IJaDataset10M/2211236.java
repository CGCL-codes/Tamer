package edu.ucdavis.cs.dblp.data.viz;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import vtk.vtkActor;
import vtk.vtkConeSource;
import vtk.vtkPanel;
import vtk.vtkPolyDataMapper;

/**
 * *
 * 
 * @author Sebastien Jourdain / Artenum
 */
public class VTKSample {

    public static void main(String[] args) {
        vtkPanel panel3D = new vtkPanel();
        vtkConeSource coneSource = new vtkConeSource();
        coneSource.SetRadius(2);
        coneSource.SetAngle(15);
        coneSource.SetHeight(2);
        coneSource.SetResolution(10);
        vtkPolyDataMapper coneMapper = new vtkPolyDataMapper();
        coneMapper.SetInput(coneSource.GetOutput());
        vtkActor coneActor = new vtkActor();
        coneActor.SetMapper(coneMapper);
        panel3D.GetRenderer().AddActor(coneActor);
        JFrame frame = new JFrame("Artenum Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel3D, BorderLayout.CENTER);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
