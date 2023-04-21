package edu.gsbme.yakitori.Renderer.Loader;

import javax.media.j3d.TransformGroup;
import edu.gsbme.geometrykernel.data.Idata;
import edu.gsbme.geometrykernel.data.BaseModel.ListModel;
import edu.gsbme.yakitori.Algorithm.ScalingController;
import edu.gsbme.yakitori.Exception.IncorrectCellException;
import edu.gsbme.yakitori.Exception.IncorrectOptionException;
import edu.gsbme.yakitori.Exception.IncorrectRenderClassException;
import edu.gsbme.yakitori.Renderer.Renderer;
import edu.gsbme.yakitori.Renderer.Options.DefaultLoaderOptions;
import edu.gsbme.yakitori.Renderer.Options.DefaultLoaderOptions.LoaderOperation;
import edu.gsbme.yakitori.Renderer.Pipeline.FMLPipeline;
import edu.gsbme.yakitori.Renderer.Pipeline.FMLPipelineOptions;

/**
 * Object loader
 * @author David
 *
 */
public class ObjectLoader extends ILoader {

    private Renderer renderer;

    /**
	 * @param model (brep) object geometry model
	 * @param displaySet
	 */
    public ObjectLoader(ListModel model, Renderer renderer) {
        super(model);
        this.renderer = renderer;
    }

    @Override
    public TransformGroup load() {
        TransformGroup tg = new TransformGroup();
        FMLPipelineOptions pipeline_options = new FMLPipelineOptions();
        pipeline_options.setRenderer(renderer);
        pipeline_options.setReferenceLib(renderer.returnRenderController().getRenderReferenceLib());
        pipeline_options.setTransformScale(ScalingController.getScalingFactor(metadata.getMax()));
        pipeline_options.setDisplayConfig(renderer.getDisplayOptions());
        pipeline_options.setParentBranch(tg);
        pipeline_options.setDisplayMetaData(metadata);
        FMLPipeline fml_pipeline = new FMLPipeline();
        DefaultLoaderOptions loaderOptions = (DefaultLoaderOptions) renderer.getLoaderOptions();
        if (loaderOptions.getLoaderOperation() == LoaderOperation.CLEAR_ADD) {
            renderer.returnRenderController().getRenderReferenceLib().clear();
        }
        if (loaderOptions.renderDim0Cells()) {
            if (loaderOptions.getCL_RENDER_Dim0_All()) {
                for (int i = 0; i < model.getDim0ArraySize(); i++) {
                    Idata pt = model.getDim0Object(i);
                    try {
                        fml_pipeline.Render(pipeline_options, pt.getID(), pt);
                    } catch (IncorrectCellException e) {
                        e.printStackTrace();
                    } catch (IncorrectOptionException e) {
                        e.printStackTrace();
                    } catch (IncorrectRenderClassException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < loaderOptions.get_CL_Dim0ArraySize(); i++) {
                    Idata pt = model.getDim0Object(loaderOptions.get_CL_Dim0ArrayIndex(i));
                    try {
                        fml_pipeline.Render(pipeline_options, pt.getID(), pt);
                    } catch (IncorrectCellException e) {
                        e.printStackTrace();
                    } catch (IncorrectOptionException e) {
                        e.printStackTrace();
                    } catch (IncorrectRenderClassException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (loaderOptions.renderDim1Cells()) {
            if (loaderOptions.getCL_RENDER_Dim1_All()) {
                for (int i = 0; i < model.getDim1ArraySize(); i++) {
                    Idata data = model.getDim1Object(i);
                    try {
                        fml_pipeline.Render(pipeline_options, data.getID(), data);
                    } catch (IncorrectCellException e) {
                        e.printStackTrace();
                    } catch (IncorrectOptionException e) {
                        e.printStackTrace();
                    } catch (IncorrectRenderClassException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < loaderOptions.get_CL_Dim1ArraySize(); i++) {
                    Idata data = model.getDim1Object(loaderOptions.get_CL_Dim1ArrayIndex(i));
                    try {
                        fml_pipeline.Render(pipeline_options, data.getID(), data);
                    } catch (IncorrectCellException e) {
                        e.printStackTrace();
                    } catch (IncorrectOptionException e) {
                        e.printStackTrace();
                    } catch (IncorrectRenderClassException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (loaderOptions.renderDim2Cells()) {
            if (loaderOptions.getCL_RENDER_Dim2_All()) {
                for (int i = 0; i < model.getDim2ArraySize(); i++) {
                    Idata data = model.getDim2Object(i);
                    try {
                        fml_pipeline.Render(pipeline_options, data.getID(), data);
                    } catch (IncorrectCellException e) {
                        e.printStackTrace();
                    } catch (IncorrectOptionException e) {
                        e.printStackTrace();
                    } catch (IncorrectRenderClassException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < loaderOptions.get_CL_Dim2ArraySize(); i++) {
                    Idata data = model.getDim2Object(loaderOptions.get_CL_Dim2ArrayIndex(i));
                    try {
                        fml_pipeline.Render(pipeline_options, data.getID(), data);
                    } catch (IncorrectCellException e) {
                        e.printStackTrace();
                    } catch (IncorrectOptionException e) {
                        e.printStackTrace();
                    } catch (IncorrectRenderClassException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (loaderOptions.renderDim3Cells()) {
            if (loaderOptions.getCL_RENDER_Dim3_All()) {
                for (int i = 0; i < model.getDim3ArraySize(); i++) {
                    Idata data = model.getDim3Object(i);
                    try {
                        fml_pipeline.Render(pipeline_options, data.getID(), data);
                    } catch (IncorrectCellException e) {
                        e.printStackTrace();
                    } catch (IncorrectOptionException e) {
                        e.printStackTrace();
                    } catch (IncorrectRenderClassException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < loaderOptions.get_CL_Dim3ArraySize(); i++) {
                    Idata data = model.getDim3Object(loaderOptions.get_CL_Dim3ArrayIndex(i));
                    try {
                        fml_pipeline.Render(pipeline_options, data.getID(), data);
                    } catch (IncorrectCellException e) {
                        e.printStackTrace();
                    } catch (IncorrectOptionException e) {
                        e.printStackTrace();
                    } catch (IncorrectRenderClassException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return tg;
    }
}
