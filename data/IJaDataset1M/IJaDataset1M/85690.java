package eu.soa4all.wp6.composer.run.wp8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.soa4all.lpml.Activity;
import org.soa4all.lpml.AnnotationType;
import org.soa4all.lpml.Connector;
import org.soa4all.lpml.Flow;
import org.soa4all.lpml.Parameter;
import org.soa4all.lpml.Process;
import org.soa4all.lpml.SemanticAnnotation;
import org.soa4all.lpml.Binding;
import org.soa4all.lpml.impl.ActivityImpl;
import org.soa4all.lpml.impl.ConnectorImpl;
import org.soa4all.lpml.impl.FlowImpl;
import org.soa4all.lpml.impl.ParameterImpl;
import org.soa4all.lpml.impl.ProcessImpl;
import org.soa4all.lpml.impl.SemanticAnnotationImpl;
import org.soa4all.lpml.impl.BindingImpl;
import com.thoughtworks.xstream.XStream;
import eu.soa4all.wp6.composer.utils.ModelIOUtils;

public class ProcessGenerator {

    private String outputURL = System.getProperty("user.dir");

    private XStream xstream = new XStream();
}
