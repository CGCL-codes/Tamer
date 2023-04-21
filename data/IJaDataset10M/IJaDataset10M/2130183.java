package net.disy.ogc.wpspd.v_1_0_0.sample;

import net.disy.ogc.wps.v_1_0_0.annotation.Metadata;
import net.disy.ogc.wps.v_1_0_0.annotation.OutputParameter;
import net.disy.ogc.wps.v_1_0_0.annotation.Process;
import net.disy.ogc.wps.v_1_0_0.annotation.ProcessMethod;
import net.disy.ogc.wpspd.v_1_0_0.Geometry;

@Process(id = "sampleSymbolizedPoint", title = "Symbolized point", description = "Returns a symbolized point", metadata = { @Metadata(about = "tag", href = "wps-demo:ObjectVisualizer") })
public class SampleSymbolizedPointAnnotatedObject extends AbstractSampleParameterlessAnnotatedObject<Geometry> {

    public SampleSymbolizedPointAnnotatedObject() {
        super(Geometry.class, "SampleSymbolizedPoint.xml");
    }

    @Override
    @ProcessMethod
    @OutputParameter(id = "geometry", title = "Resulting geometry", description = "Resulting geometry")
    public Geometry execute() {
        return super.execute();
    }
}
