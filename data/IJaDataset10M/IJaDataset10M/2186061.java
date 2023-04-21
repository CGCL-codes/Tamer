package iconsensus.vtkrenderer;

import java.io.File;
import java.io.IOException;
import vtk.*;

public class SeriesStackView implements POIListener {

    private vtkCanvas renderCanvas = null;

    private vtkImageActor imageActor = null;

    private vtkWindowLevelLookupTable windowLevelLut = null;

    private vtkImageShiftScale modalityTransformFilter = null;

    private vtkImageMapToColors windowLevelFilter = null;

    private vtkInteractorStyleImage iStyleImage = null;

    private vtkJPEGWriter jpegWriter = null;

    private int currentImageIndex = 0;

    private int window = 0;

    private int level = 0;

    private vtkAssembly rootAssembly = null;

    private boolean jpegExportEnabled = false;

    private String jpegExportDir = null;

    private SeriesModel model = null;

    public SeriesStackView(SeriesModel serModel) {
        model = serModel;
        renderCanvas = new vtkCanvas();
        imageActor = new vtkImageActor();
        modalityTransformFilter = new vtkImageShiftScale();
        windowLevelFilter = new vtkImageMapToColors();
        windowLevelLut = new vtkWindowLevelLookupTable();
        iStyleImage = new vtkInteractorStyleImage();
        jpegWriter = new vtkJPEGWriter();
        currentImageIndex = 0;
        window = 2000;
        level = 200;
        initializePipeline();
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    public void initializePipeline() {
        rootAssembly = new vtkAssembly();
        ImageModel imgModel = model.getImageModel(currentImageIndex);
        imgModel.addPOIListener(this);
        vtkRenderer renderer = renderCanvas.GetRenderer();
        renderer.SetBackground(0.0, 0.0, 0.0);
        rootAssembly.AddPart(imageActor);
        renderer.AddActor(rootAssembly);
        int wc = imgModel.getWindowCenter();
        int ww = imgModel.getWindowWidth();
        float rs = imgModel.getRescaleSlope();
        float ri = imgModel.getRescaleIntercept();
        if ((wc == ImageModel.WINDOW_CENTER_NOT_PROVIDED) && (ww == ImageModel.WINDOW_WIDTH_NOT_PROVIDED) && (rs == ImageModel.SLOPE_NOT_PROVIDED) && (ri == ImageModel.INTERCEPT_NOT_PROVIDED)) {
            imageActor.SetInput(imgModel.getImageData());
            return;
        }
        if ((wc != ImageModel.WINDOW_CENTER_NOT_PROVIDED) && (ww != ImageModel.WINDOW_WIDTH_NOT_PROVIDED)) {
            window = imgModel.getWindowWidth();
            level = imgModel.getWindowWidth();
        }
        if (rs != ImageModel.SLOPE_NOT_PROVIDED) {
            modalityTransformFilter.SetInput(imgModel.getImageData());
            float shift = imgModel.getRescaleIntercept() / imgModel.getRescaleSlope();
            modalityTransformFilter.SetShift(shift);
            modalityTransformFilter.SetScale(imgModel.getRescaleSlope());
            modalityTransformFilter.SetOutputScalarTypeToInt();
        }
        buildLinearWindowLevelLut(imgModel);
        if (rs != ImageModel.SLOPE_NOT_PROVIDED) {
            windowLevelFilter.SetInput(modalityTransformFilter.GetOutput());
        } else {
            windowLevelFilter.SetInput(imgModel.getImageData());
        }
        windowLevelFilter.SetLookupTable(windowLevelLut);
        windowLevelFilter.SetOutputFormatToLuminance();
        if (jpegExportEnabled) {
            jpegWriter.SetInput(windowLevelFilter.GetOutput());
            jpegWriter.SetFileName(jpegExportDir + imgModel.getImageInstanceUID() + ".jpg");
            jpegWriter.Write();
        }
        imageActor.SetInput(windowLevelFilter.GetOutput());
        renderCanvas.GetRenderWindow().GetInteractor().SetInteractorStyle(iStyleImage);
        renderCanvas.GetRenderWindow().GetInteractor().SetPicker(new vtkPointPicker());
        renderer.ResetCameraClippingRange();
        renderer.GetActiveCamera().ParallelProjectionOn();
        renderCanvas.GetRenderWindow().SetSize(renderCanvas.getWidth(), renderCanvas.getHeight());
        double[] imagePosition = imageActor.GetPosition();
        imageActor.SetPosition(imagePosition[0], imagePosition[1], 0.0);
    }

    private double getMinimimForWindowLevel(ImageModel imageModel) {
        double min = imageModel.getImageData().GetScalarTypeMin();
        if (modalityTransformFilter != null) {
            min = modalityTransformFilter.GetOutput().GetScalarTypeMin();
        }
        return min;
    }

    private double getMaximumForWindowLevel(ImageModel imageModel) {
        double max = imageModel.getImageData().GetScalarTypeMax();
        if (modalityTransformFilter != null) {
            max = modalityTransformFilter.GetOutput().GetScalarTypeMax();
        }
        return max;
    }

    public void enableJPEGExport(String directoryName) {
        jpegExportEnabled = true;
        jpegExportDir = directoryName;
    }

    public void disableJPEGExport() {
        jpegExportEnabled = false;
    }

    private void buildLinearWindowLevelLut(ImageModel imageModel) {
        windowLevelLut.SetRampToLinear();
        double min = getMinimimForWindowLevel(imageModel);
        double max = getMaximumForWindowLevel(imageModel);
        windowLevelLut.SetTableRange(min, max);
        windowLevelLut.SetWindow(1500);
        windowLevelLut.SetLevel(-500);
        windowLevelLut.Build();
    }

    public vtkCanvas getCanvas() {
        return renderCanvas;
    }

    public void showSlice(int index) {
        renderCanvas.GetRenderer().RemoveActor(rootAssembly);
        currentImageIndex = index;
        getCurrentImageModel().removePOIListener(this);
        initializePipeline();
        render();
    }

    private int getTotalNumberOfSlices() {
        return model.getTotalNumberOfSlices();
    }

    public void render() {
        if (renderCanvas != null) {
            renderCanvas.repaint();
        }
    }

    public void poiAdded(POIEvent poiEvent) {
        ImageModel model = getCurrentImageModel();
        double yOffset = model.getHeight() * model.getRowPixelSpacing();
        System.out.println("Point added is " + poiEvent.getPoint().x + ", " + poiEvent.getPoint().y + ", " + poiEvent.getPoint().z);
        vtkRegularPolygonSource circle = new vtkRegularPolygonSource();
        circle.SetRadius(10.0);
        circle.SetNormal(0.0, 0.0, 1.0);
        circle.SetNumberOfSides(100);
        vtkPolyDataMapper circleMapper = new vtkPolyDataMapper();
        circleMapper.SetInput(circle.GetOutput());
        vtkActor circleActor = new vtkActor();
        circleActor.SetMapper(circleMapper);
        circleActor.GetProperty().SetColor(1.0, 0.0, 0.0);
        circleActor.GetProperty().SetEdgeColor(1.0, 0.0, 0.0);
        circleActor.GetProperty().SetEdgeVisibility(10);
        circleActor.GetProperty().SetRepresentationToPoints();
        circleActor.GetProperty().SetPointSize(2);
        circleActor.SetPosition(poiEvent.getPoint().x, yOffset - poiEvent.getPoint().y, 0);
        rootAssembly.AddPart(circleActor);
    }

    public ImageModel getCurrentImageModel() {
        ImageModel imgModel = model.getImageModel(currentImageIndex);
        return imgModel;
    }
}
