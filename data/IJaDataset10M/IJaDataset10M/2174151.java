package er.directtoweb.components.strings;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import er.directtoweb.components.ERDCustomEditComponent;
import er.extensions.appserver.ERXResponseRewriter;
import er.extensions.foundation.ERXProperties;

/**
 * Very, very basic version of a TinyMCE integration. As it doesn't make much sense to load the the JS files
 * it's excepted that you put them somewhere on your server and specify the location with
 * the property <code>er.directtoweb.ERDEditHTML.tinyMceSourceUrl=http://somewhere/.../tiny_mce.js</code>.<br />
 * The default is the TinyMce server which would be very unfriendly and slow to use in deployment...
 * <br /><br />
 * You can also use <code>er.directtoweb.ERDEditHTML.tinyMceSourceFileName</code> and 
 * <code>er.directtoweb.ERDEditHTML.tinyMceSourceFrameworkName</code> properties to specify file name and framework name ("app" by default)
 * of TinyMCE if you want to store the files in WebServerResources of your application of framework. For example:
 * <pre>
 * er.directtoweb.ERDEditHTML.tinyMceSourceFileName = tiny_mce/tiny_mce.js
 * </pre>
 * 
 * @author ak
 *
 */
public class ERDEditHTML extends ERDCustomEditComponent {

    public static final String DEFAULT_URL = "http://tinymce.moxiecode.com/tinymce/jscripts/tiny_mce/tiny_mce.js";

    public static final String SOURCE_URL_PROPERTY = "er.directtoweb.ERDEditHTML.tinyMceSourceUrl";

    public static final String FRAMEWORK_NAME_PROPERTY = "er.directtoweb.ERDEditHTML.tinyMceSourceFrameworkName";

    public static final String FILE_NAME_PROPERTY = "er.directtoweb.ERDEditHTML.tinyMceSourceFileName";

    public ERDEditHTML(WOContext context) {
        super(context);
    }

    public boolean isStateless() {
        return true;
    }

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }

    @Override
    public void appendToResponse(WOResponse response, WOContext context) {
        super.appendToResponse(response, context);
        String url = ERXProperties.stringForKeyWithDefault(SOURCE_URL_PROPERTY, DEFAULT_URL);
        String fileName = ERXProperties.stringForKeyWithDefault(FILE_NAME_PROPERTY, url);
        String framework = ERXProperties.stringForKeyWithDefault(FRAMEWORK_NAME_PROPERTY, "app");
        ERXResponseRewriter.addScriptResourceInHead(response, context, framework, fileName);
        ERXResponseRewriter.addScriptCodeInHead(response, context, String.format("tinyMCE.init({%s});", richTextMode()), "tinyMCEInit");
    }

    private String richTextMode() {
        return stringValueForBinding("richTextMode");
    }
}
