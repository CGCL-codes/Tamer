package net.sf.ij_plugins.itk.tests.IO;

import InsightToolkit.itkImageFileReaderUC2;
import InsightToolkit.itkImageFileReaderUC2_Pointer;
import InsightToolkit.itkImageFileWriterUC2;
import InsightToolkit.itkImageFileWriterUC2_Pointer;

/**
 * @author Jarek Sacha
 * @version $Revision: 1.1 $
 */
public class ReadWriteTest {

    public static void main(String[] args) {
        String inFile = "test_data/blobs.png";
        String outFile = "ReadWriteTest.png";
        itkImageFileReaderUC2_Pointer reader = itkImageFileReaderUC2.itkImageFileReaderUC2_New();
        itkImageFileWriterUC2_Pointer writer = itkImageFileWriterUC2.itkImageFileWriterUC2_New();
        reader.SetFileName(inFile);
        writer.SetFileName(outFile);
        writer.SetInput(reader.GetOutput());
        writer.Update();
    }
}
