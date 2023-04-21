package org.seasr.meandre.components.vis.html;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.annotations.Component.FiringPolicy;
import org.meandre.annotations.Component.Licenses;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextProperties;
import org.seasr.datatypes.core.BasicDataTypesTools;
import org.seasr.datatypes.core.DataTypeParser;
import org.seasr.datatypes.core.Names;
import org.seasr.meandre.components.abstracts.AbstractExecutableComponent;
import org.seasr.meandre.support.generic.encoding.Base64;

/**
 * @author Lily Dong
 * @author Boris Capitanu
 * @author Devin Griffiths
 */
@Component(creator = "Devin Griffiths", description = "Generates HTML frames based on the input data." + "The encoding of the data is specified via the " + Names.PROP_ENCODING + " property. " + "Supported MIME types: 'text/plain', 'image/<EXT>' (where <EXT> is one of the standard " + "image types; ex: jpg, png...)", firingPolicy = FiringPolicy.all, name = "HTML Frame Maker", rights = Licenses.UofINCSA, tags = "html", baseURL = "meandre://seasr.org/components/foundry/", dependency = { "protobuf-java-2.2.0.jar", "commons-lang-2.4.jar" })
public class HTMLFrameMaker extends AbstractExecutableComponent {

    @ComponentInput(name = Names.PORT_RAW_DATA, description = "Raw data encoded in one of the supported encoding types." + "<br>For text mime type:" + "<br>TYPE: java.lang.String" + "<br>TYPE: org.seasr.datatypes.BasicDataTypes.Strings" + "<br>TYPE: byte[]" + "<br>TYPE: org.seasr.datatypes.BasicDataTypes.Bytes" + "<br>TYPE: java.lang.Object" + "<br><br>For image mime type:" + "<br>TYPE: byte[]")
    protected static final String IN_RAW_DATA = Names.PORT_RAW_DATA;

    @ComponentOutput(name = Names.PORT_HTML, description = "The HTML fragment wrapping the input data." + "<br>TYPE: org.seasr.datatypes.BasicDataTypes.Strings")
    protected static final String OUT_HTML = Names.PORT_HTML;

    @ComponentProperty(defaultValue = "text/plain", description = "Specifies the MIME encoding of the input data.", name = Names.PROP_ENCODING)
    protected static final String PROP_ENCODING = Names.PROP_ENCODING;

    @ComponentProperty(defaultValue = "", description = "Specifies the ID attached to the HTML fragment.", name = Names.PROP_ID)
    protected static final String PROP_ID = Names.PROP_ID;

    @ComponentProperty(defaultValue = "", description = "Specifies a style attribute for the HTML fragment.", name = Names.PROP_CSS)
    protected static final String PROP_CSS = Names.PROP_CSS;

    @ComponentProperty(defaultValue = "1", description = "Specifies the number of frames -- max 4.", name = "frames")
    protected static final String PROP_FRAMES = "frames";

    @ComponentProperty(defaultValue = "vertical", description = "Specifies the orientation of the frames.", name = "orientation")
    protected static final String PROP_ORIENTATION = "orientation";

    private String _mimeType;

    private String _id;

    private String _css;

    private int _frames;

    private String _orientation;

    @Override
    public void initializeCallBack(ComponentContextProperties ccp) throws Exception {
        _mimeType = ccp.getProperty(PROP_ENCODING).toLowerCase();
        _id = ccp.getProperty(PROP_ID);
        if (_id.trim().length() == 0) _id = null;
        _css = ccp.getProperty(PROP_CSS);
        if (_css.trim().length() == 0) _css = null;
        _frames = Integer.parseInt(ccp.getProperty(PROP_FRAMES));
        _orientation = ccp.getProperty(PROP_ORIENTATION).toLowerCase();
    }

    List<Object> textobjects = new ArrayList<Object>();

    List<Object> imageobjects = new ArrayList<Object>();

    @Override
    public void executeCallBack(ComponentContext cc) throws Exception {
        if (_mimeType.startsWith("text")) {
            Object textdata = DataTypeParser.parseAsString(cc.getDataComponentFromInput(IN_RAW_DATA))[0];
            textobjects.add(textdata);
            if (textobjects.size() < _frames) return;
        }
        if (_mimeType.startsWith("text")) {
            String htmlTextFrames = org.seasr.meandre.support.generic.html.HTMLFrameMaker.makeHtmlTextFrame(textobjects, _orientation, _id, _css);
            console.fine("Pushing out text frames: " + htmlTextFrames);
            cc.pushDataComponentToOutput(OUT_HTML, BasicDataTypesTools.stringToStrings(htmlTextFrames));
        } else if (_mimeType.startsWith("image")) {
            byte[] imagedata = (byte[]) (cc.getDataComponentFromInput(IN_RAW_DATA));
            String imgBase64 = new String(Base64.encode(imagedata));
            imageobjects.add(imgBase64);
            if (imageobjects.size() < _frames) return;
            String htmlImageFrames = org.seasr.meandre.support.generic.html.HTMLFrameMaker.makeHtmlImageFrame(imageobjects, _orientation, _id, _css, _mimeType);
            console.fine("Pushing out image frames: " + htmlImageFrames);
            cc.pushDataComponentToOutput(OUT_HTML, BasicDataTypesTools.stringToStrings(htmlImageFrames));
        } else throw new UnsupportedEncodingException("Unknown MIME type specified: " + _mimeType);
    }

    @Override
    public void disposeCallBack(ComponentContextProperties ccp) throws Exception {
    }

    @Override
    public void handleStreamInitiators() throws Exception {
        super.handleStreamInitiators();
        textobjects.clear();
        imageobjects.clear();
    }

    @Override
    public void handleStreamTerminators() throws Exception {
        super.handleStreamTerminators();
        textobjects.clear();
        imageobjects.clear();
    }
}
